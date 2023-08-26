package com.my.spendright.act.ui.budget;

public class BudgetGrpDetailModel {
    String id,name;
    boolean chk;

    public BudgetGrpDetailModel(String id, String name, boolean chk) {
        this.id = id;
        this.name = name;
        this.chk = chk;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChk() {
        return chk;
    }

    public void setChk(boolean chk) {
        this.chk = chk;
    }
}
