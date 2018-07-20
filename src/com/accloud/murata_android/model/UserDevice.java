package com.accloud.murata_android.model;

/**
 * Created by Administrator on 2015/4/27.
 */
public class UserDevice {
    private String detailName;
    private String value;

    public UserDevice() {
    }

    public UserDevice(String detailName, String value) {
        this.detailName = detailName;
        this.value = value;
    }

    public String getDetailName() {
        return detailName;
    }

    public void setDetailName(String detailName) {
        this.detailName = detailName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "UserDevice{" +
                ", detailName='" + detailName + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
