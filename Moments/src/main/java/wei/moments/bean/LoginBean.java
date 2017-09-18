package wei.moments.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/7/2 0002.
 */
public class LoginBean implements Parcelable {

    /**
     * data : {"type_of_navigation":null,"m_wb_account":null,"m_patner":0,"m_interest":null,"m_video_state":2,"m_rong_token":"GBJ/kWzFcOdMLWP30fLcDEwJJSDByALOfYJqr9LgKKg7IHpJo8qNpjAD9Z/z1lwlgc6o9FmDtpvDgoS/P+zmypf3GuFrV1rz7VTpB/NnVIOXETTyqyvGkWzfwYqgWmS5mpg36BY2NYQ=","m_gps_lat":"34.759415","m_pay_pw":null,"m_waist":50,"m_isuse_gps":1,"m_mobile":"13781558352","m_gps_long":"113.667218","m_out_pw":"e10adc3949ba59abbe56e057f20f883e","m_oltime":"2017-03-30 10:51:32","m_balance":9999845,"token":"b64f2d51199c48ac8b9a56d84dc77bd4","m_add_state":1,"m_sex":2,"m_register_date":"2017-03-21 15:02:38","m_jiguang_alias":null,"m_age":22,"m_invite_code":null,"m_hips":75,"m_head_photo":"https://liemo-test.oss-cn-qingdao.aliyuncs.com/dynamic_image/u=2934335366,916431300&fm=23&gp=0.jpg","m_cover_video":"https://liemo-test.oss-cn-qingdao.aliyuncs.com/dynamic_image/20170321_150349.mp4","m_signature":null,"m_pwd":"e10adc3949ba59abbe56e057f20f883e","m_tag":null,"m_identity":4,"m_id":"95f1bb08063e4e5e9a00d8288f51f32f","m_registration_id":"1507bfd3f7ca1f3f6a4","m_tall":160,"m_work_state":0,"m_weight":40,"m_bust":75,"m_type":1,"m_account":700000454,"m_wx_account":"o-NbXvyQ0LRTXmPIQziZ6k_VdO7U","m_nick_name":"！","m_model_state":1,"m_cover":"http://wx.qlogo.cn/mmopen/icYCGPejnCYHQK0wXROITa70g23BbVic9GliaeHnbuLKKKjj0rTAJxDcYmsAib5mCK4PFCPN1vRgJtnkFODZy9zVrQ/0","m_qq_account":null,"m_city":"郑州市","m_app_state":1,"m_cover_video_first":null}
     * tips : 登录成功
     * code : 200
     */

    private DataBean data;
    private String tips;
    private String code;

    protected LoginBean(Parcel in) {
        tips = in.readString();
        code = in.readString();
    }

