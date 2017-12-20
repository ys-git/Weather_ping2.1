package app.ys.weather_ping21;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import java.net.URL;
import android.widget.TextView;

public class Main extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    // Project Created by Ferdousur Rahman Shajib
    // www.androstock.com


    TextView cityField,cloud,rain,latt, detailsField, currentTemperatureField, humidity_field, pressure_field, weatherIcon, updatedField,sealevel,groundlevel,windspeed,winddeg,sun,set;

    Typeface weatherFont;
    double lat,lon;
    private static final int PERMISSION_ACCESS_COARSE_LOCATION = 1;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private GoogleApiClient googleApiClient;
    LocationManager locationManager;
    String s,q;
    //TextView t1,t2;
    boolean connected = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.main);
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        } else
            connected = false;

        if (connected == false) {

            Toast.makeText(Main.this, "Please Enable Internet", Toast.LENGTH_LONG).show();
        }
        checkLocationPermission();

        getLocation();


        googleApiClient = new GoogleApiClient.Builder(this, this, this).addApi(LocationServices.API).build();

    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            new AlertDialog.Builder(this)
                    .setTitle("WeatherPing")
                    .setMessage("Please allow the app to access Device Location")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(Main.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_LOCATION);
                            checkLocationPermission();
                        }
                    })
                    .create()
                    .show();




        } else {
            return true;
        }return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_COARSE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // All good!
                } else {
                    Toast.makeText(this, "Need your location!", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }
    @Override
    public void onConnected(Bundle bundle) {
        Log.i(Main.class.getSimpleName(), "Connected to Google Play Services!");

        if(Integer.valueOf(android.os.Build.VERSION.SDK)<23);
        {
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            lat = lastLocation.getLatitude();
            lon = lastLocation.getLongitude();
            s=String.valueOf(lat);
            q=String.valueOf(lon);
            ex();
        }


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            //t1.setText("Latitude: " + lastLocation.getLatitude() + "\n Longitude: " + lastLocation.getLongitude());

            lat = lastLocation.getLatitude();
            lon = lastLocation.getLongitude();
            //t1 = (TextView)findViewById(R.id.t);
            //t2 = (TextView)findViewById(R.id.y);


            s=String.valueOf(lat);
            q=String.valueOf(lon);
            ex();


        }
    }
    @Override
    public void onConnectionSuspended(int i) {
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(Main.class.getSimpleName(), "Can't connect to Google Play Services!");
    }


    //Check if loc is enabled
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
    public void onProviderEnabled(String provider) {

    }
    @Override
    public void onLocationChanged(Location location) {
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }
    @Override
    public void onProviderDisabled(String provider) {

        Toast.makeText(Main.this, "Please Enable Location", Toast.LENGTH_SHORT).show();
    }
    public void ex()
    {
        Typeface tf = Typeface.createFromAsset(getAssets(),
                "fonts/YanoneKaffeesatz-Thin.ttf");

        weatherFont = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/weathericons-regular-webfont.ttf");

        cityField = (TextView)findViewById(R.id.textView20);
        updatedField = (TextView)findViewById(R.id.textView2);
        detailsField = (TextView)findViewById(R.id.textView9);
        currentTemperatureField = (TextView)findViewById(R.id.textView6);
        humidity_field = (TextView)findViewById(R.id.textView3);
        pressure_field = (TextView)findViewById(R.id.textView10);
        sealevel = (TextView)findViewById(R.id.textView35);
        groundlevel = (TextView)findViewById(R.id.textView36);
        windspeed = (TextView)findViewById(R.id.textView29);
        winddeg = (TextView)findViewById(R.id.textView30);
        sun = (TextView)findViewById(R.id.textView26);
        set = (TextView)findViewById(R.id.textView27);
        pressure_field = (TextView)findViewById(R.id.textView10);
        weatherIcon = (TextView)findViewById(R.id.textView19);
        //mint = (TextView)findViewById(R.id.textView7);
        cloud = (TextView)findViewById(R.id.textView34);
        latt = (TextView)findViewById(R.id.textView37);
        //rain = (TextView)findViewById(R.id.textView22);
        weatherIcon.setTypeface(weatherFont);


        cityField.setTypeface(tf);
        // mint.setTypeface(tf);
        //updatedField.setTypeface(tf);
        detailsField.setTypeface(tf);
        currentTemperatureField.setTypeface(tf);
        humidity_field.setTypeface(tf);
        pressure_field.setTypeface(tf);
        sealevel.setTypeface(tf);
        groundlevel.setTypeface(tf);
        windspeed.setTypeface(tf);
        winddeg.setTypeface(tf);
        latt.setTypeface(tf);
        cloud.setTypeface(tf);
        sun.setTypeface(tf);
        set.setTypeface(tf);
        sealevel.setTypeface(tf);
        groundlevel.setTypeface(tf);
        //rain.setTypeface(tf);
        cloud.setTypeface(tf);






        Fetch.placeIdTask asyncTask =new Fetch.placeIdTask(new Fetch.AsyncResponse() {
            public void processFinish(String weather_city, String weather_description, String weather_temperature, String weather_humidity, String weather_pressure,String mit,String mat, String slv,String glv,String wind_sp, String wind_deg,String weather_updatedOn, String weather_iconText, String sun_rise,String sun_set,String cloudss) {


                cityField.setText(weather_city);
                updatedField.setText("Updated on: "+weather_updatedOn);
                detailsField.setText(weather_description);
                currentTemperatureField.setText(weather_temperature);
                humidity_field.setText("         "+weather_humidity);
                pressure_field.setText(weather_pressure);
                sun.setText(sun_rise);
                set.setText(sun_set);
                sealevel.setText("Sea Level: "+slv);
                groundlevel.setText("Ground Level: "+glv);
                windspeed.setText("Speed "+wind_sp);
                winddeg.setText("Direction "+wind_deg);
                //mint.setText(mit+" /"+mat);
                //rain.setText(rn);
                cloud.setText(cloudss);
                latt.setText("Lat: "+s+"    Long: "+q);
                weatherIcon.setText(Html.fromHtml(weather_iconText));

            }
        });
        asyncTask.execute(s,q); //  asyncTask.execute("Latitude", "Longitude")

    }



}


