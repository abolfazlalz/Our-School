package com.futech.our_school.utils.country;

public class CityData {

    private int id;
    private String name;
    private String latitude;
    private String longitude;
    private float phi;
    private float lambda;
    private StateData state;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public float getPhi() {
        return phi;
    }

    public float getLambda() {
        return lambda;
    }

    public StateData getState() {
        return state;
    }
}
