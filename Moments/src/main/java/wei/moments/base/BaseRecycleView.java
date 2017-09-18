package wei.moments.base;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2017/4/13 0013.
 */

public class BaseRecycleView extends RecyclerView {
    private boolean isClick = true;

    public BaseRecycleView(Context context) {
        super(context);
    }

    public BaseRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public boolean isClick() {
        return isClick;
    }

    public void setClick(boolean click) {
        isClick = click;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(!isClick()){
            return false;
        }
        return super.dispatchTouchEvent(ev);

    }
}
