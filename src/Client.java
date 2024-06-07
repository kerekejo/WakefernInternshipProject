import java.io.*;
import java.net.*;
import java.util.Base64;
import java.util.Scanner;


public class Client {


    //Constructor
    public Client(String host) throws IOException {
        //Attempts to connect to the server
        int PORT = 5000;
        //Initialized data; socket, and authorization code; authorization code is currently made final be sure to change the authentication within the server file if you change this.
        Socket socket = null;
        BufferedReader br = null;

        //Create new socket and respond about the connections validity
        socket = new Socket(host, PORT);
        boolean connect = false;
        try {
            int logInAttempts = 0;

            for(int i=0; i<3; i++) {
                PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
                br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                //Authorize the client, using hard-coded authorization code. Essentially this just verifies that the client is a valid connection to the server (can be changed)
                // PrintWriter authorization = new PrintWriter(socket.getOutputStream(), true);
                // String authorizationCode = "Lorem Ipsum";
                //authorization.println(authorizationCode);
                String code = "";
                Scanner scanner = new Scanner(System.in);
                System.out.println("Please enter your username: ");
                String username = scanner.nextLine();
                System.out.println("Please enter your password: ");
                String password = scanner.nextLine();
                code = username + password;

                Base64.Encoder encoder = Base64.getEncoder();
                byte[] encoded = encoder.encode(code.getBytes());

                String encodedString = new String(encoded);
                pw.println(encodedString);

                String line = br.readLine();
                System.out.println(line);


                if (line.equals("User Validated")) {
                    System.out.println("Connected to " + host + ":" + PORT);
                    System.out.println("Successfully connected to " + host);
                    connect = true;
                }
            }
            if(!connect) {
                System.out.println("Failed to connect to " + host + ":" + PORT);
                socket.close();
            }
        } catch (Exception e) {
            System.err.println(e);
            return;
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