package driver;

import client.CacheUnitClientObserver;
import view.CacheUnitClientView;

public class CacheUnitClientDriver
        extends java.lang.Object {

    public static void main(String[] args) {
        CacheUnitClientObserver cacheUnitClientObserver = new CacheUnitClientObserver();
        CacheUnitClientView view = new CacheUnitClientView();
        view.addPropertyChangeListener(cacheUnitClientObserver);
        view.start();
    }
}
