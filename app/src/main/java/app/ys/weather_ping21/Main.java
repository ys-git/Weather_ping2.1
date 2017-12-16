package app.ys.weather_ping21;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
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


    TextView cityField, detailsField, currentTemperatureField, humidity_field, pressure_field, weatherIcon, updatedField;

    Typeface weatherFont;
    double lat,lon;
    private static final int PERMISSION_ACCESS_COARSE_LOCATION = 1;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private GoogleApiClient googleApiClient;
    LocationManager locationManager;
    String s,q;
    //TextView t1,t2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.main);
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
            //t1.setText(s);
            //t2.setText(q);

            weatherFont = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/weathericons-regular-webfont.ttf");

            cityField = (TextView)findViewById(R.id.textView8);
            //updatedField = (TextView)findViewById(R.id.updated_field);
            //detailsField = (TextView)findViewById(R.id.details_field);
            //currentTemperatureField = (TextView)findViewById(R.id.current_temperature_field);
            //humidity_field = (TextView)findViewById(R.id.humidity_field);
            //pressure_field = (TextView)findViewById(R.id.pressure_field);
            //weatherIcon = (TextView)findViewById(R.id.weather_icon);
            //weatherIcon.setTypeface(weatherFont);



            Fetch.placeIdTask asyncTask =new Fetch.placeIdTask(new Fetch.AsyncResponse() {
                public void processFinish(String weather_city, String weather_description, String weather_temperature, String weather_humidity, String weather_pressure, String weather_updatedOn, String weather_iconText, String sun_rise) {

                    cityField.setText(weather_city);
                    //updatedField.setText(weather_updatedOn);
                    //detailsField.setText(weather_description);
                    //currentTemperatureField.setText(weather_temperature);
                    //humidity_field.setText("Humidity: "+weather_humidity);
                    //pressure_field.setText("Pressure: "+weather_pressure);
                    //weatherIcon.setText(Html.fromHtml(weather_iconText));

                }
            });
            asyncTask.execute(s,q); //  asyncTask.execute("Latitude", "Longitude")


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



}


