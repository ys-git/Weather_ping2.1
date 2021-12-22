package app.ys.weather_ping21.Notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import app.ys.weather_ping21.Constants.WPConstants;

public class WPBroadcastReceiverService extends BroadcastReceiver {
    String TAG = "MyReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences switches = context.getSharedPreferences("toggle", Context.MODE_PRIVATE);
        if ((switches.getInt("Toggle3", -1)) == 1){
            if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
                //boot has completed, starting the background notification service.
                Log.wtf(TAG, "Device booted up!  Inside Receiver");
                Intent service = new Intent(context, WPForegroundService.class);
                service.setAction(WPConstants.ACTION.STARTFOREGROUND_ACTION);
                context.startService(service);
                Log.wtf(TAG, "Intent Created");
            }
        }
    }
}