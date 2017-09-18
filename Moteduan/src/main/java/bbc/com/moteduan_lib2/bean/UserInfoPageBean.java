package bbc.com.moteduan_lib2.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/6/13 0013.
 */

public class UserInfoPageBean {


    /**
     * map : {"content":{"gift_num":0,"head_photo":"http://bbc-oss1.oss-cn-beijing.aliyuncs.com/iOS/920ECF74-A59B-4553-8A2A-99F542832EBB.jpg","secret_type":1,"pin_num":0,"content_sex":"1","content":"啦啦啦","photos":"{\"list\":[]}","send_time":"2017-09-05 10:21:35","content_id":"3c5fa988e2784d1fa56a1149fe4e8cc8","names":"首页测试","video_path":null,"zan_num":0,"content_type":3,"send_addres":"","use_id":"3195ba05f13a4a1197f2bc17c2a48dae","video_first_path":null},"two":[{"type_of_navigation":2,"adress":"精印图文(通泰路)==通泰路","photos_type":"http://bbc-oss1.oss-cn-beijing.aliyuncs.com/liemo-app-photos-of-type-s/img_chang%402x.png","trader_id":"ba56bcda9dc44b2aa94af29d11163be6","end_time":"2017-10-05 14:00:00","photos_urlc":"http://bbc-oss1.oss-cn-beijing.aliyuncs.com/liemo-app-photos-of-type-cai/icon_shangyan%402x.png","start_time":"2017-10-05 10:00:00","user_id":"3195ba05f13a4a1197f2bc17c2a48dae","reward_price":300,"small_of_navigation":6,"small_navigation":"商演"}],"attention_type":0,"ablums":[{"send_time":"2017-09-04 14:00:23","photos_url":"http://bbc-oss1.oss-cn-beijing.aliyuncs.com/iOS/CE2FE0D6-6AF5-4FA2-ACB6-DDB615463883.jpg","height":"1280","width":"720","albums_id":"373272b7ddcd4e63b10759fd295d170e"},{"send_time":"2017-09-01 15:44:54","photos_url":"http://bbc-oss1.oss-cn-beijing.aliyuncs.com/iOS/55D9DF5C-D5D8-4AFA-8106-16EC44E9DB23.jpg","height":"1136","width":"640","albums_id":"ba0284cf4e4e435d856b6d2e38374688"},{"send_time":"2017-09-01 15:39:41","photos_url":"http://bbc-oss1.oss-cn-beijing.aliyuncs.com/iOS/CF75AA7F-E343-47DF-B0D9-1880B2D2D666.jpg","height":"1136","width":"640","albums_id":"702e510d9d874d7e98c360cc681b937a"}],"one":[{"type_of_navigation":1,"adress":"许昌吴庄驴肉(宏昌街店)==通泰路与宏昌街交叉口西30米路北","photos_type":"http://bbc-oss1.oss-cn-beijing.aliyuncs.com/liemo-app-photos-of-type-s/img_chifan%402x.png","trader_id":"20cb3d0c66294722bdd05b26ad322d32","end_time":"2017-09-07 21:00:00","photos_urlc":"http://bbc-oss1.oss-cn-beijing.aliyuncs.com/liemo-app-photos-of-type-cai/icon_chifan%402x.png","start_time":"2017-09-07 19:00:00","user_id":"3195ba05f13a4a1197f2bc17c2a48dae","reward_price":600,"small_of_navigation":1,"small_navigation":"吃饭"}],"three":[{"type_of_navigation":3,"adress":"许昌吴庄驴肉(宏昌街店)==通泰路与宏昌街交叉口西30米路北","photos_type":"http://bbc-oss1.oss-cn-beijing.aliyuncs.com/liemo-app-photos-of-type-s/img_shangwufanju%402x.png","trader_id":"0c9f1ac0bd304a3ba1ff67740d3d63b9","end_time":"2017-09-07 24:00:00","photos_urlc":"http://bbc-oss1.oss-cn-beijing.aliyuncs.com/liemo-app-photos-of-type-cai/icon_shangfan%402x.png","start_time":"2017-09-07 22:00:00","user_id":"3195ba05f13a4a1197f2bc17c2a48dae","reward_price":600,"small_of_navigation":11,"small_navigation":"商务饭局"},{"type_of_navigation":3,"adress":"许昌吴庄驴肉(宏昌街店)==通泰路与宏昌街交叉口西30米路北","photos_type":"http://bbc-oss1.oss-cn-beijing.aliyuncs.com/liemo-app-photos-of-type-s/img_shangwufanju%402x.png","trader_id":"1c707d2033ff46adae014a5c789e0f3b","end_time":"2017-09-05 13:00:00","photos_urlc":"http://bbc-oss1.oss-cn-beijing.aliyuncs.com/liemo-app-photos-of-type-cai/icon_shangfan%402x.png","start_time":"2017-09-05 11:00:00","user_id":"3195ba05f13a4a1197f2bc17c2a48dae","reward_price":200,"small_of_navigation":11,"small_navigation":"商务饭局"}],"user":{"u_video_state":0,"u_tall":180,"u_head_photo":"http://bbc-oss1.oss-cn-beijing.aliyuncs.com/iOS/920ECF74-A59B-4553-8A2A-99F542832EBB.jpg","user_code":0,"u_weight":60,"u_card_state":0,"u_id":"3195ba05f13a4a1197f2bc17c2a48dae","u_age":21,"u_sex":1,"u_nick_name":"首页测试","u_cover_video":"","u_cover_video_first":"","num":2,"b_num":2}}
     * tips : 成功!
     * code : 200
     */

