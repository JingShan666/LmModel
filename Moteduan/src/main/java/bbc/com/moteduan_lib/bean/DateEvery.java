package bbc.com.moteduan_lib.bean;

/**
 * Created by Administrator on 2016/12/6 0006.
 */
public class DateEvery {

    private String datecover;
    private int datetype_iv;

    public int getDateType_img() {
        return dateType_img;
    }

    public void setDateType_img(int dateType_img) {
        this.dateType_img = dateType_img;
    }

    private int dateType_img;
    private String datetype_tv;
    private String datenickname;
    private int datesex;
    private String datetime;
    private String datelocation;
    private String datemoney;
    private String dateicon;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getInviteId() {
        return inviteId;
    }

    public void setInviteId(String inviteId) {
        this.inviteId = inviteId;
    }

    private String uid;
    private String inviteId;

    public DateEvery(String datecover, int datetype_iv, int dateType_img, String datetype_tv, String datenickname, int datesex, String datetime, String datelocation, String datemoney, String dateicon, String uid, String inviteId, String age) {
        this.datecover = datecover;
        this.datetype_iv = datetype_iv;
        this.dateType_img = dateType_img;
        this.datetype_tv = datetype_tv;
        this.datenickname = datenickname;
        this.datesex = datesex;
        this.datetime = datetime;
        this.datelocation = datelocation;
        this.datemoney = datemoney;
        this.dateicon = dateicon;
        this.uid = uid;
        this.inviteId = inviteId;
        this.age = age;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    private String age;


    public DateEvery(String datecover, int datetype_iv, int dateType_img, String datetype_tv, String datenickname, int datesex, String datetime, String datelocation, String datemoney, String dateicon, String age) {
        this.datecover = datecover;
        this.datetype_iv = datetype_iv;
        this.dateType_img = dateType_img;
        this.datetype_tv = datetype_tv;
        this.datenickname = datenickname;
        this.datesex = datesex;
        this.datetime = datetime;
        this.datelocation = datelocation;
        this.datemoney = datemoney;
        this.dateicon = dateicon;
        this.age = age;
    }

    public DateEvery() {
    }

    public String getDatecover() {
        return datecover;
    }

    public void setDatecover(String datecover) {
        this.datecover = datecover;
    }

    public int getDatetype_iv() {
        return datetype_iv;
    }

    public void setDatetype_iv(int datetype_iv) {
        this.datetype_iv = datetype_iv;
    }

    public String getDatetype_tv() {
        return datetype_tv;
    }

    public void setDatetype_tv(String datetype_tv) {
        this.datetype_tv = datetype_tv;
    }

    public String getDatenickname() {
        return datenickname;
    }

    public void setDatenickname(String datenickname) {
        this.datenickname = datenickname;
    }

    public int getDatesex() {
        return datesex;
    }

    public void setDatesex(int datesex) {
        this.datesex = datesex;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getDatelocation() {
        return datelocation;
    }

    public void setDatelocation(String datelocation) {
        this.datelocation = datelocation;
    }

    public String getDatemoney() {
        return datemoney;
    }

    public void setDatemoney(String datemoney) {
        this.datemoney = datemoney;
    }

    public String getDateicon() {
        return dateicon;
    }

    public void setDateicon(String dateicon) {
        this.dateicon = dateicon;
    }
}
