package com.accloud.murata_android.config;

import com.accloud.murata_android.application.MainApplication;
import com.accloud.murata_android.utils.DisplayUtils;

/**
 * Created by Xuri on 2015/4/7.
 */
public interface AppConstant {
    public static int REGISTER = 0;
    public static int RESETPASSWORD = 1;

    public static final int POPUP_ADD = 0;
    public static final int POPUP_MORE = 1;

    public static String STREAMID_BUZZER = "buzzer";
    public static String STREAMID_LIGHT_1 = "light1";
    public static String STREAMID_LIGHT_2 = "light2";
    public static String STREAMID_LED = "led_rgb";
    public static String STREAMID_LED_RED = "led_rgb_r";
    public static String STREAMID_LED_GREEN = "led_rgb_g";
    public static String STREAMID_LED_BLUE = "led_rgb_b";
    public static String STREAMID_TEMPERATURE = "temperature";
    public static String STREAMID_HUMIDITY = "humidity";
    public static String STREAMID_LIGHTSENSOR = "illuminance";
    public static String STREAMID_MOTO = "moto";
    public static String STREAMID_MOTO_PWM = "moto_pwm";

    public static final int TEMPERATURE = 0;
    public static final int HUMIDITY = 1;

    public static final int BOUNDARY_DISTANT = DisplayUtils.dip2px(MainApplication.getInstance(), 40);
    public static final int BOUNDARY_SPEED = DisplayUtils.dip2px(MainApplication.getInstance(), 40);
}
