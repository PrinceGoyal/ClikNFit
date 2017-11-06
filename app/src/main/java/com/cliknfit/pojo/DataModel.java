package com.cliknfit.pojo;

import java.util.ArrayList;

/**
 * Created by prince on 09/08/17.
 */

public class DataModel {

    private UserModel user;

    private ArrayList<QNAModel> results;


    private ArrayList<WorkChildModel> health_problems;
    private ArrayList<WorkChildModel> workout_interests;
    private ArrayList<WorkChildModel> workout_locations;


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

    public ArrayList<QNAModel> getQna() {
        return results;
    }

    public void setQna(ArrayList<QNAModel> qna) {
        this.results = qna;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }
}



