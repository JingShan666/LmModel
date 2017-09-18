package bbc.com.moteduan_lib.mywidget;


import bbc.com.moteduan_lib.tools.ConvertUtils;
import bbc.com.moteduan_lib.tools.ScreenUtil;

/**
 * Created by admin on 2016/1/20.
 */
public class SwipeMenuLayout extends android.widget.LinearLayout
{
    /**
     * 关闭
     */
    public static final int SLIDE_STATUS_OFF = 0;
    /**
     * 开始滚动
     */
    public static final int SLIDE_STATUS_START_SCROLL = 1;
    /**
     * 已经打开
     */
    public static final int SLIDE_STATUS_ON = 2;
    private static final int TAN = 2;
    /**
     * 滚动类
     */
    private android.widget.Scroller mScroller;
    /**
     * 屏幕尺寸类
     */
    private int[] screenSize;
    /**
     * 主内容区域
     */
    private android.view.ViewGroup mContent;
    /**
     * 菜单区域
     */
    private android.view.ViewGroup mMenu;
    /**
     * 默认的宽度
     */
    private int mMenuWidth = 120;

    private int lastX,lastY;

    public SwipeMenuLayout(android.content.Context context)
    {
        this(context,null);
    }

    public SwipeMenuLayout(android.content.Context context, android.util.AttributeSet attrs)
    {
        this(context, attrs,0);
    }

    @android.annotation.TargetApi(android.os.Build.VERSION_CODES.HONEYCOMB)
    public SwipeMenuLayout(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(android.content.Context context)
    {
        screenSize = ScreenUtil.getScreenSize(context);
        mMenuWidth = ConvertUtils.dp2px(context,mMenuWidth);
        this.setOrientation(android.widget.LinearLayout.HORIZONTAL);
        mScroller = new android.widget.Scroller(context);
    }

    boolean isOnce = false;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        if (!isOnce)
        {
            mContent = (android.view.ViewGroup) getChildAt(0);
            mContent.getLayoutParams().width = screenSize[0];
            mMenu = (android.view.ViewGroup) getChildAt(1);
            mMenu.getLayoutParams().width = mMenuWidth;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 设置默认的显示
     */
    public void shrink()
    {
        android.util.Log.e("----------",getScrollX() + "");
        if (getScrollX() != 0)
        {
            smoothScrollTo(0, 0);
        }
    }

    @Override
    public boolean onTouchEvent(android.view.MotionEvent event)
    {
        /**
         * 重置上次滑动的View
         */
        if (onSlidingListener != null) {
            onSlidingListener.onSliding(this, SLIDE_STATUS_START_SCROLL);
        }
        int x = (int) event.getX();
        int y = (int) event.getY();
        int scrollX = getScrollX();
        switch (event.getAction())
        {
            case android.view.MotionEvent.ACTION_DOWN:
            {
                /**
                 * 如果没有结束则停止动画
                 */
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                break;
            }
            case android.view.MotionEvent.ACTION_MOVE:
            {
                int deltaX = x - lastX;
                int deltaY = y - lastY;
                if (Math.abs(deltaX) < Math.abs(deltaY) * TAN) {
                    break;
                }
                int newScrollX = scrollX - deltaX;
                if (deltaX != 0) {
                    if (newScrollX < 0) {
                        newScrollX = 0;
                    } else if (newScrollX > mMenuWidth) {
                        newScrollX = mMenuWidth;
                    }
                    this.scrollTo(newScrollX, 0);
//                    return true;
                }
                break;
            }
            case android.view.MotionEvent.ACTION_UP:
            {
                int mX = 0;
                if (scrollX - mMenuWidth * 0.75f > 0) {
                    mX = mMenuWidth;
                }
                this.smoothScrollTo(mX, 0);
                if (onSlidingListener != null) {
                    onSlidingListener.onSliding(this, mX == 0 ? SLIDE_STATUS_OFF : SLIDE_STATUS_ON);
                }
                break;
            }
        }
        lastX = x;
        lastY = y;

        return super.onTouchEvent(event);
    }

    /**
     * 换换滚动到的位置
     * @param x
     * @param y
     */
    private void smoothScrollTo(int x, int y)
    {
        int scrollX = getScrollX();
        int dealtX = x - scrollX;
        /**
         * 1.startX
         * 2.startY
         * 3.endX
         * 4.endY
         * 5.duration
         */
        mScroller.startScroll(scrollX,0,dealtX,0, Math.abs(dealtX) * 3);
        invalidate();
    }

    /*public void onTouchEvents(MotionEvent event)
    {
        *//**
         * 重置上次滑动的View
         *//*
        if (onSlidingListener != null) {
            onSlidingListener.onSliding(this, SLIDE_STATUS_START_SCROLL);
        }
        int x = (int) event.getX();
        int y = (int) event.getY();
        int scrollX = getScrollX();

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
            {
                *//**
                 * 如果没有结束则停止动画
                 *//*
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                break;
            }
            case MotionEvent.ACTION_MOVE:
            {
                int deltaX = x - lastX;
                int deltaY = y - lastY;
                if (Math.abs(deltaX) < Math.abs(deltaY) * TAN) {
                    break;
                }
                int newScrollX = scrollX - deltaX;
                if (deltaX != 0) {
                    if (newScrollX < 0) {
                        newScrollX = 0;
                    } else if (newScrollX > mMenuWidth) {
                        newScrollX = mMenuWidth;
                    }
                    this.scrollTo(newScrollX, 0);
                }
                break;
            }
            case MotionEvent.ACTION_UP:
            {
                int mX = 0;
                if (scrollX - mMenuWidth * 0.75f > 0) {
                    mX = mMenuWidth;
                }
                this.smoothScrollTo(mX, 0);
                if (onSlidingListener != null) {
                    onSlidingListener.onSliding(this, mX == 0 ? SLIDE_STATUS_OFF : SLIDE_STATUS_ON);
                }
                break;
            }
        }
        lastX = x;
        lastY = y;
    }*/

    @Override
    public void computeScroll()
    {
        if (mScroller.computeScrollOffset())
        {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    public void setOnSlidingListener(SwipeMenuLayout.OnSlidingListener onSlidingListener)
    {
        this.onSlidingListener = onSlidingListener;
    }

    private SwipeMenuLayout.OnSlidingListener onSlidingListener;

    public interface OnSlidingListener
    {
        void onSliding(android.view.View view, int state);
    }


}
