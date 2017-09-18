package bbc.com.moteduan_lib.tools;

/**
 * Created by yx on 2016/7/26.
 */
public class InputMethodUtils {

    private android.content.Context context;

    public InputMethodUtils(android.content.Context context) {
        this.context = context;
    }

    /**
     * 如果输入法在窗口上已经显示，则隐藏，反之则显示)
     */
    public void toggle(){
        android.view.inputmethod.InputMethodManager imm = (android.view.inputmethod.InputMethodManager) context.getSystemService(android.content.Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 强制显示或关闭输入框
     * @param view 目标view
     * @param openClose 是否打开输入框 true 显示  false 隐藏
     */
    public void switchInput(android.view.View view,boolean openClose){
        android.view.inputmethod.InputMethodManager imm = (android.view.inputmethod.InputMethodManager) context.getSystemService(android.content.Context.INPUT_METHOD_SERVICE);
        if(openClose){
            view.requestFocus();
            imm.showSoftInput(view, android.view.inputmethod.InputMethodManager.SHOW_FORCED);
        }else{
            view.clearFocus();
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
        }
    }

    /**
     * 调用隐藏系统默认的输入法
     * @param WidgetSearchActivity WidgetSearchActivity是当前的Activity
     */
    public void openSystemDefaultInputSoft(android.app.Activity WidgetSearchActivity){
        ((android.view.inputmethod.InputMethodManager)context.getSystemService(android.content.Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(WidgetSearchActivity.getCurrentFocus().getWindowToken(), android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 获取输入法打开的状态(
     * 还可以获取根布局设置监听 rootLayout.addOnLayoutChangeListener(this);
     *  public void onLayoutChange(android.view.View v,
                                     int left,    int top,    int right,    int bottom,
                                     int oldLeft, int oldTop, int oldRight, int oldBottom) {

     if((bottom) < oldBottom){//软键盘打开
     }else if(bottom > oldBottom){//软键盘关闭
     })
     * @return 返回true，则表示输入法打开
     */
    public boolean getStatus(){
        android.view.inputmethod.InputMethodManager imm = (android.view.inputmethod.InputMethodManager)context.getSystemService(android.content.Context.INPUT_METHOD_SERVICE);
        boolean isOpen=imm.isActive();//isOpen若返回true，则表示输入法打开
        return isOpen;
    }
}
