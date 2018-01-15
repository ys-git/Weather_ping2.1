package app.ys.weather_ping21;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class Main extends AppCompatActivity implements LocationListener {

    Button getLocationBtn;
    public ProgressDialog pDialog;
    TextView locationText;
    ImageView img,setts;
    TextView cityField,cloud,latt,detailsField, currentTemperatureField, humidity_field, pressure_field, weatherIcon, updatedField,windspeed,winddeg,sun,set;

    LocationManager locationManager;
    Typeface weatherFont;
    double lat,lon;
    String s,q,celss,cel_t;
    String tempp;
    SharedPreferences switches;
    Double far;
    Double cel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        switches = getSharedPreferences("toggle", Context.MODE_PRIVATE);
        setContentView(R.layout.main);

        img=(ImageView)findViewById(R.id.rld);
        setts=(ImageView)findViewById(R.id.sett);
        currentTemperatureField = (TextView)findViewById(R.id.textView6);

        //getLocationBtn = (Button)findViewById(R.id.getLocationBtn);
        // locationText = (TextView)findViewById(R.id.locationText);


        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }



        getLocation();

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pDialog = new ProgressDialog(Main.this);
                pDialog.setMessage("Refreshing..."); // Setting Message
                pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                pDialog.show(); // Display Progress Dialog
                pDialog.setCancelable(false);
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            getLocation();
                            Thread.sleep(500);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        pDialog.dismiss();
                    }
                }).start();
                ey();
            }
        });

        setts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i= new Intent(Main.this,Settings.class);
                startActivity(i);
            }

        });

    }
    @Override
    public void onResume() {
        super.onResume();

        if ((switches.getString("Toggle1", null)) == "On") {
            try{
                //cel = Integer.parseInt(tempp);
                String temp_1=tempp;
                cel = Double.parseDouble(temp_1);
                far = (cel*1.8)+32;
                Double de = new Double(far);
                int i = de.intValue();
                celss = Integer.toString(i);
                cel_t = String.valueOf(celss);
                currentTemperatureField.setText(cel_t+" °F");
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
            }

        }

        if ((switches.getString("Toggle1", null)) == "Off") {
            {
                currentTemperatureField.setText(tempp+" °C");
            }
        }

        if((switches.getString("Toggle3", null))=="On"||(switches.getString("Toggle3", null))=="Off")
        {
            Intent startIntent = new Intent(Main.this, ForegroundService.class);
            startIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
            startService(startIntent);

        }


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
        //locationText.setText("Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude());
        lat=location.getLatitude();
        lon=location.getLongitude();
        s=String.valueOf(lat);
        q=String.valueOf(lon);

        /*try {+
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            locationText.setText(locationText.getText() + "\n"+addresses.get(0).getAddressLine(0)+", "+
                    addresses.get(0).getAddressLine(1)+", "+addresses.get(0).getAddressLine(2));
        }catch(Exception e)
        {

        }*/
        ex();

    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(Main.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    void ex()
    {
        Typeface tf = Typeface.createFromAsset(getAssets(),
                "fonts/YanoneKaffeesatz-Thin.ttf");

        weatherFont = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/weathericons-regular-webfont.ttf");

        if((switches.getString("Toggle2", null))=="On")
        {
            Intent startIntent = new Intent(Main.this, ForegroundService.class);
            startIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
            startService(startIntent);
        }

        cityField = (TextView)findViewById(R.id.textView20);
        updatedField = (TextView)findViewById(R.id.textView2);
        detailsField = (TextView)findViewById(R.id.textView9);

        humidity_field = (TextView)findViewById(R.id.textView3);
        pressure_field = (TextView)findViewById(R.id.textView10);
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
        updatedField.setTypeface(tf);
        detailsField.setTypeface(tf);
        currentTemperatureField.setTypeface(tf);
        //humidity_field.setTypeface(tf);
        //pressure_field.setTypeface(tf);
        //windspeed.setTypeface(tf);
        //winddeg.setTypeface(tf);
        //latt.setTypeface(tf);
        //cloud.setTypeface(tf);
        //sun.setTypeface(tf);
        //set.setTypeface(tf);
        //rain.setTypeface(tf);
        //cloud.setTypeface(tf);





        Fetch.placeIdTask asyncTask =new Fetch.placeIdTask(new Fetch.AsyncResponse() {
            public void processFinish(String weather_city, String weather_description, String weather_temperature, String weather_humidity, String weather_pressure,String wind_sp, String wind_deg,String weather_updatedOn, String weather_iconText, String sun_rise,String sun_set,String cloudss) {

                tempp=weather_temperature;
                cityField.setText(weather_city);
                updatedField.setText("Updated on: "+weather_updatedOn);
                detailsField.setText(weather_description);
                currentTemperatureField.setText(weather_temperature+" °C");
                humidity_field.setText("         "+weather_humidity);
                pressure_field.setText(weather_pressure);
                sun.setText(sun_rise);
                set.setText(sun_set);
                windspeed.setText("Speed "+wind_sp);
                winddeg.setText("Direction "+wind_deg);
                //mint.setText(mit+" /"+mat);
                //rain.setText(rn);
                cloud.setText(cloudss);
                latt.setText("Lat: "+s+"   Long: "+q);
                weatherIcon.setText(Html.fromHtml(weather_iconText));



            }
        });
        asyncTask.execute(s,q); //  asyncTask.execute("Latitude", "Longitude")
    }

    void ey()
    {
        switches = getSharedPreferences("toggle", Context.MODE_PRIVATE);
        if ((switches.getString("Toggle1", null)) == "On") {
            try{
                //cel = Integer.parseInt(tempp);
                cel = Double.parseDouble(tempp);
                far = (cel*1.8)+32;
                Double de = new Double(far);
                int i = de.intValue();
                celss = Integer.toString(i);
                cel_t = String.valueOf(celss);
                currentTemperatureField.setText(cel_t+" °F");
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace();
            }

        }

        if ((switches.getString("Toggle1", null)) == "Off") {
            {
                currentTemperatureField.setText(tempp+" °C");
            }
        }
    }
}



