package com.stucom.jcacay.dam2project.model;

import java.util.List;

public class Ranking {
    private List<Player> data;
    private int count, errorCode;
    private String errorMsg;

    public Ranking(List<Player> data, int count, int errorCode, String errorMsg) {
        this.data = data;
        this.count = count;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public List<Player> getData() {
        return data;
    }

    public void setData(List<Player> data) {
        this.data = data;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
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
