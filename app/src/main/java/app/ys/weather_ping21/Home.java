package app.ys.weather_ping21;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by YS on 11-12-2017.
 */

public class Home extends AppCompatActivity implements LocationListener {
    SharedPreferences sdata;
    TextView t1,t2;
    String s,q;
    public Double lat,lon;
    LocationManager locationManager;
    TextView city, details, temp, hum, pre, weatherIcon, updated;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        //t1=(TextView)findViewById(R.id.textView3);
        //t2=(TextView)findViewById(R.id.textView2);
        getLocation();
        /*city = (TextView)findViewById(R.id.city_field);
        updated = (TextView)findViewById(R.id.updated_field);
        details = (TextView)findViewById(R.id.details_field);
        temp = (TextView)findViewById(R.id.current_temperature_field);
        hum = (TextView)findViewById(R.id.humidity_field);
        pre = (TextView)findViewById(R.id.pressure_field);
        weatherIcon = (TextView)findViewById(R.id.weather_icon);
        weatherIcon.setTypeface(weatherFont);*/


        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);


        }
        s=String.valueOf(lat);
        q=String.valueOf(lon);

        Fetch.placeIdTask asyncTask =new Fetch.placeIdTask(new Fetch.AsyncResponse() {
            public void processFinish(String weather_city, String weather_description, String weather_temperature, String weather_humidity, String weather_pressure, String weather_updatedOn, String weather_iconText, String sun_rise) {

                city.setText(weather_city);
                updated.setText(weather_updatedOn);
                details.setText(weather_description);
                temp.setText(weather_temperature);
                hum.setText("Humidity: "+weather_humidity);
                pre.setText("Pressure: "+weather_pressure);
                weatherIcon.setText(Html.fromHtml(weather_iconText));


            }
        });
        asyncTask.execute(s, q);

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
        t1.setText("Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude());
        lat=location.getLatitude();
        lon=location.getLongitude();
        /*try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            locationText.setText(locationText.getText() + "\n"+addresses.get(0).getAddressLine(0)+", "+
                    addresses.get(0).getAddressLine(1)+", "+addresses.get(0).getAddressLine(2));
        }catch(Exception e)
        {
        }*/
    }

    @Override
    public void onProviderDisabled(String provider) {
        /*Toast toast = Toast.makeText(getApplicationContext(),
                "Custom toast background color",
                Toast.LENGTH_SHORT);

        View toastView = toast.getView();
        toastView.setBackgroundResource(R.drawable.toast_drawable);
        toast.show();*/

        Toast.makeText(Home.this, "Please Enable Location and restart the app", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }
}


