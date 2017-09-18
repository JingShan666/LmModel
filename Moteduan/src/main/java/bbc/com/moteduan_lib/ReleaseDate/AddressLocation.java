package bbc.com.moteduan_lib.ReleaseDate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.google.gson.Gson;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.xutils.common.Callback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import bbc.com.moteduan_lib.R;
import bbc.com.moteduan_lib.ReleaseDate.adapter.RangelvAdapter;
import bbc.com.moteduan_lib.ReleaseDate.bean.HatAreaBean;
import bbc.com.moteduan_lib.ReleaseDate.bean.ShopAreaBean;
import bbc.com.moteduan_lib.base.BaseActivity;
import bbc.com.moteduan_lib.database.SpDataCache;
import bbc.com.moteduan_lib.log.LogDebug;

import com.liemo.shareresource.Url;

import bbc.com.moteduan_lib.network.Xutils3.Xutils;
import bbc.com.moteduan_lib.tools.TextUtils;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import wei.toolkit.utils.Loger;


public class AddressLocation extends BaseActivity implements GeocodeSearch.OnGeocodeSearchListener {
    private static final String TAG = "AddressLocation";
    private ImageButton back;
    private TextView confirm_address;
    private RelativeLayout titleLayout;
    private RelativeLayout detailLocation;
    private ListView left_lv;
    private TagFlowLayout flowlayout;
    public static final int REQUEST_LOCATION = 1000;
    public static final int RESULT_LOCATION = 2000;
    private static final int DATATIMECHOSE_REQUEST = 3000;

    private TextView address;
    private List<String> subDistrictname = new ArrayList<>();
    private RangelvAdapter lvAdapter;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    private TagAdapter tagAdapter;
    private LayoutInflater mInflater;
    private HashMap<Integer, Object> rangemap;
    private int curpoi = 0;
    private List<String> hatList;
    private List<String> shopList;
    private List<String> hatIDList;
    private String[] shopItems;
    private ArrayList<String> selectedList;
    private int tagcount;
    private String city;
    private ArrayList<String> lllist;

