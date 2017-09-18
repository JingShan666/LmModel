package wei.moments;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.google.gson.Gson;
import com.liemo.shareresource.Url;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wei.moments.base.BaseFragment;
import wei.moments.base.BaseRvAdapter;
import wei.moments.base.BaseRvHolder;
import wei.moments.bean.LoginBean;
import wei.moments.bean.MomentListItemBean;
import wei.moments.bean.comment.CommentListBean;
import wei.moments.bean.comment.CommentReplyBean;
import wei.moments.database.SPUtils;
import wei.moments.network.ReqCallback;
import wei.moments.network.ReqUrl;
import wei.toolkit.utils.ImageLoad;
import wei.toolkit.utils.Loger;
import wei.toolkit.utils.SoftInputUtils;
import wei.toolkit.utils.TextTools;
import wei.toolkit.utils.ToastUtil;
import wei.toolkit.widget.CircleImageBorderView;

/**
 * Created by Administrator on 2017/5/5 0005.
 */

public class CommentFragment extends BaseFragment {
    private static String TAG = "CommentFragment";
    private RecyclerView mRv;
    private List<CommentListBean.ListBean> mListBeen;
    private MomentListItemBean mMomentListItemBean;
    private RelativeLayout mButtomEdit;
    private int page = 1;
    private long mTimeStrmp;

    /**
     * COMMENT_TYPE_1 1级评论 COMMENT_TYPE_2 2级评论
     */
    private int COMMENT_TYPE = -1;
    private final int COMMENT_TYPE_1 = 1;
    private final int COMMENT_TYPE_2 = 2;

    /**
     * 当发起2级评论时把2级数据列表赋值给mTempChildCommentList以便数据提交成功后
     * 通过这个引用来更新数据
     */
    private List<CommentListBean.CommentBean> mTempChildCommentList;

    public CommentFragment() {
        super();
    }

