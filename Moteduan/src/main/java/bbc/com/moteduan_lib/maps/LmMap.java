package bbc.com.moteduan_lib.maps;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import bbc.com.moteduan_lib.app.App;
import bbc.com.moteduan_lib.log.LogDebug;


/**
 * Created by Administrator on 2017/4/5 0005.
 * 地图定位
 */

public class LmMap {
    public static final String TAG = "LmMap";
    private static IMap map = new GaodeMap();

    private LmMap() {
    }

    public static IMap get() {
        return map;
    }

    private static class GaodeMap implements IMap, AMapLocationListener {
        private AMapLocationClient client;
        private AMapLocationClientOption option;
        private LmLocationListener listener;

        public GaodeMap() {
            client = new AMapLocationClient(App.getApp());
            option = new AMapLocationClientOption();
            option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            option.setInterval(2000);
            client.setLocationOption(option);
        }

        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation == null) {
                LogDebug.log(TAG, "aMapLocation == null");
                return;
            }
            if (aMapLocation.getErrorCode() != 0) {
                LogDebug.log(TAG, "aMapLocation.getErrorCode() == " + aMapLocation.getErrorCode() + " " + aMapLocation.getErrorInfo());
                return;
            }
            if (listener == null) {
                LogDebug.log(TAG, "listener == null");
                return;
            }
            LmLocation location = new LmLocation();
            location.setCity(aMapLocation.getCity());
            location.setLatitude(aMapLocation.getLatitude());
            location.setLongitude(aMapLocation.getLongitude());
            location.setStreet(aMapLocation.getStreet());
            location.setStreetNumber(aMapLocation.getStreetNum());
            listener.onLocationChange(location);
        }

        @Override
        public void startLocation() {
            client.stopLocation();
            client.unRegisterLocationListener(this);
            client.setLocationListener(this);
            client.startLocation();
        }

        @Override
        public void stopLocation() {
            client.stopLocation();
            client.unRegisterLocationListener(this);
            listener = null;
        }

        @Override
        public void onDestroy() {
            client.onDestroy();
        }

        @Override
        public void setLocationListener(LmLocationListener l) {
            listener = l;
        }

    }

}
