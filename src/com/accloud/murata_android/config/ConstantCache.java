package com.accloud.murata_android.config;

import android.graphics.Typeface;
import com.accloud.murata_android.application.MainApplication;
import com.accloud.service.ACUserDevice;
import com.accloud.utils.PreferencesUtils;

/**
 * Created by Administrator on 2015/4/28.
 */
public class ConstantCache {
    public static long deviceId = PreferencesUtils.getLong(MainApplication.getInstance(), "deviceId", 0);
    public static boolean isDeviceOnline;
    public static String physicalDeviceId;
    public static long subDomainId;
    public static long owner;
    public static String deviceName;

    public static int led_status = 0;
    public static int moto_status = 0;

    public static ACUserDevice userDevice;

    public static Typeface tf = Typeface.createFromAsset(MainApplication.getInstance().getAssets(), "fonts/myfont.ttf");
}
