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

                    case "Request Data":
                    case "request data":
                        DataPack requestedData;
                        JSONworker jw = new JSONworker();
                        String recordRequest = in.readLine();
                        requestedData = jw.readJSON(recordRequest);
                        if(requestedData == null) {
                            wr.println("Data " + recordRequest + " has not been found or is invalid.");
                            break;
                        }
                        wr.println(requestedData);
                        break;
                    case "Send Data":
                    case "send data":
                        JSONworker jw2 = new JSONworker();
                        DataPack dp = new DataPack();
                        dp.setUserName(in.readLine());
                        dp.setDate(in.readLine());
                        jw2.writeJSON(dp);
                        wr.println(dp.toString() + " successfully added to the database");
                        break;

                    default:
                        wr.println("Unknown option");
                        wr.println("System Disconnecting");
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
