package wei.toolkit.widget

import android.annotation.SuppressLint
import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import wei.toolkit.R

/**
 * Created by Administrator on 2017/9/8 0008.
 */
class VViewPager : ViewPager {
    private var isBanSlide: Boolean = false
    private var isChildViewAnewMeasure: Boolean = false

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        @SuppressLint("CustomViewStyleable")
        val ta = context.obtainStyledAttributes(attrs, R.styleable.VViewPager)
        isChildViewAnewMeasure = ta.getBoolean(R.styleable.VViewPager_childViewAnewMeasure, false)
        isBanSlide = ta.getBoolean(R.styleable.VViewPager_banSlide, false)
        ta.recycle()
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        try {
            if (isBanSlide) {
                return false
            }
            return super.onInterceptTouchEvent(ev)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        try {
            if (isBanSlide) {
                return false
            }
            return super.onTouchEvent(ev)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightMeasureSpec = heightMeasureSpec
        if (isChildViewAnewMeasure) {
            var height = 0
            //下面遍历所有child的高度
            for (i in 0..childCount - 1) {
                val child = getChildAt(i)
                child.measure(widthMeasureSpec,
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
                val h = child.measuredHeight
                if (h > height) {
                    //采用最大的view的高度。
                    height = h
                }
            }
            heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height,
                    View.MeasureSpec.EXACTLY)
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
}