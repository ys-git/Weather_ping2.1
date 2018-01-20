package app.ys.weather_ping21;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static com.google.android.gms.internal.zzip.runOnUiThread;

public class ForegroundService extends Service implements LocationListener {
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    ScheduledFuture beeperHandle;
    private static final String LOG_TAG = "ForegroundService";
    LocationManager locationManager;
    double lat,lon;
    SharedPreferences switches;
    String s,q;
    String cty,des,tmp;


    @Override
    public void onCreate()
    {
        switches = getSharedPreferences("toggle", Context.MODE_PRIVATE);
        super.onCreate();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)) {
            Log.i(LOG_TAG, "Received Start Foreground Intent ");

            }

        else
        if (intent.getAction().equals(Constants.ACTION.STOPFOREGROUND_ACTION)) {
            Toast.makeText(this,"Stop Service",Toast.LENGTH_SHORT).show();
            Log.i(LOG_TAG, "Received Stop Foreground Intent");
            stopForeground(true);
            stopSelf();
            /*beeperHandle.cancel(true);
            scheduler.shutdown();*/
        }
        takePicsPeriodically(5);
        return START_STICKY;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "In onDestroy");
        beeperHandle.cancel(true);
        scheduler.shutdown();
    }
    @Override
    public IBinder onBind(Intent intent) {

        return null;

    }

    public void takePicsPeriodically(long period) {
        beeperHandle = scheduler.scheduleAtFixedRate(beeper,period, period,TimeUnit.SECONDS);
        Log.i("MyTestService", "Service at pics");
    }

    final Runnable beeper = new Runnable() {
        public void run() {
            try {


                sendNotification();

                Log.i("MyTestService", "Service running");
            }catch (Exception e) {
                Log.e("TAG","error in executing: It will no longer be run!: "+e.getMessage());
                e.printStackTrace();
            }
        }
    };

    public void sendNotification() {

        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.drawable.logoq);
        getLocation();

        //RemoteViews notificationView = new RemoteViews(this.getPackageName(),R.layout.notification_dark);

        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("WeatherPing")
                .setTicker("WeatherPing")
                .setContentText(tmp+" C"+" in "+cty)
                .setSmallIcon(R.drawable.logoq)
                .setLargeIcon(Bitmap.createScaledBitmap(icon, 128, 128, false))
                //.setContent(notificationView)
                .setOngoing(true).build();
        notification.contentIntent=  PendingIntent.getActivity(this, 0,
                new Intent(this, Splashload.class), PendingIntent.FLAG_UPDATE_CURRENT);

        startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE,
                notification);
    }



    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        lat=location.getLatitude();
        lon=location.getLongitude();
        s=String.valueOf(lat);
        q=String.valueOf(lon);

        ex();

    }

    @Override
    public void onProviderDisabled(String provider) {
        //Toast.makeText(this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    void ex()
    {
        Fetches.placeIdTask asyncTask =new Fetches.placeIdTask(new Fetches.AsyncResponse() {
            public void processFinish(String weather_city, String weather_description, String weather_temperature) {

                cty=weather_city;
                des=weather_description;
                tmp=weather_temperature;

            }
        });
        asyncTask.execute(s,q);
    }


}


