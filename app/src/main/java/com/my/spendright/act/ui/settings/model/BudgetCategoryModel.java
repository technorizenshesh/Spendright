package com.my.spendright.act.ui.settings.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BudgetCategoryModel {

    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private List<Data> data;



    public String getResult() {
        return result;
    }

    public void setResult(String result) {
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

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public class Data {

        @SerializedName("bcat_id")
        @Expose
        private String bcatId;



        @SerializedName("bcat_category_id")
        @Expose
        private String bcatCategoryId;

        @SerializedName("bcat_category_name")
        @Expose
        private String bcatCategoryName;
        @SerializedName("bcat_status")
        @Expose
        private String bcatStatus;
        @SerializedName("bcat_budget_amount")
        @Expose
        private String bcatBudgetAmount;
        @SerializedName("bcat_user_id")
        @Expose
        private String bcatUserId;
        @SerializedName("bcat_due_date")
        @Expose
        private String bcatDueDate;

        @SerializedName("bcat_group_id")
        @Expose
        private String bcatGroupId;

        @SerializedName("bcat_category_emoji")
        @Expose
        private String bcatCategoryEmoji;


       @SerializedName("bcat_unlock_share_status")
        @Expose
        private String bcatUnlockShareStatus = "0";


        public String getBcatUnlockShareStatus() {
            return bcatUnlockShareStatus;
        }

        public void setBcatUnlockShareStatus(String bcatUnlockShareStatus) {
            this.bcatUnlockShareStatus = bcatUnlockShareStatus;
        }

        public String getBcatCategoryEmoji() {
            return bcatCategoryEmoji;
        }

        public void setBcatCategoryEmoji(String bcatCategoryEmoji) {
            this.bcatCategoryEmoji = bcatCategoryEmoji;
        }

        @SerializedName("chk")
        @Expose
        private boolean chk=false;

        public boolean isChk() {
            return chk;
        }

        public void setChk(boolean chk) {
            this.chk = chk;
        }

        public String getBcatId() {
            return bcatId;
        }

        public void setBcatId(String bcatId) {
            this.bcatId = bcatId;
        }

        public String getBcatCategoryId() {
            return bcatCategoryId;
        }

        public void setBcatCategoryId(String bcatCategoryId) {
            this.bcatCategoryId = bcatCategoryId;
        }

        public String getBcatStatus() {
            return bcatStatus;
        }

        public void setBcatStatus(String bcatStatus) {
            this.bcatStatus = bcatStatus;
        }

        public String getBcatBudgetAmount() {
            return bcatBudgetAmount;
        }

        public void setBcatBudgetAmount(String bcatBudgetAmount) {
            this.bcatBudgetAmount = bcatBudgetAmount;
        }

        public String getBcatUserId() {
            return bcatUserId;
        }

        public void setBcatUserId(String bcatUserId) {
            this.bcatUserId = bcatUserId;
        }

        public String getBcatDueDate() {
            return bcatDueDate;
        }

        public void setBcatDueDate(String bcatDueDate) {
            this.bcatDueDate = bcatDueDate;
        }


        public String getBcatGroupId() {
            return bcatGroupId;
        }

        public void setBcatGroupId(String bcatGroupId) {
            this.bcatGroupId = bcatGroupId;
        }

        public String getBcatCategoryName() {
            return bcatCategoryName;
        }

        public void setBcatCategoryName(String bcatCategoryName) {
            this.bcatCategoryName = bcatCategoryName;
        }
    }

}

