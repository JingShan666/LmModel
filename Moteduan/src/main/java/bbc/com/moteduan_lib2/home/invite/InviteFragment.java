package bbc.com.moteduan_lib2.home.invite;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bbc.com.moteduan_lib.R;
import bbc.com.moteduan_lib.database.SpDataCache;
import bbc.com.moteduan_lib.network.Req;

import com.liemo.shareresource.Url;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import bbc.com.moteduan_lib2.OpenCityActivity;
import bbc.com.moteduan_lib2.bean.InviteNavigationBean;
import wei.toolkit.bean.EventBusMessages;
import wei.toolkit.utils.Loger;
import wei.toolkit.utils.ToastUtil;

/**
 * Created by Administrator on 2017/5/18 0018.
 * 首页点击后进入的页面
 */

public class InviteFragment extends Fragment {
    private static final String TAG = "InviteFragment";
    private SlidingTabLayout mTab;
    private ViewPager mVp;
    private List<Fragment> mFragments = new ArrayList<>();
    private boolean isInitData;
    private TextView locationText;
    private static final int REQUEST_CODE_OPEN_CITY = 1;
    private ImageView back;
    private TextView title;

    public InviteFragment(int dataType, String title, String url) {
        Bundle b = new Bundle();
        b.putInt("dataType", dataType);
        b.putString("title", title == null ? "" : title);
        b.putString("url", url == null ? Url.inviteNavigationTab : url);
        setArguments(b);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invite, container, false);
        EventBus.getDefault().register(this);
        mTab = (SlidingTabLayout) view.findViewById(R.id.fragment_invite_tab);
        mVp = (ViewPager) view.findViewById(R.id.fragment_invite_vp);
        locationText = (TextView) view.findViewById(R.id.fragment_invite_location_text);
        back = (ImageView) view.findViewById(R.id.fragment_invite_back);
        title = (TextView) view.findViewById(R.id.fragment_invite_title);
        title.setText(getArguments() == null ? "" : getArguments().getString("title"));
        initEvents();
        initDatas();
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        setOpenCityText(SpDataCache.getCity());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser) {
//            if (!isInitData) {
//                initDatas();
//            }
//        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void setOpenCityText(String text) {
        if (TextUtils.isEmpty(text)) return;
        if (locationText != null) {
            locationText.setText(SpDataCache.getCity());
        }
    }

    @Subscribe
    public void onEventBusMainThread(EventBusMessages.GpsChangeNotification gpsChangeNotification) {
        if (gpsChangeNotification != null) {
            Loger.log(TAG, "onEventBusMainThread city = " + gpsChangeNotification.getCity() + " latitude = " + gpsChangeNotification.getLatitude() + " longitude = " + gpsChangeNotification.getLongitude());
            setOpenCityText(gpsChangeNotification.getCity());
        } else {
            setOpenCityText(SpDataCache.getCity());
        }
    }

    void initEvents() {
        locationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), OpenCityActivity.class), REQUEST_CODE_OPEN_CITY);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_OPEN_CITY) {

            if (data == null) return;
            final String city = data.getStringExtra("city");
            final double latitude = data.getDoubleExtra("latitude", 0.00d);
            final double longitude = data.getDoubleExtra("longitude", 0.00d);
            setOpenCityText(city);
            SpDataCache.saveAddress(getContext(), city, city, latitude, longitude);
//            LoginBean loginBean = SpDataCache.getSelfInfo(App.getApp());
//            if (loginBean != null) {
//                UploadGPS.request(loginBean.getData().getM_id(), latitude, longitude, city, new Callback.CommonCallback<String>() {
//                    @Override
//                    public void onSuccess(String result) {
//                        try {
//                            JSONObject jsonObject = new JSONObject(result);
//                            if (jsonObject.has("code")) {
//                                String code = jsonObject.getString("code");
//                                if (!"200".equals(code)) {
//                                    String tips = "网络不畅通,城市设置失败";
//                                    if (jsonObject.has("tips")) {
//                                        tips = jsonObject.getString("tips");
//                                    }
////                                    ToastUtil.show(getContext(), tips);
//                                    return;
//                                }
//                            }
//                        } catch (JSONException e) {
//                            Loger.log(TAG, e.getMessage());
////                            ToastUtil.show(getContext(), "网络不畅通,城市设置失败");
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable ex, boolean isOnCallback) {
//                        Loger.log(TAG, ex.getMessage());
////                        ToastUtil.show(getContext(), "网络不畅通,城市设置失败");
//                    }
//
//                    @Override
//                    public void onCancelled(CancelledException cex) {
//                        Loger.log(TAG, cex.getMessage());
////                        ToastUtil.show(getContext(), "网络不畅通,城市设置失败");
//                    }
//
//                    @Override
//                    public void onFinished() {
//
//                    }
//                });
//            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initDatas() {

        mFragments = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        Req.post(getArguments() == null ? "" :getArguments().getString("url"), map, new Req.ReqCallback() {
            @Override
            public void success(String result) {
                Loger.log(TAG,result);
                InviteNavigationBean bean = new Gson().fromJson(result, InviteNavigationBean.class);
                if (!"200".equals(bean.getCode())) {
                    ToastUtil.show(getContext(), bean.getTips());
                    return;
                }
                List<InviteNavigationBean.NavigationBean> list = bean.getNavigation();
                if (list == null || list.size() < 1) {
                    failed("数据为空");
                    return;
                }
                isInitData = true;
                mFragments.clear();
                String url = "";
                int dataType = getArguments() == null ? -1 : getArguments().getInt("dataType",-1);
                if(dataType == 1){
                    //娱乐1
                    url = Url.Invite.play;
                }else if(dataType == 2){
                    //通告2
                    url = Url.Invite.notice;
                }else if(dataType == 3){
                    //商务3
                    url = Url.Invite.business;
                }
                List<String> navigaTitles = new ArrayList<>();
                for (InviteNavigationBean.NavigationBean b : list) {
                    mFragments.add(new InviteListFragment(b.getSmall_of_navigation(), dataType,url));
                    navigaTitles.add(b.getSmall_navigation());
                }
                String[] ts = navigaTitles.toArray(new String[]{});
                mTab.setViewPager(mVp, ts, getActivity(), (ArrayList<Fragment>) mFragments);
            }

            @Override
            public void failed(String msg) {
                //ToastUtil.show(getContext(), msg+"999");
                ToastUtil.show(getContext(), "网络连接不可用，请稍后重试");
            }

            @Override
            public void completed() {

            }
        });
    }

    private class VpAdapter extends FragmentPagerAdapter {

        public VpAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }
}
