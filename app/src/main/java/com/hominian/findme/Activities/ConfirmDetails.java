package com.hominian.findme.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.hominian.findme.Adapters.SliderAdapter;
import com.hominian.findme.DataModels.PersonModel;
import com.hominian.findme.R;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.List;

public class ConfirmDetails extends AppCompatActivity {


    private TextView nameTv;
    private TextView ageTv;
    private TextView genderTv;
    private TextView nationalityTv;
    private TextView missingSinceField;
    private TextView missingSinceTv;
    private TextView eyeColorField;
    private TextView eyeColorTv;
    private TextView heightField;
    private TextView heightTv;
    private TextView weightField;
    private TextView personalityField;
    private TextView personalityTv;

    private TextView publishedOnFiels;
    private TextView publishedOnTv;

    private TextView detailsField;
    private TextView detailsTv;

    private TextView contactsField;
    private TextView contactsTv;




    private PersonModel person;

    private List<Uri> imageList;

    private SliderView sliderView;


    private void initFindViewByIds(){

        nameTv = findViewById(R.id.nameTv);
        ageTv = findViewById(R.id.ageTv);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_details);


//        Toolbar toolbar = findViewById(R.id.toolbar_confirm_details);
//        setSupportActionBar(toolbar);
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        }




        person = getIntent().getParcelableExtra("personData");

        if (person != null && person.getImageList() != null) {
            imageList = person.getImageList();
        }


        sliderView = findViewById(R.id.imageSlider);
        sliderView.setSliderAdapter(new SliderAdapter(this, imageList));
        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);



    }



}
