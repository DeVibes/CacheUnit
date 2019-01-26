package client;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JOptionPane;

public class CacheUnitClient
        extends java.lang.Object {

    public CacheUnitClient() {}

    public String send(String request) {

        try (Socket socket = new Socket("localhost",12345)) {

            PrintWriter send = new PrintWriter(socket.getOutputStream(),true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            if (request.equals("Stats")) {

                send.println(request);

                request = in.readLine();

            }

            else if (request.startsWith("LRU") || request.startsWith("NRU") || request.startsWith("RANDOM")) {

                send.println(request);
            }
            else {

                JSONObject jsonObject = null;

                JSONParser parser = new JSONParser();

                try {
                    Object obj = parser.parse(new FileReader(request));

                    jsonObject = (JSONObject) obj;

                }
                catch (Exception e) {
                    System.out.println("Failed parsing");
                    request = "Failed";
                }

                System.out.println("Sent: " + jsonObject);

                send.println(jsonObject);

                request = in.readLine();

                JOptionPane.showMessageDialog(null, request);
            }
        }

        catch (Exception e) {
            JOptionPane.showMessageDialog(null, "No server found........");

            request = null;
        }
        return request;
    }

}
