package com.zombietechinc.rovingrepairs;

/**
 * Created by User on 8/29/2017.
 */

public class Vehicle {
    String make;
    int year;
    String model;
    String uniqueKey;


    public Vehicle (String make, int year, String model, String uniqueKey){
        this.make = make;
        this.year = year;
        this.model = model;
        this.uniqueKey = uniqueKey;
    }

    public Vehicle(){

    }


    public String getUniqueKey() {
        return uniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    public Vehicle(String make){
        this.make = make;
        this.year = year;
        this.model = model;
        this.uniqueKey = uniqueKey;

    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }}


