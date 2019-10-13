package com.hominian.findme.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hominian.findme.Adapters.SliderAdapter;
import com.hominian.findme.DataModels.PersonModel;
import com.hominian.findme.R;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.apache.commons.text.WordUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import id.zelory.compressor.Compressor;


public class ConfirmDetails extends AppCompatActivity {

    private static final String TAG = "ConfirmDetails";
    private TextView nameTv;
    private TextView ageTv;
    private TextView genderTv;
    private TextView nationalityTv;
    private TextView missingSinceField;
    private TextView missingSinceTv;
    private TextView eyeColorField;
    private TextView eyeColorTv;
    private TextView heightField;
    private TextView heightTv;
    private TextView weightField;
    private TextView weightTv;
    private TextView personalityField;
    private TextView personalityTv;

    private TextView publishedOnField;
    private TextView publishedOnTv;

    private TextView detailsField;
    private TextView detailsTv;

    private TextView contactsField;
    private TextView contactsTv;

    private Button uploadButton;


    private PersonModel person;

    private List<Uri> imageList;
    private List<String> imageDownnloadUrls;

    private SliderView sliderView;

    private FirebaseAuth mAuth;
    private FirebaseFirestore rootRef;
    private CollectionReference finds;
    private StorageReference mStorageReference;

    int counter;
    Bitmap image_bitmap;

    Dialog disclaimerDialog;


