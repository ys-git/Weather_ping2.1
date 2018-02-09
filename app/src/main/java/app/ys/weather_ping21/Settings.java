package app.ys.weather_ping21;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.Set;

public class Settings extends AppCompatActivity {

    SharedPreferences sdata,switches,updateint;
    String user;
    TextView t,t2,t3,t4,t5,t6,t7,t8;
    Switch sw,sw1,sw2;
    RadioButton rba,rbb,rbc;
    RadioGroup rg;
    Button rate;
    private AdView mAdView1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.settings);
        Typeface tf = Typeface.createFromAsset(getAssets(),
                "fonts/calibril.ttf");
        sdata = getSharedPreferences("my", Context.MODE_PRIVATE);
        switches = getSharedPreferences("toggle", Context.MODE_PRIVATE);
        updateint = getSharedPreferences("update", Context.MODE_PRIVATE);

        rate= (Button)findViewById(R.id.button2);



        MobileAds.initialize(this,"ca-app-pub-1967731466728317~5398191171");

        // Gets the ad view defined in layout/ad_fragment.xml with ad unit ID set in
        // values/strings.xml.
        mAdView1 = findViewById(R.id.ad_view);


        // Create an ad request. Check your logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("F6FD88C8AC1C935CB11EFA4E910FE1B0")
                .build();

        // Start loading the ad in the background.
        mAdView1.loadAd(adRequest);

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


        rate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse("market://details?id=ys.weather_ping");
                Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(myAppLinkToMarket);
                } catch (ActivityNotFoundException e) {
                    //Toast.makeText(this, " unable to find market app", Toast.LENGTH_LONG).show();
                }
            }

            });





        sw=(Switch) findViewById(R.id.switch4);
        sw1=(Switch) findViewById(R.id.switch3);
        //sw2=(Switch) findViewById(R.id.switch1);
        //sw3=(Switch) findViewById(R.id.switch4);

        if((switches.getString("Toggle1", null))=="On")
        {
            sw.setChecked(true);

        }
        else
        {
            sw.setChecked(false);
        }


        if((switches.getString("Toggle2", null))=="On")
        {
            sw1.setChecked(true);

        }
        else
        {
            sw1.setChecked(false);
            //sw2.setEnabled(false);
        }

        /*if((switches.getString("Toggle3", null))=="On")
        {
            sw2.setChecked(true);

        }
        else
        {
            sw2.setChecked(false);
        }*/


        if((updateint.getString("Interval", null))=="10")
        {
            rba.setChecked(true);
            rbb.setChecked(false);
            rbc.setChecked(false);

        }
        else
        if((updateint.getString("Interval", null))=="30")
        {
            rbb.setChecked(true);
            rba.setChecked(false);
            rbc.setChecked(false);
            //sw2.setEnabled(false);
        }
        else
        if((updateint.getString("Interval", null))=="180")
        {
            rbc.setChecked(true);
            rba.setChecked(false);
            rbb.setChecked(false);
        }



        rba.setOnClickListener(new View.OnClickListener() {
            SharedPreferences.Editor nd = updateint.edit();

            @Override
            public void onClick(View v) {
                nd.putString("Interval","10");
                nd.apply();
                stopService(new Intent(Settings.this, ForegroundService.class));
                Intent startIntent = new Intent(Settings.this, ForegroundService.class);
                startIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
                startService(startIntent);
                Snackbar.make(findViewById(android.R.id.content), "Notification refresh interval set to 10 mins", Snackbar.LENGTH_LONG)
                        .show();

            }
        });

        rbb.setOnClickListener(new View.OnClickListener() {
            SharedPreferences.Editor nd = updateint.edit();

            @Override
            public void onClick(View v) {
                nd.putString("Interval","30");
                nd.apply();
                stopService(new Intent(Settings.this, ForegroundService.class));
                Intent startIntent = new Intent(Settings.this, ForegroundService.class);
                startIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
                startService(startIntent);
                Snackbar.make(findViewById(android.R.id.content), "Notification refresh interval set to 30 mins", Snackbar.LENGTH_LONG)
                        .show();

            }
        });

        rbc.setOnClickListener(new View.OnClickListener() {
            SharedPreferences.Editor nd = updateint.edit();

            @Override
            public void onClick(View v) {
                nd.putString("Interval","180");
                nd.apply();
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

                    ed.putString("Toggle1","On");
                    ed.apply();
                    Snackbar.make(findViewById(android.R.id.content), "Fahrenheit Selected", Snackbar.LENGTH_LONG)
                            .show();

                }

                else
                {
                    //Toast.makeText(Settings.this, "Toggle button is off", Toast.LENGTH_SHORT).show();

                    ed.putString("Toggle1","Off");
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
                    ed.putString("Toggle2", "On");
                    ed.putString("Toggle2en", "On");
                    ed.apply();
                    rba.setEnabled(true);
                    rbb.setEnabled(true);
                    rbc.setEnabled(true);
                    Snackbar.make(findViewById(android.R.id.content), "Notification On", Snackbar.LENGTH_LONG)
                            .show();
                    Intent startIntent = new Intent(Settings.this, ForegroundService.class);
                    startIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
                    startService(startIntent);

                }

                else
                {
                    ed.putString("Toggle2","Off");
                    ed.apply();
                    rba.setEnabled(false);
                    rbb.setEnabled(false);
                    rbc.setEnabled(false);

                    Snackbar.make(findViewById(android.R.id.content), "Notification Off", Snackbar.LENGTH_LONG)
                            .show();
                    ed.putString("Toggle3","Off");
                    ed.apply();
                    stopService(new Intent(Settings.this, ForegroundService.class));
                }

            }
        });


        /*sw2.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener()
        {
            SharedPreferences.Editor ed = switches.edit();

            @Override
            public void onCheckedChanged(CompoundButton toggleButton, boolean isChecked)
            {
                if(isChecked)
                {
                    ed.putString("Toggle3","On");
                    ed.apply();
                    Snackbar.make(findViewById(android.R.id.content), "Dark Notification", Snackbar.LENGTH_LONG)
                            .show();
                }

                else
                {
                    ed.putString("Toggle3","Off");
                    ed.apply();
                    Snackbar.make(findViewById(android.R.id.content), "Light Notification", Snackbar.LENGTH_LONG)
                            .show();
                }

            }
        });*/



    }

    @Override
    public void onPause() {
        if (mAdView1 != null) {
            mAdView1.pause();
        }
        super.onPause();
    }


    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        if (mAdView1 != null) {
            mAdView1.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mAdView1 != null) {
            mAdView1.resume();
        }
    }
}
