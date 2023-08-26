package com.my.spendright.act.ui.report;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PieChartModel {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("result")
    @Expose
    private List<Result> result;

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

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }


    public class Result {

        @SerializedName("expense_traking_category_name")
        @Expose
        private String expenseTrakingCategoryName;
        @SerializedName("service")
        @Expose
        private String service;
        @SerializedName("holder_name")
        @Expose
        private String holderName;
        @SerializedName("total")
        @Expose
        private String total;
        @SerializedName("date_time")
        @Expose
        private String dateTime;

        public String getExpenseTrakingCategoryName() {
            return expenseTrakingCategoryName;
        }

        public void setExpenseTrakingCategoryName(String expenseTrakingCategoryName) {
            this.expenseTrakingCategoryName = expenseTrakingCategoryName;
        }

        public String getService() {
            return service;
        }

        public void setService(String service) {
            this.service = service;
        }

        public String getHolderName() {
            return holderName;
        }

        public void setHolderName(String holderName) {
            this.holderName = holderName;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

    }

}

