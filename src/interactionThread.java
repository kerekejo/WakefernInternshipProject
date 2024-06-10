import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class interactionThread extends Thread {
    protected Socket socket;
    private static Vector<String> activeUsers = new Vector();
    public interactionThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {


        try {
            //Buffered Reader to read from the inputStream and PrintWriter to write to the output stream
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter wr = new PrintWriter(socket.getOutputStream(), true);


            //Upon user connection check the userID, this allows the client to skip the login process
            String userId = in.readLine();

            //Static method to check if the user was previously connected
            if(validateUserID(userId)) {
                //Previously connected user connected
                System.out.println(userId);
                System.out.println("User with the session ID: " + userId + " has reconnected");
            }
            else if(validateUser(socket)) {
                //Validate a new user. The client assigns a userId upon acceptance of the request and adds userID to the session history.
                System.out.println("Client accepted");

                //Receive a new sessionID from the client and store it
                activeUsers.add(in.readLine());

            } else {
                socket.close();
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
        for(int i = 0; i < 3; i--){

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

}