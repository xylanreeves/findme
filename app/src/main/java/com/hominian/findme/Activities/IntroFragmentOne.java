package com.hominian.findme.Activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.hominian.findme.R;

public class IntroFragmentOne extends Fragment {

    private ImageView imageView1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.intro_fragment_one, container, false);

        imageView1 = view.findViewById(R.id.imagefrag1);
        Glide.with(this)
                .load(R.drawable.frag_1)
                .fitCenter()
                .into(imageView1);


        return view;
    }



}
