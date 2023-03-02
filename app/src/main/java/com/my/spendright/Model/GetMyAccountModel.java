package com.my.spendright.Model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetMyAccountModel {

    @SerializedName("Account_detail")
    @Expose
    private List<AccountDetail> accountDetail = null;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;

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

    public class AccountDetail {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("month_name")
        @Expose
        private String monthName;
        @SerializedName("total")
        @Expose
        private String total;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMonthName() {
            return monthName;
        }

        public void setMonthName(String monthName) {
            this.monthName = monthName;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

    }
}