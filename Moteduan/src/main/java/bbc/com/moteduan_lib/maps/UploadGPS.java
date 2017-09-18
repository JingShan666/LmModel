package bbc.com.moteduan_lib.maps;


import com.liemo.shareresource.Url;

import org.xutils.common.Callback;

import java.util.HashMap;
import java.util.Map;

import bbc.com.moteduan_lib.network.Xutils3.Xutils;
import wei.toolkit.utils.Loger;

/**
 * Created by Administrator on 2016/7/19 0019.
 */
public class UploadGPS {
    private static final String TAG = "UploadGPS";

    public static <T> void request(String memberId, double latitude, double longitude, String city, final Callback.CommonCallback<T> callback) {
        Map<String, Object> map = new HashMap<>();
        map.put("member_id", memberId);
        map.put("gps_lat", latitude);
        map.put("gps_long", longitude);
        map.put("current_city", city);
        Xutils.post(Url.updateAddress, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (callback != null) {
                    callback.onSuccess((T) result);
                }
                Loger.log(TAG, "UploadGPS = " + result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (callback != null) {
                    callback.onError(ex, isOnCallback);
                }
                Loger.log(TAG, "UploadGPS ex = " + ex.toString());
            }

            @Override
            public void onCancelled(org.xutils.common.Callback.CancelledException cex) {
                if (callback != null) {
                    callback.onCancelled(cex);
                }
            }

            @Override
            public void onFinished() {
                if (callback != null) {
                    callback.onFinished();
                }
            }
        });
    }



}
