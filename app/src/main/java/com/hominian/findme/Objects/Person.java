package com.hominian.findme.Objects;

import android.net.Uri;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Person {

    private static int count;

    private String name;
    private int age;
    private String gender;
    private String nationality;
    private String lat;
    private String lng;
    private String extraDetails;
    private Uri imgUri1;
    private Uri imgUri2;
    private Uri imgUri3;
    private Uri imgUri4;
    private Uri imgUri5;


    @ServerTimestamp
    private Date date;


    public Person() {
        //for firebase's sake
    }


}
