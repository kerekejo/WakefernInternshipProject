import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

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

                String options = in.readLine();
                options = new String(Base64.getUrlDecoder().decode(options), StandardCharsets.UTF_8);
                System.out.println("Received Client Input: " + options);
                if (options.equals("exit")) {
                    break;
                }

                wr.println("Server replied: "  + options);

            }

        } catch (IOException e) {
            System.err.println(e);
            return;
        }
    }

}