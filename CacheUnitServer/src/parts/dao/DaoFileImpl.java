package parts.dao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import parts.dm.DataModel;

public class DaoFileImpl<T>
        extends Object
        implements IDao<Long, DataModel<T>> {

    public String filePath;
    public Map <Long,DataModel<T>> memo;
    public int capacity;
    public ObjectInputStream ois;
    public ObjectOutputStream oos;

    public DaoFileImpl(String filePath) {
        this.filePath = filePath;
        this.capacity = 100000;
        this.memo = new HashMap<>(capacity);
    }

    public DaoFileImpl(String filePath, int capacity)  {
        this.filePath = filePath;
        this.capacity = capacity;
        this.memo = new HashMap<>(capacity);

    }

    @SuppressWarnings("unchecked")
    @Override
    public void delete(DataModel<T> entity) {


        // Read map from file
        try {
            ois = new ObjectInputStream(new FileInputStream(filePath));
            memo = (HashMap<Long, DataModel<T>>) ois.readObject();
            memo.remove(entity.getDataModelId());

            System.out.println("DataModel ID: " + entity.getDataModelId() + " Removed from File");
        }
        catch (FileNotFoundException e) {
            System.out.println("Dao - DELETE - READ - File not found");
        }
        catch (IOException e) {
            System.out.println("Dao - DELETE - READ - Error init stream");
        }
        catch (ClassNotFoundException e) {
            System.out.println("Dao - DELETE - READ - Class not found");
        }

        // Write map on file
        try {
            oos = new ObjectOutputStream(new FileOutputStream(filePath));
            oos.writeObject(memo);
            oos.close();
        }
        catch (FileNotFoundException e) {
            System.out.println("Dao - DELETE - WRITE - File not found");
        }
        catch (IOException e) {
            System.out.println("Dao - DELETE - WRITE - Error init stream");
        }

    }

    @SuppressWarnings({ "unchecked" })
    @Override
    public DataModel<T> find(Long id) {


        // Read map from file
        try {
            ois = new ObjectInputStream(new FileInputStream(filePath));
            memo = (HashMap<Long, DataModel<T>>) ois.readObject();

            if (memo.containsKey(id)) {
                System.out.println("DataModel ID: " + id +  " Found in File");
                return memo.get(id);
            }
            else
                System.out.println("DataModel ID: " + id +  " Not found in File"); {
                return null;
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("Dao - FIND - File not found");
            return null;
        }
        catch (IOException e) {
            System.out.println("Dao - FIND - File is empty");
            return null;
        }
        catch (ClassNotFoundException e) {
            System.out.println("Dao - FIND - Class not found");
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void save(DataModel<T> entity) {

        // Read map from file
        try {
            ois = new ObjectInputStream(new FileInputStream(filePath));
            memo = (HashMap<Long, DataModel<T>>) ois.readObject();

            if (memo.containsKey(entity.getDataModelId()) == false) {
                memo.put(entity.getDataModelId(), entity);
                System.out.println("DataModel ID: " + entity.getDataModelId() +  " Added to File");
            }
            else
                System.out.println("DataModel ID: " + entity.getDataModelId() +  " Found in File");

            oos = new ObjectOutputStream(new FileOutputStream(filePath));
            oos.writeObject(memo);
            oos.close();

        }
        catch (FileNotFoundException e) {
            System.out.println("Dao - SAVE - READ - File not found");
            e.printStackTrace();
        }
        catch (IOException e) {
            System.out.println("Empty file...initializing");
            // Write map on file
            try {
                memo.put(entity.getDataModelId(), entity);
                oos = new ObjectOutputStream(new FileOutputStream(filePath));
                oos.writeObject(memo);
                oos.close();

                System.out.println("DataModel ID: " + entity.getDataModelId() +  " Added to File");
            }
            catch (FileNotFoundException ex) {
                System.out.println("Dao - SAVE - WRITE - not found");
                ex.printStackTrace();
            }
            catch (IOException ex) {
                System.out.println("Error - Dao - SAVE - WRITE - IOEx");
                ex.printStackTrace();
            }

        } catch (ClassNotFoundException e) {
            System.out.println("Dao - save - READ - Class not found");
            e.printStackTrace();
        }


    }
}
