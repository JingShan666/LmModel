package bbc.com.moteduan_lib.tools;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import bbc.com.moteduan_lib.bean.SimpleDateBean;

/**
 * Created by yx on 2016/4/9.
 * 日期工具
 */
public class DateUtils {

    public static final SimpleDateFormat dateFormat_yyyy_MM_dd_HH_mm = new SimpleDateFormat("yyyy/MM/dd HH:mm");
    public static final SimpleDateFormat dateFormat_yyyy_MM_dd_HH_mm_ss = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final String yyyyMMdd_HHmmssSSSS = "yyyy_MM_dd HH:mm:ss SSSS";
    public static final String yyyyMMdd_HHmmss_SSSS = "yyyy_MM_dd'_'HH:mm:ss'_'SSSS";
    public static final String yyyy_MM_dd = "yyyy-MM-dd";
    public static final String MM_dd = "MM-dd";
    public static final String MM_dd2 = "MM月dd日";
    public static final String yyyy_MM_dd2 = "yyyy年MM月dd日";
    public static final String yyyyMMdd_HHmmss3 = "yyyy-MM-dd HH:mm:ss";
    public static final String yyyyMMdd_HHmmss2 = "yyyyMMdd_HHmmss";
    public static final String yyyyMMdd_HHmmss = "yyyy_MM_dd HH:mm:ss";
    public static final String yyyyMMdd_HHmm = "yyyy_MM_dd HH:mm";
    public static final String MMdd_HHmm = "MM_dd HH:mm";
    public static final String HH_mm_ss = "HH:mm:ss";
    public static final String HH_mm = "HH:mm";
    public static final String HH = "HH";
    public static final String mm_ss = "mm:ss";
    public static String[] weekDaysName = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
    public static String[] weekDaysCode = {"0", "1", "2", "3", "4", "5", "6"};

    public static final int TEMP_INT = 11;

    public static SimpleDateFormat getSimpleDateFormat(String format) {
        return new SimpleDateFormat(format);
    }


    /**
     *
     * @param other 正数是当天的前?天，负数则相反
     * @return
     */
    public static SimpleDateBean getOtherDayInfo(int other){
        Date date = getDateForDate(new Date(),other);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        SimpleDateBean bean = new SimpleDateBean();
        bean.setYear(year);
        bean.setDay(day);
        bean.setMonth(month);
        bean.setWeek(week);
        bean.setHour(hour);
        bean.setMinute(minute);
        bean.setSecond(second);
        return bean;
    }

    /**
     * 根据给定date 获取 days天前后日期
     *
     * @param date
     * @param days 整数 +1 表示 未来1天的日期
     * @return
     */
    public static Date getDateForDate(Date date, int days) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }

    /**
     * 将long类型时间转换为正常日期
     *
     * @param time
     * @param format "yyyy/MM/dd HH:mm:ss"
     * @return
     */
    public static String transformationDate(long time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
//        java.util.Date dt = new Date(time);
//       return sdf.format(dt);
        return sdf.format(time);
    }

    /**
     * 将long类型时间转换为正常日期
     *
     * @param date
     * @param format "yyyy/MM/dd HH:mm:ss"
     * @return
     */
    public static String transformationDate(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
//        java.util.Date dt = new Date(time);
        return sdf.format(date);
//        return sdf.format(time);
    }


    /**
     * 根据日期获得周几
     *
     * @param date
     * @return
     */
    public static String getWeekOfDate(String date) throws java.text.ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getSimpleDateFormat(yyyy_MM_dd).parse(date));
        int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return weekDaysName[intWeek];
    }

    /**
     * 根据日期获得星期
     *
     * @param date
     * @return
     */
    public static String getWeekOfDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return weekDaysName[intWeek];
    }

    /**
     * 根据日期获得星期
     *
     * @param date
     * @return
     */
    public static String getWeekOfDate(long date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return weekDaysName[intWeek];
    }

    /**
     * 获得周一的日期
     *
     * @param date
     * @return
     */
    public static String getMonday(Date date) {

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);

        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        return dateFormat_yyyy_MM_dd_HH_mm.format(calendar.getTime());

    }

    /**
     * 获得周三的日期
     *
     * @param date
     * @return
     */
    public static String getWednesday(Date date) {

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);

        calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);

        return dateFormat_yyyy_MM_dd_HH_mm.format(calendar.getTime());

    }

    /**
     * 获得周五的日期
     *
     * @param date
     * @return
     */
    public static String getFriday(Date date) {

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);

        calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);

        return dateFormat_yyyy_MM_dd_HH_mm.format(calendar.getTime());
    }

    /**
     * 当前日期前几天或者后几天的日期
     *
     * @param n
     * @return
     */
    public static String afterNDay(int n) {

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(new Date());

        calendar.add(Calendar.DATE, n);

        Date date = calendar.getTime();

        String s = dateFormat_yyyy_MM_dd_HH_mm.format(date);

        return s;

    }

    /**
     * 判断两个日期是否在同一周
     *
     * @param date1
     * @param date2
     * @return
     */
