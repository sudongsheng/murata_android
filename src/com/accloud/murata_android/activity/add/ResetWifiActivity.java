package com.accloud.murata_android.activity.add;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.accloud.cloudservice.AC;
import com.accloud.cloudservice.ACDeviceActivator;
import com.accloud.cloudservice.PayloadCallback;
import com.accloud.murata_android.R;
import com.accloud.murata_android.activity.BaseActivity;
import com.accloud.murata_android.config.ConstantCache;
import com.accloud.murata_android.utils.Pop;
import com.accloud.service.ACDeviceBind;
import com.accloud.service.ACException;

public class ResetWifiActivity extends BaseActivity implements View.OnClickListener {

    private TextView wifi_name;
    private Button reset;
    private EditText password;

    ACDeviceActivator deviceActivator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_wifi);
        setTitle(getString(R.string.reset_wifi_title));
        setNavBtn(R.drawable.back, 0);
        wifi_name = (TextView) findViewById(R.id.reset_text_value);
        reset = (Button) findViewById(R.id.reset);
        password = (EditText) findViewById(R.id.reset_edit_pwd);
        reset.setOnClickListener(this);
        deviceActivator = AC.deviceActivator(AC.DEVICE_MURATA);
        wifi_name.setText(deviceActivator.getSSID());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.reset:
                reset.setEnabled(false);
                reset.setText(getString(R.string.reset_wifi_active));
                deviceActive();
                break;
        }
    }

    public void deviceActive() {
        deviceActivator.startAbleLink(deviceActivator.getSSID(), password.getText().toString(), ConstantCache.physicalDeviceId, AC.DEVICE_ACTIVATOR_DEFAULT_TIMEOUT, new PayloadCallback<ACDeviceBind>() {
            @Override
            public void success(ACDeviceBind code) {
                reset.setEnabled(true);
                reset.setText(getString(R.string.reset_wifi_active_hint));
                Pop.popToast(ResetWifiActivity.this, getString(R.string.reset_wifi_success));
            }

            @Override
            public void error(ACException e) {
                reset.setEnabled(true);
                reset.setText(getString(R.string.reset_wifi_active_hint));
                Pop.popToast(ResetWifiActivity.this, e.getErrorCode() + "-->" + e.getMessage());
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        deviceActivator.stopAbleLink();
    }

    @Override
    protected void HandleTitleBarEvent(TitleBar component, View v) {
        finish();
    }
}
