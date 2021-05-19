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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.hominian.findme.R;

import java.util.Arrays;
import java.util.List;

public class DonationActivity extends AppCompatActivity implements PurchasesUpdatedListener, View.OnClickListener {

    private static final String TAG = "DonationActivity";

    private Button btcButton1;
    private Button btcButton2;
    private Button paypalButton;

    private Button usd1;
    private Button usd5;
    private Button usd10;


    private Toast toast;

    public RewardedAd rewardedAd;

    private BillingClient mBillingClient;
    private List<SkuDetails> skuList;

    private Button adButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation);

        adButton = findViewById(R.id.adButton);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        initAd();
        setupGoogleBilling();

        Toolbar toolbar = findViewById(R.id.toolbar_donate);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        btcButton1 = findViewById(R.id.mBtcAddress1);
        btcButton2 = findViewById(R.id.mBtcAddress2);
        paypalButton = findViewById(R.id.mPaypalAddress);

        usd1 = findViewById(R.id.donate_one);
        usd5 = findViewById(R.id.donate_five);
        usd10 = findViewById(R.id.donate_ten);

        usd1.setOnClickListener(this);
        usd5.setOnClickListener(this);
        usd10.setOnClickListener(this);


    }

    private void setupGoogleBilling() {

        mBillingClient = BillingClient.newBuilder(this).setListener(this).enablePendingPurchases().build();
        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        });


        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        params.setSkusList(Arrays.asList("usd_1", "usd_5", "usd_10")).setType(BillingClient.SkuType.INAPP);

        mBillingClient.querySkuDetailsAsync(params.build(),
                new SkuDetailsResponseListener() {
                    @Override
                    public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> skuDetailsList) {
                        // Process the result.
                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && skuDetailsList != null){
                            skuList = skuDetailsList;
                        }
                    }
                });


    }


    @Override
    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases) {

        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {

            for (Purchase purchase : purchases) {

            }

        } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
            // Handle an error caused by a user cancelling the purchase flow.
            Toast.makeText(this, "😢", Toast.LENGTH_SHORT).show();
        } else {
            // Handle any other error codes.
        }

    }


    private void initAd() {

        rewardedAd = new RewardedAd(this, getResources().getString(R.string.ad_unit_id));

        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {


            }

            @Override
            public void onRewardedAdFailedToLoad(int errorCode) {
                // Ad failed to load.
            }
        };

        rewardedAd.loadAd(new AdRequest.Builder()
//                .addTestDevice(getResources().getString(R.string.test_device_id))
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build(), adLoadCallback);

    }


    public void viewAd(View view) {

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

    public void setBtcButton2(View view) {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("BtcAddress2", btcButton2.getText().toString());
        clipboardManager.setPrimaryClip(clipData);
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(DonationActivity.this, "BTC Address-2 Copied", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void setPaypalButton(View view) {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("PaypalAddress", paypalButton.getText().toString());
        clipboardManager.setPrimaryClip(clipData);
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(DonationActivity.this, "Paypal Address Copied", Toast.LENGTH_SHORT);
        toast.show();
    }


    @Override
    public void onClick(View v) {

        if (v == usd1) {
            if (skuList == null) {
                Toast.makeText(this, "Try again after some moment", Toast.LENGTH_SHORT).show();
            } else {
                BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                        .setSkuDetails(skuList.get(0))
                        .build();
                mBillingClient.launchBillingFlow(this, flowParams);
            }
        }

        if (v == usd5) {
            if (skuList == null) {
                Toast.makeText(this, "Try again after some moment", Toast.LENGTH_SHORT).show();
            } else {
                BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                        .setSkuDetails(skuList.get(1))
                        .build();
                mBillingClient.launchBillingFlow(this, flowParams);
            }
        }

        if (v == usd10) {
            if (skuList == null) {
                Toast.makeText(this, "Try again after some moment", Toast.LENGTH_SHORT).show();
            } else {
                BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                        .setSkuDetails(skuList.get(2))
                        .build();
                mBillingClient.launchBillingFlow(this, flowParams);
            }
        }

    }


}

