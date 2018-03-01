package app.ys.weather_ping21;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
//import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.Set;

public class Settings extends AppCompatActivity {

    SharedPreferences sdata,switches;
    String user;
    TextView t,t2,t3,t4,t5,t6,t7,t8,te,t9;
    Switch sw,sw1,sw2;
    RadioButton rba,rbb,rbc;
    RadioGroup rg;
    Button rate,feedback;


    //private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.settings);

        //
        // te = (TextView) findViewById(R.id.textView59);
        // Initialize the Mobile Ads SDK.
        //MobileAds.initialize(this, "ca-app-pub-1967731466728317~5398191171");

        // Create the InterstitialAd and set the adUnitId.
        /*mInterstitialAd = new InterstitialAd(this);
        // Defined in res/values/strings.xml
        mInterstitialAd.setAdUnitId(getString(R.string.ad_unit_id));
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("F6FD88C8AC1C935CB11EFA4E910FE1B0")
                .build();

        mInterstitialAd.loadAd(adRequest);

        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }

            @Override
            public void onAdClosed() {
                //Toast.makeText(getApplicationContext(), "Ad is closed!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                //Toast.makeText(getApplicationContext(), "Ad failed to load! error code: " + errorCode, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLeftApplication() {
                //Toast.makeText(getApplicationContext(), "Ad left application!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdOpened() {
                //Toast.makeText(getApplicationContext(), "Ad is opened!", Toast.LENGTH_SHORT).show();
            }
        });*/


        Typeface tf = Typeface.createFromAsset(getAssets(),
                "fonts/calibril.ttf");
        sdata = getSharedPreferences("my", Context.MODE_PRIVATE);
        switches = getSharedPreferences("toggle", Context.MODE_PRIVATE);


        rate= (Button)findViewById(R.id.button2);
        feedback= (Button)findViewById(R.id.button3);

        Typeface tf3 = Typeface.createFromAsset(getAssets(),
                "fonts/DINMedium.ttf");

        user = sdata.getString("Name", "There!");
        t=(TextView)findViewById(R.id.textView7);
        t2=(TextView)findViewById(R.id.textView8);
        t3=(TextView)findViewById(R.id.textView11);
        t4=(TextView)findViewById(R.id.textView12);
        t5=(TextView)findViewById(R.id.textView14);
        t6=(TextView)findViewById(R.id.textView16);
        t7=(TextView)findViewById(R.id.textView18);
        t8=(TextView)findViewById(R.id.textView13);
        t9=(TextView)findViewById(R.id.textView58);
        rba = (RadioButton) findViewById(R.id.radioButton);
        rbb = (RadioButton) findViewById(R.id.radioButton2);
        rbc = (RadioButton) findViewById(R.id.radioButton5);
        rg = (RadioGroup)findViewById(R.id.radioGroup2);


        t.setText("Hey "+user+"!!");
        t.setTypeface(tf);
        t2.setTypeface(tf);
        t3.setTypeface(tf);
        t4.setTypeface(tf);
        t5.setTypeface(tf);
        t6.setTypeface(tf);
        t7.setTypeface(tf);
        t8.setTypeface(tf);
        t9.setTypeface(tf);


