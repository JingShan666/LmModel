package wei.toolkit.widget

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * Created by Administrator on 2017/8/4 0004.
 */
class VRecycleView : RecyclerView {

    var isClick = true


    constructor(context: Context?) : this(context, null!!)
    constructor(context: Context?, attributeSet: AttributeSet) : this(context, attributeSet, 0)
    constructor(context: Context?, attributeSet: AttributeSet, defStyle: Int) : super(context, attributeSet, defStyle)


    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (!isClick) {
            return false
        }
        return super.dispatchTouchEvent(ev)
    }
}