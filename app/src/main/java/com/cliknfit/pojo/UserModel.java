package com.cliknfit.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.Marker;

import java.io.Serializable;

/**
 * Created by prince on 09/08/17.
 */

public class UserModel implements Serializable {

    /*{
        "username": "Rahul",
            "email": "rahul@gmail.com",
            "login_type": "phone",
            "facebook_id": "",
            "google_id": "",
            "phone": "8252634152",
            "device_id": "546441343433453",
            "device_token": "hb56456565555465464646",
            "device_type": "android",
            "lat": "28.603771",
            "lan": "77.356335",
            "type": 4,
            "status": "0",
            "updated_at": "2017-08-08 22:54:31",
            "created_at": "2017-08-08 22:54:31",
            "id": 13
 "country_code": "",
            "dob": "0000-00-00",
                "name": "Test2",
                "image": "",
                "address": "",
                "per_hr_rate": 0,
                "is_online": 0,
                "age": 0,
                "city": "",
                "about_me": "",
                "additional_info": "",
                "speciality": "",
                "motto": ""

    }*/

    private String username, email,gym_address, login_type, facebook_id, google_id, phone, device_id, device_token, device_type, lat, lan, updated_at, created_at,
            name, image, address, city, about_me, additional_info, speciality, motto,display_phone, country_code, dob,thumb_image,booked_times;

    private int type, id, age, per_hr_rate, is_online, status, is_agree,point,display_point;
    private Marker marker;
    private float rating;

    public String getBooked_times() {
        return booked_times;
    }

    public void setBooked_times(String booked_times) {
        this.booked_times = booked_times;
    }

    public String getDisplay_phone() {
        return display_phone;
    }

    public void setDisplay_phone(String display_phone) {
        this.display_phone = display_phone;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getDisplay_point() {
        return display_point;
    }

    public void setDisplay_point(int display_point) {
        this.display_point = display_point;
    }


    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }



    public String getGym_address() {
        return gym_address;
    }

    public void setGym_address(String gym_address) {
        this.gym_address = gym_address;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIs_agree() {
        return is_agree;
    }

    public void setIs_agree(int is_agree) {
        this.is_agree = is_agree;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin_type() {
        return login_type;
    }

    public void setLogin_type(String login_type) {
        this.login_type = login_type;
    }

    public String getFacebook_id() {
        return facebook_id;
    }

    public void setFacebook_id(String facebook_id) {
        this.facebook_id = facebook_id;
    }

    public String getGoogle_id() {
        return google_id;
    }

    public void setGoogle_id(String google_id) {
        this.google_id = google_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLan() {
        return lan;
    }

    public void setLan(String lan) {
        this.lan = lan;
    }


    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAbout_me() {
        return about_me;
    }

    public void setAbout_me(String about_me) {
        this.about_me = about_me;
    }

    public String getAdditional_info() {
        return additional_info;
    }

    public void setAdditional_info(String additional_info) {
        this.additional_info = additional_info;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getMotto() {
        return motto;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getPer_hr_rate() {
        return per_hr_rate;
    }

    public void setPer_hr_rate(int per_hr_rate) {
        this.per_hr_rate = per_hr_rate;
    }

    public int getIs_online() {
        return is_online;
    }

    public void setIs_online(int is_online) {
        this.is_online = is_online;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }


}
