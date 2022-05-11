package com.my.spendright.ElectircalBill.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetMerchatAcocunt {

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
        @SerializedName("Meter_Number")
        @Expose
        private String meterNumber;
        @SerializedName("Customer_District")
        @Expose
        private String customerDistrict;
        @SerializedName("Address")
        @Expose
        private String address;

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public String getMeterNumber() {
            return meterNumber;
        }

        public void setMeterNumber(String meterNumber) {
            this.meterNumber = meterNumber;
        }

        public String getCustomerDistrict() {
            return customerDistrict;
        }

        public void setCustomerDistrict(String customerDistrict) {
            this.customerDistrict = customerDistrict;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

    }
}