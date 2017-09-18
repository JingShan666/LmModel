package bbc.com.moteduan_lib.tools;

public class TimeDateUtils {

	public static final String dateFormat= "yyyy/MM/dd HH:mm:ss";
	public static final String EMPTY_STRING = "";

	public static java.util.Date getDate(String dataString) {
		return getDate(dataString, "yyyy-MM-dd HH:mm:ss");
	}

	public static String getCurrentDate(){
		java.text.SimpleDateFormat f = new java.text.SimpleDateFormat(dateFormat);
		return f.format(new java.util.Date());
	}

	public static java.util.Date getDate(String dataStr, String format) {
		java.text.SimpleDateFormat f = new java.text.SimpleDateFormat(format);
		java.util.Date date = null;
		try {
			date = f.parse(dataStr);
		} catch (java.text.ParseException ex) {
			ex.printStackTrace();
		}
		return date;
	}

	public static String getDateStr(String format){
		java.text.SimpleDateFormat sf = new java.text.SimpleDateFormat(format);
		java.util.Date date = new java.util.Date();
		return sf.format(date);
	}
	
	public static String formatDate(long date, String format) {
		return android.text.format.DateFormat.format(format, date).toString();
	}

	public static String formatDate(long time) {
		java.text.SimpleDateFormat sf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Date date = new java.util.Date(time);
		return sf.format(date);
	}
	

	public static long getSystemTime() {
		android.text.format.Time time = new android.text.format.Time();
		time.setToNow();
		int year = time.year;
		int month = time.month;//
		int date = time.monthDay;
		int hour = time.hour; // 0-23
		int minute = time.minute;
		int second = time.second;
		String sMonth, sDate, sHour, sMinute, sSecond;
		if (month < 10) {
			sMonth = "0" + month;
		} else {
			sMonth = "" + month;
		}
		if (date < 10) {
			sDate = "0" + date;
		} else {
			sDate = "" + date;
		}
		if (hour < 10) {
			sHour = "0" + hour;
		} else {
			sHour = "" + hour;
		}
		if (minute < 10) {
			sMinute = "0" + minute;
		} else {
			sMinute = "" + minute;
		}
		if (second < 10) {
			sSecond = "0" + second;
		} else {
			sSecond = "" + second;
		}
		long currentTime = Long.parseLong(year + sMonth + sDate + sHour
				+ sMinute + sSecond);
		return currentTime;
	}

	public static String getSystemTime(String pattern) {
		if (pattern == null || pattern.trim().length() < 5) {
			pattern = "yyyy-MM-dd HH:mm:ss";
		}
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat(pattern);
		java.util.Date curDate = new java.util.Date(System.currentTimeMillis());//
		return formatter.format(curDate);
	}
	
}
