import java.net.*;
import java.io.*;

public class Server {
    private final int PORT = 5000;
    private ServerSocket server = null;
    private Socket socket = null;


    public Server() {
        // starts server and waits for a connection
        try {
            this.server = new ServerSocket(PORT);
            System.out.println("Server started");
            System.out.println("Waiting for a client ...");

            while (true) {
                //Case when a user connects
                socket = server.accept();
                if(validateUser(socket)) {
                    //Double checks that a user is a valid socket connection
                    System.out.println("Client accepted");
                    interactionThread clientThread = new interactionThread(socket);
                    clientThread.start();
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
    private boolean validateUser(Socket socket) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            if (in.readLine().equals("Lorem Ipsum")) {
                System.out.println("User Validated");
                return true;
            }
            System.out.println("User Not valid");
            return false;
        } catch (IOException e) {
            System.err.println(e);
            return false;
        }
    }

    public static void main(String args[])
    {
        //Start the Server
        Server server = new Server();
    }
}
