package com.hominian.findme.Activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hominian.findme.Adapters.ProfileSliderAdapter;
import com.hominian.findme.DataModels.PersonModel;
import com.hominian.findme.R;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.apache.commons.text.WordUtils;

public class ConfirmEditActivity extends AppCompatActivity {

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


    private Button uploadButton;

    PersonModel mPerson;


    FirebaseFirestore db;
    DocumentReference findsref;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_details);
        initFindViewByIds();
        initLayout();

        mPerson = getIntent().getParcelableExtra("personData");

        db = FirebaseFirestore.getInstance();
        findsref = db.collection("finds").document(mPerson.getPersonId());

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmEditActivity.this);
                builder.setMessage("Confirm and Update Details?")
                        .setNegativeButton("Go Back", null)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                confirmEditAndUpload();
                                startActivity(new Intent(ConfirmEditActivity.this, MainActivity.class));
                                finish();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }


    private void confirmEditAndUpload(){

        findsref.update("name", mPerson.getName());
        findsref.update("age", mPerson.getAge());
        findsref.update("missingSince", mPerson.getMissingSince());
        findsref.update("gender", mPerson.getGender());
        findsref.update("eyeColor", mPerson.getEyeColor());
        findsref.update("personality", mPerson.getPersonality());
        findsref.update("height", mPerson.getHeight());
        findsref.update("weight", mPerson.getWeight());
        findsref.update("nationality", mPerson.getNationality());
        findsref.update("details", mPerson.getDetails());
        findsref.update("contactDetails", mPerson.getContactDetails());


    }

    @SuppressLint("SetTextI18n")
    private void initLayout() {

        SliderView sliderView = findViewById(R.id.imageSlider_p);
        sliderView.setSliderAdapter(new ProfileSliderAdapter(this, mPerson.getImageDownloadUrls()));
        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);


        String comma = mPerson.getAge().equals("") ? "" : ",";
        String genderComma = mPerson.getGender().equals("") ? "" : ",";


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

    private void initFindViewByIds(){

        nameTv = findViewById(R.id.nameTv_c);
        ageTv = findViewById(R.id.ageTv_c);
        genderTv = findViewById(R.id.genderTv_c);
        nationalityTv = findViewById(R.id.nationalityTv_c);
        missingSinceTv = findViewById(R.id.missing_sinceTv_c);
        eyeColorTv = findViewById(R.id.eyeTv_c);
        heightTv = findViewById(R.id.heightTv_c);
        weightTv = findViewById(R.id.weightTv_c);
        personalityTv = findViewById(R.id.personalityTv_c);
        publishedOnTv = findViewById(R.id.pubished_onTv_c);
        detailsTv = findViewById(R.id.detailTv_c);
        contactsTv = findViewById(R.id.contactTv_c);


        publishedOnField = findViewById(R.id.pb_c);
        missingSinceField = findViewById(R.id.missing_since_field_c);
        eyeColorField = findViewById(R.id.eye_color_c);
        heightField = findViewById(R.id.height_c);
        weightField = findViewById(R.id.weight_c);
        personalityField = findViewById(R.id.personality_c);
        detailsField = findViewById(R.id.details_c);
        contactsField = findViewById(R.id.contact_c);

        uploadButton = findViewById(R.id.upload_button_c);

    }


}
