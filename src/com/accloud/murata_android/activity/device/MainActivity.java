package com.accloud.murata_android.activity.device;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.accloud.cloudservice.AC;
import com.accloud.cloudservice.PayloadCallback;
import com.accloud.cloudservice.VoidCallback;
import com.accloud.murata_android.R;
import com.accloud.murata_android.activity.BaseActivity;
import com.accloud.murata_android.adapter.MainAdapter;
import com.accloud.murata_android.application.MainApplication;
import com.accloud.murata_android.config.AppConstant;
import com.accloud.murata_android.config.Config;
import com.accloud.murata_android.config.ConstantCache;
import com.accloud.murata_android.model.UserDevice;
import com.accloud.murata_android.utils.Pop;
import com.accloud.murata_android.view.DrawerMenu;
import com.accloud.service.*;
import com.accloud.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private ImageView noDevice;
    private ListView listView;
    private LinearLayout sensor_ll;
    private TextView temperature;
    private TextView humidity;
    private TextView light_sensor;
    private TextView ownerText;
    private MainAdapter adapter;
    DrawerMenu drawerMenu;

    //设备管理器
    ACBindMgr bindMgr;
    //消息队列管理器
    ACPushMgr pushMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(getString(R.string.main_title));
        setNavBtn(R.drawable.personal, R.drawable.more, R.drawable.add);
        drawerMenu = initSlideMenu();

        noDevice = (ImageView) findViewById(R.id.no_device);
        listView = (ListView) findViewById(R.id.device_list);
        sensor_ll = (LinearLayout) findViewById(R.id.main_sensor_ll);
        temperature = (TextView) findViewById(R.id.main_temperature);
        humidity = (TextView) findViewById(R.id.main_humidity);
        light_sensor = (TextView) findViewById(R.id.main_lightsensor);
        ownerText = (TextView) findViewById(R.id.userType);

        sensor_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SensorActivity.class);
                startActivity(intent);
            }
        });
        //获取设备管理器
        bindMgr = AC.bindMgr();
        //获取消息队列管理器
        pushMgr = AC.pushMgr();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!ConstantCache.isDeviceOnline) {
            temperature.setTextColor(getResources().getColor(R.color.login_text));
            humidity.setTextColor(getResources().getColor(R.color.login_text));
            light_sensor.setTextColor(getResources().getColor(R.color.login_text));
        }
        noDevice.setVisibility(View.INVISIBLE);
        noDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pop.popWindow(MainActivity.this, findViewById(R.id.device_list), AppConstant.POPUP_ADD);
            }
        });

        drawerMenu.updateNickName();
        if (!AC.accountMgr().isLogin())
            MainActivity.this.finish();
        else {
            adapter = new MainAdapter(this);
            getDeviceList();
        }
    }

    //开启监听实时消息
    public void watch(final long deviceId) {
        pushMgr.connect(new VoidCallback() {
            @Override
            public void success() {
                ACObject primaryKey = new ACObject();
                primaryKey.put("deviceId", deviceId);
                pushMgr.watch(new ACPushTable("murata_sensor", primaryKey, ACPushTable.OPTYPE_CREATE | ACPushTable.OPTYPE_UPDATE | ACPushTable.OPTYPE_REPLACE | ACPushTable.OPTYPE_DELETE), new VoidCallback() {
                    @Override
                    public void success() {
                    }

                    @Override
                    public void error(ACException e) {
                    }
                });
            }

            @Override
            public void error(ACException e) {
            }
        });
        pushMgr.onReceive(new PayloadCallback<ACPushReceive>() {
            @Override
            public void success(ACPushReceive pushReceive) {
                LogUtil.d("test1", pushReceive.getPayload().toString());
                ACObject object = pushReceive.getPayload();
                temperature.setText(object.get(AppConstant.STREAMID_HUMIDITY).toString() + " °C");
                humidity.setText(object.get(AppConstant.STREAMID_TEMPERATURE).toString() + " %");
                light_sensor.setText(object.get(AppConstant.STREAMID_LIGHTSENSOR).toString() + " lx");
//                if (adapter != null) {
//                    adapter.deviceList[0].setValue(object.get(AppConstant.STREAMID_BUZZER).toString());
//                    adapter.deviceList[1].setValue(object.get(AppConstant.STREAMID_LIGHT_1).toString());
//                    adapter.deviceList[2].setValue(object.get(AppConstant.STREAMID_LIGHT_2).toString());
//                    adapter.deviceList[3].setValue(object.get(AppConstant.STREAMID_LED).toString());
//                    adapter.deviceList[4].setValue(object.get(AppConstant.STREAMID_MOTO).toString());
//                    adapter.notifyDataSetChanged();
//                }
            }

            @Override
            public void error(ACException e) {

            }
        });
    }

    //获取传感器数据
    public void getSensorData() {
        ACMsg msg = new ACMsg();
        msg.setName("getSensorData");
        msg.put("deviceId", ConstantCache.deviceId);
        AC.sendToService(Config.SUBMAJORDOMAIN, Config.serviceName, Config.serviceVersion, msg, new PayloadCallback<ACMsg>() {
            @Override
            public void success(ACMsg acMsg) {
                ACObject object = acMsg.get("getSensorData");
                temperature.setText(object.get(AppConstant.STREAMID_TEMPERATURE).toString() + " °C");
                humidity.setText(object.get(AppConstant.STREAMID_HUMIDITY).toString() + " %");
                light_sensor.setText(object.get(AppConstant.STREAMID_LIGHTSENSOR).toString() + " lx");
            }

            @Override
            public void error(ACException e) {
                Pop.popToast(MainActivity.this, "getSensorData:" + e.getErrorCode() + "-->" + e.getMessage());
                temperature.setText("0 °C");
                humidity.setText("0 %");
                light_sensor.setText("0 lx");
            }
        });
    }

    //获取设备列表
    public void getDeviceList() {
        ACMsg msg = new ACMsg();
        msg.setName("getDeviceDetailList");
        msg.put("deviceId", ConstantCache.deviceId);
        AC.sendToService(Config.SUBMAJORDOMAIN, Config.serviceName, Config.serviceVersion, msg, new PayloadCallback<ACMsg>() {
            @Override
            public void success(ACMsg acMsg) {
                if (!acMsg.contains("deviceId")) {
                    noDevice.setVisibility(View.VISIBLE);
                    if (adapter != null) {
                        adapter.deviceList = new UserDevice[0];
                        listView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                    return;
                }
                Long deviceId = acMsg.get("deviceId");
                Long owner = acMsg.get("owner");
                if (owner == MainApplication.getInstance().mUser.getUserId()) {
                    ownerText.setText(getResources().getString(R.string.owner));
                } else {
                    ownerText.setText(getResources().getString(R.string.member));
                }
                Long subDomainId = acMsg.get("subDomainId");
                String deviceName = acMsg.get("deviceName");
                setTitle(getString(R.string.main_title) + ":" + deviceName);
                String physicalDeviceId = acMsg.get("physicalDeviceId");
                boolean isDeviceOnline = acMsg.get("isDeviceOnline");
                if (isDeviceOnline) {
                    temperature.setTextColor(getResources().getColor(R.color.white));
                    humidity.setTextColor(getResources().getColor(R.color.white));
                    light_sensor.setTextColor(getResources().getColor(R.color.white));
                }

                ConstantCache.deviceId = deviceId;
                ConstantCache.owner = owner;
                ConstantCache.subDomainId = subDomainId;
                ConstantCache.deviceName = deviceName;
                ConstantCache.isDeviceOnline = isDeviceOnline;
                ConstantCache.physicalDeviceId = physicalDeviceId;
                getSensorData();
                watch(deviceId);
                List<ACObject> objects = acMsg.get("deviceDetails");
                UserDevice[] userDevices = new UserDevice[5];
                for (int i = 0; i < objects.size(); i++) {
                    ACObject object = objects.get(i);
                    String name = object.get("detailName");
                    String value = object.get("value").toString();
                    switch (name) {
                        case AppConstant.STREAMID_BUZZER:
                            userDevices[0] = new UserDevice(name, value);
                            break;
                        case AppConstant.STREAMID_LIGHT_1:
                            userDevices[1] = new UserDevice(name, value);
                            break;
                        case AppConstant.STREAMID_LIGHT_2:
                            userDevices[2] = new UserDevice(name, value);
                            break;
                        case AppConstant.STREAMID_LED:
                            userDevices[3] = new UserDevice(name, value);
                            break;
                        case AppConstant.STREAMID_MOTO:
                            userDevices[4] = new UserDevice(name, value);
                            break;
                    }
                }
                adapter.deviceList = userDevices;
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void error(ACException e) {
                Pop.popToast(MainActivity.this, "GetDeviceList:" + e.getErrorCode() + "-->" + e.getMessage());
            }
        });
    }

    @Override
    protected void HandleTitleBarEvent(TitleBar component, View v) {
        switch (component) {
            case RIGHT:
                Pop.popWindow(MainActivity.this, findViewById(R.id.device_list), AppConstant.POPUP_MORE);
                break;
            case RIGHT2:
                Pop.popWindow(MainActivity.this, findViewById(R.id.device_list), AppConstant.POPUP_ADD);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerMenu.slidingMenu.isMenuShowing()) {
            drawerMenu.slidingMenu.showContent();
        } else
            super.onBackPressed();
    }

}
