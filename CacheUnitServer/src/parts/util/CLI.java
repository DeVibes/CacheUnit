package parts.util;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

public class CLI
        extends java.lang.Object
        implements java.lang.Runnable {

    private InputStream in;
    private OutputStream out;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private String inputCommand;
    public CLI(InputStream in, OutputStream out) {
        this.in = in;
        this.out = out;
    }

    public void	addPropertyChangeListener(PropertyChangeListener pcl) {
        this.pcs.addPropertyChangeListener(pcl);
    }

    public void	removePropertyChangeListener(PropertyChangeListener pcl)  {
        this.pcs.removePropertyChangeListener(pcl);
    }
    @SuppressWarnings("resource")
    @Override
    public void run() {

        while (true) {
            System.out.println("---------CLI---------\nstart - start server\nshutdown - ternimate server\n--------------\nenter command....\n----------\n");
            inputCommand = new Scanner(in).nextLine();

            if (inputCommand.equals("start") || inputCommand.equals("shutdown")) {
                this.pcs.firePropertyChange("command", null, inputCommand);
                continue;
            }
            else
                System.out.println("wrong input....");
        }
    }


    public void	write(String string) {

    }
}
