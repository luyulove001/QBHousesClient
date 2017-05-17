package com.qc.qbhousesclient.ui.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qc.qbhousesclient.R;
import com.qc.qbhousesclient.api.Api;
import com.qc.qbhousesclient.ui.activity.base.BaseActivity;
import com.qc.qbhousesclient.utils.Md5Algorithm;
import com.qc.qbhousesclient.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import talex.zsw.baselibrary.util.klog.KLog;

/**
 * Created by cly on 2017/5/8.
 */

public class FindPasswordActivity extends BaseActivity {
    EditText mAccount, mCaptcha, mPassword, mRePassword;
    TextView mGetCaptcha, mLogin;
    ImageView btn_back;
    TextView mCenterText;
    private String phone;
    private String signdata;
    private String sign;
    private String mid;

    @Override
    public int getLayoutId() {
        return R.layout.activity_reset_psd;
    }

    @Override
    public void initViews() {
        mAccount = findView(R.id.login_register_count);
        mCaptcha = findView(R.id.login_register_yzm);
        mPassword = findView(R.id.login_register_pwd);
        mRePassword = findView(R.id.login_register_repwd);
        mGetCaptcha = findView(R.id.login_register_getpwd);
        mLogin = findView(R.id.login_register_complete);
        btn_back = findView(R.id.title_leftimageview);
        mCenterText = findView(R.id.title_centertextview);
    }

    @Override
    public void initListener() {
        setOnClick(mGetCaptcha);
        setOnClick(mLogin);
        setOnClick(btn_back);
    }

    @Override
    public void initData() {
        mCenterText.setText(getIntent().getStringExtra("title"));
        mid = "1";
        sign = System.currentTimeMillis() + "";
        signdata= Md5Algorithm.signMD5("mid=" + mid + "&sign=" + sign);
    }

    @Override
    public void progressClick(View view) {
        switch (view.getId()) {
            case R.id.login_register_getpwd:
                doGetIdentifyCode();
                break;
            case R.id.login_register_complete:
                doLogin();
                break;
            case R.id.title_leftimageview:
                onBackPressed();
                break;
        }
    }

    private void doLogin() {
        phone = mAccount.getText().toString();
        RequestParams params = new RequestParams(Api.baseUrl + "/api/authapi/login");
        params.addBodyParameter("account", phone);
        params.addBodyParameter("logintype", "1");
        params.addBodyParameter("password", "123456");
        params.addBodyParameter("deviceid", "111222");
        params.addBodyParameter("mid", mid);
        params.addBodyParameter("sign", sign);
        params.addBodyParameter("signdata", signdata);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject json = new JSONObject(result);
                    String msg = json.getString("msg");
                    ToastUtils.showToast(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                KLog.d("doLogin : onError");
            }

            @Override
            public void onCancelled(CancelledException cex) {
                KLog.d("doLogin : onCancelled");
            }

            @Override
            public void onFinished() {
                KLog.d("doLogin : onFinished");
            }
        });
    }

    private void doGetIdentifyCode() {
        phone = mAccount.getText().toString();
        Toast.makeText(FindPasswordActivity.this, phone, Toast.LENGTH_SHORT).show();
        RequestParams params = new RequestParams(Api.baseUrl + "/api/authapi/getIdentifyingCode");
        params.addBodyParameter("phone", phone);
        params.addBodyParameter("use", "2");
        params.addBodyParameter("mid", mid);
        params.addBodyParameter("sign", sign);
        params.addBodyParameter("signdata", signdata);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject json = new JSONObject(result);
                    String msg = json.getString("msg");
                    ToastUtils.showToast(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                KLog.d("doGetIdentifyCode : onError");
                ex.printStackTrace();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                KLog.d("doGetIdentifyCode : onCancelled");
                cex.printStackTrace();
            }

            @Override
            public void onFinished() {
                KLog.d("doGetIdentifyCode : onFinished");
            }
        });
    }

    private void doConfirmChangePwd() {
        RequestParams params = new RequestParams(Api.baseUrl + "/api/authapi/forgotpass");
        params.addBodyParameter("identifyingcode", "");
        params.addBodyParameter("phone", phone);
        params.addBodyParameter("password", "123456");
        params.addBodyParameter("password", "2");
        params.addBodyParameter("mid", mid);
        params.addBodyParameter("sign", sign);
        params.addBodyParameter("signdata", signdata);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject json = new JSONObject(result);
                    String msg = json.getString("msg");
                    ToastUtils.showToast(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
}