    private GeocodeSearch mGeocodeSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_location);
        initView();
        initData();
    }

    @SuppressLint("UseSparseArrays")
    @Override
    public void initView() {
        mGeocodeSearch = new GeocodeSearch(this);
        mGeocodeSearch.setOnGeocodeSearchListener(this);

        rangemap = new HashMap<>();

        hatList = new ArrayList<>();
        hatIDList = new ArrayList<>();
        shopList = new ArrayList<>();

        selectedList = new ArrayList<>();
        lllist = new ArrayList<>();

        mInflater = LayoutInflater.from(AddressLocation.this);
        back = (ImageButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        confirm_address = (TextView) findViewById(R.id.confirm_address);
        titleLayout = (RelativeLayout) findViewById(R.id.titleLayout);
        detailLocation = (RelativeLayout) findViewById(R.id.detailLocation);
        left_lv = (ListView) findViewById(R.id.left_lv);
        address = (TextView) findViewById(R.id.address);

        //流式标签
        flowlayout = (TagFlowLayout) findViewById(R.id.flowlayout);
        lvAdapter = new RangelvAdapter(hatList, AddressLocation.this);
        left_lv.setAdapter(lvAdapter);

        confirm_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("items", selectedList);
                intent.putExtra("lllist", lllist);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        //区
        left_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                curpoi = position;
                resetlv(position);
                requestShopData(hatIDList.get(position));
                if (selectedList.size() >= 3) {
                    toast.showText("最多选择三个商圈");
                    flowlayout.setClickable(false);
                }
            }

        });

        //详细定位
        detailLocation.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {

            }
        });


    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        city = intent.getStringExtra("city");
        Loger.log(TAG, "intent city = " + city);
        if (TextUtils.isEmpty(city)) {
            city = SpDataCache.getCity();
            Loger.log(TAG, "SpDataCache.getCity city = " + city);
        }
        requestData();
    }


    //请求区数据
    private void requestData() {
        Map<String, Object> areaMap = new HashMap<>();
        areaMap.put("city", city);
        Xutils.post(Url.hatArea, areaMap, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Loger.log(TAG, result);
                Gson gson = new Gson();
                HatAreaBean hatAreaBean = gson.fromJson(result, HatAreaBean.class);
                if ("200".equals(hatAreaBean.getCode())) {
                    List<HatAreaBean.DataBean> data = hatAreaBean.getData();
                    for (HatAreaBean.DataBean dataBean : data) {
                        hatList.add(dataBean.getAreaName());
                        hatIDList.add(dataBean.getID() + "");
                    }
                    lvAdapter.notifyDataSetChanged();
                    Loger.log(TAG, "hatIDList " + hatIDList.size() + "-" + lvAdapter.getCount());
                    if (hatIDList.size() > 0) {
                        requestShopData(hatIDList.get(0));
                        Loger.log(TAG, "hatIDList.get(0) " + hatIDList.get(0));
                    }
                    curpoi = 0;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            resetlv(0);
                        }
                    }, 500);

                } else {
                    toast.showText(hatAreaBean.getTips());
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


    //请求商圈数据
    private void requestShopData(String s) {
        Map<String, Object> areaMap = new HashMap<>();
        areaMap.put("area", s);
        Xutils.post(Url.shopArea, areaMap, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Loger.log(TAG, result);
                Gson gson = new Gson();
                ShopAreaBean shopAreaBean = gson.fromJson(result, ShopAreaBean.class);
                String data = shopAreaBean.getData();
                shopItems = data.split(",");
                refreshFlowLayou();
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


    //重置listview
    private void resetlv(int position) {
        LogDebug.err(lvAdapter.getCount() + "count");
        for (int i = 0; i < lvAdapter.getCount(); i++) {
            LogDebug.err("i" + i);
            RelativeLayout rll = (RelativeLayout) left_lv.getChildAt(i);
            if (rll != null) {
                ImageView img = (ImageView) rll.getChildAt(1);
                if (i == position) {
                    img.setVisibility(View.VISIBLE);
                } else {
                    img.setVisibility(View.GONE);
                }
            }
        }
    }


    private void refreshFlowLayou() {

        String[] values = new String[shopItems.length];

        for (int i = 0; i < shopItems.length; i++) {
            values[i] = shopItems[i];
        }
        TagAdapter<String> tagAdapter = new TagAdapter<String>(values) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) mInflater.inflate(R.layout.locationtv, flowlayout, false);
                tv.setText(s);
                return tv;
            }
        };
        tagcount = 3 - selectedList.size();
        if (tagcount == 0) {
            flowlayout.setSelected(false);
        } else {
            flowlayout.setMaxSelectCount(tagcount);
        }
        flowlayout.setAdapter(tagAdapter);
        final Set<Integer> selectPosSet = (Set<Integer>) rangemap.get(curpoi);
        if (selectPosSet != null && selectPosSet.size() > 0) {
            for (Integer selectint : selectPosSet) {
                Loger.log(TAG, selectint + "==========");
                tagAdapter.setSelectedList(selectint);
            }
        } else {
            Loger.log(TAG, "selectPosSet 值为空");
        }


        flowlayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(final View view, final int position, FlowLayout parent) {
                Observable.create(new ObservableOnSubscribe<Object>() {
                    @Override
                    public void subscribe(ObservableEmitter<Object> o) throws Exception {
                        synchronized (selectedList) {
                            int index = selectedList.indexOf(shopItems[position]);
                            if (index > -1) {
                                if (selectedList.size() == lllist.size()) {
                                    lllist.remove(index);
                                }
                                selectedList.remove(index);
                            } else {
                                if (selectedList.size() < 3) {
                                    //根据地理位置定位，先城市，后地点名
                                    GeocodeQuery query = new GeocodeQuery(city + shopItems[position], "");
//                        mGeocodeSearch.getFromLocationNameAsyn(query);
                                    try {
                                        List geocodeResults = mGeocodeSearch.getFromLocationName(query);
                                        if (geocodeResults == null || geocodeResults.size() < 1) {
                                            o.onNext("未找到此地点‘经纬度’请重新选择");
                                            return;
                                        }
                                        GeocodeAddress address = (GeocodeAddress) geocodeResults.get(0);
                                        double latitude = address.getLatLonPoint().getLatitude();
                                        double longitude = address.getLatLonPoint().getLongitude();
                                        String ll = latitude + "," + longitude;
                                        lllist.add(ll);
                                        selectedList.add(shopItems[position]);
                                        Loger.log(TAG, " 同步 AddressLocation = " + shopItems[position] + " " + ll);
                                    } catch (AMapException e) {
                                        e.printStackTrace();
                                        Loger.log(TAG, e.getErrorMessage());
                                        o.onNext("网络拥堵,请重新选择");
                                    }
                                } else {
                                    o.onNext("最多选择三个商圈");
                                }
                            }
                        }
                    }
                }).subscribeOn(Schedulers.io()).
                        observeOn(AndroidSchedulers.mainThread()).
                        subscribe(new Observer<Object>() {

                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(Object value) {
                                toast.showText(value.toString());

                            }

                            @Override
                            public void onError(Throwable e) {
                            }

                            @Override
                            public void onComplete() {

                            }
                        });

                return true;


            }
        });
        //点击标签时的回调
        flowlayout.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                Loger.log(TAG, "choose:" + selectPosSet.toString());
                rangemap.put(curpoi, selectPosSet);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, android.content.Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (REQUEST_LOCATION == requestCode && RESULT_LOCATION == resultCode) {
            String poi = data.getStringExtra("poi");
            if (poi != null) {
                address.setText(poi);
            }
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
        if (geocodeResult != null && geocodeResult.getGeocodeAddressList() != null
                && geocodeResult.getGeocodeAddressList().size() > 0) {
            GeocodeAddress address = geocodeResult.getGeocodeAddressList().get(0);
            double latitude = address.getLatLonPoint().getLatitude();
            double longitude = address.getLatLonPoint().getLongitude();
            String ll = latitude + "," + longitude;
            lllist.add(ll);
            Loger.log(TAG, "异步 AddressLocation = " + ll);
        }
    }
}
