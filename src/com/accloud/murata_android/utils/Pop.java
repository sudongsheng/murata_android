package com.accloud.murata_android.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import com.accloud.cloudservice.AC;
import com.accloud.cloudservice.PayloadCallback;
import com.accloud.cloudservice.VoidCallback;
import com.accloud.murata_android.R;
import com.accloud.murata_android.activity.add.DeviceReadyActivity;
import com.accloud.murata_android.activity.add.ResetWifiActivity;
import com.accloud.murata_android.activity.device.MainActivity;
import com.accloud.murata_android.activity.login.LoginActivity;
import com.accloud.murata_android.application.MainApplication;
import com.accloud.murata_android.config.AppConstant;
import com.accloud.murata_android.config.Config;
import com.accloud.murata_android.config.ConstantCache;
import com.accloud.service.ACException;
import com.accloud.utils.PreferencesUtils;
import com.zxing.CaptureActivity;
import com.zxing.ShareActivity;

/**
 * Created by Administrator on 2015/1/15 0015.
 */
public class Pop {
    static Toast toast = null;
    static PopupWindow popupWindow;

    public static void popToast(Context context, String title) {
        if (toast == null)
            toast = Toast.makeText(context, title, Toast.LENGTH_LONG);
        else
            toast.setText(title);
        toast.show();
    }

    public static CustomDialog popDialog(Context context, int layout) {
        return new CustomDialog(context, layout).show();
    }

    public static CustomDialog popDialog(Context context, int layout, int style) {
        return new CustomDialog(context, layout, style).show();
    }

    public static void popWindow(final Activity activity, View parent, int flag) {
        backgroundAlpha(activity, 0.5f);
        LayoutInflater mLayoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //弹出窗口包含的视图
        View menuView = mLayoutInflater.inflate(R.layout.popup_more, null, true);
        //创建弹出窗口，指定大小
        popupWindow = new PopupWindow(menuView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        //设置窗口显示的动画效果
        popupWindow.setAnimationStyle(R.style.PopupAnimation);

        //设置弹出窗口的背景
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        //设置是否允许在外点击使其消失
        popupWindow.setOutsideTouchable(true);
        //设置窗口显示的位置
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        //更新弹出窗口的状态
        popupWindow.update();
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(activity, 1f);
            }
        });
        initPopupView(activity, menuView, flag);
    }

    public static void initPopupView(final Activity activity, View view, int flag) {
        if (flag == AppConstant.POPUP_MORE) {
            view.findViewById(R.id.popup_add).setVisibility(View.GONE);
            TextView share = (TextView) view.findViewById(R.id.popup_share);
            TextView reset_wifi = (TextView) view.findViewById(R.id.popup_reset_wifi);
            TextView delete = (TextView) view.findViewById(R.id.popup_delete);
            TextView logout = (TextView) view.findViewById(R.id.popup_logout);
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindow.dismiss();
                    if (ConstantCache.deviceId == 0) {
                        Pop.popToast(activity, activity.getString(R.string.bind_device_first));
                    } else if (ConstantCache.owner != MainApplication.getInstance().mUser.getUserId()) {
                        Pop.popToast(activity, activity.getString(R.string.share_with_owner_only));
                    } else {
                        AC.bindMgr().getShareCode(Config.SUBMAJORDOMAIN, ConstantCache.deviceId, 5 * 60, new PayloadCallback<String>() {
                            @Override
                            public void success(String shareCode) {
                                Intent intent = new Intent(activity, ShareActivity.class);
                                intent.putExtra("shareCode", shareCode);
                                intent.putExtra("deviceId", ConstantCache.deviceId);
                                activity.startActivity(intent);
                            }

                            @Override
                            public void error(ACException e) {
                                Pop.popToast(activity, e.getErrorCode() + "-->" + e.getMessage());
                            }
                        });
                    }
                }
            });
            reset_wifi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindow.dismiss();
                    if (ConstantCache.deviceId == 0) {
                        Pop.popToast(activity, activity.getString(R.string.bind_device_first));
                    } else {
                        Intent intent = new Intent(activity, ResetWifiActivity.class);
                        activity.startActivity(intent);
                    }
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindow.dismiss();
                    if (ConstantCache.deviceId == 0) {
                        Pop.popToast(activity, activity.getString(R.string.bind_device_first));
                    } else {
                        new AlertDialog.Builder(activity).setTitle(activity.getString(R.string.unbind)).setMessage(activity.getString(R.string.unbind_confirm)).setNegativeButton(activity.getString(R.string.no), null).setPositiveButton(activity.getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int m) {
                                unbindDevice(activity);
                            }
                        }).show();
                    }
                }
            });
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindow.dismiss();
                    AC.accountMgr().logout();
                    Intent intent = new Intent(activity, LoginActivity.class);
                    activity.startActivity(intent);
                }
            });
        } else {
            view.findViewById(R.id.popup_more).setVisibility(View.GONE);
            TextView bind = (TextView) view.findViewById(R.id.popup_bind);
            TextView bindShare = (TextView) view.findViewById(R.id.popup_bind_share);
            bind.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindow.dismiss();
                    Intent intent = new Intent(activity, DeviceReadyActivity.class);
                    activity.startActivity(intent);
                }
            });
            bindShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindow.dismiss();
                    Intent intent = new Intent(activity, CaptureActivity.class);
                    activity.startActivity(intent);
                }
            });
        }

    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public static void backgroundAlpha(Activity activity, float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        activity.getWindow().setAttributes(lp);
    }

    //管理员解绑设备
    public static void unbindDevice(final Activity activity) {
        if (ConstantCache.deviceId == 0)
            Pop.popToast(activity, activity.getString(R.string.bind_device_first));
        else {
            AC.bindMgr().unbindDevice(Config.SUBMAJORDOMAIN, ConstantCache.deviceId, new VoidCallback() {
                @Override
                public void success() {
                    Pop.popToast(activity, activity.getString(R.string.unbind_success));
                    PreferencesUtils.putLong(activity, "deviceId", 0);
                    ((MainActivity) activity).getDeviceList();
                }

                @Override
                public void error(ACException e) {
                    Pop.popToast(activity, e.getErrorCode() + "-->" + e.getMessage());
                }
            });
        }
    }
}
