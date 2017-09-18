package wei.moments.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

/**
 * Created by Administrator on 2017/4/21 0021.
 */

public class DefSizeVideoView extends VideoView {
    public DefSizeVideoView(Context context) {
        super(context);
    }

    public DefSizeVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DefSizeVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getDefaultSize(getWidth(), widthMeasureSpec);
        int height = getDefaultSize(getHeight(), heightMeasureSpec);
        setMeasuredDimension(width, height);
    }
}
