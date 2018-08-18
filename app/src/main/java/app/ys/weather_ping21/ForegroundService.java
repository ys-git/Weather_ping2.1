package app.ys.weather_ping21;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

//import static com.google.android.gms.internal.zzip.runOnUiThread;

public class ForegroundService extends Service{ //implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    ScheduledFuture beeperHandle;
    private static final String LOG_TAG = "ForegroundService";
    LocationManager locationManager;
    private static final int PERMISSION_ACCESS_COARSE_LOCATION = 1;
    private static final String APP_ID = "15160e4cc6c35852be653f728de32299";
    double lat, lon;
    Location loc;
    long period;
    private GoogleApiClient googleApiClient;
    SharedPreferences switches;
    String weather = "0.0";
    String s, q;
    String city="City",des="Loading...",cap,country=" ";


    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();

    private final static int ALL_PERMISSIONS_RESULT = 101;
    LocationTrack locationTrack;


    @Override
    public void onCreate() {
        switches = getSharedPreferences("toggle", Context.MODE_PRIVATE);
        super.onCreate();

        //googleApiClient = new GoogleApiClient.Builder(this, this, this).addApi(LocationServices.API).build();
        //googleApiClient.connect();

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)) {
                Log.i(LOG_TAG, "Received Start Foreground Intent ");
                if ((switches.getInt("Interval", 10)) == 10) {
                    period = 3;
                }
                if ((switches.getInt("Interval", 30)) == 30) {
                    period = 10;
                }
                if ((switches.getInt("Interval", 180)) == 180) {
                    period = 11;
                }

                lx();


            } else if (intent.getAction().equals(Constants.ACTION.STOPFOREGROUND_ACTION)) {
                Toast.makeText(this, "Stop Service", Toast.LENGTH_SHORT).show();
                Log.i(LOG_TAG, "Received Stop Foreground Intent");
                stopForeground(true);
                stopSelf();
            /*beeperHandle.cancel(true);
            scheduler.shutdown();*/
            }

            tx(period);


        }catch (RuntimeException e) {
            e.printStackTrace();
        }
        return START_STICKY;


    }


    @Override
    public void onDestroy() {
        if ((switches.getInt("Toggle2", -1)) == 1) {
            super.onDestroy();
            Log.i(LOG_TAG, "In onDestroy");
            try {
                beeperHandle.cancel(true);
                scheduler.shutdown();
            }catch(NullPointerException e)
            {}
            locationTrack.stopListener();

        }

        if ((switches.getInt("Toggle2", -1)) == 0) {
            super.onDestroy();
            Log.i(LOG_TAG, "In onDestroy");


        }
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;

    }

    public void lx(){
        locationTrack = new LocationTrack(ForegroundService.this);
        lon = locationTrack.getLongitude();
        lat = locationTrack.getLatitude();
        //Toast.makeText(getApplicationContext(), "Longitude:" + Double.toString(lon) + "\nLatitude:" + Double.toString(lat), Toast.LENGTH_SHORT).show();
        String units = "metric";
        String url = String.format("http://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&units=%s&appid=%s",
                lat, lon, units, APP_ID);
        new GetWeatherTask().execute(url);
    }



    public void tx(long periods) {
        beeperHandle = scheduler.scheduleAtFixedRate(beeper, 0, periods, TimeUnit.MINUTES);
        //Log.i("MyTestService", "Service at tx");

    }

    final Runnable beeper = new Runnable() {

        public void run() {
            try {

                lx();

                Log.i("MyTestService", "Inside Service");
            } catch (Exception e) {
                Log.e("TAG", "error in executing: It will no longer be run!: " + e.getMessage());
                e.printStackTrace();
            }
        }

    };


    public void sendNotification() {

        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.drawable.logonotif);







        Log.i("MyTestService", "Sending notification");


        //RemoteViews notificationView = new RemoteViews(this.getPackageName(),R.layout.notification_dark);

        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle(weather + " °C" + " in "+city+", "+country)
                .setTicker("WeatherPing")
                .setContentText(des)
                .setSmallIcon(R.drawable.logonotif)
                .setColor(this.getResources().getColor(R.color.colorPrimary))
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                        R.drawable.weather))
                //.setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
                //.setContent(notificationView)
                .setOngoing(true).build();
        notification.contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, Splashload.class), PendingIntent.FLAG_UPDATE_CURRENT);

        startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE,
                notification);
    }


    private class GetWeatherTask extends AsyncTask<String, Void, String> {


        public GetWeatherTask() {

        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                Log.i("MyTestService", "Inside WeatherTask");
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder builder = new StringBuilder();

                String inputString;
                while ((inputString = bufferedReader.readLine()) != null) {
                    builder.append(inputString);
                }

                JSONObject topLevel = new JSONObject(builder.toString());
                JSONObject main = topLevel.getJSONObject("main");
                JSONObject details = topLevel.getJSONArray("weather").getJSONObject(0);

                des = details.getString("description");
                capital(des);
                city = topLevel.getString("name");
                JSONObject syst = topLevel.getJSONObject("sys");
                country = topLevel.getJSONObject("sys").getString("country");
                //city = name.getString("name");
                weather = String.valueOf(main.getDouble("temp"));
                sendNotification();

                urlConnection.disconnect();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return weather;
        }

        @Override
        protected void onPostExecute(String temp) {

            //textView.setText("Current Weather: " + temp);
        }
    }

    void capital(String n)
    {
        n=" "+n;
        cap="";
        Log.i("MyTestService", "Inside Capital");
        for(int i=0;i<n.length();i++)
        {
            char x=n.charAt(i);
            if(x==' ')
            {
                cap=cap+" ";
                char y=n.charAt(i+1);
                cap=cap+Character.toUpperCase(y);
                i++;
            }
            else
            {
                cap=cap+x;
            }
        }

        des=cap;

    }




}


