package bbc.com.moteduan_lib.renzheng;


import bbc.com.moteduan_lib.app.App;
import bbc.com.moteduan_lib.base.BaseActivity;

public class DialogActivity extends BaseActivity {

	@Override
	protected void onCreate(android.os.Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		App.getApp().addActivity(this);
		// showWifiTips();
		showCameraTips();

	}

	@Override
	public void initView() {

	}

	@Override
	public void initData() {

	}

	/**
	 * 打开wifi提示框
	 */
	private void showWifiTips() {
		// 构建一个Builder对话框
		android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
		builder.setTitle("WIFI已断开！");
		builder.setMessage("是否打开网络设置");
		builder.setPositiveButton(android.R.string.ok,
				new android.app.AlertDialog.OnClickListener() {
					public void onClick(android.content.DialogInterface dialog, int which) {
						if (android.os.Build.VERSION.SDK_INT > 10) {
							// 3.0以上打开设置界面，也可以直接用ACTION_WIRELESS_SETTINGS打开到wifi界面
							startActivity(new android.content.Intent(
									android.provider.Settings.ACTION_SETTINGS));
						} else {
							startActivity(new android.content.Intent(
									android.provider.Settings.ACTION_WIRELESS_SETTINGS));
						}
						finish();
					}
				});
		builder.setNegativeButton(android.R.string.cancel,
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(android.content.DialogInterface dialog, int which) {
						finish();
					}
				});
		builder.show();
	}

	/**
	 * 打开设置提示框
	 */
	private void showCameraTips() {
		// 构建一个Builder对话框
		android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
		builder.setTitle("提示");
		builder.setMessage("您的摄像机权限被禁止，是否设置？");
		builder.setPositiveButton(android.R.string.cancel,
				new android.app.AlertDialog.OnClickListener() {
					public void onClick(android.content.DialogInterface dialog, int which) {
						finish();
					}
				});
		builder.setNegativeButton("设置",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(android.content.DialogInterface dialog, int which) {
						// 打开设置界面
						startActivity(new android.content.Intent(android.provider.Settings.ACTION_SETTINGS));
						finish();
					}
				});
		builder.show();
	}
}
