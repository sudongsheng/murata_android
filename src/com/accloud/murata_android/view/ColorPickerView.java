package com.accloud.murata_android.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.accloud.murata_android.R;
import com.accloud.murata_android.utils.ColorConversion;
import com.accloud.utils.LogUtil;

/**
 * 类名: ColorPickerView <br/>
 * 功能: 颜色选择器. <br/>
 * 日期: 2014-3-30 上午10:14:15 <br/>
 *
 * @author msl
 */
public class ColorPickerView extends FrameLayout implements View.OnTouchListener {
    private static final String TAG = ColorPickerView.class.getSimpleName();

    private ImageView iv_color_range;//颜色选择盘
    private ImageView iv_color_range_bg;//颜色选择盘
    private ImageView iv_color_picker;//颜色选择器

    private int range_radius;//圆盘半径
    private int picker_radius;//颜色选择器半径
    private int centerX;//圆盘中心X坐标
    private int centerY;//圆盘中心Y坐标
    private onColorChangedListener colorChangedListener;//颜色变换监听

    private ImageView showPicker;
    int currentX, currentY;//上次触摸坐标
    private float hue = 177;
    private float saturation = 100;
    private int brightness = 63;
    private View view;

    public ColorPickerView(Context context) {
        super(context);

    }

    public ColorPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * init:(初始化). <br/>
     *
     * @param context
     * @author msl
     * @since 1.0
     */
    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.color_picker, this);
        iv_color_range = (ImageView) view.findViewById(R.id.iv_color_range);
        iv_color_range_bg = (ImageView) view.findViewById(R.id.iv_color_range_bg);
        iv_color_picker = (ImageView) view.findViewById(R.id.iv_color_picker);
        iv_color_range.setOnTouchListener(this);
        iv_color_range_bg.setAlpha((100 - brightness) * 255 / 100);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (range_radius == 0) {//未初始化
            range_radius = iv_color_range.getWidth() / 2;//圆盘半径
            picker_radius = iv_color_picker.getWidth() / 2;//选择器半径
            centerX = iv_color_range.getRight() - range_radius;
            centerY = iv_color_range.getHeight() / 2 - iv_color_range.getBottom();
//            bitmap = ((BitmapDrawable) iv_color_range.getDrawable()).getBitmap();//获取圆盘图片
//            LogUtil.d(TAG, "range left:" + iv_color_range.getLeft());
//            LogUtil.d(TAG, "range top:" + iv_color_range.getTop());
//            LogUtil.d(TAG, "range right:" + iv_color_range.getRight());
//            LogUtil.d(TAG, "range bottom:" + iv_color_range.getBottom());
            LogUtil.d(TAG, "width:" + range_radius);
            LogUtil.d(TAG, "pick width:" + picker_radius);
            LogUtil.d(TAG, "centerX:" + centerX);
            LogUtil.d(TAG, "centerY:" + centerY);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN://按下
                LogUtil.d(TAG, "currentX:" + currentX + "  currentY:" + currentY);
                currentX = (int) event.getX() - (view.getWidth() / 2 - range_radius);
                currentY = (int) (event.getY() - (view.getHeight() - range_radius * 2)) * -1;
                getColor(currentX, currentY, false);
                break;
            case MotionEvent.ACTION_MOVE://拖动
                currentX = (int) event.getX() - (view.getWidth() / 2 - range_radius);
                currentY = (int) (event.getY() - (view.getHeight() - range_radius * 2)) * -1;
                getColor(currentX, currentY, false);
                break;
            case MotionEvent.ACTION_UP:
                currentX = (int) event.getX() - (view.getWidth() / 2 - range_radius);
                currentY = (int) (event.getY() - (view.getHeight() - range_radius * 2)) * -1;
                getColor(currentX, currentY, true);
                break;
        }
        return true;
    }

    public void getColor(int currentX, int currentY, boolean flag) {
        hue = (float) ColorConversion.getDegreeByPoint(new Point(centerX, centerY), new Point(currentX, currentY));
        float diff = (float) Math.sqrt((currentX - centerX) * (currentX - centerX) + (currentY - centerY) *
                (currentY - centerY));//两个圆心距离+颜色选择器半径
        saturation = diff * 100 / range_radius;
        if (flag) {
            LogUtil.d(TAG, "diff:" + diff);
            LogUtil.d(TAG, "hue:" + hue + "  saturation:" + saturation);
        }
        if (hue > 360 || hue < 0)
            hue = 0;
        if (saturation > 100)
            saturation = 100;
        else if (saturation < 0)
            saturation = 0;
        if (brightness < 0)
            brightness = 0;
        else if (brightness > 100)
            brightness = 100;
        if (diff <= (range_radius - picker_radius)) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv_color_picker.getLayoutParams();
            params.setMargins(Math.abs(currentX) - picker_radius, Math.abs(currentY) - picker_radius, 0, 0);
            iv_color_picker.setLayoutParams(params);
        } else {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv_color_picker.getLayoutParams();
            float top = Math.abs(range_radius - ((currentY - centerY) * (range_radius - picker_radius) / diff));
            float left = Math.abs(range_radius + ((currentX - centerX) * (range_radius - picker_radius) / diff));
            top -= picker_radius;
            left -= picker_radius;

            params.setMargins((int) left, (int) top, 0, 0);
            iv_color_picker.setLayoutParams(params);
        }
        if (colorChangedListener != null && flag) {//读取颜色
            LogUtil.d("AC" + TAG, "hue:" + hue + " saturation:" + saturation + " brightness:" + brightness);
            int[] colors = ColorConversion.hsvToRgb(hue, saturation, brightness);
            colorChangedListener.colorChanged(colors[0], colors[1], colors[2]);
        }
        if (showPicker != null) {
            showPicker.setBackgroundColor(getColor(hue, saturation, brightness));
        }
        requestDisallowInterceptTouchEvent(true);//通知父控件勿拦截本控件touch事件
    }

    public int getColor(float hue, float saturation, float brightness) {
        try {
            int[] colors = ColorConversion.hsvToRgb(hue, saturation, brightness);
            return Color.rgb(colors[0], colors[1], colors[2]);
        } catch (Exception e) {
            return 0;
        }
    }

    public void setBrightness(int brightness) {
        if (brightness < 0 || brightness > 100)
            return;
        this.brightness = brightness;
        iv_color_range_bg.setAlpha((100 - brightness) * 255 / 100);
        if (showPicker != null)
            showPicker.setBackgroundColor(getColor(hue, saturation, brightness));
    }

    public void setColorChangeWithBrightness() {
        LogUtil.d("AC" + TAG, "hue:" + hue + " saturation:" + saturation + " brightness:" + brightness);
        if (colorChangedListener != null) {//读取颜色
            int[] colors = ColorConversion.hsvToRgb(hue, saturation, brightness);
            colorChangedListener.colorChanged(colors[0], colors[1], colors[2]);
        }
    }

    public void setShowPicker(ImageView showPicker) {
        this.showPicker = showPicker;
    }

    public void setOnTouchListenerView(View v) {
        this.view = v;
        v.setOnTouchListener(this);
    }

    public void setColorChangedListener(onColorChangedListener colorChangedListener) {
        this.colorChangedListener = colorChangedListener;
    }

    /**
     * 颜色变换监听接口
     */
    public interface onColorChangedListener {
        public void colorChanged(int red, int green, int blue);
    }

}
