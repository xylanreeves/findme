package com.hominian.findme.Objects;

import android.net.Uri;

public class Person {

    private static int count;

    private String name;
    private int age;
    private String gender;
    private String nationality;
    private String lat;
    private String lng;
    private String extraDetails;
    private Uri imgUri;


    public Person() {
        //for firebase's sake
    }

    public Person(String name, int age, String gender, String nationality, String lat, String lng, String extraDetails, Uri imgUri) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.nationality = nationality;
        this.lat = lat;
        this.lng = lng;
        this.extraDetails = extraDetails;
        this.imgUri = imgUri;
    }

    public static int getCount() {
        return count;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getExtraDetails() {
        return extraDetails;
    }

    public void setExtraDetails(String extraDetails) {
        this.extraDetails = extraDetails;
    }

    public Uri getImgUri() {
        return imgUri;
    }

    public void setImgUri(Uri imgUri) {
        this.imgUri = imgUri;
    }
}
