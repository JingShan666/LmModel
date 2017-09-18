package bbc.com.moteduan_lib;

import android.test.ActivityInstrumentationTestCase;

import bbc.com.moteduan_lib.login.LoginFirstActivity;

/**
 * Created by Administrator on 2017/2/16 0016.
 */
public class HomeActivityTest extends ActivityInstrumentationTestCase<LoginFirstActivity> {
    private LoginFirstActivity mHomeActivity;
    public HomeActivityTest() {
        super("bbc.com.moteduan.leftmenu", LoginFirstActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mHomeActivity = getActivity();

    }

    public void testUpdate(){
//        Class cl = mHomeActivity.getClass();
//        Method method = null;
//        try {
//            method = cl.getDeclaredMethod("rxAndroid",HomeActivity.class);
//            method.setAccessible(true);
//            method.invoke(cl.newInstance(),null);
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        }
        mHomeActivity.getCode();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
