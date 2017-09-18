package bbc.com.moteduan_lib2.home.invite;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;
import com.liemo.shareresource.Const;
import com.liemo.shareresource.Url;

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
import bbc.com.moteduan_lib.tools.SnackbarUtil;
import bbc.com.moteduan_lib.tools.ToastUtils;
import bbc.com.moteduan_lib2.AuthenticationActivity;
import bbc.com.moteduan_lib2.UserInfoActivity;
import bbc.com.moteduan_lib2.bean.InviteBean;
import bbc.com.moteduan_lib2.mineInvite.InviteDetailsDidNotSignUpActivity;
import bbc.com.moteduan_lib2.mineNotive.NoticeDetailsDidNotSignUpActivity;
import wei.toolkit.helper.EmptyDataViewHolder;
import wei.toolkit.utils.DateUtils;
import wei.toolkit.utils.Loger;
import wei.toolkit.utils.ToastUtil;
import wei.toolkit.widget.CircleImageBorderView;
import wei.toolkit.widget.RoundedImageView;

/**
 * Created by Administrator on 2017/5/23 0023.
 */

public class InviteListFragment extends Fragment {
    public static final String TAG = "InviteListFragment";
    private SpringView mSv;
    private RecyclerView mRv;
    private List<InviteBean.SheduleBean.ListBean> mLists = new ArrayList<>();
    private long timeStamp;
    private int mCurrentPage = 1;
    private int mCurrentPageSize = 10;
    private boolean isInitData;
    private RvAdapter rvAdapter;
    private static final int REQUEST_CODE_DETAILS = 1;
    private InviteBean.SheduleBean.ListBean itemBean;
    public InviteListFragment() {
        super();
    }

