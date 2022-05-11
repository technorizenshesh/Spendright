package com.my.spendright.TvCabelBill.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetMerchatAcocuntTv {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("content")
    @Expose
    private Content content;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public class Content {

        @SerializedName("Customer_Name")
        @Expose
        private String customerName;
        @SerializedName("Status")
        @Expose
        private String status;
        @SerializedName("DUE_DATE")
        @Expose
        private String dueDate;
        @SerializedName("Customer_Number")
        @Expose
        private Integer customerNumber;
        @SerializedName("Customer_Type")
        @Expose
        private String customerType;
        @SerializedName("Current_Bouquet")
        @Expose
        private String currentBouquet;
        @SerializedName("Current_Bouquet_Code")
        @Expose
        private String currentBouquetCode;
        @SerializedName("Renewal_Amount")
        @Expose
        private Integer renewalAmount;

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getDueDate() {
            return dueDate;
        }

        public void setDueDate(String dueDate) {
            this.dueDate = dueDate;
        }

        public Integer getCustomerNumber() {
            return customerNumber;
        }

        public void setCustomerNumber(Integer customerNumber) {
            this.customerNumber = customerNumber;
        }

        public String getCustomerType() {
            return customerType;
        }

        public void setCustomerType(String customerType) {
            this.customerType = customerType;
        }

        public String getCurrentBouquet() {
            return currentBouquet;
        }

        public void setCurrentBouquet(String currentBouquet) {
            this.currentBouquet = currentBouquet;
        }

        public String getCurrentBouquetCode() {
            return currentBouquetCode;
        }

        public void setCurrentBouquetCode(String currentBouquetCode) {
            this.currentBouquetCode = currentBouquetCode;
        }

        public Integer getRenewalAmount() {
            return renewalAmount;
        }

        public void setRenewalAmount(Integer renewalAmount) {
            this.renewalAmount = renewalAmount;
        }

    }
}