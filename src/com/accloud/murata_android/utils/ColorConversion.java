package com.accloud.murata_android.utils;

import android.graphics.Point;

/**
 * Created by Administrator on 2015/4/22.
 */
public class ColorConversion {

    public static double getDegreeByPoint(Point p1, Point p2) {
        double distance;
        distance = Math.sqrt(Math.pow((p2.x - p1.x), 2) + Math.pow((p2.y - p1.y), 2)); //距离，即斜边长度

        if (distance == 0)
            return 0; //自身， 不计算

        //角度设为α，分别计算sinα与cosα值
        double sα = (p2.y - p1.y) / distance;
        double cα = (p2.x - p1.x) / distance;
        double α = 0.0;

        //sinα与cosα的值，在I, II, III, IV象限的符号分别为(+,+), (+,-), (-,-), (-,+)
        if (sα >= 0 && cα >= 0) { //在第一象限
            α = Math.asin(sα);
        } else if (sα >= 0 && cα < 0) { //第II
            α = Math.PI - Math.asin(sα);
        } else if (sα < 0 && cα < 0) { //第III
            α = Math.PI - Math.asin(sα);
        } else if (sα < 0 && cα >= 0) { //第IV
            α = Math.PI * 2 + Math.asin(sα);
        }
        return α * 180 / Math.PI;
    }

    /**
     * @param H 0-360
     * @param S 0-100
     * @param V 0-100
     * @return color in hex string
     */
    public static int[] hsvToRgb(float H, float S, float V) {
        int[] colors = new int[3];
        float R, G, B;
        H /= 360f;
        S /= 100f;
        V /= 100f;

        if (S == 0) {
            R = V * 255;
            G = V * 255;
            B = V * 255;
        } else {
            float var_h = H * 6;
            if (var_h == 6)
                var_h = 0; // H must be < 1
            int var_i = (int) Math.floor((double) var_h); // Or ... var_i =
            // floor( var_h )
            float var_1 = V * (1 - S);
            float var_2 = V * (1 - S * (var_h - var_i));
            float var_3 = V * (1 - S * (1 - (var_h - var_i)));

            float var_r;
            float var_g;
            float var_b;
            if (var_i == 0) {
                var_r = V;
                var_g = var_3;
                var_b = var_1;
            } else if (var_i == 1) {
                var_r = var_2;
                var_g = V;
                var_b = var_1;
            } else if (var_i == 2) {
                var_r = var_1;
                var_g = V;
                var_b = var_3;
            } else if (var_i == 3) {
                var_r = var_1;
                var_g = var_2;
                var_b = V;
            } else if (var_i == 4) {
                var_r = var_3;
                var_g = var_1;
                var_b = V;
            } else {
                var_r = V;
                var_g = var_1;
                var_b = var_2;
            }

            R = var_r * 255; // RGB results from 0 to 255
            G = var_g * 255;
            B = var_b * 255;
        }

        colors[0] = (int) (R);
        colors[1] = (int) (G);
        colors[2] = (int) (B);

//        String rs = Integer.toHexString();
//        String gs = Integer.toHexString((int) (G));
//        String bs = Integer.toHexString((int) (B));
//
//        if (rs.length() == 1)
//            rs = "0" + rs;
//        if (gs.length() == 1)
//            gs = "0" + gs;
//        if (bs.length() == 1)
//            bs = "0" + bs;
        return colors;
    }
}
