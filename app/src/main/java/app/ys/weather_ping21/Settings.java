package app.ys.weather_ping21;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class Settings extends AppCompatActivity {

    SharedPreferences sdata;
    String user;
    TextView t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.settings);
        sdata = getSharedPreferences("my", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sdata.edit();
        user = sdata.getString("Name", "There!");
        t=(TextView)findViewById(R.id.textView7);
        t.setText("Hey "+user+"!!");



    }
}
