package com.hominian.findme.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
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

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference findsReference = db.collection("finds");

    private ProfileAdapter profileAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


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




        if (mAuth.getCurrentUser() != null){
            numId.setText(mAuth.getCurrentUser().getPhoneNumber());
            numId.setVisibility(View.VISIBLE);
            signOutBtn.setVisibility(View.VISIBLE);
        } else {
            numId.setText(R.string.guest_);
            signOutBtn.setVisibility(View.INVISIBLE);
        }


        InitRecyclerView();

        profileAdapter.setOnItemClickListener(new ProfileAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                PersonModel mPerson = documentSnapshot.toObject(PersonModel.class);

                Intent mIntent = new Intent(MainActivity.this, ProfileActivity.class);
                mIntent.putExtra("mPerson", mPerson);
                startActivity(mIntent);
            }
        });

    }

    private void InitRecyclerView() {
    Query query = findsReference.orderBy("timeStamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<PersonModel> options = new FirestoreRecyclerOptions.Builder<PersonModel>()
                .setQuery(query, PersonModel.class)
                .build();

        profileAdapter = new ProfileAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewId);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(profileAdapter);
    }


    @Override
    protected void onStart() {
        super.onStart();
        profileAdapter.startListening();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        profileAdapter.stopListening();
    }

    //Clicks
    public void drawerToggle(View view){
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.openDrawer(GravityCompat.START);
    }
    public void preferencesClick(View view){
        startActivity(new Intent(MainActivity.this, ProfileActivity.class));
    }
    public void addPerson(View view){

        if (mUser != null){
            Toast.makeText(this, "mUSer: " + mUser.getUid() + "\n" + mUser.getPhoneNumber(), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, AddDetailsActivity.class));
        } else {

            startActivity(new Intent(MainActivity.this, PhoneAuth.class));
        }

    }

    public void openDonationPage(View view){
        startActivity(new Intent(MainActivity.this, DonationActivity.class));
    }


    public void signOut(View view){


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
}