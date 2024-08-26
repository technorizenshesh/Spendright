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
    private List<AccountDetail> accountDetail;
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
        @SerializedName("user_name")
        @Expose
        private String userName;
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
        @SerializedName("country_code")
        @Expose
        private String countryCode;
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
        @SerializedName("monnify_contractCode")
        @Expose
        private String monnifyContractCode;
        @SerializedName("monnify_accountReference")
        @Expose
        private String monnifyAccountReference;
        @SerializedName("monnify_accountName")
        @Expose
        private String monnifyAccountName;
        @SerializedName("monnify_currencyCode")
        @Expose
        private String monnifyCurrencyCode;
        @SerializedName("monnify_customerEmail")
        @Expose
        private String monnifyCustomerEmail;
        @SerializedName("monnify_customerName")
        @Expose
        private String monnifyCustomerName;
        @SerializedName("batch_id")
        @Expose
        private String batchId;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("check_user")
        @Expose
        private String checkUser;
        @SerializedName("user_date_time")
        @Expose
        private String userDateTime;
        @SerializedName("date_time")
        @Expose
        private String dateTime;
        @SerializedName("payment_wallet")
        @Expose
        private String paymentWallet;
        @SerializedName("other_legal_name")
        @Expose
        private String otherLegalName;
        @SerializedName("full_address")
        @Expose
        private String fullAddress;
        @SerializedName("referral_code")
        @Expose
        private String referralCode;
        @SerializedName("state")
        @Expose
        private String state;
        @SerializedName("bank_verification_number")
        @Expose
        private String bankVerificationNumber;
        @SerializedName("gender")
        @Expose
        private String gender;
        @SerializedName("monnify_accountNumber")
        @Expose
        private String monnifyAccountNumber;
        @SerializedName("monnify_bankName")
        @Expose
        private String monnifyBankName;
        @SerializedName("monnify_bankCode")
        @Expose
        private String monnifyBankCode;
        @SerializedName("monnify_collectionChannel")
        @Expose
        private String monnifyCollectionChannel;
        @SerializedName("monnify_reservationReference")
        @Expose
        private String monnifyReservationReference;
        @SerializedName("monnify_reservedAccountType")
        @Expose
        private String monnifyReservedAccountType;
        @SerializedName("monnify_status")
        @Expose
        private String monnifyStatus;
        @SerializedName("monnify_createdOn")
        @Expose
        private String monnifyCreatedOn;
        @SerializedName("monnify_contractCode_1")
        @Expose
        private String monnifyContractCode1;
        @SerializedName("monnify_accountReference_1")
        @Expose
        private String monnifyAccountReference1;
        @SerializedName("monnify_accountName_1")
        @Expose
        private String monnifyAccountName1;
        @SerializedName("monnify_currencyCode_1")
        @Expose
        private String monnifyCurrencyCode1;
        @SerializedName("monnify_customerEmail_1")
        @Expose
        private String monnifyCustomerEmail1;
        @SerializedName("monnify_customerName_1")
        @Expose
        private String monnifyCustomerName1;
        @SerializedName("monnify_accountNumber_1")
        @Expose
        private String monnifyAccountNumber1;
        @SerializedName("monnify_bankName_1")
        @Expose
        private String monnifyBankName1;
        @SerializedName("monnify_bankCode_1")
        @Expose
        private String monnifyBankCode1;
        @SerializedName("monnify_collectionChannel_1")
        @Expose
        private String monnifyCollectionChannel1;
        @SerializedName("monnify_reservationReference_1")
        @Expose
        private String monnifyReservationReference1;
        @SerializedName("monnify_reservedAccountType_1")
        @Expose
        private String monnifyReservedAccountType1;
        @SerializedName("monnify_status_1")
        @Expose
        private String monnifyStatus1;
        @SerializedName("monnify_createdOn_1")
        @Expose
        private String monnifyCreatedOn1;
        @SerializedName("kyc_status")
        @Expose
        private String kycStatus;
        @SerializedName("flutter_flw_ref")
        @Expose
        private String flutterFlwRef;
        @SerializedName("flutter_order_ref")
        @Expose
        private String flutterOrderRef;
        @SerializedName("flutter_account_number")
        @Expose
        private String flutterAccountNumber;
        @SerializedName("flutter_account_status")
        @Expose
        private String flutterAccountStatus;
        @SerializedName("flutter_bank_name")
        @Expose
        private String flutterBankName;
        @SerializedName("flutter_created_at")
        @Expose
        private String flutterCreatedAt;
        @SerializedName("flutter_expiry_date")
        @Expose
        private String flutterExpiryDate;
        @SerializedName("flutter_note")
        @Expose
        private String flutterNote;
        @SerializedName("flutter_amount")
        @Expose
        private String flutterAmount;
        @SerializedName("bvn_number")
        @Expose
        private String bvnNumber;
        @SerializedName("my_referral_no")
        @Expose
        private String myReferralNo;

        @SerializedName("finger_prints_key")
        @Expose
        private String fingerPrintsKey;

        @SerializedName("referral_bonus")
        @Expose
        private String referralBonus;


        @SerializedName("last_login")
        @Expose
        private String lastLogin;

        @SerializedName("recreate_monnify_account")
        @Expose
        private String recreateMonnifyAccount;

        @SerializedName("token")
        @Expose
        private String token;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getRecreateMonnifyAccount() {
            return recreateMonnifyAccount;
        }

        public void setRecreateMonnifyAccount(String recreateMonnifyAccount) {
            this.recreateMonnifyAccount = recreateMonnifyAccount;
        }

        public String getLastLogin() {
            return lastLogin;
        }

        public void setLastLogin(String lastLogin) {
            this.lastLogin = lastLogin;
        }

        @SerializedName("payment_wallet_original") // wallet balance
        @Expose
        private String paymentWalletOriginal;

        public String getPaymentWalletOriginal() {
            return paymentWalletOriginal;
        }

        public void setPaymentWalletOriginal(String paymentWalletOriginal) {
            this.paymentWalletOriginal = paymentWalletOriginal;
        }

        public String getReferralBonus() {
            return referralBonus;
        }

        public void setReferralBonus(String referralBonus) {
            this.referralBonus = referralBonus;
        }

        public String getFingerPrintsKey() {
            return fingerPrintsKey;
        }

        public void setFingerPrintsKey(String fingerPrintsKey) {
            this.fingerPrintsKey = fingerPrintsKey;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
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

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
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

        public String getMonnifyContractCode() {
            return monnifyContractCode;
        }

        public void setMonnifyContractCode(String monnifyContractCode) {
            this.monnifyContractCode = monnifyContractCode;
        }

        public String getMonnifyAccountReference() {
            return monnifyAccountReference;
        }

        public void setMonnifyAccountReference(String monnifyAccountReference) {
            this.monnifyAccountReference = monnifyAccountReference;
        }

        public String getMonnifyAccountName() {
            return monnifyAccountName;
        }

        public void setMonnifyAccountName(String monnifyAccountName) {
            this.monnifyAccountName = monnifyAccountName;
        }

        public String getMonnifyCurrencyCode() {
            return monnifyCurrencyCode;
        }

        public void setMonnifyCurrencyCode(String monnifyCurrencyCode) {
            this.monnifyCurrencyCode = monnifyCurrencyCode;
        }

        public String getMonnifyCustomerEmail() {
            return monnifyCustomerEmail;
        }

        public void setMonnifyCustomerEmail(String monnifyCustomerEmail) {
            this.monnifyCustomerEmail = monnifyCustomerEmail;
        }

        public String getMonnifyCustomerName() {
            return monnifyCustomerName;
        }

        public void setMonnifyCustomerName(String monnifyCustomerName) {
            this.monnifyCustomerName = monnifyCustomerName;
        }

        public String getBatchId() {
            return batchId;
        }

        public void setBatchId(String batchId) {
            this.batchId = batchId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCheckUser() {
            return checkUser;
        }

        public void setCheckUser(String checkUser) {
            this.checkUser = checkUser;
        }

        public String getUserDateTime() {
            return userDateTime;
        }

        public void setUserDateTime(String userDateTime) {
            this.userDateTime = userDateTime;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

        public String getPaymentWallet() {
            return paymentWallet;
        }

        public void setPaymentWallet(String paymentWallet) {
            this.paymentWallet = paymentWallet;
        }

        public String getOtherLegalName() {
            return otherLegalName;
        }

        public void setOtherLegalName(String otherLegalName) {
            this.otherLegalName = otherLegalName;
        }

        public String getFullAddress() {
            return fullAddress;
        }

        public void setFullAddress(String fullAddress) {
            this.fullAddress = fullAddress;
        }

        public String getReferralCode() {
            return referralCode;
        }

        public void setReferralCode(String referralCode) {
            this.referralCode = referralCode;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getBankVerificationNumber() {
            return bankVerificationNumber;
        }

        public void setBankVerificationNumber(String bankVerificationNumber) {
            this.bankVerificationNumber = bankVerificationNumber;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getMonnifyAccountNumber() {
            return monnifyAccountNumber;
        }

        public void setMonnifyAccountNumber(String monnifyAccountNumber) {
            this.monnifyAccountNumber = monnifyAccountNumber;
        }

        public String getMonnifyBankName() {
            return monnifyBankName;
        }

        public void setMonnifyBankName(String monnifyBankName) {
            this.monnifyBankName = monnifyBankName;
        }

        public String getMonnifyBankCode() {
            return monnifyBankCode;
        }

        public void setMonnifyBankCode(String monnifyBankCode) {
            this.monnifyBankCode = monnifyBankCode;
        }

        public String getMonnifyCollectionChannel() {
            return monnifyCollectionChannel;
        }

        public void setMonnifyCollectionChannel(String monnifyCollectionChannel) {
            this.monnifyCollectionChannel = monnifyCollectionChannel;
        }

        public String getMonnifyReservationReference() {
            return monnifyReservationReference;
        }

        public void setMonnifyReservationReference(String monnifyReservationReference) {
            this.monnifyReservationReference = monnifyReservationReference;
        }

        public String getMonnifyReservedAccountType() {
            return monnifyReservedAccountType;
        }

        public void setMonnifyReservedAccountType(String monnifyReservedAccountType) {
            this.monnifyReservedAccountType = monnifyReservedAccountType;
        }

        public String getMonnifyStatus() {
            return monnifyStatus;
        }

        public void setMonnifyStatus(String monnifyStatus) {
            this.monnifyStatus = monnifyStatus;
        }

        public String getMonnifyCreatedOn() {
            return monnifyCreatedOn;
        }

        public void setMonnifyCreatedOn(String monnifyCreatedOn) {
            this.monnifyCreatedOn = monnifyCreatedOn;
        }

        public String getMonnifyContractCode1() {
            return monnifyContractCode1;
        }

        public void setMonnifyContractCode1(String monnifyContractCode1) {
            this.monnifyContractCode1 = monnifyContractCode1;
        }

        public String getMonnifyAccountReference1() {
            return monnifyAccountReference1;
        }

        public void setMonnifyAccountReference1(String monnifyAccountReference1) {
            this.monnifyAccountReference1 = monnifyAccountReference1;
        }

        public String getMonnifyAccountName1() {
            return monnifyAccountName1;
        }

        public void setMonnifyAccountName1(String monnifyAccountName1) {
            this.monnifyAccountName1 = monnifyAccountName1;
        }

        public String getMonnifyCurrencyCode1() {
            return monnifyCurrencyCode1;
        }

        public void setMonnifyCurrencyCode1(String monnifyCurrencyCode1) {
            this.monnifyCurrencyCode1 = monnifyCurrencyCode1;
        }

        public String getMonnifyCustomerEmail1() {
            return monnifyCustomerEmail1;
        }

        public void setMonnifyCustomerEmail1(String monnifyCustomerEmail1) {
            this.monnifyCustomerEmail1 = monnifyCustomerEmail1;
        }

        public String getMonnifyCustomerName1() {
            return monnifyCustomerName1;
        }

        public void setMonnifyCustomerName1(String monnifyCustomerName1) {
            this.monnifyCustomerName1 = monnifyCustomerName1;
        }

        public String getMonnifyAccountNumber1() {
            return monnifyAccountNumber1;
        }

        public void setMonnifyAccountNumber1(String monnifyAccountNumber1) {
            this.monnifyAccountNumber1 = monnifyAccountNumber1;
        }

        public String getMonnifyBankName1() {
            return monnifyBankName1;
        }

        public void setMonnifyBankName1(String monnifyBankName1) {
            this.monnifyBankName1 = monnifyBankName1;
        }

        public String getMonnifyBankCode1() {
            return monnifyBankCode1;
        }

        public void setMonnifyBankCode1(String monnifyBankCode1) {
            this.monnifyBankCode1 = monnifyBankCode1;
        }

        public String getMonnifyCollectionChannel1() {
            return monnifyCollectionChannel1;
        }

        public void setMonnifyCollectionChannel1(String monnifyCollectionChannel1) {
            this.monnifyCollectionChannel1 = monnifyCollectionChannel1;
        }

        public String getMonnifyReservationReference1() {
            return monnifyReservationReference1;
        }

        public void setMonnifyReservationReference1(String monnifyReservationReference1) {
            this.monnifyReservationReference1 = monnifyReservationReference1;
        }

        public String getMonnifyReservedAccountType1() {
            return monnifyReservedAccountType1;
        }

        public void setMonnifyReservedAccountType1(String monnifyReservedAccountType1) {
            this.monnifyReservedAccountType1 = monnifyReservedAccountType1;
        }

        public String getMonnifyStatus1() {
            return monnifyStatus1;
        }

        public void setMonnifyStatus1(String monnifyStatus1) {
            this.monnifyStatus1 = monnifyStatus1;
        }

        public String getMonnifyCreatedOn1() {
            return monnifyCreatedOn1;
        }

        public void setMonnifyCreatedOn1(String monnifyCreatedOn1) {
            this.monnifyCreatedOn1 = monnifyCreatedOn1;
        }

        public String getKycStatus() {
            return kycStatus;
        }

        public void setKycStatus(String kycStatus) {
            this.kycStatus = kycStatus;
        }

        public String getFlutterFlwRef() {
            return flutterFlwRef;
        }

        public void setFlutterFlwRef(String flutterFlwRef) {
            this.flutterFlwRef = flutterFlwRef;
        }

        public String getFlutterOrderRef() {
            return flutterOrderRef;
        }

        public void setFlutterOrderRef(String flutterOrderRef) {
            this.flutterOrderRef = flutterOrderRef;
        }

        public String getFlutterAccountNumber() {
            return flutterAccountNumber;
        }

        public void setFlutterAccountNumber(String flutterAccountNumber) {
            this.flutterAccountNumber = flutterAccountNumber;
        }

        public String getFlutterAccountStatus() {
            return flutterAccountStatus;
        }

        public void setFlutterAccountStatus(String flutterAccountStatus) {
            this.flutterAccountStatus = flutterAccountStatus;
        }

        public String getFlutterBankName() {
            return flutterBankName;
        }

        public void setFlutterBankName(String flutterBankName) {
            this.flutterBankName = flutterBankName;
        }

        public String getFlutterCreatedAt() {
            return flutterCreatedAt;
        }

        public void setFlutterCreatedAt(String flutterCreatedAt) {
            this.flutterCreatedAt = flutterCreatedAt;
        }

        public String getFlutterExpiryDate() {
            return flutterExpiryDate;
        }

        public void setFlutterExpiryDate(String flutterExpiryDate) {
            this.flutterExpiryDate = flutterExpiryDate;
        }

        public String getFlutterNote() {
            return flutterNote;
        }

        public void setFlutterNote(String flutterNote) {
            this.flutterNote = flutterNote;
        }

        public String getFlutterAmount() {
            return flutterAmount;
        }

        public void setFlutterAmount(String flutterAmount) {
            this.flutterAmount = flutterAmount;
        }

        public String getBvnNumber() {
            return bvnNumber;
        }

        public void setBvnNumber(String bvnNumber) {
            this.bvnNumber = bvnNumber;
        }

        public String getMyReferralNo() {
            return myReferralNo;
        }

        public void setMyReferralNo(String myReferralNo) {
            this.myReferralNo = myReferralNo;
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

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

    }

}

