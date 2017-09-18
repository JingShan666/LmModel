package bbc.com.moteduan_lib.bean;

/**
 * Created by Administrator on 2016/12/6 0006.
 */
public class NoticeEvery {
    private String noticecover;
    private int noticetype_iv;
     private String noticetype_tv;
    private String noticeicon;
    private String noticename;
    private String noticetime;
    private String noticelocation;
    private String noticemoney;

    public NoticeEvery(int notice_img, String noticeicon, String noticename, String noticetime, String noticelocation, String noticemoney, String uid, String inviteId, String noticecover, int noticetype_iv, String noticetype_tv) {
        this.notice_img = notice_img;
        this.noticeicon = noticeicon;
        this.noticename = noticename;
        this.noticetime = noticetime;
        this.noticelocation = noticelocation;
        this.noticemoney = noticemoney;
        this.uid = uid;
        this.inviteId = inviteId;
        this.noticecover = noticecover;
        this.noticetype_iv = noticetype_iv;
        this.noticetype_tv = noticetype_tv;
    }

    private String uid;

    public String getInviteId() {
        return inviteId;
    }

    public void setInviteId(String inviteId) {
        this.inviteId = inviteId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    private String inviteId;

    public int getNotice_img() {
        return notice_img;
    }

    public void setNotice_img(int notice_img) {
        this.notice_img = notice_img;
    }

    private int notice_img;

    public NoticeEvery() {
    }

    public NoticeEvery(String noticecover, int noticetype_iv, String noticetype_tv, String noticeicon, String noticename, String noticetime, String noticelocation, String noticemoney, int notice_img) {
        this.noticecover = noticecover;
        this.noticetype_iv = noticetype_iv;
        this.noticetype_tv = noticetype_tv;
        this.noticeicon = noticeicon;
        this.noticename = noticename;
        this.noticetime = noticetime;
        this.noticelocation = noticelocation;
        this.noticemoney = noticemoney;
        this.notice_img = notice_img;
    }

    public String getNoticecover() {
        return noticecover;
    }

    public void setNoticecover(String noticecover) {
        this.noticecover = noticecover;
    }

    public int getNoticetype_iv() {
        return noticetype_iv;
    }

    public void setNoticetype_iv(int noticetype_iv) {
        this.noticetype_iv = noticetype_iv;
    }

    public String getNoticetype_tv() {
        return noticetype_tv;
    }

    public void setNoticetype_tv(String noticetype_tv) {
        this.noticetype_tv = noticetype_tv;
    }

    public String getNoticeicon() {
        return noticeicon;
    }

    public void setNoticeicon(String noticeicon) {
        this.noticeicon = noticeicon;
    }

    public String getNoticename() {
        return noticename;
    }

    public void setNoticename(String noticename) {
        this.noticename = noticename;
    }

    public String getNoticetime() {
        return noticetime;
    }

    public void setNoticetime(String noticetime) {
        this.noticetime = noticetime;
    }

    public String getNoticelocation() {
        return noticelocation;
    }

    public void setNoticelocation(String noticelocation) {
        this.noticelocation = noticelocation;
    }

    public String getNoticemoney() {
        return noticemoney;
    }

    public void setNoticemoney(String noticemoney) {
        this.noticemoney = noticemoney;
    }
}