    private void initFindViewByIds(){

        nameTv = findViewById(R.id.nameTv);
        ageTv = findViewById(R.id.ageTv);
        genderTv = findViewById(R.id.genderTv);
        nationalityTv = findViewById(R.id.nationalityTv);
        missingSinceTv = findViewById(R.id.missing_sinceTv);
        eyeColorTv = findViewById(R.id.eyeTv);
        heightTv = findViewById(R.id.heightTv);
        weightTv = findViewById(R.id.weightTv);
        personalityTv = findViewById(R.id.personalityTv);
        publishedOnTv = findViewById(R.id.pubished_onTv);
        detailsTv = findViewById(R.id.detailTv);
        contactsTv = findViewById(R.id.contactTv);


        publishedOnField = findViewById(R.id.pb_);
        missingSinceField = findViewById(R.id.missing_since_field);
        eyeColorField = findViewById(R.id.eye_color_);
        heightField = findViewById(R.id.height_);
        weightField = findViewById(R.id.weight_);
        personalityField = findViewById(R.id.personality_);
        detailsField = findViewById(R.id.details_);
        contactsField = findViewById(R.id.contact_);

        uploadButton = findViewById(R.id.upload_button);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_details);


//        Toolbar toolbar = findViewById(R.id.toolbar_confirm_details);
//        setSupportActionBar(toolbar);
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        }

        mAuth = FirebaseAuth.getInstance();
        rootRef = FirebaseFirestore.getInstance();
        mStorageReference = FirebaseStorage.getInstance().getReference();

        imageDownnloadUrls = new ArrayList<>();

        initFindViewByIds();

        disclaimerDialog = new Dialog(this);


        person = getIntent().getParcelableExtra("personData");

        if (person != null && person.getImageList() != null) {
            imageList = person.getImageList();
        }


        sliderView = findViewById(R.id.imageSlider);
        sliderView.setSliderAdapter(new SliderAdapter(this, imageList));
        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);



        initLayout();


    }

    public void showDisclaimerDialog(View view){

        TextView mTerms;
        Button mBAck;
        Button mUpload;

        disclaimerDialog.setContentView(R.layout.disclaimer_dialog);

        mTerms = disclaimerDialog.findViewById(R.id.termsTv);
        mBAck = disclaimerDialog.findViewById(R.id.back_btn);
        mUpload = disclaimerDialog.findViewById(R.id.upload_btn);

        mTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ConfirmDetails.this, TermsActivity.class));
            }
        });

        mBAck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disclaimerDialog.dismiss();
            }
        });

        mUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadDetails();
            }
        });

        disclaimerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        disclaimerDialog.show();

    }


    private void uploadDetails(){


        finds = rootRef.collection("finds");
        String pId = finds.getId();
        StorageReference imagesDocument = mStorageReference.child("person_images");



        counter = 1;

        if (imageList.size() != 0) {
            for (Uri uri : imageList) {

                File imageFile = new File(uri.getPath());


                try {
                    image_bitmap = new Compressor(this)
                                .setQuality(75)
                                .setCompressFormat(Bitmap.CompressFormat.WEBP)
                                .compressToBitmap(imageFile);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                image_bitmap.compress(Bitmap.CompressFormat.WEBP, 100, baos);
                byte[] imageByte = baos.toByteArray();


                UploadTask uploadTask = imagesDocument.child(pId).child("image" + counter +".webp").putBytes(imageByte);
                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()){
                            if (task.getResult() != null){
                                imageDownnloadUrls.add(task.getResult().toString());
                                counter++;
                            }
                        } else {
                            Toast.makeText(ConfirmDetails.this, "Error uploading images!", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "Image Upload Error! : " + task.getException());
                        }
                    }
                });

            }


            if(counter == imageList.size()){

                PersonModel mPerson = new PersonModel(person.getName(), finds.getId(), person.getMissingSince(), person.getAge(), person.getGender(),
                        person.getEyeColor(), person.getPersonality(), person.getHeight(), person.getWeight(), person.getNationality(),
                        person.getDetails(), person.getContactDetails(), imageDownnloadUrls, Timestamp.now(), mAuth.getCurrentUser().getUid());

                finds.add(mPerson).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()){

                            Toast.makeText(ConfirmDetails.this, "Upload Successful!", Toast.LENGTH_LONG).show();

                            Intent mIntent = new Intent(ConfirmDetails.this, MainActivity.class);
                            startActivity(mIntent);
                            finish();

                        } else {
                            Toast.makeText(ConfirmDetails.this, "Upload Error, Please Try again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        } else {
            //imageList == 0
            //then-->
            PersonModel mPerson = new PersonModel(person.getName(), finds.getId(), person.getMissingSince(), person.getAge(), person.getGender(),
                    person.getEyeColor(), person.getPersonality(), person.getHeight(), person.getWeight(), person.getNationality(),
                    person.getDetails(), person.getContactDetails(), imageDownnloadUrls, Timestamp.now(), mAuth.getCurrentUser().getUid());

            finds.add(mPerson).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    if (task.isSuccessful()){

                        Toast.makeText(ConfirmDetails.this, "Upload Successful!", Toast.LENGTH_LONG).show();

                        Intent mIntent = new Intent(ConfirmDetails.this, MainActivity.class);
                        startActivity(mIntent);
                        finish();

                    } else {
                        Toast.makeText(ConfirmDetails.this, "Upload Error, Please Try again!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    @SuppressLint("SetTextI18n")
    private void initLayout() {

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();


            String name = person.getName();
            String age = person.getAge();
            String comma = age.equals("") ? "" : ",";

            String gender = person.getGender();
            String nationality = person.getNationality();
            String genderComma = nationality.equals("") ? "" : ",";

            String missingSince = person.getMissingSince();
            String publishedOn = formatter.format(date);

            String eyeColor = person.getEyeColor();
            String height = person.getHeight();
            String weight = person.getWeight();
            String personalityType = person.getPersonality();

            String details = person.getDetails();
            String contacts = person.getContactDetails();


            if (name.equals("")) {
                nameTv.setText(R.string.no_name);
            } else nameTv.setText(WordUtils.capitalizeFully(name) + comma);

            if (age.equals("")) {
                ageTv.setVisibility(View.GONE);
            } else ageTv.setText(age);

            if (gender.equals("")) {
                genderTv.setVisibility(View.GONE);
            } else genderTv.setText(WordUtils.capitalizeFully(gender) + genderComma);

            if (nationality.equals("")) {
                nationalityTv.setVisibility(View.GONE);
            } else nationalityTv.setText(WordUtils.capitalizeFully(nationality));


            if (missingSince.equals("")) {
                missingSinceTv.setVisibility(View.GONE);
                missingSinceField.setVisibility(View.GONE);
            } else missingSinceTv.setText(missingSince);


            publishedOnTv.setText(publishedOn);

            if (eyeColor.equals("")) {
                eyeColorField.setVisibility(View.GONE);
                eyeColorTv.setVisibility(View.GONE);
            } else eyeColorTv.setText(WordUtils.capitalizeFully(eyeColor));

            if (height.equals("")) {
                heightField.setVisibility(View.GONE);
                heightTv.setVisibility(View.GONE);
            } else heightTv.setText(height);

            if (weight.equals("")) {
                weightField.setVisibility(View.GONE);
                weightTv.setVisibility(View.GONE);
            } else weightTv.setText(weight);

            if (personalityType.equals("")) {
                personalityField.setVisibility(View.GONE);
                personalityTv.setVisibility(View.GONE);
            } else personalityTv.setText(personalityType);


            if (details.equals("")) {
                detailsField.setVisibility(View.GONE);
                detailsTv.setVisibility(View.GONE);
            } else detailsTv.setText(details);

            if (contacts.equals("")) {
                contactsField.setVisibility(View.GONE);
                contactsTv.setVisibility(View.GONE);
            } else contactsTv.setText(contacts);






    }


}
