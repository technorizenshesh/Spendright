package com.my.spendright.ElectircalBill.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PayFinalModel {

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
    @SerializedName("customerName")
    @Expose
    private String customerName;
    @SerializedName("customerAddress")
    @Expose
    private String customerAddress;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("tokenAmount")
    @Expose
    private Double tokenAmount;
    @SerializedName("exchangeReference")
    @Expose
    private String exchangeReference;
    @SerializedName("resetToken")
    @Expose
    private Object resetToken;
    @SerializedName("configureToken")
    @Expose
    private Object configureToken;
    @SerializedName("units")
    @Expose
    private String units;
    @SerializedName("fixChargeAmount")
    @Expose
    private Object fixChargeAmount;
    @SerializedName("tariff")
    @Expose
    private String tariff;
    @SerializedName("taxAmount")
    @Expose
    private Object taxAmount;

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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Double getTokenAmount() {
        return tokenAmount;
    }

    public void setTokenAmount(Double tokenAmount) {
        this.tokenAmount = tokenAmount;
    }

    public String getExchangeReference() {
        return exchangeReference;
    }

    public void setExchangeReference(String exchangeReference) {
        this.exchangeReference = exchangeReference;
    }

    public Object getResetToken() {
        return resetToken;
    }

    public void setResetToken(Object resetToken) {
        this.resetToken = resetToken;
    }

    public Object getConfigureToken() {
        return configureToken;
    }

    public void setConfigureToken(Object configureToken) {
        this.configureToken = configureToken;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public Object getFixChargeAmount() {
        return fixChargeAmount;
    }

    public void setFixChargeAmount(Object fixChargeAmount) {
        this.fixChargeAmount = fixChargeAmount;
    }

    public String getTariff() {
        return tariff;
    }

    public void setTariff(String tariff) {
        this.tariff = tariff;
    }

    public Object getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(Object taxAmount) {
        this.taxAmount = taxAmount;
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
            @SerializedName("product_name")
            @Expose
            private String productName;
            @SerializedName("unique_element")
            @Expose
            private String uniqueElement;
            @SerializedName("unit_price")
            @Expose
            private Integer unitPrice;
            @SerializedName("quantity")
            @Expose
            private Integer quantity;
            @SerializedName("service_verification")
            @Expose
            private Object serviceVerification;
            @SerializedName("channel")
            @Expose
            private String channel;
            @SerializedName("commission")
            @Expose
            private Integer commission;
            @SerializedName("total_amount")
            @Expose
            private Double totalAmount;
            @SerializedName("discount")
            @Expose
            private Object discount;
            @SerializedName("type")
            @Expose
            private String type;
            @SerializedName("email")
            @Expose
            private String email;
            @SerializedName("phone")
            @Expose
            private String phone;
            @SerializedName("name")
            @Expose
            private Object name;
            @SerializedName("convinience_fee")
            @Expose
            private Integer convinienceFee;
            @SerializedName("amount")
            @Expose
            private Integer amount;
            @SerializedName("platform")
            @Expose
            private String platform;
            @SerializedName("method")
            @Expose
            private String method;
            @SerializedName("transactionId")
            @Expose
            private String transactionId;

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getProductName() {
                return productName;
            }

            public void setProductName(String productName) {
                this.productName = productName;
            }

            public String getUniqueElement() {
                return uniqueElement;
            }

            public void setUniqueElement(String uniqueElement) {
                this.uniqueElement = uniqueElement;
            }

            public Integer getUnitPrice() {
                return unitPrice;
            }

            public void setUnitPrice(Integer unitPrice) {
                this.unitPrice = unitPrice;
            }

            public Integer getQuantity() {
                return quantity;
            }

            public void setQuantity(Integer quantity) {
                this.quantity = quantity;
            }

            public Object getServiceVerification() {
                return serviceVerification;
            }

            public void setServiceVerification(Object serviceVerification) {
                this.serviceVerification = serviceVerification;
            }

            public String getChannel() {
                return channel;
            }

            public void setChannel(String channel) {
                this.channel = channel;
            }

            public Integer getCommission() {
                return commission;
            }

            public void setCommission(Integer commission) {
                this.commission = commission;
            }

            public Double getTotalAmount() {
                return totalAmount;
            }

            public void setTotalAmount(Double totalAmount) {
                this.totalAmount = totalAmount;
            }

            public Object getDiscount() {
                return discount;
            }

            public void setDiscount(Object discount) {
                this.discount = discount;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
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

            public Object getName() {
                return name;
            }

            public void setName(Object name) {
                this.name = name;
            }

            public Integer getConvinienceFee() {
                return convinienceFee;
            }

            public void setConvinienceFee(Integer convinienceFee) {
                this.convinienceFee = convinienceFee;
            }

            public Integer getAmount() {
                return amount;
            }

            public void setAmount(Integer amount) {
                this.amount = amount;
            }

            public String getPlatform() {
                return platform;
            }

            public void setPlatform(String platform) {
                this.platform = platform;
            }

            public String getMethod() {
                return method;
            }

            public void setMethod(String method) {
                this.method = method;
            }

            public String getTransactionId() {
                return transactionId;
            }

            public void setTransactionId(String transactionId) {
                this.transactionId = transactionId;
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



