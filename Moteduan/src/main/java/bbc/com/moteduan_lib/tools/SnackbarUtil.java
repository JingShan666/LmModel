package bbc.com.moteduan_lib.tools;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 2017/2/20 0020.
 */
public class SnackbarUtil {


    public static void show(View view, String msg) {
        Snackbar snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_SHORT);
//        Snackbar.SnackbarLayout snackbarLayout = (Snackbar.SnackbarLayout) snackbar.getView();
//        Class c = snackbarLayout.getClass();
//        try {
//            Field field = c.getDeclaredField("mMessageView");
//            field.setAccessible(true);
//            TextView textView = (TextView) field.get(snackbarLayout);
//            textView.setTextColor(Color.WHITE);
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
        snackbar.show();
    }
}
