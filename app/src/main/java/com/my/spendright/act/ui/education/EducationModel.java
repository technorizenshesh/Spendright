package com.my.spendright.act.ui.education;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class EducationModel {

    @SerializedName("response_description")
    @Expose
    private String responseDescription;
    @SerializedName("content")
    @Expose
    private Content content;

    public String getResponseDescription() {
        return responseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }


    public class Content {

        @SerializedName("ServiceName")
        @Expose
        private String serviceName;
        @SerializedName("serviceID")
        @Expose
        private String serviceID;
        @SerializedName("convinience_fee")
        @Expose
        private String convinienceFee;
        @SerializedName("varations")
        @Expose
        private List<Variation> variations;


        public String getServiceName() {
            return serviceName;
        }

        public void setServiceName(String serviceName) {
            this.serviceName = serviceName;
        }

        public String getServiceID() {
            return serviceID;
        }

        public void setServiceID(String serviceID) {
            this.serviceID = serviceID;
        }

        public String getConvinienceFee() {
            return convinienceFee;
        }

        public void setConvinienceFee(String convinienceFee) {
            this.convinienceFee = convinienceFee;
        }

        public List<Variation> getVariations() {
            return variations;
        }

        public void setVariations(List<Variation> variations) {
            this.variations = variations;
        }


        public class Variation {

            @SerializedName("variation_code")
            @Expose
            private String variationCode;
            @SerializedName("name")
            @Expose
            private String name;
            @SerializedName("variation_amount")
            @Expose
            private String variationAmount;
            @SerializedName("fixedPrice")
            @Expose
            private String fixedPrice;

            public String getVariationCode() {
                return variationCode;
            }

            public void setVariationCode(String variationCode) {
                this.variationCode = variationCode;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getVariationAmount() {
                return variationAmount;
            }

            public void setVariationAmount(String variationAmount) {
                this.variationAmount = variationAmount;
            }

            public String getFixedPrice() {
                return fixedPrice;
            }

            public void setFixedPrice(String fixedPrice) {
                this.fixedPrice = fixedPrice;
            }

        }
    }

}






