package com.my.spendright.airetime.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetOperatorModel {

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

        @SerializedName("operator_id")
        @Expose
        private String operatorId;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("operator_image")
        @Expose
        private String operatorImage;

        public String getOperatorId() {
            return operatorId;
        }

        public void setOperatorId(String operatorId) {
            this.operatorId = operatorId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getOperatorImage() {
            return operatorImage;
        }

        public void setOperatorImage(String operatorImage) {
            this.operatorImage = operatorImage;
        }

    }


}