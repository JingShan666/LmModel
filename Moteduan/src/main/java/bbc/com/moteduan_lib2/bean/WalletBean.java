package bbc.com.moteduan_lib2.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/6/18 0018.
 */

public class WalletBean {

    /**
     * member_balance : 1070
     * order : {"lastPage":true,"pageSize":10,"pageNumber":1,"list":[{"order_number":"201706172307042307048752","time":"2017-06-17 23:07:04","timeStamp":"1497712024000","ids":"6130047990ba41bd9294506569a040ea","member_order_id":"6924d3bca63b40fc8cfed01020b3fb20","explans":"订单结束获取金额","money":200,"code":2,"type":2,"order_type":1,"member_id":"422037f0dfb6477d8e937bb5996e30ae"},{"order_number":"201706172304252304253088","time":"2017-06-17 23:04:25","timeStamp":"1497711865000","ids":"b6ed8c5a34ff4bd69de7679a2ade196b","member_order_id":"429fa6aad8834af2997e90824674e15b","explans":"订单结束获取金额","money":160,"code":2,"type":2,"order_type":1,"member_id":"422037f0dfb6477d8e937bb5996e30ae"},{"order_number":"65132","time":"2017-02-23 19:10:51","timeStamp":"1597667904000","ids":"10245645645","member_order_id":"10245645645","explans":"提现","money":158.4,"code":1,"type":3,"order_type":0,"member_id":"422037f0dfb6477d8e937bb5996e30ae"},{"order_number":"2313","time":"2017-02-23 21:21:44","timeStamp":"1597667904000","ids":"103456456","member_order_id":"103456456","explans":"提现","money":400,"code":1,"type":3,"order_type":0,"member_id":"422037f0dfb6477d8e937bb5996e30ae"},{"order_number":"6121361","time":"2017-02-25 00:05:59","timeStamp":"1597667904000","ids":"104456","member_order_id":"104456","explans":"提现","money":160,"code":1,"type":3,"order_type":0,"member_id":"422037f0dfb6477d8e937bb5996e30ae"},{"order_number":"13321","time":"2017-02-25 20:28:52","timeStamp":"1597667904000","ids":"105456456","member_order_id":"105456456","explans":"提现","money":384,"code":1,"type":3,"order_type":0,"member_id":"422037f0dfb6477d8e937bb5996e30ae"},{"order_number":"78132","time":"2017-02-22 13:50:39","timeStamp":"1597667904000","ids":"4564654","member_order_id":"4564654","explans":"提现","money":294.4,"code":1,"type":3,"order_type":0,"member_id":"422037f0dfb6477d8e937bb5996e30ae"},{"order_number":"123456","time":"2017-02-22 00:08:21","timeStamp":"1597667904000","ids":"545635456","member_order_id":"545635456","explans":"提现","money":432,"code":1,"type":3,"order_type":0,"member_id":"422037f0dfb6477d8e937bb5996e30ae"},{"order_number":"78132333","time":"2017-02-22 13:50:39","timeStamp":"1597667904000","ids":"sdfasdfsf23424234243wesdfdf","member_order_id":"sdfasdfsf23424234243wesdfdf","explans":"提现","money":294.4,"code":0,"type":3,"order_type":0,"member_id":"422037f0dfb6477d8e937bb5996e30ae"}],"firstPage":true,"totalRow":9,"totalPage":1}
     * tips : 请求成功！
     * code : 200
     */

    private float member_balance;
    private OrderBean order;
    private String tips;
    private String code;
    private long timeStamp;
    public float getMember_balance() {
        return member_balance;
    }

    public void setMember_balance(float member_balance) {
        this.member_balance = member_balance;
    }

    public OrderBean getOrder() {
        return order;
    }

