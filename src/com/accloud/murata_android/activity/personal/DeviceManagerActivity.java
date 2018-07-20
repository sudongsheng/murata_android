package com.accloud.murata_android.activity.personal;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import com.accloud.cloudservice.AC;
import com.accloud.cloudservice.PayloadCallback;
import com.accloud.murata_android.R;
import com.accloud.murata_android.activity.BaseActivity;
import com.accloud.murata_android.adapter.DeviceManageAdapter;
import com.accloud.murata_android.config.AppConstant;
import com.accloud.murata_android.utils.Pop;
import com.accloud.service.ACException;
import com.accloud.service.ACUserDevice;

import java.util.List;

/**
 * Created by Administrator on 2015/4/29.
 */
public class DeviceManagerActivity extends BaseActivity {
    private ListView deviceList;
    private DeviceManageAdapter adapter;
    private ImageView noDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_manage);
        setTitle(getString(R.string.device_manager_title));
        setNavBtn(R.drawable.back, 0);

        deviceList = (ListView) findViewById(R.id.device_manage_list);
        noDevice = (ImageView) findViewById(R.id.device_manage_empty);
        adapter = new DeviceManageAdapter(this);
        deviceList.setAdapter(adapter);

        noDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pop.popWindow(DeviceManagerActivity.this, findViewById(R.id.device_manage_list), AppConstant.POPUP_ADD);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        noDevice.setVisibility(View.INVISIBLE);
        AC.bindMgr().listDevices(new PayloadCallback<List<ACUserDevice>>() {
            @Override
            public void success(List<ACUserDevice> userDevices) {
                if (userDevices == null) {
                    noDevice.setVisibility(View.VISIBLE);
                } else if (userDevices.size() == 0) {
                    noDevice.setVisibility(View.VISIBLE);
                    adapter.deviceList.clear();
                    adapter.notifyDataSetChanged();
                } else {
                    adapter.deviceList = userDevices;
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void error(ACException e) {
                Pop.popToast(DeviceManagerActivity.this, e.getErrorCode() + "-->" + e.getMessage());
            }
        });
    }

    @Override
    protected void HandleTitleBarEvent(TitleBar component, View v) {
        finish();
    }
}
