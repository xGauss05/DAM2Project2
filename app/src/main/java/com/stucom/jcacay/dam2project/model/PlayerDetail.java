package com.stucom.jcacay.dam2project.model;

import com.google.gson.annotations.SerializedName;

public class PlayerDetail {

    @SerializedName("data")
    private Player player;
    private int errorCode;
    private String errorMsg;

    public PlayerDetail(Player player, int errorCode, String errorMsg) {
        this.player = player;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;

    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
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
