package com.hominian.findme.Activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hominian.findme.DataModels.PersonModel;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.hominian.findme.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "AddDetailsActivity";
    private static final int STORAGE_CAMERA_REQUEST_CODE = 23;
    private static final String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

    private Toolbar toolbar;
    private Uri mImageUri;
    private ImageView img1, img2, img3, img4, img5;
    private ImageButton cancelBtn1, cancelBtn2, cancelBtn3, cancelBtn4, cancelBtn5;
    boolean img1Vacant, img2Vacant, img3Vacant, img4Vacant, img5Vacant;


    private Uri[] imageUriList = new Uri[5];
    private int mView;

    //Firebase_instances
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseAuth.AuthStateListener authStateListener;

    //FirebaseFirestore_database
    private FirebaseFirestore rootRef;


    private EditText name;
    private EditText missingSince;
    private EditText age;
    private EditText gender;
    private EditText personalityType;
    private EditText height;
    private EditText weight;
    private EditText nationality;
    private EditText moreDetails;
    private EditText contactDetails;

    private Button confirmButton;



    private void findViewIds() {

        //EditTexts and Button
        name = findViewById(R.id.enter_name_id);
        missingSince = findViewById(R.id.missing_since_id);
        age = findViewById(R.id.age_id);
        gender = findViewById(R.id.gender_id);
        personalityType = findViewById(R.id.personaility_id);
        height = findViewById(R.id.height_id);
        weight = findViewById(R.id.weight_id);
        nationality = findViewById(R.id.nationality_id);
        moreDetails = findViewById(R.id.details_id);
        contactDetails = findViewById(R.id.contact_id);

        confirmButton = findViewById(R.id.confirm_btn);



        //cardView_profile_images
        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        img4 = findViewById(R.id.img4);
        img5 = findViewById(R.id.img5);

        //remove_image_buttons
        cancelBtn1 = findViewById(R.id.cancelButton1);
        cancelBtn2 = findViewById(R.id.cancelButton2);
        cancelBtn3 = findViewById(R.id.cancelButton3);
        cancelBtn4 = findViewById(R.id.cancelButton4);
        cancelBtn5 = findViewById(R.id.cancelButton5);
    }

    public void confirm(View view){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please confirm the details in the next page\nMake sure to check for any errors or improvement!")
                .setNegativeButton("Go Back", null)
                .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String mName = name.getText().toString().trim();
                        String mMissingSince = missingSince.getText().toString().trim();
                        String mAge = age.getText().toString().trim();
                        String mGender = gender.getText().toString().trim();
                        String mPersonalityType = personalityType.getText().toString().trim();
                        String mHeight = height.getText().toString().trim();
                        String mWeight = weight.getText().toString().trim();
                        String mNationality = nationality.getText().toString().trim();
                        String mDetails = moreDetails.getText().toString();
                        String mContactDetail = contactDetails.getText().toString().trim();


                        List<Uri> imageList = new ArrayList<>();
                        for (Uri u : imageUriList) {
                           if (u != null) {
                               imageList.add(u);
                           }
                        }
                        PersonModel mPerson = new PersonModel(mName, mMissingSince, mAge, mGender, mPersonalityType, mHeight, mWeight, mNationality, mDetails, mContactDetail, imageList);

                        Intent confirmPageIntent = new Intent(AddDetailsActivity.this, ConfirmDetails.class);
                        confirmPageIntent.putExtra("personData", mPerson);
                        startActivity(confirmPageIntent);

                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();



    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_details);

        toolbar = findViewById(R.id.toolbar_detail);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUser = mAuth.getCurrentUser();
                if (mUser == null) {
                    startActivity(new Intent(AddDetailsActivity.this, PhoneAuth.class));
                    finish();
                }
            }
        };

        findViewIds();


        missingSince.setOnClickListener(this);

        img1.setOnClickListener(this);
        img2.setOnClickListener(this);
        img3.setOnClickListener(this);
        img4.setOnClickListener(this);
        img5.setOnClickListener(this);

        cancelBtn1.setOnClickListener(this);
        cancelBtn2.setOnClickListener(this);
        cancelBtn3.setOnClickListener(this);
        cancelBtn4.setOnClickListener(this);
        cancelBtn5.setOnClickListener(this);

        imageUriList = new Uri[5];

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onClick(View v) {



        if (v == img1) {
            if (imageUriList[0] == null) {
                mView=1;
                chooseImage();
            }
        } else if (v == img2) {
            if (imageUriList[1] == null) {
                mView=2;
                chooseImage();
            }
        } else if (v == img3) {
            if (imageUriList[2] == null) {
                mView=3;
                chooseImage();
            }
        } else if (v == img4) {
            if (imageUriList[3] == null) {
                mView=4;
                chooseImage();
            }
        } else if (v == img5) {
            if (imageUriList[4] == null) {
                mView=5;
                chooseImage();
            }
        }

        if (v == cancelBtn1) {
            if (imageUriList[0] != null) {
                imageUriList[0] = null;
                Glide.with(this).load(R.drawable.ic_add_circle_black_24dp).centerInside().into(img1);
                cancelBtn1.setVisibility(View.INVISIBLE);
            }
        } else if (v == cancelBtn2){
            if (imageUriList[1] != null){
                imageUriList[1] = null;
                Glide.with(this).load(R.drawable.ic_add_circle_black_24dp).centerInside().into(img2);
                cancelBtn2.setVisibility(View.INVISIBLE);
            }
        } else if (v == cancelBtn3){
            if (imageUriList[2] != null){
                imageUriList[2] = null;
                Glide.with(this).load(R.drawable.ic_add_circle_black_24dp).centerInside().into(img3);
                cancelBtn3.setVisibility(View.INVISIBLE);
            }
        } else if (v == cancelBtn4){
            if (imageUriList[3] != null){
                imageUriList[3] = null;
                Glide.with(this).load(R.drawable.ic_add_circle_black_24dp).centerInside().into(img4);
                cancelBtn4.setVisibility(View.INVISIBLE);
            }
        } else if (v == cancelBtn5){
            if (imageUriList[4] != null){
                imageUriList[4] = null;
                Glide.with(this).load(R.drawable.ic_add_circle_black_24dp).centerInside().into(img5);
                cancelBtn5.setVisibility(View.INVISIBLE);
            }
        }


    }


    private void chooseImage() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_DENIED) {

                askStoragePermission();

            } else {
                cropImage();
            }
        } else {
            cropImage();
        }

    }

    private void cropImage() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);


            if (resultCode == RESULT_OK) {
                mImageUri = result.getUri();

                if (mView==1){
                    imageUriList[0]= mImageUri;
                    Glide.with(this).load(imageUriList[0]).centerCrop().into(img1);
                    cancelBtn1.setVisibility(View.VISIBLE);
                } else if (mView==2){
                    imageUriList[1]= mImageUri;
                    Glide.with(this).load(imageUriList[1]).centerCrop().into(img2);
                    cancelBtn2.setVisibility(View.VISIBLE);
                } else if (mView==3){
                    imageUriList[2]= mImageUri;
                    Glide.with(this).load(imageUriList[2]).centerCrop().into(img3);
                    cancelBtn3.setVisibility(View.VISIBLE);
                } else if (mView==4){
                    imageUriList[3]= mImageUri;
                    Glide.with(this).load(imageUriList[3]).centerCrop().into(img4);
                    cancelBtn4.setVisibility(View.VISIBLE);
                } else if (mView==5){
                    imageUriList[4]= mImageUri;
                    Glide.with(this).load(imageUriList[4]).centerCrop().into(img5);
                    cancelBtn5.setVisibility(View.VISIBLE);
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
                Toast.makeText(this, "Error Occured: " + error + "Please try again", Toast.LENGTH_SHORT).show();
            }
        }
    }





    public void askStoragePermission() {


        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission Required!")
                    .setMessage("Permisssion is needed for required action.")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(AddDetailsActivity.this, PERMISSIONS, STORAGE_CAMERA_REQUEST_CODE);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create()
                    .show();

        } else {
            ActivityCompat.requestPermissions(this, PERMISSIONS, STORAGE_CAMERA_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if (requestCode == STORAGE_CAMERA_REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                cropImage();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }

    }





}
