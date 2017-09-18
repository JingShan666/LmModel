package wei.toolkit.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Administrator on 2017/4/19 0019.
 */

public class CircleProgressImage extends ImageView {
    private float maxProgress;
    private float currentProgress;
    private Paint paint;
    private float strokeWidht = 9f;
    private ProgressListener mProgressListener;
    private RectF mRectF = new RectF();

    public CircleProgressImage(Context context) {
        super(context);
        init(context);
    }


    public CircleProgressImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CircleProgressImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.parseColor("#A03E93"));
        paint.setStrokeWidth(strokeWidht);
        paint.setStyle(Paint.Style.STROKE);
    }

    public float getMaxProgress() {
        return maxProgress;
    }

    public void setMaxProgress(float maxProgress) {
        this.maxProgress = maxProgress;
    }

    public float getCurrentProgress() {
        return currentProgress;
    }

    public void setCurrentProgress(float currentProgress) {
        this.currentProgress += currentProgress;
        if (this.mProgressListener != null) {
            mProgressListener.progress(this.currentProgress, this.maxProgress);
        }
        postInvalidate();
    }

    public void reset() {
        setScaleX(1.0f);
        setScaleY(1.0f);
        this.currentProgress = 0;
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawArc(mRectF,270, currentProgress / maxProgress * 360f, false, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRectF.set(strokeWidht, strokeWidht, w - strokeWidht, h - strokeWidht);

    }

    public void setProgressListener(ProgressListener progressListener) {
        this.mProgressListener = progressListener;
    }

    public static interface ProgressListener {
        public void progress(float currentProgress, float maxProgress);
    }
}