//    public static boolean isSameWeekDates(Date date1, Date date2) {
//        Calendar cal1 = Calendar.getInstance();
//        Calendar cal2 = Calendar.getInstance();
//        cal1.setTime(date1);
//        cal2.setTime(date2);
//        int subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
//        if (0 == subYear) {
//            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2
//                    .get(Calendar.WEEK_OF_YEAR))
//                return true;
//        } else if (1 == subYear && 11 == cal2.get(java.util.Calendar.MONTH)) {
//            // 如果12月的最后一周横跨来年第一周的话则最后一周即算做来年的第一周
//            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2
//                    .get(Calendar.WEEK_OF_YEAR))
//                return true;
//        } else if (-1 == subYear && 11 == cal1.get(java.util.Calendar.MONTH)) {
//            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2
//                    .get(Calendar.WEEK_OF_YEAR))
//                return true;
//        }
//        return false;
//    }

    /**
     * 判断是否在同本周
     *
     * @param date1
     * @return
     */
//    public static boolean isWeekDates(long date1) {
//       Calendar cal1 = Calendar.getInstance();
//        Calendar cal2 = Calendar.getInstance();
//        cal1.setTimeInMillis(date1);
//        int subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
//        if (0 == subYear) {
//            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
//                return true;
//        } else if (1 == subYear && 11 == cal2.get(java.util.Calendar.MONTH)) {
//            // 如果12月的最后一周横跨来年第一周的话则最后一周即算做来年的第一周
//            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
//                return true;
//        } else if (-1 == subYear && 11 == cal1.get(Calendar.MONTH)) {
//            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
//                return true;
//        }
//        return false;
//    }

    /**
     * 判断是否在同本周
     *
     * @param date1
     * @return
     */
//    public static boolean isWeekDates(Date date1) {
//        Calendar cal1 = java.util.Calendar.getInstance();
//        Calendar cal2 = java.util.Calendar.getInstance();
//        cal1.setTime(date1);
//        int subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
//        if (0 == subYear) {
//            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
//                return true;
//        } else if (1 == subYear && 11 == cal2.get(java.util.Calendar.MONTH)) {
//            // 如果12月的最后一周横跨来年第一周的话则最后一周即算做来年的第一周
//            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
//                return true;
//        } else if (-1 == subYear && 11 == cal1.get(java.util.Calendar.MONTH)) {
//            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
//                return true;
//        }
//        return false;
//    }

    /**
     * 是否当天
     *
     * @param time
     * @return
     */
    public static boolean isSameDay(long time) {

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c2.setTimeInMillis(time);
        if (c1.get(Calendar.DATE) == c2.get(Calendar.DATE)) {
            return true;
        }
        return false;
    }

    /**
     * 是否当天
     *
     * @param date
     * @return
     */
    public static boolean isSameDay(Date date) {

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c2.setTime(date);
        if (c1.get(Calendar.DATE) == c2.get(Calendar.DATE)) {
            return true;
        }
        return false;
    }

    /**
     * 是否当月
     *
     * @param time
     * @return
     */
    public static boolean isSameMonth(long time) {

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c2.setTimeInMillis(time);
        if (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)) {
            if (c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否当月
     *
     * @param date
     * @return
     */
    public static boolean isSameMonth(Date date) {

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c2.setTime(date);
        if (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)) {
            if (c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否当年
     *
     * @param time
     * @return
     */
    public static boolean isSameYear(long time) {

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c2.setTimeInMillis(time);
        if (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)) {
            return true;
        }
        return false;
    }

    /**
     * 是否当年
     *
     * @param date
     * @return
     */
    public static boolean isSameYear(Date date) {

        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c2.setTime(date);
        if (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)) {
            return true;
        }
        return false;
    }


}
