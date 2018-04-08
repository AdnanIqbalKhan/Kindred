package com.kindred.kindred;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

/**
 * Created by Adnan Iqbal Khan on 18-Mar-18.
 */

public class Util {
    public static boolean checkInternetConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static void sendNotification(String post_id, String From, String message, String detailMsg) {
        HashMap<String, String> data = new HashMap<>();
        data.put("from", From);
        data.put("message", message);
        data.put("detail_msg", detailMsg);
        FirebaseDatabase.getInstance().getReference().child("Notification").child(post_id).setValue(data);
    }

    public static void send_SingleNotification(String post_id, String From, String To, String message, String detailMsg) {
        HashMap<String, String> data = new HashMap<>();
        data.put("post_id", post_id);
        data.put("from", From);
        data.put("to", To);
        data.put("message", message);
        data.put("detail_msg", detailMsg);
        FirebaseDatabase.getInstance().getReference().child("SingleNotification").push().setValue(data);
    }
}
