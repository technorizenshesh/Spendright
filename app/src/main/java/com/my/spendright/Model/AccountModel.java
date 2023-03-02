package com.my.spendright.Model;

public class AccountModel {
    String accountNumber,bankName,orderRef,amount;

    public AccountModel(String accountNumber, String bankName, String orderRef, String amount) {
        this.accountNumber = accountNumber;
        this.bankName = bankName;
        this.orderRef = orderRef;
        this.amount = amount;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getOrderRef() {
        return orderRef;
    }

    public void setOrderRef(String orderRef) {
        this.orderRef = orderRef;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
