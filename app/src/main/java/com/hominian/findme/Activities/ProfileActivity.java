package com.hominian.findme.Activities;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.hominian.findme.Adapters.ImageViewAdapter;
import com.hominian.findme.R;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ViewPager viewPager = findViewById(R.id.imageViewPager);
        ImageViewAdapter imageViewAdapter = new ImageViewAdapter(this);
        viewPager.setAdapter(imageViewAdapter);
    }
}
