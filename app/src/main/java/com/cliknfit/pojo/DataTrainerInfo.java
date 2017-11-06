package com.cliknfit.pojo;

import java.util.ArrayList;

/**
 * Created by prince on 23/08/17.
 */

public class DataTrainerInfo {

    private boolean error;
    private String message;
    private DataObj data;

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

    public DataObj getData() {
        return data;
    }

    public void setData(DataObj data) {
        this.data = data;
    }


    public class DataObj {
        private OtherObject other;

        private ArrayList<UserModel> results;

        public ArrayList<UserModel> getResults() {
            return results;
        }

        public void setResults(ArrayList<UserModel> results) {
            this.results = results;
        }

        public OtherObject getOther() {
            return other;
        }

        public void setOther(OtherObject other) {
            this.other = other;
        }
    }

    public class OtherObject {
        private LatestnewsFeed latest_newsfeed;

        public LatestnewsFeed getLatest_newsfeed() {
            return latest_newsfeed;
        }

        public void setLatest_newsfeed(LatestnewsFeed latest_newsfeed) {
            this.latest_newsfeed = latest_newsfeed;
        }
    }

    public class LatestnewsFeed {
        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }
}
