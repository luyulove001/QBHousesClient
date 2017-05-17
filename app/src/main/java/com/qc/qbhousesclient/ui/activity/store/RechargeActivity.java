package com.qc.qbhousesclient.ui.activity.store;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.qc.qbhousesclient.R;
import com.qc.qbhousesclient.ui.activity.base.BaseActivity;
import com.qc.qbhousesclient.utils.AuthResult;
import com.qc.qbhousesclient.utils.Constant;
import com.qc.qbhousesclient.utils.MD5;
import com.qc.qbhousesclient.utils.PayResult;
import com.qc.qbhousesclient.utils.Util;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by cly on 2017/5/9.
 */

public class RechargeActivity extends BaseActivity implements IWXAPIEventHandler {
    RelativeLayout mBuyOne, mBuyTwenty, mBuyThirty, mBuyForty, mBuyFifty, mBuySixty;
    TextView mOneNum, mTwentyNum, mThirtyNum, mFortyNum, mFiftyNum, mSixtyNum, mOnePrice, mTwentyPrice, mThirtyPrice, mFortyPrice, mFiftyPrice, mSixtyPrice, mOtherPrice;
    LinearLayout mRecharge;
    ImageView mBack;
    TextView mCenterText;
    private IWXAPI api;

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(RechargeActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RechargeActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        Toast.makeText(RechargeActivity.this, "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RechargeActivity.this, "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };


    @Override
    public int getLayoutId() {
        return R.layout.activity_recharge;
    }

    @Override
    public void initViews() {
        mBuyOne = findView(R.id.lyt_buy_one);
        mBuyTwenty = findView(R.id.lyt_buy_twenty);
        mBuyThirty = findView(R.id.lyt_buy_thirty);
        mBuyForty = findView(R.id.lyt_buy_forty);
        mBuyFifty = findView(R.id.lyt_buy_fifty);
        mBuySixty = findView(R.id.lyt_buy_sixty);
        mOneNum = findView(R.id.buy_one_number);
        mOnePrice = findView(R.id.buy_one_price);
        mTwentyNum = findView(R.id.buy_twenty_number);
        mTwentyPrice = findView(R.id.buy_twenty_price);
        mThirtyNum = findView(R.id.buy_thirty_number);
        mThirtyPrice = findView(R.id.buy_thirty_price);
        mFortyNum = findView(R.id.buy_forty_number);
        mFortyPrice = findView(R.id.buy_forty_price);
        mFiftyNum = findView(R.id.buy_fifty_number);
        mFiftyPrice = findView(R.id.buy_fifty_price);
        mSixtyNum = findView(R.id.buy_sixty_number);
        mSixtyPrice = findView(R.id.buy_sixty_price);
        mRecharge = findView(R.id.lyt_recharge);
        mBack = findView(R.id.title_leftimageview);
        mCenterText = findView(R.id.title_centertextview);
    }

    @Override
    public void initListener() {
        setOnClick(mBuyOne);
        setOnClick(mBuyTwenty);
        setOnClick(mBuyThirty);
        setOnClick(mBuyForty);
        setOnClick(mBuyFifty);
        setOnClick(mBuySixty);
        setOnClick(mRecharge);
        setOnClick(mBack);
    }

    @Override
    public void initData() {
        api = WXAPIFactory.createWXAPI(this, Constant.WX_APPID);
        api.registerApp(Constant.WX_APPID);
        mCenterText.setText(getIntent().getStringExtra("title"));
    }

    @Override
    public void progressClick(View view) {
        switch (view.getId()) {
            case R.id.lyt_buy_one:
                show("1");
                Runnable alipayRunnable = new Runnable() {
                    @Override
                    public void run() {
                        String url = "http://106.15.61.209:7880/centor/api/kmapi/createRechargeOrder?token=1234567890&shopid=2&quantity=10&price=8&amount=80&paytype=1&systemtype=1";
                        try {
                            byte[] buf = Util.httpGet(url);
                            if (buf != null && buf.length > 0) {
                                String content = new String(buf);
                                Log.e("get server pay params:", content);
                                JSONObject json = new JSONObject(content);
                                json = json.getJSONObject("data");
                                if (null != json) {
                                    alipay(json.getString("orderstring"));
                                }
                            } else {
                                Log.d("PAY_GET", "服务器请求错误");
                            }
                        } catch (Exception e) {
                            Log.e("PAY_GET", "异常：" + e.getMessage());
                        }
                    }
                };
                new Thread(alipayRunnable).start();
                break;
            case R.id.lyt_buy_twenty:
                //                show("20");
                boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
                Toast.makeText(RechargeActivity.this, String.valueOf(isPaySupported), Toast.LENGTH_SHORT).show();
                break;
            case R.id.lyt_buy_thirty:
                show("30");
                break;
            case R.id.lyt_buy_forty:
                show("40");
                break;
            case R.id.lyt_buy_fifty:
                show("50");
                break;
            case R.id.lyt_buy_sixty:
                show("60");
                break;
            case R.id.lyt_recharge:
                //                Intent i = new Intent(this, PersonalActivity.class);
                //                startActivity(i);
                mRecharge.setEnabled(false);
                Toast.makeText(RechargeActivity.this, "获取订单中...", Toast.LENGTH_SHORT).show();
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        String url = "http://106.15.61.209:7880/centor/api/kmapi/createRechargeOrder?token=1234567890&shopid=2&quantity=10&price=8&amount=80&paytype=2&systemtype=2";
                        try {
                            byte[] buf = Util.httpGet(url);
                            if (buf != null && buf.length > 0) {
                                String content = new String(buf);
                                Log.e("get server pay params:", content);
                                JSONObject json = new JSONObject(content);
                                json = json.getJSONObject("data");
                                if (null != json && !json.has("retcode")) {
                                    PayReq req = new PayReq();
                                    // req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
                                    req.appId = json.getString("appid");
                                    req.partnerId = json.getString("partnerid");
                                    req.prepayId = json.getString("prepayid");
                                    req.nonceStr = json.getString("noncestr");
                                    req.timeStamp = json.getString("timestamp");
                                    req.packageValue = json.getString("package");
                                    List<NameValuePair> signParams = new LinkedList<NameValuePair>();
                                    signParams.add(new BasicNameValuePair("appid", req.appId));
                                    signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
                                    signParams.add(new BasicNameValuePair("package", req.packageValue));
                                    signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
                                    signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
                                    signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
                                    //req.sign = genAppSign(signParams);
                                    req.sign = json.getString("sign");
                                    req.extData = "app data"; // optional
                                    //Toast.makeText(RechargeActivity.this, "正常调起支付", Toast.LENGTH_SHORT).show();
                                    // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                                    api.registerApp(Constant.WX_APPID);
                                    api.sendReq(req);
                                } else {
                                    Log.d("PAY_GET", "返回错误" + json.getString("retmsg"));
                                    //Toast.makeText(RechargeActivity.this, "返回错误" + json.getString("retmsg"), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Log.d("PAY_GET", "服务器请求错误");
                                //Toast.makeText(RechargeActivity.this, "服务器请求错误", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.e("PAY_GET", "异常：" + e.getMessage());
                            //Toast.makeText(RechargeActivity.this, "异常：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                new Thread(r).start();
                mRecharge.setEnabled(true);
                break;
            case R.id.lyt_other:
                show("其他");
                break;
            case R.id.title_leftimageview:
                onBackPressed();
                break;
        }

    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    private String genAppSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append("");

        String appSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        Log.e("orion", appSign);
        return appSign;
    }

    @Override
    public void onResp(BaseResp resp) {
        int result = 0;

        Toast.makeText(this, "baseresp.getType = " + resp.getType(), Toast.LENGTH_SHORT).show();

        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = R.string.errcode_success;
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = R.string.errcode_cancel;
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = R.string.errcode_deny;
                break;
            case BaseResp.ErrCode.ERR_UNSUPPORT:
                result = R.string.errcode_unsupported;
                break;
            default:
                result = R.string.errcode_unknown;
                break;
        }

        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
    }

    private void show(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    private void alipay(String info) {
        final String orderInfo = info;// 订单信息
        Log.d("alipay", info);
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(RechargeActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }
}
