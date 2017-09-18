package wei.moments.decoration;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Administrator on 2017/4/17 0017.
 */

public class GridlayoutDecoration extends RecyclerView.ItemDecoration {
    private int mDecorationHeight = 15;
    private GridLayoutManager manager;
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if(manager == null){
            manager = (GridLayoutManager) parent.getLayoutManager();
        }
        int spanCount = manager.getSpanCount();
        int position = parent.getChildLayoutPosition(view);
        if ((position + 1) % spanCount != 0) {
            outRect.set(0, 0, mDecorationHeight, mDecorationHeight);
        } else {
            outRect.set(0, 0, 0, mDecorationHeight);
        }

    }
}
