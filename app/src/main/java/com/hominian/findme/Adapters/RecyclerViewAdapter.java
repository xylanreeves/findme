package com.hominian.findme.Adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hominian.findme.DataModels.GridModel;
import com.hominian.findme.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    private List<GridModel> gridModelList;

    public RecyclerViewAdapter(Context mContext, List<GridModel> gridModelList) {
        this.mContext = mContext;
        this.gridModelList = gridModelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        view = LayoutInflater.from(mContext).inflate(R.layout.profile_cards, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int i) {

        viewHolder.name.setText(gridModelList.get(i).getName()+",");
        viewHolder.age.setText(gridModelList.get(i).getAge() + "");
        viewHolder.details.setText(gridModelList.get(i).getDetails());
        Glide.with(mContext).load(gridModelList.get(i).getImage()).into(viewHolder.image);




    }

    @Override
    public int getItemCount() {
        return gridModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name, age, details;
        ImageView image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.nameTextView);
            age = itemView.findViewById(R.id.ageTextView);
            details = itemView.findViewById(R.id.detailsTextView);
            image = itemView.findViewById(R.id.gridImageView);

        }
    }
}
