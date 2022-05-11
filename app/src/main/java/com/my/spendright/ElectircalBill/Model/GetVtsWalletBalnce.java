package com.my.spendright.ElectircalBill.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetVtsWalletBalnce {

    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("contents")
    @Expose
    private Contents contents;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Contents getContents() {
        return contents;
    }

    public void setContents(Contents contents) {
        this.contents = contents;
    }

    public class Contents {

        @SerializedName("balance")
        @Expose
        private Double balance;

        public Double getBalance() {
            return balance;
        }

        public void setBalance(Double balance) {
            this.balance = balance;
        }

    }

}