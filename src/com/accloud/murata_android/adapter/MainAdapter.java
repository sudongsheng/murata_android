package com.accloud.murata_android.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.accloud.cloudservice.AC;
import com.accloud.cloudservice.PayloadCallback;
import com.accloud.murata_android.R;
import com.accloud.murata_android.activity.device.LightActivity;
import com.accloud.murata_android.activity.device.MainActivity;
import com.accloud.murata_android.activity.device.MotoActivity;
import com.accloud.murata_android.config.AppConstant;
import com.accloud.murata_android.config.ConstantCache;
import com.accloud.murata_android.controller.SendToDevice;
import com.accloud.murata_android.listerner.OnCustomDialogListener;
import com.accloud.murata_android.model.UserDevice;
import com.accloud.murata_android.utils.CustomDialog;
import com.accloud.murata_android.utils.Pop;
import com.accloud.murata_android.utils.ViewHolder;
import com.accloud.service.ACDeviceMsg;
import com.accloud.service.ACException;
import com.accloud.service.ACObject;
import com.accloud.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/4/21.
 */
public class MainAdapter extends BaseAdapter {
    public static final String TAG = MainAdapter.class.getSimpleName();
    public UserDevice[] deviceList;
    private MainActivity activity;
    private Boolean flag = true;
    private CustomDialog dialog;
    private SendToDevice sendToDevice;
    String led_status;
    String moto_status;

    public MainAdapter(MainActivity activity) {
        this.activity = activity;
        deviceList = new UserDevice[5];
        sendToDevice = new SendToDevice();
    }

    @Override
    public int getCount() {
        return deviceList.length;
    }

