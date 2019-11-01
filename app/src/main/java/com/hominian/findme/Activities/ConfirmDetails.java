package com.hominian.findme.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
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
    private List<String> imageDownloadUrls;

    private SliderView sliderView;

    private FirebaseAuth mAuth;
    private FirebaseFirestore rootRef;
    private DocumentReference findsRef;
    private CollectionReference mUploaders;
    private StorageReference mStorageReference;
    private StorageReference imagesDocument;


    int countDown;
    Bitmap image_bitmap;

    //dialog_views
    private Dialog disclaimerDialog;
    private TextView mTerms;
    private Button mBAck;
    private Button mUpload;
    private ProgressBar mProgressBar;
    private TextView errorTv;
    private ProgressBar progressHorizontal;
    private static int counter;

    private Handler mHandler;


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


        imageDownloadUrls = new ArrayList<>();

        initFindViewByIds();

        //Upload_dialog_init
        initDialog();


        person = getIntent().getParcelableExtra("personData");

        if (person != null && person.getImageList() != null) {
            imageList = person.getImageList();
        }


        sliderView = findViewById(R.id.imageSlider_);
        sliderView.setSliderAdapter(new SliderAdapter(this, imageList));
        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);

        initLayout();


        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDisclaimerDialog();
            }
        });

    }

    private void initDialog() {

        disclaimerDialog = new Dialog(this);
        disclaimerDialog.setContentView(R.layout.disclaimer_dialog);
        mTerms = disclaimerDialog.findViewById(R.id.termsTv);
        mBAck = disclaimerDialog.findViewById(R.id.back_btn);
        mUpload = disclaimerDialog.findViewById(R.id.upload_btn);
        mProgressBar = disclaimerDialog.findViewById(R.id.mBar);
        errorTv = disclaimerDialog.findViewById(R.id.error_text);
        progressHorizontal = disclaimerDialog.findViewById(R.id.progress_h);


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

    }


    @SuppressLint("SetTextI18n")
    private void uploadDetails() {

        errorTv.setVisibility(View.VISIBLE);
        mUpload.setVisibility(View.INVISIBLE);
        mBAck.setEnabled(false);
        mProgressBar.setVisibility(View.VISIBLE);
        disclaimerDialog.setCancelable(false);


        findsRef = rootRef.collection("finds").document();
        mUploaders = rootRef.collection("uploaders");
        final String phoneNumber = mAuth.getCurrentUser().getPhoneNumber();


        if (imageList.size() > 0) {

            errorTv.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            disclaimerDialog.setCancelable(false);

            errorTv.setText("Please Wait...");
            errorTv.setVisibility(View.VISIBLE);

            PersonModel mPerson = new PersonModel(person.getName(), findsRef.getId(), person.getMissingSince(), person.getAge(), person.getGender(),
                    person.getEyeColor(), person.getPersonality(), person.getHeight(), person.getWeight(), person.getNationality(),
                    person.getDetails(), person.getContactDetails(), imageDownloadUrls, Timestamp.now(), phoneNumber);

            findsRef.set(mPerson).addOnCompleteListener(new OnCompleteListener<Void>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        mUploaders.document(phoneNumber).update("finds", FieldValue.arrayUnion(findsRef.getId()));
                        uploadImages();
                    } else {
                        errorTv.setText("Please try again after some time");
                    }
                }
            });

        } else {

            //user has not uploaded a single image
            //continue without images

            errorTv.setText("Uploading...");
            errorTv.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            errorTv.setVisibility(View.VISIBLE);

            final PersonModel mPerson = new PersonModel(person.getName(), findsRef.getId(), person.getMissingSince(), person.getAge(), person.getGender(),
                    person.getEyeColor(), person.getPersonality(), person.getHeight(), person.getWeight(), person.getNationality(),
                    person.getDetails(), person.getContactDetails(), imageDownloadUrls, Timestamp.now(), phoneNumber);

            findsRef.set(mPerson).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {


                        mUploaders.document(phoneNumber).update("finds", FieldValue.arrayUnion(findsRef.getId()));
                        Toast.makeText(ConfirmDetails.this, "Upload Successful!", Toast.LENGTH_LONG).show();

                        Intent mIntent = new Intent(ConfirmDetails.this, MainActivity.class);
                        startActivity(mIntent);
                        finish();
                        mProgressBar.setVisibility(View.INVISIBLE);
                        errorTv.setText("Upload Successful!");
                        errorTv.setTextColor(getResources().getColor(R.color.green_primary));
                        mUpload.setEnabled(false);
                        mUpload.setVisibility(View.VISIBLE);
                    } else {
                        errorTv.setText("Server Error!\nPlease try again");
                        errorTv.setTextColor(getResources().getColor(R.color.red_primary));
                        errorTv.setVisibility(View.VISIBLE);
                        mUpload.setVisibility(View.VISIBLE);
                        mBAck.setEnabled(true);
                        mProgressBar.setVisibility(View.INVISIBLE);
                        disclaimerDialog.setCancelable(true);
                    }
                }
            });
        }
    }


    @SuppressLint("SetTextI18n")
    private void uploadImages() {

        imagesDocument = mStorageReference.child("person_images");
        errorTv.setText("Processing Images...");

        counter = 0;
        for (int i = 0; i < imageList.size(); i++) {

            final int inf = i;

            if (imageList.get(i).getPath() != null) {

                File imageFile = new File(imageList.get(i).getPath());

                try {
                    image_bitmap = new Compressor(this)
                            .setQuality(75)
                            .setCompressFormat(Bitmap.CompressFormat.WEBP)
                            .compressToBitmap(imageFile);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "ImageCompression failed!" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                image_bitmap.compress(Bitmap.CompressFormat.WEBP, 100, baos);
                byte[] imageByte = baos.toByteArray();


                UploadTask uploadTask = imagesDocument.child(findsRef.getId()).child("image" + inf + ".webp").putBytes(imageByte);

                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        imagesDocument.child(findsRef.getId()).child("image" + inf + ".webp")
                                .getDownloadUrl()
                                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {

                                        counter++;
                                        if (task.isSuccessful() && task.getResult() != null) {
                                            findsRef.update("imageDownloadUrls", FieldValue.arrayUnion(task.getResult().toString()));
                                            errorTv.setText("Uploading image " + (inf + 1) + "/" + imageList.size());
                                            errorTv.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                                            errorTv.setVisibility(View.VISIBLE);

                                        } else {
                                            imagesDocument.child(findsRef.getId()).child("image" + inf + ".webp").delete();
                                            Toast.makeText(ConfirmDetails.this, "ImageUrl Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }

                                        if (counter >= imageList.size() - 1) {

                                            CountDownTimer countDownTimer = new CountDownTimer(10000, 1000) {
                                                @Override
                                                public void onTick(long millisUntilFinished) {
                                                    countDown = (int) (millisUntilFinished / 1000);

                                                    errorTv.setText("Upload Successful!\nRedirecting to Home in " + countDown);
                                                    errorTv.setTextColor(getResources().getColor(R.color.green_primary));
                                                }

                                                @Override
                                                public void onFinish() {
                                                    countDown = 0;
                                                }
                                            }.start();

                                            mUpload.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    disclaimerDialog.dismiss();
                                                    mHandler.removeCallbacksAndMessages(null);
                                                    startActivity(new Intent(ConfirmDetails.this, MainActivity.class));
                                                    finish();
                                                }
                                            });
                                            mProgressBar.setVisibility(View.INVISIBLE);
                                            mUpload.setText("Redirect Now");
                                            mUpload.setVisibility(View.VISIBLE);
                                            mUpload.setEnabled(true);

                                            mHandler = new Handler();

                                            mHandler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    disclaimerDialog.dismiss();
                                                    startActivity(new Intent(ConfirmDetails.this, MainActivity.class));
                                                    finish();
                                                }
                                            }, 10000);


                                        }

                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        counter++;
                        Toast.makeText(ConfirmDetails.this, "Error uploading image " + inf, Toast.LENGTH_SHORT).show();
                    }
                });

            }
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

    private void showDisclaimerDialog() {
        errorTv.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        mUpload.setEnabled(true);
        mUpload.setVisibility(View.VISIBLE);
        mBAck.setVisibility(View.VISIBLE);
        mBAck.setEnabled(true);
        disclaimerDialog.show();
    }


    private void initFindViewByIds() {

        nameTv = findViewById(R.id.nameTv_c);
        ageTv = findViewById(R.id.ageTv_c);
        genderTv = findViewById(R.id.genderTv_c);
        nationalityTv = findViewById(R.id.nationalityTv_c);
        missingSinceTv = findViewById(R.id.missing_sinceTv_c);
        eyeColorTv = findViewById(R.id.eyeTv_c);
        heightTv = findViewById(R.id.heightTv_c);
        weightTv = findViewById(R.id.weightTv_c);
        personalityTv = findViewById(R.id.personalityTv_c);
        publishedOnTv = findViewById(R.id.pubished_onTv_c);
        detailsTv = findViewById(R.id.detailTv_c);
        contactsTv = findViewById(R.id.contactTv_c);


        publishedOnField = findViewById(R.id.pb_c);
        missingSinceField = findViewById(R.id.missing_since_field_c);
        eyeColorField = findViewById(R.id.eye_color_c);
        heightField = findViewById(R.id.height_c);
        weightField = findViewById(R.id.weight_c);
        personalityField = findViewById(R.id.personality_c);
        detailsField = findViewById(R.id.details_c);
        contactsField = findViewById(R.id.contact_c);

        uploadButton = findViewById(R.id.upload_button_c);

    }

}
