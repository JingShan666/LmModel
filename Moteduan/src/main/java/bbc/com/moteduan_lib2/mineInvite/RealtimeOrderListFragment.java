package bbc.com.moteduan_lib2.mineInvite;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.google.gson.Gson;
import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bbc.com.moteduan_lib.R;
import bbc.com.moteduan_lib.bean.LoginBean;
import bbc.com.moteduan_lib.database.SpDataCache;
import bbc.com.moteduan_lib.log.LogDebug;
import bbc.com.moteduan_lib.network.ImageLoad;
import bbc.com.moteduan_lib.network.Req;

import com.liemo.shareresource.Url;

import bbc.com.moteduan_lib.tools.SnackbarUtil;
import bbc.com.moteduan_lib.tools.ToastUtils;
import bbc.com.moteduan_lib2.base.BaseFragment;
import bbc.com.moteduan_lib2.bean.MineOrderBean;
import wei.toolkit.helper.EmptyDataViewHolder;
import wei.toolkit.utils.DateUtils;
import wei.toolkit.utils.ToastUtil;
import wei.toolkit.widget.RoundedImageView;

/**
 * Created by Administrator on 2017/6/5 0005.
 */

public class RealtimeOrderListFragment extends BaseFragment {
    public static final String TAG = "RealtimeOrderListFragment";
    private SpringView springView;
    private RecyclerView recyclerView;
    private List<MineOrderBean.TraderOrderBean.ListBean> dataList = new ArrayList<>();
    private long timeStamp;
    private int mCurrentPage = 1;
    private int mCurrentPageSize = 10;
    private boolean isInitData;
    private RvAdapter rvAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine_apply, container, false);
        springView = (SpringView) view.findViewById(R.id.fragment_mine_apply_sv);
        springView.setHeader(new DefaultHeader(getContext()));
        springView.setFooter(new DefaultFooter(getContext()));
        springView.setListener(onFreshListener);
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_mine_apply_rv);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        rvAdapter = new RvAdapter();
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (!isInitData) {
                initData(1);
                isInitData = true;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            if (isInitData) {
                initData(1);
            }
        }
    }

    private SpringView.OnFreshListener onFreshListener = new SpringView.OnFreshListener() {
        @Override
        public void onRefresh() {
            initData(1);
        }

        @Override
        public void onLoadmore() {
            initData(2);
        }
    };

    // 1下拉 2上拉
    private void initData(final int type) {
        LoginBean loginBean = SpDataCache.getSelfInfo(getContext());
        Map<String, Object> map = new HashMap<>();
        map.put("member_id", loginBean == null ? "" : loginBean.getData().getM_id());
        if (1 == type) {
            map.put("timeStamp", timeStamp < 1 ? System.currentTimeMillis() : timeStamp);
            map.put("pageNumber", mCurrentPage = 1);
        } else if (2 == type) {
            map.put("timeStamp", timeStamp);
            map.put("pageNumber", ++mCurrentPage);
        }
        map.put("pageSize", mCurrentPageSize);
        Req.post(Url.realtimeOrderList, map, new Req.ReqCallback() {
            @Override
            public void success(String result) {
                MineOrderBean bean = new Gson().fromJson(result, MineOrderBean.class);
                if (!"200".equals(bean.getCode())) {
                    String tips = bean.getTips();
                    if ("220".equals(bean.getCode())) {
                        // 在别处登陆了。
                    } else if ("201".equals(bean.getCode())) {
                        // 没数据
                        if (type == 1) {
                            if (dataList.size() > 0) {
                                dataList.clear();
                            }
                            recyclerView.setAdapter(rvAdapter);
                            return;
                        } else if (type == 2) {
                            tips = getResources().getString(R.string.sr_no_more_data);
                        }
                    }
//                    ToastUtil.show(getContext(), bean.getTips());
                    SnackbarUtil.show(recyclerView, tips);
                    return;
                }
                if (type == 1) {
                    dataList.clear();
                    timeStamp = bean.getTimeStamp() > 0 ? bean.getTimeStamp() : System.currentTimeMillis();
                    dataList.addAll(bean.getTraderOrder().getList());
                    recyclerView.setAdapter(rvAdapter);
                } else {
                    dataList.addAll(bean.getTraderOrder().getList());
                    recyclerView.getAdapter().notifyDataSetChanged();
                }


            }

            @Override
            public void failed(String msg) {
                //ToastUtil.show(getContext(), msg);
                ToastUtil.show(getContext(),"网络连接不可用，请稍后重试");
            }

            @Override
            public void completed() {
                springView.onFinishFreshAndLoad();
            }
        });
    }


    private class RvAdapter extends RecyclerSwipeAdapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (-1 == viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.helper_empty_data_view, parent, false);
                return new EmptyDataViewHolder(view);
            }
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mine_order, parent, false);
            return new RvHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            if (holder instanceof EmptyDataViewHolder) {
                holder.itemView.setVisibility(View.VISIBLE);
            } else if (holder instanceof RvHolder) {
                final RvHolder h = (RvHolder) holder;
                mItemManger.bindView(h.itemView,position);
                final MineOrderBean.TraderOrderBean.ListBean bean = dataList.get(position);

                ImageLoad.bind(h.img, bean.getPhotos_typeb());
                ImageLoad.bind(h.typeIcon, bean.getPhotos_urlc());
                h.typeName.setText(bean.getSmall_navigation());
                h.price.setText(bean.getReward_price() + "币");

                if (!TextUtils.isEmpty(bean.getStart_time()) && !TextUtils.isEmpty(bean.getEnd_time())) {
                    h.time.setText(DateUtils.getCustomFormatTime(bean.getStart_time(), bean.getEnd_time()));
                }


                String[] addressArr = bean.getAdress().split("==");
                if (addressArr[0] != null) {
                    h.address.setText(addressArr[0]);
                } else {
                    h.address.setText(bean.getAdress());
                }

                int trader_state = bean.getTrader_state();
                LogDebug.log(TAG, "trader_state : " + trader_state);
                switch (trader_state) {
                    case 1:
                        h.state.setImageResource(R.drawable.icon_order_pending);
                        h.swipeLayout.setSwipeEnabled(false);
                        break;
                    case 2:
                        h.state.setImageResource(R.drawable.icon_order_sure);
                        h.swipeLayout.setSwipeEnabled(false);
                        break;
                    case 3:
                        h.state.setImageResource(R.drawable.icon_order_dating);
                        h.swipeLayout.setSwipeEnabled(false);
                        break;
                    case 4:
                        h.state.setImageResource(R.drawable.icon_order_completed);
                        h.swipeLayout.setSwipeEnabled(true);
                        break;
                    case 5:
                    case 6:
                    case 7:
                        h.state.setImageResource(R.drawable.icon_order_cancel);
                        h.swipeLayout.setSwipeEnabled(true);
                        break;
                    case 8:
                    case 9:
                        // 拒绝
                        h.state.setImageResource(R.drawable.icon_order_decline);
                        h.swipeLayout.setSwipeEnabled(true);
                        break;
                    case 10:
                        h.state.setImageResource(R.drawable.icon_order_lose);
                        h.swipeLayout.setSwipeEnabled(true);
                        break;
                    case 11:
                        // 用户取消预约
                        h.state.setImageResource(R.drawable.icon_order_cancel);
                        h.swipeLayout.setSwipeEnabled(true);
                        break;
                    default:
                        h.swipeLayout.setSwipeEnabled(false);
                        h.state.setImageResource(0);
                        break;
                }
                /**
                 * 打开时调用：循环调用onStartOpen,onUpdate,onHandRelease,onUpdate,onOpen,
                 * 关闭时调用：onStartClose,onUpdate,onHandRelease,onHandRelease,onUpdate,onClose
                 */
                h.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
                    @Override
                    public void onStartOpen(SwipeLayout layout) {
                        layout.setClickable(false);
                    }

                    @Override
                    public void onOpen(SwipeLayout layout) {
                    }

                    @Override
                    public void onStartClose(SwipeLayout layout) {
                    }

                    @Override
                    public void onClose(SwipeLayout layout) {
                        layout.setClickable(true);
                    }

                    @Override
                    public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                    }

                    @Override
                    public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                    }
                });

                h.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("traderorder_id", bean.getTrader_order_id());
                        Req.post(Url.deleteOrder, map, new Req.ReqCallback() {
                            @Override
                            public void success(String result) {
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    String code = jsonObject.getString("code");
                                    String tips = jsonObject.getString("tips");
                                    ToastUtils.getInstance(getContext()).showText(tips);
                                    if (!"200".equals(code)) {
                                        return;
                                    }
                                    h.swipeLayout.close(false,true);
                                    dataList.remove(position);
                                    notifyDataSetChanged();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void failed(String msg) {
                                //ToastUtils.getInstance(getContext()).showText(msg);
                                ToastUtil.show(getContext(),"网络连接不可用，请稍后重试");
                            }

                            @Override
                            public void completed() {

                            }
                        });
                    }
                });
                h.swipeLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getActivity(), RealtimeOrderDetailsActivity.class).putExtra("orderId", bean.getTrader_order_id()));
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return isEmptyData() ? 1 : dataList.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (isEmptyData()) return -1;
            return super.getItemViewType(position);
        }

        boolean isEmptyData() {
            return dataList.size() < 1;
        }

        @Override
        public int getSwipeLayoutResourceId(int position) {
            return R.id.swipe;
        }
    }

    private class RvHolder extends RecyclerView.ViewHolder {
        public ImageView state;
        public RoundedImageView img;
        public ImageView typeIcon;
        public TextView typeName;
        public TextView price;
        public TextView time;
        public TextView address;
        public TextView delete;
        public SwipeLayout swipeLayout;

        public RvHolder(View itemView) {
            super(itemView);
            state = (ImageView) itemView.findViewById(R.id.item_mine_order_state);
            img = (RoundedImageView) itemView.findViewById(R.id.item_mine_order_img);
            typeIcon = (ImageView) itemView.findViewById(R.id.item_mine_order_type_icon);
            typeName = (TextView) itemView.findViewById(R.id.item_mine_order_type_name);
            price = (TextView) itemView.findViewById(R.id.item_mine_order_price);
            time = (TextView) itemView.findViewById(R.id.item_mine_order_time);
            address = (TextView) itemView.findViewById(R.id.item_mine_order_address);
            delete = (TextView) itemView.findViewById(R.id.item_mine_order_delete);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            swipeLayout.setClickToClose(true);
        }
    }
}
