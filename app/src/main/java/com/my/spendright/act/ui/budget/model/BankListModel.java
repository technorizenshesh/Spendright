package com.my.spendright.act.ui.budget.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BankListModel {

    @SerializedName("responseBody")
    @Expose
    private List<Result> result;
    @SerializedName("responseMessage")
    @Expose
    private String message;
    @SerializedName("requestSuccessful")
    @Expose
    private boolean status;

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

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public class Result {

        @SerializedName("name")
        @Expose
        private String bank;
        @SerializedName("code")
        @Expose
        private String bankCode;
        @SerializedName("ussdTemplate")
        @Expose
        private String accountCode;
        @SerializedName("baseUssdCode")
        @Expose
        private String ussdCode;
        @SerializedName("transferUssdTemplate")
        @Expose
        private String transferFormat;

        public String getBank() {
            return bank;
        }

        public void setBank(String bank) {
            this.bank = bank;
        }

        public String getBankCode() {
            return bankCode;
        }

        public void setBankCode(String bankCode) {
            this.bankCode = bankCode;
        }

        public String getAccountCode() {
            return accountCode;
        }

        public void setAccountCode(String accountCode) {
            this.accountCode = accountCode;
        }

        public String getUssdCode() {
            return ussdCode;
        }

        public void setUssdCode(String ussdCode) {
            this.ussdCode = ussdCode;
        }

        public String getTransferFormat() {
            return transferFormat;
        }

        public void setTransferFormat(String transferFormat) {
            this.transferFormat = transferFormat;
        }

    }

}

