package com.accloud.murata_android.model;

/**
 * Created by Xuri on 2015/1/31.
 */
public class RecordInfo {
    public long type;
    public long action;
    public long time;

    public long getType() {
        return type;
    }

    public void setType(long type) {
        this.type = type;
    }

    public long getAction() {
        return action;
    }

    public void setAction(long action) {
        this.action = action;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "RecordInfo{" +
                "type=" + type +
                ", action=" + action +
                ", time=" + time +
                '}';
    }
}