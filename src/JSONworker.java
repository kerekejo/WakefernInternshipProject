import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;


import java.io.*;


public class JSONworker {

    public JSONworker(){

    }

    public void writeJSON(DataPack dPack) throws IOException {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        FileWriter writer = new FileWriter("datapack.json", true);
        writer.write(gson.toJson(dPack));
        writer.write("\n");
        writer.close();
    }

    public DataPack readJSON(String dataRecord) throws FileNotFoundException {
        try {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            BufferedReader bufferedReader = new BufferedReader(new FileReader("datapack.json"));
            String extractedData = bufferedReader.readLine();
            while(!extractedData.contains(dataRecord)){
                extractedData = bufferedReader.readLine();
                if(extractedData == null){
                    return null;
                }
            }
            DataPack dPack = gson.fromJson(extractedData, DataPack.class);
            return dPack;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}



class DataPack{
    private String userName;
    private String Date;



    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }
    public String getDate() {
        return Date;
    }

    @Override
    public String toString() {
        return userName + ' ' + Date;
    }
}