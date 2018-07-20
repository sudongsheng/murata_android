package com.accloud.murata_android.activity.device;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.accloud.cloudservice.AC;
import com.accloud.cloudservice.PayloadCallback;
import com.accloud.cloudservice.VoidCallback;
import com.accloud.murata_android.R;
import com.accloud.murata_android.activity.BaseActivity;
import com.accloud.murata_android.config.AppConstant;
import com.accloud.murata_android.config.ConstantCache;
import com.accloud.service.*;
import com.accloud.utils.LogUtil;

public class SensorActivity extends BaseActivity /*implements View.OnTouchListener, GestureDetector.OnGestureListener */{
    private ImageView temperatureImg;
    private ImageView humidityPoint;
    private TextView temperatureTV;
    private TextView humidityTV;
    private LinearLayout linearLayout;
//    private ImageView upImage;

    private GestureDetector myGesture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);
        setTitle("温度和湿度");
        setNavBtn(R.drawable.back, 0);

        temperatureImg = (ImageView) findViewById(R.id.temperature);
        humidityPoint = (ImageView) findViewById(R.id.humidity_point);
        temperatureTV = (TextView) findViewById(R.id.temperature_text);
        humidityTV = (TextView) findViewById(R.id.humidity_text);
//        linearLayout = (LinearLayout) findViewById(R.id.sensor_ll);
//        upImage = (ImageView) findViewById(R.id.sensor_up_image);
//
//        linearLayout.setOnTouchListener(this);
//
//        myGesture = new GestureDetector(this);
//
//        upImage.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                myGesture.onTouchEvent(event);
//                return false;
//            }
//        });
//
//        upImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setClass(SensorActivity.this, SensorHistoryActivity.class);
//                startActivity(intent);
//                finish();
//                overridePendingTransition(R.anim.sensor_zoom_in, R.anim.sensor_zoom_out);
//            }
//        });

        AC.pushMgr().onReceive(new PayloadCallback<ACPushReceive>() {
            @Override
            public void success(ACPushReceive pushReceive) {
                LogUtil.d("test1", pushReceive.getPayload().toString());
                ACObject object = pushReceive.getPayload();
                int temperature = ((Long) object.get(AppConstant.STREAMID_TEMPERATURE)).intValue();
                int humidity = ((Long) object.get(AppConstant.STREAMID_HUMIDITY)).intValue();
                setTemperature(humidity);
                setHumidityPoint(temperature);
            }

            @Override
            public void error(ACException e) {

            }
        });
    }

    private void setTemperature(int h) {
        RelativeLayout.LayoutParams laParams = (RelativeLayout.LayoutParams) temperatureImg.getLayoutParams();
        laParams.height = (int) (h * 5 * getResources().getDisplayMetrics().density / 2);
        temperatureImg.setLayoutParams(laParams);
        temperatureTV.setText(h + "°");
    }

    private void setHumidityPoint(int h) {
        humidityPoint.setRotation(3 * h - 150);
        humidityTV.setText("" + h);
    }

    private int dip2px(int dpValue) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    protected void HandleTitleBarEvent(TitleBar component, View v) {
        finish();
    }

//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        myGesture.onTouchEvent(event);
//        return true;
//    }
//
//    @Override
//    public boolean onDown(MotionEvent e) {
//        return false;
//    }
//
//    @Override
//    public void onShowPress(MotionEvent e) {
//
//    }
//
//    @Override
//    public boolean onSingleTapUp(MotionEvent e) {
//        return false;
//    }
//
//    @Override
//    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//        return false;
//    }
//
//    @Override
//    public void onLongPress(MotionEvent e) {
//    }
//
//    @Override
//    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//        if (e2.getY() - e1.getY() < -dip2px(30) && Math.abs(velocityY) > dip2px(25)) {
//            Intent intent = new Intent();
//            intent.setClass(SensorActivity.this, SensorHistoryActivity.class);
//            startActivity(intent);
//            overridePendingTransition(R.anim.sensor_zoom_in, R.anim.sensor_zoom_out);
//        }
//        return false;
//    }
}
