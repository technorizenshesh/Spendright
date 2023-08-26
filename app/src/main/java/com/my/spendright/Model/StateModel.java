package com.my.spendright.Model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



public class StateModel {

    @SerializedName("result")
    @Expose
    private Result result;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public class Result {

        @SerializedName("states")
        @Expose
        private List<State> states;

        public List<State> getStates() {
            return states;
        }

        public void setStates(List<State> states) {
            this.states = states;
        }
        public class State {

            @SerializedName("value")
            @Expose
            private String value;
            @SerializedName("name")
            @Expose
            private String name;

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

        }


    }


}