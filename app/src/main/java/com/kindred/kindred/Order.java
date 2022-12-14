package com.kindred.kindred;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by usman on 1/31/2018.
 */

public class Order {

    private String name;
    private String user_id;
    private String date_time;
    private String purchasing_location;
    private String dropoff_location;
    private String delivered;
    private String confirmed;
    private Object posted_on;
    private String thumb_image;
    private String image_id;
    private String services_charges;
    private Provider provider;

    public Order() {
    }

    public Order(String name, String user_id, String date_time, String purchasing_location, String dropoff_location, String delivered, String confirmed, Object posted_on, String thumb_image, String image_id, String services_charges, Provider provider) {
        this.name = name;
        this.user_id = user_id;
        this.date_time = date_time;
        this.purchasing_location = purchasing_location;
        this.dropoff_location = dropoff_location;
        this.delivered = delivered;
        this.confirmed = confirmed;
        this.posted_on = posted_on;
        this.thumb_image = thumb_image;
        this.image_id = image_id;
        this.services_charges = services_charges;
        this.provider = provider;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }

    public String getPurchasing_location() {
        return purchasing_location;
    }

    public void setPurchasing_location(String purchasing_location) {
        this.purchasing_location = purchasing_location;
    }

    public String getDropoff_location() {
        return dropoff_location;
    }

    public void setDropoff_location(String dropoff_location) {
        this.dropoff_location = dropoff_location;
    }

    public String getDelivered() {
        return delivered;
    }

    public void setDelivered(String delivered) {
        this.delivered = delivered;
    }

    public String getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(String confirmed) {
        this.confirmed = confirmed;
    }

    public Object getPosted_on() {
        return posted_on;
    }

    public void setPosted_on(Object posted_on) {
        this.posted_on = posted_on;
    }

    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public String getServices_charges() {
        return services_charges;
    }

    public void setServices_charges(String services_charges) {
        this.services_charges = services_charges;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("user_id", user_id);
        result.put("date_time", date_time);
        result.put("purchasing_location", purchasing_location);
        result.put("dropoff_location", dropoff_location);
        result.put("confirmed", confirmed);
        result.put("delivered", delivered);
        result.put("posted_on", posted_on);
        result.put("image_id", image_id);
        result.put("services_charges", services_charges);

        return result;
    }
}
