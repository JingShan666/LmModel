package bbc.com.moteduan_lib.database;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;

import bbc.com.moteduan_lib.app.App;
import bbc.com.moteduan_lib.bean.LoginBean;
import bbc.com.moteduan_lib.log.LogDebug;
import bbc.com.moteduan_lib2.bean.InvitePushListBean;
import wei.toolkit.utils.Loger;


/**
 * Created by Administrator on 2016/6/29 0029.
 */
public class SpDataCache {
    public static final String TAG = "SpDataCache";
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
    public static final String ADDRESS_GPS = "address_gps";
    public static final String INVITE_PUSH_LIST = "invite_push_list";


    public static InvitePushListBean getInvitePushList() {
        String result = getInvitePushListCache().getString("result", "");
        LogDebug.log(TAG, "getInvitePushList " + result);
        if (!TextUtils.isEmpty(result)) {
            return new Gson().fromJson(result, InvitePushListBean.class);
        }
        return null;
    }

    public static void saveInvitePushListBean(InvitePushListBean invitePushListBean) {
        String json = new Gson().toJson(invitePushListBean);
        saveInvitePushListBean(json);

    }

    public static void saveInvitePushListBean(String json) {
        LogDebug.log(TAG, "saveInvitePushListBean " + json);
        getInvitePushListCache().edit().putString("result", json).apply();

    }

    public static void saveSelfInfo(Context context, String result) {
        getInfoCache().edit().putString("result", result).apply();
    }

    public static void saveSelfInfo(LoginBean loginBean) {
        String json = new Gson().toJson(loginBean);
        saveSelfInfo(null, json);
    }

    public static void saveAccount(Context context, String phtotourl, String name, String account, String sex) {
        getAccountCache().edit()
                .putString("phtotourl", phtotourl)
                .putString("name", name)
                .putString("account", account)
                .putString("sex", sex)
                .apply();
    }

    public static void saveAddress(Context context, String city, String address, double latitude, double longitude) {
        Loger.log(TAG, "saveAddress  city = " + city + " address = " + address + " latitude = " + latitude + " longitude = " + longitude);
        getAddressCache().edit()
                .putString("city", city)
                .putString("address", address)
                .putString("latitude", latitude + "")
                .putString("longitude", longitude + "").apply();
    }


    public static double getLatitude() {
        double latitude = Double.valueOf(getAddressCache().getString("latitude", "34.7472500000"));
        Loger.log(TAG, "getLatitude = " + latitude);
        return latitude;
    }

    public static double getLongitude() {
        double longitude = Double.valueOf(getAddressCache().getString("longitude", "113.6249300000"));
        Loger.log(TAG, "getLongitude = " + longitude);
        return longitude;
    }

    public static String getCity() {
        String city = getAddressCache().getString("city", "郑州市");
        Loger.log(TAG, "getCity = " + city);
        return city;
    }

    public static void saveGpsAddress(Context context, String city, String address, double latitude, double longitude) {
        Loger.log(TAG, "saveGpsAddress = " + city + " address = " + address + " latitude = " + latitude + " longitude = " + longitude);
        getAddressGpsCache().edit()
                .putString("city", city)
                .putString("address", address)
                .putString("latitude", latitude + "")
                .putString("longitude", longitude + "").apply();
    }

    public static double getGpsLatitude() {
        double latitude = Double.valueOf(getAddressGpsCache().getString("latitude", "34.7472500000"));
        Loger.log(TAG, "getGpsLatitude = " + latitude);
        return latitude;
    }

    public static double getGpsLongitude() {
        double longitude = Double.valueOf(getAddressGpsCache().getString("longitude", "113.6249300000"));
        Loger.log(TAG, "getGpsLongitude = " + longitude);
        return longitude;
    }

    public static String getGpsCity() {
        String city = getAddressGpsCache().getString("city", "郑州市");
        Loger.log(TAG, "getGpsCity = " + city);
        return city;
    }

    /**
     * 获取个人信息
     *
     * @param con
     * @return null 没有信息存在
     */
    public static LoginBean getSelfInfo(Context con) {
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
        saveSelfInfo(loginBean);
    }

    public static void saveModeAddState(int modeAddState) {
        LoginBean loginBean = getSelfInfo(null);
        if (loginBean == null) {
            return;
        }
        LoginBean.DataBean bean = loginBean.getData();
        if (bean == null) {
            return;
        }
        bean.setM_add_state(modeAddState);
        saveSelfInfo(loginBean);
    }

    public static void saveModelWorkState(int modeWorkState) {
        LoginBean loginBean = getSelfInfo(null);
        if (loginBean == null) {
            return;
        }
        LoginBean.DataBean bean = loginBean.getData();
        if (bean == null) {
            return;
        }
        bean.setM_work_state(modeWorkState);
        saveSelfInfo(loginBean);
    }

    public static void clean() {
        getInvitePushListCache().edit().clear().apply();
        getInfoCache().edit().clear().apply();
        getAccountCache().edit().clear().apply();
        getAddressCache().edit().clear().apply();
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

    private static SharedPreferences getAddressGpsCache() {
        return AddressGpsCache.addressGpsSharedPreferences;
    }

    private static SharedPreferences getInvitePushListCache() {
        return InvitePushListChache.invitePushListPreferences;
    }

    private static class InfoCache {
        private static SharedPreferences infoSharedPreferences = App.getApp().getSharedPreferences(SELF_INFO, Context.MODE_PRIVATE);
    }

    private static class AddressCache {
        private static SharedPreferences addressSharedPreferences = App.getApp().getSharedPreferences(ADDRESS_DETAIL, Context.MODE_PRIVATE);
    }

    private static class AddressGpsCache {
        private static SharedPreferences addressGpsSharedPreferences = App.getApp().getSharedPreferences(ADDRESS_GPS, Context.MODE_PRIVATE);
    }

    private static class AccountChache {
        private static SharedPreferences accountSharedPreferences = App.getApp().getSharedPreferences(ACCOUNT, Context.MODE_PRIVATE);
    }

    private static class InvitePushListChache {
        private static SharedPreferences invitePushListPreferences = App.getApp().getSharedPreferences(INVITE_PUSH_LIST, Context.MODE_PRIVATE);
    }
}
