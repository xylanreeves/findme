package com.hominian.findme.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.hominian.findme.R;

public class AddDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "AddDetailsActivity";
    private static final int IMAGE_PICK_INTENT = 71;

    Toolbar toolbar;
    private Uri filepath;
    private ImageView img1, img2, img3, img4, img5;
    boolean img1Vacant, img2Vacant, img3Vacant, img4Vacant, img5Vacant;
    EditText editTextPhone;
    String codeSent;

    //Firebase instances
    FirebaseAuth mAuth;


    private void findViewIds(){
        img1=findViewById(R.id.img1);
        img2=findViewById(R.id.img2);
        img3=findViewById(R.id.img3);
        img4=findViewById(R.id.img4);
        img5=findViewById(R.id.img5);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_details);
        toolbar=findViewById(R.id.toolbar_detail);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mAuth = FirebaseAuth.getInstance();
        editTextPhone = findViewById(R.id.mobile_id);
        findViewIds();

        img1.setOnClickListener(this);
        img2.setOnClickListener(this);
        img3.setOnClickListener(this);
        img4.setOnClickListener(this);
        img5.setOnClickListener(this);

    }



    @Override
    public void onClick(View v) {

        if (v==img1){
            Log.i(TAG,"onClick: img1");
            if (!img1Vacant){
                chooseImage();
            }
        }
        else if (v==img2){
            Log.i(TAG,"onClick: img2");
            if (!img2Vacant){
                chooseImage();
            }
        }
        else if (v==img3){
            Log.i(TAG,"onClick: img3");
            if (!img3Vacant){
                chooseImage();
            }
        }
        else if (v==img4){
            Log.i(TAG, "onClick: img4");
            if (!img4Vacant){
                chooseImage();
            }
        }
        else if (v==img5){
            Log.i(TAG, "onClick: img5");
            if (!img5Vacant){
                chooseImage();
            }
        }
    }



    private void chooseImage(){
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

                filepath = result.getUri();
            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
                Toast.makeText(this, "Error Occured", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void clickNext(View view){
        Toast.makeText(this, "     .,sdf\nFuck Off!", Toast.LENGTH_SHORT).show();
    }



//    private void initPhone() {
//
//
//        private void sendVerificationCode(){
//
//            String phoneNumber = editTextPhone.getText().toString();
//
//            if (phoneNumber.isEmpty()){
//                editTextPhone.setError("Phone Number is required for Verification");
//                editTextPhone.requestFocus();
//                return;
//            }
//            if (phoneNumber.length() < 10){
//                editTextPhone.setError("Please enter a valid Phone Number");
//                editTextPhone.requestFocus();
//                return;
//            }
//
//            PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                    phoneNumber,        // Phone number to verify
//                    60,                 // Timeout duration
//                    TimeUnit.SECONDS,   // Unit of timeout
//                    this,               // Activity (for callback binding)
//                    mCallbacks);        // OnVerificationStateChangedCallbacksPhoneAuthActivity.java
//
//        }
//
//        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//            @Override
//            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
//
//            }
//
//            @Override
//            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//                super.onCodeSent(s, forceResendingToken);
//
//                codeSent = s;
//            }
//
//            @Override
//            public void onVerificationFailed(FirebaseException e) {
//
//            }
//        }
//
//        private void verifySignInCode(){
//            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);
//        }
//    }



}
