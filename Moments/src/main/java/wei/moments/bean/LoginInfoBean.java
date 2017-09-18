package wei.moments.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/5/17 0017.
 */

public class LoginInfoBean implements Parcelable{
    /**
     * u_invite_code : null
     * u_tall : null
     * u_gps_long : null
     * u_head_photo : http://wx.qlogo.cn/mmopen/Q3auHgzwzM6BPOGRdqJdqNHHcrZjOBibNsEG9gcK2gbetuELIxmQDS2DgTx1H15SiaCQMAicE2NzvjSBtf1b4jujg/0
     * u_city : null
     * u_jiguang_alias : null
     * u_pwd : null
     * u_nick_name : 阿尔卑斯的幸福930
     * u_cover_video_first : null
     * u_app_state : 1
     * u_qq_account : null
     * token : 637824822820400bafa74a4b185d2968
     * u_registration_id : 13065ffa4e079c494ea
     * u_video_state : 2
     * u_wx_account : o-NbXv4y3k4uYErYzl93aLj6jK4c
     * u_mobile : 18336317215
     * u_type : 0
     * u_cover : http://wx.qlogo.cn/mmopen/Q3auHgzwzM6BPOGRdqJdqNHHcrZjOBibNsEG9gcK2gbetuELIxmQDS2DgTx1H15SiaCQMAicE2NzvjSBtf1b4jujg/0
     * u_balance : 20.0
     * u_isuse_gps : 1
     * u_weight : null
     * u_id : e37581b538cd475a9bf12d6a28fbb1b3
     * u_sex : 1
     * u_age : 22
     * u_wb_account : null
     * u_account : 600345
     * u_cover_video : https://liemo-test.oss-cn-qingdao.aliyuncs.com/dynamic_image/20170329_112130.mp4
     * u_register_date : 2017-03-08 19:21:03
     * u_rong_token : N3DeYg1kVaxtCWStPT3ZgEwJJSDByALOfYJqr9LgKKj8VIua9+ZT2+6V5d4hLUtwojxhvSDm1GHTBtm+2SQISky9PDtCGHvA57Z2m+gCX18rkTXySsOFMNnl20z478Bompg36BY2NYQ=
     * u_gps_lat : null
     * u_code : k4xxos
     * u_tag : null
     */

    private DataBean data;
    /**
     * data : {"u_invite_code":null,"u_tall":null,"u_gps_long":null,"u_head_photo":"http://wx.qlogo.cn/mmopen/Q3auHgzwzM6BPOGRdqJdqNHHcrZjOBibNsEG9gcK2gbetuELIxmQDS2DgTx1H15SiaCQMAicE2NzvjSBtf1b4jujg/0","u_city":null,"u_jiguang_alias":null,"u_pwd":null,"u_nick_name":"阿尔卑斯的幸福930","u_cover_video_first":null,"u_app_state":1,"u_qq_account":null,"token":"637824822820400bafa74a4b185d2968","u_registration_id":"13065ffa4e079c494ea","u_video_state":2,"u_wx_account":"o-NbXv4y3k4uYErYzl93aLj6jK4c","u_mobile":"18336317215","u_type":0,"u_cover":"http://wx.qlogo.cn/mmopen/Q3auHgzwzM6BPOGRdqJdqNHHcrZjOBibNsEG9gcK2gbetuELIxmQDS2DgTx1H15SiaCQMAicE2NzvjSBtf1b4jujg/0","u_balance":20,"u_isuse_gps":1,"u_weight":null,"u_id":"e37581b538cd475a9bf12d6a28fbb1b3","u_sex":1,"u_age":22,"u_wb_account":null,"u_account":600345,"u_cover_video":"https://liemo-test.oss-cn-qingdao.aliyuncs.com/dynamic_image/20170329_112130.mp4","u_register_date":"2017-03-08 19:21:03","u_rong_token":"N3DeYg1kVaxtCWStPT3ZgEwJJSDByALOfYJqr9LgKKj8VIua9+ZT2+6V5d4hLUtwojxhvSDm1GHTBtm+2SQISky9PDtCGHvA57Z2m+gCX18rkTXySsOFMNnl20z478Bompg36BY2NYQ=","u_gps_lat":null,"u_code":"k4xxos","u_tag":null}
     * tips : 校验成功！
     * code : 200
     */

