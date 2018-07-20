package com.accloud.murata_android.activity.personal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.accloud.cloudservice.AC;
import com.accloud.cloudservice.PayloadCallback;
import com.accloud.cloudservice.VoidCallback;
import com.accloud.murata_android.R;
import com.accloud.murata_android.activity.BaseActivity;
import com.accloud.murata_android.activity.device.MainActivity;
import com.accloud.murata_android.application.MainApplication;
import com.accloud.murata_android.config.Config;
import com.accloud.murata_android.config.ConstantCache;
import com.accloud.murata_android.listerner.OnCustomDialogListener;
import com.accloud.murata_android.utils.CustomDialog;
import com.accloud.murata_android.utils.Pop;
import com.accloud.service.ACException;
import com.accloud.utils.LogUtil;
import com.accloud.utils.PreferencesUtils;
import com.zxing.ShareActivity;

/**
 * Created by Administrator on 2015/4/30.
 */
public class MyDeviceActivity extends BaseActivity implements View.OnClickListener {
    private TextView myDeviceName;
    private TextView myDeviceShareMember;
    private TextView myDeviceShareDevice;
    private Button myDeviceChoose;
    private Button myDeviceUnbind;

    private void initView() {
        myDeviceName = (TextView) findViewById(R.id.my_device_name);
        myDeviceShareMember = (TextView) findViewById(R.id.my_device_share_member);
        myDeviceShareDevice = (TextView) findViewById(R.id.my_device_share_device);
        myDeviceChoose = (Button) findViewById(R.id.my_device_choose);
        myDeviceUnbind = (Button) findViewById(R.id.my_device_unbind);

        myDeviceName.setText(ConstantCache.userDevice.getName());
        myDeviceName.setOnClickListener(this);
        myDeviceShareMember.setOnClickListener(this);
        myDeviceShareDevice.setOnClickListener(this);
        myDeviceChoose.setOnClickListener(this);
        myDeviceUnbind.setOnClickListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_device);
        setTitle(getString(R.string.my_device_title));
        setNavBtn(R.drawable.back, 0);
        initView();
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.my_device_name:
                showDialog(ConstantCache.userDevice.getDeviceId());
                break;
            case R.id.my_device_share_member:
                intent = new Intent(this, AuthorizedMemberActivity.class);
                intent.putExtra("isOwner", MainApplication.getInstance().mUser.getUserId() == ConstantCache.userDevice.getOwner());
                startActivity(intent);
                break;
            case R.id.my_device_share_device:
                if (ConstantCache.userDevice.getOwner() != MainApplication.getInstance().mUser.getUserId()) {
                    Pop.popToast(this, getString(R.string.not_owner));
                } else {
                    AC.bindMgr().getShareCode(Config.SUBMAJORDOMAIN, ConstantCache.userDevice.getDeviceId(), 5 * 60, new PayloadCallback<String>() {
                        @Override
                        public void success(String shareCode) {
                            Intent intent = new Intent(MyDeviceActivity.this, ShareActivity.class);
                            intent.putExtra("shareCode", shareCode);
                            intent.putExtra("deviceId", ConstantCache.userDevice.getDeviceId());
                            startActivity(intent);
                        }

                        @Override
                        public void error(ACException e) {
                            Pop.popToast(MyDeviceActivity.this, e.getErrorCode() + "-->" + e.getMessage());
                        }
                    });
                }
                break;
            case R.id.my_device_choose:
                PreferencesUtils.putLong(this, "deviceId", ConstantCache.userDevice.getDeviceId());
                ConstantCache.deviceId = ConstantCache.userDevice.getDeviceId();
                ConstantCache.deviceName = ConstantCache.userDevice.getName();
                ConstantCache.subDomainId = ConstantCache.userDevice.getSubDomainId();
                ConstantCache.owner = ConstantCache.userDevice.getOwner();
                ConstantCache.physicalDeviceId = ConstantCache.userDevice.getPhysicalDeviceId();
                ConstantCache.isDeviceOnline = false;
                PreferencesUtils.putString(this, "aesKey" + ConstantCache.deviceId, ConstantCache.userDevice.getAesKey());
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.my_device_unbind:
                unbindDevice();
                break;
        }
    }

    //解绑设备
    public void unbindDevice() {
        AC.bindMgr().unbindDevice(Config.SUBMAJORDOMAIN, ConstantCache.userDevice.getDeviceId(), new VoidCallback() {
            @Override
            public void success() {
                Pop.popToast(MyDeviceActivity.this, getString(R.string.unbind_success));
                MyDeviceActivity.this.finish();
            }

            @Override
            public void error(ACException e) {
                Pop.popToast(MyDeviceActivity.this, e.getErrorCode() + "-->" + e.getMessage());
            }
        });
    }

    @Override
    protected void HandleTitleBarEvent(TitleBar component, View v) {
        finish();
    }

    private void showDialog(final long deviceId) {
        final CustomDialog dialog = Pop.popDialog(MyDeviceActivity.this, R.layout.dialog_set_name, R.style.customDialog);
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
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
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
                            myDeviceName.setText(deviceName);
                            dialog.dismiss();
                            ConstantCache.deviceName = deviceName;
                        }

                        @Override
                        public void error(ACException e) {
                            Pop.popToast(MyDeviceActivity.this, e.getErrorCode() + "-->" + e.getMessage());
                        }
                    });
                }
            }
        });
    }
}
