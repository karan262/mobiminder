package com.example.mobiminder;

import android.content.Context;
import android.location.Location;
import android.preference.PreferenceManager;

import java.text.DateFormat;
import java.util.Date;

public class Common {
    public static final String KEY_REQUESTING_LOCATION_UPDATES = "LocationUpdatesEnable" ;

    public static String getLocationText(Location mLocation) {
//        return mLocation==null?"Unkonwn Loction":new StringBuilder()
//                .append(mLocation.getLatitude())
//                .append("/")
//                .append(mLocation.getLongitude())
//                .toString();
        return "You are near your location";
    }

    public static CharSequence getLocationTitle(MyBackgroundService myBackgroundService) {
//        return String.format("Location Updated : %1$s", DateFormat.getDateInstance().format(new Date()));
        return  "Meeting at office";
    }

    public static void setRequestingLocationUpdates(Context context, boolean value) {
        PreferenceManager.
                getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(KEY_REQUESTING_LOCATION_UPDATES,value)
                .apply();
    }

    public static boolean requestingLocationUpdates(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(KEY_REQUESTING_LOCATION_UPDATES,false);
    }
}
