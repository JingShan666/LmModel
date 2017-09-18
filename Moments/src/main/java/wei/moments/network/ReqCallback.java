package wei.moments.network;

/**
 * Created by Administrator on 2017/4/26 0026.
 */

public interface ReqCallback {
    interface Callback<T> extends ReqCallback {
        void success(T result);
        void failed(String msg);
    }
    interface Callback1<T> extends Callback<T>{
        void completed();
    }
}
