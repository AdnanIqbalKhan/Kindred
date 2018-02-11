package com.kindred.kindred;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by usman on 1/31/2018.
 */

public class Order {
    String name;
    String user_id;
    String date;
    String time;
    String purchasing_location;
    String dropoff_location;
    String deliverd;
    String confirmed;
    String posted_on;
    String thumb_image;
    String services_charges;

    public Order(){
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Order(String name, String user_id, String date, String time, String purchasing_location, String dropoff_location, String thumb_image, String services_charges){
        this.name = name;
        this.user_id = user_id;
        this.date = date;
        this.time = time;
        this.purchasing_location = purchasing_location;
        this.dropoff_location = dropoff_location;
        this.deliverd = "false";
        this.confirmed = "false";
        Map<String, String> postTime = ServerValue.TIMESTAMP;
        String timestamp = String.valueOf(postTime);
        this.posted_on = timestamp;
        this.thumb_image = thumb_image;
        this.services_charges = services_charges;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("name",name);
        result.put("user_id",user_id);
        result.put("date",date);
        result.put("time",time);
        result.put("purchasing_location",purchasing_location);
        result.put("dropoff_location",dropoff_location);
        result.put("confirmed",confirmed);
        result.put("delivered",deliverd);
        result.put("posted_on",posted_on);
        result.put("thumb_image", thumb_image);
        result.put("service_charges", services_charges);

        return result;
    }

    public String getName() {
        return name;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getPurchasing_location() {
        return purchasing_location;
    }

    public String getDropoff_location() {
        return dropoff_location;
    }

    public String getDeliverd() {
        return deliverd;
    }

    public String getConfirmed() {
        return confirmed;
    }

    public String getPosted_on() {
        return posted_on;
    }

    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }
    public String getServices_charges() {
        return services_charges;
    }

    public void setServices_charges(String services_charges) {
        this.services_charges = services_charges;
    }
}
