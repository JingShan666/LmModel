package bbc.com.moteduan_lib2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import bbc.com.moteduan_lib.R;
import bbc.com.moteduan_lib.base.BaseActivity;
import bbc.com.moteduan_lib.database.SpDataCache;
import bbc.com.moteduan_lib2.bean.InvitePushListBean;
import bbc.com.moteduan_lib2.mineInvite.ApplyOrderDetailsActivity;
import bbc.com.moteduan_lib2.mineInvite.AppointmentOrderDetailsActivity;
import bbc.com.moteduan_lib2.mineInvite.InviteDetailsDidNotSignUpActivity;
import bbc.com.moteduan_lib2.mineInvite.RealtimeOrderDetailsActivity;
import bbc.com.moteduan_lib2.mineNotive.NoticeDetailsActivity;
import bbc.com.moteduan_lib2.mineNotive.NoticeDetailsDidNotSignUpActivity;

/**
 * Created by Administrator on 2017/6/16 0016.
 */

public class InvitePushListActivity extends BaseActivity {
    private ImageView back;
    private TextView title;
    private RecyclerView recyclerView;
    private InvitePushListBean invitePushListBean;
    private RelativeLayout emptyDataView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_push_list);
        initView();
        initData();
        initEvents();
    }

    @Override
    public void initView() {
        back = (ImageView) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.title);
        recyclerView = (RecyclerView) findViewById(R.id.activity_invite_push_list_rv);
        emptyDataView = (RelativeLayout) findViewById(R.id.helper_empty_data_view_root);
        title.setText("订单推送");
        invitePushListBean = SpDataCache.getInvitePushList();
        if (invitePushListBean == null) {
            emptyDataView.setVisibility(View.VISIBLE);
            return;
        }
        if (invitePushListBean.getList() != null && invitePushListBean.getList().size() > 0) {
            LinearLayoutManager manager = new LinearLayoutManager(this);
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(new RvAdapter());
            recyclerView.scrollToPosition(invitePushListBean.getList().size() - 1);
        } else {
            emptyDataView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void initData() {

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

    private class RvAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invite_push_list, parent, false);
            return new RvHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            RvHolder h = (RvHolder) holder;
            final InvitePushListBean.ListBean bean = invitePushListBean.getList().get(position);
            h.content.setText(bean.getAlert());
            h.time.setText(bean.getTime());
            h.watch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String orderType = bean.getType();
                    String orderId = bean.getId();
                    if ("DD_1".equals(orderType)) {
                        // 我的报名详情页面
                        startActivity(new Intent(InvitePushListActivity.this, ApplyOrderDetailsActivity.class).putExtra("orderId", orderId));
                    } else if ("DD_2".equals(orderType)) {
                        // 实时详情页面
                        startActivity(new Intent(InvitePushListActivity.this, RealtimeOrderDetailsActivity.class).putExtra("orderId", orderId));
                    } else if ("DD_3".equals(orderType)) {
                        // 预约详情页面
                        startActivity(new Intent(InvitePushListActivity.this, AppointmentOrderDetailsActivity.class).putExtra("orderId", orderId));
                    } else if ("DD_4".equals(orderType)) {
                        //通告详情页面
                        startActivity(new Intent(InvitePushListActivity.this, NoticeDetailsActivity.class).putExtra("orderId", orderId));
                    } else if ("BMX_1".equals(orderType)) {
                        // 用户发单后的提醒，跳转用户发的订单页面
                        startActivity(new Intent(InvitePushListActivity.this, InviteDetailsDidNotSignUpActivity.class).putExtra("orderId", orderId));
                    } else if ("BMX_2".equals(orderType)) {
                        //用户发单后的提醒， 跳转用户发的通告页面
                        startActivity(new Intent(InvitePushListActivity.this, NoticeDetailsDidNotSignUpActivity.class).putExtra("orderId", orderId));
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return invitePushListBean.getList().size();
        }
    }

    private class RvHolder extends RecyclerView.ViewHolder {
        public TextView content;
        public TextView watch;
        public TextView time;

        public RvHolder(View itemView) {
            super(itemView);
            content = (TextView) itemView.findViewById(R.id.item_invite_push_list_content);
            watch = (TextView) itemView.findViewById(R.id.item_invite_push_list_watch);
            time = (TextView) itemView.findViewById(R.id.item_invite_push_list_time);
        }
    }
}
