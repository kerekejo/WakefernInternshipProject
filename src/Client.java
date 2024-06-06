import java.io.*;
import java.net.*;
import java.util.Scanner;


public class Client {

    //Initialized data; socket, and authorization code; authorization code is currently made final be sure to change the authentication within the server file if you change this.
    private Socket socket = null;
    private final int PORT = 5000;
    private final String authorizationCode = "Lorem Ipsum";


    //Constructor
    public Client(String host) {
        //Attempts to connect to the server
        try {
            //Create new socket and respond about the connections validity
            this.socket = new Socket(host, PORT);

            //Authorize the client, using hard-coded authorization code. Essentially this just verifies that the client is a valid connection to the server (can be changed)
            PrintWriter authorization = new PrintWriter(socket.getOutputStream(), true);
            authorization.println(authorizationCode);

            System.out.println("Connected to " + host + ":" + PORT);
            System.out.println("Successfully connected to " + host);



        } catch (IOException u) {
            System.out.println("Failed to connect to " + host + ":" + PORT);
            System.err.println(u);
            return;
        }
        try {
            while (!socket.isClosed()) {
                //Creates a buffered reader to read in the input stream and a printwriter to write to the output stream
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);

                //Initialize a scanner to read in the user input
                Scanner scanner = new Scanner(System.in);
                String options = " ";
                String response;

                do {
                    System.out.println("Select One: Request Data, Send Data, Exit");
                    options = scanner.nextLine();
                    switch(options) {
                        case "Request Data":
                        case "request data":
                            pw.println(options);
                            System.out.println("What is the name of the record?");
                            pw.println(scanner.nextLine());
                            response = br.readLine();
                            if(response == null){
                                System.out.println("No data found.");
                                break;
                            }
                            System.out.println(response);
                            break;

                        case "Send Data":
                        case "send data":
                            pw.println(options);
                            System.out.println("Username: ");
                            pw.println(scanner.nextLine());
                            System.out.println("Date: ");
                            pw.println(scanner.nextLine());
                            response = br.readLine();

                            System.out.println(response);
                            break;

                        case "Exit":
                        case "exit":
                            System.out.println("Goodbye");
                            socket.close();
                            break;

                        default:
                            System.out.println("Invalid option");
                            break;


                    }

                } while(!options.equalsIgnoreCase("exit"));
                socket.close();
            }
        } catch(IOException u) {
            System.err.println(u);
        }
    }






}
