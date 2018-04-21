package com.kindred.kindred;

/**
 * Created by Adnan Iqbal Khan on 26-Feb-18.
 */

public class Provider {
    String name;
    String uid;
    String image_id;

    public Provider() {
    }

    public Provider(String name, String uid, String image_id) {
        this.name = name;
        this.uid = uid;
        this.image_id = image_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }
}
