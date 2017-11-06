package com.cliknfit.pojo;


import java.util.ArrayList;

/**
 * Created by prince on 28/08/17.
 */

public class ModelMySession {

    private boolean error;
    private String message;
    private ModelMySessionData data;

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

    public ModelMySessionData getData() {
        return data;
    }

    public void setData(ModelMySessionData data) {
        this.data = data;
    }

    public class ModelMySessionData {
        ArrayList<ModelMySessionResults> results;


        public ArrayList<ModelMySessionResults> getResults() {
            return results;
        }

        public void setResults(ArrayList<ModelMySessionResults> results) {
            this.results = results;
        }
    }

}
