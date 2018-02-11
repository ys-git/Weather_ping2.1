package app.ys.weather_ping21;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class BReceiver extends BroadcastReceiver {
    String TAG = "MyReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences switches = context.getSharedPreferences("toggle", Context.MODE_PRIVATE);
        if ((switches.getInt("Toggle3", -1)) == 1){
            if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
                //boot has completed, now time to start our background service.
                Log.wtf(TAG, "Device booted up!  Inside Receiver");
                Intent service = new Intent(context, ForegroundService.class);
                service.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
                context.startService(service);
                Log.wtf(TAG, "Intent Created");
            }
        }
    }
}
