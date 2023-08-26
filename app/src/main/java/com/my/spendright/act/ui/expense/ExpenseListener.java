package com.my.spendright.act.ui.expense;

public interface ExpenseListener {
    void onExpense(String id, String stDate, String enDate, String stAmt, String enAmt, String transType);
}
