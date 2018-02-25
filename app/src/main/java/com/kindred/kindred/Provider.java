package com.kindred.kindred;

/**
 * Created by Adnan Iqbal Khan on 26-Feb-18.
 */

public class Provider {
    String name;
    String uid;
    String image;

    public Provider() {
    }

    public Provider(String name, String uid, String image) {
        this.name = name;
        this.uid = uid;
        this.image = image;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