    @Override
    public Object getItem(int i) {
        return deviceList[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(activity).inflate(R.layout.adapter_main_list_device, null);
        RelativeLayout rr = ViewHolder.get(view, R.id.adapter_main_list_device);
        TextView deviceName = ViewHolder.get(view, R.id.adapter_main_deviceName);
        TextView icon_nav = ViewHolder.get(view, R.id.adapter_main_icon_nav);
        ImageView img = ViewHolder.get(view, R.id.adapter_main_icon);
        final ToggleButton toggleButton = ViewHolder.get(view, R.id.adapter_main_toggle_button);

        if (ConstantCache.isDeviceOnline) {
            deviceName.setTextColor(activity.getResources().getColor(R.color.phone_text));
        } else
            deviceName.setTextColor(activity.getResources().getColor(R.color.login_text));
        final UserDevice device = deviceList[i];

        switch (device.getDetailName()) {
            case AppConstant.STREAMID_BUZZER:
                deviceName.setText(activity.getString(R.string.buffer));
                icon_nav.setVisibility(View.INVISIBLE);
                toggleButton.setVisibility(View.VISIBLE);
                if (device.getValue().equals("0")) {
                    toggleButton.setChecked(false);
                } else {
                    toggleButton.setChecked(true);
                }
                setImgBackageResources(img, R.drawable.icon_buffer_open, R.drawable.icon_buffer_close);
                break;
            case AppConstant.STREAMID_LIGHT_1:
                deviceName.setText(activity.getString(R.string.light1));
                icon_nav.setVisibility(View.INVISIBLE);
                toggleButton.setVisibility(View.VISIBLE);
                if (device.getValue().equals("0")) {
                    toggleButton.setChecked(false);
                } else {
                    toggleButton.setChecked(true);
                }
                setImgBackageResources(img, R.drawable.icon_light_on, R.drawable.icon_light_off);
                break;
            case AppConstant.STREAMID_LIGHT_2:
                deviceName.setText(activity.getString(R.string.light2));
                icon_nav.setVisibility(View.INVISIBLE);
                toggleButton.setVisibility(View.VISIBLE);
                if (device.getValue().equals("0")) {
                    toggleButton.setChecked(false);
                } else {
                    toggleButton.setChecked(true);
                }
                setImgBackageResources(img, R.drawable.icon_light_on, R.drawable.icon_light_off);
                break;
            case AppConstant.STREAMID_LED:
                deviceName.setText(activity.getString(R.string.led));
                if ((ConstantCache.led_status == 0 && device.getValue().equals("0")) || ConstantCache.led_status == 2) {
                    icon_nav.setText(activity.getString(R.string.off));
                    led_status = "0";
                } else {
                    icon_nav.setText(activity.getString(R.string.on));
                    led_status = "1";
                }
                toggleButton.setVisibility(View.INVISIBLE);
                icon_nav.setVisibility(View.VISIBLE);
                setImgBackageResources(img, R.drawable.icon_led_on, R.drawable.icon_led_off);
                break;
            case AppConstant.STREAMID_MOTO:
                deviceName.setText(activity.getString(R.string.moto));
                if ((ConstantCache.moto_status == 0 && device.getValue().equals("0")) || ConstantCache.moto_status == 2) {
                    icon_nav.setText(activity.getString(R.string.off));
                    moto_status = "0";
                } else {
                    icon_nav.setText(activity.getString(R.string.on));
                    moto_status = "1";
                }
                toggleButton.setVisibility(View.INVISIBLE);
                icon_nav.setVisibility(View.VISIBLE);
                setImgBackageResources(img, R.drawable.icon_moto_open, R.drawable.icon_moto_close);
                break;
        }
        rr.setOnClickListener(new View.OnClickListener() {
            Intent intent;

            @Override
            public void onClick(View view) {
                switch (device.getDetailName()) {
                    case AppConstant.STREAMID_BUZZER:
                    case AppConstant.STREAMID_LIGHT_1:
                    case AppConstant.STREAMID_LIGHT_2:
                        if (toggleButton.isChecked())
                            toggleButton.setChecked(false);
                        else
                            toggleButton.setChecked(true);
                        break;
                    case AppConstant.STREAMID_LED:
                        intent = new Intent(activity, LightActivity.class);
                        intent.putExtra("value", led_status/*device.getValue()*/);
                        activity.startActivity(intent);
                        break;
                    case AppConstant.STREAMID_MOTO:
                        intent = new Intent(activity, MotoActivity.class);
                        intent.putExtra("value", moto_status/*device.getValue()*/);
                        activity.startActivity(intent);
                        break;
                }
            }
        });
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (flag && b) {
                    showProgressDialog();
                    toggleButton.setEnabled(false);
                    sendToDevice.openDevice(device.getDetailName(), new PayloadCallback<ACDeviceMsg>() {
                        @Override
                        public void success(ACDeviceMsg respMsg) {
                            dismissProgressDialog();
                            toggleButton.setEnabled(true);
                            ACObject object = respMsg.getObject();
                            if (object.get("result").toString().equals("0")) {
                                Pop.popToast(AC.context, activity.getString(R.string.open));
                            } else {
                                if (object.get("code").toString().equals("103"))
                                    Pop.popToast(AC.context, activity.getString(R.string.open_error));
                                else
                                    Pop.popToast(AC.context, activity.getString(R.string.retry));
                                flag = false;
                                toggleButton.setChecked(false);
                            }
                        }

                        @Override
                        public void error(ACException e) {
                            dismissProgressDialog();
                            toggleButton.setEnabled(true);
                            Pop.popToast(AC.context, e.getErrorCode() + "-->" + e.getMessage());
                            flag = false;
                            toggleButton.setChecked(false);
                        }
                    });
                } else if (flag) {
                    showProgressDialog();
                    toggleButton.setEnabled(false);
                    sendToDevice.closeDevice(device.getDetailName(), new PayloadCallback<ACDeviceMsg>() {
                        @Override
                        public void success(ACDeviceMsg respMsg) {
                            dismissProgressDialog();
                            toggleButton.setEnabled(true);
                            ACObject object = respMsg.getObject();
                            if (object.get("result").toString().equals("0")) {
                                Pop.popToast(AC.context, activity.getString(R.string.close));
                            } else {
                                if (object.get("code").toString().equals("103"))
                                    Pop.popToast(AC.context, activity.getString(R.string.close_error));
                                else
                                    Pop.popToast(AC.context, activity.getString(R.string.retry));
                                flag = false;
                                toggleButton.setChecked(true);
                            }
                        }

                        @Override
                        public void error(ACException e) {
                            dismissProgressDialog();
                            toggleButton.setEnabled(true);
                            Pop.popToast(AC.context, e.getErrorCode() + "-->" + e.getMessage());
                            flag = false;
                            toggleButton.setChecked(true);
                        }
                    });
                } else {
                    flag = true;
                }
            }
        });
        return view;
    }

    private void setImgBackageResources(ImageView img, int openDrawableRes, int closeDrawableRes) {
        if (ConstantCache.isDeviceOnline) {
            img.setBackgroundResource(openDrawableRes);
        } else {
            img.setBackgroundResource(closeDrawableRes);
        }
    }

    private void showProgressDialog() {
        if (dialog == null)
            dialog = Pop.popDialog(activity, R.layout.window_layout, R.style.customDialog);
        else
            dialog.show();
    }

    private void dismissProgressDialog() {
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
