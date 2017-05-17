package com.qc.qbhousesclient.ui.activity.store;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.qc.qbhousesclient.R;
import com.qc.qbhousesclient.ui.activity.LoginActivity;
import com.qc.qbhousesclient.ui.activity.base.BaseActivity;
import com.qc.qbhousesclient.ui.view.CircleImageView;

/**
 * Created by cly on 2017/5/8.
 */

public class PersonalActivity extends BaseActivity {
    CircleImageView mIcon;
    TextView mNickname, mPhone, mExit;
    ImageView mBack;
    TextView mCenterText;


    @Override
    public int getLayoutId() {
        return R.layout.activity_store_personal;
    }

    @Override
    public void initViews() {
        mIcon = findView(R.id.icon);
        mNickname = findView(R.id.nickname);
        mPhone = findView(R.id.phone_number);
        mExit = findView(R.id.exit);
        mBack = findView(R.id.title_leftimageview);
        mCenterText = findView(R.id.title_centertextview);
    }

    @Override
    public void initListener() {
        setOnClick(mIcon);
        setOnClick(mExit);
        setOnClick(mBack);
    }

    @Override
    public void initData() {
        mCenterText.setText(getIntent().getStringExtra("title"));
    }

    @Override
    public void progressClick(View view) {
        switch (view.getId()) {
            case R.id.icon:
                Toast.makeText(this, "更换头像", Toast.LENGTH_SHORT).show();
                break;
            case R.id.exit:
                Intent i = new Intent(this, LoginActivity.class);
                startActivity(i);
                break;
            case R.id.title_leftimageview:
                onBackPressed();
                break;
        }
    }
}