    private MapBean map;
    private String tips;
    private String code;

    public MapBean getMap() {
        return map;
    }

    public void setMap(MapBean map) {
        this.map = map;
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

    public static class MapBean {
        /**
         * content : {"gift_num":0,"head_photo":"http://bbc-oss1.oss-cn-beijing.aliyuncs.com/iOS/920ECF74-A59B-4553-8A2A-99F542832EBB.jpg","secret_type":1,"pin_num":0,"content_sex":"1","content":"啦啦啦","photos":"{\"list\":[]}","send_time":"2017-09-05 10:21:35","content_id":"3c5fa988e2784d1fa56a1149fe4e8cc8","names":"首页测试","video_path":null,"zan_num":0,"content_type":3,"send_addres":"","use_id":"3195ba05f13a4a1197f2bc17c2a48dae","video_first_path":null}
         * two : [{"type_of_navigation":2,"adress":"精印图文(通泰路)==通泰路","photos_type":"http://bbc-oss1.oss-cn-beijing.aliyuncs.com/liemo-app-photos-of-type-s/img_chang%402x.png","trader_id":"ba56bcda9dc44b2aa94af29d11163be6","end_time":"2017-10-05 14:00:00","photos_urlc":"http://bbc-oss1.oss-cn-beijing.aliyuncs.com/liemo-app-photos-of-type-cai/icon_shangyan%402x.png","start_time":"2017-10-05 10:00:00","user_id":"3195ba05f13a4a1197f2bc17c2a48dae","reward_price":300,"small_of_navigation":6,"small_navigation":"商演"}]
         * attention_type : 0
         * ablums : [{"send_time":"2017-09-04 14:00:23","photos_url":"http://bbc-oss1.oss-cn-beijing.aliyuncs.com/iOS/CE2FE0D6-6AF5-4FA2-ACB6-DDB615463883.jpg","height":"1280","width":"720","albums_id":"373272b7ddcd4e63b10759fd295d170e"},{"send_time":"2017-09-01 15:44:54","photos_url":"http://bbc-oss1.oss-cn-beijing.aliyuncs.com/iOS/55D9DF5C-D5D8-4AFA-8106-16EC44E9DB23.jpg","height":"1136","width":"640","albums_id":"ba0284cf4e4e435d856b6d2e38374688"},{"send_time":"2017-09-01 15:39:41","photos_url":"http://bbc-oss1.oss-cn-beijing.aliyuncs.com/iOS/CF75AA7F-E343-47DF-B0D9-1880B2D2D666.jpg","height":"1136","width":"640","albums_id":"702e510d9d874d7e98c360cc681b937a"}]
         * one : [{"type_of_navigation":1,"adress":"许昌吴庄驴肉(宏昌街店)==通泰路与宏昌街交叉口西30米路北","photos_type":"http://bbc-oss1.oss-cn-beijing.aliyuncs.com/liemo-app-photos-of-type-s/img_chifan%402x.png","trader_id":"20cb3d0c66294722bdd05b26ad322d32","end_time":"2017-09-07 21:00:00","photos_urlc":"http://bbc-oss1.oss-cn-beijing.aliyuncs.com/liemo-app-photos-of-type-cai/icon_chifan%402x.png","start_time":"2017-09-07 19:00:00","user_id":"3195ba05f13a4a1197f2bc17c2a48dae","reward_price":600,"small_of_navigation":1,"small_navigation":"吃饭"}]
         * three : [{"type_of_navigation":3,"adress":"许昌吴庄驴肉(宏昌街店)==通泰路与宏昌街交叉口西30米路北","photos_type":"http://bbc-oss1.oss-cn-beijing.aliyuncs.com/liemo-app-photos-of-type-s/img_shangwufanju%402x.png","trader_id":"0c9f1ac0bd304a3ba1ff67740d3d63b9","end_time":"2017-09-07 24:00:00","photos_urlc":"http://bbc-oss1.oss-cn-beijing.aliyuncs.com/liemo-app-photos-of-type-cai/icon_shangfan%402x.png","start_time":"2017-09-07 22:00:00","user_id":"3195ba05f13a4a1197f2bc17c2a48dae","reward_price":600,"small_of_navigation":11,"small_navigation":"商务饭局"},{"type_of_navigation":3,"adress":"许昌吴庄驴肉(宏昌街店)==通泰路与宏昌街交叉口西30米路北","photos_type":"http://bbc-oss1.oss-cn-beijing.aliyuncs.com/liemo-app-photos-of-type-s/img_shangwufanju%402x.png","trader_id":"1c707d2033ff46adae014a5c789e0f3b","end_time":"2017-09-05 13:00:00","photos_urlc":"http://bbc-oss1.oss-cn-beijing.aliyuncs.com/liemo-app-photos-of-type-cai/icon_shangfan%402x.png","start_time":"2017-09-05 11:00:00","user_id":"3195ba05f13a4a1197f2bc17c2a48dae","reward_price":200,"small_of_navigation":11,"small_navigation":"商务饭局"}]
         * user : {"u_video_state":0,"u_tall":180,"u_head_photo":"http://bbc-oss1.oss-cn-beijing.aliyuncs.com/iOS/920ECF74-A59B-4553-8A2A-99F542832EBB.jpg","user_code":0,"u_weight":60,"u_card_state":0,"u_id":"3195ba05f13a4a1197f2bc17c2a48dae","u_age":21,"u_sex":1,"u_nick_name":"首页测试","u_cover_video":"","u_cover_video_first":"","num":2,"b_num":2}
         */

        private ContentBean content;
        private int attention_type;
        private UserBean user;
        private List<OneBean> two;
        private List<AblumsBean> ablums;
        private List<OneBean> one;
        private List<OneBean> three;

        public ContentBean getContent() {
            return content;
        }

        public void setContent(ContentBean content) {
            this.content = content;
        }

        public int getAttention_type() {
            return attention_type;
        }

        public void setAttention_type(int attention_type) {
            this.attention_type = attention_type;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public List<OneBean> getTwo() {
            return two;
        }

        public void setTwo(List<OneBean> two) {
            this.two = two;
        }

        public List<AblumsBean> getAblums() {
            return ablums;
        }

        public void setAblums(List<AblumsBean> ablums) {
            this.ablums = ablums;
        }

        public List<OneBean> getOne() {
            return one;
        }
        public void setOne(List<OneBean> one) {
            this.one = one;
        }

        public List<OneBean> getThree() {
            return three;
        }

        public void setThree(List<OneBean> three) {
            this.three = three;
        }

        public static class ContentBean {
            /**
             * gift_num : 0
             * head_photo : http://bbc-oss1.oss-cn-beijing.aliyuncs.com/iOS/920ECF74-A59B-4553-8A2A-99F542832EBB.jpg
             * secret_type : 1
             * pin_num : 0
             * content_sex : 1
             * content : 啦啦啦
             * photos : {"list":[]}
             * send_time : 2017-09-05 10:21:35
             * content_id : 3c5fa988e2784d1fa56a1149fe4e8cc8
             * names : 首页测试
             * video_path : null
             * zan_num : 0
             * content_type : 3
             * send_addres :
             * use_id : 3195ba05f13a4a1197f2bc17c2a48dae
             * video_first_path : null
             */

            private int gift_num;
            private String head_photo;
            private int secret_type;
            private int pin_num;
            private String content_sex;
            private String content;
            private String photos;
            private String send_time;
            private String content_id;
            private String names;
            private Object video_path;
            private int zan_num;
            private int content_type;
            private String send_addres;
            private String use_id;
            private Object video_first_path;

            public int getGift_num() {
                return gift_num;
            }

            public void setGift_num(int gift_num) {
                this.gift_num = gift_num;
            }

            public String getHead_photo() {
                return head_photo;
            }

            public void setHead_photo(String head_photo) {
                this.head_photo = head_photo;
            }

            public int getSecret_type() {
                return secret_type;
            }

            public void setSecret_type(int secret_type) {
                this.secret_type = secret_type;
            }

            public int getPin_num() {
                return pin_num;
            }

            public void setPin_num(int pin_num) {
                this.pin_num = pin_num;
            }

            public String getContent_sex() {
                return content_sex;
            }

            public void setContent_sex(String content_sex) {
                this.content_sex = content_sex;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getPhotos() {
                return photos;
            }

            public void setPhotos(String photos) {
                this.photos = photos;
            }

            public String getSend_time() {
                return send_time;
            }

            public void setSend_time(String send_time) {
                this.send_time = send_time;
            }

            public String getContent_id() {
                return content_id;
            }

            public void setContent_id(String content_id) {
                this.content_id = content_id;
            }

            public String getNames() {
                return names;
            }

            public void setNames(String names) {
                this.names = names;
            }

            public Object getVideo_path() {
                return video_path;
            }

            public void setVideo_path(Object video_path) {
                this.video_path = video_path;
            }

            public int getZan_num() {
                return zan_num;
            }

            public void setZan_num(int zan_num) {
                this.zan_num = zan_num;
            }

            public int getContent_type() {
                return content_type;
            }

            public void setContent_type(int content_type) {
                this.content_type = content_type;
            }

            public String getSend_addres() {
                return send_addres;
            }

            public void setSend_addres(String send_addres) {
                this.send_addres = send_addres;
            }

            public String getUse_id() {
                return use_id;
            }

            public void setUse_id(String use_id) {
                this.use_id = use_id;
            }

            public Object getVideo_first_path() {
                return video_first_path;
            }

            public void setVideo_first_path(Object video_first_path) {
                this.video_first_path = video_first_path;
            }
        }

        public static class UserBean {
            /**
             * u_video_state : 0
             * u_tall : 180
             * u_head_photo : http://bbc-oss1.oss-cn-beijing.aliyuncs.com/iOS/920ECF74-A59B-4553-8A2A-99F542832EBB.jpg
             * user_code : 0
             * u_weight : 60
             * u_card_state : 0
             * u_id : 3195ba05f13a4a1197f2bc17c2a48dae
             * u_age : 21
             * u_sex : 1
             * u_nick_name : 首页测试
             * u_cover_video :
             * u_cover_video_first :
             * num : 2
             * b_num : 2
             */

            private int u_video_state;
            private int u_tall;
            private String u_head_photo;
            private int user_code;
            private int u_weight;
            private int u_card_state;
            private String u_id;
            private int u_age;
            private int u_sex;
            private String u_nick_name;
            private String u_cover_video;
            private String u_cover_video_first;
            private int num;
            private int b_num;

            public int getU_video_state() {
                return u_video_state;
            }

            public void setU_video_state(int u_video_state) {
                this.u_video_state = u_video_state;
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

            public int getUser_code() {
                return user_code;
            }

            public void setUser_code(int user_code) {
                this.user_code = user_code;
            }

            public int getU_weight() {
                return u_weight;
            }

            public void setU_weight(int u_weight) {
                this.u_weight = u_weight;
            }

            public int getU_card_state() {
                return u_card_state;
            }

            public void setU_card_state(int u_card_state) {
                this.u_card_state = u_card_state;
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

            public String getU_cover_video() {
                return u_cover_video;
            }

            public void setU_cover_video(String u_cover_video) {
                this.u_cover_video = u_cover_video;
            }

            public String getU_cover_video_first() {
                return u_cover_video_first;
            }

            public void setU_cover_video_first(String u_cover_video_first) {
                this.u_cover_video_first = u_cover_video_first;
            }

            public int getNum() {
                return num;
            }

            public void setNum(int num) {
                this.num = num;
            }

            public int getB_num() {
                return b_num;
            }

            public void setB_num(int b_num) {
                this.b_num = b_num;
            }
        }

        public static class TwoBean {
            /**
             * type_of_navigation : 2
             * adress : 精印图文(通泰路)==通泰路
             * photos_type : http://bbc-oss1.oss-cn-beijing.aliyuncs.com/liemo-app-photos-of-type-s/img_chang%402x.png
             * trader_id : ba56bcda9dc44b2aa94af29d11163be6
             * end_time : 2017-10-05 14:00:00
             * photos_urlc : http://bbc-oss1.oss-cn-beijing.aliyuncs.com/liemo-app-photos-of-type-cai/icon_shangyan%402x.png
             * start_time : 2017-10-05 10:00:00
             * user_id : 3195ba05f13a4a1197f2bc17c2a48dae
             * reward_price : 300
             * small_of_navigation : 6
             * small_navigation : 商演
             */

            private int type_of_navigation;
            private String adress;
            private String photos_type;
            private String trader_id;
            private String end_time;
            private String photos_urlc;
            private String start_time;
            private String user_id;
            private int reward_price;
            private int small_of_navigation;
            private String small_navigation;

            public int getType_of_navigation() {
                return type_of_navigation;
            }

            public void setType_of_navigation(int type_of_navigation) {
                this.type_of_navigation = type_of_navigation;
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

            public String getTrader_id() {
                return trader_id;
            }

            public void setTrader_id(String trader_id) {
                this.trader_id = trader_id;
            }

            public String getEnd_time() {
                return end_time;
            }

            public void setEnd_time(String end_time) {
                this.end_time = end_time;
            }

            public String getPhotos_urlc() {
                return photos_urlc;
            }

            public void setPhotos_urlc(String photos_urlc) {
                this.photos_urlc = photos_urlc;
            }

            public String getStart_time() {
                return start_time;
            }

            public void setStart_time(String start_time) {
                this.start_time = start_time;
            }

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public int getReward_price() {
                return reward_price;
            }

            public void setReward_price(int reward_price) {
                this.reward_price = reward_price;
            }

            public int getSmall_of_navigation() {
                return small_of_navigation;
            }

            public void setSmall_of_navigation(int small_of_navigation) {
                this.small_of_navigation = small_of_navigation;
            }

            public String getSmall_navigation() {
                return small_navigation;
            }

            public void setSmall_navigation(String small_navigation) {
                this.small_navigation = small_navigation;
            }
        }

        public static class AblumsBean {
            /**
             * send_time : 2017-09-04 14:00:23
             * photos_url : http://bbc-oss1.oss-cn-beijing.aliyuncs.com/iOS/CE2FE0D6-6AF5-4FA2-ACB6-DDB615463883.jpg
             * height : 1280
             * width : 720
             * albums_id : 373272b7ddcd4e63b10759fd295d170e
             */

            private String send_time;
            private String photos_url;
            private String height;
            private String width;
            private String albums_id;

            public String getSend_time() {
                return send_time;
            }

            public void setSend_time(String send_time) {
                this.send_time = send_time;
            }

            public String getPhotos_url() {
                return photos_url;
            }

            public void setPhotos_url(String photos_url) {
                this.photos_url = photos_url;
            }

            public String getHeight() {
                return height;
            }

            public void setHeight(String height) {
                this.height = height;
            }

            public String getWidth() {
                return width;
            }

            public void setWidth(String width) {
                this.width = width;
            }

            public String getAlbums_id() {
                return albums_id;
            }

            public void setAlbums_id(String albums_id) {
                this.albums_id = albums_id;
            }
        }

        public static class OneBean {
            /**
             * type_of_navigation : 1
             * adress : 许昌吴庄驴肉(宏昌街店)==通泰路与宏昌街交叉口西30米路北
             * photos_type : http://bbc-oss1.oss-cn-beijing.aliyuncs.com/liemo-app-photos-of-type-s/img_chifan%402x.png
             * trader_id : 20cb3d0c66294722bdd05b26ad322d32
             * end_time : 2017-09-07 21:00:00
             * photos_urlc : http://bbc-oss1.oss-cn-beijing.aliyuncs.com/liemo-app-photos-of-type-cai/icon_chifan%402x.png
             * start_time : 2017-09-07 19:00:00
             * user_id : 3195ba05f13a4a1197f2bc17c2a48dae
             * reward_price : 600
             * small_of_navigation : 1
             * small_navigation : 吃饭
             */

            private int type_of_navigation;
            private String adress;
            private String photos_type;
            private String trader_id;
            private String end_time;
            private String photos_urlc;
            private String start_time;
            private String user_id;
            private int reward_price;
            private int small_of_navigation;
            private String small_navigation;

            public int getType_of_navigation() {
                return type_of_navigation;
            }

            public void setType_of_navigation(int type_of_navigation) {
                this.type_of_navigation = type_of_navigation;
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

            public String getTrader_id() {
                return trader_id;
            }

            public void setTrader_id(String trader_id) {
                this.trader_id = trader_id;
            }

            public String getEnd_time() {
                return end_time;
            }

            public void setEnd_time(String end_time) {
                this.end_time = end_time;
            }

            public String getPhotos_urlc() {
                return photos_urlc;
            }

            public void setPhotos_urlc(String photos_urlc) {
                this.photos_urlc = photos_urlc;
            }

            public String getStart_time() {
                return start_time;
            }

            public void setStart_time(String start_time) {
                this.start_time = start_time;
            }

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public int getReward_price() {
                return reward_price;
            }

            public void setReward_price(int reward_price) {
                this.reward_price = reward_price;
            }

            public int getSmall_of_navigation() {
                return small_of_navigation;
            }

            public void setSmall_of_navigation(int small_of_navigation) {
                this.small_of_navigation = small_of_navigation;
            }

            public String getSmall_navigation() {
                return small_navigation;
            }

            public void setSmall_navigation(String small_navigation) {
                this.small_navigation = small_navigation;
            }
        }

        public static class ThreeBean {
            /**
             * type_of_navigation : 3
             * adress : 许昌吴庄驴肉(宏昌街店)==通泰路与宏昌街交叉口西30米路北
             * photos_type : http://bbc-oss1.oss-cn-beijing.aliyuncs.com/liemo-app-photos-of-type-s/img_shangwufanju%402x.png
             * trader_id : 0c9f1ac0bd304a3ba1ff67740d3d63b9
             * end_time : 2017-09-07 24:00:00
             * photos_urlc : http://bbc-oss1.oss-cn-beijing.aliyuncs.com/liemo-app-photos-of-type-cai/icon_shangfan%402x.png
             * start_time : 2017-09-07 22:00:00
             * user_id : 3195ba05f13a4a1197f2bc17c2a48dae
             * reward_price : 600
             * small_of_navigation : 11
             * small_navigation : 商务饭局
             */

            private int type_of_navigation;
            private String adress;
            private String photos_type;
            private String trader_id;
            private String end_time;
            private String photos_urlc;
            private String start_time;
            private String user_id;
            private int reward_price;
            private int small_of_navigation;
            private String small_navigation;

            public int getType_of_navigation() {
                return type_of_navigation;
            }

            public void setType_of_navigation(int type_of_navigation) {
                this.type_of_navigation = type_of_navigation;
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

            public String getTrader_id() {
                return trader_id;
            }

            public void setTrader_id(String trader_id) {
                this.trader_id = trader_id;
            }

            public String getEnd_time() {
                return end_time;
            }

            public void setEnd_time(String end_time) {
                this.end_time = end_time;
            }

            public String getPhotos_urlc() {
                return photos_urlc;
            }

            public void setPhotos_urlc(String photos_urlc) {
                this.photos_urlc = photos_urlc;
            }

            public String getStart_time() {
                return start_time;
            }

            public void setStart_time(String start_time) {
                this.start_time = start_time;
            }

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public int getReward_price() {
                return reward_price;
            }

            public void setReward_price(int reward_price) {
                this.reward_price = reward_price;
            }

            public int getSmall_of_navigation() {
                return small_of_navigation;
            }

            public void setSmall_of_navigation(int small_of_navigation) {
                this.small_of_navigation = small_of_navigation;
            }

            public String getSmall_navigation() {
                return small_navigation;
            }

            public void setSmall_navigation(String small_navigation) {
                this.small_navigation = small_navigation;
            }
        }
    }
}
