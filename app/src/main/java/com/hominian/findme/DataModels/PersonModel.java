package com.hominian.findme.DataModels;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.firestore.Exclude;

import java.util.List;

@IgnoreExtraProperties
public class PersonModel implements Parcelable {


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
    @Exclude
    private List<Uri> imageList;
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


    public String getMissingSince() {
        return missingSince;
    }


    public String getAge() {
        return age;
    }


    public String getEyeColor() {
        return eyeColor;
    }


    public String getPersonality() {
        return personality;
    }


    public String getHeight() {
        return height;
    }


    public String getWeight() {
        return weight;
    }


    public String getNationality() {
        return nationality;
    }


    public String getDetails() {
        return details;
    }


    public String getContactDetails() {
        return contactDetails;
    }


    public List<Uri> getImageList() {
        return imageList;
    }

    public List<String> getImageDownloadUrls() {
        return imageDownloadUrls;
    }

    public String getPersonId() {
        return personId;
    }

    public String getGender() {
        return gender;
    }

    public Timestamp getTimeStamp() {
        return timeStamp;
    }


    public String getUploaderId() {
        return uploaderId;
    }


    public PersonModel() {
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

    @Override
    public String toString() {
        return "PersonModel{" +
                "name='" + name + '\'' +
                ", personId='" + personId + '\'' +
                ", missingSince='" + missingSince + '\'' +
                ", age='" + age + '\'' +
                ", gender='" + gender + '\'' +
                ", eyeColor='" + eyeColor + '\'' +
                ", personality='" + personality + '\'' +
                ", height='" + height + '\'' +
                ", weight='" + weight + '\'' +
                ", nationality='" + nationality + '\'' +
                ", details='" + details + '\'' +
                ", contactDetails='" + contactDetails + '\'' +
                ", imageDownloadUrls=" + imageDownloadUrls +
                ", timeStamp=" + timeStamp +
                ", uploaderId='" + uploaderId + '\'' +
                '}';
    }
}
