package com.turingoal.bjcorona.ses.bean;

import java.io.Serializable;

/**
 * 工作模式
 */
public class WorkMode implements Serializable {
    private int mode; // 模式
    private int startChargeTime; // 充电开始时间
    private int endChargeTime; // 充电结束时间
    private int startDischargeTime; // 放电开始时间
    private int endDischargeTime; // 放电结束时间
    private int chargePowerPercentage; // 充电功率百分比
    private int disChargePowerPercentage; // 充电功率百分比

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getStartChargeTime() {
        return startChargeTime;
    }

    public void setStartChargeTime(int startChargeTime) {
        this.startChargeTime = startChargeTime;
    }

    public int getEndChargeTime() {
        return endChargeTime;
    }

    public void setEndChargeTime(int endChargeTime) {
        this.endChargeTime = endChargeTime;
    }

    public int getStartDischargeTime() {
        return startDischargeTime;
    }

    public void setStartDischargeTime(int startDischargeTime) {
        this.startDischargeTime = startDischargeTime;
    }

    public int getEndDischargeTime() {
        return endDischargeTime;
    }

    public void setEndDischargeTime(int endDischargeTime) {
        this.endDischargeTime = endDischargeTime;
    }

    public int getChargePowerPercentage() {
        return chargePowerPercentage;
    }

    public void setChargePowerPercentage(int chargePowerPercentage) {
        this.chargePowerPercentage = chargePowerPercentage;
    }

    public int getDisChargePowerPercentage() {
        return disChargePowerPercentage;
    }

    public void setDisChargePowerPercentage(int disChargePowerPercentage) {
        this.disChargePowerPercentage = disChargePowerPercentage;
    }
}
