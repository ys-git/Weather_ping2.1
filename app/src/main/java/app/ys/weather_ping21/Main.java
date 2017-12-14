package app.ys.weather_ping21;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;

/**
 * Created by YS on 14-12-2017.
 */

public class Main extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar myActionBar = getSupportActionBar();
        myActionBar.hide();

    }
}

