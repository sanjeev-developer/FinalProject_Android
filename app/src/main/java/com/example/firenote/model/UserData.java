package com.example.firenote.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Usernotes")
public class UserData implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    public int id;
    String title;
    String subtitle;
    String audiodata;
    double lat;
    double lng;
    String imagedata;
    String category;
    String datedata;


    public UserData(Parcel in) {
        id = in.readInt();
        title = in.readString();
        subtitle = in.readString();
        audiodata = in.readString();
        lat = in.readDouble();
        lng = in.readDouble();
        imagedata = in.readString();
        category = in.readString();
        datedata = in.readString();
    }

    public static final Creator<UserData> CREATOR = new Creator<UserData>() {
        @Override
        public UserData createFromParcel(Parcel in) {
            return new UserData(in);
        }

        @Override
        public UserData[] newArray(int size) {
            return new UserData[size];
        }
    };

    public UserData() {

    }

    public String getDatedata() {
        return datedata;
    }

    public void setDatedata(String datedata) {
        this.datedata = datedata;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getAudiodata() {
        return audiodata;
    }

    public void setAudiodata(String audiodata) {
        this.audiodata = audiodata;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getImagedata() {
        return imagedata;
    }

    public void setImagedata(String imagedata) {
        this.imagedata = imagedata;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(subtitle);
        dest.writeString(audiodata);
        dest.writeDouble(lat);
        dest.writeDouble(lng);
        dest.writeString(imagedata);
        dest.writeString(category);
        dest.writeString(datedata);
    }
}
