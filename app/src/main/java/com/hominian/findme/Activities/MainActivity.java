package com.hominian.findme.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
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

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser mUser;
    private TextView id;
    private TextView numId;
    private TextView signOutBtn;

    private FirebaseFirestore db;
    private CollectionReference findsReference;
    private CollectionReference uploaderRef;

    private ProfileAdapter profileAdapter;

    RecyclerView recyclerView;
    Button addButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        db = FirebaseFirestore.getInstance();
        findsReference = db.collection("finds");
        uploaderRef = db.collection("uploader");

        signOutBtn = findViewById(R.id.sign_out_id);
        id = findViewById(R.id.textView_id);
        numId = findViewById(R.id.textView_number);

        mAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUser = mAuth.getCurrentUser();
            }
        };


        if (mAuth.getCurrentUser() != null) {
            numId.setText(mAuth.getCurrentUser().getPhoneNumber());
            numId.setVisibility(View.VISIBLE);
            signOutBtn.setVisibility(View.VISIBLE);
        } else {
            numId.setText(R.string.guest_);
            signOutBtn.setVisibility(View.INVISIBLE);
        }


        InitRecyclerView();
        buttonSlideAnimation();

    }

    private void buttonSlideAnimation() {
        addButton = findViewById(R.id.bottom_add_button);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;

        final TranslateAnimation slideDownAnimation = new TranslateAnimation(0f, 0f, 0f, height);
        final TranslateAnimation slideUpAnimation = new TranslateAnimation(0f, 0f, height, 0f);
        slideDownAnimation.setDuration(500);
        slideUpAnimation.setDuration(1500);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    addButton.setVisibility(View.INVISIBLE);
                    addButton.startAnimation(slideDownAnimation);
                } else {
                    addButton.setVisibility(View.VISIBLE);
                    addButton.startAnimation(slideUpAnimation);
                }
            }
        });

    }

    private void InitRecyclerView() {

        Query query = findsReference.orderBy("timeStamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<PersonModel> options = new FirestoreRecyclerOptions.Builder<PersonModel>()
                .setQuery(query, PersonModel.class)
                .build();

        profileAdapter = new ProfileAdapter(options, this);

        recyclerView = findViewById(R.id.recyclerViewId);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(profileAdapter);


        //ClickListeners

        profileAdapter.setOnItemClickListener(new ProfileAdapter.OnItemClickListener() {
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

                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                intent.putExtra("personObject", mPerson);
                startActivity(intent);
            }
        });

        profileAdapter.setOnItemLongClickListener(new ProfileAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(final DocumentSnapshot documentSnapshot, final int position, View itemView) {

                final PopupMenu popupMenu = new PopupMenu(MainActivity.this, itemView);
                popupMenu.inflate(R.menu.options_menu);

                if (mAuth.getCurrentUser().getPhoneNumber().equals("+919717388845")) {

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            switch (item.getItemId()) {
                                case R.id.option_edit:
                                    //action

                                    PersonModel personModel = documentSnapshot.toObject(PersonModel.class);

                                    Intent intent = new Intent(MainActivity.this, EditDetailsActivity.class);
                                    intent.putExtra("personId", personModel.getPersonId());
                                    startActivity(intent);

                                    break;
                                case R.id.option_delete:
                                    //action
                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                    builder.setMessage("Delete \"" + documentSnapshot.getId() + "\" ?")
                                            .setNegativeButton("Go Back", null)
                                            .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    findsReference.document(documentSnapshot.getId()).delete();
                                                    uploaderRef.document(mAuth.getCurrentUser().getPhoneNumber()).update("finds", FieldValue.arrayRemove(documentSnapshot.getId()));
                                                }
                                            });

                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.show();

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
        profileAdapter.startListening();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (authStateListener != null){
        mAuth.removeAuthStateListener(authStateListener);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        profileAdapter.stopListening();
    }

    //Clicks
    public void drawerToggle(View view) {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void preferencesClick(View view) {
        startActivity(new Intent(MainActivity.this, ProfileActivity.class));
    }

    public void addPerson(View view) {

        if (mUser != null) {
            startActivity(new Intent(MainActivity.this, AddDetailsActivity.class));
        } else {
            startActivity(new Intent(MainActivity.this, PhoneAuth.class));
        }

    }

    public void openDonationPage(View view) {
        startActivity(new Intent(MainActivity.this, DonationActivity.class));
    }


    public void signOut(View view) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Confirm Sign Out?")
                .setNegativeButton("Go Back", null)
                .setPositiveButton("Sign Out", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAuth.signOut();
                        Intent signOutIntent = new Intent(MainActivity.this, MainActivity.class);
                        signOutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(signOutIntent);
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }


    public void openTerms(View view) {
        startActivity(new Intent(MainActivity.this, TermsActivity.class));
    }

    public void yourUploadsPage(View view) {
        startActivity(new Intent(MainActivity.this, UploadsActivity.class));
    }
}