package bbc.com.moteduan_lib.mywidget.swipemenulistview;


/**
 * 
 * @author baoyz
 * @date 2014-8-23
 * 
 */
public class SwipeMenuItem {

	private int id;
	private android.content.Context mContext;
	private String title;
	private android.graphics.drawable.Drawable icon;
	private android.graphics.drawable.Drawable background;
	private int titleColor;
	private int titleSize;
	private int width;

	public SwipeMenuItem(android.content.Context context) {
		mContext = context;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTitleColor() {
		return titleColor;
	}

	public int getTitleSize() {
		return titleSize;
	}

	public void setTitleSize(int titleSize) {
		this.titleSize = titleSize;
	}

	public void setTitleColor(int titleColor) {
		this.titleColor = titleColor;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setTitle(int resId) {
		setTitle(mContext.getString(resId));
	}

	public android.graphics.drawable.Drawable getIcon() {
		return icon;
	}

	public void setIcon(android.graphics.drawable.Drawable icon) {
		this.icon = icon;
	}

	public void setIcon(int resId) {
		this.icon = mContext.getResources().getDrawable(resId);
	}

	public android.graphics.drawable.Drawable getBackground() {
		return background;
	}

	public void setBackground(android.graphics.drawable.Drawable background) {
		this.background = background;
	}

	public void setBackground(int resId) {
		this.background = mContext.getResources().getDrawable(resId);
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

}
