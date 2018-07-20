package com.accloud.murata_android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import com.accloud.murata_android.R;
import com.accloud.murata_android.utils.DisplayUtils;

/**
 * Created by fangzhenyi on 2015/7/28 0028.
 */
public class CustomHumidityView extends RelativeLayout {
    private CustomLeftView customleftView;
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
    private int xCount = 6;
    //图表平行于y轴的数目
    private int yCount = 26;

    public void setChatlistenr(ChatListenner chatlistenr) {
        customChatView.setChatListener(chatlistenr);
    }


    public void setData(int[] data) {
        customChatView.setData(data);
    }

    public CustomHumidityView(Context context) {
        super(context);
        init();
    }

    public CustomHumidityView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        layoutInflater.inflate(R.layout.custom_humidity_view, this, true);
        customleftView = (CustomLeftView) this.findViewById(R.id.hum_customleftView);
        customChatView = (CustomHistoryView) this.findViewById(R.id.hum_customChatview);
        customleftView.setData(true);
        customChatView.setData(true);

        intervalX = DisplayUtils.dip2px(getContext(), 40);
        intervaleY = DisplayUtils.dip2px(getContext(), 22);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(yCount * intervalX, (xCount - 1) * intervaleY + DisplayUtils.dip2px(getContext(), 20));
    }
}
