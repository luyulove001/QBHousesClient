package com.qc.qbhousesclient.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.qc.qbhousesclient.R;
import com.qc.qbhousesclient.api.Api;
import com.qc.qbhousesclient.model.bean.UserBean;
import com.qc.qbhousesclient.ui.activity.base.BaseActivity;
import com.qc.qbhousesclient.ui.activity.store.MainActivity;
import com.qc.qbhousesclient.utils.Md5Algorithm;
import com.qc.qbhousesclient.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import talex.zsw.baselibrary.util.klog.KLog;


/**
 * Created by cly on 2017/5/5.
 */
public class LoginActivity extends BaseActivity {
    TextView mResetPwd;

    EditText mPhone;

    EditText mPassword;

    ImageView mClearPhone, mClearPwd;

    ImageView mLoginIcon;

    Button mLogin;
    private String phone;
    private String password;
    private String signdata;
    private String sign;
    private String mid;
    private boolean prompt = false;
    private boolean checkUpResult = true;
    private ProgressDialog mDialog;

    TextWatcher mTextWatcher = new TextWatcher() {
        private CharSequence temp;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            temp = s;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (temp.length() > 0) {
                mClearPwd.setVisibility(View.VISIBLE);
                mClearPhone.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initViews() {
        mResetPwd = findView(R.id.tv_reset_pwd);
        mPhone = findView(R.id.et_phone);
        mPassword = findView(R.id.et_pwd);
        mClearPhone = findView(R.id.iv_phone_clear);
        mClearPwd = findView(R.id.iv_pwd_clear);
        mLogin = findView(R.id.btn_login);
        mLoginIcon = findView(R.id.login_icon);
        mDialog = new ProgressDialog(this);
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDialog.setMessage("请稍等");
        mDialog.setIndeterminate(false);
        // 设置ProgressDialog 是否可以按退回按键取消
        mDialog.setCancelable(false);
    }

    @Override
    public void initListener() {
        setOnClick(mLogin);
        setOnClick(mClearPhone);
        setOnClick(mClearPwd);
        setOnClick(mResetPwd);
        setOnClick(mLoginIcon);
        mPhone.addTextChangedListener(mTextWatcher);
        mPassword.addTextChangedListener(mTextWatcher);
    }

    @Override
    public void initData() {
        mid = "1";
        sign = System.currentTimeMillis() + "";
        signdata= Md5Algorithm.signMD5("mid=" + mid + "&sign=" + sign);
    }

    @Override
    public void progressClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                prompt = true;
                if (checkUpResult == false) {
                    checkUpResult = true;
                }
                getData();
                break;
            case R.id.iv_pwd_clear:
                mPassword.setText("");
                mPassword.setHint(R.string.hint_pwd);
                break;
            case R.id.iv_phone_clear:
                mPhone.setText("");
                mPhone.setHint(R.string.hint_account);
                break;
            case R.id.tv_reset_pwd:
                Intent intent_forgot_password = new Intent(this, FindPasswordActivity.class);
                startActivity(intent_forgot_password);
                Toast.makeText(this, "test", Toast.LENGTH_SHORT).show();
                break;
            case R.id.login_icon:
                Intent i = new Intent(this, com.qc.qbhousesclient.ui.activity.servicer.MainActivity.class);
                startActivity(i);
                break;
        }
    }

    private void getData() {
        phone = mPhone.getText().toString().trim();
        password = mPassword.getText().toString().trim();

        if (phone.equals("") && prompt) {
            Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
            checkUpResult = false;
            prompt = false;
        }

        if (!checkPhoneNumber(phone) && prompt) {
            Toast.makeText(this, "手机号格式不正确", Toast.LENGTH_SHORT).show();
            checkUpResult = false;
            prompt = false;
        }


        if (password.equals("") && prompt) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            checkUpResult = false;
            prompt = false;
        }

        doLogin();
    }

    private void doLogin() {
        if (checkUpResult) {
            mDialog.show();
            phone = mPhone.getText().toString();
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
                        String code = json.getString("code");
                        if (code.equals("100000")) {
                            json = json.getJSONObject("data");
                            String token = json.getString("token");
                            doGetUserInfo(token);
                        } else {
                            ToastUtils.showToast(json.getString("msg"));
                            mDialog.hide();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    KLog.d("doLogin : onError");
                    mDialog.hide();
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
    }

    private void doGetUserInfo(final String token) {
        RequestParams params = new RequestParams(Api.baseUrl + "/api/kmapi/userinfo");
        params.addBodyParameter("token", token);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject json = new JSONObject(result);
                    String code = json.getString("code");
                    if (code.equals("100000")) {
                        String data = json.getString("data");
                        Gson gson = new Gson();
                        UserBean user = gson.fromJson(data, UserBean.class);
                        KLog.d(user.toString());
                        mDialog.dismiss();
                        Intent i = new Intent();
                        i.putExtra("token", token);
                        if (user.getRole().equals("3")) { //3为门店，1/2为投资商，服务商
                            i.setClass(LoginActivity.this, MainActivity.class);
                        } else {
                            i.setClass(LoginActivity.this, com.qc.qbhousesclient.ui.activity.servicer.MainActivity.class);
                        }
                        startActivity(i);
                    } else {
                        ToastUtils.showToast(json.getString("msg"));
                        mDialog.hide();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                KLog.d("doGetUserInfo : onError");
                mDialog.hide();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    public static boolean checkPhoneNumber(String mobiles) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(mobiles);
        b = m.matches();
        return b;
    }
}
