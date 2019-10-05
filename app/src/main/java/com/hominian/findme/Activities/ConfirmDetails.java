package com.hominian.findme.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.hominian.findme.DataModels.PersonModel;
import com.hominian.findme.R;

public class ConfirmDetails extends AppCompatActivity {


    private PersonModel person;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_details);


        Toolbar toolbar = findViewById(R.id.toolbar_confirm_details);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        person = getIntent().getParcelableExtra("personData");


    }



}