    public static final Creator<LoginBean> CREATOR = new Creator<LoginBean>() {
        @Override
        public LoginBean createFromParcel(Parcel in) {
            return new LoginBean(in);
        }

        @Override
        public LoginBean[] newArray(int size) {
            return new LoginBean[size];
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
        dest.writeString(tips);
        dest.writeString(code);
    }

    public static class DataBean implements Parcelable{
        /**
         * type_of_navigation : null
         * m_wb_account : null
         * m_patner : 0
         * m_interest : null
         * m_video_state : 2
         * m_rong_token : GBJ/kWzFcOdMLWP30fLcDEwJJSDByALOfYJqr9LgKKg7IHpJo8qNpjAD9Z/z1lwlgc6o9FmDtpvDgoS/P+zmypf3GuFrV1rz7VTpB/NnVIOXETTyqyvGkWzfwYqgWmS5mpg36BY2NYQ=
         * m_gps_lat : 34.759415
         * m_pay_pw : null
         * m_waist : 50
         * m_isuse_gps : 1
         * m_mobile : 13781558352
         * m_gps_long : 113.667218
         * m_out_pw : e10adc3949ba59abbe56e057f20f883e
         * m_oltime : 2017-03-30 10:51:32
         * m_balance : 9999845
         * token : b64f2d51199c48ac8b9a56d84dc77bd4
         * m_add_state : 1
         * m_sex : 2
         * m_register_date : 2017-03-21 15:02:38
         * m_jiguang_alias : null
         * m_age : 22
         * m_invite_code : null
         * m_hips : 75
         * m_head_photo : https://liemo-test.oss-cn-qingdao.aliyuncs.com/dynamic_image/u=2934335366,916431300&fm=23&gp=0.jpg
         * m_cover_video : https://liemo-test.oss-cn-qingdao.aliyuncs.com/dynamic_image/20170321_150349.mp4
         * m_signature : null
         * m_pwd : e10adc3949ba59abbe56e057f20f883e
         * m_tag : null
         * m_identity : 4
         * m_id : 95f1bb08063e4e5e9a00d8288f51f32f
         * m_registration_id : 1507bfd3f7ca1f3f6a4
         * m_tall : 160
         * m_work_state : 0
         * m_weight : 40
         * m_bust : 75
         * m_type : 1
         * m_account : 700000454
         * m_wx_account : o-NbXvyQ0LRTXmPIQziZ6k_VdO7U
         * m_nick_name : ！
         * m_model_state : 1
         * m_cover : http://wx.qlogo.cn/mmopen/icYCGPejnCYHQK0wXROITa70g23BbVic9GliaeHnbuLKKKjj0rTAJxDcYmsAib5mCK4PFCPN1vRgJtnkFODZy9zVrQ/0
         * m_qq_account : null
         * m_city : 郑州市
         * m_app_state : 1
         * m_cover_video_first : null
         */

        private String type_of_navigation;
        private String m_wb_account;
        private int m_patner;
        private String m_interest;
        private int m_video_state;
        private String m_rong_token;
        private String m_gps_lat;
        private String m_pay_pw;
        private int m_waist;
        private int m_isuse_gps;
        private String m_mobile;
        private String m_gps_long;
        private String m_out_pw;
        private String m_oltime;
        private double m_balance;
        private String token;
        private int m_add_state;
        private int m_sex;
        private String m_register_date;
        private String m_jiguang_alias;
        private int m_age;
        private String m_invite_code;
        private int m_hips;
        private String m_head_photo;
        private String m_cover_video;
        private String m_signature;
        private String m_pwd;
        private String m_tag;
        private int m_identity;
        private String m_id;
        private String m_registration_id;
        private int m_tall;
        private int m_work_state;
        private int m_weight;
        private int m_bust;
        private int m_type;
        private int m_account;
        private String m_wx_account;
        private String m_nick_name;
        private int m_model_state;
        private String m_cover;
        private String m_qq_account;
        private String m_city;
        private int m_app_state;
        private String m_cover_video_first;

        protected DataBean(Parcel in) {
            type_of_navigation = in.readString();
            m_wb_account = in.readString();
            m_patner = in.readInt();
            m_interest = in.readString();
            m_video_state = in.readInt();
            m_rong_token = in.readString();
            m_gps_lat = in.readString();
            m_pay_pw = in.readString();
            m_waist = in.readInt();
            m_isuse_gps = in.readInt();
            m_mobile = in.readString();
            m_gps_long = in.readString();
            m_out_pw = in.readString();
            m_oltime = in.readString();
            m_balance = in.readDouble();
            token = in.readString();
            m_add_state = in.readInt();
            m_sex = in.readInt();
            m_register_date = in.readString();
            m_jiguang_alias = in.readString();
            m_age = in.readInt();
            m_invite_code = in.readString();
            m_hips = in.readInt();
            m_head_photo = in.readString();
            m_cover_video = in.readString();
            m_signature = in.readString();
            m_pwd = in.readString();
            m_tag = in.readString();
            m_identity = in.readInt();
            m_id = in.readString();
            m_registration_id = in.readString();
            m_tall = in.readInt();
            m_work_state = in.readInt();
            m_weight = in.readInt();
            m_bust = in.readInt();
            m_type = in.readInt();
            m_account = in.readInt();
            m_wx_account = in.readString();
            m_nick_name = in.readString();
            m_model_state = in.readInt();
            m_cover = in.readString();
            m_qq_account = in.readString();
            m_city = in.readString();
            m_app_state = in.readInt();
            m_cover_video_first = in.readString();
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

        public String getType_of_navigation() {
            return type_of_navigation;
        }

        public void setType_of_navigation(String type_of_navigation) {
            this.type_of_navigation = type_of_navigation;
        }

        public String getM_wb_account() {
            return m_wb_account;
        }

        public void setM_wb_account(String m_wb_account) {
            this.m_wb_account = m_wb_account;
        }

        public int getM_patner() {
            return m_patner;
        }

        public void setM_patner(int m_patner) {
            this.m_patner = m_patner;
        }

        public String getM_interest() {
            return m_interest;
        }

        public void setM_interest(String m_interest) {
            this.m_interest = m_interest;
        }

        public int getM_video_state() {
            return m_video_state;
        }

        public void setM_video_state(int m_video_state) {
            this.m_video_state = m_video_state;
        }

        public String getM_rong_token() {
            return m_rong_token;
        }

        public void setM_rong_token(String m_rong_token) {
            this.m_rong_token = m_rong_token;
        }

        public String getM_gps_lat() {
            return m_gps_lat;
        }

        public void setM_gps_lat(String m_gps_lat) {
            this.m_gps_lat = m_gps_lat;
        }

        public String getM_pay_pw() {
            return m_pay_pw;
        }

        public void setM_pay_pw(String m_pay_pw) {
            this.m_pay_pw = m_pay_pw;
        }

        public int getM_waist() {
            return m_waist;
        }

        public void setM_waist(int m_waist) {
            this.m_waist = m_waist;
        }

        public int getM_isuse_gps() {
            return m_isuse_gps;
        }

        public void setM_isuse_gps(int m_isuse_gps) {
            this.m_isuse_gps = m_isuse_gps;
        }

        public String getM_mobile() {
            return m_mobile;
        }

        public void setM_mobile(String m_mobile) {
            this.m_mobile = m_mobile;
        }

        public String getM_gps_long() {
            return m_gps_long;
        }

        public void setM_gps_long(String m_gps_long) {
            this.m_gps_long = m_gps_long;
        }

        public String getM_out_pw() {
            return m_out_pw;
        }

        public void setM_out_pw(String m_out_pw) {
            this.m_out_pw = m_out_pw;
        }

        public String getM_oltime() {
            return m_oltime;
        }

        public void setM_oltime(String m_oltime) {
            this.m_oltime = m_oltime;
        }

        public double getM_balance() {
            return m_balance;
        }

        public void setM_balance(double m_balance) {
            this.m_balance = m_balance;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public int getM_add_state() {
            return m_add_state;
        }

        public void setM_add_state(int m_add_state) {
            this.m_add_state = m_add_state;
        }

        public int getM_sex() {
            return m_sex;
        }

        public void setM_sex(int m_sex) {
            this.m_sex = m_sex;
        }

        public String getM_register_date() {
            return m_register_date;
        }

        public void setM_register_date(String m_register_date) {
            this.m_register_date = m_register_date;
        }

        public String getM_jiguang_alias() {
            return m_jiguang_alias;
        }

        public void setM_jiguang_alias(String m_jiguang_alias) {
            this.m_jiguang_alias = m_jiguang_alias;
        }

        public int getM_age() {
            return m_age;
        }

        public void setM_age(int m_age) {
            this.m_age = m_age;
        }

        public String getM_invite_code() {
            return m_invite_code;
        }

        public void setM_invite_code(String m_invite_code) {
            this.m_invite_code = m_invite_code;
        }

        public int getM_hips() {
            return m_hips;
        }

        public void setM_hips(int m_hips) {
            this.m_hips = m_hips;
        }

        public String getM_head_photo() {
            return m_head_photo;
        }

        public void setM_head_photo(String m_head_photo) {
            this.m_head_photo = m_head_photo;
        }

        public String getM_cover_video() {
            return m_cover_video;
        }

        public void setM_cover_video(String m_cover_video) {
            this.m_cover_video = m_cover_video;
        }

        public String getM_signature() {
            return m_signature;
        }

        public void setM_signature(String m_signature) {
            this.m_signature = m_signature;
        }

        public String getM_pwd() {
            return m_pwd;
        }

        public void setM_pwd(String m_pwd) {
            this.m_pwd = m_pwd;
        }

        public String getM_tag() {
            return m_tag;
        }

        public void setM_tag(String m_tag) {
            this.m_tag = m_tag;
        }

        public int getM_identity() {
            return m_identity;
        }

        public void setM_identity(int m_identity) {
            this.m_identity = m_identity;
        }

        public String getM_id() {
            return m_id;
        }

        public void setM_id(String m_id) {
            this.m_id = m_id;
        }

        public String getM_registration_id() {
            return m_registration_id;
        }

        public void setM_registration_id(String m_registration_id) {
            this.m_registration_id = m_registration_id;
        }

        public int getM_tall() {
            return m_tall;
        }

        public void setM_tall(int m_tall) {
            this.m_tall = m_tall;
        }

        public int getM_work_state() {
            return m_work_state;
        }

        public void setM_work_state(int m_work_state) {
            this.m_work_state = m_work_state;
        }

        public int getM_weight() {
            return m_weight;
        }

        public void setM_weight(int m_weight) {
            this.m_weight = m_weight;
        }

        public int getM_bust() {
            return m_bust;
        }

        public void setM_bust(int m_bust) {
            this.m_bust = m_bust;
        }

        public int getM_type() {
            return m_type;
        }

        public void setM_type(int m_type) {
            this.m_type = m_type;
        }

        public int getM_account() {
            return m_account;
        }

        public void setM_account(int m_account) {
            this.m_account = m_account;
        }

        public String getM_wx_account() {
            return m_wx_account;
        }

        public void setM_wx_account(String m_wx_account) {
            this.m_wx_account = m_wx_account;
        }

        public String getM_nick_name() {
            return m_nick_name;
        }

        public void setM_nick_name(String m_nick_name) {
            this.m_nick_name = m_nick_name;
        }

        public int getM_model_state() {
            return m_model_state;
        }

        public void setM_model_state(int m_model_state) {
            this.m_model_state = m_model_state;
        }

        public String getM_cover() {
            return m_cover;
        }

        public void setM_cover(String m_cover) {
            this.m_cover = m_cover;
        }

        public String getM_qq_account() {
            return m_qq_account;
        }

        public void setM_qq_account(String m_qq_account) {
            this.m_qq_account = m_qq_account;
        }

        public String getM_city() {
            return m_city;
        }

        public void setM_city(String m_city) {
            this.m_city = m_city;
        }

        public int getM_app_state() {
            return m_app_state;
        }

        public void setM_app_state(int m_app_state) {
            this.m_app_state = m_app_state;
        }

        public String getM_cover_video_first() {
            return m_cover_video_first;
        }

        public void setM_cover_video_first(String m_cover_video_first) {
            this.m_cover_video_first = m_cover_video_first;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(type_of_navigation);
            dest.writeString(m_wb_account);
            dest.writeInt(m_patner);
            dest.writeString(m_interest);
            dest.writeInt(m_video_state);
            dest.writeString(m_rong_token);
            dest.writeString(m_gps_lat);
            dest.writeString(m_pay_pw);
            dest.writeInt(m_waist);
            dest.writeInt(m_isuse_gps);
            dest.writeString(m_mobile);
            dest.writeString(m_gps_long);
            dest.writeString(m_out_pw);
            dest.writeString(m_oltime);
            dest.writeDouble(m_balance);
            dest.writeString(token);
            dest.writeInt(m_add_state);
            dest.writeInt(m_sex);
            dest.writeString(m_register_date);
            dest.writeString(m_jiguang_alias);
            dest.writeInt(m_age);
            dest.writeString(m_invite_code);
            dest.writeInt(m_hips);
            dest.writeString(m_head_photo);
            dest.writeString(m_cover_video);
            dest.writeString(m_signature);
            dest.writeString(m_pwd);
            dest.writeString(m_tag);
            dest.writeInt(m_identity);
            dest.writeString(m_id);
            dest.writeString(m_registration_id);
            dest.writeInt(m_tall);
            dest.writeInt(m_work_state);
            dest.writeInt(m_weight);
            dest.writeInt(m_bust);
            dest.writeInt(m_type);
            dest.writeInt(m_account);
            dest.writeString(m_wx_account);
            dest.writeString(m_nick_name);
            dest.writeInt(m_model_state);
            dest.writeString(m_cover);
            dest.writeString(m_qq_account);
            dest.writeString(m_city);
            dest.writeInt(m_app_state);
            dest.writeString(m_cover_video_first);
        }
    }
}
