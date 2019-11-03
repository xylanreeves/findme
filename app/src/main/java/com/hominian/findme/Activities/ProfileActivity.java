package com.hominian.findme.Activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.hominian.findme.Adapters.ProfileSliderAdapter;
import com.hominian.findme.DataModels.PersonModel;
import com.hominian.findme.R;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.apache.commons.text.WordUtils;

public class ProfileActivity extends AppCompatActivity {


    private static final String TAG = "ProfileActivity";

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
    private TextView weightTv;
    private TextView personalityField;
    private TextView personalityTv;

    private TextView publishedOnField;
    private TextView publishedOnTv;

    private TextView detailsField;
    private TextView detailsTv;

    private TextView contactsField;
    private TextView contactsTv;

    private PersonModel mPerson;

    private AdView mAdView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(getResources().getString(R.string.test_device_id))
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener(){
            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }
        });

        initFindViewByIds();
        mPerson = getIntent().getParcelableExtra("personObject");
        initLayout();


    }



    @SuppressLint("SetTextI18n")
    private void initLayout() {


        SliderView sliderView = findViewById(R.id.imageSlider_p);
        sliderView.setSliderAdapter(new ProfileSliderAdapter(this, mPerson.getImageDownloadUrls()));
        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);


        String comma = mPerson.getAge().equals("") ? "" : ",";
        String genderComma = mPerson.getNationality().equals("") ? "" : ",";


        if (mPerson.getName().equals("")) {
            nameTv.setText(R.string.no_name);
        } else nameTv.setText(WordUtils.capitalizeFully(mPerson.getName()) + comma);

        if (mPerson.getAge().equals("")) {
            ageTv.setVisibility(View.GONE);
        } else ageTv.setText(mPerson.getAge());

        if (mPerson.getGender().equals("")) {
            genderTv.setVisibility(View.GONE);
        } else genderTv.setText(WordUtils.capitalizeFully(mPerson.getGender()) + genderComma);

        if (mPerson.getNationality().equals("")) {
            nationalityTv.setVisibility(View.GONE);
        } else nationalityTv.setText(WordUtils.capitalizeFully(mPerson.getNationality()));


        if (mPerson.getMissingSince().equals("")) {
            missingSinceTv.setVisibility(View.GONE);
            missingSinceField.setVisibility(View.GONE);
        } else missingSinceTv.setText(mPerson.getMissingSince());


        publishedOnTv.setText(DateUtils.getRelativeTimeSpanString(mPerson.getTimeStamp().getSeconds()*1000));

        if (mPerson.getEyeColor().equals("")) {
            eyeColorField.setVisibility(View.GONE);
            eyeColorTv.setVisibility(View.GONE);
        } else eyeColorTv.setText(WordUtils.capitalizeFully(mPerson.getEyeColor()));

        if (mPerson.getHeight().equals("")) {
            heightField.setVisibility(View.GONE);
            heightTv.setVisibility(View.GONE);
        } else heightTv.setText(mPerson.getHeight());

        if (mPerson.getWeight().equals("")) {
            weightField.setVisibility(View.GONE);
            weightTv.setVisibility(View.GONE);
        } else weightTv.setText(mPerson.getWeight());

        if (mPerson.getPersonality().equals("")) {
            personalityField.setVisibility(View.GONE);
            personalityTv.setVisibility(View.GONE);
        } else personalityTv.setText(mPerson.getPersonality());


        if (mPerson.getDetails().equals("")) {
            detailsField.setVisibility(View.GONE);
            detailsTv.setVisibility(View.GONE);
        } else detailsTv.setText(mPerson.getDetails());

        if (mPerson.getContactDetails().equals("")) {
            contactsField.setVisibility(View.GONE);
            contactsTv.setVisibility(View.GONE);
        } else contactsTv.setText(mPerson.getContactDetails());


}

    private void initFindViewByIds() {

        nameTv = findViewById(R.id.nameTv_p);
        ageTv = findViewById(R.id.ageTv_p);
        genderTv = findViewById(R.id.genderTv_p);
        nationalityTv = findViewById(R.id.nationalityTv_p);
        missingSinceTv = findViewById(R.id.missing_sinceTv_p);
        eyeColorTv = findViewById(R.id.eyeTv_p);
        heightTv = findViewById(R.id.heightTv_p);
        weightTv = findViewById(R.id.weightTv_p);
        personalityTv = findViewById(R.id.personalityTv_p);
        publishedOnTv = findViewById(R.id.pubished_onTv_p);
        detailsTv = findViewById(R.id.detailTv_p);
        contactsTv = findViewById(R.id.contactTv_p);


        publishedOnField = findViewById(R.id.pb_p);
        missingSinceField = findViewById(R.id.missing_since_field_p);
        eyeColorField = findViewById(R.id.eye_color_p);
        heightField = findViewById(R.id.height_p);
        weightField = findViewById(R.id.weight_p);
        personalityField = findViewById(R.id.personality_p);
        detailsField = findViewById(R.id.details_p);
        contactsField = findViewById(R.id.contact_p);
    }

}
