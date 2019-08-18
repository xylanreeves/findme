package com.hominian.findme.DataModels;

public class GridModel {

    private String name;
    private String details;
    private int image, age;


    public GridModel() {
    }

    public GridModel(String name, String details, int image, int age) {
        this.name = name;
        this.details = details;
        this.image = image;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public String getDetails() {
        return details;
    }

    public int getImage() {
        return image;
    }

    public int getAge() {
        return age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void setAge(int age) {
        this.age = age;
    }
}