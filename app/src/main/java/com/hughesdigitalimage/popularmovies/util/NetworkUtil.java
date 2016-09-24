package com.hughesdigitalimage.popularmovies.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.lang.ref.WeakReference;

/**
 * Created by David on 9/24/16.
 */

public class NetworkUtil {

    public static boolean isDeviceConnectedToNetwork(WeakReference<Context> weakContext){

        Context context = weakContext.get();
        if (context == null) {
            return false;
        }

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null) {
            return false;
        }

        if (networkInfo.isConnected()){
            return true;
        } else {
            return false;
        }
    }
}
