package com.my.spendright.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetSetbudgetExpence {

    @SerializedName("result")
    @Expose
    private List<Result> result = null;
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


        @SerializedName("group_id")
        @Expose
        private String groupId;
        @SerializedName("group_Name")
        @Expose
        private String groupName;
        @SerializedName("group_image")
        @Expose
        private String groupImage;
        @SerializedName("Category_detail")
        @Expose
        private List<CategoryDetail> categoryDetail = null;

        boolean isShowHide = true;

        public boolean isShowHide() {
            return isShowHide;
        }

        public void setShowHide(boolean showHide) {
            isShowHide = showHide;
        }

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public String getGroupImage() {
            return groupImage;
        }

        public void setGroupImage(String groupImage) {
            this.groupImage = groupImage;
        }

        public List<CategoryDetail> getCategoryDetail() {
            return categoryDetail;
        }

        public void setCategoryDetail(List<CategoryDetail> categoryDetail) {
            this.categoryDetail = categoryDetail;
        }

        public class CategoryDetail {

            @SerializedName("id")
            @Expose
            private String id;
            @SerializedName("user_id")
            @Expose
            private String userId;
            @SerializedName("group_id")
            @Expose
            private String groupId;
            @SerializedName("category_name")
            @Expose
            private String categoryName;
            @SerializedName("amount")
            @Expose
            private String amount;
            @SerializedName("select_end__day_month_week")
            @Expose
            private String selectEndDayMonthWeek;
            @SerializedName("select_month_week")
            @Expose
            private String selectMonthWeek;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getGroupId() {
                return groupId;
            }

            public void setGroupId(String groupId) {
                this.groupId = groupId;
            }

            public String getCategoryName() {
                return categoryName;
            }

            public void setCategoryName(String categoryName) {
                this.categoryName = categoryName;
            }

            public String getAmount() {
                return amount;
            }

            public void setAmount(String amount) {
                this.amount = amount;
            }

            public String getSelectEndDayMonthWeek() {
                return selectEndDayMonthWeek;
            }

            public void setSelectEndDayMonthWeek(String selectEndDayMonthWeek) {
                this.selectEndDayMonthWeek = selectEndDayMonthWeek;
            }

            public String getSelectMonthWeek() {
                return selectMonthWeek;
            }

            public void setSelectMonthWeek(String selectMonthWeek) {
                this.selectMonthWeek = selectMonthWeek;
            }

        }
    }

}

