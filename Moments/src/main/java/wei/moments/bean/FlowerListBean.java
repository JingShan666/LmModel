package wei.moments.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/5/4 0004.
 */

public class FlowerListBean {

    /**
     * gift : {"lastPage":true,"pageSize":10,"pageNumber":1,"list":[{"nick_name":"阿尔卑斯的幸福930","num":1,"sex":"1","head_photo":"https://liemo-test.oss-cn-qingdao.aliyuncs.com/dynamic_image/lm_img_1497516551016","use_id":"c25ea61e1e6d42bfb725f2d59a2edd6d"},{"nick_name":"帅威","num":3,"sex":"1","head_photo":"http://liemo-test.oss-cn-qingdao.aliyuncs.com/HomeImage/boy%402x.png","use_id":"d442a623f941458fa0848f3fa2d8666f"}],"firstPage":true,"totalRow":2,"totalPage":1}
     * tips : 请求数据成功！
     * code : 200
     */

    private GiftBean gift;
    private String tips;
    private String code;
    private long timeStamp;
    public GiftBean getGift() {
        return gift;
    }

    public void setGift(GiftBean gift) {
        this.gift = gift;
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

    public static class GiftBean {
        /**
         * lastPage : true
         * pageSize : 10
         * pageNumber : 1
         * list : [{"nick_name":"阿尔卑斯的幸福930","num":1,"sex":"1","head_photo":"https://liemo-test.oss-cn-qingdao.aliyuncs.com/dynamic_image/lm_img_1497516551016","use_id":"c25ea61e1e6d42bfb725f2d59a2edd6d"},{"nick_name":"帅威","num":3,"sex":"1","head_photo":"http://liemo-test.oss-cn-qingdao.aliyuncs.com/HomeImage/boy%402x.png","use_id":"d442a623f941458fa0848f3fa2d8666f"}]
         * firstPage : true
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
             * nick_name : 阿尔卑斯的幸福930
             * num : 1
             * sex : 1
             * head_photo : https://liemo-test.oss-cn-qingdao.aliyuncs.com/dynamic_image/lm_img_1497516551016
             * use_id : c25ea61e1e6d42bfb725f2d59a2edd6d
             */

            private String nick_name;
            private int num;
            private String sex;
            private String head_photo;
            private String use_id;

            public String getNick_name() {
                return nick_name;
            }

            public void setNick_name(String nick_name) {
                this.nick_name = nick_name;
            }

            public int getNum() {
                return num;
            }

            public void setNum(int num) {
                this.num = num;
            }

            public String getSex() {
                return sex;
            }

            public void setSex(String sex) {
                this.sex = sex;
            }

            public String getHead_photo() {
                return head_photo;
            }

            public void setHead_photo(String head_photo) {
                this.head_photo = head_photo;
            }

            public String getUse_id() {
                return use_id;
            }

            public void setUse_id(String use_id) {
                this.use_id = use_id;
            }
        }
    }
}
