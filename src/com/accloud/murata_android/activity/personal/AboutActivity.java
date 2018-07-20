package com.accloud.murata_android.activity.personal;

import android.os.Bundle;
import android.view.View;
import com.accloud.murata_android.R;
import com.accloud.murata_android.activity.BaseActivity;

/**
 * Created by Xuri on 2015/1/27.
 */
public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setTitle(getString(R.string.about_title));
        setNavBtn(R.drawable.back, 0);
    }

    @Override
    protected void HandleTitleBarEvent(TitleBar component, View v) {
        finish();
    }
}
