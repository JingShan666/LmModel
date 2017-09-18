package wei.toolkit.decoration;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import wei.toolkit.R;


/**
 * Created by Administrator on 2017/4/17 0017.
 */

public class LinelayoutDecoration extends RecyclerView.ItemDecoration {
    private int mDecorationHeight = 20;
    private  Drawable drawable;
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int chiCount = parent.getChildCount();
        if(drawable == null){
            drawable = new ColorDrawable(ContextCompat.getColor(parent.getContext(), R.color.v_decoration));
        }
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        for(int i = 0;i < chiCount; i++){
            View view = parent.getChildAt(i);
            int top = view.getBottom();
            int bottom = top + mDecorationHeight;
            drawable.setBounds(left,top,right,bottom);
            drawable.draw(c);
        }
    }
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = mDecorationHeight;
    }

}
