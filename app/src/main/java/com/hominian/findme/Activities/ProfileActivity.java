package com.hominian.findme.Activities;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hominian.findme.Adapters.ProfileSliderAdapter;
import com.hominian.findme.DataModels.PersonModel;
import com.hominian.findme.R;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

public class ProfileActivity extends AppCompatActivity {


    private SliderView sliderView;
    private PersonModel mPerson;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        initSliderView();
        initLayout();

        mPerson = getIntent().getParcelableExtra("personData");






    }

    private void initLayout() {

    }

    private void initSliderView() {
        sliderView = findViewById(R.id.imageSlider_p);
        sliderView.setSliderAdapter(new ProfileSliderAdapter(this, mPerson.getImageDownloadUrls()));
        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);

    }


}
