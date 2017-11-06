package com.cliknfit.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by prince on 11/08/17.
 */

public class CommonStatusResultObj implements Parcelable {

    private boolean error;
    private String message;
    private DataModelResultObj data;


    protected CommonStatusResultObj(Parcel in) {
        error = in.readByte() != 0;
        message = in.readString();
    }

    public static final Creator<CommonStatusResultObj> CREATOR = new Creator<CommonStatusResultObj>() {
        @Override
        public CommonStatusResultObj createFromParcel(Parcel in) {
            return new CommonStatusResultObj(in);
        }

        @Override
        public CommonStatusResultObj[] newArray(int size) {
            return new CommonStatusResultObj[size];
        }
    };

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataModelResultObj getData() {
        return data;
    }

    public void setData(DataModelResultObj data) {
        this.data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (error ? 1 : 0));
        parcel.writeString(message);
    }
}
