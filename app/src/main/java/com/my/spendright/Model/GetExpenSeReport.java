package com.my.spendright.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class GetExpenSeReport implements Serializable {

    @SerializedName("result")
    @Expose
    private List<Result> result = null;
    @SerializedName("total_income")
    @Expose
    private String totalIncome;
    @SerializedName("total_expense")
    @Expose
    private String totalExpense;
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

    public String getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(String totalIncome) {
        this.totalIncome = totalIncome;
    }

    public String getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(String totalExpense) {
        this.totalExpense = totalExpense;
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

    public class Result implements Serializable {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("account_budget_id")
        @Expose
        private String accountBudgetId;
        @SerializedName("pay_name")
        @Expose
        private String payName;
        @SerializedName("transaction_amount")
        @Expose
        private String transactionAmount;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("main_category_id")
        @Expose
        private String mainCategoryId;
        @SerializedName("main_category_name")
        @Expose
        private String mainCategoryName;
        @SerializedName("date_time")
        @Expose
        private String dateTime;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("emoji")
        @Expose
        private String emoji;

        @SerializedName("refrence_no")
        @Expose
        private String refrence_no;

        @SerializedName("service")
        @Expose
        private String service;

        @SerializedName("flag")
        @Expose
        private String flag;

        @SerializedName("cat_name")
        @Expose
        private String cat_name;

        @SerializedName("admin_fee")
        @Expose
        private String adminFee;

        public String getAdminFee() {
            return adminFee;
        }

        public void setAdminFee(String adminFee) {
            this.adminFee = adminFee;
        }

        public String getCat_name() {
            return cat_name;
        }

        public void setCat_name(String cat_name) {
            this.cat_name = cat_name;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public String getService() {
            return service;
        }

        public void setService(String service) {
            this.service = service;
        }

        public String getRefrence_no() {
            return refrence_no;
        }

        public void setRefrence_no(String refrence_no) {
            this.refrence_no = refrence_no;
        }

        public String getEmoji() {
            return emoji;
        }

        public void setEmoji(String emoji) {
            this.emoji = emoji;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getAccountBudgetId() {
            return accountBudgetId;
        }

        public void setAccountBudgetId(String accountBudgetId) {
            this.accountBudgetId = accountBudgetId;
        }

        public String getPayName() {
            return payName;
        }

        public void setPayName(String payName) {
            this.payName = payName;
        }

        public String getTransactionAmount() {
            return transactionAmount;
        }

        public void setTransactionAmount(String transactionAmount) {
            this.transactionAmount = transactionAmount;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getMainCategoryId() {
            return mainCategoryId;
        }

        public void setMainCategoryId(String mainCategoryId) {
            this.mainCategoryId = mainCategoryId;
        }

        public String getMainCategoryName() {
            return mainCategoryName;
        }

        public void setMainCategoryName(String mainCategoryName) {
            this.mainCategoryName = mainCategoryName;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

    }
}

