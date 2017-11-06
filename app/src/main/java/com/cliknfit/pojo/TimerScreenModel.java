package com.cliknfit.pojo;

/**
 * Created by prince on 14/09/17.
 */

public class TimerScreenModel {

    private boolean error;
    private String message;
    private TimerScreenData data;

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

    public TimerScreenData getData() {
        return data;
    }

    public void setData(TimerScreenData data) {
        this.data = data;
    }

    public class TimerScreenData{
       private ModelMySessionResults results;

        public ModelMySessionResults getResults() {
            return results;
        }

        public void setResults(ModelMySessionResults results) {
            this.results = results;
        }
    }

}
