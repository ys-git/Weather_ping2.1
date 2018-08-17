package app.ys.weather_ping21;


import android.*;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
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

//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;
//import com.google.android.gms.ads.MobileAds;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Main extends AppCompatActivity {

    public ProgressDialog pDialog;
    TextView locationText, tt1, tt2, tt3, tt4, tt5, tt6, tt7, tt8, tt9, tt10, tt11, tt12, tt13, tt14,vis_field;
    TextView te1, te2, te3, te4, te5, te6;
    ImageView img, setts, img1, img2, img3, img4, img5, img6;
    ImageView info1, info2, info3, info4, info5;
    Double con;
    String carbon, sulphur, ozone, pm_10, pm_25, nitrogen, time, board_name, dea, cap;
    String airquality, cityname, aqi, dominant;
    private SwipeRefreshLayout swipeRefreshLayout;
    TextView cityField, cloud, latt, detailsField, currentTemperatureField, humidity_field, pressure_field, weatherIcon, updatedField, windspeed, winddeg, sun, set;


    Double vis_dou;
    //private AdView mAdView;
    String vis;
    //private static final String APP_ID = "56ff39608b186e1073a21f9eeca85f67";
    //String units="metric";

    Integer k = 0;


    Typeface weatherFont;
    double lat, lon, lng;
    String s, q, celss, cel_t, pm10a;
    String tempp=null;
    SharedPreferences switches;
    Double far,far_r;
    Double cel;
    String air = "0.0", url;
    boolean connected = false;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        switches = getSharedPreferences("toggle", Context.MODE_PRIVATE);
        setContentView(R.layout.main);
        Log.i("WP", "Inside Oncreate");

        if((switches.getInt("tut", -1))==1)
        {
            Log.i("WP", "Handy Info Being displayed");
            new AlertDialog.Builder(Main.this)
                    .setTitle("A bit of handy info....")
                    .setCancelable(false)
                    .setMessage(" ?    Information Popups"+"\n"+"\n"+" ?     Swipe down to refresh")
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .create()
                    .show();
            SharedPreferences.Editor ed = switches.edit();
            ed.putInt("tut",0);
            ed.apply();
            Log.i("WP", "Inside InfoPopus");

        }

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network

            connected = true;
        } else
            connected = false;
        Log.i("WP", "Connectivity to the Internet Checked");

        if (connected == false) {

            Snackbar.make(findViewById(android.R.id.content), "Please Enable Internet", Snackbar.LENGTH_LONG)
                    .show();
            Log.i("WP", "Connectivity to the Internet Failed");
        }

        init();
        startLocationButtonClick();
        stopLocationButtonClick();



        /*MobileAds.initialize(this, "ca-app-pub-1967731466728317~5398191171");

        mAdView = findViewById(R.id.ad_view);

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("F6FD88C8AC1C935CB11EFA4E910FE1B0")
                .build();
        mAdView.loadAd(adRequest);*/


        //img=(ImageView)findViewById(R.id.rld);
        setts = (ImageView) findViewById(R.id.sett);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_to_refresh_layout);
        currentTemperatureField = (TextView) findViewById(R.id.textView6);
        tt1 = (TextView) findViewById(R.id.textView15);
        tt2 = (TextView) findViewById(R.id.textView24);
        tt3 = (TextView) findViewById(R.id.textView25);
        tt4 = (TextView) findViewById(R.id.textView31);
        tt5 = (TextView) findViewById(R.id.textView32);
        tt6 = (TextView) findViewById(R.id.textView35);
        tt7 = (TextView) findViewById(R.id.textView36);
        tt8 = (TextView) findViewById(R.id.textView37);
        tt9 = (TextView) findViewById(R.id.textView47);
        tt10 = (TextView) findViewById(R.id.textView49);
        tt11 = (TextView) findViewById(R.id.textView50);
        tt12 = (TextView) findViewById(R.id.textView52);
        tt13 = (TextView) findViewById(R.id.textView22);
        tt14 = (TextView) findViewById(R.id.textView54);
        vis_field = (TextView) findViewById(R.id.textView55);


        te1 = (TextView) findViewById(R.id.textView40);
        te2 = (TextView) findViewById(R.id.textView42);
        te3 = (TextView) findViewById(R.id.textView43);
        te4 = (TextView) findViewById(R.id.textView44);
        te5 = (TextView) findViewById(R.id.textView45);
        te6 = (TextView) findViewById(R.id.textView46);


        img1 = (ImageView) findViewById(R.id.imageView23);
        img2 = (ImageView) findViewById(R.id.imageView22);
        img3 = (ImageView) findViewById(R.id.imageView24);
        img4 = (ImageView) findViewById(R.id.imageView25);
        img5 = (ImageView) findViewById(R.id.imageView26);
        img6 = (ImageView) findViewById(R.id.imageView27);

        info1 = (ImageView) findViewById(R.id.imageView32);
        info2 = (ImageView) findViewById(R.id.imageView33);
        info3 = (ImageView) findViewById(R.id.imageView31);
        info4 = (ImageView) findViewById(R.id.imageView34);
        info5 = (ImageView) findViewById(R.id.imageView35);


        //getLocationBtn = (Button)findViewById(R.id.getLocationBtn);
        // locationText = (TextView)findViewById(R.id.locationText);


        /*if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
            Log.i("WP", "Checking for the Location permission");
        }*/


        /*Log.i("WP", "Executing getLocation");
        getLocation();
        Log.i("WP", "getLocation executed");*/


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

                Intent i = new Intent(Main.this, app.ys.weather_ping21.Settings.class);
                startActivity(i);
            }

        });
        info1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Main.this)
                        .setTitle("Info")
                        .setCancelable(true)
                        .setMessage("Since it is not possible to measure pollutant concentrations in every part of the country, the app shows the air index of the most nearby location.\n" +
                                "Sample concentrations are used to infer what pollutant concentrations \n" +
                                "are around sampling site for variety of time periods.\n" +
                                "\n" +
                                "Moreover if the Current location is way too remote or isolated, Air Index may \n" +
                                "not be available for such locations.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .create()
                        .show();
            }

        });

        info2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (k == 1) {
                    new AlertDialog.Builder(Main.this)
                            .setTitle("Good")
                            .setCancelable(true)
                            .setMessage("Air quality is considered satisfactory, and air pollution poses little or no risk.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .create()
                            .show();

                }

                if (k == 2) {
                    new AlertDialog.Builder(Main.this)
                            .setTitle("Moderate")
                            .setCancelable(true)
                            .setMessage("Air quality is acceptable; however, for some pollutants there may be a moderate health concern for a very small number of people who are unusually sensitive to air pollution.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .create()
                            .show();

                }

                if (k == 3) {
                    new AlertDialog.Builder(Main.this)
                            .setTitle("High")
                            .setCancelable(true)
                            .setMessage("Members of sensitive groups may experience health effects. The general public is not likely to be affected.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .create()
                            .show();

                }

                if (k == 4) {
                    new AlertDialog.Builder(Main.this)
                            .setTitle("Unhealthy")
                            .setCancelable(true)
                            .setMessage("Everyone may begin to experience health effects; members of sensitive groups may experience more serious health effects.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .create()
                            .show();

                }

                if (k == 5) {
                    new AlertDialog.Builder(Main.this)
                            .setTitle("Very Unhealthy")
                            .setCancelable(true)
                            .setMessage("Health warnings of emergency conditions. The entire population is more likely to be affected.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .create()
                            .show();

                }

                if (k == 6) {
                    new AlertDialog.Builder(Main.this)
                            .setTitle("Hazardous")
                            .setCancelable(true)
                            .setMessage("Health alert: everyone may experience more serious health effects.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .create()
                            .show();

                }

            }

        });
        info3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Main.this)
                        .setTitle("Info")
                        .setCancelable(true)
                        .setMessage("Pollutant which exceeds its presence with respect to other pollutants present in the atmosphere.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .create()
                        .show();
            }

        });
        info4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    if (dominant.equals("pm25") && dominant != null) {
                        new AlertDialog.Builder(Main.this)
                                .setTitle("PM₂.₅")
                                .setCancelable(true)


                                .setMessage("The term fine particles, or particulate matter 2.5 (PM₂.₅), refers to tiny particles or droplets in the air that are two and one half microns or less in width. Like inches, meters and miles, a micron is a unit of measurement for distance. There are about 25,000 microns in an inch. The widths of the larger particles in the PM₂.₅ size range would be about thirty times smaller than that of a human hair. The smaller particles are so small that several thousand of them could fit on the period at the end of this sentence.")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                })
                                .create()
                                .show();

                    } else if (dominant.equals("pm10") && dominant != null) {
                        new AlertDialog.Builder(Main.this)
                                .setTitle("PM₁₀")
                                .setCancelable(true)
                                .setMessage("Particulate matter (PM₁₀) pollution consists of very small liquid and solid particles floating in the air. Of greatest concern to public health are the particles small enough to be inhaled into the deepest parts of the lung. These particles are less than 10 microns in diameter - about 1/7th the thickness of the a human hair - and are known as PM₁₀. This includes fine particulate matter known as PM₂.₅.\n" +
                                        "\n" +
                                        "PM₁₀ is a major component of air pollution that threatens both our health and our environment.")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                })
                                .create()
                                .show();


                    } else if (dominant.equals("o3") && dominant != null) {
                        new AlertDialog.Builder(Main.this)
                                .setTitle("O₃")
                                .setCancelable(true)
                                .setMessage("Good Ozone: Ozone occurs naturally in the Earth's upper atmosphere - 6 to 30 miles above the Earth's surface - where it forms a protective layer that shields us from the sun's harmful ultraviolet rays. Manmade chemicals are known to destroy this beneficial ozone" +
                                        "\n" +
                                        "Bad Ozone: In the Earth's lower atmosphere, near ground level, ozone is formed when pollutants emitted by cars, power plants, industrial boilers, refineries, chemical plants, and other sources react chemically in the presence of sunlight. Ozone at ground level is a harmful air pollutant.")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                })
                                .create()
                                .show();


                    } else if (dominant.equals("no2") && dominant != null) {
                        new AlertDialog.Builder(Main.this)
                                .setTitle("NO₂")
                                .setCancelable(true)
                                .setMessage("Nitrogen dioxide(NO₂) is a nasty-smelling gas. Some nitrogen dioxide is formed naturally in the atmosphere by lightning and some is produced by plants, soil and water. However, only about 1% of the total amount of nitrogen dioxide found in our cities' air is formed this way.\n" +
                                        "\n" +
                                        "Nitrogen dioxide is an important air pollutant because it contributes to the formation of photochemical smog, which can have significant impacts on human health.")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                })
                                .create()
                                .show();


                    } else if (dominant.equals("so2") && dominant != null) {
                        new AlertDialog.Builder(Main.this)
                                .setTitle("SO₂")
                                .setCancelable(true)
                                .setMessage("Sulfur dioxide is the chemical compound with the formula SO₂" +
                                        "At standard atmosphere, it is a toxic gas with a pungent, irritating smell. It is released naturally by volcanic activity and is produced as a by-product of the burning of fossil fuels contaminated with sulfur compounds.")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                })
                                .create()
                                .show();


                    } else if (dominant.equals("co") && dominant != null) {
                        new AlertDialog.Builder(Main.this)
                                .setTitle("CO")
                                .setCancelable(true)
                                .setMessage("CO is a colorless, odorless gas that can be harmful when inhaled in large amounts. CO is released when something is burned. The greatest sources of CO to outdoor air are cars, trucks and other vehicles or machinery that burn fossil fuels.")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                })
                                .create()
                                .show();


                    }
                } catch (Exception e) {

                }

            }

        });
        info5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Main.this)
                        .setTitle("Why air quality monitoring is essential?")
                        .setCancelable(true)
                        .setMessage("The starting point of air quality monitoring is to first study if an area has an air pollution problem. Monitoring helps in assessing the level of pollution in relation to the ambient air quality standards. " +
                                "Standards are a regulatory measure to set the target for pollution reduction and achieve clean air \n" +
                                "Real time monitoring results will help in calculating air quality index to issue health advisories as well as for formulation of action plan to meet standards.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .create()
                        .show();
            }

        });


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i("WP", "Swiped down to refresh");
                Snackbar.make(findViewById(android.R.id.content), "Refreshing...", Snackbar.LENGTH_LONG)
                        .show();

                if (ContextCompat.checkSelfPermission(Main.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(Main.this, "Denied Location Permission", Toast.LENGTH_LONG).show();

                }
                Log.i("WP", "Again calling getLocation");
                startLocationButtonClick();
                stopLocationButtonClick();
                Log.i("WP", "Location Refreshed");

                /*AdRequest adRequest = new AdRequest.Builder()
                        .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                        .addTestDevice("F6FD88C8AC1C935CB11EFA4E910FE1B0")
                        .build();
                mAdView.loadAd(adRequest);*/

                if ((switches.getInt("Toggle2", -1)) == 1) {
                    stopService(new Intent(Main.this, ForegroundService.class));
                    Intent startIntent = new Intent(Main.this, ForegroundService.class);
                    startIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
                    startService(startIntent);
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
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
    public void onPause() {
        //if (mAdView != null) {
        //    mAdView.pause();
        //}
        super.onPause();
        if (mRequestingLocationUpdates) {
            // pausing location updates
            stopLocationUpdates();
        }
    }



    @Override
    public void onDestroy() {
        // if (mAdView != null) {
        //   mAdView.destroy();
        //}
        super.onDestroy();
    }


    @Override
    public void onResume() {
        super.onResume();
        //if (mAdView != null) {
        //  mAdView.resume();
        //}
        if(tempp!=null) {
            if ((switches.getInt("Toggle1", -1)) == 1) {

                try {
                    String temp_1 = tempp;
                    cel = Double.parseDouble(temp_1);
                    far = (cel * 1.8) + 32;
                    far_r=round(far, 1);
                    String temp_d = String.valueOf(far_r);
                    currentTemperatureField.setText(temp_d + " °F");
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

            }

            if ((switches.getInt("Toggle1", -1)) == 0) {
                {
                    currentTemperatureField.setText(tempp + " °C");
                }
            }
        }


    }
// DEPRECATED SECTION-NO LONGER WORKING
//------------------------------------------------------------------------------------------------------------------------------
//    /*void getLocation() {
//        try {Log.i("WP", "Inside getLocation");
//            Location location;
//            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 0, this);
//            if (locationManager != null) {
//                location = locationManager
//                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//                if (location != null) {
//
//                    lat = location.getLatitude();
//                    lon = location.getLongitude();
//                }
//                s = String.valueOf(lat);
//                q = String.valueOf(lon);
//                Log.i("WP", "Getting Lat nad Lon"+"Lat= "+s+"  Long= "+q);
//            }
//            final String URL = "https://api.waqi.info/feed/geo:" + lat + ";" + lon + "/?token=7b119f79e8a4e507e6f9719a1015f4ac0a0cb3d4";
//
//            ex();
//            //String url = String.format("http://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&units=%s&appid=%s",
//            //        lat, lon, units, APP_ID);
//            //new Main.GetWeatherTask().execute(url);
//            new FetchDataTask().execute(URL);
//        }
//        catch(SecurityException e) {
//            e.printStackTrace();
//        }
//        if ((switches.getInt("Toggle2",-1)) == 1) {
//            stopService(new Intent(Main.this, ForegroundService.class));
//            Intent startIntent = new Intent(Main.this, ForegroundService.class);
//            startIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
//            startService(startIntent);
//        }
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//        *//*//*/locationText.setText("Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude());
//        lat = location.getLatitude();
//        lon = location.getLongitude();
//        s = String.valueOf(lat);
//        q = String.valueOf(lon);
//        final String URL = "https://api.waqi.info/feed/geo:" + lat + ";" + lon + "/?token=7b119f79e8a4e507e6f9719a1015f4ac0a0cb3d4";
//
//        ex();
//        //String url = String.format("http://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&units=%s&appid=%s",
//        //        lat, lon, units, APP_ID);
//        //new Main.GetWeatherTask().execute(url);
//        new FetchDataTask().execute(URL);*//*
//
//
//
//    }
//
//    @Override
//    public void onProviderDisabled(String provider) {
//        provider=null;
//
//
//        if (connected == false&&provider==null) {
//
//            Snackbar.make(findViewById(android.R.id.content), "Please Enable Location and Internet", Snackbar.LENGTH_LONG)
//                    .show();
//        }
//        else
//        {
//            Snackbar.make(findViewById(android.R.id.content), "Please Enable Location", Snackbar.LENGTH_LONG)
//                    .show();
//
//        }
//    }
//
//    @Override
//    public void onStatusChanged(String provider, int status, Bundle extras) {
//
//    }
//
//    @Override
//    public void onProviderEnabled(String provider) {
//
//    }*/

    //-----------------------------------------------------------------------------------------------------------------------------
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
            s = String.valueOf(lat);
            q = String.valueOf(lon);
                Log.i("WP", "Getting Lat nad Lon"+"Lat= "+s+"  Long= "+q);

            final String URL = "https://api.waqi.info/feed/geo:" + lat + ";" + lon + "/?token=7b119f79e8a4e507e6f9719a1015f4ac0a0cb3d4";

            ex();
            //String url = String.format("http://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&units=%s&appid=%s",
            //        lat, lon, units, APP_ID);
            //new Main.GetWeatherTask().execute(url);
            new FetchDataTask().execute(URL);
            if ((switches.getInt("Toggle2",-1)) == 1) {
            stopService(new Intent(Main.this, ForegroundService.class));
            Intent startIntent = new Intent(Main.this, ForegroundService.class);
            startIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
            startService(startIntent);
        }


            // giving a blink animation on TextView
            //txtLocationResult.setAlpha(0);
            //txtLocationResult.animate().alpha(1).setDuration(300);

            // location last updated time
            //txtUpdatedOn.setText("Last updated on: " + mLastUpdateTime);
        }

        //toggleButtons();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("is_requesting_updates", mRequestingLocationUpdates);
        outState.putParcelable("last_known_location", mCurrentLocation);
        outState.putString("last_updated_on", mLastUpdateTime);

    }

    private void startLocationUpdates() {
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
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
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(Main.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);

                                Toast.makeText(Main.this, errorMessage, Toast.LENGTH_LONG).show();
                        }

                        updateLocationUI();
                    }
                });
    }


    public void startLocationButtonClick() {
        // Requesting ACCESS_FINE_LOCATION using Dexter library
        Dexter.withActivity(this)
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
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "Location updates stopped!", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.e(TAG, "User agreed to make required location settings changes.");
                        // Nothing to do. startLocationupdates() gets called in onResume again.
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.e(TAG, "User chose not to make required location settings changes.");
                        mRequestingLocationUpdates = false;
                        break;
                }
                break;
        }
    }

    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
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





    //------------------------------------------------------------------------------------------------------------------

    void ex() {
        Log.i("WP", "Inside ex");

        Typeface tf = Typeface.createFromAsset(getAssets(),
                "fonts/YanoneKaffeesatz-Thin.ttf");

        Typeface tf3 = Typeface.createFromAsset(getAssets(),
                "fonts/DINMedium.ttf");

        Typeface tf4 = Typeface.createFromAsset(getAssets(),
                "fonts/calibril.ttf");


        weatherFont = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/weathericons-regular-webfont.ttf");


        cityField = (TextView) findViewById(R.id.textView20);
        updatedField = (TextView) findViewById(R.id.textView2);
        detailsField = (TextView) findViewById(R.id.textView9);
        humidity_field = (TextView) findViewById(R.id.textView3);
        pressure_field = (TextView) findViewById(R.id.textView10);
        windspeed = (TextView) findViewById(R.id.textView29);
        winddeg = (TextView) findViewById(R.id.textView30);
        sun = (TextView) findViewById(R.id.textView26);
        set = (TextView) findViewById(R.id.textView27);
        pressure_field = (TextView) findViewById(R.id.textView10);
        weatherIcon = (TextView) findViewById(R.id.textView19);
        cloud = (TextView) findViewById(R.id.textView34);
        latt = (TextView) findViewById(R.id.textView37);
        weatherIcon.setTypeface(weatherFont);


        cityField.setTypeface(tf);
        vis_field.setTypeface(tf3);
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



        Fetch.placeIdTask asyncTask = new Fetch.placeIdTask(new Fetch.AsyncResponse() {
            public void processFinish(String weather_city, String weather_description, String weather_temperature, String weather_humidity, String weather_pressure,String vis, String wind_sp, String wind_deg, String weather_updatedOn, String weather_iconText, String sun_rise, String sun_set, String cloudss) {

                tempp = weather_temperature;
                cityField.setText(weather_city);
                updatedField.setText("Updated on: " + weather_updatedOn);
                capital(weather_description);
                currentTemperatureField.setText(weather_temperature + " °C");
                detailsField.setText(dea);
                if(vis!="No data") {
                    vis_dou = Double.parseDouble(vis);
                    vis_dou = (vis_dou / 1000);
                    String visible = String.valueOf(vis_dou);
                    vis_field.setText("Visibility: "+visible+" Km");
                }else{

                vis_field.setText("Visibility: "+vis);}

                humidity_field.setText("      " + weather_humidity);
                pressure_field.setText(weather_pressure);
                sun.setText(sun_rise);
                set.setText(sun_set);
                windspeed.setText("Speed " + wind_sp);
                winddeg.setText("Direction " + wind_deg);
                //mint.setText(mit+" /"+mat);
                //rain.setText(rn);
                cloud.setText(cloudss);
                latt.setText("Lat: " + s + "   Long: " + q);
                weatherIcon.setText(Html.fromHtml(weather_iconText));

                if ((switches.getInt("Toggle1", -1)) == 1) {
                    try {

                        cel = Double.parseDouble(weather_temperature);
                        far = (cel * 1.8) + 32;
                        far_r=round(far, 1);
                        String temp_d = String.valueOf(far_r);
                        currentTemperatureField.setText(temp_d + " °F");
                    } catch (NumberFormatException nfe) {
                        nfe.printStackTrace();
                    }

                }


                Log.i(Main.class.getSimpleName(), "Displaying Fetched data");
            }

        });
        asyncTask.execute(s, q); //  asyncTask.execute("Latitude", "Longitude")
    }

    private class FetchDataTask extends AsyncTask<String, Void, String> {

        Typeface tf = Typeface.createFromAsset(getAssets(),
                "fonts/YanoneKaffeesatz-Thin.ttf");

        Typeface tf3 = Typeface.createFromAsset(getAssets(),
                "fonts/DINMedium.ttf");

        Typeface tf4 = Typeface.createFromAsset(getAssets(),
                "fonts/calibril.ttf");

        @Override
        protected String doInBackground(String... params) {

            InputStream inputStream = null;
            String result = null;
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(params[0]);

            try {

                HttpResponse response = client.execute(httpGet);
                inputStream = response.getEntity().getContent();

                // convert inputstream to string
                if (inputStream != null) {
                    result = convertInputStreamToString(inputStream);
                    Log.i("App", "Data received:" + result);

                } else
                    result = "Failed to fetch data";

                return result;

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String dataFetched) {
            //parse the JSON data and then display
            parseJSON(dataFetched);
        }


        private String convertInputStreamToString(InputStream inputStream) throws IOException {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            String result = "";
            while ((line = bufferedReader.readLine()) != null)
                result += line;

            inputStream.close();
            return result;

        }

        private void parseJSON(String data) {

            try {


                JSONObject jsonObject = new JSONObject(data);
                JSONObject mains = jsonObject.getJSONObject("data");
                dominant = mains.getString("dominentpol");
                aqi = mains.getString("aqi");


                if (mains.has("city")) {
                    JSONObject name = mains.getJSONObject("city");
                    cityname = name.getString("name");
                    tt10.setText(cityname);
                } else {
                    tt10.setText(cityname);

                }

                if (mains.has("time")) {
                    JSONObject tm = mains.getJSONObject("time");
                    time = tm.getString("s");
                    tt12.setText("Air Index Last Updated on: \n   " + time);
                } else {
                    tt12.setText("Air Index Last Updated on: No data");
                }

                if (mains.has("iaqi")) {
                    JSONObject aqis = mains.getJSONObject("iaqi");
                    if (aqis.has("co")) {
                        JSONObject co = aqis.getJSONObject("co");
                        carbon = co.getString("v");
                        tt7.setText("CO :   " + carbon);
                    } else {
                        tt7.setText("CO : No data");
                    }

                    if (aqis.has("no2")) {
                        JSONObject no2 = aqis.getJSONObject("no2");
                        nitrogen = no2.getString("v");
                        tt5.setText("NO₂ :   " + nitrogen);
                    } else {
                        tt5.setText("NO₂ : No data");
                    }

                    if (aqis.has("o3")) {
                        JSONObject o3 = aqis.getJSONObject("o3");
                        ozone = o3.getString("v");
                        tt4.setText("O₃ :   " + ozone);
                    } else {
                        tt4.setText("O₃ : No data");
                    }

                    if (aqis.has("pm10")) {
                        JSONObject pm10 = aqis.getJSONObject("pm10");
                        pm_10 = pm10.getString("v");
                        tt3.setText("PM₁₀ :   " + pm_10);
                    } else {
                        tt3.setText("PM₁₀ : No data");
                    }

                    if (aqis.has("pm25")) {
                        JSONObject pm25 = aqis.getJSONObject("pm25");
                        pm_25 = pm25.getString("v");
                        tt2.setText("PM₂.₅ :   " + pm_25);
                    } else {
                        tt2.setText("PM₂.₅ : No data");
                    }

                    if (aqis.has("so2")) {
                        JSONObject so2 = aqis.getJSONObject("so2");
                        sulphur = so2.getString("v");
                        tt6.setText("SO₂ :   " + sulphur);
                    } else {
                        tt6.setText("SO₂ : No data");
                    }

                } else {

                }
                Log.i("WP", "Fetching Air Index");

                JSONArray jsonarray = mains.getJSONArray("attributions");
                JSONObject elearray = jsonarray.getJSONObject(0);
                board_name = elearray.getString("name");

                tt10.setTypeface(tf);
                tt11.setText("Measured by: " + board_name);
                tt11.setTypeface(tf4);
                tt13.setText(aqi);

                img1.setVisibility(View.INVISIBLE);
                img2.setVisibility(View.INVISIBLE);
                img3.setVisibility(View.INVISIBLE);
                img4.setVisibility(View.INVISIBLE);
                img5.setVisibility(View.INVISIBLE);
                img6.setVisibility(View.INVISIBLE);

                te1.setVisibility(View.INVISIBLE);
                te2.setVisibility(View.INVISIBLE);
                te3.setVisibility(View.INVISIBLE);
                te4.setVisibility(View.INVISIBLE);
                te5.setVisibility(View.INVISIBLE);
                te6.setVisibility(View.INVISIBLE);


                if (dominant.equals("pm25")) {
                    tt9.setText("PM₂.₅ :  " + pm_25);
                } else if (dominant.equals("pm10")) {
                    tt9.setText("PM₁₀ :  " + pm_10);

                } else if (dominant.equals("o3")) {
                    tt9.setText("O₃ :  " + ozone);

                } else if (dominant.equals("no2")) {
                    tt9.setText("NO₂ :  " + nitrogen);

                } else if (dominant.equals("so2")) {
                    tt9.setText("SO₂ :  " + sulphur);

                } else if (dominant.equals("co")) {
                    tt9.setText("CO :  " + carbon);

                } else {

                    tt9.setText(dominant);
                }


                tt2.setTypeface(tf3);
                tt3.setTypeface(tf3);


                tt4.setTypeface(tf3);
                tt5.setTypeface(tf3);
                tt6.setTypeface(tf3);
                tt7.setTypeface(tf3);

                String s = aqi;
                double d = Double.parseDouble(s);
                Double de = new Double(d);
                int con = de.intValue();

                if (con > 0 && con <= 50) {
                    img1.setVisibility(View.VISIBLE);
                    te1.setVisibility(View.VISIBLE);
                    te1.setText("  " + aqi);
                    te1.setTypeface(tf3);
                    tt14.setText(" Good");
                    tt13.setTextColor(Color.parseColor("#38d145"));
                    k = 1;

                } else if (con > 51 && con <= 100) {
                    img2.setVisibility(View.VISIBLE);
                    te2.setVisibility(View.VISIBLE);
                    te2.setText("  " + aqi);
                    te2.setTypeface(tf3);
                    tt14.setText(" Moderate");
                    tt13.setTextColor(Color.parseColor("#c4ff0e"));
                    k = 2;

                } else if (con > 101 && con <= 150) {
                    img3.setVisibility(View.VISIBLE);
                    te3.setVisibility(View.VISIBLE);
                    te3.setText("  " + aqi);
                    te3.setTypeface(tf3);
                    tt14.setText(" High");
                    tt13.setTextColor(Color.parseColor("#fff200"));
                    k = 3;

                } else if (con > 151 && con <= 200) {
                    img4.setVisibility(View.VISIBLE);
                    te4.setVisibility(View.VISIBLE);
                    te4.setText("  " + aqi);
                    te4.setTypeface(tf3);
                    tt14.setText(" Unhealthy");
                    tt13.setTextColor(Color.parseColor("#ff7f27"));
                    k = 4;

                } else if (con > 201 && con <= 300) {
                    img5.setVisibility(View.VISIBLE);
                    te5.setVisibility(View.VISIBLE);
                    te5.setText("  " + aqi);
                    te5.setTypeface(tf3);
                    tt14.setText(" Very\nUnhealthy");
                    tt13.setTextColor(Color.parseColor("#ec1c24"));
                    k = 5;

                } else if (con > 300) {
                    img6.setVisibility(View.VISIBLE);
                    te6.setVisibility(View.VISIBLE);
                    te6.setText("  " + aqi);
                    te6.setTypeface(tf3);
                    tt14.setText(" Hazardous");
                    tt13.setTextColor(Color.parseColor("#88001b"));
                    k = 6;

                }

                Log.i("WP", "Air Quality Fetched");


            } catch (Exception e) {
                Log.i("App", "Error parsing data" + e.getMessage());

            }
        }
    }

    void capital(String n) {
        n = " " + n;
        cap = "";
        Log.i("MyTestService", "Inside Capital");
        for (int i = 0; i < n.length(); i++) {
            char x = n.charAt(i);
            if (x == ' ') {
                cap = cap + " ";
                char y = n.charAt(i + 1);
                cap = cap + Character.toUpperCase(y);
                i++;
            } else {
                cap = cap + x;
            }
        }

        dea = cap;
    }

    public static double round(double value, int places) {

        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}



