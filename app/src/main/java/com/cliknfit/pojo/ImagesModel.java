package com.cliknfit.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by prince on 11/08/17.
 */

public class ImagesModel implements Parcelable{

       /*"id": 1,
               "user_id": 14,
               "image": "2017_08_09_11_08_35_android_120x120 - Copy.jpg",
               "created_at": "2017-08-09 11:35:37",
               "updated_at": "2017-08-09 11:35:37"*/


    private String id, user_id, image, created_at, updated_at,video;

    protected ImagesModel(Parcel in) {
        id = in.readString();
        user_id = in.readString();
        image = in.readString();
        created_at = in.readString();
        updated_at = in.readString();
        video = in.readString();
    }

    public static final Creator<ImagesModel> CREATOR = new Creator<ImagesModel>() {
        @Override
        public ImagesModel createFromParcel(Parcel in) {
            return new ImagesModel(in);
        }

        @Override
        public ImagesModel[] newArray(int size) {
            return new ImagesModel[size];
        }
    };

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(user_id);
        parcel.writeString(image);
        parcel.writeString(created_at);
        parcel.writeString(updated_at);
        parcel.writeString(video);
    }
}
