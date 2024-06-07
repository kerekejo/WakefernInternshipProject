import java.io.*;
import java.net.*;
import java.util.Base64;
import java.util.Scanner;
import java.util.Random;

public class Client {
    private int sessionId = -999;
    static final int PORT = 5000;
    private Socket socket;


    //Constructor
    public Client(String host) throws IOException {


        try {
            boolean connect = false;

            //SessionId is only possible for a non-logged in user, therefore runs on initial login.
            if(sessionId == -999) {
                //Connect to the server and initialize a printwriter and a buffered reader.
                socket = connectToServer(host, sessionId);
                PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                //For loop allows three chances before disconnecting, if they get it correct the loop breaks.
                for (int i = 0; i < 3; i++) {
                    String code = "";
                    Scanner scanner = new Scanner(System.in);
                    System.out.println("Please enter your username: ");
                    String username = scanner.nextLine();
                    System.out.println("Please enter your password: ");
                    String password = scanner.nextLine();
                    code = username + password;

                    //Encrypts the username and password given
                    Base64.Encoder encoder = Base64.getEncoder();
                    byte[] encoded = encoder.encode(code.getBytes());

                    //Send the encoded string to the server
                    String encodedString = new String(encoded);
                    pw.println(encodedString);

                    //Reads the server response
                    String line = br.readLine();
                    System.out.println(line);


                if (line.equals("User Validated")) {
                    //Client case for when the server confirms the user connection. Assigns the client a random ID number.
                    Random rand = new Random();
                    System.out.println("Connected to " + host + ":" + PORT);
                    System.out.println("Successfully connected to " + host);
                    sessionId = rand.nextInt(1000);

                    //Send the newly created sessionID the server
                    pw.println(sessionId);
                    connect = true;
                    break;
                 }
                }
                if (!connect) {
                    //Client case for when the server denies the user connection.
                    System.out.println("Failed to connect to " + host + ":" + PORT);
                    socket.close();
                }

            }
            socket.close();
            connectToServer(host, sessionId);

            while(true) {
                //This is the further interaction between client and server should the client have previously connected
                break;
            }


        } catch (IOException e) {
            System.err.println(e);
            return;
        }


    }



    public static Socket connectToServer(String host, int userId){
        //Static method that sends the userID to the server during the process of server connection. Returns the socket.
       try {
           Socket socket = new Socket(host, PORT);
           PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
           pw.println(userId);
           return socket;
       } catch (IOException e){
           System.err.println(e);
           return null;
       }
    }
}
/*
public static void main(String[] args) {
    Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
        public void run() {
            System.out.println("In shutdown hook");
        }
    }, "Shutdown-thread"));
}
 */