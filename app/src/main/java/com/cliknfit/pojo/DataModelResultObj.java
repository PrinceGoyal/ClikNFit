package com.cliknfit.pojo;

import java.util.ArrayList;

/**
 * Created by prince on 11/08/17.
 */

public class DataModelResultObj {


    private WorkOutResult results;
    private UserModel user;
    private boolean is_registered,is_verified,is_login,status,diff_login;
    private int rating,point;

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public boolean isDiff_login() {
        return diff_login;
    }

    public void setDiff_login(boolean diff_login) {
        this.diff_login = diff_login;
    }

    public boolean is_registered() {
        return is_registered;
    }

    public void setIs_registered(boolean is_registered) {
        this.is_registered = is_registered;
    }

    public boolean is_verified() {
        return is_verified;
    }

    public void setIs_verified(boolean is_verified) {
        this.is_verified = is_verified;
    }

    public boolean is_login() {
        return is_login;
    }

    public void setIs_login(boolean is_login) {
        this.is_login = is_login;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public WorkOutResult getResults() {
        return results;
    }

    public void setResults(WorkOutResult results) {
        this.results = results;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }
}



