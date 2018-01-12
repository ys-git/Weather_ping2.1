package app.ys.weather_ping21;


import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

public class ForegroundService extends Service {
    private static final String LOG_TAG = "ForegroundService";



    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)) {
            //Toast.makeText(this,"Start Service",Toast.LENGTH_SHORT).show();
            Log.i(LOG_TAG, "Received Start Foreground Intent ");


            Intent notificationIntent = new Intent(this, Main.class);
            notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                    notificationIntent, 0);

            RemoteViews notificationView = new RemoteViews(this.getPackageName(),R.layout.notification);



            Bitmap icon = BitmapFactory.decodeResource(getResources(),
                    R.mipmap.ic_launcher);

            Notification notification = new NotificationCompat.Builder(this)
                    .setContentTitle("WeatherPing")
                    .setTicker("ping ping")
                    .setContentText("wohoooo")
                    .setSmallIcon(R.drawable.logoq)
                    .setLargeIcon(
                            Bitmap.createScaledBitmap(icon, 128, 128, false))
                    .setContent(notificationView)
                    .setOngoing(true).build();



            startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE,
                    notification);
        }


        else
            if (intent.getAction().equals(Constants.ACTION.STOPFOREGROUND_ACTION)) {
            Toast.makeText(this,"Stop Service",Toast.LENGTH_SHORT).show();
            Log.i(LOG_TAG, "Received Stop Foreground Intent");
            stopForeground(true);
            stopSelf();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "In onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Used only in case of bound services.
        return null;
    }

}
