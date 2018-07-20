package com.accloud.murata_android.view;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;
import com.accloud.murata_android.R;
import com.accloud.murata_android.utils.DisplayUtils;

/**
 * Created by fangzhenyi on 2015/7/27 0027.
 */
public class CustomLeftView extends View {
    //y方向间距
    private int intervaleY;
    private int xCount;

    private String temperature[] = {"60°", "40°", "30°", "20°", "10°", "0°", "-10°", "-20°"};

    private String humidity[] = {"100%", "80%", "60%", "40%", "20%", "0%"};

    private String data[];

    public CustomLeftView(Context context) {
        super(context);
        init();
    }

    public CustomLeftView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init() {
        intervaleY = DisplayUtils.dip2px(getContext(), 22);
    }

    public void setData(boolean a) {
        if (a) {
            data = humidity;
            xCount = 6;
        } else {
            data = temperature;
            xCount = 8;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(getResources().getColor(R.color.theme));

        //绘制虚线
        Paint paint = new Paint();
        PathEffect effects = new DashPathEffect(new float[]{5, 5, 5, 5}, 1);
        paint.setPathEffect(effects);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);//去锯齿
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(4);

        //绘制文字
        Paint paintText = new Paint();
        paintText.setColor(getResources().getColor(R.color.white));
        paintText.setStyle(Paint.Style.STROKE);
        paintText.setAntiAlias(true);
        paintText.setTextSize(DisplayUtils.sp2px(getContext(), 14));
        paintText.setTextAlign(Paint.Align.RIGHT);
        Path path = new Path();
        for (int i = 0; i < xCount; i++) {
            path.moveTo(0, intervaleY * i);
            path.lineTo(DisplayUtils.dip2px(getContext(), 50), intervaleY * i);
            canvas.drawPath(path, paint);
            canvas.drawText(data[i], DisplayUtils.dip2px(getContext(), 40), intervaleY * i - DisplayUtils.dip2px(getContext(), 5), paintText);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(DisplayUtils.dip2px(getContext(), 50), (xCount - 1) * intervaleY + DisplayUtils.dip2px(getContext(), 50));
    }
}
