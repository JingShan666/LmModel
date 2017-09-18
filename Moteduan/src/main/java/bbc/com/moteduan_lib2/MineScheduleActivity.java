package bbc.com.moteduan_lib2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bbc.com.moteduan_lib.R;
import bbc.com.moteduan_lib.ReleaseDate.ReleaseSkill;
import bbc.com.moteduan_lib.base.BaseActivity;
import bbc.com.moteduan_lib.database.SpDataCache;
import bbc.com.moteduan_lib.network.Req;
import com.liemo.shareresource.Url;
import bbc.com.moteduan_lib2.bean.InviteNavigationBean;
import wei.toolkit.utils.Loger;

/**
 * Created by Administrator on 2017/6/14 0014.
 * 我的档期页面
 */

public class MineScheduleActivity extends BaseActivity {
    private static String TAG = "MineScheduleActivity";
    private ImageView back;
    private ImageView edit;
    private RecyclerView recyclerView;
    private List<MineScheduleBean.SheduleBean> scheduleList = new ArrayList<>();
    private List<InviteNavigationBean.NavigationBean> mInviteTypes = new ArrayList<>();
    private RelativeLayout emptyDataView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_schedule);
        initView();
        initData();
        initEvents();
    }

    @Override
    public void initView() {
        back = (ImageView) findViewById(R.id.back);
        edit = (ImageView) findViewById(R.id.edit);
        emptyDataView = (RelativeLayout) findViewById(R.id.helper_empty_data_view_root);
        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(new RvAdapter());
    }

    @Override
    public void initData() {
        Req.post(Url.inviteNavigationTab, null, new Req.ReqCallback() {
            @Override
            public void success(String result) {
                InviteNavigationBean bean = new Gson().fromJson(result, InviteNavigationBean.class);
                List<InviteNavigationBean.NavigationBean> list = bean.getNavigation();
                if (list != null || list.size() > 0) {
                    mInviteTypes.clear();
                    mInviteTypes.addAll(list);
                }
            }

            @Override
            public void failed(String msg) {
                toast.showText(msg);
            }

            @Override
            public void completed() {
                Map<String, Object> map = new HashMap<>();
                map.put("member_id", SpDataCache.getSelfInfo(MineScheduleActivity.this).getData().getM_id());
                Req.post(Url.peekSelfSchedule, map, new Req.ReqCallback() {
                    @Override
                    public void success(String result) {
                        Loger.log(TAG,result);
                        MineScheduleBean mineScheduleBean = new Gson().fromJson(result, MineScheduleBean.class);
                        if (!"200".equals(mineScheduleBean.getCode_shedule())) {
                            if ("201".equals(mineScheduleBean.getCode_shedule())) {
                                emptyDataView.setVisibility(View.VISIBLE);
                                return;
                            }
                            toast.showText(mineScheduleBean.getTips());
                            return;
                        }
                        if (null != mineScheduleBean.getShedule()) {
                            scheduleList.addAll(mineScheduleBean.getShedule());
                            recyclerView.getAdapter().notifyDataSetChanged();
                        } else {
                            emptyDataView.setVisibility(View.VISIBLE);
                        }
                    }
                    @Override
                    public void failed(String msg) {
                        //toast.showText(msg);
                        toast.showText("网络连接不可用，请稍后重试");

                    }

                    @Override
                    public void completed() {

                    }
                });
            }
        });

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

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MineScheduleActivity.this, ReleaseSkill.class));
            }
        });

    }


    private class RvAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mine_schedule, parent, false);
            return new RvHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            RvHolder h = (RvHolder) holder;
            MineScheduleBean.SheduleBean bean = scheduleList.get(position);
            String[] startTimes = null;
            String[] endTimes = null;
            if (!TextUtils.isEmpty(bean.getStart_time()) && bean.getStart_time().contains(" ")) {
                startTimes = bean.getStart_time().split(" ");
            }
            if (!TextUtils.isEmpty(bean.getEnd_time()) && bean.getEnd_time().contains(" ")) {
                endTimes = bean.getEnd_time().split(" ");
            }
            if (null != startTimes && null != endTimes) {
                h.time.setText(startTimes[1] + "-" + endTimes[1]);
                h.date.setText(startTimes[0]);
            }
            if (!TextUtils.isEmpty(bean.getContent())) {
                String[] contents = bean.getContent().split(",");
                if (mInviteTypes.size() > 0) {
                    for (int i = 0; i < contents.length; i++) {
                        for (int i1 = 0; i1 < mInviteTypes.size(); i1++) {
                            if (contents[i].equals(mInviteTypes.get(i1).getSmall_of_navigation() + "")) {
                                contents[i] = mInviteTypes.get(i1).getSmall_navigation();
                                break;
                            }
                        }
                    }
                }
                List<String> list = new ArrayList<>(Arrays.asList(contents));
                list.add(0, "接受:");
                contents = list.toArray(new String[]{});
                h.inviteTypeTfl.setAdapter(new TagAdapter<String>(contents) {
                    @Override
                    public View getView(FlowLayout parent, int position, String string) {
                        TextView textView = new TextView(parent.getContext());
                        textView.setTextColor(ContextCompat.getColor(MineScheduleActivity.this, R.color.sr_font_grey));
                        if (position == 0) {
                            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_js, 0, 0, 0);
                            textView.setCompoundDrawablePadding(10);
                        } else {
                            textView.setBackgroundColor(Color.parseColor("#f2f2f2"));
                            textView.setPadding(9, 3, 9, 3);
                        }
                        textView.setText(string);
                        return textView;
                    }
                });
            }

            if (!TextUtils.isEmpty(bean.getB_shoparea())) {
                String[] contents = bean.getB_shoparea().split(",");
                List<String> list = new ArrayList<>(Arrays.asList(contents));
                list.add(0, "接受商圈范围:");
                contents = list.toArray(new String[]{});
                h.inviteAddressTfl.setAdapter(new TagAdapter<String>(contents) {
                    @Override
                    public View getView(FlowLayout parent, int position, String string) {
                        TextView textView = new TextView(parent.getContext());
                        textView.setTextColor(ContextCompat.getColor(MineScheduleActivity.this, R.color.sr_font_grey));
                        if (position == 0) {
                            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.icon_sq, 0, 0, 0);
                            textView.setCompoundDrawablePadding(10);
                        } else {
                            textView.setBackgroundColor(Color.parseColor("#f2f2f2"));
                            textView.setPadding(9, 3, 9, 3);
                        }
                        textView.setText(string);
                        return textView;
                    }
                });
            }

        }

        @Override
        public int getItemCount() {
            return scheduleList.size();
        }
    }

    private class RvHolder extends RecyclerView.ViewHolder {
        public TextView time;
        public TextView date;
        public TagFlowLayout inviteTypeTfl;
        public TagFlowLayout inviteAddressTfl;

        public RvHolder(View itemView) {
            super(itemView);
            time = (TextView) itemView.findViewById(R.id.item_mine_schedule_time);
            date = (TextView) itemView.findViewById(R.id.item_mine_schedule_date);
            inviteTypeTfl = (TagFlowLayout) itemView.findViewById(R.id.item_mine_schedule_invite_type_tfl);
            inviteAddressTfl = (TagFlowLayout) itemView.findViewById(R.id.item_mine_schedule_invite_address_tfl);
        }
    }

    public static class MineScheduleBean {
        /**
         * code_shedule : 200
         * shedule : [{"content":"1,2,3","b_shoparea":"碧沙岗,锦艺城,中原万达","end_time":"2017-06-14 24:00:00","start_time":"2017-06-14 10:00:00"}]
         * tips : 成功！
         */

        private String code_shedule;
        private String tips;
        private List<SheduleBean> shedule;

        public String getCode_shedule() {
            return code_shedule;
        }

        public void setCode_shedule(String code_shedule) {
            this.code_shedule = code_shedule;
        }

        public String getTips() {
            return tips;
        }

        public void setTips(String tips) {
            this.tips = tips;
        }

        public List<SheduleBean> getShedule() {
            return shedule;
        }

        public void setShedule(List<SheduleBean> shedule) {
            this.shedule = shedule;
        }

        public static class SheduleBean {
            /**
             * content : 1,2,3
             * b_shoparea : 碧沙岗,锦艺城,中原万达
             * end_time : 2017-06-14 24:00:00
             * start_time : 2017-06-14 10:00:00
             */

            private String content;
            private String b_shoparea;
            private String end_time;
            private String start_time;

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getB_shoparea() {
                return b_shoparea;
            }

            public void setB_shoparea(String b_shoparea) {
                this.b_shoparea = b_shoparea;
            }

            public String getEnd_time() {
                return end_time;
            }

            public void setEnd_time(String end_time) {
                this.end_time = end_time;
            }

            public String getStart_time() {
                return start_time;
            }

            public void setStart_time(String start_time) {
                this.start_time = start_time;
            }
        }
    }

}
