package com.my.spendright.utils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddCategoryModel {

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
        @SerializedName("account_id")
        @Expose
        private String accountId;
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("group_id")
        @Expose
        private String groupId;
        @SerializedName("category_name")
        @Expose
        private String categoryName;
        @SerializedName("amount")
        @Expose
        private String amount;
        @SerializedName("select_end__day_month_week")
        @Expose
        private String selectEndDayMonthWeek;
        @SerializedName("select_month_week")
        @Expose
        private String selectMonthWeek;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAccountId() {
            return accountId;
        }

        public void setAccountId(String accountId) {
            this.accountId = accountId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getSelectEndDayMonthWeek() {
            return selectEndDayMonthWeek;
        }

        public void setSelectEndDayMonthWeek(String selectEndDayMonthWeek) {
            this.selectEndDayMonthWeek = selectEndDayMonthWeek;
        }

        public String getSelectMonthWeek() {
            return selectMonthWeek;
        }

        public void setSelectMonthWeek(String selectMonthWeek) {
            this.selectMonthWeek = selectMonthWeek;
        }

    }

}

