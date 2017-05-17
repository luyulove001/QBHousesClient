package com.qc.qbhousesclient.ui.activity.store;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qc.qbhousesclient.R;
import com.qc.qbhousesclient.ui.activity.base.BaseActivity;
import com.qc.qbhousesclient.ui.view.CircleImageView;


public class MainActivity extends BaseActivity {

    CircleImageView mIcon;
    TextView mNickname, mPhone;
    LinearLayout mRecharge, mUnusedTicket, mUsedTicket;
    RelativeLayout mTicketDetail;
    TextView mCenterText;
    ImageView mMenu, mCloseMenu;
    private DrawerLayout mDrawerLayout;
    private ListView lvLeftMenu;

    @Override
    public int getLayoutId() {
        return R.layout.activity_store_main;
    }

    @Override
    public void initViews() {
        mIcon = findView(R.id.icon);
        mNickname = findView(R.id.nickname);
        mPhone = findView(R.id.phone_number);
        mRecharge = findView(R.id.recharge);
        mUnusedTicket = findView(R.id.ticket_unused);
        mUsedTicket = findView(R.id.ticket_used);
        mTicketDetail = findView(R.id.ticket_detail);

        mMenu = findView(R.id.title_leftimageview);
        mCloseMenu = findView(R.id.close_menu);
        mMenu.setImageResource(R.drawable.qb_side_menu);
        mCenterText = findView(R.id.title_centertextview);

        mDrawerLayout = findView(R.id.dl_left);
        lvLeftMenu = findView(R.id.lv_left_menu);
    }

    @Override
    public void initListener() {
        setOnClick(mIcon);
        setOnClick(mRecharge);
        setOnClick(mUnusedTicket);
        setOnClick(mUsedTicket);
        setOnClick(mTicketDetail);
        setOnClick(mCloseMenu);
        setOnClick(mMenu);
    }

    @Override
    public void initData() {
        final String[] lvs = getResources().getStringArray(R.array.store_menu);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, lvs);
        lvLeftMenu.setAdapter(arrayAdapter);
        lvLeftMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent();
                i.putExtra("title", lvs[position]);
                switch (position) {
                    case 0:
                        mDrawerLayout.closeDrawer(Gravity.START);
                        break;
                    case 1:
                        i.setClass(MainActivity.this, TicketListActivity.class);
                        startActivity(i);
                        break;
                    case 2:
                        i.setClass(MainActivity.this, RechargeActivity.class);
                        startActivity(i);
                        break;
                    case 3:
                        i.setClass(MainActivity.this, PersonalActivity.class);
                        startActivity(i);
                        break;
                }
            }
        });
        mCenterText.setText("首页");
    }

    @Override
    public void progressClick(View view) {
        Intent i = new Intent();
        switch (view.getId()) {
            case R.id.icon:
                i.setClass(this, PersonalActivity.class);
                startActivity(i);
                break;
            case R.id.recharge:
                i.setClass(this, RechargeActivity.class);
                startActivity(i);
                break;
            case R.id.ticket_unused:
                i.setClass(this, TicketListActivity.class);
                startActivity(i);
                break;
            case R.id.ticket_used:
                i.setClass(this, TicketListActivity.class);
                startActivity(i);
                break;
            case R.id.ticket_detail:
                i.setClass(this, TicketListActivity.class);
                startActivity(i);
                break;
            case R.id.close_menu:
                mDrawerLayout.closeDrawer(Gravity.START);
                break;
            case R.id.title_leftimageview:
                if (!mDrawerLayout.isDrawerOpen(Gravity.START)) {
                    mDrawerLayout.openDrawer(Gravity.START);
                }
                break;
            default:
                break;
        }
    }

}
