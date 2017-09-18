package bbc.com.moteduan_lib2.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/6/4 0004.
 */

public class MineOrderBean {


    /**
     * traderOrder : {"lastPage":true,"pageSize":5,"pageNumber":1,"firstPage":true,"list":[{"min_age":18,"u_tall":175,"u_head_photo":"http://liemo-test.oss-cn-qingdao.aliyuncs.com/HomeImage/boy%402x.png","photos_urlc":"http://liemo-test.oss-cn-qingdao.aliyuncs.com/liemo_photos_of_type_cai/icon_chifan%402x.png","reward_price":80,"trader_hours":2,"u_nick_name":"帅威","m_sex":1,"trader_state":1,"small_of_navigation":1,"trader_order_id":"26cbc55a71e34c498a05f7c3ad05a0d1","m_head_photo":"https://liemo-test.oss-cn-qingdao.aliyuncs.com/dynamic_image/lm_img_1496473888396","u_mobile":"15515886283","neednum":1,"photos_typeb":"http://liemo-test.oss-cn-qingdao.aliyuncs.com/liemo_photos_of_type_b/img_chifan%402x.png","min_tall":160,"max_age":25,"u_weight":60,"m_id":"f7afcf2fd8224aa28344d4365aa438b3","u_sex":1,"u_age":24,"trader_money":160,"m_type":0,"subsidy":0,"adress":"绿地之窗云峰座一楼","photos_type":"http://liemo-test.oss-cn-qingdao.aliyuncs.com/liemo_photos_of_type_s/img_chifan%402x.png","end_time":"2017-06-04 24:00:00","u_rong_token":"CmIGEb5om6o+qGsQHyx15pWSHWwaGiwxqv7U9JVZS/So9R5/cdN6JhgwDi4JC5DWxla52IEb9s+sWAnjrwA6iHlsuO2TesxvoKIlGFYBGfQT3HTiLkW9/6o4fL4p2P77","max_tall":190,"start_time":"2017-06-04 22:00:00"}],"totalRow":1,"totalPage":1}
     * tips : 数据请求成功！
     * code : 200
     */

    private TraderOrderBean traderOrder;
    private String tips;
    private String code;
    private long timeStamp;

    public TraderOrderBean getTraderOrder() {
        return traderOrder;
    }

