package parts.memory;

import java.util.ArrayList;

import com.hit.algorithm.IAlgoCache;
import parts.dm.DataModel;

public class CacheUnit<T>
        extends Object {

    public IAlgoCache<Long, DataModel<T>> algo;
    public ArrayList<Long> cacheIds;

    public CacheUnit(IAlgoCache<Long,DataModel<T>> algo) {

        this.algo = algo;
        cacheIds = new ArrayList<>();
    }

    public DataModel<T>[] getDataModels(Long[] ids) {

        @SuppressWarnings("unchecked")
        DataModel <T> []listOfDataModels = (DataModel <T> []) new DataModel<?>[ids.length];
        int i = 0;
        for (Long id : ids) {

            if (algo.getElement(id) == null)
                System.out.println("DataModel ID: " + id + " Not found in Cache");
            else {
                listOfDataModels[i] = algo.getElement(id);
                System.out.println("DataModel ID: " + id + " Found in Cache");
            }
            i++;
        }
        return listOfDataModels;
    }

    public DataModel<T>[] putDataModels(DataModel<T>[] datamodels) {

        @SuppressWarnings("unchecked")
        DataModel <T> []listOfDataModels = (DataModel <T> []) new DataModel<?>[datamodels.length];
        DataModel<T> dataModelReturned;
        int i = 0;
        for (DataModel<T> dModel : datamodels) {

            dataModelReturned = algo.putElement(dModel.getDataModelId(), dModel);
            System.out.println("DataModel ID: " + dModel.getDataModelId() + " Added to Cache");
            if (dataModelReturned != null) {
                listOfDataModels[i++] = dataModelReturned;
                cacheIds.remove(dataModelReturned.getDataModelId());
            }
            if (cacheIds.contains(dModel.getDataModelId()) == false)
                cacheIds.add(dModel.getDataModelId());
        }
        if (i == 0)
            listOfDataModels = null;
        else {
            DataModel <T> []smallerArray = (DataModel<T>[]) new DataModel<?>[i];
            for (int j=0; j<i; j++)
                smallerArray[j] = listOfDataModels[j];
            return smallerArray;
        }
        return listOfDataModels;
    }

    public void removeDataModels(Long[] ids) {

        for (Long id : ids) {
            try {
                algo.removeElement(id);
                if (cacheIds.remove(id))
                    System.out.println("DataModel ID: " + id + " Removed from Cache");
                else
                    System.out.println("DataModel ID: " + id + " Is not found in Cache");
            }catch (Exception e) {
                System.out.println("DataModel ID: " + id + " Is not found in Cache");
                continue;
            }
        }
    }


}
