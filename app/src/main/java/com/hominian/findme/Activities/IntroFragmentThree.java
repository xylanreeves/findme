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

public class IntroFragmentThree extends Fragment {

    private ImageView imageView3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.intro_fragment_three, container, false);

        imageView3 = view.findViewById(R.id.imagefrag3);
        Glide.with(this)
                .load(R.drawable.frag_3)
                .centerCrop()
                .into(imageView3);

        return view;
    }


}
