package com.accloud.murata_android.model;

/**
 * Created by Administrator on 2015/4/25.
 */
public class UserInfo {
    private long userId;
    private String nickName;

    public UserInfo() {
    }

    public UserInfo(long userId, String nickName) {
        this.userId = userId;
        this.nickName = nickName;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "userId=" + userId +
                ", nickName='" + nickName + '\'' +
                '}';
    }
}
