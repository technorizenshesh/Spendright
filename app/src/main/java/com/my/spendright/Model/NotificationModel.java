package com.my.spendright.Model;



import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationModel {

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
        @SerializedName("datetime")
        @Expose
        private String dateTime;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("emoji")
        @Expose
        private String emoji;
        @SerializedName("trx_type")
        @Expose
        private String trxType;
        @SerializedName("vtpass_sub_category_id")
        @Expose
        private String vtpassSubCategoryId;
        @SerializedName("vtpass_booking_last_insert_id")
        @Expose
        private String vtpassBookingLastInsertId;
        @SerializedName("expense_traking_account_id")
        @Expose
        private String expenseTrakingAccountId;
        @SerializedName("expense_traking_category_id")
        @Expose
        private String expenseTrakingCategoryId;
        @SerializedName("flag")
        @Expose
        private String flag;
        @SerializedName("admin_fee")
        @Expose
        private String adminFee;
        @SerializedName("seen_status")
        @Expose
        private String seenStatus;
        @SerializedName("refrence_no")
        @Expose
        private String refrenceNo;
        @SerializedName("service")
        @Expose
        private String service;
        @SerializedName("holder_name")
        @Expose
        private String holderName;
        @SerializedName("cat_name")
        @Expose
        private String catName;


        @SerializedName("notification_type")
        @Expose
        private String notificationType;

        @SerializedName("message")
        @Expose
        private String message;


        @SerializedName("chk")
        @Expose
        private boolean chk = false;


        @SerializedName("image")
        @Expose
        private String image;


        @SerializedName("title")
        @Expose
        private String title;


        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public boolean isChk() {
            return chk;
        }

        public void setChk(boolean chk) {
            this.chk = chk;
        }

        public String getNotificationType() {
            return notificationType;
        }

        public void setNotificationType(String notificationType) {
            this.notificationType = notificationType;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
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

        public String getEmoji() {
            return emoji;
        }

        public void setEmoji(String emoji) {
            this.emoji = emoji;
        }

        public String getTrxType() {
            return trxType;
        }

        public void setTrxType(String trxType) {
            this.trxType = trxType;
        }

        public String getVtpassSubCategoryId() {
            return vtpassSubCategoryId;
        }

        public void setVtpassSubCategoryId(String vtpassSubCategoryId) {
            this.vtpassSubCategoryId = vtpassSubCategoryId;
        }

        public String getVtpassBookingLastInsertId() {
            return vtpassBookingLastInsertId;
        }

        public void setVtpassBookingLastInsertId(String vtpassBookingLastInsertId) {
            this.vtpassBookingLastInsertId = vtpassBookingLastInsertId;
        }

        public String getExpenseTrakingAccountId() {
            return expenseTrakingAccountId;
        }

        public void setExpenseTrakingAccountId(String expenseTrakingAccountId) {
            this.expenseTrakingAccountId = expenseTrakingAccountId;
        }

        public String getExpenseTrakingCategoryId() {
            return expenseTrakingCategoryId;
        }

        public void setExpenseTrakingCategoryId(String expenseTrakingCategoryId) {
            this.expenseTrakingCategoryId = expenseTrakingCategoryId;
        }

        public String getFlag() {
            return flag;
        }

        public void setFlag(String flag) {
            this.flag = flag;
        }

        public String getAdminFee() {
            return adminFee;
        }

        public void setAdminFee(String adminFee) {
            this.adminFee = adminFee;
        }

        public String getSeenStatus() {
            return seenStatus;
        }

        public void setSeenStatus(String seenStatus) {
            this.seenStatus = seenStatus;
        }

        public String getRefrenceNo() {
            return refrenceNo;
        }

        public void setRefrenceNo(String refrenceNo) {
            this.refrenceNo = refrenceNo;
        }

        public String getService() {
            return service;
        }

        public void setService(String service) {
            this.service = service;
        }

        public String getHolderName() {
            return holderName;
        }

        public void setHolderName(String holderName) {
            this.holderName = holderName;
        }

        public String getCatName() {
            return catName;
        }

        public void setCatName(String catName) {
            this.catName = catName;
        }

    }

}

