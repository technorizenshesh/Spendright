package com.my.spendright.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetMyPercentageModel {

    @SerializedName("Account_detail")
    @Expose
    private List<AccountDetail> accountDetail = null;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;

    public List<AccountDetail> getAccountDetail() {
        return accountDetail;
    }

    public void setAccountDetail(List<AccountDetail> accountDetail) {
        this.accountDetail = accountDetail;
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


    public class AccountDetail {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("sub_cat_name")
        @Expose
        private String subCatName;
        @SerializedName("category_percentage")
        @Expose
        private String categoryPercentage;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSubCatName() {
            return subCatName;
        }

        public void setSubCatName(String subCatName) {
            this.subCatName = subCatName;
        }

        public String getCategoryPercentage() {
            return categoryPercentage;
        }

        public void setCategoryPercentage(String categoryPercentage) {
            this.categoryPercentage = categoryPercentage;
        }

    }

}