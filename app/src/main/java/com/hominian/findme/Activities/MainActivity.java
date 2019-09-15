package com.hominian.findme.Activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hominian.findme.Adapters.RecyclerViewAdapter;
import com.hominian.findme.DataModels.GridModel;
import com.hominian.findme.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    List<GridModel> mList;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    FirebaseUser mUser;
    TextView id, numId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initList();
        initRecyclerView();

        mAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUser = mAuth.getCurrentUser();
            }
        };



        id = findViewById(R.id.textView_id);
        numId = findViewById(R.id.textView_number);
        if (mAuth.getCurrentUser() != null){
            numId.setText(mAuth.getCurrentUser().getPhoneNumber());
            numId.setVisibility(View.VISIBLE);
            id.setVisibility(View.VISIBLE);
        } else {
            numId.setVisibility(View.INVISIBLE);
            id.setVisibility(View.INVISIBLE);
        }

    }


    public void initList(){

        mList = new ArrayList<>();
        mList.add(new GridModel("No Name", "detail", R.drawable.dp, 12));
        mList.add(new GridModel("No Name", "detail", R.drawable.dp,9));
        mList.add(new GridModel("Phoebe", "TomGirl", R.drawable.dp, 24));
        mList.add(new GridModel("Kristen", "details", R.drawable.dp, 30));
        mList.add(new GridModel("Kate", "detail", R.drawable.dp, 26));
        mList.add(new GridModel("No Name", "detail", R.drawable.dp, 12));

    }



    public void initRecyclerView(){



        RecyclerView recyclerView = findViewById(R.id.recyclerViewId);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(new RecyclerViewAdapter(this, mList));
    }


    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(authStateListener);

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


    



}