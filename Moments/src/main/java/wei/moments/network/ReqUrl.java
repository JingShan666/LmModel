package wei.moments.network;


import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.Map;

import wei.toolkit.utils.Loger;


/**
 * Created by Administrator on 2017/4/26 0026.
 */

public class ReqUrl {

    public static <T> void post(String url, Map map, final ReqCallback.Callback<T> back) {
        Xutils.post(url, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (back != null) {
                    back.success((T) result);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (back != null) {
                    back.failed(ex.getMessage());
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
                if (back != null) {
                    back.failed(cex.getMessage());
                }
            }

            @Override
            public void onFinished() {
            }
        });
    }

    public static <T> void post(String url, Map map, final ReqCallback.Callback1<T> back) {
        Xutils.post(url, map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (back != null) {
                    back.success((T) result);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (back != null) {
                    back.failed(ex.getMessage());
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
                if (back != null) {
                    back.failed(cex.getMessage());
                }
            }

            @Override
            public void onFinished() {
                if (back != null) {
                    back.completed();
                }
            }
        });
    }


    private static class Xutils {
        public static <T> Callback.Cancelable post(String url, Map<String, Object> map, Callback.CommonCallback<T> callback) {
            RequestParams params = new RequestParams(url);
            params.setReadTimeout(1000 * 30);
            if (null != map) {
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    Loger.log("post", entry.getKey() + "=" + entry.getValue());
                    params.addParameter(entry.getKey(), entry.getValue());
                }
            }
            Callback.Cancelable cancelable = x.http().post(params, callback);
            return cancelable;
        }
    }


}
