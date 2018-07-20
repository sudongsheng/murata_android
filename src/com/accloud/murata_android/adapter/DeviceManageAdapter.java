package com.accloud.murata_android.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.accloud.murata_android.R;
import com.accloud.murata_android.activity.personal.MyDeviceActivity;
import com.accloud.murata_android.config.ConstantCache;
import com.accloud.service.ACUserDevice;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/4/30.
 */
public class DeviceManageAdapter extends BaseAdapter {
    public List<ACUserDevice> deviceList;
    private Context context;

    public DeviceManageAdapter(Context context) {
        this.context = context;
        deviceList = new ArrayList<>();
//        deviceList.add(new ACUserDevice(1, 1, "1111", 1, "", "1111"));
//        deviceList.add(new ACUserDevice(2, 1, "2222", 1, "", "1111"));
    }

    @Override
    public int getCount() {
        return deviceList.size();
    }

    @Override
    public Object getItem(int i) {
        return deviceList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.adapter_device_manager, null);
        RelativeLayout rl = (RelativeLayout) view.findViewById(R.id.adapter_device_manager);
        TextView deviceName = (TextView) view.findViewById(R.id.adapter_deviceName);
        final ACUserDevice userDevice = deviceList.get(i);
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MyDeviceActivity.class);
                ConstantCache.userDevice = userDevice;
                context.startActivity(intent);
            }
        });
        deviceName.setText(userDevice.getName());
        return view;
    }
}
