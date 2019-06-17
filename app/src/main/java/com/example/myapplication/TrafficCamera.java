package com.example.myapplication;

public class TrafficCamera {
    String label;
    String image;
    String owner;
    double[] coordinates;
    public TrafficCamera(String label, String image, String owner, double[] coordinates){
        this.label = label;
        this.image = image;
        this.owner = owner;
        this.coordinates = coordinates;

    }
}