        rate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse("market://details?id=app.ys.weatherping21");
                Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(myAppLinkToMarket);
                } catch (ActivityNotFoundException e) {
                    //Toast.makeText(this, " unable to find market app", Toast.LENGTH_LONG).show();
                }
            }

            });

        feedback.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "yogendrasingh890@gmail.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback@WeatherPing");

                try {
                    startActivity(Intent.createChooser(emailIntent, null));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(Settings.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }
            }

        });





        sw=(Switch) findViewById(R.id.switch4);
        sw1=(Switch) findViewById(R.id.switch3);
        sw2=(Switch) findViewById(R.id.switch1);
        //sw3=(Switch) findViewById(R.id.switch4);

        if((switches.getInt("Toggle1", -1))==1)
        {
            sw.setChecked(true);

        }
        else
        {
            sw.setChecked(false);
        }


        //te.setText((switches.getInt("Toggle2", null)));


        if((switches.getInt("Toggle2", -1))==1)
        {
            sw1.setChecked(true);
            sw2.setEnabled(true);

        }
        else
        {
             sw1.setChecked(false);
             sw2.setChecked(false);
             sw2.setEnabled(false);
        }

        if((switches.getInt("Toggle3", -1))==1)
        {
            sw2.setChecked(true);

        }
        else
        {
            sw2.setChecked(false);
        }


        if((switches.getInt("Interval", 10))==10)
        {
            rba.setChecked(true);
            rbb.setChecked(false);
            rbc.setChecked(false);

        }
        else
        if((switches.getInt("Interval", 30))==30)
        {
            rbb.setChecked(true);
            rba.setChecked(false);
            rbc.setChecked(false);
            //sw2.setEnabled(false);
        }
        else
        if((switches.getInt("Interval", 180))==180)
        {
            rbc.setChecked(true);
            rba.setChecked(false);
            rbb.setChecked(false);
        }



        rba.setOnClickListener(new View.OnClickListener() {
            SharedPreferences.Editor ed = switches.edit();

            @Override
            public void onClick(View v) {
                ed.putInt("Interval",10);
                ed.apply();
                stopService(new Intent(Settings.this, ForegroundService.class));
                Intent startIntent = new Intent(Settings.this, ForegroundService.class);
                startIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
                startService(startIntent);
                Snackbar.make(findViewById(android.R.id.content), "Notification refresh interval set to 10 mins", Snackbar.LENGTH_LONG)
                        .show();

            }
        });

        rbb.setOnClickListener(new View.OnClickListener() {
            SharedPreferences.Editor ed = switches.edit();

            @Override
            public void onClick(View v) {
                ed.putInt("Interval",30);
                ed.apply();
                stopService(new Intent(Settings.this, ForegroundService.class));
                Intent startIntent = new Intent(Settings.this, ForegroundService.class);
                startIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
                startService(startIntent);
                Snackbar.make(findViewById(android.R.id.content), "Notification refresh interval set to 30 mins", Snackbar.LENGTH_LONG)
                        .show();

            }
        });

        rbc.setOnClickListener(new View.OnClickListener() {
            SharedPreferences.Editor ed = switches.edit();

            @Override
            public void onClick(View v) {
                ed.putInt("Interval",180);
                ed.apply();
                stopService(new Intent(Settings.this, ForegroundService.class));
                Intent startIntent = new Intent(Settings.this, ForegroundService.class);
                startIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
                startService(startIntent);
                Snackbar.make(findViewById(android.R.id.content), "Notification refresh interval set to 3 hrs", Snackbar.LENGTH_LONG)
                        .show();

            }
        });



        sw.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton toggleButton, boolean isChecked)
            {
                SharedPreferences.Editor ed = switches.edit();
                if(isChecked)
                {

                    //Toast.makeText(Settings.this, "Toggle button is on", Toast.LENGTH_SHORT).show();

                    ed.putInt("Toggle1",1);
                    ed.apply();
                    Snackbar.make(findViewById(android.R.id.content), "Fahrenheit Selected", Snackbar.LENGTH_LONG)
                            .show();

                }

                else
                {
                    //Toast.makeText(Settings.this, "Toggle button is off", Toast.LENGTH_SHORT).show();

                    ed.putInt("Toggle1",0);
                    ed.apply();
                    Snackbar.make(findViewById(android.R.id.content), "Celsius Selected", Snackbar.LENGTH_LONG)
                            .show();
                }

            }
        });


        sw1.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton toggleButton, boolean isChecked)
            {SharedPreferences.Editor ed = switches.edit();
                if(isChecked) {
                    ed.putInt("Toggle2", 1);
                    ed.apply();
                    rba.setEnabled(true);
                    rbb.setEnabled(true);
                    rbc.setEnabled(true);
                    sw2.setEnabled(true);
                    Snackbar.make(findViewById(android.R.id.content), "Notification On", Snackbar.LENGTH_LONG)
                            .show();
                    Intent startIntent = new Intent(Settings.this, ForegroundService.class);
                    startIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
                    startService(startIntent);

                }


                else
                {
                    ed.putInt("Toggle2",0);
                    ed.putInt("Toggle3",0);
                    ed.apply();
                    sw2.setChecked(false);
                    sw2.setEnabled(false);
                    rba.setEnabled(false);
                    rbb.setEnabled(false);
                    rbc.setEnabled(false);

                    Snackbar.make(findViewById(android.R.id.content), "Notification Off", Snackbar.LENGTH_LONG)
                            .show();
                    stopService(new Intent(Settings.this, ForegroundService.class));
                }

            }
        });


        sw2.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener()
        {
            SharedPreferences.Editor ed = switches.edit();

            @Override
            public void onCheckedChanged(CompoundButton toggleButton, boolean isChecked)
            {
                if(isChecked)
                {
                    ed.putInt("Toggle3",1);
                    ed.apply();
                    Snackbar.make(findViewById(android.R.id.content), "Start on Boot selected", Snackbar.LENGTH_LONG)
                            .show();
                }









                else
                {
                    ed.putInt("Toggle3",0);
                    ed.apply();
                    Snackbar.make(findViewById(android.R.id.content), "Start on Boot denied", Snackbar.LENGTH_LONG)
                            .show();
                }

            }
        });



    }


    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }


    @Override
    public void onResume() {
        super.onResume();
    }

    /*private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }*/



}
