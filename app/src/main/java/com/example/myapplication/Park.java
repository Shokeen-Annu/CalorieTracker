package com.example.myapplication;

public class Park {

    private double latitude;
    private double longitude;
    private String name;

    public Park(double lat,double lng, String name)
    {
        this.latitude = lat;
        this.longitude = lng;
        this.name = name;
    }

    public double getLatitude()
    {
        return this.latitude;
    }
    public double getLongitude()
    {
        return this.longitude;
    }
    public String getName()
    {
        return this.name;
    }

}
