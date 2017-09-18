package bbc.com.moteduan_lib2.home.invite;

import java.util.List;

/**
 * Created by Administrator on 2017/8/28 0028.
 */

public class InvitePageBean {

    private List<OneBean> two;
    private List<OneBean> three;
    private List<OneBean> one;
    private List<CarouselsBean> carousels;

    public List<OneBean> getTwo() {
        return two;
    }

    public void setTwo(List<OneBean> two) {
        this.two = two;
    }

    public List<OneBean> getThree() {
        return three;
    }

    public void setThree(List<OneBean> three) {
        this.three = three;
    }

    public List<OneBean> getOne() {
        return one;
    }

    public void setOne(List<OneBean> one) {
        this.one = one;
    }

    public List<CarouselsBean> getCarousels() {
        return carousels;
    }

    public void setCarousels(List<CarouselsBean> carousels) {
        this.carousels = carousels;
    }

    public static class OneBean {
        /**
         * type_of_navigation : 1
         * photos_all : http://bbc-oss1.oss-cn-beijing.aliyuncs.com/liemo-app-photos-all/img_kafei%402x.png
         * u_head_photo : http://bbc-oss1.oss-cn-beijing.aliyuncs.com/iOS/981BCCB4-4F42-4340-965E-FB11C780FB30.jpg
         * reward_price : 300
         * u_id : 9577c90bc59048e0b9f603265af8fad6
         * u_age : 35
         * u_sex : 1
         * u_nick_name : 哒哒哒哒
         * adress : 时间都去哪儿了咖啡餐厅(东站店)==郑东新区东风东路东普惠路西创业路北1幢1层101-102号
         * end_time : 2017-08-28 19:00:00
         * trader_id : c43ce68a6de64a58a3ae5aad066aec84
         * trader_city : 郑州市
         * start_time : 2017-08-28 16:00:00
         */

        private int type_of_navigation;
        private String photos_all;
        private String u_head_photo;
        private int reward_price;
        private String u_id;
        private int u_age;
        private int u_sex;
        private String u_nick_name;
        private String adress;
        private String end_time;
        private String trader_id;
        private String trader_city;
        private String start_time;
        private int k_num/*0未报名，1已报名*/;

        public int getK_num() {
            return k_num;
        }

        public void setK_num(int k_num) {
            this.k_num = k_num;
        }

        public int getType_of_navigation() {
            return type_of_navigation;
        }

        public void setType_of_navigation(int type_of_navigation) {
            this.type_of_navigation = type_of_navigation;
        }

        public String getPhotos_all() {
            return photos_all;
        }

        public void setPhotos_all(String photos_all) {
            this.photos_all = photos_all;
        }

        public String getU_head_photo() {
            return u_head_photo;
        }

        public void setU_head_photo(String u_head_photo) {
            this.u_head_photo = u_head_photo;
        }

        public int getReward_price() {
            return reward_price;
        }

        public void setReward_price(int reward_price) {
            this.reward_price = reward_price;
        }

        public String getU_id() {
            return u_id;
        }

        public void setU_id(String u_id) {
            this.u_id = u_id;
        }

        public int getU_age() {
            return u_age;
        }

        public void setU_age(int u_age) {
            this.u_age = u_age;
        }

        public int getU_sex() {
            return u_sex;
        }

        public void setU_sex(int u_sex) {
            this.u_sex = u_sex;
        }

        public String getU_nick_name() {
            return u_nick_name;
        }

        public void setU_nick_name(String u_nick_name) {
            this.u_nick_name = u_nick_name;
        }

        public String getAdress() {
            return adress;
        }

        public void setAdress(String adress) {
            this.adress = adress;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public String getTrader_id() {
            return trader_id;
        }

        public void setTrader_id(String trader_id) {
            this.trader_id = trader_id;
        }

        public String getTrader_city() {
            return trader_city;
        }

        public void setTrader_city(String trader_city) {
            this.trader_city = trader_city;
        }

        public String getStart_time() {
            return start_time;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }
    }

    public static class CarouselsBean {
        /**
         * html_url : http://app.liemoapp.com:8080/BBC/red/index.html
         * html_name : 红包
         * photo_url : http://bbc-oss1.oss-cn-beijing.aliyuncs.com/liemo-app-banner/banner.png
         * carousel_id : 60a88afd301e4287b3884897c255b7e
         * if_full : 1
         */

        private String html_url;
        private String html_name;
        private String photo_url;
        private String carousel_id;
        private int if_full;

        public String getHtml_url() {
            return html_url;
        }

        public void setHtml_url(String html_url) {
            this.html_url = html_url;
        }

        public String getHtml_name() {
            return html_name;
        }

        public void setHtml_name(String html_name) {
            this.html_name = html_name;
        }

        public String getPhoto_url() {
            return photo_url;
        }

        public void setPhoto_url(String photo_url) {
            this.photo_url = photo_url;
        }

        public String getCarousel_id() {
            return carousel_id;
        }

        public void setCarousel_id(String carousel_id) {
            this.carousel_id = carousel_id;
        }

        public int getIf_full() {
            return if_full;
        }

        public void setIf_full(int if_full) {
            this.if_full = if_full;
        }
    }
}
