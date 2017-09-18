package bbc.com.moteduan_lib2.wallet;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.liaoinstan.springview.container.DefaultFooter;
import com.liaoinstan.springview.container.DefaultHeader;
import com.liaoinstan.springview.widget.SpringView;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bbc.com.moteduan_lib.R;
import bbc.com.moteduan_lib.base.BaseActivity;
import bbc.com.moteduan_lib.bean.LoginBean;
import bbc.com.moteduan_lib.database.SpDataCache;
import bbc.com.moteduan_lib.home.WithDrawActivity;
import bbc.com.moteduan_lib.log.LogDebug;
import bbc.com.moteduan_lib.network.ImageLoad;
import bbc.com.moteduan_lib.network.Req;

import com.liemo.shareresource.Url;

import org.json.JSONException;
import org.json.JSONObject;

import bbc.com.moteduan_lib.tools.ToastUtils;
import bbc.com.moteduan_lib2.AuthenticationActivity;
import bbc.com.moteduan_lib2.base.BaseFragment;
import bbc.com.moteduan_lib2.bean.WalletBean;
import wei.toolkit.helper.EmptyDataViewHolder;
import wei.toolkit.utils.DateUtils;
import wei.toolkit.utils.Loger;
import wei.toolkit.utils.ToastUtil;
import wei.toolkit.widget.CircleImageBorderView;
import wei.toolkit.widget.VSpringView;

/**
 * Created by Administrator on 2017/6/17 0017.
 *
 */

public class MineWalletActivity extends BaseActivity {
    public static final String TAG = "MineWalletActivity";
    private static AppBarLayout appBarLayout;
    private static LinearLayout needScrollLayout;
    private TextView withdraw;
    private static TextView balance;
    private ImageView back;
    private ImageView blur;
    private static LoginBean loginBean;
    private static ViewPager viewPager;
    private static int appBarLayoutVerticalOffset;
    private static WalletBean walletBean;
    private static DecimalFormat decimalFormat = new DecimalFormat("#.##");
    private TabLayout tabLayout;
    private String[] tabLayoutTitle = {"账单明细","提现记录"};
    private List<Fragment> fragmentList = new ArrayList<>();

