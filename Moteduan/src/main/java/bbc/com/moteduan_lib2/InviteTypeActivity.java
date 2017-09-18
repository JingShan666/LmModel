package bbc.com.moteduan_lib2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bbc.com.moteduan_lib.R;
import bbc.com.moteduan_lib.base.BaseActivity;
import bbc.com.moteduan_lib.network.Req;
import com.liemo.shareresource.Url;
import bbc.com.moteduan_lib2.bean.InviteNavigationBean;
import bbc.com.moteduan_lib2.bean.InviteTypeBean;
import wei.toolkit.decoration.GridlayoutDecoration;
import wei.toolkit.utils.ToastUtil;

/**
 * Created by Administrator on 2017/5/31 0031.
 */

public class InviteTypeActivity extends BaseActivity {
    private ImageView mBack;
    private TextView mTitle;
    private Button mSure;
    private RecyclerView mRv;
    private List<InviteTypeBean> mInviteTypes = new ArrayList<>();
    private List<InviteTypeBean> mInviteTypesResult;
    private int mMaxSelectNum = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_type);
        initView();
        initData();
    }

    @Override
    public void initView() {
        mMaxSelectNum = getIntent().getIntExtra("selectNumber", 6);
        mInviteTypesResult = getIntent().getParcelableArrayListExtra("data");
        mBack = (ImageView) findViewById(R.id.bar_action_back);
        mTitle = (TextView) findViewById(R.id.bar_action_title);
        mTitle.setText(String.format("选择喜欢的%1$s类别",getResources().getString(R.string.md_invite_label)));
        mSure = (Button) findViewById(R.id.activity_invite_type_sure);
        mRv = (RecyclerView) findViewById(R.id.activity_invite_type_rv);
        GridLayoutManager manager = new GridLayoutManager(this, 3);


        manager.setOrientation(GridLayoutManager.VERTICAL);
        mRv.setLayoutManager(manager);
        mRv.addItemDecoration(new GridlayoutDecoration());
        mRv.setAdapter(new RvAdapter());

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInviteTypes == null || mInviteTypes.size() < 1) {
                    finish();
                    return;
                }

                if (null == mInviteTypesResult) {
                    mInviteTypesResult = new ArrayList<InviteTypeBean>();
                } else {
                    mInviteTypesResult.clear();
                }

                for (InviteTypeBean bean : mInviteTypes) {
                    if (bean.isSelected()) {
                        mInviteTypesResult.add(bean);
                    }
                }
                if (mInviteTypesResult.size() > mMaxSelectNum) {
                    toast.showText("最多选择" + mMaxSelectNum + "个");
                    return;
                }
                Intent intent = new Intent();
                intent.putParcelableArrayListExtra("data", (ArrayList<? extends Parcelable>) mInviteTypesResult);
                setResult(RESULT_OK, intent);
                finish();

            }
        });

    }

    @Override
    public void initData() {
        Map<String, Object> map = new HashMap<>();
        Req.post(Url.NavigationLabel.playAndBusiness, map, new Req.ReqCallback() {
            @Override
            public void success(String result) {
                InviteNavigationBean bean = new Gson().fromJson(result, InviteNavigationBean.class);
                if (!"200".equals(bean.getCode())) {
                    ToastUtil.show(InviteTypeActivity.this, bean.getTips());
                    return;
                }
                List<InviteNavigationBean.NavigationBean> list = bean.getNavigation();
                if (list == null || list.size() < 1) {
                    failed("数据为空");
                    return;
                }
                mInviteTypes.clear();
                for (InviteNavigationBean.NavigationBean navigationBean : list) {
                    if (navigationBean.getSmall_of_navigation() == 0) {
                        list.remove(navigationBean);
                        break;
                    }
                }
                for (InviteNavigationBean.NavigationBean b : list) {
                    boolean isSelected = false;
                    String label = b.getSmall_navigation();
                    if (mInviteTypesResult != null && mInviteTypesResult.size() > 0) {
                        for (InviteTypeBean inviteTypeBean : mInviteTypesResult) {
                            if (label.equals(inviteTypeBean.getLabel())) {
                                isSelected = true;
                                break;
                            }
                        }
                    }
                    mInviteTypes.add(new InviteTypeBean(label, b.getSmall_of_navigation() + "", isSelected));
                }
                mRv.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void failed(String msg) {
            }

            @Override
            public void completed() {

            }
        });
    }

    private class RvAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invite_type, parent, false);
            return new RvHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            RvHolder h = (RvHolder) holder;
            final InviteTypeBean bean = mInviteTypes.get(position);
            h.mLabel.setText(bean.getLabel());
            h.mLabel.setSelected(bean.isSelected());
            h.mLabel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bean.isSelected()) {
                        bean.setSelected(false);
                    } else {
                        bean.setSelected(true);
                    }
                    notifyItemChanged(position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mInviteTypes.size();
        }
    }

    private class RvHolder extends RecyclerView.ViewHolder {
        public TextView mLabel;

        public RvHolder(View itemView) {
            super(itemView);
            mLabel = (TextView) itemView.findViewById(R.id.item_invite_type_label);
        }
    }
}
