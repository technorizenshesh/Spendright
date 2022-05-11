package com.my.spendright.airetime.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetProductTypeModel {

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

        @SerializedName("product_type_id")
        @Expose
        private Integer productTypeId;
        @SerializedName("name")
        @Expose
        private String name;

        public Integer getProductTypeId() {
            return productTypeId;
        }

        public void setProductTypeId(Integer productTypeId) {
            this.productTypeId = productTypeId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
}