    @SuppressLint("ValidFragment")
    public InviteListFragment(int requestType, int dataType, String url) {
        Bundle b = new Bundle();
        b.putInt("requestType", requestType);
        b.putInt("dataType", dataType);
        b.putString("url", url == null ? "" : url);
        setArguments(b);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_invite_list, container, false);
        mSv = (SpringView) view.findViewById(R.id.fragment_invite_list_sv);
        mSv.setHeader(new DefaultHeader(getContext()));
        mSv.setFooter(new DefaultFooter(getContext()));
        mSv.setListener(onFreshListener);
        mRv = (RecyclerView) view.findViewById(R.id.fragment_invite_list_rv);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRv.setLayoutManager(manager);
        rvAdapter = new RvAdapter();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) return;
        if(requestCode == REQUEST_CODE_DETAILS){
            if(itemBean != null){
                itemBean.setK_num(1);
                itemBean = null;
                if(rvAdapter != null){
                    rvAdapter.notifyDataSetChanged();
                }

            }
        }

    }

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
        map.put("type", getArguments() == null ? 0 : getArguments().getInt("requestType", 0));
        map.put("current_city", SpDataCache.getCity());
        Req.post(getArguments() == null ? "" : getArguments().getString("url"), map, new Req.ReqCallback() {
            @Override
            public void success(String result) {
                Loger.log(TAG, result);
                InviteBean bean = new Gson().fromJson(result, InviteBean.class);
                String tips = bean.getTips();
                if (!"200".equals(bean.getCode())) {
                    if ("220".equals(bean.getCode())) {
                        // 在别处登录了。
                    } else if ("201".equals(bean.getCode())) {
                        // 没有数据
                        if (type == 1) {
                            if (mLists.size() > 0) {
                                mLists.clear();
                            }
                            mRv.setAdapter(rvAdapter);
                            return;
                        } else if (type == 2) {
                            tips = getResources().getString(R.string.sr_no_more_data);
                        }
                    }
//                    ToastUtil.show(getContext(), bean.getTips());
                    SnackbarUtil.show(mRv, tips);
                    return;
                }
                if (type == 1) {
                    mLists.clear();
                    timeStamp = bean.getTimeStamp() > 0 ? bean.getTimeStamp() : System.currentTimeMillis();
                    mLists.addAll(bean.getShedule().getList());
                    mRv.setAdapter(rvAdapter);
                } else {
                    mLists.addAll(bean.getShedule().getList());
                    mRv.getAdapter().notifyDataSetChanged();
                }


            }

            @Override
            public void failed(String msg) {
                Loger.log(TAG, msg);
            }

            @Override
            public void completed() {
                mSv.onFinishFreshAndLoad();
            }
        });
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

    private class RvAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (-1 == viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.helper_empty_data_view, parent, false);
                return new EmptyDataViewHolder(view);
            }
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dateitem, parent, false);
            return new RvHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            if (holder instanceof EmptyDataViewHolder) {
                EmptyDataViewHolder h = (EmptyDataViewHolder) holder;
                h.itemView.setVisibility(View.VISIBLE);
            } else if (holder instanceof RvHolder) {
                final InviteBean.SheduleBean.ListBean bean = mLists.get(position);
                final RvHolder h = (RvHolder) holder;
                ImageLoad.bind(h.conver, bean.getPhotos_all());
                ImageLoad.bind(h.portrait, bean.getU_head_photo());
                h.name.setText(bean.getU_nick_name());
                h.age.setText(bean.getU_age() + "");

                if (!TextUtils.isEmpty(bean.getStart_time()) && !TextUtils.isEmpty(bean.getEnd_time())) {
                    h.time.setText(DateUtils.getCustomFormatTime(bean.getStart_time(), bean.getEnd_time()));
                }

                String[] addressArr = bean.getAdress().split("==");
                if (addressArr[0] != null) {
                    h.address.setText(addressArr[0]);
                } else {
                    h.address.setText(bean.getAdress());
                }

                h.price.setText(bean.getReward_price() + "");

                if(bean.getK_num() == 1){
                    h.apply.setEnabled(false);
                    h.apply.setText("已报名");
                }else{
                    h.apply.setEnabled(true);
                    h.apply.setText("报名");
                }

                h.apply.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final LoginBean loginBean = SpDataCache.getSelfInfo(getActivity());
                        if (null == loginBean || TextUtils.isEmpty(loginBean.getData().getM_id())) {
                            ToastUtil.show(getActivity(), "您还未登录，请先登录");
                            Intent intent = new Intent();
                            intent.setAction("bbc.com.moteduan_lib2.login.LoginActivity");
                            getActivity().startActivity(intent);
                            return;
                        }
                        new AlertDialog.Builder(getContext())
                                .setMessage("确定要报名吗？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        Map<String, Object> map = new HashMap<String, Object>();
                                        map.put("member_id", loginBean.getData().getM_id());
                                        map.put("trader_id", bean.getTrader_id());
                                        String url = "";
                                        int dataType = getArguments() == null ? -1 : getArguments().getInt("dataType");
                                        if (dataType == Const.Companion.getINVITE_TYPE_NOVICE()) {
                                            url = Url.Invite.applyNotice;
                                        } else {
                                            url = Url.applyInviteOrder;
                                        }
                                        Req.post(url, map, new Req.ReqCallback() {
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
//                                                                mLists.remove(position);
//                                                                notifyDataSetChanged();
                                                    bean.setK_num(1);
                                                    h.apply.setEnabled(false);
                                                    h.apply.setText("已报名");
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }

                                            @Override
                                            public void failed(String msg) {
                                                ToastUtils.getInstance(getContext()).showText(msg);
                                            }

                                            @Override
                                            public void completed() {

                                            }
                                        });
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .show();

//                        Map<String, Object> map = new HashMap<>();
//                        map.put("member_id", SpDataCache.getSelfInfo(getContext()).getData().getM_id());
//                        Req.post(Url.getModelAuthState, map, new Req.ReqCallback() {
//                            @Override
//                            public void success(String result) {
//                                LogDebug.log(TAG, result);
//                                JSONObject jsonObject = null;
//                                try {
//                                    jsonObject = new JSONObject(result);
//                                    String code = jsonObject.getString("code");
//                                    String tips = jsonObject.getString("tips");
//                                    if (!"200".equals(code)) {
//                                        startActivity(new Intent(getActivity(), AuthenticationActivity.class));
//                                        ToastUtils.getInstance(getContext()).showText(tips);
//                                        return;
//                                    }
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                            @Override
//                            public void failed(String msg) {
//                            }
//                            @Override
//                            public void completed() {
//                            }
//                        });

                    }

                });

                h.portrait.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getActivity(), UserInfoActivity.class).putExtra("userId", bean.getU_id()));
                    }
                });

                h.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int dataType = getArguments() == null ? -1 : getArguments().getInt("dataType");
                        if (dataType == Const.Companion.getINVITE_TYPE_NOVICE()) {
                            startActivityForResult(new Intent(getActivity(), NoticeDetailsDidNotSignUpActivity.class)
                                    .putExtra("orderId", bean.getTrader_id()), REQUEST_CODE_DETAILS);
                        } else {
                            startActivityForResult(new Intent(getActivity(), InviteDetailsDidNotSignUpActivity.class)
                                    .putExtra("orderId", bean.getTrader_id()), REQUEST_CODE_DETAILS);
                        }
                        itemBean = bean;

                    }
                });
            }


        }

        @Override
        public int getItemCount() {
            return isEmptyData() ? 1 : mLists.size();
        }

        boolean isEmptyData() {
            return mLists.size() < 1;
        }

        @Override
        public int getItemViewType(int position) {
            if (isEmptyData()) return -1;
            return super.getItemViewType(position);
        }
    }

    private class RvHolder extends RecyclerView.ViewHolder {
        public RoundedImageView conver;
        public CircleImageBorderView portrait;
        public TextView name;
        public TextView age;
        public TextView time;
        public TextView address;
        public TextView price;
        public TextView apply;

        public RvHolder(View itemView) {
            super(itemView);
            conver = (RoundedImageView) itemView.findViewById(R.id.datecover);
            portrait = (CircleImageBorderView) itemView.findViewById(R.id.dateicon);
            name = (TextView) itemView.findViewById(R.id.datename);
            age = (TextView) itemView.findViewById(R.id.datesex);
            time = (TextView) itemView.findViewById(R.id.datetime);
            address = (TextView) itemView.findViewById(R.id.datelocation);
            price = (TextView) itemView.findViewById(R.id.datemoney);
            apply = (TextView) itemView.findViewById(R.id.dateapply);
        }
    }
}
