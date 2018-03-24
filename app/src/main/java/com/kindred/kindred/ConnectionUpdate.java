package com.kindred.kindred;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Adnan Iqbal Khan on 18-Mar-18.
 */

public class ConnectionUpdate extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

//        TextView temp = rootView.findViewById(R.id.login_internet_con_tb);
//        temp.setVisibility(View.VISIBLE);

        if (Util.checkInternetConnection(context)) {
            Toast.makeText(context, "connected", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "not connected", Toast.LENGTH_LONG).show();
        }
    }
}
