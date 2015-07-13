package com.thoughtworks.rdlian.footprint.dao.model;

import io.realm.RealmObject;

/**
 * Created by rdlian on 7/8/15.
 */
public class Point extends RealmObject {

    private double latitude;
    private double longitude;
    private String name;

    public Point(){}

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
