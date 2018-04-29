package com.kindred.kindred;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;

import io.reactivex.annotations.NonNull;

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
//        TODO Order notification
//        HashMap<String, String> data = new HashMap<>();
//        data.put("from", From);
//        data.put("message", message);
//        data.put("detail_msg", detailMsg);
//        FirebaseDatabase.getInstance().getReference().child("Notification").child(post_id).setValue(data);
    }

    public static void sendSingleNotification(String post_id, String From, String To, String message, String detailMsg) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("message", message);
        data.put("detail_msg", detailMsg);
        data.put("time", ServerValue.TIMESTAMP);
        Log.d("Notify_CHECK", "postID: " + post_id + " , to id: " + To + " , from id: " + From);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("SingleNotification").child(post_id).child(To).child(From);
        String notifKey = ref.push().getKey();
        ref.child(notifKey).setValue(data);
    }

    public static float dpToPx(@NonNull Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }
}
