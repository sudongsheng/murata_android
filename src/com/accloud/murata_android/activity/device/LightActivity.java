package com.accloud.murata_android.activity.device;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.accloud.cloudservice.PayloadCallback;
import com.accloud.murata_android.R;
import com.accloud.murata_android.activity.BaseActivity;
import com.accloud.murata_android.config.AppConstant;
import com.accloud.murata_android.config.ConstantCache;
import com.accloud.murata_android.controller.SendToDevice;
import com.accloud.murata_android.view.ColorPickerView;
import com.accloud.service.ACDeviceMsg;
import com.accloud.service.ACException;
import com.accloud.utils.LogUtil;

/**
 * Created by Administrator on 2015/4/22.
 */
public class LightActivity extends BaseActivity {
    //    private Button cancel;
//    private Button sure;
    private ImageView showPicker;
    private ColorPickerView pickerView;
    private SeekBar seekBar;
    private RelativeLayout touchRR;
    private String value;
    private SendToDevice sendToDevice;
    private ToggleButton toggleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        value = getIntent().getStringExtra("value");
        setContentView(R.layout.activity_light);
        setTitle(getString(R.string.light_title));
        setNavBtn(R.drawable.back, 0);
        initView();

        sendToDevice = new SendToDevice();
    }

    private void initView() {
        pickerView = (ColorPickerView) findViewById(R.id.light_picker);
        toggleButton = (ToggleButton) findViewById(R.id.light_toggle);

        if (value.equals("0")) {
            toggleButton.setChecked(false);
        } else if (value.equals("1")) {
            toggleButton.setChecked(true);
        } /*else {
            flag = true;
            showPicker.setBackgroundColor(Color.parseColor(value));
        }*/

        showPicker = (ImageView) findViewById(R.id.light_color_selector_bg);
//        cancel = (Button) findViewById(R.id.light_cancel);
//        sure = (Button) findViewById(R.id.light_sure);
        seekBar = (SeekBar) findViewById(R.id.light_seekbar);
        touchRR = (RelativeLayout) findViewById(R.id.light_touch_rr);
        pickerView.setShowPicker(showPicker);
        pickerView.setOnTouchListenerView(touchRR);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                pickerView.setBrightness(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                pickerView.setColorChangeWithBrightness();
            }
        });

        pickerView.setColorChangedListener(new ColorPickerView.onColorChangedListener() {
            @Override
            public void colorChanged(int red, int green, int blue) {
                sendToDevice.setLEDColor(red, green, blue, new PayloadCallback<ACDeviceMsg>() {
                    @Override
                    public void success(ACDeviceMsg acDeviceMsg) {

                    }

                    @Override
                    public void error(ACException e) {

                    }
                });
            }
        });

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    sendToDevice.openDevice(AppConstant.STREAMID_LED, new PayloadCallback<ACDeviceMsg>() {
                        @Override
                        public void success(ACDeviceMsg acDeviceMsg) {
                            ConstantCache.led_status = 1;
                        }

                        @Override
                        public void error(ACException e) {

                        }
                    });
                } else {
                    sendToDevice.closeDevice(AppConstant.STREAMID_LED, new PayloadCallback<ACDeviceMsg>() {
                        @Override
                        public void success(ACDeviceMsg acDeviceMsg) {
                            ConstantCache.led_status = 2;
                        }

                        @Override
                        public void error(ACException e) {

                        }
                    });
                }
            }
        });
    }

    @Override
    protected void HandleTitleBarEvent(TitleBar component, View v) {
        finish();
    }
}
