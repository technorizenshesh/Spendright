package com.my.spendright.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class vAccountModel {

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

        @SerializedName("bank")
        @Expose
        private List<Bank> bank;

        public List<Bank> getBank() {
            return bank;
        }

        public void setBank(List<Bank> bank) {
            this.bank = bank;
        }

        public class Bank {

            @SerializedName("bankCode")
            @Expose
            private String bankCode;
            @SerializedName("bankName")
            @Expose
            private String bankName;
            @SerializedName("accountNumber")
            @Expose
            private String accountNumber;
            @SerializedName("type")
            @Expose
            private String type;

            @SerializedName("bvn_connect_status")
            @Expose
            private String bvnConnectStatus;

            public String getBvnConnectStatus() {
                return bvnConnectStatus;
            }

            public void setBvnConnectStatus(String bvnConnectStatus) {
                this.bvnConnectStatus = bvnConnectStatus;
            }

            public String getBankCode() {
                return bankCode;
            }

            public void setBankCode(String bankCode) {
                this.bankCode = bankCode;
            }

            public String getBankName() {
                return bankName;
            }

            public void setBankName(String bankName) {
                this.bankName = bankName;
            }

            public String getAccountNumber() {
                return accountNumber;
            }

            public void setAccountNumber(String accountNumber) {
                this.accountNumber = accountNumber;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

        }


    }


}