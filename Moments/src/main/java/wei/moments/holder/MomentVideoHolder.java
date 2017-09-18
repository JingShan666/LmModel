package wei.moments.holder;

import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import wei.moments.R;
import wei.moments.base.BaseRvHolder;
import wei.toolkit.widget.CircleImageBorderView;
import wei.toolkit.widget.VideoPlayView;

/**
 * Created by Administrator on 2017/4/14 0014.
 */

public class MomentVideoHolder extends BaseRvHolder {
    public CircleImageBorderView mPortrait;
    public TextView mName;
    public TextView mAge;
    public ImageView mWatch;
    public TextView mContent;
    public TextView mPraise;
    public TextView mFlower;
    public TextView mComment;
    public ImageView mPlay;
    public ImageView mThumbnail;
    public VideoPlayView mVideoView;
    public ProgressBar mProgressBar;
    public RelativeLayout mControlContainer;
    public ImageView delete;
    public ImageView roleFlag;
    public TextView sendTime;
    public TextView sendAddress;
    public MomentVideoHolder(View itemView) {
        super(itemView);

        mPortrait = (CircleImageBorderView) itemView.findViewById(R.id.item_moments_video_portrait);
        mName = (TextView) itemView.findViewById(R.id.item_moments_video_name);
        mAge = (TextView) itemView.findViewById(R.id.item_moments_video_age);
        mWatch = (ImageView) itemView.findViewById(R.id.item_moments_video_watch);
        mContent = (TextView) itemView.findViewById(R.id.item_moments_video_conetnt);
        mPraise = (TextView) itemView.findViewById(R.id.item_moments_video_praise);
        mFlower = (TextView) itemView.findViewById(R.id.item_moments_video_flower);
        mComment = (TextView) itemView.findViewById(R.id.item_moments_video_comment);
        mPlay = (ImageView) itemView.findViewById(R.id.item_moments_video_play);
        mVideoView = (VideoPlayView) itemView.findViewById(R.id.item_moments_video_view);
        mProgressBar = (ProgressBar) itemView.findViewById(R.id.item_moments_video_progress);
        mThumbnail = (ImageView) itemView.findViewById(R.id.item_moments_video_thumbnail);
        mControlContainer = (RelativeLayout) itemView.findViewById(R.id.item_moments_video_control_container);
        delete = (ImageView) itemView.findViewById(R.id.item_moments_video_delete);
        roleFlag = (ImageView) itemView.findViewById(R.id.item_moments_video_role_flag);
        sendTime = (TextView) itemView.findViewById(R.id.item_moments_video_time);
        sendAddress = (TextView) itemView.findViewById(R.id.item_moments_video_address);

        mContent.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
