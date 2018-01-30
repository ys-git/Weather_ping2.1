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
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;

public class Main extends AppCompatActivity implements LocationListener {

    Button getLocationBtn;
    public ProgressDialog pDialog;
    TextView locationText,tt1,tt2;
    ImageView img,setts;
    String airquality,cityname;
    private SwipeRefreshLayout swipeRefreshLayout;
    TextView cityField,cloud,latt,detailsField, currentTemperatureField, humidity_field, pressure_field, weatherIcon, updatedField,windspeed,winddeg,sun,set;

    LocationManager locationManager;
    String token="7b119f79e8a4e507e6f9719a1015f4ac0a0cb3d4";

    Typeface weatherFont;
    double lat,lon,lng;
    String s,q,celss,cel_t;
    String tempp;
    SharedPreferences switches;
    Double far;
    Double cel;
    String air="0.0",url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        switches = getSharedPreferences("toggle", Context.MODE_PRIVATE);
        setContentView(R.layout.main);

        //img=(ImageView)findViewById(R.id.rld);
        setts=(ImageView)findViewById(R.id.sett);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_to_refresh_layout);
        currentTemperatureField = (TextView)findViewById(R.id.textView6);
        //tt1=(TextView)findViewById(R.id.textView15);
        tt2=(TextView)findViewById(R.id.textView23);

        //getLocationBtn = (Button)findViewById(R.id.getLocationBtn);
        // locationText = (TextView)findViewById(R.id.locationText);


        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }



        getLocation();

        /*img.setOnClickListener(new View.OnClickListener() {
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
        });*/

        setts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i= new Intent(Main.this,Settings.class);
                startActivity(i);
            }

        });



        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Snackbar.make(findViewById(android.R.id.content), "Refreshing...", Snackbar.LENGTH_LONG)
                        .show();
                getLocation();
                if((switches.getString("Toggle2", null))=="On")
                {
                    stopService(new Intent(Main.this, ForegroundService.class));
                    Intent startIntent = new Intent(Main.this, ForegroundService.class);
                    startIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
                    startService(startIntent);
                }
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        Snackbar.make(findViewById(android.R.id.content), "Refreshed", Snackbar.LENGTH_LONG)
                                .show();
                    }
                }, 700);

            }

        });
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.colorAccent, android.R.color.holo_blue_light);


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


    }


    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();

        }
        if((switches.getString("Toggle2", null))=="On")
        {
            stopService(new Intent(Main.this, ForegroundService.class));
            Intent startIntent = new Intent(Main.this, ForegroundService.class);
            startIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
            startService(startIntent);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        //locationText.setText("Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude());
        lat=location.getLatitude();
        lon=location.getLongitude();
        s=String.valueOf(lat);
        lon=lng;




        //url = ("https://api.waqi.info/feed/geo:"+lat+";"+lon+"/?token=7b119f79e8a4e507e6f9719a1015f4ac0a0cb3d4");

        url =new String("https://api.waqi.info/feed/geo:"+lat+";"+lon+"/?token=7b119f79e8a4e507e6f9719a1015f4ac0a0cb3d4");




        /*try {+
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            locationText.setText(locationText.getText() + "\n"+addresses.get(0).getAddressLine(0)+", "+
                    addresses.get(0).getAddressLine(1)+", "+addresses.get(0).getAddressLine(2));
        }catch(Exception e)
        {

        }*/
        ex();
        new GetAirQuality();

    }

    @Override
    public void onProviderDisabled(String provider) {
        //Toast.makeText(Main.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
        Snackbar.make(findViewById(android.R.id.content), "Please Enable Location", Snackbar.LENGTH_LONG)
                .show();
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

        Typeface tf3 = Typeface.createFromAsset(getAssets(),
                "fonts/DINMedium.ttf");

        Typeface tf4 = Typeface.createFromAsset(getAssets(),
                "fonts/calibril.ttf");

        weatherFont = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/weathericons-regular-webfont.ttf");



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
        detailsField.setTypeface(tf3);
        currentTemperatureField.setTypeface(tf4);
        humidity_field.setTypeface(tf3);
        pressure_field.setTypeface(tf3);
        windspeed.setTypeface(tf3);
        winddeg.setTypeface(tf3);
        latt.setTypeface(tf3);
        cloud.setTypeface(tf3);
        sun.setTypeface(tf3);
        set.setTypeface(tf3);
        //rain.setTypeface(tf);






        Fetch.placeIdTask asyncTask =new Fetch.placeIdTask(new Fetch.AsyncResponse() {
            public void processFinish(String weather_city, String weather_description, String weather_temperature, String weather_humidity, String weather_pressure,String wind_sp, String wind_deg,String weather_updatedOn, String weather_iconText, String sun_rise,String sun_set,String cloudss) {

                tempp=weather_temperature;
                cityField.setText(weather_city);
                updatedField.setText("Updated on: "+weather_updatedOn);
                detailsField.setText(weather_description);
                currentTemperatureField.setText(weather_temperature+" °C");
                humidity_field.setText("      "+weather_humidity);
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


    private class GetAirQuality extends AsyncTask<String, Void, String> {


        public GetAirQuality() {

        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                Log.i("Air", "Inside AirQuality");
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder builder = new StringBuilder();

                String inputString;
                while ((inputString = bufferedReader.readLine()) != null) {
                    builder.append(inputString);
                }

                JSONObject airq = new JSONObject(builder.toString());
                //JSONObject data = airq.getJSONObject("data");
               /* JSONObject iaqi = airq.getJSONObject("iaqi");
                JSONObject noo = airq.getJSONObject("iaqi");
                JSONObject ooo = airq.getJSONObject("iaqi");
                JSONObject pmten = airq.getJSONObject("iaqi");
                JSONObject pmtwofive = airq.getJSONObject("iaqi");
                JSONObject soo = airq.getJSONObject("iaqi");
                JSONObject time = airq.getJSONObject("time");*/


                //airquality = data.getString("aqi");
                cityname = airq.getString("status");
                //Carbonmono=iaqi.getString("")
                //nitrogen=noo.getString("v");
                //o3=ooo.getString("v");
                //pm10=pmten.getString("v");
                //pm25=pmtwofive.getString("v");
               // so2=soo.getString("v");
                //tt1.setText(airquality);
                tt2.setText(cityname);

                urlConnection.disconnect();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return air;
        }

        @Override
        protected void onPostExecute(String temp) {

        }
    }
}



