import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.*;
import java.io.*;
import java.util.*;


public class Server {
    private static Vector<String> activeUsers = new Vector<String>();


    public Server() {


        // starts server and waits for a connection
        try {

            //Initial message upon server boot
            int PORT = 5000;
            ServerSocket server = new ServerSocket(PORT);
            System.out.println("Server started");
            System.out.println("Waiting for a client ...");


            while (true) {

                //Case when a user connects
                Socket socket = server.accept();
                interactionThread interactionThread = new interactionThread(socket);
                interactionThread.start();


            }
        } catch (IOException e) {
            System.err.println(e);
            return;
        }

    }



    public static void main(String args[])
    {
        //Start the Server
        Server server = new Server();
    }
}
