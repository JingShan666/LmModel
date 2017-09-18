package bbc.com.moteduan_lib.mywidget;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import bbc.com.moteduan_lib.R;
import bbc.com.moteduan_lib.home.HomeActivity;
import bbc.com.moteduan_lib.tools.ScreenUtils;


/**
 * Created by yx on 2016/8/24.
 */
public class DialogProgress {

    private AlertDialog dialog;
    private ProgressBar progressBar;
    private TextView proTxt;
    private TextView title;

    private Button cancle;
    private Button ok;
    private ImageView jiazai_img;
    private final View view;


    private TextView jiazai_text;
    private RotateAnimation rotanimation;
    private int screenHeight;
    private int screenWidth;
    private int width;
    private int height;
    private PopupWindow popupWindow;
    private View rootView;
    private final Context context;
    private final Activity act;


    public void setJiazai_text(String text) {
        jiazai_text.setText(text);
    }
    public TextView getJiazai_text() {
        return jiazai_text;
    }
    public DialogProgress(View v, Activity activity){
        act = activity;
        context = v.getContext();
        view = LayoutInflater.from(context).inflate(R.layout.dialog_progress, null);
        jiazai_img = (ImageView) view.findViewById(R.id.jiazai_img);
        jiazai_text = (TextView) view.findViewById(R.id.jiazai_text);
        screenHeight = ScreenUtils.getScreenHeight(context);
        screenWidth = ScreenUtils.getScreenWidth(context);
        //高度设置为屏幕的0.3
        height = (int) (screenHeight*0.3);
        //宽度设置为屏幕的0.5
        width = (int) (screenWidth*0.5);
        popupWindow = new PopupWindow(view,width,height);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);
        rootView = v;
    }

    public void gonetv(){
        jiazai_text.setVisibility(View.GONE);
    }

    public void gonebg(){
        view.setBackgroundResource(R.color.transparent);
    }

    public void show() {
        if (popupWindow != null){
            if (act instanceof HomeActivity){
                popupWindow.showAtLocation(rootView, Gravity.CENTER,0,0);
                startAnimation();
            } else {
                popupWindow.showAtLocation(rootView, Gravity.CENTER,0,0);
                backgroundAlpha((float) 0.4);
                startAnimation();
            }
        }

    }


    private void startAnimation() {
        /** 设置旋转动画 */
        rotanimation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotanimation.setDuration(1000);//设置动画持续时间
        rotanimation.setRepeatCount(-1);//设置重复次数
        LinearInterpolator lir = new LinearInterpolator();
        rotanimation.setInterpolator(lir);
        jiazai_img.setAnimation(rotanimation);
    }

    public void dismiss() {
        if(null == popupWindow)return;

        if (popupWindow.isShowing()) {
            if (act instanceof HomeActivity){
                popupWindow.dismiss();
                rotanimation.cancel();
            } else {
                popupWindow.dismiss();
                rotanimation.cancel();
                backgroundAlpha((float) 1);
            }
        }

    }

    /**
    * 设置添加屏幕的背景透明度
    * @param bgAlpha
    */
    public void backgroundAlpha(float bgAlpha)
    {

        WindowManager.LayoutParams lp = act.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
       act.getWindow().setAttributes(lp);
    }


}
