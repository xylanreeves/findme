package com.hominian.findme.Activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.hominian.findme.R;

public class DonationActivity extends AppCompatActivity{


   private Button btcButton1;
   private Button btcButton2;
   private Button paypalButton;

   Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation);

        Toolbar toolbar = findViewById(R.id.toolbar_donate);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        btcButton1 = findViewById(R.id.mBtcAddress1);
        btcButton2 = findViewById(R.id.mBtcAddress2);
        paypalButton = findViewById(R.id.mPaypalAddress);

    }



    public void setBtcButton1(View view) {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("BtcAddress1", btcButton1.getText().toString());
        clipboardManager.setPrimaryClip(clipData);
        if (toast != null) {
            toast.cancel();
        }
            toast = Toast.makeText(DonationActivity.this, "BTC Address-1 Copied", Toast.LENGTH_SHORT);
            toast.show();
    }

    public void setBtcButton2(View view){
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("BtcAddress2", btcButton2.getText().toString());
        clipboardManager.setPrimaryClip(clipData);
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(DonationActivity.this, "BTC Address-2 Copied", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void setPaypalButton(View view){
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("PaypalAddress", paypalButton.getText().toString());
        clipboardManager.setPrimaryClip(clipData);
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(DonationActivity.this, "Paypal Address Copied", Toast.LENGTH_SHORT);
        toast.show();
    }
}

