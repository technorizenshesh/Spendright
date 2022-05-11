package com.my.spendright.airetime.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PayAirtimeModel {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("response_description")
    @Expose
    private String responseDescription;
    @SerializedName("requestId")
    @Expose
    private String requestId;
    @SerializedName("transactionId")
    @Expose
    private String transactionId;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("transaction_date")
    @Expose
    private TransactionDate transactionDate;
    @SerializedName("purchased_code")
    @Expose
    private String purchasedCode;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getResponseDescription() {
        return responseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public TransactionDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(TransactionDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getPurchasedCode() {
        return purchasedCode;
    }

    public void setPurchasedCode(String purchasedCode) {
        this.purchasedCode = purchasedCode;
    }

    public class TransactionDate {

        @SerializedName("date")
        @Expose
        private String date;
        @SerializedName("timezone_type")
        @Expose
        private Integer timezoneType;
        @SerializedName("timezone")
        @Expose
        private String timezone;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public Integer getTimezoneType() {
            return timezoneType;
        }

        public void setTimezoneType(Integer timezoneType) {
            this.timezoneType = timezoneType;
        }

        public String getTimezone() {
            return timezone;
        }

        public void setTimezone(String timezone) {
            this.timezone = timezone;
        }

    }
}

