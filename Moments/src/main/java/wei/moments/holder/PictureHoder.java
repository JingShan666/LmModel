package wei.moments.holder;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import wei.moments.R;
import wei.moments.base.BaseRvHolder;

/**
 * Created by Administrator on 2017/4/17 0017.
 */

public class PictureHoder extends BaseRvHolder {
    public ImageView imageView;
    private static int recyclerViewWidth;

    public PictureHoder(View itemView) {
        this(itemView, null);
    }

    public PictureHoder(final View itemView, RecyclerView recyclerView) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.item_picture_image);
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
            if (recyclerViewWidth < 1) {
                recyclerViewWidth = recyclerView.getMeasuredWidth();
            }
            imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, recyclerViewWidth / columnCount));
        }
    }


}
