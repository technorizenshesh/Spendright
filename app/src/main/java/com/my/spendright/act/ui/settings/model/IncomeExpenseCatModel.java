package com.my.spendright.act.ui.settings.model;



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class IncomeExpenseCatModel implements Serializable {

    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("categories")
    @Expose
    private List<Category> categories;

    @SerializedName("admin_minimum_amount_to_be_added")
    @Expose
    private String adminMinimumAmount;

    public String getAdminMinimumAmount() {
        return adminMinimumAmount;
    }

    public void setAdminMinimumAmount(String adminMinimumAmount) {
        this.adminMinimumAmount = adminMinimumAmount;
    }


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

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public class Category implements Serializable{

        @SerializedName("cat_id")
        @Expose
        private String catId;
        @SerializedName("cat_name")
        @Expose
        private String catName;
        @SerializedName("cat_type")
        @Expose
        private String catType;
        @SerializedName("cat_user_id")
        @Expose
        private String catUserId;

        @SerializedName("cat_emoji")
        @Expose
        private String catEmoji;


        @SerializedName("chk")
        @Expose
        private boolean chk = false;

        public boolean isChk() {
            return chk;
        }

        public void setChk(boolean chk) {
            this.chk = chk;
        }

        public String getCatEmoji() {
            return catEmoji;
        }

        public void setCatEmoji(String catEmoji) {
            this.catEmoji = catEmoji;
        }

        public String getCatId() {
            return catId;
        }

        public void setCatId(String catId) {
            this.catId = catId;
        }

        public String getCatName() {
            return catName;
        }

        public void setCatName(String catName) {
            this.catName = catName;
        }

        public String getCatType() {
            return catType;
        }

        public void setCatType(String catType) {
            this.catType = catType;
        }


        public String getCatUserId() {
            return catUserId;
        }

        public void setCatUserId(String catUserId) {
            this.catUserId = catUserId;
        }

    }



}
