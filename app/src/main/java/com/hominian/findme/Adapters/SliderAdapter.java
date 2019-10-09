package com.hominian.findme.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hominian.findme.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterVH> {

    private Context mContext;
    private List<Uri> imgList;


    public SliderAdapter(Context mContext, List<Uri> imgList) {
        this.mContext = mContext;
        this.imgList = imgList;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
            View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_confirm_details, null);
        return new SliderAdapterVH(mView);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {

        Uri imgUri = imgList.get(position);

        Glide.with(mContext).load(imgUri).into(viewHolder.imageViewBackground);

    }


    @Override
    public int getCount() {
        return imgList.size();
    }





    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        ImageView imageViewBackground;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);

        }
    }



}