    public void setTraderOrder(TraderOrderBean traderOrder) {
        this.traderOrder = traderOrder;
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

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public static class TraderOrderBean {
        /**
         * lastPage : true
         * pageSize : 5
         * pageNumber : 1
         * firstPage : true
         * list : [{"min_age":18,"u_tall":175,"u_head_photo":"http://liemo-test.oss-cn-qingdao.aliyuncs.com/HomeImage/boy%402x.png","photos_urlc":"http://liemo-test.oss-cn-qingdao.aliyuncs.com/liemo_photos_of_type_cai/icon_chifan%402x.png","reward_price":80,"trader_hours":2,"u_nick_name":"帅威","m_sex":1,"trader_state":1,"small_of_navigation":1,"trader_order_id":"26cbc55a71e34c498a05f7c3ad05a0d1","m_head_photo":"https://liemo-test.oss-cn-qingdao.aliyuncs.com/dynamic_image/lm_img_1496473888396","u_mobile":"15515886283","neednum":1,"photos_typeb":"http://liemo-test.oss-cn-qingdao.aliyuncs.com/liemo_photos_of_type_b/img_chifan%402x.png","min_tall":160,"max_age":25,"u_weight":60,"m_id":"f7afcf2fd8224aa28344d4365aa438b3","u_sex":1,"u_age":24,"trader_money":160,"m_type":0,"subsidy":0,"adress":"绿地之窗云峰座一楼","photos_type":"http://liemo-test.oss-cn-qingdao.aliyuncs.com/liemo_photos_of_type_s/img_chifan%402x.png","end_time":"2017-06-04 24:00:00","u_rong_token":"CmIGEb5om6o+qGsQHyx15pWSHWwaGiwxqv7U9JVZS/So9R5/cdN6JhgwDi4JC5DWxla52IEb9s+sWAnjrwA6iHlsuO2TesxvoKIlGFYBGfQT3HTiLkW9/6o4fL4p2P77","max_tall":190,"start_time":"2017-06-04 22:00:00"}]
         * totalRow : 1
         * totalPage : 1
         */

        private boolean lastPage;
        private int pageSize;
        private int pageNumber;
        private boolean firstPage;
        private int totalRow;
        private int totalPage;
        private List<ListBean> list;

        public boolean isLastPage() {
            return lastPage;
        }

        public void setLastPage(boolean lastPage) {
            this.lastPage = lastPage;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public int getPageNumber() {
            return pageNumber;
        }

        public void setPageNumber(int pageNumber) {
            this.pageNumber = pageNumber;
        }

        public boolean isFirstPage() {
            return firstPage;
        }

        public void setFirstPage(boolean firstPage) {
            this.firstPage = firstPage;
        }

        public int getTotalRow() {
            return totalRow;
        }

        public void setTotalRow(int totalRow) {
            this.totalRow = totalRow;
        }

        public int getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(int totalPage) {
            this.totalPage = totalPage;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * min_age : 18
             * u_tall : 175
             * u_head_photo : http://liemo-test.oss-cn-qingdao.aliyuncs.com/HomeImage/boy%402x.png
             * photos_urlc : http://liemo-test.oss-cn-qingdao.aliyuncs.com/liemo_photos_of_type_cai/icon_chifan%402x.png
             * reward_price : 80
             * trader_hours : 2
             * u_nick_name : 帅威
             * m_sex : 1
             * trader_state : 1
             * small_of_navigation : 1
             * trader_order_id : 26cbc55a71e34c498a05f7c3ad05a0d1
             * m_head_photo : https://liemo-test.oss-cn-qingdao.aliyuncs.com/dynamic_image/lm_img_1496473888396
             * u_mobile : 15515886283
             * neednum : 1
             * photos_typeb : http://liemo-test.oss-cn-qingdao.aliyuncs.com/liemo_photos_of_type_b/img_chifan%402x.png
             * min_tall : 160
             * max_age : 25
             * u_weight : 60
             * m_id : f7afcf2fd8224aa28344d4365aa438b3
             * u_sex : 1
             * u_age : 24
             * trader_money : 160
             * m_type : 0
             * subsidy : 0
             * adress : 绿地之窗云峰座一楼
             * photos_type : http://liemo-test.oss-cn-qingdao.aliyuncs.com/liemo_photos_of_type_s/img_chifan%402x.png
             * end_time : 2017-06-04 24:00:00
             * u_rong_token : CmIGEb5om6o+qGsQHyx15pWSHWwaGiwxqv7U9JVZS/So9R5/cdN6JhgwDi4JC5DWxla52IEb9s+sWAnjrwA6iHlsuO2TesxvoKIlGFYBGfQT3HTiLkW9/6o4fL4p2P77
             * max_tall : 190
             * start_time : 2017-06-04 22:00:00
             */

            private int min_age;
            private int u_tall;
            private String u_head_photo;
            private String photos_urlc;
            private int reward_price;
            private int trader_hours;
            private String u_nick_name;
            private int m_sex;
            private int trader_state;
            private int small_of_navigation;
            private String trader_order_id;
            private String m_head_photo;
            private String u_mobile;
            private int neednum;
            private String photos_typeb;
            private int min_tall;
            private int max_age;
            private int u_weight;
            private String m_id;
            private int u_sex;
            private int u_age;
            private int trader_money;
            private int m_type;
            private int subsidy;
            private String adress;
            private String photos_type;
            private String end_time;
            private String u_rong_token;
            private int max_tall;
            private String start_time;
            private String small_navigation;

            public String getSmall_navigation() {
                return small_navigation;
            }

            public void setSmall_navigation(String small_navigation) {
                this.small_navigation = small_navigation;
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

            public int getTrader_hours() {
                return trader_hours;
            }

            public void setTrader_hours(int trader_hours) {
                this.trader_hours = trader_hours;
            }

            public String getU_nick_name() {
                return u_nick_name;
            }

            public void setU_nick_name(String u_nick_name) {
                this.u_nick_name = u_nick_name;
            }

            public int getM_sex() {
                return m_sex;
            }

            public void setM_sex(int m_sex) {
                this.m_sex = m_sex;
            }

            public int getTrader_state() {
                return trader_state;
            }

            public void setTrader_state(int trader_state) {
                this.trader_state = trader_state;
            }

            public int getSmall_of_navigation() {
                return small_of_navigation;
            }

            public void setSmall_of_navigation(int small_of_navigation) {
                this.small_of_navigation = small_of_navigation;
            }

            public String getTrader_order_id() {
                return trader_order_id;
            }

            public void setTrader_order_id(String trader_order_id) {
                this.trader_order_id = trader_order_id;
            }

            public String getM_head_photo() {
                return m_head_photo;
            }

            public void setM_head_photo(String m_head_photo) {
                this.m_head_photo = m_head_photo;
            }

            public String getU_mobile() {
                return u_mobile;
            }

            public void setU_mobile(String u_mobile) {
                this.u_mobile = u_mobile;
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

            public int getMin_tall() {
                return min_tall;
            }

            public void setMin_tall(int min_tall) {
                this.min_tall = min_tall;
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

            public String getM_id() {
                return m_id;
            }

            public void setM_id(String m_id) {
                this.m_id = m_id;
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

            public int getTrader_money() {
                return trader_money;
            }

            public void setTrader_money(int trader_money) {
                this.trader_money = trader_money;
            }

            public int getM_type() {
                return m_type;
            }

            public void setM_type(int m_type) {
                this.m_type = m_type;
            }

            public int getSubsidy() {
                return subsidy;
            }

            public void setSubsidy(int subsidy) {
                this.subsidy = subsidy;
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

            public String getEnd_time() {
                return end_time;
            }

            public void setEnd_time(String end_time) {
                this.end_time = end_time;
            }

            public String getU_rong_token() {
                return u_rong_token;
            }

            public void setU_rong_token(String u_rong_token) {
                this.u_rong_token = u_rong_token;
            }

            public int getMax_tall() {
                return max_tall;
            }

            public void setMax_tall(int max_tall) {
                this.max_tall = max_tall;
            }

            public String getStart_time() {
                return start_time;
            }

            public void setStart_time(String start_time) {
                this.start_time = start_time;
            }
        }
    }
}
