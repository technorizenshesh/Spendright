package com.my.spendright.act.ui.budget.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BudgetGrpModel {

    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("groups")
    @Expose
    private List<Group> groups;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
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

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public class Group {

        @SerializedName("group_row_id")
        @Expose
        private String groupRowId;
        @SerializedName("group_name")
        @Expose
        private String groupName;
        @SerializedName("group_description")
        @Expose
        private String groupDescription;
        @SerializedName("group_owner_id")
        @Expose
        private String groupOwnerId;
        @SerializedName("group_color")
        @Expose
        private String groupColor;
        @SerializedName("group_status")
        @Expose
        private String groupStatus;

        @SerializedName("amount")
        @Expose
        private String amount;

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getGroupRowId() {
            return groupRowId;
        }

        public void setGroupRowId(String groupRowId) {
            this.groupRowId = groupRowId;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public String getGroupDescription() {
            return groupDescription;
        }

        public void setGroupDescription(String groupDescription) {
            this.groupDescription = groupDescription;
        }

        public String getGroupOwnerId() {
            return groupOwnerId;
        }

        public void setGroupOwnerId(String groupOwnerId) {
            this.groupOwnerId = groupOwnerId;
        }

        public String getGroupColor() {
            return groupColor;
        }

        public void setGroupColor(String groupColor) {
            this.groupColor = groupColor;
        }

        public String getGroupStatus() {
            return groupStatus;
        }

        public void setGroupStatus(String groupStatus) {
            this.groupStatus = groupStatus;
        }

    }

}

