package wei.moments.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;

import wei.moments.bean.LoginBean;


/**
 * Created by Administrator on 2016/6/29 0029.
 */
public class SPUtils {

    /**
     * 默认用户类型，1用户、2模特、3游客
     */
    public static final String ROLE_TYPE_DEF = "2";

    /**
     * 登录成功后返回自己的个人信息
     */
    public static final String SELF_INFO = "selfInfo";
    /**
     * 账户和密码
     */
    public static final String ACCOUNT = "account";
    /**
     * 地址
     */
    public static final String ADDRESS_DETAIL = "address";
    /**
     * 登录成功后返回自己的个人认证状态
     */
    private static Context mContext;
    public static void saveSelfInfo(Context con, String result) {
        mContext = con;
        getInfoCache().edit().putString("result", result).apply();
    }
    public static void saveSelfInfo(Context con,LoginBean loginBean) {
        String json = new Gson().toJson(loginBean);
        saveSelfInfo(con, json);
    }

    public static void saveAccount(Context context, String phtotourl, String name, String account, String sex) {
        mContext = context;
        getAccountCache().edit()
                .putString("phtotourl", phtotourl)
                .putString("name", name)
                .putString("account", account)
                .putString("sex", sex)
                .apply();

    }

    public static void saveAddress(Context context, String city, String address, double latitude, double longitude) {
        mContext = context;
        getAddressCache().edit()
                .putString("city", city)
                .putString("address", address)
                .putString("latitude", latitude + "")
                .putString("longitude", longitude + "").apply();
    }

    public static double getLatitude(Context context) {
        mContext = context;
        return Double.valueOf(getAddressCache().getString("latitude", "0.00"));
    }
    public static double getLongitude(Context context) {
        mContext = context;
        return Double.valueOf(getAddressCache().getString("longitude", "0.00"));
    }
    public static String getCity() {
        return getAddressCache().getString("city", "郑州市");
    }
    /**
     * 获取个人信息
     *
     * @param con
     * @return null 没有信息存在
     */
    public static LoginBean getSelfInfo(Context con) {
        mContext = con;
        String result = getInfoCache().getString("result", "");
        if (!TextUtils.isEmpty(result)) {
            return new Gson().fromJson(result, LoginBean.class);
        } else {
            return null;
        }
    }


    public static void saveBindingPhone(Context context, String bindingPhone) {
        LoginBean loginBean = getSelfInfo(context);
        if (loginBean == null) {
            return;
        }
        LoginBean.DataBean bean = loginBean.getData();
        if (bean == null) {
            return;
        }
        bean.setM_mobile(bindingPhone);

        String json = new Gson().toJson(loginBean);
        saveSelfInfo(context, json);
    }

    public static void saveModeAddState(Context context,int modeAddState) {
        LoginBean loginBean = getSelfInfo(context);
        if (loginBean == null) {
            return;
        }
        LoginBean.DataBean bean = loginBean.getData();
        if (bean == null) {
            return;
        }
        bean.setM_add_state(modeAddState);
        String json = new Gson().toJson(loginBean);
        saveSelfInfo(context, json);
    }

    public static void saveModelWorkState(Context context,int modeWorkState) {
        LoginBean loginBean = getSelfInfo(context);
        if (loginBean == null) {
            return;
        }
        LoginBean.DataBean bean = loginBean.getData();
        if (bean == null) {
            return;
        }
        bean.setM_work_state(modeWorkState);
        String json = new Gson().toJson(loginBean);
        saveSelfInfo(context, json);
    }

    private static SharedPreferences getInfoCache() {
        return InfoCache.infoSharedPreferences;
    }

    private static SharedPreferences getAccountCache() {
        return AccountChache.accountSharedPreferences;
    }

    private static SharedPreferences getAddressCache() {
        return AddressCache.addressSharedPreferences;
    }

    private static class InfoCache {
        private static SharedPreferences infoSharedPreferences = mContext.getSharedPreferences(SELF_INFO, Context.MODE_PRIVATE);
    }

    private static class AddressCache {
        private static SharedPreferences addressSharedPreferences = mContext.getSharedPreferences(ADDRESS_DETAIL, Context.MODE_PRIVATE);
    }

    private static class AccountChache {
        private static SharedPreferences accountSharedPreferences = mContext.getSharedPreferences(ACCOUNT, Context.MODE_PRIVATE);
    }
}
