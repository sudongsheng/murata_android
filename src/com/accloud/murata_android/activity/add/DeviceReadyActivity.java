package com.accloud.murata_android.activity.add;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.accloud.murata_android.R;
import com.accloud.murata_android.activity.BaseActivity;

/**
 * Created by Administrator on 2015/4/30.
 */
public class DeviceReadyActivity extends BaseActivity {
    private Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_ready);
        setTitle(getString(R.string.device_ready_title));
        setNavBtn(R.drawable.back, 0);
        next = (Button) findViewById(R.id.device_ready_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DeviceReadyActivity.this, AddDeviceActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void HandleTitleBarEvent(TitleBar component, View v) {
        finish();
    }
}
