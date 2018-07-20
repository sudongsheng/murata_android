package com.accloud.murata_android.application;

import android.app.Application;
import com.accloud.murata_android.config.Config;
import com.accloud.murata_android.model.UserInfo;
import com.accloud.cloudservice.AC;
import com.accloud.utils.PreferencesUtils;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UpdateConfig;

/**
 * Created by Xuri on 2015/1/17.
 */
public class MainApplication extends Application {
    private static MainApplication mInstance;
    public static UserInfo mUser;

    @Override
    public void onCreate() {
        super.onCreate();
        AC.init(this, Config.MAJORDOAMIN, Config.MAJORDOMAINID, AC.TEST_MODE);
        mInstance = this;
        initUserInfo();

        UpdateConfig.setDebug(true);
    }

    public void initUserInfo() {
        if (AC.accountMgr().isLogin()) {
            long uid = PreferencesUtils.getLong(MainApplication.getInstance(), "uid", 0);
            String name = PreferencesUtils.getString(MainApplication.getInstance(), "name");
            mUser = new UserInfo(uid, name);
        }
    }

    public static void UserLogin(UserInfo user) {
        mUser = user;
        PreferencesUtils.putLong(MainApplication.getInstance(), "uid", mUser.getUserId());
        PreferencesUtils.putString(MainApplication.getInstance(), "name", mUser.getNickName());
    }

    public static MainApplication getInstance() {
        return mInstance;
    }

}
