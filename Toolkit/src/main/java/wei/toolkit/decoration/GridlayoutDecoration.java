package wei.toolkit.decoration;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Administrator on 2017/4/17 0017.
 */

public class GridlayoutDecoration extends RecyclerView.ItemDecoration {
    private int mDecorationHeight;
    private GridLayoutManager manager;

    public GridlayoutDecoration() {
        this(15);
    }

    public GridlayoutDecoration(int decorationHeight) {
        this.mDecorationHeight = decorationHeight;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (manager == null) {
            manager = (GridLayoutManager) parent.getLayoutManager();
        }
        int spanCount = manager.getSpanCount();
        int position = parent.getChildLayoutPosition(view);
        if ((position + 1) % spanCount != 0) {
            outRect.right = mDecorationHeight;
            outRect.bottom = mDecorationHeight;
        } else {
            outRect.bottom = mDecorationHeight;
        }

    }
}
