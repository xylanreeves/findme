package com.hominian.findme.Activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

    private static final String TAG = "UploadsActivity";

    private TextView heading;
    private TextView signInBtn;
    private Toolbar toolbar;

    private RecyclerView recyclerView;
    private ProfileAdapter uploadsAdapter;

    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private boolean admin;

    private FirebaseFirestore db;
    private CollectionReference findsRef;
    private CollectionReference uploaderRef;

    private SwipeRefreshLayout swipeRefreshLayout;

    private Query query;
    private FirestoreRecyclerOptions<PersonModel> options;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploads);

        toolbar = findViewById(R.id.toolbar_uploads);
        setSupportActionBar(toolbar);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        db = FirebaseFirestore.getInstance();
        findsRef = db.collection("finds");
        uploaderRef = db.collection("uploader");

        heading = findViewById(R.id.uploadsHeading);
        signInBtn = findViewById(R.id.sign_in_button_upld);
        mAuth = FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.recycle_uploads);
        swipeRefreshLayout = findViewById(R.id.swipeLayout);


        authStateListener = new FirebaseAuth.AuthStateListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUser = firebaseAuth.getCurrentUser();
            }
        };


        if (mAuth.getCurrentUser() != null) {


            signInBtn.setVisibility(View.GONE);
            heading.setVisibility(View.GONE);


            initRecycleView();
            Toast.makeText(UploadsActivity.this, "Hold the item for more options", Toast.LENGTH_SHORT).show();

            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    uploadsAdapter.notifyDataSetChanged();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }, 1000);
                }
            });

            if (mAuth.getCurrentUser().getPhoneNumber() != null) {
                admin = mAuth.getCurrentUser().getPhoneNumber().equals(getResources().getString(R.string.admin_));
            }

        } else {

            heading.setText(getResources().getString(R.string.sign_in_to_see_your_uploads));
            heading.setVisibility(View.VISIBLE);
            signInBtn.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            swipeRefreshLayout.setVisibility(View.GONE);
            signInBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(UploadsActivity.this, PhoneAuth.class).putExtra("uploadsflag", "blueFlag"));
                    finish();
                }
            });
        }


    }

    @SuppressLint("SetTextI18n")
    private void initRecycleView() {


        query = findsRef.whereEqualTo("uploaderId", mAuth.getCurrentUser().getPhoneNumber())
                .orderBy("timeStamp", Query.Direction.DESCENDING);


        options = new FirestoreRecyclerOptions.Builder<PersonModel>()
                .setQuery(query, PersonModel.class)
                .build();

        uploadsAdapter = new ProfileAdapter(options, this);
        uploadsAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                int items = uploadsAdapter.getItemCount();

                if (items == 0){
                    recyclerView.setVisibility(View.GONE);
                    heading.setText(getResources().getString(R.string.zero_uploads));
                    heading.setVisibility(View.VISIBLE);
                }
            }

        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(uploadsAdapter);



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
                        person.getUploaderId()
                );

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

                            PersonModel personModel = documentSnapshot.toObject(PersonModel.class);

                            switch (item.getItemId()) {
                                case R.id.option_edit:
                                    //action


                                    Intent intent = new Intent(UploadsActivity.this, EditDetailsActivity.class);
                                    intent.putExtra("personId", personModel.getPersonId());
                                    startActivity(intent);

                                    break;

                                case R.id.option_delete:
                                    //action

                                    if (!admin) {

                                        long timePassed = 0;
                                        if (personModel != null) {
                                            timePassed = Timestamp.now().getSeconds() - (personModel.getTimeStamp().getSeconds());
                                        }
                                        int daysRemaining = (int) (27 - (timePassed / 86400));
                                        int hoursRemaining = (int) ((27 * 24) - (timePassed / 3600));
                                        int minutesRemaining = (int) ((27 * 24 * 60) - (timePassed / 60));

                                        if (minutesRemaining > 0) {

                                            if (hoursRemaining < 24) {
                                                if (minutesRemaining < 60) {
                                                    Toast.makeText(UploadsActivity.this, "Deletion can be performed after " + minutesRemaining + " minutes.", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(UploadsActivity.this, "Deletion can be performed after " + hoursRemaining + " hour.", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Toast.makeText(UploadsActivity.this, "Deletion can be performed after " + daysRemaining + " days.", Toast.LENGTH_SHORT).show();
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

                                    break;

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

        if (mAuth.getCurrentUser() != null && uploadsAdapter != null) {
            uploadsAdapter.startListening();
        }
        mAuth.addAuthStateListener(authStateListener);
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
        if (uploadsAdapter != null) {
            uploadsAdapter.stopListening();
        }
    }
}