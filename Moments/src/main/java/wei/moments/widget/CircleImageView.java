package wei.moments.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Administrator on 2017/4/18 0018.
 */

public class CircleImageView extends ImageView {
    private int minSize;

    public CircleImageView(Context context) {
        super(context);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {

        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }
        if (!(drawable instanceof BitmapDrawable)) {
            super.onDraw(canvas);
            return;
        }
        BitmapDrawable bd = (BitmapDrawable) drawable;
        Bitmap bitmap = bd.getBitmap();
        if (bitmap == null) {
            return;
        }

        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        float scale = 1.0f;
        int bSize = Math.min(bitmap.getWidth(), bitmap.getHeight());
        scale = minSize * 1.0f / bSize;
        Matrix matrix = new Matrix();
        matrix.setScale(scale, scale);
        bitmapShader.setLocalMatrix(matrix);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setShader(bitmapShader);
        canvas.drawCircle(minSize / 2, minSize / 2, minSize / 2, paint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        minSize = Math.min(w, h);
    }
}
