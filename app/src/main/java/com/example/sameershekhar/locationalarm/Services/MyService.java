package com.example.sameershekhar.locationalarm.Services;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import com.example.sameershekhar.locationalarm.MainActivity;
import com.example.sameershekhar.locationalarm.NotificationReceiverActivity;
import com.example.sameershekhar.locationalarm.R;
import com.example.sameershekhar.locationalarm.data.Alarm;
import com.example.sameershekhar.locationalarm.data.DataBaseHelper;
import com.example.sameershekhar.locationalarm.myBroadcastReceiver;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Created by sameershekhar on 12-Mar-18.
 */

public class MyService  extends Service
{
    private static final String TAG = "BOOMBOOMTESTGPS";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 10000;
    private static final float LOCATION_DISTANCE = 500;
    private MediaPlayer mediaPlayer;
    public static String location;
    private List<Alarm> alarmList;


    private class LocationListener implements android.location.LocationListener
    {
        Location mLastLocation;

        public LocationListener(String provider)
        {
            Log.d(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location)
        {
            MainActivity.origin=new LatLng(location.getLatitude(), location.getLongitude());

            Log.d(TAG, "onLocationChanged: " + location.getLatitude()+" "+location.getLongitude());
            //UtilFunctions utilFunctions= new UtilFunctions(getApplicationContext());
            //String address=utilFunctions.getCompleteAddressString(location.getLatitude(),location.getLongitude());

            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            try {
                 MainActivity.addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 10);
                Log.d("addfunreturn","i am in address function goin out");
                if (MainActivity.addresses != null) {
                    Address returnedAddress = MainActivity.addresses.get(0);
                    Log.d("address1", "" + MainActivity.addresses.get(0).getAddressLine(0));
                    Log.d("address2", "" + returnedAddress.toString());
                    StringBuilder strReturnedAddress = new StringBuilder("");

                    for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                        strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                    }
                    //String retAdd = strReturnedAddress.toString();
                    String res=MainActivity.addresses.get(0).getAddressLine(0);
                   matchLocation(MainActivity.addresses.get(0).getAddressLine(0));

                } else {
                    Log.d("address2", "No Address returned!");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("address3", "Canont get Address!");
            }
           // Log.d("Address",address);
            mLastLocation.set(location);
        }

        @Override
        public void onProviderDisabled(String provider)
        {
            Log.d(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider)
        {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[] {
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate()
    {



        Log.e(TAG, "onCreate");
        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }

    @Override
    public void onDestroy()
    {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    public void matchLocation(String address)
    {

         Log.v("current address",address);
        alarmList=MainActivity.checkedAlarm;
        for (int i=0;i<alarmList.size();i++)
        {
            Log.v("set alarm",alarmList.get(i).getLocation());
            if (address.toLowerCase().contains(alarmList.get(i).getLocation().toLowerCase())) {
                //Toast.makeText(getApplicationContext(),
                  //      "Your Message" + MainActivity.alarmArrayList.get(i).getLocation().toLowerCase(), Toast.LENGTH_LONG).show();


                Intent notificationIntent = new Intent(this, NotificationReceiverActivity.class);
                notificationIntent.setAction(Intent.ACTION_MAIN);
                notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                notificationIntent.putExtra("screen", "debugScreen");
                notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                NotificationManager nMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                //PendingIntent intent2 = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                stackBuilder.addNextIntentWithParentStack(notificationIntent);
// Get the PendingIntent containing the entire back stack
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Builder nBuilder =
                        new NotificationCompat.Builder(this)
                                .setSmallIcon(R.drawable.alarm)
                                .setContentTitle(alarmList.get(i).getLocation().toUpperCase())
                                .setContentText(alarmList.get(i).getDescription())
                                .setContentIntent(resultPendingIntent).setPriority(NotificationManager.IMPORTANCE_HIGH)
                                .setAutoCancel(true)
                                .setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                                + "://" + getPackageName() + "/raw/xyz"));;
                nMgr.notify(0, nBuilder.build());

            }

        }

    }

    @Override
    public boolean stopService(Intent name) {
        return super.stopService(name);
    }

    public void startAlarm() {
        int i = 0;
        Intent intent = new Intent(getApplicationContext(), myBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 234324243, intent, 0);
        AlarmManager alarmManager = (AlarmManager)getApplicationContext().getSystemService(ALARM_SERVICE);
        alarmManager.set(alarmManager.RTC_WAKEUP, System.currentTimeMillis() + 0, pendingIntent);


    }
}
