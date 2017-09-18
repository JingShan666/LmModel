package wei.moments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.liemo.shareresource.Url;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wei.moments.base.BaseFragment;
import wei.moments.base.BaseRecycleView;
import wei.moments.base.BaseRvAdapter;
import wei.moments.base.BaseRvHolder;
import wei.moments.bean.FlowerListBean;
import wei.moments.network.ReqCallback;
import wei.moments.network.ReqUrl;
import wei.toolkit.utils.ImageLoad;
import wei.toolkit.utils.Loger;
import wei.toolkit.utils.ToastUtil;
import wei.toolkit.widget.CircleImageBorderView;


/**
 * Created by Administrator on 2017/4/17 0017.
 */

public class SendFlowerFragment extends BaseFragment {
    private static String TAG = "SendFlowerFragment";
    private BaseRecycleView mRv;
    private List<FlowerListBean.GiftBean.ListBean> mListItemBeen;
    private int page = 1;
    private long mTimeStrmp;

    public SendFlowerFragment() {
        super();
    }

    public SendFlowerFragment(String content_id) {
        Bundle b = new Bundle();
        b.putString("content_id", content_id);
        setArguments(b);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lm_fragment_send_flower, container, false);

        mListItemBeen = new ArrayList<>();

        mRv = (BaseRecycleView) view.findViewById(R.id.fragment_send_flower_rv);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRv.setLayoutManager(manager);
        mRv.setAdapter(new SendFlowerAdapter());
        mRv.addOnScrollListener(onScrollListener);
        initDatas(page);
        return view;
    }

    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                LinearLayoutManager manager = (LinearLayoutManager) mRv.getLayoutManager();
                if (manager.findLastCompletelyVisibleItemPosition() == mListItemBeen.size() - 1) {
                    ++page;
                    initDatas(page);
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    };

    private void initDatas(final int pageNum) {
        Map<String, Object> map = new HashMap<>();
        String content_id = getArguments() == null ? "" : getArguments().getString("content_id");
        map.put("content_id", content_id);
        map.put("pageSize", 10);
        map.put("pageNumber", pageNum);
        if (pageNum < 2) {
            map.put("timeStamp", mTimeStrmp < 1 ? System.currentTimeMillis() : mTimeStrmp);
        } else {
            map.put("timeStamp", mTimeStrmp);
        }
        ReqUrl.post(Url.sendFlowerList, map, new ReqCallback.Callback<String>() {
            @Override
            public void success(String result) {
                Loger.log(TAG, result);
                FlowerListBean bean = new Gson().fromJson(result, FlowerListBean.class);
                if (!"200".equals(bean.getCode())) {
                    if ("220".equals(bean.getCode())) {
                        // 让用户重新登录
                    } else if ("201".equals(bean.getCode())) {
                        // 没数据
                        if (page > 1) {
                            --page;
                        }
                        return;
                    }
                    ToastUtil.show(getContext(), bean.getTips());
                    return;
                }

                if (pageNum < 2) {
                    mListItemBeen.clear();
                    mTimeStrmp = bean.getTimeStamp() > 0 ? bean.getTimeStamp() : System.currentTimeMillis();
                }
                mListItemBeen.addAll(bean.getGift().getList());
                mRv.getAdapter().notifyDataSetChanged();

            }

            @Override
            public void failed(String msg) {
                ToastUtil.show(getContext(), msg);
            }
        });
    }

    private class SendFlowerAdapter extends BaseRvAdapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lm_item_send_flower, parent, false);
            return new SendFlowerHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            SendFlowerHolder h = (SendFlowerHolder) holder;
            FlowerListBean.GiftBean.ListBean bean = mListItemBeen.get(position);
            ImageLoad.bind(h.circleImageView, bean.getHead_photo());
            h.name.setText(bean.getNick_name());
            h.num.setText(bean.getNum() + "");
        }

        @Override
        public int getItemCount() {
            return mListItemBeen.size();
        }
    }

    private class SendFlowerHolder extends BaseRvHolder {
        public CircleImageBorderView circleImageView;
        public TextView name;
        public TextView num;

        public SendFlowerHolder(View itemView) {
            super(itemView);
            circleImageView = (CircleImageBorderView) itemView.findViewById(R.id.item_send_flower_portrait);
            name = (TextView) itemView.findViewById(R.id.item_send_flower_name);
            num = (TextView) itemView.findViewById(R.id.item_send_flower_flower);
        }
    }
}
