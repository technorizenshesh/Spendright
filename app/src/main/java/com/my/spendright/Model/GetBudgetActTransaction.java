package com.my.spendright.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetBudgetActTransaction {

    @SerializedName("Account_detail")
    @Expose
    private List<AccountDetail> accountDetail = null;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;

    public List<AccountDetail> getAccountDetail() {
        return accountDetail;
    }

    public void setAccountDetail(List<AccountDetail> accountDetail) {
        this.accountDetail = accountDetail;
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


    public class AccountDetail {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("account_id")
        @Expose
        private String accountId;
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("holder_name")
        @Expose
        private String holderName;
        @SerializedName("current_balance")
        @Expose
        private String currentBalance;
        @SerializedName("total")
        @Expose
        private String total;
        @SerializedName("income")
        @Expose
        private String income;
        @SerializedName("expense")
        @Expose
        private String expense;
        @SerializedName("available_balance")
        @Expose
        private String availableBalance;
        @SerializedName("transaction")
        @Expose
        private List<Transaction> transaction = null;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAccountId() {
            return accountId;
        }

        public void setAccountId(String accountId) {
            this.accountId = accountId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getHolderName() {
            return holderName;
        }

        public void setHolderName(String holderName) {
            this.holderName = holderName;
        }

        public String getCurrentBalance() {
            return currentBalance;
        }

        public void setCurrentBalance(String currentBalance) {
            this.currentBalance = currentBalance;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getIncome() {
            return income;
        }

        public void setIncome(String income) {
            this.income = income;
        }

        public String getExpense() {
            return expense;
        }

        public void setExpense(String expense) {
            this.expense = expense;
        }

        public String getAvailableBalance() {
            return availableBalance;
        }

        public void setAvailableBalance(String availableBalance) {
            this.availableBalance = availableBalance;
        }

        public List<Transaction> getTransaction() {
            return transaction;
        }

        public void setTransaction(List<Transaction> transaction) {
            this.transaction = transaction;
        }

        public class Transaction {

            @SerializedName("id")
            @Expose
            private String id;
            @SerializedName("user_id")
            @Expose
            private String userId;
            @SerializedName("account_budget_id")
            @Expose
            private String accountBudgetId;
            @SerializedName("pay_name")
            @Expose
            private String payName;
            @SerializedName("transaction_amount")
            @Expose
            private String transactionAmount;
            @SerializedName("type")
            @Expose
            private String type;
            @SerializedName("main_category_id")
            @Expose
            private String mainCategoryId;
            @SerializedName("main_category_name")
            @Expose
            private String mainCategoryName;
            @SerializedName("date_time")
            @Expose
            private String dateTime;
            @SerializedName("description")
            @Expose
            private String description;

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

            public String getAccountBudgetId() {
                return accountBudgetId;
            }

            public void setAccountBudgetId(String accountBudgetId) {
                this.accountBudgetId = accountBudgetId;
            }

            public String getPayName() {
                return payName;
            }

            public void setPayName(String payName) {
                this.payName = payName;
            }

            public String getTransactionAmount() {
                return transactionAmount;
            }

            public void setTransactionAmount(String transactionAmount) {
                this.transactionAmount = transactionAmount;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getMainCategoryId() {
                return mainCategoryId;
            }

            public void setMainCategoryId(String mainCategoryId) {
                this.mainCategoryId = mainCategoryId;
            }

            public String getMainCategoryName() {
                return mainCategoryName;
            }

            public void setMainCategoryName(String mainCategoryName) {
                this.mainCategoryName = mainCategoryName;
            }

            public String getDateTime() {
                return dateTime;
            }

            public void setDateTime(String dateTime) {
                this.dateTime = dateTime;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

        }

    }
}

