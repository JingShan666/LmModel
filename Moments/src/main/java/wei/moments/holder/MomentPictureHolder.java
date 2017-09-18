package wei.moments.holder;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import wei.moments.R;
import wei.moments.base.BaseRecycleView;
import wei.moments.base.BaseRvHolder;
import wei.moments.decoration.GridlayoutDecoration;
import wei.toolkit.widget.CircleImageBorderView;

/**
 * Created by Administrator on 2017/4/17 0017.
 */

public class MomentPictureHolder extends BaseRvHolder {
    public CircleImageBorderView mPortrait;
    public ImageView mPictureSingle;
    public TextView mName;
    public TextView mAge;
    public ImageView mWatch;
    public TextView mContent;
    public BaseRecycleView mPicRv;
    public TextView mPraise;
    public TextView mFlower;
    public TextView mComment;
    public ImageView delete;
    public ImageView roleFlag;
    public TextView sendTime;
    public TextView sendAddress;
    public MomentPictureHolder(View itemView) {
        super(itemView);
        mPortrait = (CircleImageBorderView) itemView.findViewById(R.id.item_moments_picture_portrait);
        mName = (TextView) itemView.findViewById(R.id.item_moments_picture_name);
        mAge = (TextView) itemView.findViewById(R.id.item_moments_picture_age);
        mWatch = (ImageView) itemView.findViewById(R.id.item_moments_picture_watch);
        mContent = (TextView) itemView.findViewById(R.id.item_moments_picture_conetnt);
        mPicRv = (BaseRecycleView) itemView.findViewById(R.id.item_moments_picture_rv);
        mPraise = (TextView) itemView.findViewById(R.id.item_moments_picture_praise);
        mFlower = (TextView) itemView.findViewById(R.id.item_moments_picture_flower);
        mComment = (TextView) itemView.findViewById(R.id.item_moments_picture_comment);
        delete = (ImageView) itemView.findViewById(R.id.item_moments_picture_delete);
        mPictureSingle = (ImageView) itemView.findViewById(R.id.item_moments_picture_single);
        roleFlag = (ImageView) itemView.findViewById(R.id.item_moments_picture_role_flag);
        sendTime = (TextView) itemView.findViewById(R.id.item_moments_picture_time);
        sendAddress = (TextView) itemView.findViewById(R.id.item_moments_picture_address);
        GridLayoutManager manager = new GridLayoutManager(itemView.getContext(),3);
        manager.setOrientation(GridLayoutManager.VERTICAL);
        mPicRv.setLayoutManager(manager);
        mPicRv.addItemDecoration(new GridlayoutDecoration());

        mContent.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
