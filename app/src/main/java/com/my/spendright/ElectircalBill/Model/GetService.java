package com.my.spendright.ElectircalBill.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetService {

    @SerializedName("response_description")
    @Expose
    private String responseDescription;
    @SerializedName("content")
    @Expose
    private List<Content> content = null;

    public String getResponseDescription() {
        return responseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }

    public List<Content> getContent() {
        return content;
    }

    public void setContent(List<Content> content) {
        this.content = content;
    }

    public class Content {

        @SerializedName("serviceID")
        @Expose
        private String serviceID;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("minimium_amount")
        @Expose
        private String minimiumAmount;
        @SerializedName("maximum_amount")
        @Expose
        private String maximumAmount;
        @SerializedName("convinience_fee")
        @Expose
        private String convinienceFee;
        @SerializedName("product_type")
        @Expose
        private String productType;
        @SerializedName("image")
        @Expose
        private String image;

        public String getServiceID() {
            return serviceID;
        }

        public void setServiceID(String serviceID) {
            this.serviceID = serviceID;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMinimiumAmount() {
            return minimiumAmount;
        }

        public void setMinimiumAmount(String minimiumAmount) {
            this.minimiumAmount = minimiumAmount;
        }

        public String getMaximumAmount() {
            return maximumAmount;
        }

        public void setMaximumAmount(String maximumAmount) {
            this.maximumAmount = maximumAmount;
        }

        public String getConvinienceFee() {
            return convinienceFee;
        }

        public void setConvinienceFee(String convinienceFee) {
            this.convinienceFee = convinienceFee;
        }

        public String getProductType() {
            return productType;
        }

        public void setProductType(String productType) {
            this.productType = productType;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

    }

}