    private String tips;
    private String code;

    protected LoginInfoBean(Parcel in) {
        data = in.readParcelable(DataBean.class.getClassLoader());
        tips = in.readString();
        code = in.readString();
    }

    public static final Creator<LoginInfoBean> CREATOR = new Creator<LoginInfoBean>() {
        @Override
        public LoginInfoBean createFromParcel(Parcel in) {
            return new LoginInfoBean(in);
        }

        @Override
        public LoginInfoBean[] newArray(int size) {
            return new LoginInfoBean[size];
        }
    };

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(data, flags);
        dest.writeString(tips);
        dest.writeString(code);
    }

    public static class DataBean implements Parcelable{
        private Object u_invite_code;
        private Object u_tall;
        private Object u_gps_long;
        private String u_head_photo;
        private Object u_city;
        private Object u_jiguang_alias;
        private Object u_pwd;
        private String u_nick_name;
        private Object u_cover_video_first;
        private int u_app_state;
        private Object u_qq_account;
        private String token;
        private String u_registration_id;
        private int u_video_state;
        private String u_wx_account;
        private String u_mobile;
        private int u_type;
        private String u_cover;
        private double u_balance;
        private int u_isuse_gps;
        private Object u_weight;
        private String u_id;
        private int u_sex;
        private int u_age;
        private Object u_wb_account;
        private int u_account;
        private String u_cover_video;
        private String u_register_date;
        private String u_rong_token;
        private Object u_gps_lat;
        private String u_code;
        private Object u_tag;

        protected DataBean(Parcel in) {
            u_head_photo = in.readString();
            u_nick_name = in.readString();
            u_app_state = in.readInt();
            token = in.readString();
            u_registration_id = in.readString();
            u_video_state = in.readInt();
            u_wx_account = in.readString();
            u_mobile = in.readString();
            u_type = in.readInt();
            u_cover = in.readString();
            u_balance = in.readDouble();
            u_isuse_gps = in.readInt();
            u_id = in.readString();
            u_sex = in.readInt();
            u_age = in.readInt();
            u_account = in.readInt();
            u_cover_video = in.readString();
            u_register_date = in.readString();
            u_rong_token = in.readString();
            u_code = in.readString();
        }

        public static final Creator<DataBean> CREATOR = new Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel in) {
                return new DataBean(in);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };

        public Object getU_invite_code() {
            return u_invite_code;
        }

        public void setU_invite_code(Object u_invite_code) {
            this.u_invite_code = u_invite_code;
        }

        public Object getU_tall() {
            return u_tall;
        }

        public void setU_tall(Object u_tall) {
            this.u_tall = u_tall;
        }

        public Object getU_gps_long() {
            return u_gps_long;
        }

        public void setU_gps_long(Object u_gps_long) {
            this.u_gps_long = u_gps_long;
        }

        public String getU_head_photo() {
            return u_head_photo;
        }

        public void setU_head_photo(String u_head_photo) {
            this.u_head_photo = u_head_photo;
        }

        public Object getU_city() {
            return u_city;
        }

        public void setU_city(Object u_city) {
            this.u_city = u_city;
        }

        public Object getU_jiguang_alias() {
            return u_jiguang_alias;
        }

        public void setU_jiguang_alias(Object u_jiguang_alias) {
            this.u_jiguang_alias = u_jiguang_alias;
        }

        public Object getU_pwd() {
            return u_pwd;
        }

        public void setU_pwd(Object u_pwd) {
            this.u_pwd = u_pwd;
        }

        public String getU_nick_name() {
            return u_nick_name;
        }

        public void setU_nick_name(String u_nick_name) {
            this.u_nick_name = u_nick_name;
        }

        public Object getU_cover_video_first() {
            return u_cover_video_first;
        }

        public void setU_cover_video_first(Object u_cover_video_first) {
            this.u_cover_video_first = u_cover_video_first;
        }

        public int getU_app_state() {
            return u_app_state;
        }

        public void setU_app_state(int u_app_state) {
            this.u_app_state = u_app_state;
        }

        public Object getU_qq_account() {
            return u_qq_account;
        }

        public void setU_qq_account(Object u_qq_account) {
            this.u_qq_account = u_qq_account;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getU_registration_id() {
            return u_registration_id;
        }

        public void setU_registration_id(String u_registration_id) {
            this.u_registration_id = u_registration_id;
        }

        public int getU_video_state() {
            return u_video_state;
        }

        public void setU_video_state(int u_video_state) {
            this.u_video_state = u_video_state;
        }

        public String getU_wx_account() {
            return u_wx_account;
        }

        public void setU_wx_account(String u_wx_account) {
            this.u_wx_account = u_wx_account;
        }

        public String getU_mobile() {
            return u_mobile;
        }

        public void setU_mobile(String u_mobile) {
            this.u_mobile = u_mobile;
        }

        public int getU_type() {
            return u_type;
        }

        public void setU_type(int u_type) {
            this.u_type = u_type;
        }

        public String getU_cover() {
            return u_cover;
        }

        public void setU_cover(String u_cover) {
            this.u_cover = u_cover;
        }

        public double getU_balance() {
            return u_balance;
        }

        public void setU_balance(double u_balance) {
            this.u_balance = u_balance;
        }

        public int getU_isuse_gps() {
            return u_isuse_gps;
        }

        public void setU_isuse_gps(int u_isuse_gps) {
            this.u_isuse_gps = u_isuse_gps;
        }

        public Object getU_weight() {
            return u_weight;
        }

        public void setU_weight(Object u_weight) {
            this.u_weight = u_weight;
        }

        public String getU_id() {
            return u_id;
        }

        public void setU_id(String u_id) {
            this.u_id = u_id;
        }

        public int getU_sex() {
            return u_sex;
        }

        public void setU_sex(int u_sex) {
            this.u_sex = u_sex;
        }

        public int getU_age() {
            return u_age;
        }

        public void setU_age(int u_age) {
            this.u_age = u_age;
        }

        public Object getU_wb_account() {
            return u_wb_account;
        }

        public void setU_wb_account(Object u_wb_account) {
            this.u_wb_account = u_wb_account;
        }

        public int getU_account() {
            return u_account;
        }

        public void setU_account(int u_account) {
            this.u_account = u_account;
        }

        public String getU_cover_video() {
            return u_cover_video;
        }

        public void setU_cover_video(String u_cover_video) {
            this.u_cover_video = u_cover_video;
        }

        public String getU_register_date() {
            return u_register_date;
        }

        public void setU_register_date(String u_register_date) {
            this.u_register_date = u_register_date;
        }

        public String getU_rong_token() {
            return u_rong_token;
        }

        public void setU_rong_token(String u_rong_token) {
            this.u_rong_token = u_rong_token;
        }

        public Object getU_gps_lat() {
            return u_gps_lat;
        }

        public void setU_gps_lat(Object u_gps_lat) {
            this.u_gps_lat = u_gps_lat;
        }

        public String getU_code() {
            return u_code;
        }

        public void setU_code(String u_code) {
            this.u_code = u_code;
        }

        public Object getU_tag() {
            return u_tag;
        }

        public void setU_tag(Object u_tag) {
            this.u_tag = u_tag;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(u_head_photo);
            dest.writeString(u_nick_name);
            dest.writeInt(u_app_state);
            dest.writeString(token);
            dest.writeString(u_registration_id);
            dest.writeInt(u_video_state);
            dest.writeString(u_wx_account);
            dest.writeString(u_mobile);
            dest.writeInt(u_type);
            dest.writeString(u_cover);
            dest.writeDouble(u_balance);
            dest.writeInt(u_isuse_gps);
            dest.writeString(u_id);
            dest.writeInt(u_sex);
            dest.writeInt(u_age);
            dest.writeInt(u_account);
            dest.writeString(u_cover_video);
            dest.writeString(u_register_date);
            dest.writeString(u_rong_token);
            dest.writeString(u_code);
        }
    }
}
