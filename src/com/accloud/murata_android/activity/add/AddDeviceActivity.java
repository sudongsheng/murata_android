package com.accloud.murata_android.activity.add;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.accloud.cloudservice.AC;
import com.accloud.cloudservice.ACDeviceActivator;
import com.accloud.cloudservice.PayloadCallback;
import com.accloud.cloudservice.VoidCallback;
import com.accloud.murata_android.R;
import com.accloud.murata_android.activity.BaseActivity;
import com.accloud.murata_android.config.Config;
import com.accloud.murata_android.config.ConstantCache;
import com.accloud.murata_android.listerner.OnCustomDialogListener;
import com.accloud.murata_android.utils.CustomDialog;
import com.accloud.murata_android.utils.Pop;
import com.accloud.service.ACDeviceBind;
import com.accloud.service.ACException;
import com.accloud.service.ACUserDevice;
import com.accloud.utils.PreferencesUtils;

import java.util.List;

public class AddDeviceActivity extends BaseActivity implements View.OnClickListener {

    private TextView wifi_name;
    private Button connect;
    private EditText password;

    ACDeviceActivator deviceActivator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_wifi);
        setTitle(getString(R.string.add_device_title));
        setNavBtn(R.drawable.back, 0);
        wifi_name = (TextView) findViewById(R.id.set_wifi_name);
        connect = (Button) findViewById(R.id.connect);
        password = (EditText) findViewById(R.id.set_pwd);
        connect.setOnClickListener(this);
        deviceActivator = AC.deviceActivator(AC.DEVICE_MURATA);
    }

    @Override
    protected void onResume() {
        super.onResume();
        wifi_name.setText(deviceActivator.getSSID());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.connect:
                connect.setEnabled(false);
                connect.setText(getString(R.string.add_device_active));
                deviceActive();
                break;
        }
    }

    public void deviceActive() {
        deviceActivator.startAbleLink(deviceActivator.getSSID(), password.getText().toString(), AC.DEVICE_ACTIVATOR_DEFAULT_TIMEOUT, new PayloadCallback<List<ACDeviceBind>>() {
            @Override
            public void success(List<ACDeviceBind> deviceBinds) {
                if (deviceBinds.size() > 0) {
                    deviceActivator.stopAbleLink();
                }
                ConstantCache.physicalDeviceId = deviceBinds.get(0).getPhysicalDeviceId();
                connect.setText(getString(R.string.add_device_bind));
                Pop.popToast(AddDeviceActivity.this, getString(R.string.add_device_active_success));
                bindDevice();
            }

            @Override
            public void error(ACException e) {
                connect.setEnabled(true);
                connect.setText(getString(R.string.add_device_active_retry));
                Pop.popToast(AddDeviceActivity.this, e.getErrorCode() + "-->" + e.getMessage());
            }
        });
    }

    public void bindDevice() {
        AC.bindMgr().bindDevice(Config.SUBMAJORDOMAIN, ConstantCache.physicalDeviceId, "", new PayloadCallback<ACUserDevice>() {
            @Override
            public void success(ACUserDevice userDevice) {
                Pop.popToast(AddDeviceActivity.this, getString(R.string.device) + userDevice.getDeviceId() + getString(R.string.add_device_bind_success));
                PreferencesUtils.putLong(AddDeviceActivity.this, "deviceId", userDevice.getDeviceId());
                ConstantCache.deviceId = userDevice.getDeviceId();
                ConstantCache.owner = userDevice.getOwner();
                ConstantCache.subDomainId = userDevice.getSubDomainId();
                PreferencesUtils.putString(AddDeviceActivity.this, "aesKey" + userDevice.getDeviceId(), userDevice.getAesKey());
                showDialog(userDevice.getDeviceId());
            }

            @Override
            public void error(ACException e) {
                connect.setEnabled(true);
                connect.setText(getString(R.string.add_device_active_retry));
                Pop.popToast(AddDeviceActivity.this, e.getErrorCode() + "-->" + e.getMessage());
            }
        });
    }

    private void showDialog(final long deviceId) {
        final CustomDialog dialog = Pop.popDialog(AddDeviceActivity.this, R.layout.dialog_set_name, R.style.customDialog);
        LinearLayout linearLayout = dialog.findView(R.id.dialog_set_name);
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) linearLayout.getLayoutParams();
        params.width = getResources().getDisplayMetrics().widthPixels - 100;
        linearLayout.setLayoutParams(params);
        final EditText editName = dialog.findView(R.id.set_device_name);
        Button cancel = dialog.findView(R.id.set_cancel);
        Button sure = dialog.findView(R.id.set_sure);
        dialog.setOnCustomDialogListener(new OnCustomDialogListener() {
            @Override
            public void onFinish() {
                Pop.popToast(AddDeviceActivity.this, getString(R.string.add_device_add_success));
                dialog.dismiss();
                AddDeviceActivity.this.finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                AddDeviceActivity.this.finish();
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String deviceName = editName.getText().toString();
                if (deviceName.length() > 0) {
                    AC.bindMgr().changeName(Config.SUBMAJORDOMAIN, deviceId, deviceName, new VoidCallback() {
                        @Override
                        public void success() {
                            dialog.dismiss();
                            ConstantCache.deviceName = deviceName;
                            AddDeviceActivity.this.finish();
                        }

                        @Override
                        public void error(ACException e) {
                            Pop.popToast(AddDeviceActivity.this, e.getErrorCode() + "-->" + e.getMessage());
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

    @Override
    protected void onStop() {
        super.onStop();
        deviceActivator.stopAbleLink();
    }
}
