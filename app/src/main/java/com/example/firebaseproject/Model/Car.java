package com.example.firebaseproject.Model;

import java.io.Serializable;

public class Car implements Serializable {

    String car_brand;
    String car_model;
    int car_id;
    int year;

    public Car(){}

    public Car (String car_brand, String car_model, int car_id, int year){
        this.car_brand = car_brand;
        this.car_model = car_model;
        this.car_id = car_id;
        this.year = year;
    }

    public String getCar_brand() {
        return car_brand;
    }

    public void setCar_brand(String car_brand) {
        this.car_brand = car_brand;
    }

    public String getCar_model() {
        return car_model;
    }

    public void setCar_model(String car_model) {
        this.car_model = car_model;
    }

    public int getCar_id() {
        return car_id;
    }

    public void setCar_id(int car_id) {
        this.car_id = car_id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
