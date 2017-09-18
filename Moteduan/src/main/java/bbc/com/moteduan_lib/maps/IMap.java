package bbc.com.moteduan_lib.maps;

/**
 * Created by Administrator on 2017/4/5 0005.
 */

public interface IMap {
    void startLocation();
    void stopLocation();
    void onDestroy();
    void setLocationListener(LmLocationListener listener);
}
