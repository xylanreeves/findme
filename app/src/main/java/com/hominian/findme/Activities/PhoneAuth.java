package com.hominian.findme.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;
import com.hominian.findme.R;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class PhoneAuth extends AppCompatActivity implements View.OnClickListener {

    private TextView tV3;
    private EditText phoneNumber;
    private CountryCodePicker ccp;
    private Button verifyCode;
    private Button sendCodeButton;
    private PinView pinView;
    private ProgressBar mProgressBar;

    private CountDownTimer countDownTimer;
    private long timeLeftInMilliSec = 60000;
    private boolean timerRunning;

    private String number;
    private String verificationId;
    private PhoneAuthProvider.ForceResendingToken resendToken;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;

    private FirebaseAuth mAuth;
    private FirebaseFirestore rootRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        Toolbar toolbar = findViewById(R.id.toolbar_phone);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        verifyCode = findViewById(R.id.verify_code_button);
        phoneNumber = findViewById(R.id.phone_tv);
        tV3 = findViewById(R.id.tv3);
        ccp = findViewById(R.id.ccp_id);
        ccp.registerCarrierNumberEditText(phoneNumber);
        sendCodeButton = findViewById(R.id.mSendCode);
        pinView = findViewById(R.id.pinview_id);
        mProgressBar = findViewById(R.id.progressBar);

        //clicks_handle
        verifyCode.setOnClickListener(this);
        sendCodeButton.setOnClickListener(this);

        //visibilty
        verifyCode.setEnabled(false);
        tV3.setVisibility(View.INVISIBLE);
        pinView.setVisibility(View.INVISIBLE);

        //Firebase_Stuff
        mAuth = FirebaseAuth.getInstance();
        rootRef = FirebaseFirestore.getInstance();


        //init_callbacks
        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onCodeSent(String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                verificationId = s;
                resendToken = forceResendingToken;




                verifyCode.setEnabled(true);
                mProgressBar.setVisibility(View.VISIBLE);


            }

            //auto verification
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                String code = phoneAuthCredential.getSmsCode();

                mProgressBar.setVisibility(View.INVISIBLE);
                if (code !=null){
                    pinView.setText(code);
                    verifyCode(code);
                }

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                mProgressBar.setVisibility(View.INVISIBLE);
                if (e instanceof FirebaseAuthInvalidCredentialsException){
                    Toast.makeText(PhoneAuth.this, "Invalid code: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                } else if (e instanceof FirebaseTooManyRequestsException){
                    Toast.makeText(PhoneAuth.this, "Too many tries! " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }

                Toast.makeText(PhoneAuth.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };
    }





    //Code Verification
    private void verifyCode(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signWithCredential(credential);
    }




    //signIn using PhoneAuth
    private void signWithCredential(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){

                            String phoneNumber = task.getResult().getUser().getPhoneNumber();
                            mProgressBar.setVisibility(View.INVISIBLE);
                            if (phoneNumber != null) {
                                rootRef.collection("uploaders").document(phoneNumber);
                            }
                            Toast.makeText(PhoneAuth.this, "Verification Complete", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(PhoneAuth.this, AddDetailsActivity.class));
                            finish();

                        } else {


                            Toast.makeText(PhoneAuth.this, "Please try again", Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }




    @Override
    public void onClick(View v) {

        if (v == sendCodeButton) {

            number = ccp.getFullNumberWithPlus();
            if (!ccp.isValidFullNumber()) {
                Toast.makeText(this, "Please Enter a Valid Phone Number", Toast.LENGTH_SHORT).show();
            } else {
                sendCode(number);

                Toast.makeText(PhoneAuth.this, "Code sent to " + number, Toast.LENGTH_SHORT).show();
                startTimer();
                tV3.setVisibility(View.VISIBLE);
                pinView.setVisibility(View.VISIBLE);
            }

            if (v == verifyCode) {
                String otp = pinView.getText().toString();
                if (otp.length() < 6){
                    Toast.makeText(this, "Invalid Code entered!", Toast.LENGTH_SHORT).show();
                }
                verifyCode(otp);

            }
        }
    }

    private void sendCode(String number) {

        sendCodeButton.setEnabled(false);

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                this,
                mCallBacks
        );
    }


    private void startTimer(){
        countDownTimer = new CountDownTimer(timeLeftInMilliSec, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMilliSec = millisUntilFinished;

                int seconds = (int) (timeLeftInMilliSec/1000);

                String timeLeftText;
                timeLeftText = "" + seconds;
                if (seconds<10) timeLeftText = "0" + timeLeftText;

                sendCodeButton.setText(timeLeftText);
                sendCodeButton.setEnabled(false);
            }

            @Override
            public void onFinish() {
                sendCodeButton.setEnabled(true);
                sendCodeButton.setText(R.string.send_code);
            }
        }.start();

        timerRunning = true;
    }


}
