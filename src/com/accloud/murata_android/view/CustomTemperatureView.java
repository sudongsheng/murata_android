package com.accloud.murata_android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import com.accloud.murata_android.R;
import com.accloud.murata_android.utils.DisplayUtils;

/**
 * Created by fangzhenyi on 2015/7/27 0027.
 */
public class CustomTemperatureView extends RelativeLayout {

    private CustomLeftView leftView;
    private CustomHistoryView customChatView;

    //X方向间距
    private int intervalX;
    //y方向间距
    private int intervaleY;
    //上一次距离
    private int lastx = 0;
    //图表上一次marginleft值
    private int lastmargin = 0;
    //图表平行于x轴的数目
    private int xCount = 8;
    //图表平行于y轴的数目
    private int yCount = 26;

    public CustomTemperatureView(Context context) {
        super(context);
        init();
    }

    public CustomTemperatureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.custom_temperature_view, this, true);
        leftView = (CustomLeftView) this.findViewById(R.id.custom_left_view);
        customChatView = (CustomHistoryView) this.findViewById(R.id.customChatview);
        leftView.setData(false);
        customChatView.setData(false);
        init();
    }

    public void init() {
        intervalX = DisplayUtils.dip2px(getContext(), 40);
        intervaleY = DisplayUtils.dip2px(getContext(), 22);
    }

    public void setChatlistenr(ChatListenner chatlisten) {
        customChatView.setChatListener(chatlisten);
    }

    public void setData(int[] data) {
        customChatView.setData(data);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(yCount * intervalX + DisplayUtils.sp2px(getContext(), 10), (xCount - 1) * intervaleY + DisplayUtils.dip2px(getContext(), 20));
    }
}
