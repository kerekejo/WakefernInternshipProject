import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Base64;
import java.util.Vector;


public class Server2 {
    private static Vector<String> activeUsers = new Vector<String>();


    public Server2() {


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


                //Initialize a reader and a writer to/from the client
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter wr = new PrintWriter(socket.getOutputStream(), true);

                //Upon user connection check the userID, this allows the client to skip the login process
                String userId = in.readLine();

                //Static method to check if the user was previously connected
                if(validateUserID(userId)) {
                    //Previously connected user connected
                    System.out.println(userId);
                    System.out.println("User with the session ID: " + userId + " has reconnected");
                    interactionThread interactionThread = new interactionThread(socket);
                    interactionThread.start();
                }
                else if(validateUser(socket)) {
                    //Validate a new user. The client assigns a userId upon acceptance of the request and adds userID to the session history.
                    System.out.println("Client accepted");

                    //Receive a new sessionID from the client and store it
                    activeUsers.add(in.readLine());
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

        //Initialize the GSON, and the reader and writer to/from the client
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter wr = new PrintWriter(socket.getOutputStream(), true);

        //Responds to the three user attempts to login
        for(int i = 0; i < 3; i++){

            //Decrypt the username and password
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] decoded = decoder.decode(in.readLine());
            String decodedString = new String(decoded);

            //Verify the json file, and the decrypted password and username
            try (FileReader reader = new FileReader("C:\\Users\\cijok\\IdeaProjects\\WakefernInternshipProject\\src\\password.json")) {
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

    public static boolean validateUserID(String userId){
        if(activeUsers.isEmpty()){
            return false;
        } else {
            return activeUsers.contains(userId);
        }
    }

    public static void main(String args[])
    {
        //Start the Server
        Server2 server = new Server2();
    }
}
