package wei.moments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;

import java.util.ArrayList;
import java.util.List;

import wei.moments.base.BaseActivity;
import wei.moments.bean.PoiItemBean;
import wei.moments.database.SPUtils;
import wei.toolkit.utils.Loger;

/**
 * Created by Administrator on 2017/4/28 0028.
 */

public class PoiActivity extends BaseActivity implements PoiSearch.OnPoiSearchListener {
    private ImageView mBack;
    private TextView mTitle;
    private TextView mSure;
    private TextView mSearchText;
    private TextView mPositionNone;
    private RecyclerView mRv;
    private PoiSearch.Query mPoiQuery;
    private PoiSearch mPoiSearch;
    private String mSearchType = "汽车服务|汽车销售|餐饮服务|购物服务|生活服务|体育休闲服务|医疗保健服务|" +
            "住宿服务|风景名胜|商务住宅|政府机构及社会团体|科教文化服务|交通设施服务|" +
            "金融保险服务|道路附属设施|地名地址信息";
    private List<PoiItemBean> mPoiItems;
    private int mPageNumber = 0;
    private PoiItemBean mPoiItemBean;

    private static final int REQUEST_CODE_SEARCH = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lm_activitu_poi);
        initViews();
        initEvents();
    }


    private void initViews() {
        mPoiItems = new ArrayList<>();
        mBack = (ImageView) findViewById(R.id.bar_action_back);
        mTitle = (TextView) findViewById(R.id.bar_action_title);
        mSure = (TextView) findViewById(R.id.bar_action_sure);
        mSearchText = (TextView) findViewById(R.id.activity_poi_search_text);
        mPositionNone = (TextView) findViewById(R.id.activity_poi_none);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRv = (RecyclerView) findViewById(R.id.activity_poi_rv);
        mRv.setLayoutManager(manager);
        mRv.setAdapter(new PoiAdapter());
        mRv.addOnScrollListener(scrollListener);
        mTitle.setText("所在位置");
        mSure.setVisibility(View.VISIBLE);
        mSure.setText("确定");
        loadData(mPageNumber);

    }

    private void initEvents() {
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedAddress = "";
                if (mPoiItemBean != null) {
                    selectedAddress = mPoiItemBean.getTitle();
                }
                Intent intent = new Intent();
                intent.putExtra("data", selectedAddress);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        mPositionNone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.isSelected()) {
                    return;
                }
                mPositionNone.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.lm_btn_select_on, 0);
                mPositionNone.setSelected(true);
                if (mPoiItemBean != null) {
                    mPoiItemBean.setSelected(false);
                    mRv.getAdapter().notifyDataSetChanged();
                    mPoiItemBean = null;
                }
            }
        });

        mSearchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(PoiActivity.this, PoiSearchActivity.class), REQUEST_CODE_SEARCH);
            }
        });

        mPositionNone.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.lm_btn_select_on, 0);
        mPositionNone.setSelected(true);
    }


    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (manager.findLastCompletelyVisibleItemPosition() == manager.getItemCount() - 1) {
                    ++mPageNumber;
                    loadData(mPageNumber);
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    };


    private void loadData(int pageNumber) {
        if (mPoiQuery == null) {
            mPoiQuery = new PoiSearch.Query("", mSearchType, SPUtils.getCity());
            mPoiQuery.setPageSize(20);
        }
        if (mPoiSearch == null) {
            mPoiSearch = new PoiSearch(this, mPoiQuery);
            mPoiSearch.setOnPoiSearchListener(this);
            double lat = SPUtils.getLatitude(this);
            double lon = SPUtils.getLongitude(this);
            if (lat > 0 && lon > 0) {
                mPoiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(lat, lon), 1000));
            }
        }
        mPoiQuery.setPageNum(pageNumber);
        mPoiSearch.setQuery(mPoiQuery);
        mPoiSearch.searchPOIAsyn();

    }

    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        List<PoiItem> plist = poiResult.getPois();
        if (plist == null) {
            return;
        }
        Loger.log(getClass().getSimpleName(), plist.size() + "");
        for (PoiItem item : plist) {
            PoiItemBean bean = new PoiItemBean();
            bean.setAdName(item.getAdName());
            bean.setAdCode(item.getAdCode());
            bean.setCityName(item.getCityName());
            bean.setCityCode(item.getCityCode());
            bean.setBusinessArea(item.getBusinessArea());
            bean.setSnippet(item.getSnippet());
            bean.setTitle(item.getTitle());
            Loger.log(getClass().getSimpleName(), item.getProvinceName() + "_" + item.getCityName() + "_" + item.getAdName() + "_" + item.getSnippet() + "_" + item.getTitle());
            mPoiItems.add(bean);
        }
        mRv.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {
    }

    private class PoiAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lm_item_poi, parent, false);
            return new PoiHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            PoiHolder h = (PoiHolder) holder;
            final PoiItemBean bean = mPoiItems.get(position);
            h.mCheck.setText(bean.getTitle());
            if (bean.isSelected()) {
                h.mCheck.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.lm_btn_select_on, 0);
                if (mPositionNone.isSelected()) {
                    mPositionNone.setSelected(false);
                    mPositionNone.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                }
            } else {
                h.mCheck.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }
            h.mCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!bean.isSelected()) {
                        if (mPoiItemBean != null) {
                            mPoiItemBean.setSelected(false);
                        }
                        bean.setSelected(true);
                        mPoiItemBean = bean;
                        notifyDataSetChanged();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mPoiItems.size()
                    ;
        }
    }

    private class PoiHolder extends RecyclerView.ViewHolder {
        public TextView mCheck;

        public PoiHolder(View itemView) {
            super(itemView);
            mCheck = (TextView) itemView.findViewById(R.id.lm_item_poi_address);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_SEARCH) {
            if (data != null) {
                String result = data.getStringExtra("data");
                Intent intent = new Intent();
                intent.putExtra("data", result);
                setResult(RESULT_OK, intent);
                finish();
            }
        }

    }
}
