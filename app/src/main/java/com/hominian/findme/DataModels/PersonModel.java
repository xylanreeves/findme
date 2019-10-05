package com.hominian.findme.DataModels;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.ServerTimestamp;
import com.google.type.Date;

import java.util.List;

public class PersonModel implements Parcelable{


    private String name;
    private String missingSince;
    private String age;
    private String gender;
    private String personality;
    private String height;
    private String weight;
    private String nationality;
    private String details;
    private String contactDetails;
    private List<Uri> imageList;
    private @ServerTimestamp Date timeStamp;



    public PersonModel(){
        //firebase use
    }

    public PersonModel(String name, String missingSince, String age, String gender, String personality, String height, String weight, String nationality, String details, String contactDetails, List<Uri> imageList, Date timeStamp) {
        this.name = name;
        this.missingSince = missingSince;
        this.age = age;
        this.gender = gender;
        this.personality = personality;
        this.height = height;
        this.weight = weight;
        this.nationality = nationality;
        this.details = details;
        this.contactDetails = contactDetails;
        this.imageList = imageList;
        this.timeStamp = timeStamp;
    }

    public PersonModel(String name, String missingSince, String age, String gender, String personality, String height, String weight, String nationality, String details, String contactDetails, List<Uri> imageList) {
        this.name = name;
        this.missingSince = missingSince;
        this.age = age;
        this.gender = gender;
        this.personality = personality;
        this.height = height;
        this.weight = weight;
        this.nationality = nationality;
        this.details = details;
        this.contactDetails = contactDetails;
        this.imageList = imageList;
    }


    protected PersonModel(Parcel in) {
        name = in.readString();
        missingSince = in.readString();
        age = in.readString();
        gender = in.readString();
        personality = in.readString();
        height = in.readString();
        weight = in.readString();
        nationality = in.readString();
        details = in.readString();
        contactDetails = in.readString();
        imageList = in.createTypedArrayList(Uri.CREATOR);
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

    public Date getTimeStamp() {
        return timeStamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getMissingSince() {
        return missingSince;
    }

    public void setMissingSince(String missingSince) {
        this.missingSince = missingSince;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(missingSince);
        dest.writeString(age);
        dest.writeString(gender);
        dest.writeString(personality);
        dest.writeString(height);
        dest.writeString(weight);
        dest.writeString(nationality);
        dest.writeString(details);
        dest.writeString(contactDetails);
        dest.writeTypedList(imageList);
    }
}
