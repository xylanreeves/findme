package com.hominian.findme.Activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.hominian.findme.R;

public class DonationActivity extends AppCompatActivity{

    private static final String TAG = "DonationActivity";

   private Button btcButton1;
   private Button btcButton2;
   private Button paypalButton;

   private Toast toast;

   public RewardedAd rewardedAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        initAd();

        Toolbar toolbar = findViewById(R.id.toolbar_donate);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        btcButton1 = findViewById(R.id.mBtcAddress1);
        btcButton2 = findViewById(R.id.mBtcAddress2);
        paypalButton = findViewById(R.id.mPaypalAddress);

    }




    private void initAd(){

        rewardedAd = new RewardedAd(this, "ca-app-pub-3940256099942544/5224354917");

        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                // Ad successfully loaded.
            }

            @Override
            public void onRewardedAdFailedToLoad(int errorCode) {
                // Ad failed to load.
            }
        };

        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);

    }




    public void viewAd(View view){

        if (rewardedAd.isLoaded()) {

            RewardedAdCallback adCallback = new RewardedAdCallback() {
                @Override
                public void onRewardedAdOpened() {

                }

                @Override
                public void onRewardedAdClosed() {

                   rewardedAd = createAndLoadRewardedAd();

                }

                @Override
                public void onUserEarnedReward(@NonNull RewardItem reward) {

                }

                @Override
                public void onRewardedAdFailedToShow(int errorCode) {
                    Toast.makeText(DonationActivity.this, "Cannot show the Awesome Ad at this moment! " + errorCode, Toast.LENGTH_SHORT).show();
                }

            };

            rewardedAd.show(this, adCallback);

        } else {
            Log.d(TAG, "The ad wasn't loaded yet.");
        }


    }

    public RewardedAd createAndLoadRewardedAd() {

        RewardedAd rewardedAd = new RewardedAd(this,
                "ca-app-pub-3940256099942544/5224354917");

        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                // Ad successfully loaded.
            }

            @Override
            public void onRewardedAdFailedToLoad(int errorCode) {
                // Ad failed to load.
            }
        };

        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
        return rewardedAd;
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

