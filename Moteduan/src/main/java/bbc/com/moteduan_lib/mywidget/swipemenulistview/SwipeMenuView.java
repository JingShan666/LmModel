package bbc.com.moteduan_lib.mywidget.swipemenulistview;

/**
 * 
 * @author baoyz
 * @date 2014-8-23
 * 
 */
public class SwipeMenuView extends android.widget.LinearLayout implements android.view.View.OnClickListener {

	private SwipeMenuListView mListView;
	private SwipeMenuLayout mLayout;
	private SwipeMenu mMenu;
	private SwipeMenuView.OnSwipeItemClickListener onItemClickListener;
	private int position;

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public SwipeMenuView(SwipeMenu menu, SwipeMenuListView listView) {
		super(menu.getContext());
		mListView = listView;
		mMenu = menu;
		java.util.List<SwipeMenuItem> items = menu.getMenuItems();
		int id = 0;
		for (SwipeMenuItem item : items) {
			addItem(item, id++);
		}
	}

	private void addItem(SwipeMenuItem item, int id) {
		android.widget.LinearLayout.LayoutParams params = new android.widget.LinearLayout.LayoutParams(item.getWidth(),
				android.widget.LinearLayout.LayoutParams.MATCH_PARENT);
		android.widget.LinearLayout parent = new android.widget.LinearLayout(getContext());
		parent.setId(id);
		parent.setGravity(android.view.Gravity.CENTER);
		parent.setOrientation(android.widget.LinearLayout.VERTICAL);
		parent.setLayoutParams(params);
		parent.setBackgroundDrawable(item.getBackground());
		parent.setOnClickListener(this);
		addView(parent);

		if (item.getIcon() != null) {
			parent.addView(createIcon(item));
		}
		if (!android.text.TextUtils.isEmpty(item.getTitle())) {
			parent.addView(createTitle(item));
		}

	}

	private android.widget.ImageView createIcon(SwipeMenuItem item) {
		android.widget.ImageView iv = new android.widget.ImageView(getContext());
		iv.setImageDrawable(item.getIcon());
		return iv;
	}

	private android.widget.TextView createTitle(SwipeMenuItem item) {
		android.widget.TextView tv = new android.widget.TextView(getContext());
		tv.setText(item.getTitle());
		tv.setGravity(android.view.Gravity.CENTER);
		tv.setTextSize(item.getTitleSize());
		tv.setTextColor(item.getTitleColor());
		return tv;
	}

	@Override
	public void onClick(android.view.View v) {
		if (onItemClickListener != null && mLayout.isOpen()) {
			onItemClickListener.onItemClick(this, mMenu, v.getId());
		}
	}

	public SwipeMenuView.OnSwipeItemClickListener getOnSwipeItemClickListener() {
		return onItemClickListener;
	}

	public void setOnSwipeItemClickListener(SwipeMenuView.OnSwipeItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

	public void setLayout(SwipeMenuLayout mLayout) {
		this.mLayout = mLayout;
	}

	public static interface OnSwipeItemClickListener {
		void onItemClick(SwipeMenuView view, SwipeMenu menu, int index);
	}
}
