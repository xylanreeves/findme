package com.hominian.findme.DataModels;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Uploader implements Parcelable {

    private String PhoneNumber;
    private String UID;
    private List<String> finds;

    public Uploader() {
    }

    public Uploader(String phoneNumber, String UID, List<String> finds) {
        PhoneNumber = phoneNumber;
        this.UID = UID;
        this.finds = finds;
    }

    protected Uploader(Parcel in) {
        PhoneNumber = in.readString();
        UID = in.readString();
        finds = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(PhoneNumber);
        dest.writeString(UID);
        dest.writeStringList(finds);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Uploader> CREATOR = new Creator<Uploader>() {
        @Override
        public Uploader createFromParcel(Parcel in) {
            return new Uploader(in);
        }

        @Override
        public Uploader[] newArray(int size) {
            return new Uploader[size];
        }
    };


    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public String getUID() {
        return UID;
    }

    public List<String> getFinds() {
        return finds;
    }
}
