package com.hominian.findme.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;
import com.hominian.findme.R;

import java.util.concurrent.TimeUnit;

public class PhoneAuth extends AppCompatActivity {

    Toolbar toolbar;
    TextView tV2;
    EditText phoneNumber;
    CountryCodePicker ccp;
    Button bGenerateCode;


    String number;
    FirebaseAuth mAuth;


    private PhoneAuthProvider.OnVerificationStateChangedCallbacks verificationCallbacks;
    private PhoneAuthProvider.ForceResendingToken resendToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        toolbar = findViewById(R.id.toolbar_phone);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        bGenerateCode = findViewById(R.id.generateOtpButton);
        phoneNumber = findViewById(R.id.phone_tv);
        tV2 = findViewById(R.id.tV2);
        ccp = findViewById(R.id.ccp_id);
        ccp.registerCarrierNumberEditText(phoneNumber);


        mAuth = FirebaseAuth.getInstance();


    }



    public void sendCode(View view){
        number = ccp.getFullNumberWithPlus();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                this,
                verificationCallbacks
        );

    }

//
//
//
//
//    private void setUpVerificationCallbacks(){
//
//        verificationCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//            @Override
//            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
//
//            }
//
//            @Override
//            public void onVerificationFailed(FirebaseException e) {
//
//            }
//        }
//    }
//







    public void verifyOtp(View view){
        startActivity(new Intent(PhoneAuth.this, EnterOtp.class));
    }

}