    public void setOrder(OrderBean order) {
        this.order = order;
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

    public static class OrderBean {
        /**
         * lastPage : true
         * pageSize : 10
         * pageNumber : 1
         * list : [{"order_number":"201706172307042307048752","time":"2017-06-17 23:07:04","timeStamp":"1497712024000","ids":"6130047990ba41bd9294506569a040ea","member_order_id":"6924d3bca63b40fc8cfed01020b3fb20","explans":"订单结束获取金额","money":200,"code":2,"type":2,"order_type":1,"member_id":"422037f0dfb6477d8e937bb5996e30ae"},{"order_number":"201706172304252304253088","time":"2017-06-17 23:04:25","timeStamp":"1497711865000","ids":"b6ed8c5a34ff4bd69de7679a2ade196b","member_order_id":"429fa6aad8834af2997e90824674e15b","explans":"订单结束获取金额","money":160,"code":2,"type":2,"order_type":1,"member_id":"422037f0dfb6477d8e937bb5996e30ae"},{"order_number":"65132","time":"2017-02-23 19:10:51","timeStamp":"1597667904000","ids":"10245645645","member_order_id":"10245645645","explans":"提现","money":158.4,"code":1,"type":3,"order_type":0,"member_id":"422037f0dfb6477d8e937bb5996e30ae"},{"order_number":"2313","time":"2017-02-23 21:21:44","timeStamp":"1597667904000","ids":"103456456","member_order_id":"103456456","explans":"提现","money":400,"code":1,"type":3,"order_type":0,"member_id":"422037f0dfb6477d8e937bb5996e30ae"},{"order_number":"6121361","time":"2017-02-25 00:05:59","timeStamp":"1597667904000","ids":"104456","member_order_id":"104456","explans":"提现","money":160,"code":1,"type":3,"order_type":0,"member_id":"422037f0dfb6477d8e937bb5996e30ae"},{"order_number":"13321","time":"2017-02-25 20:28:52","timeStamp":"1597667904000","ids":"105456456","member_order_id":"105456456","explans":"提现","money":384,"code":1,"type":3,"order_type":0,"member_id":"422037f0dfb6477d8e937bb5996e30ae"},{"order_number":"78132","time":"2017-02-22 13:50:39","timeStamp":"1597667904000","ids":"4564654","member_order_id":"4564654","explans":"提现","money":294.4,"code":1,"type":3,"order_type":0,"member_id":"422037f0dfb6477d8e937bb5996e30ae"},{"order_number":"123456","time":"2017-02-22 00:08:21","timeStamp":"1597667904000","ids":"545635456","member_order_id":"545635456","explans":"提现","money":432,"code":1,"type":3,"order_type":0,"member_id":"422037f0dfb6477d8e937bb5996e30ae"},{"order_number":"78132333","time":"2017-02-22 13:50:39","timeStamp":"1597667904000","ids":"sdfasdfsf23424234243wesdfdf","member_order_id":"sdfasdfsf23424234243wesdfdf","explans":"提现","money":294.4,"code":0,"type":3,"order_type":0,"member_id":"422037f0dfb6477d8e937bb5996e30ae"}]
         * firstPage : true
         * totalRow : 9
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
             * order_number : 201706172307042307048752
             * time : 2017-06-17 23:07:04
             * timeStamp : 1497712024000
             * ids : 6130047990ba41bd9294506569a040ea
             * member_order_id : 6924d3bca63b40fc8cfed01020b3fb20
             * explans : 订单结束获取金额
             * money : 200
             * code : 2
             * type : 2
             * order_type : 1
             * member_id : 422037f0dfb6477d8e937bb5996e30ae
             */

            private String order_number;
            private String time;
            private String timeStamp;
            private String ids;
            private String member_order_id;
            private String explans;
            private float money;
            private int code;
            private int type;
            private int order_type;
            private String member_id;

            public String getOrder_number() {
                return order_number;
            }

            public void setOrder_number(String order_number) {
                this.order_number = order_number;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getTimeStamp() {
                return timeStamp;
            }

            public void setTimeStamp(String timeStamp) {
                this.timeStamp = timeStamp;
            }

            public String getIds() {
                return ids;
            }

            public void setIds(String ids) {
                this.ids = ids;
            }

            public String getMember_order_id() {
                return member_order_id;
            }

            public void setMember_order_id(String member_order_id) {
                this.member_order_id = member_order_id;
            }

            public String getExplans() {
                return explans;
            }

            public void setExplans(String explans) {
                this.explans = explans;
            }

            public float getMoney() {
                return money;
            }

            public void setMoney(float money) {
                this.money = money;
            }

            public int getCode() {
                return code;
            }

            public void setCode(int code) {
                this.code = code;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getOrder_type() {
                return order_type;
            }

            public void setOrder_type(int order_type) {
                this.order_type = order_type;
            }

            public String getMember_id() {
                return member_id;
            }

            public void setMember_id(String member_id) {
                this.member_id = member_id;
            }
        }
    }
}
