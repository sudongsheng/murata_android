package com.accloud.murata_android.activity.device;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.ToggleButton;
import com.accloud.cloudservice.PayloadCallback;
import com.accloud.murata_android.R;
import com.accloud.murata_android.activity.BaseActivity;
import com.accloud.murata_android.config.AppConstant;
import com.accloud.murata_android.config.Config;
import com.accloud.murata_android.config.ConstantCache;
import com.accloud.murata_android.controller.SendToDevice;
import com.accloud.service.ACDeviceMsg;
import com.accloud.service.ACException;

/**
 * Created by Administrator on 2015/4/30.
 */
public class MotoActivity extends BaseActivity {
    private SeekBar seekBar;
    private ToggleButton toggleButton;
    private int value;

    private SendToDevice sendToDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String Value = getIntent().getStringExtra("value");
        setContentView(R.layout.activity_moto);
        setTitle(getString(R.string.moto_title));
        setNavBtn(R.drawable.back, 0);

        seekBar = (SeekBar) findViewById(R.id.moto_seekbar);
        toggleButton = (ToggleButton) findViewById(R.id.moto_toggle);
        sendToDevice = new SendToDevice();

        if (Value.equals("0")) {
            toggleButton.setChecked(false);
        } else {
            toggleButton.setChecked(true);
        }
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                value = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                sendToDevice.setMotoSpeed(value, new PayloadCallback<ACDeviceMsg>() {
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
                    sendToDevice.openDevice(AppConstant.STREAMID_MOTO, new PayloadCallback<ACDeviceMsg>() {
                        @Override
                        public void success(ACDeviceMsg acDeviceMsg) {
                            ConstantCache.moto_status = 1;
                        }

                        @Override
                        public void error(ACException e) {

                        }
                    });
                } else {
                    sendToDevice.closeDevice(AppConstant.STREAMID_MOTO, new PayloadCallback<ACDeviceMsg>() {
                        @Override
                        public void success(ACDeviceMsg acDeviceMsg) {
                            ConstantCache.moto_status = 2;
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
