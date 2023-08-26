package com.my.spendright.act.ui.budget.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.my.spendright.Model.GetCommisionModel;

import java.util.ArrayList;

public class MonnifyCommissionModel {
    @SerializedName("result")
    @Expose
    private ArrayList<Result> result;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;

    public ArrayList<Result> getResult() {
        return result;
    }

    public void setResult(ArrayList<Result> result) {
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
        @SerializedName("amount")
        @Expose
        private String amount;
        @SerializedName("monnify_fee")
        @Expose
        private String monnifyFee;
        @SerializedName("admin_fee")
        @Expose
        private String adminFee;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getMonnifyFee() {
            return monnifyFee;
        }

        public void setMonnifyFee(String monnifyFee) {
            this.monnifyFee = monnifyFee;
        }

        public String getAdminFee() {
            return adminFee;
        }

        public void setAdminFee(String adminFee) {
            this.adminFee = adminFee;
        }
    }
}
