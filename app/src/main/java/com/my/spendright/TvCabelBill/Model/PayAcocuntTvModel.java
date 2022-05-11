package com.my.spendright.TvCabelBill.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PayAcocuntTvModel {

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("content")
    @Expose
    private Content content;
    @SerializedName("response_description")
    @Expose
    private String responseDescription;
    @SerializedName("requestId")
    @Expose
    private String requestId;
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

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
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

    public class Content {

        @SerializedName("transactions")
        @Expose
        private Transactions transactions;

        public Transactions getTransactions() {
            return transactions;
        }

        public void setTransactions(Transactions transactions) {
            this.transactions = transactions;
        }
        public class Transactions {

            @SerializedName("status")
            @Expose
            private String status;
            @SerializedName("channel")
            @Expose
            private String channel;
            @SerializedName("transactionId")
            @Expose
            private String transactionId;
            @SerializedName("method")
            @Expose
            private String method;
            @SerializedName("platform")
            @Expose
            private String platform;
            @SerializedName("is_api")
            @Expose
            private Integer isApi;
            @SerializedName("discount")
            @Expose
            private Object discount;
            @SerializedName("customer_id")
            @Expose
            private Integer customerId;
            @SerializedName("email")
            @Expose
            private String email;
            @SerializedName("phone")
            @Expose
            private String phone;
            @SerializedName("type")
            @Expose
            private String type;
            @SerializedName("convinience_fee")
            @Expose
            private String convinienceFee;
            @SerializedName("commission")
            @Expose
            private Double commission;
            @SerializedName("amount")
            @Expose
            private String amount;
            @SerializedName("total_amount")
            @Expose
            private Double totalAmount;
            @SerializedName("quantity")
            @Expose
            private Integer quantity;
            @SerializedName("unit_price")
            @Expose
            private String unitPrice;
            @SerializedName("updated_at")
            @Expose
            private String updatedAt;
            @SerializedName("created_at")
            @Expose
            private String createdAt;
            @SerializedName("id")
            @Expose
            private Integer id;

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getChannel() {
                return channel;
            }

            public void setChannel(String channel) {
                this.channel = channel;
            }

            public String getTransactionId() {
                return transactionId;
            }

            public void setTransactionId(String transactionId) {
                this.transactionId = transactionId;
            }

            public String getMethod() {
                return method;
            }

            public void setMethod(String method) {
                this.method = method;
            }

            public String getPlatform() {
                return platform;
            }

            public void setPlatform(String platform) {
                this.platform = platform;
            }

            public Integer getIsApi() {
                return isApi;
            }

            public void setIsApi(Integer isApi) {
                this.isApi = isApi;
            }

            public Object getDiscount() {
                return discount;
            }

            public void setDiscount(Object discount) {
                this.discount = discount;
            }

            public Integer getCustomerId() {
                return customerId;
            }

            public void setCustomerId(Integer customerId) {
                this.customerId = customerId;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getConvinienceFee() {
                return convinienceFee;
            }

            public void setConvinienceFee(String convinienceFee) {
                this.convinienceFee = convinienceFee;
            }

            public Double getCommission() {
                return commission;
            }

            public void setCommission(Double commission) {
                this.commission = commission;
            }

            public String getAmount() {
                return amount;
            }

            public void setAmount(String amount) {
                this.amount = amount;
            }

            public Double getTotalAmount() {
                return totalAmount;
            }

            public void setTotalAmount(Double totalAmount) {
                this.totalAmount = totalAmount;
            }

            public Integer getQuantity() {
                return quantity;
            }

            public void setQuantity(Integer quantity) {
                this.quantity = quantity;
            }

            public String getUnitPrice() {
                return unitPrice;
            }

            public void setUnitPrice(String unitPrice) {
                this.unitPrice = unitPrice;
            }

            public String getUpdatedAt() {
                return updatedAt;
            }

            public void setUpdatedAt(String updatedAt) {
                this.updatedAt = updatedAt;
            }

            public String getCreatedAt() {
                return createdAt;
            }

            public void setCreatedAt(String createdAt) {
                this.createdAt = createdAt;
            }

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

        }

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