    //认证dialog
    private AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_wallet);
        loginBean = SpDataCache.getSelfInfo(MineWalletActivity.this);
        if (null == loginBean) {
            toast.showText("你还未登录账号");
            finish();
        }
        initView();
        initData();
        initEvents();
    }

    @Override
    public void initView() {
        back = (ImageView) findViewById(R.id.activity_mine_wallet_back);
        withdraw = (TextView) findViewById(R.id.activity_mine_wallet_withdraw);
        balance = (TextView) findViewById(R.id.activity_mine_wallet_balance);
        blur = (ImageView) findViewById(R.id.activity_mine_wallet_blur);
        appBarLayout = (AppBarLayout) findViewById(R.id.activity_mine_wallet_appbarlayout);
        needScrollLayout = (LinearLayout) findViewById(R.id.activity_mine_wallet_needscroll);
        viewPager = (ViewPager) findViewById(R.id.activity_mine_wallet_vp);
        tabLayout = (TabLayout) findViewById(R.id.activity_mine_wallet_tab);
        tabLayout.setupWithViewPager(viewPager);
        fragmentList.add(new MineWalletConsumeListFragment());
        fragmentList.add(new MineWalletWithdrawListFragment());
        viewPager.setAdapter(new VpAdapter(getSupportFragmentManager()));

    }

    @Override
    public void initData() {
        if (loginBean != null) {
            String photo = loginBean.getData().getM_head_photo();
            if (!TextUtils.isEmpty(photo)) {
                ImageLoad.bind(blur, photo, 3);
            }
        }
    }


    @Override
    public void initEvents() {
        super.initEvents();

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                appBarLayoutVerticalOffset = verticalOffset;
                float scale = 1f - (((float) Math.abs(verticalOffset)) / (float) (appBarLayout.getHeight() - appBarLayout.getPaddingTop()));
                Loger.log(TAG, "appBarLayout onOffsetChanged = " + verticalOffset + " " + appBarLayout.getHeight() + " " + appBarLayout.getPaddingTop());
                needScrollLayout.setAlpha(scale);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("member_id", SpDataCache.getSelfInfo(MineWalletActivity.this).getData().getM_id());
                Req.post(Url.clickbutton, map, new Req.ReqCallback() {
                    @Override
                    public void success(String result) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            String code = jsonObject.getString("code");
                            String tips = jsonObject.getString("tips");
//                            ToastUtil.show(getContext(), tips);
//                            Log.e("MineFragment:签到success",tips);

                            //判断用户是否已经认证，确定认证跳转认证界面，否则取消dialog
                            if ("201".equals(code)){
                                //未认证
                                View view = LayoutInflater.from(MineWalletActivity.this).inflate(R.layout.renzheng_dialog, null);
                                AlertDialog.Builder builder = new AlertDialog.Builder(MineWalletActivity.this).setView(view);

                                dialog = builder.create();
                                dialog.show();

                                TextView content= (TextView) view.findViewById(R.id.renzheng_content);
                                content.setText(tips);
                                //立即认证
                                Button yesRenzheng = (Button) view.findViewById(R.id.yesrenzheng);
                                yesRenzheng.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //跳转认证界面
                                        startActivity(new Intent(MineWalletActivity.this, AuthenticationActivity.class));
                                        dialog.dismiss();

                                    }
                                });
                                //稍后认证
                                Button noRenzheng = (Button) view.findViewById(R.id.norenzheng);
                                noRenzheng.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        dialog.dismiss();
                                    }
                                });

                            }else if ("200".equals(code)) {
                                startActivity(new Intent(MineWalletActivity.this, WithDrawActivity.class).putExtra("balance", balance.getText().toString().trim()));

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void failed(String msg) {
                        ToastUtil.show(MineWalletActivity.this, msg);
                    }

                    @Override
                    public void completed() {
                    }
                });

            }
        });
    }


    private class VpAdapter extends FragmentPagerAdapter {

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
            return tabLayoutTitle[position];
        }
    }

    public static class MineWalletConsumeListFragment extends BaseFragment {
        private static final  String TAG ="MineWalletConsumeListFragment";
        private RecyclerView recyclerView;
        private long timeStamp;
        private int pageNumber = 1;
        private int pageSize = 10;
        private VSpringView springView;
        private List<WalletBean.OrderBean.ListBean> listBeanList = new ArrayList<>();
        private RvAdapter rvAdapter;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_mine_wallet_list, container, false);
            springView = (VSpringView) view.findViewById(R.id.fragment_mine_wallet_list_sv);
            springView.setHeader(new DefaultHeader(getContext()));
            springView.setFooter(new DefaultFooter(getContext()));
            recyclerView = (RecyclerView) view.findViewById(R.id.fragment_mine_wallet_list_rv);
            LinearLayoutManager manager = new LinearLayoutManager(getContext());
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(manager);
            rvAdapter = new RvAdapter();
            initListenet();
            loadListData(1);
            return view;
        }

        private void initListenet() {
            springView.setListener(onFreshListener);

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    boolean isTop = !ViewCompat.canScrollVertically(recyclerView, -1);
                    Loger.log(TAG, "onTouch isTop " + isTop);
                    if (isTop) {
                        springView.setEnable(appBarLayoutVerticalOffset > -1);
                    } else {
                        springView.setEnable(true);
                    }
                }


                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                }
            });
        }

        /**
         * @param type 1下拉 2上拉
         */
        private void loadListData(final int type) {
            Map<String, Object> map = new HashMap<>();
            map.put("member_id", loginBean.getData().getM_id());
            map.put("pageSize", pageSize);
            if (1 == type) {
                if (timeStamp < 1) timeStamp = System.currentTimeMillis();
                map.put("timeStamp", timeStamp);
                map.put("pageNumber", pageNumber = 1);
            } else if (2 == type) {
                map.put("timeStamp", timeStamp);
                map.put("pageNumber", ++pageNumber);
            }
            Req.post(Url.Wallet.walletConsumeList, map, new Req.ReqCallback() {
                @SuppressLint("SetTextI18n")
                @Override
                public void success(String result) {
                    LogDebug.log(TAG, result);
                    walletBean = new Gson().fromJson(result, WalletBean.class);
                    balance.setText(decimalFormat.format(walletBean.getMember_balance()));
                    if (!"200".equals(walletBean.getCode())) {
                        String tips = walletBean.getTips();
                        if ("201".equals(walletBean.getCode())) {
                            // 没数据
                            if (type == 1) {
                                if (listBeanList.size() > 0) {
                                    listBeanList.clear();
                                }
                                recyclerView.setAdapter(rvAdapter);
                                return;
                            } else if (type == 2) {
                                tips = getResources().getString(R.string.sr_no_more_data);
                            }
                        }
                        ToastUtils.getInstance(getContext()).showText(tips);
                        return;
                    }
                    if (1 == type) {
                        listBeanList.clear();
                        listBeanList.addAll(walletBean.getOrder().getList());
                        timeStamp = walletBean.getTimeStamp() > 0 ? walletBean.getTimeStamp() : System.currentTimeMillis();
                        recyclerView.setAdapter(rvAdapter);
                    } else {
                        listBeanList.addAll(walletBean.getOrder().getList());
                        recyclerView.getAdapter().notifyDataSetChanged();
                    }

                }

                @Override
                public void failed(String msg) {
                    //ToastUtils.getInstance(getContext()).showText(msg);
                    ToastUtil.show(getContext(),"网络连接不可用，请稍后重试");
                }

                @Override
                public void completed() {
                    springView.onFinishFreshAndLoad();
                }
            });
        }

        SpringView.OnFreshListener onFreshListener = new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                loadListData(1);
            }

            @Override
            public void onLoadmore() {
                loadListData(2);
            }
        };

        private class RvAdapter extends RecyclerView.Adapter {

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                if (-1 == viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.helper_empty_data_view, parent, false);
                    return new EmptyDataViewHolder(view);
                }
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mine_wallet_note, parent, false);
                return new RvHolder(view);
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                if (holder instanceof EmptyDataViewHolder) {
                    holder.itemView.setVisibility(View.VISIBLE);
                } else if (holder instanceof RvHolder) {
                    RvHolder h = (RvHolder) holder;
                    final WalletBean.OrderBean.ListBean bean = listBeanList.get(position);
                    int other_type = bean.getOrder_type();
                    if (other_type == 0) {
                        h.money.setText("-" + decimalFormat.format(bean.getMoney()));
                        h.portrait.setImageResource(R.drawable.icon_money_subtract);
                    } else {
                        h.money.setText("+" + decimalFormat.format(bean.getMoney()));
                        h.portrait.setImageResource(R.drawable.icon_money_plus);
                    }

                    h.tips.setText(bean.getExplans());
                    String dateString = bean.getTime();
                    if (!TextUtils.isEmpty(dateString)) {
                        boolean isToday = DateUtils.isToday(dateString);
                        try {
                            Calendar calendar = Calendar.getInstance();
                            @SuppressLint("SimpleDateFormat") Date date = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm").parse(dateString);
                            calendar.setTime(date);

                            if (isToday) {
                                h.week.setText("今天");
                            } else {
                                h.week.setText(DateUtils.weekDaysName[calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH)]);
                            }
                            h.date.setText(calendar.get(Calendar.MONTH) + 1 + "-" + calendar.get(Calendar.DAY_OF_MONTH));

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
//                    int state = bean.getCode();
//                    if (0 == state) {
//                        h.state.setText("正在受理...");
//                    } else {
//                        h.state.setText("");
//                    }


                    h.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                startActivity(new Intent(getActivity(), MoneyOperationDetailsActivity.class).putExtra("detailsId", bean.getIds())
                                .putExtra("dataType",bean.getType()));
                        }
                    });

                }
            }

            @Override
            public int getItemCount() {
                return isEmptyData() ? 1 : listBeanList.size();
            }

            @Override
            public int getItemViewType(int position) {
                if (isEmptyData()) return -1;
                return super.getItemViewType(position);
            }

            boolean isEmptyData() {
                return listBeanList.size() < 1;
            }
        }


        private class RvHolder extends RecyclerView.ViewHolder {
            public TextView week;
            public TextView date;
            public TextView money;
            public TextView tips;
            public TextView state;
            public CircleImageBorderView portrait;

            public RvHolder(View itemView) {
                super(itemView);
                week = (TextView) itemView.findViewById(R.id.item_mine_wallet_note_week);
                date = (TextView) itemView.findViewById(R.id.item_mine_wallet_note_date);
                money = (TextView) itemView.findViewById(R.id.item_mine_wallet_note_money);
                tips = (TextView) itemView.findViewById(R.id.item_mine_wallet_note_tips);
                state = (TextView) itemView.findViewById(R.id.item_mine_wallet_note_state);
                portrait = (CircleImageBorderView) itemView.findViewById(R.id.item_mine_wallet_note_portrait);
            }
        }
    }



    public static class MineWalletWithdrawListFragment extends BaseFragment {
        private static final  String TAG ="MineWalletWithdrawListFragment";
        private RecyclerView recyclerView;
        private long timeStamp;
        private int pageNumber = 1;
        private int pageSize = 10;
        private VSpringView springView;
        private List<WalletBean.OrderBean.ListBean> listBeanList = new ArrayList<>();
        private RvAdapter rvAdapter;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_mine_wallet_list, container, false);
            springView = (VSpringView) view.findViewById(R.id.fragment_mine_wallet_list_sv);
            springView.setHeader(new DefaultHeader(getContext()));
            springView.setFooter(new DefaultFooter(getContext()));
            recyclerView = (RecyclerView) view.findViewById(R.id.fragment_mine_wallet_list_rv);
            LinearLayoutManager manager = new LinearLayoutManager(getContext());
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(manager);
            rvAdapter = new RvAdapter();
            initListenet();
            loadListData(1);
            return view;
        }

        private void initListenet() {
            springView.setListener(onFreshListener);

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    boolean isTop = !ViewCompat.canScrollVertically(recyclerView, -1);
                    Loger.log(TAG, "onTouch isTop " + isTop);
                    if (isTop) {
                        springView.setEnable(appBarLayoutVerticalOffset > -1);
                    } else {
                        springView.setEnable(true);
                    }
                }


                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                }
            });
        }

        /**
         * @param type 1下拉 2上拉
         */
        private void loadListData(final int type) {
            Map<String, Object> map = new HashMap<>();
            map.put("member_id", loginBean.getData().getM_id());
            map.put("pageSize", pageSize);
            if (1 == type) {
                if (timeStamp < 1) timeStamp = System.currentTimeMillis();
                map.put("timeStamp", timeStamp);
                map.put("pageNumber", pageNumber = 1);
            } else if (2 == type) {
                map.put("timeStamp", timeStamp);
                map.put("pageNumber", ++pageNumber);
            }
            Req.post(Url.Wallet.walletWithdrawList, map, new Req.ReqCallback() {
                @SuppressLint("SetTextI18n")
                @Override
                public void success(String result) {
                    LogDebug.log(TAG, result);
                    walletBean = new Gson().fromJson(result, WalletBean.class);
                    balance.setText(decimalFormat.format(walletBean.getMember_balance()));
                    if (!"200".equals(walletBean.getCode())) {
                        String tips = walletBean.getTips();
                        if ("201".equals(walletBean.getCode())) {
                            // 没数据
                            if (type == 1) {
                                if (listBeanList.size() > 0) {
                                    listBeanList.clear();
                                }
                                recyclerView.setAdapter(rvAdapter);
                                return;
                            } else if (type == 2) {
                                tips = getResources().getString(R.string.sr_no_more_data);
                            }
                        }
                        ToastUtils.getInstance(getContext()).showText(tips);
                        return;
                    }
                    if (1 == type) {
                        listBeanList.clear();
                        listBeanList.addAll(walletBean.getOrder().getList());
                        timeStamp = walletBean.getTimeStamp() > 0 ? walletBean.getTimeStamp() : System.currentTimeMillis();
                        recyclerView.setAdapter(rvAdapter);
                    } else {
                        listBeanList.addAll(walletBean.getOrder().getList());
                        recyclerView.getAdapter().notifyDataSetChanged();
                    }

                }

                @Override
                public void failed(String msg) {
                    //ToastUtils.getInstance(getContext()).showText(msg);
                    ToastUtil.show(getContext(),"网络连接不可用，请稍后重试");
                }

                @Override
                public void completed() {
                    springView.onFinishFreshAndLoad();
                }
            });
        }

        SpringView.OnFreshListener onFreshListener = new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                loadListData(1);
            }

            @Override
            public void onLoadmore() {
                loadListData(2);
            }
        };

        private class RvAdapter extends RecyclerView.Adapter {

            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                if (-1 == viewType) {
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.helper_empty_data_view, parent, false);
                    return new EmptyDataViewHolder(view);
                }
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mine_wallet_note, parent, false);
                return new RvHolder(view);
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                if (holder instanceof EmptyDataViewHolder) {
                    holder.itemView.setVisibility(View.VISIBLE);
                } else if (holder instanceof RvHolder) {
                    RvHolder h = (RvHolder) holder;
                    final WalletBean.OrderBean.ListBean bean = listBeanList.get(position);
                    int other_type = bean.getOrder_type();
                    if (other_type == 0) {
                        h.money.setText("-" + decimalFormat.format(bean.getMoney()));
                        h.portrait.setImageResource(R.drawable.icon_money_subtract);
                    } else {
                        h.money.setText("+" + decimalFormat.format(bean.getMoney()));
                        h.portrait.setImageResource(R.drawable.icon_money_plus);
                    }
                    if (1 != bean.getType()) {
                        h.portrait.setImageResource(R.drawable.ic_withdraw);
                    }
                    h.tips.setText(bean.getExplans());
                    String dateString = bean.getTime();
                    if (!TextUtils.isEmpty(dateString)) {
                        boolean isToday = DateUtils.isToday(dateString);
                        try {
                            Calendar calendar = Calendar.getInstance();
                            @SuppressLint("SimpleDateFormat") Date date = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm").parse(dateString);
                            calendar.setTime(date);

                            if (isToday) {
                                h.week.setText("今天");
                            } else {
                                h.week.setText(DateUtils.weekDaysName[calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH)]);
                            }
                            h.date.setText(calendar.get(Calendar.MONTH) + 1 + "-" + calendar.get(Calendar.DAY_OF_MONTH));

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    int state = bean.getCode();
                    if (0 == state) {
                        h.state.setText("正在受理...");
                    } else {
                        h.state.setText("已受理");
                    }


                    h.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                startActivity(new Intent(getActivity(), WithdrawInfoDetailsActivity.class).putExtra("detailsId", bean.getIds()));
                        }
                    });

                }
            }

            @Override
            public int getItemCount() {
                return isEmptyData() ? 1 : listBeanList.size();
            }

            @Override
            public int getItemViewType(int position) {
                if (isEmptyData()) return -1;
                return super.getItemViewType(position);
            }

            boolean isEmptyData() {
                return listBeanList.size() < 1;
            }
        }


        private class RvHolder extends RecyclerView.ViewHolder {
            public TextView week;
            public TextView date;
            public TextView money;
            public TextView tips;
            public TextView state;
            public CircleImageBorderView portrait;

            public RvHolder(View itemView) {
                super(itemView);
                week = (TextView) itemView.findViewById(R.id.item_mine_wallet_note_week);
                date = (TextView) itemView.findViewById(R.id.item_mine_wallet_note_date);
                money = (TextView) itemView.findViewById(R.id.item_mine_wallet_note_money);
                tips = (TextView) itemView.findViewById(R.id.item_mine_wallet_note_tips);
                state = (TextView) itemView.findViewById(R.id.item_mine_wallet_note_state);
                portrait = (CircleImageBorderView) itemView.findViewById(R.id.item_mine_wallet_note_portrait);
            }
        }
    }

}
