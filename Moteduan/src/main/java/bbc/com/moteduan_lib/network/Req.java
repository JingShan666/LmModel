package bbc.com.moteduan_lib.network;

import org.xutils.common.Callback;

import java.util.Map;

import bbc.com.moteduan_lib.network.Xutils3.Xutils;

/**
 * Created by Administrator on 2017/5/24 0024.
 */

public class Req {
    public interface ReqCallback {
        void success(String result);
        void failed(String msg);
        void completed();
    }

    public static void post(String url, Map<String, Object> map, final ReqCallback callback) {
        Xutils.post(url, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                callback.success(result);
            }
            @Override
            public void onError(Throwable x, boolean isOnCallback) {
                callback.failed(x.getMessage());
            }
            @Override
            public void onCancelled(CancelledException cex) {
                callback.failed(cex.getMessage());
            }
            @Override
            public void onFinished() {
                callback.completed();
            }
        });
    }
}
