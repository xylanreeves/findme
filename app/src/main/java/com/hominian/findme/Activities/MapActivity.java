package com.hominian.findme.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.hominian.findme.R;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "MapActivity";
    private static final int DEFAULT_ZOOM = 15;


    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private MaterialSearchBar searchBar;
    private PlacesClient placesClient;
    private List<AutocompletePrediction> predictionList;
    private AutocompleteSessionToken mToken;
    private List<String> suggestionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        searchBar = findViewById(R.id.material_search_bar);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);

        Places.initialize(MapActivity.this, "AIzaSyC5s2VRnR20G9txddlIEkuOjTT8YGhDXV0");
        placesClient = Places.createClient(this);
        mToken = AutocompleteSessionToken.newInstance();

        initSearchBar();

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;


    }



    private void initSearchBar(){


        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text.toString(), true, null, true);


            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                final FindAutocompletePredictionsRequest predictionsRequest = FindAutocompletePredictionsRequest.builder()
                        .setTypeFilter(TypeFilter.ADDRESS)
                        .setSessionToken(mToken)
                        .setQuery(s.toString())
                        .build();

                placesClient.findAutocompletePredictions(predictionsRequest).addOnCompleteListener(new OnCompleteListener<FindAutocompletePredictionsResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<FindAutocompletePredictionsResponse> task) {

                        if (task.isSuccessful()){
                            FindAutocompletePredictionsResponse predictionsResponse = task.getResult();

                            if (predictionsResponse != null){

                                predictionList = predictionsResponse.getAutocompletePredictions();
                                suggestionList = new ArrayList<>();

                                for(AutocompletePrediction p : predictionList){
                                    suggestionList.add(p.getFullText(null).toString());

                                }

                                searchBar.updateLastSuggestions(suggestionList);
                                if (!searchBar.isSuggestionsVisible()){
                                    searchBar.showSuggestionsList();
                                }
                            }
                        } else {
                            Log.i(TAG, "onComplete: Cannot Search, LOL!");
                        }

                    }
                });

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        searchBar.setSuggestionsClickListener(new SuggestionsAdapter.OnItemViewClickListener() {
            @Override
            public void OnItemClickListener(int position, View v) {


                AutocompletePrediction selectedPrediction = predictionList.get(position);

                String suggestion = searchBar.getLastSuggestions().get(position).toString();
                searchBar.setText(suggestion);
                searchBar.hideSuggestionsList();




                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null){
                    imm.hideSoftInputFromWindow(searchBar.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
                }


                String placeId = selectedPrediction.getPlaceId();
                List<Place.Field> placeFields = Arrays.asList(Place.Field.LAT_LNG);

                FetchPlaceRequest fetchPlaceRequest = FetchPlaceRequest.builder(placeId, placeFields).build();

                placesClient.fetchPlace(fetchPlaceRequest).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
                    @Override
                    public void onSuccess(FetchPlaceResponse fetchPlaceResponse) {

                        Place place = fetchPlaceResponse.getPlace();
                        Log.i(TAG, "PlaceFound: " + place.getName());

                        LatLng latLngOfPlace = place.getLatLng();

                        if (latLngOfPlace != null){
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngOfPlace, DEFAULT_ZOOM));

                        }


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        if (e instanceof ApiException){
                            e.printStackTrace();
                            int statusCode = ((ApiException) e).getStatusCode();

                            Log.i(TAG, "onFailure: " + e.getMessage());
                            Log.i(TAG, "Status Code: " + statusCode);
                        }

                        Toast.makeText(MapActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


                new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    searchBar.clearSuggestions();
                                }
                            },2000);


            }

            @Override
            public void OnItemDeleteListener(int position, View v) {



            }
        });
    }


}
