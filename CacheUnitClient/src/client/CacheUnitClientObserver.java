package client;

import view.CacheUnitClientView;

import java.beans.PropertyChangeEvent;

public class CacheUnitClientObserver
        extends java.lang.Object
        implements java.beans.PropertyChangeListener {

    int index;
    String fileName;
    CacheUnitClient cacheUnitClient;

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        cacheUnitClient = new CacheUnitClient();

        if (evt.getPropertyName().equals("FilePath"))

            cacheUnitClient.send(evt.getNewValue().toString());

        else if (evt.getPropertyName().equals("Stats")) {

            String respose = cacheUnitClient.send(evt.getPropertyName());

            if (respose != null) {

                ((CacheUnitClientView) evt.getSource()).updateUIData(respose);
            }

        }
        else {

            cacheUnitClient.send(evt.getNewValue().toString());
        }

    }

}
