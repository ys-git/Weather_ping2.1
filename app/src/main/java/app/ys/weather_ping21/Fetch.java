package app.ys.weather_ping21;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Fetch {

    private static final String OPEN_WEATHER_MAP_URL =
            "https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&units=metric";

    private static final String OPEN_WEATHER_MAP_API_KEY = "1a6b11c1dda6b00770aa9546844f64a7";

    public static String setWeatherIcon(int actualId, long sunrise, long sunset){
        Log.i("WP", "Inside Fetch");

        int id = actualId / 100;
        String icon = "";
        if(actualId == 800){
            long currentTime = new Date().getTime();
            if(currentTime>=sunrise && currentTime<sunset) {
                icon = "&#xf00d;";
            } else {
                icon = "&#xf02e;";
            }
        } else {
            switch(id) {
                case 2 : icon = "&#xf01e;";
                    break;
                case 3 : icon = "&#xf01c;";
                    break;
                case 7 : icon = "&#xf014;";
                    break;
                case 8 : icon = "&#xf013;";
                    break;
                case 6 : icon = "&#xf01b;";
                    break;
                case 5 : icon = "&#xf019;";
                    break;
            }
        }
        return icon;
    }

    public interface AsyncResponse {
        void processFinish(String output1, String output2, String output3, String output4, String output5, String output6, String output7, String output8,String output9,String output10,String output11,String output12,String output13);
    }

    public static class placeIdTask extends AsyncTask<String, Void, JSONObject> {
        public String humidity,pressure,temperature,wsp,wdeg,clo,visibility;

        public AsyncResponse delegate = null;//Call back interface

        public placeIdTask(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }

        @Override
        protected JSONObject doInBackground(String... params) {

            JSONObject jsonWeather = null;
            try {
                jsonWeather = getWeatherJSON(params[0], params[1]);
            } catch (Exception e) {
                Log.d("Error", "Cannot process JSON results", e);
            }
            return jsonWeather;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            Long s1,s2;
            String k,l;
            try {
                if(json != null){
                    DateFormat df = DateFormat.getDateTimeInstance();
                    JSONObject data = new JSONObject(json.toString());
                    JSONObject details = json.getJSONArray("weather").getJSONObject(0);

                    if (data.has("main")) {
                        JSONObject main = json.getJSONObject("main");
                        if (main.has("humidity")) {
                            humidity = main.getString("humidity") + "%";
                        } else {humidity= "No data";}
                        if (main.has("pressure")) {
                            pressure = main.getString("pressure") + " hPa";
                        } else {pressure="No data";}
                        if (main.has("temp")) {
                            temperature = String.format("%.2f", main.getDouble("temp"));
                        } else {temperature="No data";}

                    } else {}


                    if (data.has("wind")) {
                        JSONObject wind = json.getJSONObject("wind");

                        if (wind.has("speed")) {
                            wsp = wind.getString("speed") + " m/s";
                        } else {wsp="No data";}

                        if (wind.has("deg")) {
                            wdeg = wind.getString("deg") + "°";
                        } else {wdeg="No data";}
                    } else {}

                    if (data.has("clouds")) {
                        JSONObject clouds=json.getJSONObject("clouds");
                        if (clouds.has("all")) {
                            clo = clouds.getString("all") + "%";
                        } else {clo="No data";}

                    } else {}

                    if (data.has("visibility")) {
                        visibility = data.getString("visibility");
                    } else {visibility="No data";}




                    //JSONObject main = json.getJSONObject("main");
                    //JSONObject wind = json.getJSONObject("wind");
                    //JSONObject clouds=json.getJSONObject("clouds");

                    String city = json.getString("name") + ", " + json.getJSONObject("sys").getString("country");
                    String description = details.getString("description");
                    //String temperature = String.format("%.2f", main.getDouble("temp"));
                   // String humidity = main.getString("humidity") + "%";
                   // String pressure = main.getString("pressure") + " hPa";
                    //String wsp = wind.getString("speed") + " m/s";
                    //String wdeg = wind.getString("deg") + "°";
                    //String clo = clouds.getString("all") + "%";
                    String updatedOn = df.format(new Date(json.getLong("dt")*1000));
                    String iconText = setWeatherIcon(details.getInt("id"),
                            json.getJSONObject("sys").getLong("sunrise") * 1000,
                            json.getJSONObject("sys").getLong("sunset") * 1000);
                    s1=json.getJSONObject("sys").getLong("sunrise")*1000;
                    s2=json.getJSONObject("sys").getLong("sunset")*1000;


                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");
                    k=String.valueOf(simpleDateFormat.format(s1));
                    l=String.valueOf(simpleDateFormat.format(s2));
                    Log.i("WP", "Info Fetched");

                    delegate.processFinish(city, description, temperature, humidity, pressure,visibility,wsp,wdeg,updatedOn, iconText,k,l,clo);

                }
            } catch (JSONException e) {
                //Log.e(LOG_TAG, "Cannot process JSON results", e);
            }
        }
    }

    public static JSONObject getWeatherJSON(String lat, String lon){
        try {
            URL url = new URL(String.format(OPEN_WEATHER_MAP_URL, lat, lon));
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            connection.addRequestProperty("x-api-key", OPEN_WEATHER_MAP_API_KEY);

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp="";
            while((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());

            // successful
            if(data.getInt("cod") != 200){
                return null;
            }

            return data;
        }
        catch(Exception e){
            return null;
        }
    }
}
