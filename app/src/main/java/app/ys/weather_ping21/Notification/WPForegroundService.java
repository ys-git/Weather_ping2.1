package app.ys.weather_ping21.Notification;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import app.ys.weather_ping21.Constants.WPConstants;
import app.ys.weather_ping21.Services.WPLocationTrack;
import app.ys.weather_ping21.R;
import app.ys.weather_ping21.SplashScreen.WPSplashLoad;
import app.ys.weather_ping21.Utils.ActivityUtils;

//import static com.google.android.gms.internal.zzip.runOnUiThread;

public class WPForegroundService extends Service{ //implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
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
    String city="City", des="Loading...", cap, country=" ";
    public static Instant start;

    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();

    private final static int ALL_PERMISSIONS_RESULT = 101;
    WPLocationTrack locationTrack;

    @Override
    public void onCreate() {
        switches = getSharedPreferences("toggle", Context.MODE_PRIVATE);
        super.onCreate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            Log.i("WP", "sending NEW notification");
            //sendNotification();
        }
        else
        {
            startForeground(1, new Notification());
        }
        //googleApiClient = new GoogleApiClient.Builder(this, this, this).addApi(LocationServices.API).build();
        //googleApiClient.connect();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startForegroundNotification()
    {
        tx(period);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            if (intent.getAction().equals(WPConstants.ACTION.STARTFOREGROUND_ACTION)) {
                Log.i(LOG_TAG, "Received Start Foreground Intent ");
                if ((switches.getInt("Interval", 10)) == 10) {
                    period = 10;
                }
                if ((switches.getInt("Interval", 30)) == 30) {
                    period = 30;
                }
                if ((switches.getInt("Interval", 180)) == 180) {
                    period = 180;
                }

                startForegroundNotification();

            } else {
                if (intent.getAction().equals(WPConstants.ACTION.STOPFOREGROUND_ACTION)) {

                    Activity foregroundActivity = ActivityUtils.getInstance();

                    foregroundActivity.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(foregroundActivity, "Stop Service", Toast.LENGTH_SHORT).show();
                        }
                    });

                    Log.i(LOG_TAG, "Received Stop Foreground Intent");
                    stopForeground(true);
                    stopSelf();
                }

                startForegroundNotification();
            }
        }
        catch (Exception e) {
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
                locationTrack.stopListener();
            }catch(Exception e)
            {

            }
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

        Log.i("WP", "inside lx");

        locationTrack = new WPLocationTrack(WPForegroundService.this);
        locationTrack.getLocation();
        lon = locationTrack.getLongitude();
        lat = locationTrack.getLatitude();
        //Toast.makeText(getApplicationContext(), "Longitude:" + Double.toString(lon) + "\nLatitude:" + Double.toString(lat), Toast.LENGTH_SHORT).show();
        String units = "metric";
        String url = String.format("https://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&units=%s&appid=%s",
                lat, lon, units, APP_ID);
        new GetWeatherTask().execute(url);
    }

    public void tx(long periods) {
        Log.i("WP", "created new beeper handle");
        beeperHandle = scheduler.scheduleAtFixedRate(beeper, 0, periods, TimeUnit.MINUTES);
    }

    final Runnable beeper = new Runnable() {

        public void run() {
            try {
                Log.i("WP", "inside beeper runnable");
                lx();
                Log.i("service", "Inside Service");
            } catch (Exception e) {
                Log.e("TAG", "error in executing: It will no longer be run!: " + e.getMessage());
                e.printStackTrace();
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sendNotification() {

        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.drawable.logonotif);

        //------------------------
        String NOTIFICATION_CHANNEL_ID = "app.ys.weatherping";
        String channelName = "Weather Notification";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_LOW);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        chan.setSound(null,null);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

        Notification notification = notificationBuilder.setOngoing(true)
                .setTicker("WeatherPing")
                .setContentText(des)
                .setColor(this.getResources().getColor(R.color.colorPrimary))
                .setSmallIcon(R.drawable.logonotif)
                .setContentTitle(weather + " °C" + " in "+city+", "+country)
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                        R.drawable.weather1))
                .setOngoing(true)
                .build();

        notification.contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, WPSplashLoad.class), PendingIntent.FLAG_UPDATE_CURRENT);

        startForeground(2, notification);

        Log.i("service", "Sending notification");

        //RemoteViews notificationView = new RemoteViews(this.getPackageName(),R.layout.notification_dark);

        /*Notification notification = new NotificationCompat.Builder(this)
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
                notification);*/
    }

    private class GetWeatherTask extends AsyncTask<String, Void, String> {

        public GetWeatherTask() {
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected String doInBackground(String... strings) {

            try {

                Log.i("service", "Inside WeatherTask");
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

            } catch (Exception e) {
                Log.e("notification error","error while starting notification service" + e);
            }
            return weather;
        }

        @Override
        protected void onPostExecute(String temp) {

        }
    }

    void capital(String n)
    {
        n=" "+n;
        cap="";
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