package com.accloud.murata_android.utils;

import android.content.Context;

/**
 * Created by Administrator on 2015/8/1.
 */
public class DisplayUtils {

    public static int dip2px(Context context, int dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
