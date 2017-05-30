package weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by dongnd on 30/05/2017.
 */

public class Helper {
    public static String stream=null;

    public Helper(){

    }

    public String getHttpData(String urlString){
        try {
            URL url=new URL(urlString);
            HttpURLConnection connection=(HttpURLConnection)url.openConnection();

            if(connection.getResponseCode()==200){
                BufferedReader R=new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder sb=new StringBuilder();

                String line;
                while ((line=R.readLine())!=null){
                    sb.append(line);

                }
                stream=sb.toString();
                connection.disconnect();
            }

        }catch (MalformedURLException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

        return stream;
    }
}
