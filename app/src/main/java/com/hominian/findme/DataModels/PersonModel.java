package com.hominian.findme.DataModels;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.firestore.Exclude;

import java.util.List;

@IgnoreExtraProperties
public class PersonModel implements Parcelable{


    private String name;
    private String personId;
    private String missingSince;
    private String age;
    private String gender;
    private String eyeColor;
    private String personality;
    private String height;
    private String weight;
    private String nationality;
    private String details;
    private String contactDetails;
    @Exclude private List<Uri> imageList;
    private List<String> imageDownloadUrls;
    private Timestamp timeStamp;
    private String uploaderId;

    public PersonModel(String name, String personId, String missingSince, String age, String gender, String eyeColor, String personality, String height, String weight, String nationality, String details, String contactDetails, List<String> imageDownloadUrls, Timestamp timeStamp, String uploaderId) {
        this.name = name;
        this.personId = personId;
        this.missingSince = missingSince;
        this.age = age;
        this.gender = gender;
        this.eyeColor = eyeColor;
        this.personality = personality;
        this.height = height;
        this.weight = weight;
        this.nationality = nationality;
        this.details = details;
        this.contactDetails = contactDetails;
        this.imageDownloadUrls = imageDownloadUrls;
        this.timeStamp = timeStamp;
        this.uploaderId = uploaderId;
    }


    protected PersonModel(Parcel in) {
        name = in.readString();
        personId = in.readString();
        missingSince = in.readString();
        age = in.readString();
        gender = in.readString();
        eyeColor = in.readString();
        personality = in.readString();
        height = in.readString();
        weight = in.readString();
        nationality = in.readString();
        details = in.readString();
        contactDetails = in.readString();
        imageList = in.createTypedArrayList(Uri.CREATOR);
        imageDownloadUrls = in.createStringArrayList();
        timeStamp = in.readParcelable(Timestamp.class.getClassLoader());
        uploaderId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(personId);
        dest.writeString(missingSince);
        dest.writeString(age);
        dest.writeString(gender);
        dest.writeString(eyeColor);
        dest.writeString(personality);
        dest.writeString(height);
        dest.writeString(weight);
        dest.writeString(nationality);
        dest.writeString(details);
        dest.writeString(contactDetails);
        dest.writeTypedList(imageList);
        dest.writeStringList(imageDownloadUrls);
        dest.writeParcelable(timeStamp, flags);
        dest.writeString(uploaderId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PersonModel> CREATOR = new Creator<PersonModel>() {
        @Override
        public PersonModel createFromParcel(Parcel in) {
            return new PersonModel(in);
        }

        @Override
        public PersonModel[] newArray(int size) {
            return new PersonModel[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getMissingSince() {
        return missingSince;
    }

    public void setMissingSince(String missingSince) {
        this.missingSince = missingSince;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEyeColor() {
        return eyeColor;
    }

    public void setEyeColor(String eyeColor) {
        this.eyeColor = eyeColor;
    }

    public String getPersonality() {
        return personality;
    }

    public void setPersonality(String personality) {
        this.personality = personality;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(String contactDetails) {
        this.contactDetails = contactDetails;
    }

    public List<Uri> getImageList() {
        return imageList;
    }

    public void setImageList(List<Uri> imageList) {
        this.imageList = imageList;
    }

    public List<String> getImageDownloadUrls() {
        return imageDownloadUrls;
    }

    public void setImageDownloadUrls(List<String> imageDownloadUrls) {
        this.imageDownloadUrls = imageDownloadUrls;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getUploaderId() {
        return uploaderId;
    }

    public void setUploaderId(String uploaderId) {
        this.uploaderId = uploaderId;
    }

    public PersonModel(){
        //firebase use;
    }


    public PersonModel(String name, String missingSince, String age, String gender, String eyeColor, String personality, String height, String weight, String nationality, String details, String contactDetails, List<Uri> imageList, Timestamp timeStamp, String uploaderId) {
        this.name = name;
        this.missingSince = missingSince;
        this.age = age;
        this.gender = gender;
        this.eyeColor = eyeColor;
        this.personality = personality;
        this.height = height;
        this.weight = weight;
        this.nationality = nationality;
        this.details = details;
        this.contactDetails = contactDetails;
        this.imageList = imageList;
        this.timeStamp = timeStamp;
        this.uploaderId = uploaderId;
    }

    public PersonModel(String name, String missingSince, String age, String gender, String eyeColor, String personality, String height, String weight, String nationality, String details, String contactDetails, List<Uri> imageList) {
        this.name = name;
        this.missingSince = missingSince;
        this.age = age;
        this.gender = gender;
        this.eyeColor = eyeColor;
        this.personality = personality;
        this.height = height;
        this.weight = weight;
        this.nationality = nationality;
        this.details = details;
        this.contactDetails = contactDetails;
        this.imageList = imageList;
    }





    }
