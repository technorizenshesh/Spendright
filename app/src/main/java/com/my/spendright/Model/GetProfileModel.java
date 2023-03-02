package com.my.spendright.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetProfileModel {

    @SerializedName("result")
    @Expose
    private Result result;
    @SerializedName("Account_detail")
    @Expose
    private List<AccountDetail> accountDetail = null;
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

    public class Result {
        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("first_name")
        @Expose
        private String firstName;
        @SerializedName("mobile")
        @Expose
        private String mobile;
        @SerializedName("password")
        @Expose
        private String password;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("last_name")
        @Expose
        private String lastName;
        @SerializedName("city")
        @Expose
        private String city;
        @SerializedName("country_residence")
        @Expose
        private String countryResidence;
        @SerializedName("dob")
        @Expose
        private String dob;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("register_id")
        @Expose
        private String registerId;
        @SerializedName("social_id")
        @Expose
        private String socialId;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("lat")
        @Expose
        private String lat;
        @SerializedName("lon")
        @Expose
        private String lon;
        @SerializedName("loan_accounts")
        @Expose
        private String loanAccounts;
        @SerializedName("budget_accounts")
        @Expose
        private String budgetAccounts;
        @SerializedName("investment_accounts")
        @Expose
        private String investmentAccounts;
        @SerializedName("otp")
        @Expose
        private String otp;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("date_time")
        @Expose
        private String dateTime;

        @SerializedName("monnify_accountReference")
        @Expose
        private String monnifyAccountReference;

        @SerializedName("batch_id")
        @Expose
        private String batchId;

        @SerializedName("check_user")
        @Expose
        private String checkUser;

        @SerializedName("payment_wallet")
        @Expose
        private String paymentWallet;

        public String getPaymentWallet() {
            return paymentWallet;
        }

        public void setPaymentWallet(String paymentWallet) {
            this.paymentWallet = paymentWallet;
        }

        public String getCheckUser() {
            return checkUser;
        }

        public void setCheckUser(String checkUser) {
            this.checkUser = checkUser;
        }

        public String getBatchId() {
            return batchId;
        }

        public void setBatchId(String batchId) {
            this.batchId = batchId;
        }

        public String getMonnifyAccountReference() {
            return monnifyAccountReference;
        }

        public void setMonnifyAccountReference(String monnifyAccountReference) {
            this.monnifyAccountReference = monnifyAccountReference;
        }



        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCountryResidence() {
            return countryResidence;
        }

        public void setCountryResidence(String countryResidence) {
            this.countryResidence = countryResidence;
        }

        public String getDob() {
            return dob;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getRegisterId() {
            return registerId;
        }

        public void setRegisterId(String registerId) {
            this.registerId = registerId;
        }

        public String getSocialId() {
            return socialId;
        }

        public void setSocialId(String socialId) {
            this.socialId = socialId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLon() {
            return lon;
        }

        public void setLon(String lon) {
            this.lon = lon;
        }

        public String getLoanAccounts() {
            return loanAccounts;
        }

        public void setLoanAccounts(String loanAccounts) {
            this.loanAccounts = loanAccounts;
        }

        public String getBudgetAccounts() {
            return budgetAccounts;
        }

        public void setBudgetAccounts(String budgetAccounts) {
            this.budgetAccounts = budgetAccounts;
        }

        public String getInvestmentAccounts() {
            return investmentAccounts;
        }

        public void setInvestmentAccounts(String investmentAccounts) {
            this.investmentAccounts = investmentAccounts;
        }

        public String getOtp() {
            return otp;
        }

        public void setOtp(String otp) {
            this.otp = otp;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

    }

    public class AccountDetail {


        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("account_id")
        @Expose
        private String accountId;
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("holder_name")
        @Expose
        private String holderName;
        @SerializedName("current_balance")
        @Expose
        private String currentBalance;
        @SerializedName("account_name")
        @Expose
        private String accountName;
        @SerializedName("total")
        @Expose
        private String total;

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

        public String getHolderName() {
            return holderName;
        }

        public void setHolderName(String holderName) {
            this.holderName = holderName;
        }

        public String getCurrentBalance() {
            return currentBalance;
        }

        public void setCurrentBalance(String currentBalance) {
            this.currentBalance = currentBalance;
        }

        public String getAccountName() {
            return accountName;
        }

        public void setAccountName(String accountName) {
            this.accountName = accountName;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

    }

}
