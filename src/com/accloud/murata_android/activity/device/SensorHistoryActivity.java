package com.accloud.murata_android.activity.device;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.*;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.accloud.murata_android.R;
import com.accloud.murata_android.activity.BaseActivity;
import com.accloud.murata_android.config.AppConstant;
import com.accloud.murata_android.utils.DisplayUtils;
import com.accloud.murata_android.view.ChatListenner;
import com.accloud.murata_android.view.CustomHumidityView;
import com.accloud.murata_android.view.CustomTemperatureView;

public class SensorHistoryActivity extends BaseActivity implements View.OnTouchListener, GestureDetector.OnGestureListener, ChatListenner {
    private CustomHumidityView customHumidityView;
    private CustomTemperatureView customTemperatureView;

    private ImageView foreward_image;

    private ImageView back_image;

    private LinearLayout chatLinear;

    private GestureDetector mygesturer;

    private int humData[] = {20, 28, 36, 35, 67, 45, 32, 45, 76, 45, 67, 36, 66, 44, 33, 54, 35, 53, 67, 54};
    private int temData[] = {10, 11, 20, -14, 26, 18, 19, 23, 24, 14, 26, 17, 18, 22, 14, 16, 18, 26, 35, 10, 20, 40, -4, 20};

    private int temData1[] = {13, 33, 25, 14, 26, 18, 29, 33, 34, 24, 26, 27, 28, 32, 14, 16, 18, 26, 35, 10, 20, 40, -4, 20};

    private int temData2[] = {23, 25, 32, 34, 23, 32, 41, 22, 28, 32, 14, 16, 18, 26, 35, 10, 20, 40, -4, 20, -13, 23, -3, 15, 16, 17, 32, 33};

    private TextView dateTextview;

    private Time time;

    private ImageView chatImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensors_historty);
        setTitle("温度和湿度");
        setNavBtn(R.drawable.back, 0);

        customHumidityView = (CustomHumidityView) findViewById(R.id.custom_humidity_view);
        customTemperatureView = (CustomTemperatureView) findViewById(R.id.custom_temperrature_view);
        customHumidityView.setData(humData);
        customTemperatureView.setData(temData);
        chatLinear = (LinearLayout) findViewById(R.id.sensors_history_ll);
        dateTextview = (TextView) findViewById(R.id.chat_date);
        chatImage = (ImageView) findViewById(R.id.chat_down_image);

        chatLinear.setOnTouchListener(this);

        chatImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mygesturer.onTouchEvent(event);
                return false;
            }
        });

        customTemperatureView.setChatlistenr(this);
        customHumidityView.setChatlistenr(this);

        foreward_image = (ImageView) findViewById(R.id.forward_image);
        back_image = (ImageView) findViewById(R.id.back_image);
        time = new Time();
        time.setToNow();

        dateTextview.setText("" + time.year + "年" + (time.month + 1) + "月" + time.monthDay + "日");

        foreward_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time.set(time.toMillis(true) - 24 * 3600 * 1000);
                dateTextview.setText("" + time.year + "年" + (time.month + 1) + "月" + time.monthDay + "日");
                customTemperatureView.setData(temData1);
            }
        });

        back_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time.set(time.toMillis(true) + 24 * 3600 * 1000);
                dateTextview.setText("" + time.year + "年" + (time.month + 1) + "月" + time.monthDay + "日");
                customTemperatureView.setData(temData2);
            }
        });

        chatImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SensorHistoryActivity.this, SensorActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.sensor_history_zoom_in, R.anim.sensor_history_zoom_out);
            }
        });

        mygesturer = new GestureDetector(this);


    }

    @Override
    protected void HandleTitleBarEvent(TitleBar component, View v) {
        finish();
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mygesturer.onTouchEvent(event);
        return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if ((e2.getY() - e1.getY() > AppConstant.BOUNDARY_DISTANT) && (Math.abs(velocityY) > AppConstant.BOUNDARY_SPEED)) {
            finish();
            overridePendingTransition(R.anim.sensor_history_zoom_in, R.anim.sensor_history_zoom_out);
        }

        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void moveActivity() {
        Intent intent = new Intent();
        intent.setClass(SensorHistoryActivity.this, SensorActivity.class);
        startActivity(intent);
        this.finish();
        overridePendingTransition(R.anim.sensor_history_zoom_in, R.anim.sensor_history_zoom_out);
    }
}
