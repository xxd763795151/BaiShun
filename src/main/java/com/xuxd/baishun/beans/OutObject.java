package com.xuxd.baishun.beans;

/**
 * @Auther: 许晓东
 * @Date: 20-3-24 10:46
 * @Description:
 */
public class OutObject {

    private int rtnCode = 0;
    private String rtnMessage = "success";

    public OutObject() {
    }

    public int getRtnCode() {
        return rtnCode;
    }

    public OutObject setRtnCode(int rtnCode) {
        this.rtnCode = rtnCode;
        return this;
    }

    public String getRtnMessage() {
        return rtnMessage;
    }

    public OutObject setRtnMessage(String rtnMessage) {
        this.rtnMessage = rtnMessage;
        return this;
    }

    public OutObject success() {
        this.rtnCode = 0;
        return this;
    }

    public OutObject fail() {
        this.rtnCode = -9999;
        return this;
    }
}
