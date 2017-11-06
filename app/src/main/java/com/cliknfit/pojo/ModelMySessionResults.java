package com.cliknfit.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by prince on 05/09/17.
 */

public class ModelMySessionResults implements Serializable {

    /*"id": 58,
            "client_id": 17,
            "trainer_id": 93,
            "no_of_people": 5,
            "start_time": "10:00 AM",
            "end_time": "00:01 AM",
            "hour": 0,
            "actual_start_time": "00:00:00 AM",
            "actual_end_time": "00:00:00 AM",
            "warm_up_start_time": "00:00:00 AM",
            "warm_up_end_time": "00:00:00 AM",
            "cool_start_time": "00:00:00 AM",
            "cool_end_time": "00:00:00 AM",
            "break_start_time": "00:00:00 AM",
            "break_end_time": "00:00:00 AM",
            "is_trainer_started": 0,
            "is_client_started": 0,
            "workoutlocation_id": 0,
            "location_tag": "Custom address",
            "location_address": "Noida sector 62.",
            "price": 65,
            "display_point": 5,
            "point": 0,
            "commission": 5,
            "comment": "",
            "status": 2,
            "created_at": "2017-08-28 02:48:12",
            "updated_at": "2017-08-28 03:56:52",

             "trainer": {
                        "id": 93,
                        "name": "Rajendra",
                        "email": "rajendra@gmail.com",
                        "image": "",
                        "phone": "9632587412",
                        "device_id": "355463060593804",
                        "device_token": "fErz4WDW_1g:APA91bECAbY8U6ytVOcijlPYzWlBd_edxne2EobJmZB8iinMURg_3xPybi1KWhAApCzX6Lib23P-E_qbUNa_vzoDQ9h9V_xmxKf7umdTcLcPM5vIISY-ZbNRjFgLwjU_Qq6ijlj-BUZo",
                        "device_type": "android",
                        "address": "",
                        "age": 0,
                        "city": "California",
                        "about_me": "I am the best trainer for weight loss.",
                        "additional_info": "Nothing more to say."
                    }

            */
    private UserModel trainer;
    private int id, client_id, trainer_id, no_of_people, is_trainer_started, is_client_started, workoutlocation_id,
            price,total_price, point, commission, status, display_point, warm_up_min, break_min, cool_min,
            actual_break_min, actual_cool_min, actual_warm_up_min,is_trainer_complete,is_client_complete,is_emergency_stop,has_additional_hours,req_client_total_price;
    private float hour,req_total_hours;

