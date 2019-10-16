package com.hominian.findme.Adapters;


import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.hominian.findme.DataModels.PersonModel;
import com.hominian.findme.R;

import org.apache.commons.text.WordUtils;

public class ProfileAdapter extends FirestoreRecyclerAdapter<PersonModel, ProfileAdapter.ProfileHolder> {


    private OnItemClickListener mListener;


    public ProfileAdapter(@NonNull FirestoreRecyclerOptions<PersonModel> options) {
        super(options);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull ProfileHolder holder, int position, @NonNull PersonModel model) {
        String comma = model.getAge().equals("")?"":",";
        holder.nameTv.setText(WordUtils.capitalizeFully(model.getName()) + comma);
        holder.ageTv.setText(model.getAge());

        if (model.getMissingSince().equals("")){
            holder.detailsTv.setText(model.getDetails());
        } else {
            holder.detailsTv.setText("Missing Since: " + model.getMissingSince());
        }

    }

    @NonNull
    @Override
    public ProfileHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_cards, parent, false);
        return new ProfileHolder(view);
    }



    class ProfileHolder extends RecyclerView.ViewHolder{

        TextView nameTv;
        TextView ageTv;
        TextView detailsTv;


        public ProfileHolder(@NonNull View itemView) {
            super(itemView);

            nameTv = itemView.findViewById(R.id.name_tv_id);
            ageTv = itemView.findViewById(R.id.age_tv_id);
            detailsTv = itemView.findViewById(R.id.details_tv_id);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && mListener != null){
                        mListener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });

        }
    }


    public interface OnItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

}
