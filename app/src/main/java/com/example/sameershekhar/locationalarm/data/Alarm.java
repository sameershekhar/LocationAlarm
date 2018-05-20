package com.example.sameershekhar.locationalarm.data;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by sameershekhar on 09-Mar-18.
 */

public class Alarm {

    private String location, description, dateTime;
    private long milliSecond;
    private int id;
    private LatLng latLng;

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public Alarm() {
    }

    public Alarm(int id,String location , String description, String dateTime,long milliSecond) {
        this.id=id;
        this.location = location;
        this.description = description;
        this.dateTime = dateTime;
        this.milliSecond=milliSecond;
    }

    public long getMilliSecond() {
        return milliSecond;
    }

    public void setMilliSecond(long milliSecond) {
        this.milliSecond = milliSecond;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

