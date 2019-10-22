package com.hominian.findme.Activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.hominian.findme.Adapters.ProfileAdapter;
import com.hominian.findme.DataModels.PersonModel;
import com.hominian.findme.R;

public class UploadsActivity extends AppCompatActivity {

    private TextView heading;
    private TextView signInBtn;

    private RecyclerView recyclerView;
    private ProfileAdapter uploadsAdapter;

    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private boolean admin;

    private FirebaseFirestore db;
    private CollectionReference findsRef;
    private CollectionReference uploaderRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploads);

        db = FirebaseFirestore.getInstance();
        findsRef = db.collection("finds");
        uploaderRef = db.collection("uploader");

        heading = findViewById(R.id.uploadsHeading);
        signInBtn = findViewById(R.id.sign_in_button_upld);
        mAuth = FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.recycle_uploads);

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                mUser = mAuth.getCurrentUser();
                if (mAuth.getCurrentUser() != null) {
                    heading.setVisibility(View.GONE);
                    signInBtn.setVisibility(View.GONE);
                } else {
                    heading.setText(getResources().getString(R.string.sign_in_to_see_your_uploads));
                    heading.setVisibility(View.VISIBLE);
                    signInBtn.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    signInBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(UploadsActivity.this, PhoneAuth.class));
                            finish();
                        }
                    });
                }


            }
        };


        if (mAuth.getCurrentUser() != null) {
            initRecycleView();
            admin = mAuth.getCurrentUser().getPhoneNumber().equals("+919717388845");
        }

    }

    @SuppressLint("SetTextI18n")
    private void initRecycleView() {

        Query query = findsRef.whereEqualTo("uploaderId", mAuth.getCurrentUser().getPhoneNumber())
                .orderBy("timeStamp", Query.Direction.DESCENDING);


        FirestoreRecyclerOptions<PersonModel> options = new FirestoreRecyclerOptions.Builder<PersonModel>()
                .setQuery(query, PersonModel.class)
                .build();


        uploadsAdapter = new ProfileAdapter(options, this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(uploadsAdapter);

        Toast.makeText(UploadsActivity.this, "Hold the item for more options", Toast.LENGTH_SHORT).show();

        //Clicks

        uploadsAdapter.setOnItemClickListener(new ProfileAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                PersonModel person = documentSnapshot.toObject(PersonModel.class);

                PersonModel mPerson = new PersonModel(
                        person.getName(),
                        person.getPersonId(),
                        person.getMissingSince(),
                        person.getAge(),
                        person.getGender(),
                        person.getEyeColor(),
                        person.getPersonality(),
                        person.getHeight(),
                        person.getWeight(),
                        person.getNationality(),
                        person.getDetails(),
                        person.getContactDetails(),
                        person.getImageDownloadUrls(),
                        person.getTimeStamp(),
                        person.getUploaderId());

                Intent intent = new Intent(UploadsActivity.this, ProfileActivity.class);
                intent.putExtra("personObject", mPerson);
                startActivity(intent);
            }
        });


        uploadsAdapter.setOnItemLongClickListener(new ProfileAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(final DocumentSnapshot documentSnapshot, final int position, View itemView) {

                final PopupMenu popupMenu = new PopupMenu(UploadsActivity.this, itemView);
                popupMenu.inflate(R.menu.options_menu);

                if (mAuth.getCurrentUser() != null) {

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            switch (item.getItemId()) {
                                case R.id.option_edit:
                                    //action
                                    PersonModel personModel = documentSnapshot.toObject(PersonModel.class);

                                    Intent intent = new Intent(UploadsActivity.this, EditDetailsActivity.class);
                                    intent.putExtra("personId", personModel.getPersonId());
                                    startActivity(intent);

                                    break;

                                case R.id.option_delete:
                                    //action

                                    PersonModel nPerson = documentSnapshot.toObject(PersonModel.class);

                                    if (!admin) {


                                        int diff = 0;
                                        if (nPerson != null) {
                                            diff = Timestamp.now().compareTo(nPerson.getTimeStamp());
                                        }
                                        int remainingDays = 27 - (diff / 86400);
                                        int remainingHours = (27 * 24) - (diff / 3600);
                                        int remainingMinutes = (27 * 24 * 60) - (diff / 60);

                                        if (diff < (27 * 86400)) {
                                            if (remainingHours < 24) {
                                                if (remainingMinutes < 60) {
                                                    Toast.makeText(UploadsActivity.this, "Deletion can be performed after " + remainingMinutes + " minutes.", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(UploadsActivity.this, "Deletion can be performed after " + remainingHours + " hours.", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Toast.makeText(UploadsActivity.this, "Deletion can be performed after " + remainingDays + " days.", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {

                                            AlertDialog.Builder builder = new AlertDialog.Builder(UploadsActivity.this);
                                            builder.setMessage("Delete \"" + documentSnapshot.getId() + "\" ?")
                                                    .setNegativeButton("Go Back", null)
                                                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            findsRef.document(documentSnapshot.getId()).delete();
                                                            uploaderRef.document(mAuth.getCurrentUser().getPhoneNumber()).update("finds", FieldValue.arrayRemove(documentSnapshot.getId()));
                                                            Toast.makeText(UploadsActivity.this, "Delete Successful", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            AlertDialog alertDialog = builder.create();
                                            alertDialog.show();

                                        }

                                    } else {

                                        AlertDialog.Builder builder = new AlertDialog.Builder(UploadsActivity.this);
                                        builder.setMessage("Delete \"" + documentSnapshot.getId() + "\" ?")
                                                .setNegativeButton("Go Back", null)
                                                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        findsRef.document(documentSnapshot.getId()).delete();
                                                        uploaderRef.document(mAuth.getCurrentUser().getPhoneNumber()).update("finds", FieldValue.arrayRemove(documentSnapshot.getId()));
                                                        Toast.makeText(UploadsActivity.this, "Delete Successful", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                        AlertDialog alertDialog = builder.create();
                                        alertDialog.show();

                                    }

                                default:
                            }
                            return false;
                        }
                    });

                    popupMenu.show();

                }
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(authStateListener);

        if (mAuth.getCurrentUser() != null) {
            uploadsAdapter.startListening();
        }
        ;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (authStateListener != null) {
            mAuth.removeAuthStateListener(authStateListener);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuth.getCurrentUser() != null) {
            uploadsAdapter.stopListening();
        }
    }
}