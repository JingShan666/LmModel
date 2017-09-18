package wei.toolkit.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2017/6/6 0006.
 */
@SuppressLint("SimpleDateFormat")
public class DateUtils {
    public static String[] weekDaysName = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};

    public static boolean isToyear(String data) {
        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);

        Calendar cal = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.setTime(date);
        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            return true;
        }
        return false;
    }

    public static boolean isToday(String day) {
        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);

        Calendar cal = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.setTime(date);
        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);
            if (diffDay == 0) {
                return true;
            }
        }
        return false;
    }

    public static boolean isTomorrow(String day) {
        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);

        Calendar cal = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(day);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.setTime(date);
        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);
            if (diffDay == 1) {
                return true;
            }
        }
        return false;
    }

    public static String getCustomFormatTime(String startTime, String endTime) {
        if (TextUtils.isEmpty(startTime) || TextUtils.isEmpty(endTime)) return "";
        StringBuilder timeBuilder = new StringBuilder();
        try {
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(startTime));

            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endTime));
            if (isToday(startTime)) {
                timeBuilder.append("今天");
            } else if (isTomorrow(startTime)) {
                timeBuilder.append("明天");
            } else {
                timeBuilder.append(startCalendar.get(Calendar.MONTH) + 1).append("-").append(startCalendar.get(Calendar.DAY_OF_MONTH));
            }
            timeBuilder
                    .append(" ")
                    .append(startCalendar.get(Calendar.HOUR_OF_DAY))
                    .append(":")
                    .append("00")
                    .append("-")
                    .append(endCalendar.get(Calendar.HOUR_OF_DAY))
                    .append(":")
                    .append("00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeBuilder.toString();
    }


    /*返回当前系统时间与传入时间的差*/

    public static long getTimeDiff(String data) {
        long result = 0;
        try {
            result = System.currentTimeMillis() - new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(data).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }


}
