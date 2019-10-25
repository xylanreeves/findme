package com.hominian.findme.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.hominian.findme.R;

public class IntroActivity extends AppIntro {


    Fragment introFrag1;
    Fragment introfrag2;
    Fragment introFrag3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        introFrag1 = new IntroFragmentOne();
        introfrag2 = new IntroFragmentTwo();
        introFrag3 =  new IntroFragmentThree();

        addSlide(introFrag1);
        addSlide(introfrag2);
        addSlide(introFrag3);

        showStatusBar(false);


        setIndicatorColor(getResources().getColor(R.color.colorPrimaryDark), getResources().getColor(R.color.text_color_light));
        setNavBarColor(R.color.colorPrimary);
        setBarColor(getResources().getColor(R.color.colorPrimary));
        setSeparatorColor(getResources().getColor(R.color.colorPrimary));
        setColorDoneText(getResources().getColor(R.color.colorPrimaryDark));
        setColorSkipButton(getResources().getColor(R.color.colorPrimaryDark));


    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);

        startActivity(new Intent(IntroActivity.this, MainActivity.class));
        finish();

        }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);

        startActivity(new Intent(IntroActivity.this, MainActivity.class));
        finish();

    }




}
