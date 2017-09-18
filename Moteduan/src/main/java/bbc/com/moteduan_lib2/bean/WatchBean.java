package bbc.com.moteduan_lib2.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/26 0026.
 */

public class WatchBean {

    /**
     * authen : {"lastPage":true,"pageSize":20,"pageNumber":1,"list":[{"u_nick_name":"打雷","u_tall":168,"u_head_photo":"http://liemo-test.oss-cn-qingdao.aliyuncs.com/iOS/1CA5337D-184E-45A4-9DA8-D0A338BB152C.jpg","u_weight":59,"u_id":"b31e236e86c44f2091052e7a51dca1f5","u_sex":1,"u_age":30}],"firstPage":true,"totalRow":1,"totalPage":1}
     * tips : 成功!
     * code : 200
     */

    private AuthenBean authen;
    private String tips;
    private String code;
    private long timeStamp;

    public AuthenBean getAuthen() {
        return authen;
    }

    public void setAuthen(AuthenBean authen) {
        this.authen = authen;
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

    public static class AuthenBean {
        /**
         * lastPage : true
         * pageSize : 20
         * pageNumber : 1
         * list : [{"u_nick_name":"打雷","u_tall":168,"u_head_photo":"http://liemo-test.oss-cn-qingdao.aliyuncs.com/iOS/1CA5337D-184E-45A4-9DA8-D0A338BB152C.jpg","u_weight":59,"u_id":"b31e236e86c44f2091052e7a51dca1f5","u_sex":1,"u_age":30}]
         * firstPage : true
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
             * u_nick_name : 打雷
             * u_tall : 168
             * u_head_photo : http://liemo-test.oss-cn-qingdao.aliyuncs.com/iOS/1CA5337D-184E-45A4-9DA8-D0A338BB152C.jpg
             * u_weight : 59
             * u_id : b31e236e86c44f2091052e7a51dca1f5
             * u_sex : 1
             * u_age : 30
             */
            private String u_nick_name;
            private int u_tall;
            private String u_head_photo;
            private int u_weight;
            private String u_id;
            private int u_sex;
            private int u_age;

            public String getU_nick_name() {
                return u_nick_name;
            }

            public void setU_nick_name(String u_nick_name) {
                this.u_nick_name = u_nick_name;
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

            public int getU_weight() {
                return u_weight;
            }

            public void setU_weight(int u_weight) {
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
        }
    }
}
