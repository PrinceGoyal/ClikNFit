package com.cliknfit.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by prince on 08/08/17.
 */

public class CommonStatus implements Parcelable {

    private boolean error;
    private String message;
    private DataModel data;


    protected CommonStatus(Parcel in) {
        error = in.readByte() != 0;
        message = in.readString();
    }

    public static final Creator<CommonStatus> CREATOR = new Creator<CommonStatus>() {
        @Override
        public CommonStatus createFromParcel(Parcel in) {
            return new CommonStatus(in);
        }

        @Override
        public CommonStatus[] newArray(int size) {
            return new CommonStatus[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (error ? 1 : 0));
        parcel.writeString(message);
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getmessage() {
        return message;
    }

    public void setmessage(String message) {
        this.message = message;
    }

    public DataModel getData() {
        return data;
    }

    public void setData(DataModel data) {
        this.data = data;
    }
}
