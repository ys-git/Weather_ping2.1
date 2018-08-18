package app.ys.weather_ping21;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.provider.*;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

//import static com.google.android.gms.internal.zzip.runOnUiThread;

public class ForegroundService extends Service{
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

    //-------------------------------------location related
    private String mLastUpdateTime;
    private static final String TAG = Main.class.getSimpleName();

    // location updates interval - 10sec
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;


    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;

    private static final int REQUEST_CHECK_SETTINGS = 100;


    // bunch of location related apis
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    TextView txtLocationResult;

    // boolean flag to toggle the ui
    private Boolean mRequestingLocationUpdates;


    @Override
    public void onCreate() {
        switches = getSharedPreferences("toggle", Context.MODE_PRIVATE);
        super.onCreate();



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

                init();
                startLocationButtonClick();
                stopLocationButtonClick();


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

    private void init() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                mCurrentLocation = locationResult.getLastLocation();
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

                updateLocationUI();
            }
        };

        mRequestingLocationUpdates = false;

        mLocationRequest = new LocationRequest();
        //mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        //mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }
    private void restoreValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("is_requesting_updates")) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean("is_requesting_updates");
            }

            if (savedInstanceState.containsKey("last_known_location")) {
                mCurrentLocation = savedInstanceState.getParcelable("last_known_location");
            }

            if (savedInstanceState.containsKey("last_updated_on")) {
                mLastUpdateTime = savedInstanceState.getString("last_updated_on");
            }
        }

        updateLocationUI();
    }

    private void updateLocationUI() {
        if (mCurrentLocation != null) {
//            txtLocationResult.setText(
//                    "Lat: " + mCurrentLocation.getLatitude() + ", " +
//                            "Lng: " + mCurrentLocation.getLongitude()
//            );
            lat = mCurrentLocation.getLatitude();
            lon = mCurrentLocation.getLongitude();
            Log.i("WP", "Location Fetched");
            //s = String.valueOf(lat);
            //q = String.valueOf(lon);
            String units = "metric";
            String url = String.format("http://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&units=%s&appid=%s",
                    lat, lon, units, APP_ID);
            new GetWeatherTask().execute(url);

        }


    }


    private void startLocationUpdates() {
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener((Executor) this, new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG, "All location settings are satisfied.");

                        Toast.makeText(getApplicationContext(), "Started location updates!", Toast.LENGTH_SHORT).show();

                        //noinspection MissingPermission
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());

                        updateLocationUI();
                    }
                })
                .addOnFailureListener((Executor) this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);

                                Handler handler = new Handler(Looper.getMainLooper());
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        //Toast.makeText(ForegroundService.this.getApplicationContext(),"My Awesome service toast...",Toast.LENGTH_SHORT).show();
                                    }
                                });
                        }

                        updateLocationUI();
                    }
                });
    }


    public void startLocationButtonClick() {
        // Requesting ACCESS_FINE_LOCATION using Dexter library
        Dexter.withActivity((Activity) getApplicationContext())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        mRequestingLocationUpdates = true;
                        startLocationUpdates();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            // open device settings when the permission is
                            // denied permanently
                            openSettings();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }


    public void stopLocationButtonClick() {
        mRequestingLocationUpdates = false;
        stopLocationUpdates();
    }

    public void stopLocationUpdates() {
        // Removing location updates
        mFusedLocationClient
                .removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener((Executor) this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "Location updates stopped!", Toast.LENGTH_SHORT).show();

                    }
                });
    }

/*
    void getLocation() {
        try {
            Location location;
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 0, this);
            if (locationManager != null) {
                location = locationManager
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location != null) {
                    Log.d("activity", "LOC by Network");
                    lat = location.getLatitude();
                    lon = location.getLongitude();
                }
            }

            String units = "metric";
            String url = String.format("http://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&units=%s&appid=%s",
                    lat, lon, units, APP_ID);
            new GetWeatherTask().execute(url);


        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        //locationText.setText("Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude());
        /*lat=location.getLatitude();
        lon=location.getLongitude();
        s=String.valueOf(lat);
        q=String.valueOf(lon);*/





    /*}

    @Override
    public void onProviderDisabled(String provider) {
        //Toast.makeText(MainActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }
*/



    public void tx(long periods) {
        beeperHandle = scheduler.scheduleAtFixedRate(beeper, 0, periods, TimeUnit.MINUTES);
        //Log.i("MyTestService", "Service at tx");

    }

    final Runnable beeper = new Runnable() {

        public void run() {
            try {

                startLocationButtonClick();
                stopLocationButtonClick();

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
                country = "India";
                        //topLevel.getJSONObject("sys").getString("country");
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

    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(
                android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",
                BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }





}


