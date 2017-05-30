package weather;

/**
 * Created by dongnd on 30/05/2017.
 */

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dongnd on 30/05/2017.
 */

public class Common {

    public static String API_KEY="855e747f44a4ce76aa5b0c8bcbe2ebcf";
    public static String API_LINK="http://api.openweathermap.org/data/2.5/weather";

    public static String apiRequest(String lat, String lon){
        StringBuffer sb=new StringBuffer(API_LINK);

        sb.append(String.format("?lat=%s&lon=%s&APPID=%s&units=metric", lat, lon, API_KEY));
        return sb.toString();
    }

    public static String getTimeUpdate(double input){
        DateFormat dateFormat=new SimpleDateFormat("HH:mm");
        Date date=new Date();
        date.setTime((long)input*1000);

        return dateFormat.format(date);
    }

    public static String getImage(String icon){
        return String.format("http://openweathermap.org/img/w/%s.png", icon);
    }

    public static String getDate(){
        DateFormat dateFormat=new SimpleDateFormat("HH:mm");
        Date date= new Date();
        return  dateFormat.format(date);
    }
}