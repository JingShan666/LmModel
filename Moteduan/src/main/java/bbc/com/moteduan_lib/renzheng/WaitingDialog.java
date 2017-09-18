package bbc.com.moteduan_lib.renzheng;


import bbc.com.moteduan_lib.R;

public class WaitingDialog extends android.app.Dialog{
	

	private android.content.Context mContext;
	private boolean isCancel;
	private String message;
	private android.view.View mView;
	private static WaitingDialog mDialog;
	
	/**
	 * 弹出等待框
	 */
	public static synchronized void show(android.content.Context context, String message, Boolean isCancel){
		if (mDialog != null) {
			mDialog.cancel();
			mDialog = null;
		}
		mDialog = new WaitingDialog(context, message, isCancel);
		mDialog.show();
	}
	/**
	 * 取消等待框
	 */
	public static synchronized void cancelDialog(){
		if (mDialog != null) {
			mDialog.cancel();
			mDialog = null;
		}
	}
	
	public WaitingDialog(android.content.Context context, String message, Boolean isCancel) {
		super(context);
		this.mContext = context;
		this.isCancel = isCancel;
		this.message = message;
	}
	
	public void setMessage(String strMessage){
		if(mView != null){
			((android.widget.TextView)mView.findViewById(R.id.tv)).setText(strMessage);
		}
	}
	
	@Override
	public synchronized void show() {
		setCancelable(isCancel);
		requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawableResource(R.drawable.waiting_dialog_bg);
		getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN,
				android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN);
		if(message==null || message.equals("")){
			setContentView(R.layout.waiting_dialog);
		}else {
			mView = android.view.LayoutInflater.from(mContext).inflate(R.layout.waiting_dialog, null);
			setMessage(message);
			setContentView(mView);
		}
		super.show();
	}
	
	public synchronized void cancel(){
		dismiss();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if(keyCode == android.view.KeyEvent.KEYCODE_BACK && isCancel){
			dismiss();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
