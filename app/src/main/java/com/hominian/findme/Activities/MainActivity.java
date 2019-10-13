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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    TextView id, numId, signOutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initList();
        initRecyclerView();

        signOutBtn = findViewById(R.id.sign_out_id);

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
            signOutBtn.setVisibility(View.VISIBLE);
        } else {
            numId.setText(R.string.guest_);
            signOutBtn.setVisibility(View.INVISIBLE);
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