package com.example.sameershekhar.locationalarm.Services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.example.sameershekhar.locationalarm.myBroadcastReceiver;

import java.util.List;
import java.util.Locale;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by sameershekhar on 12-Mar-18.
 */

public class UtilFunctions {

    private static Context context;
    public UtilFunctions(Context context) {
        this.context=context;
    }

    public  String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Log.d("addfun","i am in address function");
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            Log.d("addfunreturn","i am in address function goin out");
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.d("address1", "" + strReturnedAddress.toString());
            } else {
                Log.d("address2", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("address3", "Canont get Address!");
        }
        Log.d("addfunreturn","i am in address function goin out");
        return strAdd;
    }

    public static void startAlarm() {
        int i = 0;
        Intent intent = new Intent(context, myBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 234324243, intent, 0);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(ALARM_SERVICE);
        alarmManager.set(alarmManager.RTC_WAKEUP, System.currentTimeMillis() + 0, pendingIntent);


    }
}