    private TrainerRating trainerrating;
    private ModelMySessionResults additional_hours;
    private String start_time, end_time, actual_start_time, actual_end_time, location_tag,
            location_address, comment, created_at, updated_at, warm_up_start_time, warm_up_end_time, cool_start_time,
            cool_end_time, break_start_time, break_end_time,trainer_rating,trainer_name,client_name,client_image,trainer_image;

    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }

    public void setTrainerrating(TrainerRating trainerrating) {
        this.trainerrating = trainerrating;
    }

    public int getReq_client_total_price() {
        return req_client_total_price;
    }

    public void setReq_client_total_price(int req_client_total_price) {
        this.req_client_total_price = req_client_total_price;
    }

    public float getReq_total_hours() {
        return req_total_hours;
    }

    public void setReq_total_hours(float req_total_hours) {
        this.req_total_hours = req_total_hours;
    }

    public String getTrainer_name() {
        return trainer_name;
    }

    public void setTrainer_name(String trainer_name) {
        this.trainer_name = trainer_name;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getClient_image() {
        return client_image;
    }

    public void setClient_image(String client_image) {
        this.client_image = client_image;
    }

    public String getTrainer_image() {
        return trainer_image;
    }

    public void setTrainer_image(String trainer_image) {
        this.trainer_image = trainer_image;
    }

    public String getTrainer_rating() {
        return trainer_rating;
    }

    public void setTrainer_rating(String trainer_rating) {
        this.trainer_rating = trainer_rating;
    }

    public int getIs_trainer_complete() {
        return is_trainer_complete;
    }

    public void setIs_trainer_complete(int is_trainer_complete) {
        this.is_trainer_complete = is_trainer_complete;
    }

    public int getIs_client_complete() {
        return is_client_complete;
    }

    public void setIs_client_complete(int is_client_complete) {
        this.is_client_complete = is_client_complete;
    }

    public int getIs_emergency_stop() {
        return is_emergency_stop;
    }

    public void setIs_emergency_stop(int is_emergency_stop) {
        this.is_emergency_stop = is_emergency_stop;
    }


    public int getDisplay_point() {
        return display_point;
    }

    public void setDisplay_point(int display_point) {
        this.display_point = display_point;
    }


    public UserModel getTrainer() {
        return trainer;
    }

    public void setTrainer(UserModel trainer) {
        this.trainer = trainer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    public int getTrainer_id() {
        return trainer_id;
    }

    public void setTrainer_id(int trainer_id) {
        this.trainer_id = trainer_id;
    }

    public int getNo_of_people() {
        return no_of_people;
    }

    public void setNo_of_people(int no_of_people) {
        this.no_of_people = no_of_people;
    }

    public int getIs_trainer_started() {
        return is_trainer_started;
    }

    public void setIs_trainer_started(int is_trainer_started) {
        this.is_trainer_started = is_trainer_started;
    }

    public int getIs_client_started() {
        return is_client_started;
    }

    public void setIs_client_started(int is_client_started) {
        this.is_client_started = is_client_started;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public int getWorkoutlocation_id() {
        return workoutlocation_id;
    }

    public void setWorkoutlocation_id(int workoutlocation_id) {
        this.workoutlocation_id = workoutlocation_id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getCommission() {
        return commission;
    }

    public void setCommission(int commission) {
        this.commission = commission;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public float getHour() {
        return hour;
    }

    public void setHour(float hour) {
        this.hour = hour;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getLocation_tag() {
        return location_tag;
    }

    public void setLocation_tag(String location_tag) {
        this.location_tag = location_tag;
    }

    public String getLocation_address() {
        return location_address;
    }

    public void setLocation_address(String location_address) {
        this.location_address = location_address;
    }

    public String getActual_start_time() {
        return actual_start_time;
    }

    public void setActual_start_time(String actual_start_time) {
        this.actual_start_time = actual_start_time;
    }

    public String getActual_end_time() {
        return actual_end_time;
    }

    public void setActual_end_time(String actual_end_time) {
        this.actual_end_time = actual_end_time;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public String getWarm_up_start_time() {
        return warm_up_start_time;
    }

    public void setWarm_up_start_time(String warm_up_start_time) {
        this.warm_up_start_time = warm_up_start_time;
    }

    public String getWarm_up_end_time() {
        return warm_up_end_time;
    }

    public void setWarm_up_end_time(String warm_up_end_time) {
        this.warm_up_end_time = warm_up_end_time;
    }

    public String getCool_start_time() {
        return cool_start_time;
    }

    public void setCool_start_time(String cool_start_time) {
        this.cool_start_time = cool_start_time;
    }

    public String getCool_end_time() {
        return cool_end_time;
    }

    public void setCool_end_time(String cool_end_time) {
        this.cool_end_time = cool_end_time;
    }

    public String getBreak_start_time() {
        return break_start_time;
    }

    public void setBreak_start_time(String break_start_time) {
        this.break_start_time = break_start_time;
    }

    public String getBreak_end_time() {
        return break_end_time;
    }

    public void setBreak_end_time(String break_end_time) {
        this.break_end_time = break_end_time;
    }

    public int getWarm_up_min() {
        return warm_up_min;
    }

    public void setWarm_up_min(int warm_up_min) {
        this.warm_up_min = warm_up_min;
    }

    public int getBreak_min() {
        return break_min;
    }

    public void setBreak_min(int break_min) {
        this.break_min = break_min;
    }

    public int getCool_min() {
        return cool_min;
    }

    public void setCool_min(int cool_min) {
        this.cool_min = cool_min;
    }

    public int getActual_break_min() {
        return actual_break_min;
    }

    public void setActual_break_min(int actual_break_min) {
        this.actual_break_min = actual_break_min;
    }

    public int getActual_cool_min() {
        return actual_cool_min;
    }

    public void setActual_cool_min(int actual_cool_min) {
        this.actual_cool_min = actual_cool_min;
    }

    public int getActual_warm_up_min() {
        return actual_warm_up_min;
    }

    public void setActual_warm_up_min(int actual_warm_up_min) {
        this.actual_warm_up_min = actual_warm_up_min;
    }

    public class TrainerRating implements Serializable {
        String rating,comment,notes;

        public String getRating() {
            return rating;
        }

        public void setRating(String rating) {
            this.rating = rating;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }
    }

    public int getHas_additional_hours() {
        return has_additional_hours;
    }

    public void setHas_additional_hours(int has_additional_hours) {
        this.has_additional_hours = has_additional_hours;
    }

    public ModelMySessionResults getAdditional_hours() {
        return additional_hours;
    }

    public void setAdditional_hours(ModelMySessionResults additional_hours) {
        this.additional_hours = additional_hours;
    }
}
