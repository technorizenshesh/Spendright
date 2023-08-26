package com.my.spendright.act.ui.budget.withdraw.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class UserSuggModel {

    @SerializedName("result")
    @Expose
    private List<Result> result;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
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

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("user_name")
        @Expose
        private String userName;

        @SerializedName("other_legal_name")
        @Expose
        private String otherLegalName;

        @SerializedName("last_legal_name")
        @Expose
        private String lastLegalLame;

        @SerializedName("mobile")
        @Expose
        private String mobile;

        @SerializedName("first_name")
        @Expose
        private String firstName;

        @SerializedName("last_name")
        @Expose
        private String lastName;

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getOtherLegalName() {
            return otherLegalName;
        }

        public void setOtherLegalName(String otherLegalName) {
            this.otherLegalName = otherLegalName;
        }

        public String getLastLegalLame() {
            return lastLegalLame;
        }

        public void setLastLegalLame(String lastLegalLame) {
            this.lastLegalLame = lastLegalLame;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

    }

}