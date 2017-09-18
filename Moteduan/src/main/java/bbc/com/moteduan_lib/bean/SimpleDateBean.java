package bbc.com.moteduan_lib.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/2/22 0022.
 */
public class SimpleDateBean implements Parcelable {

    private boolean isSelected;
    private int year;
    private int month;
    private int week;
    private int day;
    private int hour;
    private int minute;
    private int second;

    public SimpleDateBean() {

    }

    protected SimpleDateBean(Parcel in) {
        isSelected = in.readByte() != 0;
        year = in.readInt();
        month = in.readInt();
        day = in.readInt();
        week = in.readInt();
        hour = in.readInt();
        minute = in.readInt();
        second = in.readInt();

    }

    public static final Creator<SimpleDateBean> CREATOR = new Creator<SimpleDateBean>() {
        @Override
        public SimpleDateBean createFromParcel(Parcel in) {
            return new SimpleDateBean(in);
        }

        @Override
        public SimpleDateBean[] newArray(int size) {
            return new SimpleDateBean[size];
        }
    };

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isSelected ? 1 : 0));
        dest.writeInt(year);
        dest.writeInt(month);
        dest.writeInt(day);
        dest.writeInt(week);
        dest.writeInt(hour);
        dest.writeInt(minute);
        dest.writeInt(second);
    }
}
