package com.developerali.mylifequran.TimingModel;

import java.util.Map;

public class CalculationMethod {

    private int id;
    private String name;
    private Map<String, Double> params;
    private Location location;


    public CalculationMethod(int id, String name, Map<String, Double> params, Location location) {
        this.id = id;
        this.name = name;
        this.params = params;
        this.location = location;
    }

    public CalculationMethod() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Double> getParams() {
        return params;
    }

    public void setParams(Map<String, Double> params) {
        this.params = params;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