    public CommentFragment(MomentListItemBean bean) {
        Bundle b = new Bundle();
        b.putParcelable("bean", bean);
        setArguments(b);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lm_fragment_comment, container, false);
        mListBeen = new ArrayList<>();
        mMomentListItemBean = getArguments() == null ? new MomentListItemBean() : (MomentListItemBean) getArguments().getParcelable("bean");
        mRv = (RecyclerView) view.findViewById(R.id.lm_fragment_comment_rv);
        mButtomEdit = (RelativeLayout) view.findViewById(R.id.lm_fragment_comment_bottom_edit);
        mButtomEdit.setOnClickListener(buttomEditListenet);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRv.setLayoutManager(manager);
        mRv.setAdapter(new RvAdapter());
        mRv.addOnScrollListener(onScrollListener);
        loadData(page);
        initEvents();
        return view;
    }

    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                LinearLayoutManager manager = (LinearLayoutManager) mRv.getLayoutManager();
                if (manager.findLastCompletelyVisibleItemPosition() == mListBeen.size() - 1) {
                    loadData(++page);
                }
            }

        }
    };

    private View.OnClickListener buttomEditListenet = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LoginBean loginBean = SPUtils.getSelfInfo(getActivity());
            if (null == loginBean) {
                ToastUtil.show(getActivity(), "您还未登录，请先登录");
                Intent intent = new Intent();
                intent.setAction("bbc.com.moteduan_lib2.login.LoginActivity");
                getActivity().startActivity(intent);
                return;
            }
            CommentReplyBean commentReplyBean = new CommentReplyBean();
            commentReplyBean.setUseId(loginBean.getData().getM_id());
            commentReplyBean.setContentSex(ContRes.getSelfType());
            commentReplyBean.setCommentType("1");
            commentReplyBean.setContentId(mMomentListItemBean.getContent_id());
            commentReplyBean.setToUseId(mMomentListItemBean.getUse_id());
            commentReplyBean.setToUseSex(mMomentListItemBean.getContent_sex());
            showEditDialog(commentReplyBean);
            COMMENT_TYPE = COMMENT_TYPE_1;

        }
    };

    private void showEditDialog(final CommentReplyBean commentReplyBean) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.lm_comment_edit, null);
        final PopupWindow mPopupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0));
        mPopupWindow.setFocusable(true);
        final EditText mCommentContent = (EditText) view.findViewById(R.id.lm_fragment_comment_comment);
        ImageView mCommentCommit = (ImageView) view.findViewById(R.id.lm_fragment_comment_commit);
        mCommentCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentContent = mCommentContent.getText().toString().trim();
                if (TextUtils.isEmpty(commentContent)) {
                    ToastUtil.show(getContext(), "评论内容不能为空");
                    return;
                }
                commentReplyBean.setCommentContent(commentContent);
                sendComment(commentReplyBean);
                mPopupWindow.dismiss();
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
            }
        });
        String toName = commentReplyBean.getToName();
        if (!TextUtils.isEmpty(toName)) {
            mCommentContent.setHint("回复" + toName + ":");
        }
        mPopupWindow.showAtLocation(mCommentContent, Gravity.BOTTOM, 0, 0);
        SoftInputUtils.switchSoftInput(getContext());
    }

    public void sendComment(final CommentReplyBean commentReplyBean) {
        Map<String, Object> map = new HashMap<>();
        map.put("comment_type", commentReplyBean.getCommentType());
        map.put("content_id", commentReplyBean.getContentId());
        map.put("use_id", commentReplyBean.getUseId());
        map.put("content_sex", commentReplyBean.getContentSex());
        map.put("comment", commentReplyBean.getCommentContent());
        map.put("pin_use_id", commentReplyBean.getToUseId());
        map.put("pin_use_sex", commentReplyBean.getToUseSex());
        map.put("parid", commentReplyBean.getParid());
        ReqUrl.post(Url.Moments.sendComment, map, new ReqCallback.Callback<String>() {

            @Override
            public void success(String result) {
                try {
                    Loger.log(getClass().getSimpleName(), result);
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    if (!"200".equals(code)) {
                        ToastUtil.show(getContext(), jsonObject.getString("tips"));
                        if ("220".equals(code)) {
                            // 让用户重新登录
                        }
                        return;
                    }
                    switch (COMMENT_TYPE) {
                        case COMMENT_TYPE_1:
                            CommentListBean.ListBean listBean = new Gson().fromJson(jsonObject.getString("maps"), CommentListBean.ListBean.class);
                            mListBeen.add(0, listBean);
                            mRv.getAdapter().notifyDataSetChanged();
                            mRv.smoothScrollToPosition(0);
                            break;
                        case COMMENT_TYPE_2:
                            CommentListBean.ListBean listBean2 = new Gson().fromJson(jsonObject.getString("maps"), CommentListBean.ListBean.class);
                            mTempChildCommentList.clear();
                            mTempChildCommentList.addAll(listBean2.getComment());
                            mRv.getAdapter().notifyDataSetChanged();
                            break;
                        default:
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(String msg) {

            }
        });

    }


    /**
     * @param pageNumber
     */
    private void loadData(final int pageNumber) {
        Map<String, Object> map = new HashMap<>();
        map.put("content_id", mMomentListItemBean.getContent_id());
        map.put("pageSize", 10);
        map.put("pageNumber", pageNumber);
        if (pageNumber < 2) {
            map.put("timeStamp", mTimeStrmp < 1 ? System.currentTimeMillis() : mTimeStrmp);
        } else {
            map.put("timeStamp", mTimeStrmp);
        }
        ReqUrl.post(Url.commentList, map, new ReqCallback.Callback<String>() {
            @Override
            public void success(String result) {
                Loger.log(TAG, result);
                CommentListBean bean = new Gson().fromJson(result, CommentListBean.class);
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

                if (pageNumber < 2) {
                    mListBeen.clear();
                    mTimeStrmp = bean.getTimeStamp() > 0 ? bean.getTimeStamp() : System.currentTimeMillis();
                }
                mListBeen.addAll(bean.getList());
                mRv.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void failed(String msg) {
                ToastUtil.show(getContext(), msg);
            }
        });
    }

    private void initEvents() {


    }


    private class RvAdapter extends BaseRvAdapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lm_item_comment, parent, false);
            return new RvHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            final CommentListBean.ListBean bean = mListBeen.get(position);
            RvHolder h = (RvHolder) holder;
            ImageLoad.bind(h.portrait, bean.getConmmentOne().getHui_head_photo());
            h.name.setText(bean.getConmmentOne().getHui_name());
            h.time.setText(bean.getConmmentOne().getComment_date().trim());
            if (Build.VERSION.SDK_INT < 23) {
                h.content.setText(TextTools.ToDBC(bean.getConmmentOne().getContent()));
            } else {
                h.content.setText(bean.getConmmentOne().getContent());
            }
            List l = bean.getComment();
            if (l != null && l.size() > 0) {
                h.rv.setVisibility(View.VISIBLE);
                h.rv.setAdapter(new RvAdapterChild(bean.getComment()));
            } else {
                h.rv.setVisibility(View.GONE);
            }

            final int huiSex = bean.getConmmentOne().getHui_sex();
            if(huiSex == 1){
                h.roleFlag.setVisibility(View.GONE);
            }else{
                h.roleFlag.setVisibility(View.VISIBLE);
            }
            h.portrait.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (huiSex == 1) {
                        startActivity(new Intent("bbc.com.moteduan_lib2.UserInfoActivity").putExtra("userId", bean.getConmmentOne().getHui_id()));
                    }
                }
            });
            h.mCommentBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoginBean loginBean = SPUtils.getSelfInfo(getActivity());
                    if (null == loginBean) {
                        ToastUtil.show(getActivity(), "您还未登录，请先登录");
                        Intent intent = new Intent();
                        intent.setAction("bbc.com.moteduan_lib2.login.LoginActivity");
                        getActivity().startActivity(intent);
                        return;
                    }
                    if (loginBean.getData().getM_id().equals(bean.getConmmentOne().getHui_id())) {
                        return;
                    }
                    CommentReplyBean commentReplyBean = new CommentReplyBean();
                    commentReplyBean.setUseId(loginBean.getData().getM_id());
                    commentReplyBean.setContentSex(ContRes.getSelfType());
                    commentReplyBean.setCommentType("2");
                    commentReplyBean.setContentId(mMomentListItemBean.getContent_id());
                    commentReplyBean.setParid(bean.getConmmentOne().getParid());
                    commentReplyBean.setToUseId(bean.getConmmentOne().getHui_id());
                    commentReplyBean.setToUseSex(bean.getConmmentOne().getHui_sex() + "");
                    commentReplyBean.setToName(bean.getConmmentOne().getHui_name());
                    showEditDialog(commentReplyBean);
                    COMMENT_TYPE = COMMENT_TYPE_2;
                    mTempChildCommentList = bean.getComment();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mListBeen.size();
        }
    }

    private class RvHolder extends BaseRvHolder {
        public CircleImageBorderView portrait;
        public TextView name;
        public TextView time;
        public TextView content;
        public RecyclerView rv;
        public TextView mCommentBt;
        public ImageView roleFlag;
        public RvHolder(View itemView) {
            super(itemView);
            portrait = (CircleImageBorderView) itemView.findViewById(R.id.lm_item_comment_portrait);
            name = (TextView) itemView.findViewById(R.id.lm_item_comment_name);
            time = (TextView) itemView.findViewById(R.id.lm_item_comment_time);
            content = (TextView) itemView.findViewById(R.id.lm_item_comment_content);
            mCommentBt = (TextView) itemView.findViewById(R.id.lm_item_comment_bt);
            roleFlag = (ImageView) itemView.findViewById(R.id.lm_item_comment_role_flag);
            rv = (RecyclerView) itemView.findViewById(R.id.lm_item_comment_rv);
            LinearLayoutManager manager = new LinearLayoutManager(getContext());
            manager.setOrientation(LinearLayoutManager.VERTICAL);
            rv.setLayoutManager(manager);
            rv.setNestedScrollingEnabled(false);
        }
    }

    private class RvAdapterChild extends BaseRvAdapter {
        private List<CommentListBean.CommentBean> listBeen;

        public RvAdapterChild(List<CommentListBean.CommentBean> list) {
            this.listBeen = list;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lm_item_comment_child, null);
            return new RvHolderChild(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            final CommentListBean.CommentBean bean = listBeen.get(position);
            RvHolderChild h = (RvHolderChild) holder;
            String fromName = bean.getHui_name();
            String toName = bean.getB_hui_name();
            StringBuilder content = new StringBuilder();
            if (TextUtils.isEmpty(toName)) {
                content.append(fromName)
                        .append(":")
                        .append(bean.getContent());
            } else {
                content.append(fromName)
                        .append("回复")
                        .append(toName)
                        .append(":")
                        .append(bean.getContent());
            }
            if (Build.VERSION.SDK_INT < 23) {
                h.textView.setText(TextTools.ToDBC(content.toString()));
            } else {
                h.textView.setText(content.toString());
            }
            h.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoginBean loginBean = SPUtils.getSelfInfo(getActivity());
                    if (null == loginBean) {
                        ToastUtil.show(getActivity(), "您还未登录，请先登录");
                        Intent intent = new Intent();
                        intent.setAction("bbc.com.moteduan_lib2.login.LoginActivity");
                        getActivity().startActivity(intent);
                        return;
                    }
                    if (loginBean.getData().getM_id().equals(bean.getHui_id())) {
                        return;
                    }
                    CommentReplyBean commentReplyBean = new CommentReplyBean();
                    commentReplyBean.setUseId(loginBean.getData().getM_id());
                    commentReplyBean.setContentSex(ContRes.getSelfType());
                    commentReplyBean.setCommentType("2");
                    commentReplyBean.setContentId(mMomentListItemBean.getContent_id());
                    commentReplyBean.setParid(bean.getParid());
                    commentReplyBean.setToUseId(bean.getHui_id());
                    commentReplyBean.setToUseSex(bean.getHui_sex() + "");
                    commentReplyBean.setToName(bean.getHui_name());
                    showEditDialog(commentReplyBean);
                    COMMENT_TYPE = COMMENT_TYPE_2;
                    mTempChildCommentList = listBeen;
                }
            });
        }

        @Override
        public int getItemCount() {
            return listBeen.size();
        }
    }

    private class RvHolderChild extends BaseRvHolder {
        public TextView textView;

        public RvHolderChild(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.lm_item_comment_child_text);
        }
    }

}
