import java.net.*;
import java.io.*;

public class interactionThread extends Thread {
    protected Socket socket;

    public interactionThread(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            //Buffered Reader to read from the inputStream and PrintWriter to write to the output stream
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter wr = new PrintWriter(socket.getOutputStream(), true);

            while (true) {
                //Read in the users option selection, print it on the server and then move to switch statement
                String options = in.readLine();
                System.out.println("Received Client Input: " + options);
                if (options.equals("exit")) {
                    break;
                }

                switch (options) {
                    case "Hello":
                    case "hello":
                        wr.println("Hello user!");
                        break;
                    case "What day is it?":
                    case "what day is it":
                        wr.println("Today is a beautiful day!");
                        break;
                    default:
                        wr.println("Unknown option");
                        socket.close();
                        break;
                }

            }

        } catch (IOException e) {
            System.err.println(e);
            return;
        }
    }

}
