package com.my.spendright.Model;

public class HomeModal {

    private String description;
    private int imgId;
    boolean isShowHide = true;

    public boolean isShowHide() {
        return isShowHide;
    }

    public void setShowHide(boolean showHide) {
        isShowHide = showHide;
    }

    public HomeModal(String description, int imgId) {
        this.description = description;
        this.imgId = imgId;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public int getImgId() {
        return imgId;
    }
    public void setImgId(int imgId) {
        this.imgId = imgId;
    }
}