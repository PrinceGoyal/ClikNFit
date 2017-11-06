package com.cliknfit.pojo;

import java.util.ArrayList;

import static android.R.attr.rating;
import static com.cliknfit.R.id.hours;

/**
 * Created by prince on 11/08/17.
 */

public class WorkOutResult {

    private UserModel user;
    private UserModel trainer;
    private UserModel client;
    private ImagesModel video;
    private String otp;


    private ArrayList<ImagesModel> images;
    private ArrayList<BmIModel> bmi_history;
    private ArrayList<AddressModel> addresses;

    private int rating,point,price;
private float hours;

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public ImagesModel getVideo() {
        return video;
    }

    public void setVideo(ImagesModel video) {
        this.video = video;
    }

    public float getHours() {
        return hours;
    }

    public void setHours(float hours) {
        this.hours = hours;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

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

    public ArrayList<AddressModel> getAddresses() {
        return addresses;
    }

    public void setAddresses(ArrayList<AddressModel> addresses) {
        this.addresses = addresses;
    }

    public UserModel getTrainer() {
        return trainer;
    }

    public void setTrainer(UserModel trainer) {
        this.trainer = trainer;
    }

    public UserModel getClient() {
        return client;
    }

    public void setClient(UserModel client) {
        this.client = client;
    }

    public ArrayList<ImagesModel> getImages() {
        return images;
    }

    public void setImages(ArrayList<ImagesModel> images) {
        this.images = images;
    }

    public ArrayList<BmIModel> getBmi_history() {
        return bmi_history;
    }

    public void setBmi_history(ArrayList<BmIModel> bmi_history) {
        this.bmi_history = bmi_history;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    private ArrayList<WorkChildModel> health_problems;
    private ArrayList<WorkChildModel> workout_interests;
    private ArrayList<WorkChildModel> workout_programs;
    private ArrayList<WorkChildModel> workout_locations;

    public ArrayList<WorkChildModel> getWorkout_programs() {
        return workout_programs;
    }

    public void setWorkout_programs(ArrayList<WorkChildModel> workout_programs) {
        this.workout_programs = workout_programs;
    }

    public ArrayList<WorkChildModel> getHealth_problems() {
        return health_problems;
    }

    public void setHealth_problems(ArrayList<WorkChildModel> health_problems) {
        this.health_problems = health_problems;
    }

    public ArrayList<WorkChildModel> getWorkout_interests() {
        return workout_interests;
    }

    public void setWorkout_interests(ArrayList<WorkChildModel> workout_interests) {
        this.workout_interests = workout_interests;
    }

    public ArrayList<WorkChildModel> getWorkout_locations() {
        return workout_locations;
    }

    public void setWorkout_locations(ArrayList<WorkChildModel> workout_locations) {
        this.workout_locations = workout_locations;
    }
}
