package app.ys.weather_ping21;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.ActionBar;

//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;
//import com.google.android.gms.ads.MobileAds;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Intro extends AppCompatActivity implements LocationListener {

    Button next;
    public Double lat;
    public  Double lon;
    EditText e1,e2;
    String name;
    String email;
    RadioGroup radioGroup;
    TextView t1,t2,t3,t4;
    RadioButton rb1,rb2;
    Integer k;
    RadioButton he,she;
    String gender;
    private ProgressDialog pDialog;



    LocationManager locationManager;
    String DataParseUrl = "https://ysgeek1x.000webhostapp.com/insert_data.php";

    SharedPreferences sdata,switches;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final int PERMISSION_ACCESS_COARSE_LOCATION = 1;
    //private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.intro);
        ActionBar myActionBar = getSupportActionBar();
        myActionBar.hide();
        getLocation();
        Typeface tf = Typeface.createFromAsset(getAssets(),
                "fonts/YanoneKaffeesatz-Thin.ttf");

        /*MobileAds.initialize(this, "ca-app-pub-1967731466728317~5398191171");

        mAdView = findViewById(R.id.ad_view);

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("F6FD88C8AC1C935CB11EFA4E910FE1B0")
                .build();
        mAdView.loadAd(adRequest);*/



        sdata= getSharedPreferences("my",Context.MODE_PRIVATE);
        switches = getSharedPreferences("toggle", Context.MODE_PRIVATE);

        SharedPreferences.Editor ed = switches.edit();
        ed.putInt("Toggle2",1);
        ed.putInt("Toggle3",1);
        ed.putInt("Interval",30);
        ed.putInt("tut",1);
        ed.apply();


        next = (Button)findViewById(R.id.button);
        e1=(EditText)findViewById(R.id.editText);
        e2=(EditText)findViewById(R.id.editText2);
        t1=(TextView) findViewById(R.id.textView);
        t2=(TextView) findViewById(R.id.textView4);
        t3=(TextView) findViewById(R.id.textView5);
        rb1=(RadioButton)findViewById(R.id.radioButton3);
        rb2=(RadioButton)findViewById(R.id.radioButton4);
        he = (RadioButton) findViewById(R.id.radioButton3);
        she = (RadioButton) findViewById(R.id.radioButton4);

        next.setTypeface(tf);
        t1.setTypeface(tf);
        t2.setTypeface(tf);
        t3.setTypeface(tf);
        rb1.setTypeface(tf);
        rb2.setTypeface(tf);


        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if(checkedId == R.id.radioButton3) {
                    //Toast.makeText(getApplicationContext(), "choice: A",
                    //      Toast.LENGTH_SHORT).show();
                } else if(checkedId == R.id.radioButton4) {
                    //Toast.makeText(getApplicationContext(), "choice: B",
                    //      Toast.LENGTH_SHORT).show();
                }
            }

        });


        //if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        // ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        //}
        checkLocationPermission();
        name =  e1.getText().toString();
        email =  e2.getText().toString();


        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (e1.getText().toString().trim().length() == 0) {
                    e1.startAnimation(shakeError());
                    e1.setError("Enter Name");
                }
                else
                if (e2.getText().toString().trim().length() == 0) {
                    e2.startAnimation(shakeError());
                    e2.setError("Enter Email");

                }
                else
                if(isValidEmailAddress(e2.getText().toString())==false){
                    e2.setError("Enter a valid email");
                }
                else
                if (radioGroup.getCheckedRadioButtonId() == -1)
                {
                    // no radio buttons are checked
                    //Toast.makeText(getApplicationContext(),"Please select Gender",
                           // Toast.LENGTH_SHORT).show();
                    Snackbar.make(findViewById(android.R.id.content), "Please Select Gender", Snackbar.LENGTH_LONG)
                            .show();

                }
                else
                {
                    if (he.isChecked()) {
                        gender="Male";
                    } else if (she.isChecked()) {
                        gender="Female";
                    }
                    name =  e1.getText().toString();
                    email =  e2.getText().toString();

                    SendDataToServer(name, email, gender);
                    SharedPreferences.Editor editor = sdata.edit();
                    //editor.clear();
                    editor.putString("Name",name);
                    editor.putString("Email", email);
                    editor.apply();



                }
            }


        });
    }


    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            new AlertDialog.Builder(this)
                    .setTitle("WeatherPing")
                    .setCancelable(false)
                    .setMessage("Please allow the app to access Device Location")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(Intro.this,
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
        /*locationText.setText("Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude());*/
        //lat=location.getLatitude();
        //lon=location.getLongitude();

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

        Toast.makeText(Intro.this, "Please Enable Location", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }
    public TranslateAnimation shakeError() {
        TranslateAnimation shake = new TranslateAnimation(-1, 7, 0, 0);
        shake.setDuration(500);
        shake.setInterpolator(new CycleInterpolator(7));
        return shake;
    }
    public void SendDataToServer(final String name, final String email, final String gender){
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pDialog = new ProgressDialog(Intro.this);
                pDialog.setMessage("Patience is bitter....");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(false);
                pDialog.show();
            }

            @Override
            protected String doInBackground(String... params) {

                String QuickName = name ;
                String QuickEmail = email ;
                String QuickGender = gender;

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("name", QuickName));
                nameValuePairs.add(new BasicNameValuePair("email", QuickEmail));
                nameValuePairs.add(new BasicNameValuePair("gender", QuickGender));

                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(DataParseUrl);

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpClient.execute(httpPost);

                    HttpEntity entity = response.getEntity();


                } catch (ClientProtocolException e) {

                } catch (IOException e) {

                }
                return "Data Submit Successfully";
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                pDialog.dismiss();

                Intent i= new Intent(Intro.this,Main.class);
                i.putExtra("lat",lat);
                i.putExtra("lon",lon);
                startActivity(i);
                finish();

                //Toast.makeText(Intro.this, "Hello "+name+"!", Toast.LENGTH_LONG).show();

            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(name, email, gender);
    }

    public static boolean isValidEmailAddress(String emailAddress) {
        String emailRegEx;
        Pattern pattern;
        // Regex for a valid email address
        emailRegEx = "^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,4}$";
        // Compare the regex with the email address
        pattern = Pattern.compile(emailRegEx);
        Matcher matcher = pattern.matcher(emailAddress);
        if (!matcher.find()) {
            return false;
        }
        return true;
    }
}

