package com.cliknfit.pojo;

/**
 * Created by prince on 11/08/17.
 */

public class BmIModel {

  /*  "id": 3,
            "user_id": 14,
            "current_weight": "test",
            "target_weight": "test",
            "bmi": "test",
            "body_fat": "test",
            "created_at": "2017-08-09 11:29:34",
            "updated_at": "2017-08-09 11:29:34"*/


    private String id, user_id, current_weight, target_weight, bmi, body_fat, created_at, updated_at;


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

    public String getCurrent_weight() {
        return current_weight;
    }

    public void setCurrent_weight(String current_weight) {
        this.current_weight = current_weight;
    }

    public String getTarget_weight() {
        return target_weight;
    }

    public void setTarget_weight(String target_weight) {
        this.target_weight = target_weight;
    }

    public String getBmi() {
        return bmi;
    }

    public void setBmi(String bmi) {
        this.bmi = bmi;
    }

    public String getBody_fat() {
        return body_fat;
    }

    public void setBody_fat(String body_fat) {
        this.body_fat = body_fat;
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
}
