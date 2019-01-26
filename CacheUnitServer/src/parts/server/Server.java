package parts.server;

import parts.services.CacheUnitController;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server
        extends java.lang.Object
        implements java.beans.PropertyChangeListener, java.lang.Runnable {

    public ServerSocket serverSocket;
    public Boolean isAlive = false;
    private Socket socket;
    CacheUnitController<String> cacheUnitController;

    public Server() {

        cacheUnitController = new CacheUnitController<>();
    }
    @SuppressWarnings("deprecation")
    @Override
    public void run() {

        System.out.println("run In Thread - " + Thread.currentThread().getName());
        System.out.println("------Server-------\nServer started.....\n");

        System.out.println("Current memory:\nCache:");
        for (Long id : cacheUnitController.cacheUnitService.cacheUnit.cacheIds)
            System.out.println(id + " ");
        System.out.println("Dao:");
        for (Long id : cacheUnitController.cacheUnitService.daoIds)
            System.out.println(id + " ");

        System.out.println("waiting for connection....\n");

        try  {
            initServer();
            while (isAlive == true) {

                socket = serverSocket.accept();
                System.out.println("Client connected......");
                new Thread(new HandleRequest<String>(socket,cacheUnitController)).start();

            }
        } catch (Exception e) {
            e.getClass().getSimpleName();
            System.out.println("Server offline");
            Thread.currentThread().stop();
        }
    }

    private void initServer() throws IOException {
        serverSocket = new ServerSocket(12345);
        isAlive = true;

    }
    public void stopServer() {

        System.out.println("stop server In Thread - " + Thread.currentThread().getName());

        try {
            System.out.println("Server shutdown......");
            this.serverSocket.close();
            this.isAlive = false;

        } catch (Exception e)  {
            System.out.println("Exception - "+e.getClass().getSimpleName()+" in Stop server!");
        }

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        System.out.println("Prop change In Thread - " + Thread.currentThread().getName());

        Object commandFromCli = evt.getNewValue();
        if (commandFromCli.toString().equals("start") && this.isAlive == false)
            new Thread(this).start();

        if (commandFromCli.toString().equals("shutdown") && this.isAlive == true)
            stopServer();
    }


}
