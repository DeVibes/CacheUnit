package parts.services;

import parts.dm.DataModel;

public class CacheUnitController<T>
        extends java.lang.Object {

    public CacheUnitService<T> cacheUnitService;

    public CacheUnitController()  {cacheUnitService = new CacheUnitService<T>();}

    public boolean delete(DataModel<T>[] dataModels) {return cacheUnitService.delete(dataModels);}

    public DataModel<T>[] get(DataModel<T>[] dataModels) {return cacheUnitService.get(dataModels);}

    public boolean update(DataModel<T>[] dataModels) {return cacheUnitService.update(dataModels);}

    public String getStats() {return cacheUnitService.getStats();}

    public void changeCacheAlgorithm(String newAlgo) {cacheUnitService.changeCacheAlgorithm(newAlgo);}
}
