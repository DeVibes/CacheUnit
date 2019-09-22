package parts.services;

import com.hit.algorithm.LRUAlgoCacheImpl;
import com.hit.algorithm.NRUAlgoCacheImpl;
import com.hit.algorithm.RandomAlgoCacheImpl;
import parts.dao.DaoFileImpl;
import parts.dao.IDao;
import parts.dm.DataModel;
import parts.memory.CacheUnit;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CacheUnitService<T>
        extends java.lang.Object {

    private final String ABS_PATH = "/home/mark/dev/CacheUnit/CacheUnitClient/src/resources/database.txt";

    public IDao<Long, DataModel<T>> iDao;
    public CacheUnit<T> cacheUnit;
    public enum Algorithm
    {
        LRU, NRU, RANDOM;

        public String toString(){
            switch(this){
                case LRU :
                    return "LRU";
                case NRU :
                    return "NRU";
                case RANDOM :
                    return "RANDOM";
            }
            return null;
        }
    }
    public Algorithm currentAlgorithm;
    public int capacity;

    public ArrayList<Long> daoIds;
    Long[] idsArray;
    int pivot, checker;
    int requestCounter, swapCounter;
    @SuppressWarnings({ "unchecked", "resource" })
    public CacheUnitService() {

        iDao = new DaoFileImpl<>(ABS_PATH);
        cacheUnit = new CacheUnit<>(new LRUAlgoCacheImpl<>(3));
        currentAlgorithm = Algorithm.LRU;
        requestCounter = 0;
        swapCounter = 0;
        capacity = 3;
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
                    ABS_PATH));
            Map<Long,DataModel<T>> map = (HashMap<Long, DataModel<T>>) ois.readObject();
            daoIds = new ArrayList<Long>(map.keySet());
        }
        catch (Exception e){
            daoIds = new ArrayList<Long>();
        }
    }

    @SuppressWarnings("finally")
    public boolean delete(DataModel<T>[] dataModels) {

        ArrayList<Long> deletedIds = new ArrayList<>();
        requestCounter++;
        // removing from iDao
        try {
            for (DataModel<T> dm : dataModels) {
                if (iDao.find(dm.getDataModelId()) != null) {

                    iDao.delete(dm);
                    daoIds.remove(dm.getDataModelId());
                }
                else
                    deletedIds.add(dm.getDataModelId());
            }
        }
        catch (Exception e) {
            System.out.println("Delete error in CacheUnitService - Dao\n");

            System.out.println("Current memory:\nCache");
            for (Long id : cacheUnit.cacheIds)
                System.out.println(id + " ");
            System.out.println("Dao:");
            for (Long id : daoIds)
                System.out.println(id + " ");

            return false;
        }

        // removing from Cache
        finally {
            try {
                idsArray = new Long[dataModels.length];
                pivot = 0;
                for (DataModel<T> dm : dataModels) {
                    idsArray[pivot] = dm.getDataModelId();

                    if (cacheUnit.cacheIds.contains(idsArray[pivot]))
                        deletedIds.remove(idsArray[pivot]);

                    pivot++;
                }
                cacheUnit.removeDataModels(idsArray);
                for (Long id : idsArray) {
                    if (cacheUnit.cacheIds.contains(id))
                        cacheUnit.cacheIds.remove(id);
                }
            }
            catch (Exception e) {
                System.out.println("Delete error in CacheUnitService - Cache");
                return false;
            }
            finally {
                System.out.println("Current memory:\nCache:");
                for (Long id : cacheUnit.cacheIds)
                    System.out.println(id + " ");
                System.out.println("Dao:");
                for (Long id : daoIds)
                    System.out.println(id + " ");

                if (deletedIds.isEmpty())
                    return true;
                return false;
            }
        }

    }

    @SuppressWarnings("unchecked")
    public DataModel<T>[] get(DataModel<T>[] dataModels) {
        requestCounter++;
        pivot = 0; checker = 0;
        DataModel <T> [] newDataList = (DataModel<T>[]) new DataModel<?>[dataModels.length];
        idsArray = new Long[dataModels.length];
        DataModel<T> dataModel;

        for (DataModel<T> dm : dataModels) {

            idsArray[pivot] = dm.getDataModelId();
            pivot++;
        }
        newDataList = cacheUnit.getDataModels(idsArray);

        pivot = 0;

        for (DataModel<T> dm : newDataList) {
            if (dm != null)
                pivot++;
            else {
                dataModel = iDao.find(idsArray[checker]);
                if (dataModel != null) {
                    newDataList[pivot] = dataModel;
                }
            }
            checker++;
        }
        if (pivot == 0)
            newDataList = null;
        else {
            DataModel <T> []smallerArray = (DataModel<T>[]) new DataModel<?>[pivot];
            for (int j=0; j<pivot; j++)
                smallerArray[j] = newDataList[j];

            System.out.println("Current memory:\nCache:");
            for (Long id : cacheUnit.cacheIds)
                System.out.println(id + " ");
            System.out.println("Dao:");
            for (Long id : daoIds)
                System.out.println(id + " ");

            return smallerArray;
        }

        System.out.println("Current memory:\nCache:");
        for (Long id : cacheUnit.cacheIds)
            System.out.println(id + " ");
        System.out.println("Dao:");
        for (Long id : daoIds)
            System.out.println(id + " ");
        return newDataList;
    }

    @SuppressWarnings("finally")
    public boolean update(DataModel<T>[] dataModels) {
        requestCounter++;
        DataModel<T>[] leftovers = null;
        try {
            leftovers = cacheUnit.putDataModels(dataModels);
        }
        catch (Exception e) {
            System.out.println("Update error in CacheUnitService");

            System.out.println("Current memory:\nCache:");
            for (Long id : cacheUnit.cacheIds)
                System.out.println(id + " ");
            System.out.println("Dao:\n");
            for (Long id : daoIds)
                System.out.println(id + " ");
            return false;
        }
        finally {
            if (leftovers != null) {
                try {
                    for (DataModel<T> dm : leftovers) {
                        if (daoIds.contains(dm.getDataModelId()) == false) {
                            swapCounter++;
                            iDao.save(dm);
                            daoIds.add(dm.getDataModelId());
                        }
                    }
                }
                catch (Exception e) {
                    System.out.println("Update error in CacheUnitService");

                    System.out.println("Current memory:\nCache:");
                    for (Long id : cacheUnit.cacheIds)
                        System.out.println(id + " ");
                    System.out.println("Dao:");
                    for (Long id : daoIds)
                        System.out.println(id + " ");
                    return false;
                }
            }
            System.out.println("Current memory:\nCache:");
            for (Long id : cacheUnit.cacheIds)
                System.out.println(id + " ");
            System.out.println("Dao:");
            for (Long id : daoIds)
                System.out.println(id + " ");
            return true;
        }
    }

    public String getStats() {

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(currentAlgorithm);
        stringBuilder.append(",");
        stringBuilder.append(String.valueOf(capacity));
        stringBuilder.append(",");
        stringBuilder.append(String.valueOf(requestCounter));
        stringBuilder.append(",");
        stringBuilder.append(String.valueOf(cacheUnit.cacheIds.size() + daoIds.size()));
        stringBuilder.append(",");
        stringBuilder.append(String.valueOf(swapCounter));

        String finalString = stringBuilder.toString();
        return finalString;
    }

    public void changeCacheAlgorithm(String newAlgo) {

        Scanner scanner = new Scanner(newAlgo);
        scanner.useDelimiter(",");

        String cacheAlgo = scanner.next();
        int newCapacity = scanner.nextInt();

        if (!cacheAlgo.equals(currentAlgorithm.toString()) || capacity != newCapacity) {

            switch (cacheAlgo) {

                case "LRU":
                    cacheUnit = new CacheUnit<>(new LRUAlgoCacheImpl<>(newCapacity));
                    currentAlgorithm = Algorithm.LRU;
                    break;
                case "NRU":
                    cacheUnit = new CacheUnit<>(new NRUAlgoCacheImpl<>(newCapacity));
                    currentAlgorithm = Algorithm.NRU;
                    break;
                case "RANDOM":
                    cacheUnit = new CacheUnit<>(new RandomAlgoCacheImpl<>(newCapacity));
                    currentAlgorithm = Algorithm.RANDOM;
                    break;
            }

            capacity = newCapacity;
        }
    }
}
