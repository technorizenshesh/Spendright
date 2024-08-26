package com.my.spendright.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetCommisionModel {

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

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("category_name")
        @Expose
        private String categoryName;
        @SerializedName("sub_category_name")
        @Expose
        private String subCategoryName;
        @SerializedName("commision_amount")
        @Expose
        private String commisionAmount;

        @SerializedName("discount")
        @Expose
        private String discount;

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public String getSubCategoryName() {
            return subCategoryName;
        }

        public void setSubCategoryName(String subCategoryName) {
            this.subCategoryName = subCategoryName;
        }

        public String getCommisionAmount() {
            return commisionAmount;
        }

        public void setCommisionAmount(String commisionAmount) {
            this.commisionAmount = commisionAmount;
        }

    }
}

