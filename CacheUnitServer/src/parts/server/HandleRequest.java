package parts.server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import parts.dm.DataModel;
import parts.services.CacheUnitController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class HandleRequest<T>
        extends java.lang.Object
        implements java.lang.Runnable {

    private Socket socket;
    private CacheUnitController<T> cacheUnitController;

    public HandleRequest(Socket s, CacheUnitController<T> cont) {
        this.socket = s;
        this.cacheUnitController = cont;
    }

    public void run() {

        BufferedReader in;
        PrintWriter out;

        try {
            String requestInput = null;
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(),true);

            requestInput = in.readLine();

            if (requestInput.equals("Stats")) {

                out.println(cacheUnitController.getStats());

                System.out.println("Sent stats");
            }
            else if (requestInput.startsWith("LRU") || requestInput.startsWith("NRU") || requestInput.startsWith("RANDOM"))  {

                cacheUnitController.changeCacheAlgorithm(requestInput);

            }
            else {
                Request<DataModel<T>[]> request = new Gson().fromJson(requestInput, new TypeToken<Request<DataModel<T>[]>>(){}.getType());
                DataModel<T>[] dms = null;

                if (request.getHeaders().get("action").equals("UPDATE")) {
                    if (cacheUnitController.update(request.getBody()))
                        out.println("UPDATE complete");
                    else
                        out.println("UPDATE failed");
                }

                if (request.getHeaders().get("action").equals("DELETE")) {
                    if (cacheUnitController.delete(request.getBody()))
                        out.println("DELETE complete");
                    else
                        out.println("DELETE failed");
                }
                if (request.getHeaders().get("action").equals("GET")) {
                    dms = cacheUnitController.get(request.getBody());

                    String answer = "GET complete";

                    if (dms != null) {

                        for (DataModel<T> dm : dms) {
                            if (dm == null)
                                answer = "GET failed";
                            break;
                        }
                    }
                    else
                        answer = "GET failed";
                    out.println(answer);
                }

            }

        } catch (IOException e) {
            System.out.println("failed input.....");
        }
    }
}
