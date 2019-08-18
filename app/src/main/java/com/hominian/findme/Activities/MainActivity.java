package com.hominian.findme.Activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;


import com.hominian.findme.Adapters.ImageViewAdapter;
import com.hominian.findme.Adapters.RecyclerViewAdapter;
import com.hominian.findme.DataModels.GridModel;
import com.hominian.findme.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    List<GridModel> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initList();
        initRecyclerView();






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











    //Clicks
    public void drawerToggle(View view){
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.openDrawer(GravityCompat.START);
    }
    public void preferencesClick(View view){
        startActivity(new Intent(MainActivity.this, ProfileActivity.class));
    }
    public void addPerson(View view){
        startActivity(new Intent(MainActivity.this, PhoneAuth.class));
    }

    public void openDonationPage(View view){
        startActivity(new Intent(MainActivity.this, DonationActivity.class));
    }


    



}