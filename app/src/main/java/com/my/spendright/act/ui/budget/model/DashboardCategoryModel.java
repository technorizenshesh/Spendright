package com.my.spendright.act.ui.budget.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.my.spendright.act.ui.settings.model.IncomeExpenseCatModel;

import java.util.List;

public class DashboardCategoryModel {

    @SerializedName("result")
    @Expose
    private List<Result> result;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;



    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
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

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("sub_cat_name")
        @Expose
        private String subCatName;
        @SerializedName("category_image")
        @Expose
        private String categoryImage;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSubCatName() {
            return subCatName;
        }

        public void setSubCatName(String subCatName) {
            this.subCatName = subCatName;
        }

        public String getCategoryImage() {
            return categoryImage;
        }

        public void setCategoryImage(String categoryImage) {
            this.categoryImage = categoryImage;
        }
    }

}
