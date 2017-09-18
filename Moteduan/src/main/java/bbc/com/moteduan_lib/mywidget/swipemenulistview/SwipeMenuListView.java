package bbc.com.moteduan_lib.mywidget.swipemenulistview;

/**
 * 
 * @author baoyz
 * @date 2014-8-18
 * 
 */
public class SwipeMenuListView extends android.widget.ListView {

	private static final int TOUCH_STATE_NONE = 0;
	private static final int TOUCH_STATE_X = 1;
	private static final int TOUCH_STATE_Y = 2;

	private int MAX_Y = 5;
	private int MAX_X = 3;
	private float mDownX;
	private float mDownY;
	private int mTouchState;
	private int mTouchPosition;
	private SwipeMenuLayout mTouchView;
	private SwipeMenuListView.OnSwipeListener mOnSwipeListener;

	private SwipeMenuCreator mMenuCreator;
	private SwipeMenuListView.OnMenuItemClickListener mOnMenuItemClickListener;
	private android.view.animation.Interpolator mCloseInterpolator;
	private android.view.animation.Interpolator mOpenInterpolator;

	public SwipeMenuListView(android.content.Context context) {
		super(context);
		init();
	}

	public SwipeMenuListView(android.content.Context context, android.util.AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public SwipeMenuListView(android.content.Context context, android.util.AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		MAX_X = dp2px(MAX_X);
		MAX_Y = dp2px(MAX_Y);
		mTouchState = TOUCH_STATE_NONE;
	}

	@Override
	public void setAdapter(android.widget.ListAdapter adapter) {
		super.setAdapter(new SwipeMenuAdapter(getContext(), adapter) {
			@Override
			public void createMenu(SwipeMenu menu) {
				if (mMenuCreator != null) {
					mMenuCreator.create(menu);
				}
			}

			@Override
			public void onItemClick(SwipeMenuView view, SwipeMenu menu,
					int index) {
				if (mOnMenuItemClickListener != null) {
					mOnMenuItemClickListener.onMenuItemClick(
							view.getPosition(), menu, index);
				}
				if (mTouchView != null) {
					mTouchView.smoothCloseMenu();
				}
			}
		});
	}

	public void setCloseInterpolator(android.view.animation.Interpolator interpolator) {
		mCloseInterpolator = interpolator;
	}

	public void setOpenInterpolator(android.view.animation.Interpolator interpolator) {
		mOpenInterpolator = interpolator;
	}

	public android.view.animation.Interpolator getOpenInterpolator() {
		return mOpenInterpolator;
	}

	public android.view.animation.Interpolator getCloseInterpolator() {
		return mCloseInterpolator;
	}

	@Override
	public boolean onInterceptTouchEvent(android.view.MotionEvent ev) {
		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(android.view.MotionEvent ev) {
		if (ev.getAction() != android.view.MotionEvent.ACTION_DOWN && mTouchView == null)
			return super.onTouchEvent(ev);
		int action = android.support.v4.view.MotionEventCompat.getActionMasked(ev);
		action = ev.getAction();
		switch (action) {
		case android.view.MotionEvent.ACTION_DOWN:
			int oldPos = mTouchPosition;
			mDownX = ev.getX();
			mDownY = ev.getY();
			mTouchState = TOUCH_STATE_NONE;

			mTouchPosition = pointToPosition((int) ev.getX(), (int) ev.getY());

			if (mTouchPosition == oldPos && mTouchView != null
					&& mTouchView.isOpen()) {
				mTouchState = TOUCH_STATE_X;
				mTouchView.onSwipe(ev);
				return true;
			}

			android.view.View view = getChildAt(mTouchPosition - getFirstVisiblePosition());

			if (mTouchView != null && mTouchView.isOpen()) {
				mTouchView.smoothCloseMenu();
				mTouchView = null;
				return super.onTouchEvent(ev);
			}
			if (view instanceof SwipeMenuLayout) {
				mTouchView = (SwipeMenuLayout) view;
			}
			if (mTouchView != null) {
				mTouchView.onSwipe(ev);
			}
			break;
		case android.view.MotionEvent.ACTION_MOVE:
			float dy = Math.abs((ev.getY() - mDownY));
			float dx = Math.abs((ev.getX() - mDownX));
			if (mTouchState == TOUCH_STATE_X) {
				if (mTouchView != null) {
					mTouchView.onSwipe(ev);
				}
				getSelector().setState(new int[] { 0 });
				ev.setAction(android.view.MotionEvent.ACTION_CANCEL);
				super.onTouchEvent(ev);
				return true;
			} else if (mTouchState == TOUCH_STATE_NONE) {
				if (Math.abs(dy) > MAX_Y) {
					mTouchState = TOUCH_STATE_Y;
				} else if (dx > MAX_X) {
					mTouchState = TOUCH_STATE_X;
					if (mOnSwipeListener != null) {
						mOnSwipeListener.onSwipeStart(mTouchPosition);
					}
				}
			}
			break;
		case android.view.MotionEvent.ACTION_UP:
			if (mTouchState == TOUCH_STATE_X) {
				if (mTouchView != null) {
					mTouchView.onSwipe(ev);
					if (!mTouchView.isOpen()) {
						mTouchPosition = -1;
						mTouchView = null;
					}
				}
				if (mOnSwipeListener != null) {
					mOnSwipeListener.onSwipeEnd(mTouchPosition);
				}
				ev.setAction(android.view.MotionEvent.ACTION_CANCEL);
				super.onTouchEvent(ev);
				return true;
			}
			break;
		}
		return super.onTouchEvent(ev);
	}

	public void smoothOpenMenu(int position) {
		if (position >= getFirstVisiblePosition()
				&& position <= getLastVisiblePosition()) {
			android.view.View view = getChildAt(position - getFirstVisiblePosition());
			if (view instanceof SwipeMenuLayout) {
				mTouchPosition = position;
				if (mTouchView != null && mTouchView.isOpen()) {
					mTouchView.smoothCloseMenu();
				}
				mTouchView = (SwipeMenuLayout) view;
				mTouchView.smoothOpenMenu();
			}
		}
	}

	private int dp2px(int dp) {
		return (int) android.util.TypedValue.applyDimension(android.util.TypedValue.COMPLEX_UNIT_DIP, dp,
				getContext().getResources().getDisplayMetrics());
	}

	public void setMenuCreator(SwipeMenuCreator menuCreator) {
		this.mMenuCreator = menuCreator;
	}

	public void setOnMenuItemClickListener(
			SwipeMenuListView.OnMenuItemClickListener onMenuItemClickListener) {
		this.mOnMenuItemClickListener = onMenuItemClickListener;
	}

	public void setOnSwipeListener(SwipeMenuListView.OnSwipeListener onSwipeListener) {
		this.mOnSwipeListener = onSwipeListener;
	}

	public static interface OnMenuItemClickListener {
		void onMenuItemClick(int position, SwipeMenu menu, int index);
	}

	public static interface OnSwipeListener {
		void onSwipeStart(int position);

		void onSwipeEnd(int position);
	}
}
