package com.hominian.findme.Adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.hominian.findme.DataModels.PersonModel;
import com.hominian.findme.R;

import org.apache.commons.text.WordUtils;

import java.util.List;
import java.util.Random;

public class ProfileAdapter extends FirestoreRecyclerAdapter<PersonModel, ProfileAdapter.ProfileHolder> {


    private OnItemClickListener mListener;
    private OnItemLongClickListener onItemLongClickListener;
    private Context mContext;


    public ProfileAdapter(@NonNull FirestoreRecyclerOptions<PersonModel> options, Context context) {
        super(options);
        this.mContext = context;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull ProfileHolder holder, int position, @NonNull PersonModel model) {

        String name = model.getName();
        String age = model.getAge();
        String gender = model.getGender();
        String nationality = model.getNationality();
        String personality = model.getPersonality();
        String missingSince = model.getMissingSince();
        String details = model.getDetails();
        List<String> imgList = model.getImageDownloadUrls();


        String comma = age.equals("") ? "" : ",";
        if (!name.equals("")) {
            holder.nameTv.setText(WordUtils.capitalizeFully(name));
        } else {
            holder.nameTv.setText("No Name");
        }

        if (comma.equals(",")) {
            holder.comma.setText(comma);
            holder.ageTv.setText(age);
        } else {
            holder.comma.setVisibility(View.GONE);
            holder.ageTv.setVisibility(View.GONE);
        }

        Random random = new Random();

        if (imgList.size() > 0) {

            Glide.with(mContext)
                    .load(imgList.get(random.nextInt(model.getImageDownloadUrls().size())))
                    .placeholder(R.drawable.dp)
                    .error(R.drawable.dp)
                    .into(holder.displayImage);

            if (!missingSince.equals("")) {

                holder.nat_genTv.setText("Missing Since: " + missingSince);
                holder.nat_genTv.setSelected(true);
                holder.detailsTv.setVisibility(View.GONE);


            } else if (!nationality.equals("")) {
                holder.nat_genTv.setText(nationality);
                holder.detailsTv.setVisibility(View.GONE);

            } else {
                holder.nat_genTv.setVisibility(View.GONE);
                if (!details.equals("")) {
                    holder.detailsTv.setText(details);
                    holder.detailsTv.setSelected(true);
                } else holder.detailsTv.setVisibility(View.GONE);
            }


        } else {

            Glide.with(mContext)
                    .load(R.drawable.dp)
                    .into(holder.displayImage);


            if (!gender.equals("")) {

                holder.nat_genTv.setText(WordUtils.capitalizeFully(gender));
                if (!missingSince.equals("")) {
                    holder.detailsTv.setText("Missing Since: " + missingSince);
                    holder.detailsTv.setSelected(true);
                } else holder.detailsTv.setVisibility(View.GONE);

            } else if (!nationality.equals("")) {

                holder.nat_genTv.setText(WordUtils.capitalizeFully(nationality));
                if (!missingSince.equals("")) {
                    holder.detailsTv.setText("Missing Since: " + missingSince);
                    holder.detailsTv.setSelected(true);
                } else holder.detailsTv.setVisibility(View.GONE);

            } else if (!personality.equals("")) {

                holder.nat_genTv.setText(personality);
                if (!missingSince.equals("")) {
                    holder.detailsTv.setText("Missing Since: " + missingSince);
                    holder.detailsTv.setSelected(true);
                } else holder.detailsTv.setVisibility(View.GONE);

            } else if (!missingSince.equals("")) {
                holder.nat_genTv.setText("Missing Since: " + missingSince);
                holder.nat_genTv.setSelected(true);
                if (!details.equals("")) {
                    holder.detailsTv.setText(details);
                    holder.detailsTv.setSelected(true);
                } else holder.detailsTv.setVisibility(View.GONE);

            } else {
                holder.nat_genTv.setVisibility(View.GONE);
                if (!details.equals("")) {
                    holder.detailsTv.setText(details);
                } else holder.detailsTv.setVisibility(View.GONE);

            }
        }


    }

    @NonNull
    @Override
    public ProfileHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_cards, parent, false);
        return new ProfileHolder(view);

    }


    class ProfileHolder extends RecyclerView.ViewHolder {

        TextView nameTv;
        TextView comma;
        TextView ageTv;
        TextView nat_genTv;
        TextView detailsTv;
        ImageView displayImage;

        public ProfileHolder(@NonNull final View itemView) {
            super(itemView);

            nameTv = itemView.findViewById(R.id.name_tv_id);
            comma = itemView.findViewById(R.id.name_comma_id);
            nat_genTv = itemView.findViewById(R.id.nation_gender_tv_id);
            ageTv = itemView.findViewById(R.id.age_tv_id);
            detailsTv = itemView.findViewById(R.id.details_tv_id);
            displayImage = itemView.findViewById(R.id.profile_pic_id);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && mListener != null) {
                        mListener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(getSnapshots().getSnapshot(position), position, itemView);
                    }
                    return true;
                }
            });
        }

    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(DocumentSnapshot documentSnapshot, int position, View itemView);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

}
