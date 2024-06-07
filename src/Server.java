import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import netscape.javascript.JSException;
import netscape.javascript.JSObject;

import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class Server {


    public Server() {
        // starts server and waits for a connection
        try {
            int PORT = 5000;
            ServerSocket server = new ServerSocket(PORT);
            System.out.println("Server started");
            System.out.println("Waiting for a client ...");

            while (true) {
                //Case when a user connects
                Socket socket = server.accept();

                if(validateUser(socket)) {
                    //Double checks that a user is a valid socket connection
                    System.out.println("Client accepted");
                    interactionThread interactionThread = new interactionThread(socket);
                    interactionThread.start();
                } else {
                    socket.close();
                }

            }
        } catch (IOException e) {
            System.err.println(e);
            return;
        }

    }

    //Validate a user's client
    private boolean validateUser(Socket socket) throws IOException {

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter wr = new PrintWriter(socket.getOutputStream(), true);

        for(int i = 0; i < 3; i++){

            Base64.Decoder decoder = Base64.getDecoder();
            byte[] decoded = decoder.decode(in.readLine());
            String decodedString = new String(decoded);


            try (FileReader reader = new FileReader("password.json")) {
                Password p = gson.fromJson(reader, Password.class);
                if(p.getPassword().equals("1234")){
                    if(decodedString.equals("aaravp")){
                        wr.println("User Validated");
                        return true;
                    }
                    wr.println("User Not Valid");
                }


            }
            catch (IOException e) {
                wr.println("User Not Valid");
                e.printStackTrace();
            }
        }
        return false;
    }

    public static void main(String args[])
    {
        //Start the Server
        Server server = new Server();
    }
}
