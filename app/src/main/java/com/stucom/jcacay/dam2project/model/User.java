package com.stucom.jcacay.dam2project.model;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("data")
    private Player data;
    @SerializedName("errorCode")
    private int errorCode;
    @SerializedName("errorMsg")
    private String errorMsg;

    public User() {

    }

    public User(Player data, int errorCode, String errorMsg) {
        this.data = data;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public Player getData() {
        return data;
    }

    public void setData(Player data) {
        this.data = data;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
