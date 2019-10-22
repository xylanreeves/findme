package com.hominian.findme.Activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hominian.findme.DataModels.PersonModel;
import com.hominian.findme.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import id.zelory.compressor.Compressor;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class EditDetailsActivity extends AppCompatActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks {


    private static final String TAG = "EditDetailsActivity";
    public static final int PERMISSION_CODE = 555;
    public static final String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};

    private Toolbar toolbar;
    private Uri mImageUri;
    private ImageView img1, img2, img3, img4, img5;
    private ImageButton cancelBtn1, cancelBtn2, cancelBtn3, cancelBtn4, cancelBtn5;
    boolean img1Vacant, img2Vacant, img3Vacant, img4Vacant, img5Vacant;


    private Uri[] imageUriList = new Uri[5];
    List<String> imageList;
    private int mView;

    Bitmap image_bitmap;

    //Firebase_instances
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseAuth.AuthStateListener authStateListener;

    //FirebaseFirestore_database
    private FirebaseFirestore rootRef;


    private EditText name;
    private EditText missingSince;
    private EditText age;
    private EditText gender;
    private EditText personalityType;
    private EditText height;
    private EditText weight;
    private EditText nationality;
    private EditText moreDetails;
    private EditText contactDetails;
    private EditText eyeColor;

    private Button confirmDetailsButton;


    FirebaseFirestore db;
    DocumentReference findsRef;
    CollectionReference uploadersRef;
    StorageReference imagesDocument;

    PersonModel mPerson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_details);
        findViewIds();
        initFireStore();
        confirmDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmEditedDetails();
            }
        });


    }


    private void initFireStore(){
        db = FirebaseFirestore.getInstance();
        findsRef = db.collection("finds").document(getIntent().getStringExtra("personId"));
        uploadersRef = db.collection("uploader");
        imagesDocument = FirebaseStorage.getInstance().getReference().child("person_images");
    }

    @Override
    protected void onStart() {
        super.onStart();
        initLayout();
    }



    private void initLayout(){

        findsRef.
                addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        if (snapshot != null && snapshot.exists()) {
                            mPerson = snapshot.toObject(PersonModel.class);
                            if (mPerson != null) {
                                manageImages();
                                manageOtherFields();


                            }

                        } else {
                            Log.d(TAG, "Current data: null");
                            Toast.makeText(EditDetailsActivity.this, "Data Not Found", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }


    private void manageImages(){

        imageList = mPerson.getImageDownloadUrls();

        if (imageList.size() > 0 && imageList.get(0) != null) {
            Glide.with(EditDetailsActivity.this)
                    .load(imageList.get(0))
                    .centerCrop()
                    .into(img1);
            cancelBtn1.setVisibility(View.VISIBLE);
            img1Vacant = false;
        } else {
            img1Vacant = true;
        }


        if (imageList.size() > 1 && imageList.get(1) != null) {
            Glide.with(EditDetailsActivity.this)
                    .load(imageList.get(1))
                    .centerCrop()
                    .into(img2);
            cancelBtn2.setVisibility(View.VISIBLE);
            img2Vacant = false;
        } else {
            img2Vacant = true;
        }


        if (imageList.size() > 2 &&imageList.get(2) != null) {
            Glide.with(EditDetailsActivity.this)
                    .load(imageList.get(2))
                    .centerCrop()
                    .into(img3);
            cancelBtn3.setVisibility(View.VISIBLE);
            img3Vacant = false;
        } else {
            img3Vacant = true;
        }


        if (imageList.size() > 3 && imageList.get(3) != null) {
            Glide.with(EditDetailsActivity.this)
                    .load(imageList.get(3))
                    .centerCrop()
                    .into(img4);
            cancelBtn4.setVisibility(View.VISIBLE);
            img4Vacant = false;
        } else {
            img4Vacant = true;
        }


        if (imageList.size() > 4 && imageList.get(4) != null) {
            Glide.with(EditDetailsActivity.this)
                    .load(imageList.get(4))
                    .centerCrop()
                    .into(img5);
            cancelBtn5.setVisibility(View.VISIBLE);
            img5Vacant = false;
        } else {
            img5Vacant = true;
        }

        img1.setOnClickListener(this);
        img2.setOnClickListener(this);
        img3.setOnClickListener(this);
        img4.setOnClickListener(this);
        img5.setOnClickListener(this);
        cancelBtn1.setOnClickListener(this);
        cancelBtn2.setOnClickListener(this);
        cancelBtn3.setOnClickListener(this);
        cancelBtn4.setOnClickListener(this);
        cancelBtn5.setOnClickListener(this);



    }


    private void manageOtherFields() {

        name.setText(mPerson.getName());
        missingSince.setText(mPerson.getMissingSince());
        age.setText(mPerson.getAge());
        gender.setText(mPerson.getGender());
        personalityType.setText(mPerson.getPersonality());
        height.setText(mPerson.getHeight());
        weight.setText(mPerson.getWeight());
        nationality.setText(mPerson.getNationality());
        moreDetails.setText(mPerson.getDetails());
        contactDetails.setText(mPerson.getContactDetails());
        eyeColor.setText(mPerson.getEyeColor());


    }


    public void confirmEditedDetails() {

        final String mName = name.getText().toString().trim();
        final String mMissingSince = missingSince.getText().toString().trim();
        final String mAge = age.getText().toString().trim();
        final String mGender = gender.getText().toString().trim();
        final String mPersonalityType = personalityType.getText().toString().trim();
        final String mHeight = height.getText().toString().trim();
        final String mWeight = weight.getText().toString().trim();
        final String mNationality = nationality.getText().toString().trim();
        final String mDetails = moreDetails.getText().toString();
        final String mContactDetail = contactDetails.getText().toString().trim();
        final String mEyeColor = eyeColor.getText().toString().trim();

        if (imageList.size() < 1
                && mName.equals("")
                && mMissingSince.equals("")
                && mAge.equals("")
                && mGender.equals("")
                && mPersonalityType.equals("")
                && mHeight.equals("")
                && mWeight.equals("")
                && mNationality.equals("")
                && mDetails.equals("")
                && mContactDetail.equals("")
                && mEyeColor.equals(""))
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("All fields cannot be left blank!")
                    .setPositiveButton("Okay", null);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please confirm the details in the next page.\nMake sure to check for any errors, typos or improvement.")
                .setNegativeButton("Go Back", null)
                .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        PersonModel personData = new PersonModel(
                                mName,
                                mPerson.getPersonId(),
                                mMissingSince,
                                mAge,
                                mGender,
                                mEyeColor,
                                mPersonalityType,
                                mHeight,
                                mWeight,
                                mNationality,
                                mDetails,
                                mContactDetail,
                                imageList,
                                mPerson.getTimeStamp(),
                                mPerson.getUploaderId()
                                );

                        Intent confirmIntent = new Intent(EditDetailsActivity.this, ConfirmEditActivity.class);
                        confirmIntent.putExtra("personData", personData);
                        startActivity(confirmIntent);

                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();


    }


    @Override
    public void onClick(View v) {

        if (v == img1) {
            if (imageList.get(0) == null) {
                mView=0;
                chooseImage();
            }
        } else if (v == img2) {
            if (imageList.get(1) == null) {
                mView=1;
                chooseImage();
            }
        } else if (v == img3) {
            if (imageList.get(2) == null) {
                mView=2;
                chooseImage();
            }
        } else if (v == img4) {
            if (imageList.get(3) == null) {
                mView=3;
                chooseImage();
            }
        } else if (v == img5) {
            if (imageList.get(4) == null) {
                mView=4;
                chooseImage();
            }
        }

        if (v == cancelBtn1) {
            if (imageList.get(0) != null) {
                findsRef.update("imageDownloadUrls", FieldValue.arrayRemove(imageList.get(0)));
                imagesDocument.child(findsRef.getId()).child("image" + 0 + ".webp").delete();
                cancelBtn1.setVisibility(View.INVISIBLE);
            }
        } else if (v == cancelBtn2){
            if (imageList.get(1) != null) {
                findsRef.update("imageDownloadUrls", FieldValue.arrayRemove(imageList.get(1)));
                imagesDocument.child(findsRef.getId()).child("image" + 1 + ".webp").delete();
                cancelBtn2.setVisibility(View.INVISIBLE);
            }
        } else if (v == cancelBtn3){
            if (imageList.get(2) != null) {
                findsRef.update("imageDownloadUrls", FieldValue.arrayRemove(imageList.get(2)));
                imagesDocument.child(findsRef.getId()).child("image" + 2 + ".webp").delete();
                cancelBtn3.setVisibility(View.INVISIBLE);
            }
        } else if (v == cancelBtn4){
            if (imageList.get(3) != null) {
                findsRef.update("imageDownloadUrls", FieldValue.arrayRemove(imageList.get(3)));
                imagesDocument.child(findsRef.getId()).child("image" + 3 + ".webp").delete();
                cancelBtn4.setVisibility(View.INVISIBLE);
            }
        } else if (v == cancelBtn5){
            if (imageList.get(4) != null) {
                findsRef.update("imageDownloadUrls", FieldValue.arrayRemove(imageList.get(4)));
                imagesDocument.child(findsRef.getId()).child("image" + 4 + ".webp").delete();
                cancelBtn5.setVisibility(View.INVISIBLE);
            }
        }



    }

    @AfterPermissionGranted(PERMISSION_CODE)
    private void chooseImage() {
         if (EasyPermissions.hasPermissions(this, PERMISSIONS)) {
            cropImage();
        } else {
            EasyPermissions.requestPermissions(this, "Permission is needed for required action.",
                    PERMISSION_CODE,
                    PERMISSIONS);
        }
    }




    private void cropImage() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(this);
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                mImageUri = result.getUri();

                //imageCompression
                //Upload
                //DownloadImageUrls update

                File imageFile = new File(mImageUri.getPath());

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


                UploadTask uploadTask = imagesDocument.child(findsRef.getId()).child("image" + mView + ".webp").putBytes(imageByte);

                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {


                        if (task.isSuccessful()){
                            imagesDocument.child(findsRef.getId()).child("image" + mView + ".webp")
                                    .getDownloadUrl()
                                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {


                                            if (task.isSuccessful() && task.getResult() != null){
                                                findsRef.update("imageDownloadUrls", FieldValue.arrayUnion(task.getResult().toString()));
                                            } else {
                                                imagesDocument.child(findsRef.getId()).child("image" + mView + ".webp").delete();
                                            }


                                        }
                                    });
                        }
                    }
                });







            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Toast.makeText(this, "Error Occured: " + result.getError() + "Please try again", Toast.LENGTH_SHORT).show();
            }
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);

    }


    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

        if (EasyPermissions.somePermissionPermanentlyDenied(this, Arrays.asList(PERMISSIONS))){
            new AppSettingsDialog.Builder(this).build().show();
        }
    }





    private void findViewIds() {

        //EditTexts and Button
        name = findViewById(R.id.enter_name_id);
        missingSince = findViewById(R.id.missing_id);
        age = findViewById(R.id.age_id);
        gender = findViewById(R.id.gender_id);
        personalityType = findViewById(R.id.personaility_id);
        height = findViewById(R.id.height_id);
        weight = findViewById(R.id.weight_id);
        nationality = findViewById(R.id.nationality_id);
        moreDetails = findViewById(R.id.details_id);
        contactDetails = findViewById(R.id.contact_id);
        eyeColor = findViewById(R.id.eye_id);

        confirmDetailsButton = findViewById(R.id.confirm_btn_edit);


        //cardView_profile_images
        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        img4 = findViewById(R.id.img4);
        img5 = findViewById(R.id.img5);

        //remove_image_buttons
        cancelBtn1 = findViewById(R.id.cancelButton1);
        cancelBtn2 = findViewById(R.id.cancelButton2);
        cancelBtn3 = findViewById(R.id.cancelButton3);
        cancelBtn4 = findViewById(R.id.cancelButton4);
        cancelBtn5 = findViewById(R.id.cancelButton5);
    }


}
