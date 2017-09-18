package bbc.com.moteduan_lib.tools;

import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * 通过手机传感器获取手持设备方向
 * @author Ben
 * @date 2015-8-27 下午7:52:17
 */
public class MySensorManager implements SensorEventListener {
	/** 左横屏 <--手机底部 */
	public static final int SCREEN_LEFT = 1;
	/** 正反竖屏  */
	public static final int SCREEN_VERTICAL = 2;
	/** 右横屏  手机底部--> */
	public static final int SCREEN_RIGHT = 3;

	private SensorManager mSensorManager;
	private Sensor mSensor;

	private int lastScreen;// 上次屏幕方向

	private MySensorManager.OnScreenChangeListener screenChangeListener;

	public MySensorManager(android.app.Activity activity) {
		mSensorManager = (SensorManager) activity.getSystemService(activity.SENSOR_SERVICE);
		mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);// TYPE_GRAVITY
		if (null != mSensorManager) {
			// 参数三，检测的精准度
			mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_UI);
		}
	}
	public void setOnScreenChangeListener(MySensorManager.OnScreenChangeListener listener) {
		screenChangeListener = listener;
	}

	public interface OnScreenChangeListener{
		public void onScreenChanged(int screen);
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	@Override
	public void onSensorChanged(android.hardware.SensorEvent event) {
		if (event.sensor == null) {
			return;
		}
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {// TYPE_ACCELEROMETER 一个常数描述一个加速度计传感器类型
			int x = (int) event.values[0];
			int y = (int) event.values[1];
			int z = (int) event.values[2];

			if (screenChangeListener != null) {
				int thisScreen = -1;
				if (y > -5 && y < 5 && z < 9) {
					if (x < 0) {// 右横屏-->
						thisScreen = SCREEN_RIGHT;
					} else if (x >= 0) { // 左横屏 <--
						thisScreen = SCREEN_LEFT;
					}
				} else {// 正反竖屏
					thisScreen = SCREEN_VERTICAL;
				}
				if (thisScreen != lastScreen) {
					screenChangeListener.onScreenChanged(thisScreen);
					lastScreen = thisScreen;
				}
			}
		}
	}
}
