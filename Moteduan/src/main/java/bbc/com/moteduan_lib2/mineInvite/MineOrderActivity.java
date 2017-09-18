package bbc.com.moteduan_lib2.mineInvite;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bbc.com.moteduan_lib.R;
import bbc.com.moteduan_lib.base.BaseActivity;
import wei.toolkit.widget.VViewPager;

/**
 * Created by Administrator on 2017/6/4 0004.
 */

public class MineOrderActivity extends BaseActivity {
    private ImageView back;
    private TextView title;
    private TabLayout tabLayout;
    private VViewPager viewPager;
    private List<Fragment> fragmentList = new ArrayList<>();
    private String[] titles = {"我的报名","实时订单","预约订单"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_order);
        initView();
        initData();
        initEvents();
    }

    @Override
    public void initView() {
        back = (ImageView) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);
        tabLayout = (TabLayout) findViewById(R.id.activity_mine_order_tab);
        viewPager = (VViewPager) findViewById(R.id.activity_mine_order_vp);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    @Override
    public void initData() {
        title.setText("我的订单");
        fragmentList.add(new ApplyOrderListFragment());
        fragmentList.add(new RealtimeOrderListFragment());
        fragmentList.add(new AppointmentOrderListFragment());
        viewPager.setOffscreenPageLimit(fragmentList.size());
        viewPager.setAdapter(new VpAdapter(getSupportFragmentManager()));
    }

    @Override
    public void initEvents() {
        super.initEvents();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private class VpAdapter extends FragmentPagerAdapter{

        public VpAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}
