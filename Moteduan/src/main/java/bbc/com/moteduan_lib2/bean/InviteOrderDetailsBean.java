package bbc.com.moteduan_lib2.bean;

/**
 * Created by Administrator on 2017/6/14 0014.
 */

public class InviteOrderDetailsBean {

    /**
     * tips : 请求数据成功！
     * code : 200
     * trader : {"min_age":18,"u_tall":170,"u_head_photo":"https://liemo-test.oss-cn-qingdao.aliyuncs.com/dynamic_image/lm_img_1496918694381","photos_urlc":"http://liemo-test.oss-cn-qingdao.aliyuncs.com/liemo_photos_of_type_cai/icon_chifan%402x.png","reward_price":80,"u_nick_name":"阿尔卑斯的幸福930","trader_hours":2,"subsidy_price":0,"trader_id":"27ccfc5c0a4b431a93ffeafba3c41960","user_id":"c25ea61e1e6d42bfb725f2d59a2edd6d","trader_state":1,"neednum":4,"photos_typeb":"http://liemo-test.oss-cn-qingdao.aliyuncs.com/liemo_photos_of_type_b/img_chifan%402x.png","max_age":25,"u_weight":72,"min_tall":160,"u_id":"c25ea61e1e6d42bfb725f2d59a2edd6d","small_navigation":"吃饭","u_age":24,"trader_money":640,"adress":"东新区普惠路78号绿地之窗景峰座通道旁东位置(S-Z-JF-03号)==小俩口早餐快餐","photos_type":"http://liemo-test.oss-cn-qingdao.aliyuncs.com/liemo_photos_of_type_s/img_chifan%402x.png","verification":"14688","end_time":"2017-06-15 23:00:00","max_tall":190,"trader_city":"郑州市","start_time":"2017-06-15 21:00:00"}
     */

    private String tips;
    private String code;
    private TraderBean trader;

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

    public TraderBean getTrader() {
        return trader;
    }

    public void setTrader(TraderBean trader) {
        this.trader = trader;
    }

    public static class TraderBean {
        /**
         * min_age : 18
         * u_tall : 170
         * u_head_photo : https://liemo-test.oss-cn-qingdao.aliyuncs.com/dynamic_image/lm_img_1496918694381
         * photos_urlc : http://liemo-test.oss-cn-qingdao.aliyuncs.com/liemo_photos_of_type_cai/icon_chifan%402x.png
         * reward_price : 80
         * u_nick_name : 阿尔卑斯的幸福930
         * trader_hours : 2
         * subsidy_price : 0
         * trader_id : 27ccfc5c0a4b431a93ffeafba3c41960
         * user_id : c25ea61e1e6d42bfb725f2d59a2edd6d
         * trader_state : 1
         * neednum : 4
         * photos_typeb : http://liemo-test.oss-cn-qingdao.aliyuncs.com/liemo_photos_of_type_b/img_chifan%402x.png
         * max_age : 25
         * u_weight : 72
         * min_tall : 160
         * u_id : c25ea61e1e6d42bfb725f2d59a2edd6d
         * small_navigation : 吃饭
         * u_age : 24
         * trader_money : 640
         * adress : 东新区普惠路78号绿地之窗景峰座通道旁东位置(S-Z-JF-03号)==小俩口早餐快餐
         * photos_type : http://liemo-test.oss-cn-qingdao.aliyuncs.com/liemo_photos_of_type_s/img_chifan%402x.png
         * verification : 14688
         * end_time : 2017-06-15 23:00:00
         * max_tall : 190
         * trader_city : 郑州市
         * start_time : 2017-06-15 21:00:00
         */

