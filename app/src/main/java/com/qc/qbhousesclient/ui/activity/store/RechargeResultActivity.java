package com.qc.qbhousesclient.ui.activity.store;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qc.qbhousesclient.R;
import com.qc.qbhousesclient.ui.activity.base.BaseActivity;

/**
 * Created by cly on 2017/5/9.
 */

public class RechargeResultActivity extends BaseActivity {
    ImageView btn_back;
    TextView mCenterText;

    @Override
    public int getLayoutId() {
        return R.layout.activity_recharge_result;
    }

    @Override
    public void initViews() {
        btn_back = findView(R.id.title_leftimageview);
        mCenterText = findView(R.id.title_centertextview);
    }

    @Override
    public void initListener() {
        setOnClick(btn_back);
    }

    @Override
    public void initData() {
        mCenterText.setText(getIntent().getStringExtra("title"));
    }

    @Override
    public void progressClick(View view) {
        switch (view.getId()) {
            case R.id.title_leftimageview:
                onBackPressed();
                break;
        }
    }
}
