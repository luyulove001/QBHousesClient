package com.qc.qbhousesclient.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.qc.qbhousesclient.R;
import com.qc.qbhousesclient.utils.Constant;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "WXPayEntryActivity";

    private IWXAPI api;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);

        api = WXAPIFactory.createWXAPI(this, Constant.WX_APPID);
        api.handleIntent(getIntent(), this);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }


    @Override
    public void onReq(BaseReq req) {
    }


    @Override
    public void onResp(BaseResp resp) {
        int errCode = resp.errCode;
        Log.i("wxpay", "onPayFinish, errCode = " + errCode + " resp.errStr:" + resp.errStr + " resp.openId:"
                + resp.openId + " resp.transaction:" + resp.transaction);
        if (errCode == 0) {
            //0 成功 展示成功页面
            Toast.makeText(getApplicationContext(), "支付成功", Toast.LENGTH_SHORT).show();
            //            Intent intent = new Intent(Constant.ACTION_NAME);
            //            sendBroadcast(intent);
            finish();
        } else if (errCode == -1) {
            //-1 错误 可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
            Toast.makeText(getApplicationContext(), "支付失败", Toast.LENGTH_SHORT).show();
            finish();
        } else if (errCode == -2) {
            //-2 用户取消 无需处理。发生场景：用户不支付了，点击取消，返回APP。
            Toast.makeText(getApplicationContext(), "取消支付", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}