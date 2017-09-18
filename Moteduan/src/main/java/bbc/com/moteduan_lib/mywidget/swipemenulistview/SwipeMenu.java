package bbc.com.moteduan_lib.mywidget.swipemenulistview;

/**
 * 
 * @author baoyz
 * @date 2014-8-23
 * 
 */
public class SwipeMenu {

	private android.content.Context mContext;
	private java.util.List<SwipeMenuItem> mItems;
	private int mViewType;

	public SwipeMenu(android.content.Context context) {
		mContext = context;
		mItems = new java.util.ArrayList<SwipeMenuItem>();
	}

	public android.content.Context getContext() {
		return mContext;
	}

	public void addMenuItem(SwipeMenuItem item) {
		mItems.add(item);
	}

	public void removeMenuItem(SwipeMenuItem item) {
		mItems.remove(item);
	}

	public java.util.List<SwipeMenuItem> getMenuItems() {
		return mItems;
	}

	public SwipeMenuItem getMenuItem(int index) {
		return mItems.get(index);
	}

	public int getViewType() {
		return mViewType;
	}

	public void setViewType(int viewType) {
		this.mViewType = viewType;
	}

}
