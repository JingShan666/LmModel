package bbc.com.moteduan_lib.tools;

/**
 * Created by admin on 2016/1/11.
 */
public class ScreenUtil
{
    /**
     * 获取手机屏幕宽高
     * @param mContext
     * @return
     */
    public static int[] getScreenSize(android.content.Context mContext)
    {
        android.view.WindowManager mManager = (android.view.WindowManager)
                mContext.getSystemService(android.content.Context.WINDOW_SERVICE);
        android.util.DisplayMetrics mOutMetrics = new android.util.DisplayMetrics();
        mManager.getDefaultDisplay().getMetrics(mOutMetrics);

        return new int[]{mOutMetrics.widthPixels,mOutMetrics.heightPixels};
    }

}
