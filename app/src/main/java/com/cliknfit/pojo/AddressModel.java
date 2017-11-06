package com.cliknfit.pojo;

/**
 * Created by prince on 25/08/17.
 */

public class AddressModel {

    /*"id": 4,
            "user_id": 18,
            "address": "#23, Sector 23, Noida City Center, Noida, UP",
            "created_at": "2017-08-24 08:44:19",
            "updated_at": "2017-08-24 08:44:19"*/

    private String address,created_at,updated_at;
    private int id,user_id;


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
