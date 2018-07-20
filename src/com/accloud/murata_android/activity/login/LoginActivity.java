package com.accloud.murata_android.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.view.View;
import android.widget.*;
import com.accloud.murata_android.R;
import com.accloud.murata_android.activity.BaseActivity;
import com.accloud.murata_android.activity.device.MainActivity;
import com.accloud.murata_android.application.MainApplication;
import com.accloud.murata_android.config.AppConstant;
import com.accloud.murata_android.model.UserInfo;
import com.accloud.murata_android.utils.Pop;
import com.accloud.cloudservice.AC;
import com.accloud.cloudservice.PayloadCallback;
import com.accloud.murata_android.view.CusEditText;
import com.accloud.service.ACAccountMgr;
import com.accloud.service.ACException;
import com.accloud.service.ACUserInfo;
import com.accloud.utils.LogUtil;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private CusEditText phoneEditText;
    private CusEditText pwdEditText;
    private TextView forgetPwd;
    private TextView register;
    private Button loginBtn;

    private String tel;
    private String pwd;
    //账号管理器
    ACAccountMgr accountMgr;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setNavBtn(R.drawable.back, 0);
        setTitle(getString(R.string.login_title));
        initView();

        //通过AC获取账号管理器
        accountMgr = AC.accountMgr();
    }

    public void initView() {
        phoneEditText = (CusEditText) findViewById(R.id.login_edit_phone);
        pwdEditText = (CusEditText) findViewById(R.id.login_edit_pwd);
        forgetPwd = (TextView) findViewById(R.id.forgetPwd);
        register = (TextView) findViewById(R.id.register);
        loginBtn = (Button) findViewById(R.id.login);
        phoneEditText.setText(getString(R.string.login_phone));
        phoneEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        pwdEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        pwdEditText.setText(getString(R.string.login_pwd));

        forgetPwd.setText(Html.fromHtml("<u>" + getString(R.string.login_reset) + "？" + "</u>"));
        forgetPwd.setOnClickListener(this);
        register.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (accountMgr.isLogin()) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            LoginActivity.this.finish();
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.forgetPwd:
                intent = new Intent(LoginActivity.this, PhoneActivity.class);
                intent.putExtra("flag", AppConstant.RESETPASSWORD);
                startActivity(intent);
                break;
            case R.id.register:
                intent = new Intent(LoginActivity.this, PhoneActivity.class);
                intent.putExtra("flag", AppConstant.REGISTER);
                startActivity(intent);
                break;
            case R.id.login:
                phoneEditText.hideHint();
                pwdEditText.hideHint();
                tel = phoneEditText.getText();
                pwd = pwdEditText.getText();
                if (tel.length() == 0) {
                    phoneEditText.showHint();
                    phoneEditText.setHintText(getString(R.string.login_phone_empty));
                } else if (pwd.length() == 0) {
                    pwdEditText.showHint();
                    pwdEditText.setHintText(getString(R.string.login_pwd_empty));
                } else
                    login();
                break;
        }
    }

    @Override
    protected void HandleTitleBarEvent(TitleBar component, View v) {
        switch (component) {
            case LEFT:
                finish();
                break;
        }
    }


    @Override
    public void onBackPressed() {
        finish();
    }

    public void login() {
        /**
         * @param email或telephone
         * @param password
         * @param callback<userId>
         */
        accountMgr.login(tel, pwd, new PayloadCallback<ACUserInfo>() {
            @Override
            public void success(ACUserInfo userInfo) {
                Pop.popToast(LoginActivity.this, getString(R.string.login_success));
                MainApplication.getInstance().UserLogin(new UserInfo(userInfo.getUserId(), userInfo.getName()));
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();
            }

            @Override
            public void error(ACException e) {
                pwdEditText.showHint();
                LogUtil.d("TAG", "code:" + e.getErrorCode());
                if (e.getErrorCode() == ACException.INTERNET_ERROR)
                    pwdEditText.setHintText(getString(R.string.internet_error));
                else
                    pwdEditText.setHintText(e.getMessage());
            }
        });
    }
}
