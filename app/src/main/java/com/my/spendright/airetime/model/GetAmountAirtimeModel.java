package com.my.spendright.airetime.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetAmountAirtimeModel {

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
        @SerializedName("currency")
        @Expose
        private String currency;
        @SerializedName("variations")
        @Expose
        private List<Variation> variations = null;
        @SerializedName("varations")
        @Expose
        private List<Varation> varations = null;

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

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public List<Variation> getVariations() {
            return variations;
        }

        public void setVariations(List<Variation> variations) {
            this.variations = variations;
        }

        public List<Varation> getVarations() {
            return varations;
        }

        public void setVarations(List<Varation> varations) {
            this.varations = varations;
        }

        public class Varation {

            @SerializedName("variation_code")
            @Expose
            private String variationCode;
            @SerializedName("name")
            @Expose
            private String name;
            @SerializedName("fixedPrice")
            @Expose
            private String fixedPrice;
            @SerializedName("variation_amount")
            @Expose
            private String variationAmount;
            @SerializedName("variation_amount_min")
            @Expose
            private String variationAmountMin;
            @SerializedName("variation_amount_max")
            @Expose
            private String variationAmountMax;
            @SerializedName("variation_rate")
            @Expose
            private Object variationRate;
            @SerializedName("charged_amount")
            @Expose
            private String chargedAmount;
            @SerializedName("charged_currency")
            @Expose
            private String chargedCurrency;

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

            public String getFixedPrice() {
                return fixedPrice;
            }

            public void setFixedPrice(String fixedPrice) {
                this.fixedPrice = fixedPrice;
            }

            public String getVariationAmount() {
                return variationAmount;
            }

            public void setVariationAmount(String variationAmount) {
                this.variationAmount = variationAmount;
            }

            public String getVariationAmountMin() {
                return variationAmountMin;
            }

            public void setVariationAmountMin(String variationAmountMin) {
                this.variationAmountMin = variationAmountMin;
            }

            public String getVariationAmountMax() {
                return variationAmountMax;
            }

            public void setVariationAmountMax(String variationAmountMax) {
                this.variationAmountMax = variationAmountMax;
            }

            public Object getVariationRate() {
                return variationRate;
            }

            public void setVariationRate(Object variationRate) {
                this.variationRate = variationRate;
            }

            public String getChargedAmount() {
                return chargedAmount;
            }

            public void setChargedAmount(String chargedAmount) {
                this.chargedAmount = chargedAmount;
            }

            public String getChargedCurrency() {
                return chargedCurrency;
            }

            public void setChargedCurrency(String chargedCurrency) {
                this.chargedCurrency = chargedCurrency;
            }

        }

        public class Variation {

            @SerializedName("variation_code")
            @Expose
            private String variationCode;
            @SerializedName("name")
            @Expose
            private String name;
            @SerializedName("fixedPrice")
            @Expose
            private String fixedPrice;
            @SerializedName("variation_amount")
            @Expose
            private String variationAmount;
            @SerializedName("variation_amount_min")
            @Expose
            private String variationAmountMin;
            @SerializedName("variation_amount_max")
            @Expose
            private String variationAmountMax;
            @SerializedName("variation_rate")
            @Expose
            private Object variationRate;
            @SerializedName("charged_amount")
            @Expose
            private String chargedAmount;
            @SerializedName("charged_currency")
            @Expose
            private String chargedCurrency;

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

            public String getFixedPrice() {
                return fixedPrice;
            }

            public void setFixedPrice(String fixedPrice) {
                this.fixedPrice = fixedPrice;
            }

            public String getVariationAmount() {
                return variationAmount;
            }

            public void setVariationAmount(String variationAmount) {
                this.variationAmount = variationAmount;
            }

            public String getVariationAmountMin() {
                return variationAmountMin;
            }

            public void setVariationAmountMin(String variationAmountMin) {
                this.variationAmountMin = variationAmountMin;
            }

            public String getVariationAmountMax() {
                return variationAmountMax;
            }

            public void setVariationAmountMax(String variationAmountMax) {
                this.variationAmountMax = variationAmountMax;
            }

            public Object getVariationRate() {
                return variationRate;
            }

            public void setVariationRate(Object variationRate) {
                this.variationRate = variationRate;
            }

            public String getChargedAmount() {
                return chargedAmount;
            }

            public void setChargedAmount(String chargedAmount) {
                this.chargedAmount = chargedAmount;
            }

            public String getChargedCurrency() {
                return chargedCurrency;
            }

            public void setChargedCurrency(String chargedCurrency) {
                this.chargedCurrency = chargedCurrency;
            }

        }

    }

}


