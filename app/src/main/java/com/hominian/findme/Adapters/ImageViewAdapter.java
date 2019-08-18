package com.hominian.findme.Adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hominian.findme.R;

public class ImageViewAdapter extends PagerAdapter {

    private Context mContext;
    private int[] imageList = new int[] {R.drawable.ks1, R.drawable.ks2, R.drawable.ks3, R.drawable.ks4};

    public ImageViewAdapter(Context Context){
        this.mContext = Context;
    }

    @Override
    public int getCount() {
        return imageList.length;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(mContext)
                .load(imageList[position])
                .into(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }


}
