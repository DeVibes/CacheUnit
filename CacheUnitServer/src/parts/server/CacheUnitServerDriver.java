package parts.server;

import parts.util.CLI;

import java.io.IOException;
import java.net.UnknownHostException;

public class CacheUnitServerDriver
        extends Object {

    public CacheUnitServerDriver() {}

    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {

        System.out.println("Started new Thread - " + Thread.currentThread().getName());

        CLI cli = new CLI(System.in, System.out);
        Server server = new Server();
        cli.addPropertyChangeListener(server);
        new Thread(cli).start();
    }
}
