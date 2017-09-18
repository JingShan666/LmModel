package bbc.com.moteduan_lib2.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/5/23 0023.
 */

public class InviteBean {

    /**
     * shedule : {"lastPage":true,"pageSize":10,"pageNumber":1,"firstPage":true,"list":[{"u_nick_name":"叶空69875","adress":"心怡路与畅和街向西150米大旺城对面3楼","photos_type":"http://liemo-test.oss-cn-qingdao.aliyuncs.com/liemo_photos_of_type_s/img_taiqiu%402x.png","trader_id":"016929f8ddad4aa6bf7e379225305be3","end_time":"2017-05-24 12:00:00","u_head_photo":"http://imgsrc.baidu.com/forum/pic/item/cdca20c2d562853576fdd7e690ef76c6a6ef6376.jpg","start_time":"2017-05-24 9:00:00","reward_price":80,"u_id":"b9e1896cfc2e45bb9d4261d271eba65f","u_sex":1,"u_age":22},{"u_nick_name":"叶空69875","adress":"郑东新区东风东路东普惠路西创业路北1幢1层101号与102号之间商铺","photos_type":"http://liemo-test.oss-cn-qingdao.aliyuncs.com/liemo_photos_of_type_s/img_kafei%402x.png","trader_id":"7113415e00254245b6eb0f0adf4e1e78","end_time":"2017-05-23 24:00:00","u_head_photo":"http://imgsrc.baidu.com/forum/pic/item/cdca20c2d562853576fdd7e690ef76c6a6ef6376.jpg","start_time":"2017-05-23 22:00:00","reward_price":80,"u_id":"b9e1896cfc2e45bb9d4261d271eba65f","u_sex":1,"u_age":22}],"totalRow":2,"totalPage":1}
     * tips : 数据请求成功！
     * code : 200
     * timeStamp : 1501577700000
     */

    private SheduleBean shedule;
    private String tips;
    private String code;
    private long timeStamp;

    public SheduleBean getShedule() {
        return shedule;
    }

    public void setShedule(SheduleBean shedule) {
        this.shedule = shedule;
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

    public static class SheduleBean {
        /**
         * lastPage : true
         * pageSize : 10
         * pageNumber : 1
         * firstPage : true
         * list : [{"u_nick_name":"叶空69875","adress":"心怡路与畅和街向西150米大旺城对面3楼","photos_type":"http://liemo-test.oss-cn-qingdao.aliyuncs.com/liemo_photos_of_type_s/img_taiqiu%402x.png","trader_id":"016929f8ddad4aa6bf7e379225305be3","end_time":"2017-05-24 12:00:00","u_head_photo":"http://imgsrc.baidu.com/forum/pic/item/cdca20c2d562853576fdd7e690ef76c6a6ef6376.jpg","start_time":"2017-05-24 9:00:00","reward_price":80,"u_id":"b9e1896cfc2e45bb9d4261d271eba65f","u_sex":1,"u_age":22},{"u_nick_name":"叶空69875","adress":"郑东新区东风东路东普惠路西创业路北1幢1层101号与102号之间商铺","photos_type":"http://liemo-test.oss-cn-qingdao.aliyuncs.com/liemo_photos_of_type_s/img_kafei%402x.png","trader_id":"7113415e00254245b6eb0f0adf4e1e78","end_time":"2017-05-23 24:00:00","u_head_photo":"http://imgsrc.baidu.com/forum/pic/item/cdca20c2d562853576fdd7e690ef76c6a6ef6376.jpg","start_time":"2017-05-23 22:00:00","reward_price":80,"u_id":"b9e1896cfc2e45bb9d4261d271eba65f","u_sex":1,"u_age":22}]
         * totalRow : 2
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
             * u_nick_name : 叶空69875
             * adress : 心怡路与畅和街向西150米大旺城对面3楼
             * photos_all: http://liemo-test.oss-cn-qingdao.aliyuncs.com/liemo_photos_of_type_s/img_taiqiu%402x.png
             * trader_id : 016929f8ddad4aa6bf7e379225305be3
             * end_time : 2017-05-24 12:00:00
             * u_head_photo : http://imgsrc.baidu.com/forum/pic/item/cdca20c2d562853576fdd7e690ef76c6a6ef6376.jpg
             * start_time : 2017-05-24 9:00:00
             * reward_price : 80
             * u_id : b9e1896cfc2e45bb9d4261d271eba65f
             * u_sex : 1
             * u_age : 22
             */

            private String u_nick_name;
            private String adress;
            private String photos_all;
            private String trader_id;
            private String end_time;
            private String u_head_photo;
            private String start_time;
            private int reward_price;
            private String u_id;
            private int u_sex;
            private int u_age;
            private int k_num/*0未报名，1已报名*/;

            public int getK_num() {
                return k_num;
            }

            public void setK_num(int k_num) {
                this.k_num = k_num;
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

            public String getPhotos_all() {
                return photos_all;
            }

            public void setPhotos_all(String photos_all) {
                this.photos_all = photos_all;
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

            public String getU_head_photo() {
                return u_head_photo;
            }

            public void setU_head_photo(String u_head_photo) {
                this.u_head_photo = u_head_photo;
            }

            public String getStart_time() {
                return start_time;
            }

            public void setStart_time(String start_time) {
                this.start_time = start_time;
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
        }
    }
}
