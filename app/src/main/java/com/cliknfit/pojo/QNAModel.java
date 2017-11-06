package com.cliknfit.pojo;

import java.util.ArrayList;

/**
 * Created by prince on 10/08/17.
 */

public class QNAModel {

/*"id": 2,
        "title": "How many time per week are you going to gym?",
        "status": 1,
        "created_at": "2017-08-09 20:32:27",
        "updated_at": "2017-08-09 20:32:27",*/

    private ArrayList<UserModel> trainerList;

    private int id, status,is_banner;
    private String title, image, description, created_at, updated_at;

    public int getIs_banner() {
        return is_banner;
    }

    public void setIs_banner(int is_banner) {
        this.is_banner = is_banner;
    }

    public ArrayList<UserModel> getTrainerList() {
        return trainerList;
    }

    public void setTrainerList(ArrayList<UserModel> trainerList) {
        this.trainerList = trainerList;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private ArrayList<QNABEANAnswer> answers;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public ArrayList<QNABEANAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<QNABEANAnswer> answers) {
        this.answers = answers;
    }

    public class QNABEANAnswer {

      /*  "id": 4,
                "question_id": 2,
                "title": "1-3",
                "created_at": "2017-08-09 20:34:41",
                "updated_at": "2017-08-09 20:34:41"*/

        private int id, question_id;
        private String title, created_at, updated_at;
        private boolean check;


        public boolean isCheck() {
            return check;
        }

        public void setCheck(boolean check) {
            this.check = check;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getQuestion_id() {
            return question_id;
        }

        public void setQuestion_id(int question_id) {
            this.question_id = question_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
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


}
