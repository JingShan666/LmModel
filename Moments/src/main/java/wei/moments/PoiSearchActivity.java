package wei.moments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import wei.toolkit.utils.SoftInputUtils;
import wei.toolkit.utils.ToastUtil;

/**
 * Created by Administrator on 2017/5/2 0002.
 */

public class PoiSearchActivity extends BaseActivity implements PoiSearch.OnPoiSearchListener {
    private ImageView mBack;
    private EditText mSearchContent;
    private TextView mSearch;
    private RecyclerView mRv;
    private PoiSearch.Query mPoiQuery;
    private PoiSearch mPoiSearch;
    private List<PoiItemBean> mPoiItems;
    private int mPageNumber = 0;
    private String mSearchString = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lm_activity_poi_search);
        initViews();
        initEvents();
    }


    private void initViews() {
        mPoiItems = new ArrayList<>();
        mBack = (ImageView) findViewById(R.id.poi_search_back);
        mSearchContent = (EditText) findViewById(R.id.poi_search_content);
        mSearch = (TextView) findViewById(R.id.poi_search_sure);
        mRv = (RecyclerView) findViewById(R.id.poi_search_rv);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRv.setLayoutManager(manager);
        mRv.setAdapter(new PoiAdapter());
        mRv.addOnScrollListener(scrollListener);

    }

    private void initEvents() {
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchString = mSearchContent.getText().toString();
                if (TextUtils.isEmpty(mSearchString)) {
                    ToastUtil.show(PoiSearchActivity.this, "搜索内容不能为空");
                    return;
                }
                SoftInputUtils.switchSoftInput(PoiSearchActivity.this);
                mPageNumber = 0;
                loadData(mPageNumber);
            }
        });

    }

    private void loadData(int pageNumber) {
        if (pageNumber == 0) {
            mPoiItems.clear();
            mRv.getAdapter().notifyDataSetChanged();
        }
        mPoiQuery = new PoiSearch.Query(mSearchString, "", SPUtils.getCity());
        mPoiSearch = new PoiSearch(this, mPoiQuery);
        mPoiSearch.setOnPoiSearchListener(this);
        double lat = SPUtils.getLatitude(this);
        double lon = SPUtils.getLongitude(this);
        if (lat > 0 && lon > 0) {
            mPoiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(lat, lon), 3000));
        }
        mPoiQuery.setPageSize(20);
        mPoiQuery.setPageNum(pageNumber);
        mPoiSearch.setQuery(mPoiQuery);
        mPoiSearch.searchPOIAsyn();

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
            Loger.log(getClass().getSimpleName(),item.getProvinceName()+"_"+item.getCityName()+"_"+item.getAdName()+"_"+item.getSnippet()+"_"+item.getTitle());
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
            h.mCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String result = bean.getTitle();
                    Intent intent = new Intent();
                    intent.putExtra("data", result);
                    setResult(RESULT_OK, intent);
                    finish();
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
}
