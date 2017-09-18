package wei.moments.holder;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import wei.moments.R;
import wei.moments.base.BaseRvHolder;
import wei.moments.widget.DefSizeVideoView;

/**
 * Created by Administrator on 2017/4/21 0021.
 */

public class VideoHolder extends BaseRvHolder {
    public DefSizeVideoView videoView;
    private static int videoViewWidth;

    public VideoHolder(View itemView) {
        this(itemView, null);
    }

    public VideoHolder(View itemView, RecyclerView recyclerView) {
        super(itemView);
        videoView = (DefSizeVideoView) itemView.findViewById(R.id.item_video_v);
        if (recyclerView != null) {
            int columnCount = 1;
            RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
            if (manager != null) {
                if (manager instanceof GridLayoutManager) {
                    columnCount = ((GridLayoutManager) manager).getSpanCount();
                } else if (manager instanceof StaggeredGridLayoutManager) {
                    columnCount = ((StaggeredGridLayoutManager) manager).getSpanCount();
                }
            }
            if (videoViewWidth < 1) {
                videoViewWidth = recyclerView.getMeasuredWidth();
            }
            videoView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, videoViewWidth / columnCount));
        }
    }
}