        private int k_num;
        private String remarks;
        private int min_age;
        private int u_tall;
        private String u_head_photo;
        private String photos_urlc;
        private int reward_price;
        private String u_nick_name;
        private int trader_hours;
        private int subsidy_price;
        private String trader_id;
        private String user_id;
        private int trader_state;
        private int neednum;
        private String photos_typeb;
        private int max_age;
        private int u_weight;
        private int min_tall;
        private String u_id;
        private String small_navigation;
        private int u_age;
        private int trader_money;
        private String adress;
        private String photos_type;
        private String verification;
        private String end_time;
        private int max_tall;
        private String trader_city;
        private String start_time;

        public int getK_num() {
            return k_num;
        }

        public void setK_num(int k_num) {
            this.k_num = k_num;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public int getMin_age() {
            return min_age;
        }

        public void setMin_age(int min_age) {
            this.min_age = min_age;
        }

        public int getU_tall() {
            return u_tall;
        }

        public void setU_tall(int u_tall) {
            this.u_tall = u_tall;
        }

        public String getU_head_photo() {
            return u_head_photo;
        }

        public void setU_head_photo(String u_head_photo) {
            this.u_head_photo = u_head_photo;
        }

        public String getPhotos_urlc() {
            return photos_urlc;
        }

        public void setPhotos_urlc(String photos_urlc) {
            this.photos_urlc = photos_urlc;
        }

        public int getReward_price() {
            return reward_price;
        }

        public void setReward_price(int reward_price) {
            this.reward_price = reward_price;
        }

        public String getU_nick_name() {
            return u_nick_name;
        }

        public void setU_nick_name(String u_nick_name) {
            this.u_nick_name = u_nick_name;
        }

        public int getTrader_hours() {
            return trader_hours;
        }

        public void setTrader_hours(int trader_hours) {
            this.trader_hours = trader_hours;
        }

        public int getSubsidy_price() {
            return subsidy_price;
        }

        public void setSubsidy_price(int subsidy_price) {
            this.subsidy_price = subsidy_price;
        }

        public String getTrader_id() {
            return trader_id;
        }

        public void setTrader_id(String trader_id) {
            this.trader_id = trader_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public int getTrader_state() {
            return trader_state;
        }

        public void setTrader_state(int trader_state) {
            this.trader_state = trader_state;
        }

        public int getNeednum() {
            return neednum;
        }

        public void setNeednum(int neednum) {
            this.neednum = neednum;
        }

        public String getPhotos_typeb() {
            return photos_typeb;
        }

        public void setPhotos_typeb(String photos_typeb) {
            this.photos_typeb = photos_typeb;
        }

        public int getMax_age() {
            return max_age;
        }

        public void setMax_age(int max_age) {
            this.max_age = max_age;
        }

        public int getU_weight() {
            return u_weight;
        }

        public void setU_weight(int u_weight) {
            this.u_weight = u_weight;
        }

        public int getMin_tall() {
            return min_tall;
        }

        public void setMin_tall(int min_tall) {
            this.min_tall = min_tall;
        }

        public String getU_id() {
            return u_id;
        }

        public void setU_id(String u_id) {
            this.u_id = u_id;
        }

        public String getSmall_navigation() {
            return small_navigation;
        }

        public void setSmall_navigation(String small_navigation) {
            this.small_navigation = small_navigation;
        }

        public int getU_age() {
            return u_age;
        }

        public void setU_age(int u_age) {
            this.u_age = u_age;
        }

        public int getTrader_money() {
            return trader_money;
        }

        public void setTrader_money(int trader_money) {
            this.trader_money = trader_money;
        }

        public String getAdress() {
            return adress;
        }

        public void setAdress(String adress) {
            this.adress = adress;
        }

        public String getPhotos_type() {
            return photos_type;
        }

        public void setPhotos_type(String photos_type) {
            this.photos_type = photos_type;
        }

        public String getVerification() {
            return verification;
        }

        public void setVerification(String verification) {
            this.verification = verification;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public int getMax_tall() {
            return max_tall;
        }

        public void setMax_tall(int max_tall) {
            this.max_tall = max_tall;
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
}
