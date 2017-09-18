package wei.moments.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/24 0024.
 */

public class MomentsMsgListBean {

    /**
     * dynamics : {"lastPage":true,"pageSize":5,"pageNumber":1,"list":[{"b_content_sex":"2","b_names":"","timeStamp":"1500464099362","dynamic_type":2,"contenting":"送花","member_id":"2071a1d849094899a04404d566947478","b_use_id":"","photos":"{\"list\":[]}","content":"新版本太惊艳了！","send_time":"2017-07-18 16:29:19","content_id":"4e59c8b75bb24ae786508c502726ec78","video_path":"","timing":"2017-07-19 19:34:59","b_head_photo":"","content_type":3,"send_addres":"","video_first_path":""},{"b_content_sex":"2","b_names":"","timeStamp":"1500446727472","dynamic_type":2,"contenting":"送花","member_id":"2071a1d849094899a04404d566947478","b_use_id":"","photos":"{\"list\":[]}","content":"新版本太惊艳了！","send_time":"2017-07-18 16:29:19","content_id":"4e59c8b75bb24ae786508c502726ec78","video_path":"","timing":"2017-07-19 14:45:27","b_head_photo":"","content_type":3,"send_addres":"","video_first_path":""},{"b_content_sex":"2","b_names":"","timeStamp":"1500446723473","dynamic_type":2,"contenting":"送花","member_id":"2071a1d849094899a04404d566947478","b_use_id":"","photos":"{\"list\":[]}","content":"新版本太惊艳了！","send_time":"2017-07-18 16:29:19","content_id":"4e59c8b75bb24ae786508c502726ec78","video_path":"","timing":"2017-07-19 14:45:24","b_head_photo":"","content_type":3,"send_addres":"","video_first_path":""},{"b_content_sex":"2","b_names":"","timeStamp":"1500366736094","dynamic_type":2,"contenting":"送花","member_id":"2071a1d849094899a04404d566947478","b_use_id":"","photos":"{\"list\":[]}","content":"新版本太惊艳了！","send_time":"2017-07-18 16:29:19","content_id":"4e59c8b75bb24ae786508c502726ec78","video_path":"","timing":"2017-07-18 16:32:16","b_head_photo":"","content_type":3,"send_addres":"","video_first_path":""},{"b_content_sex":"2","b_names":"","timeStamp":"1500366727754","dynamic_type":2,"contenting":"送花","member_id":"2071a1d849094899a04404d566947478","b_use_id":"","photos":"{\"list\":[]}","content":"新版本太惊艳了！","send_time":"2017-07-18 16:29:19","content_id":"4e59c8b75bb24ae786508c502726ec78","video_path":"","timing":"2017-07-18 16:32:08","b_head_photo":"","content_type":3,"send_addres":"","video_first_path":""}],"firstPage":true,"totalRow":5,"totalPage":1}
     * tips : 数据请求成功！
     * code : 200
     */

    private DynamicsBean dynamics;
    private String tips;
    private String code;
    private long timeStamp;
    public DynamicsBean getDynamics() {
        return dynamics;
    }

    public void setDynamics(DynamicsBean dynamics) {
        this.dynamics = dynamics;
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

    public static class DynamicsBean {
        /**
         * lastPage : true
         * pageSize : 5
         * pageNumber : 1
         * list : [{"b_content_sex":"2","b_names":"","timeStamp":"1500464099362","dynamic_type":2,"contenting":"送花","member_id":"2071a1d849094899a04404d566947478","b_use_id":"","photos":"{\"list\":[]}","content":"新版本太惊艳了！","send_time":"2017-07-18 16:29:19","content_id":"4e59c8b75bb24ae786508c502726ec78","video_path":"","timing":"2017-07-19 19:34:59","b_head_photo":"","content_type":3,"send_addres":"","video_first_path":""},{"b_content_sex":"2","b_names":"","timeStamp":"1500446727472","dynamic_type":2,"contenting":"送花","member_id":"2071a1d849094899a04404d566947478","b_use_id":"","photos":"{\"list\":[]}","content":"新版本太惊艳了！","send_time":"2017-07-18 16:29:19","content_id":"4e59c8b75bb24ae786508c502726ec78","video_path":"","timing":"2017-07-19 14:45:27","b_head_photo":"","content_type":3,"send_addres":"","video_first_path":""},{"b_content_sex":"2","b_names":"","timeStamp":"1500446723473","dynamic_type":2,"contenting":"送花","member_id":"2071a1d849094899a04404d566947478","b_use_id":"","photos":"{\"list\":[]}","content":"新版本太惊艳了！","send_time":"2017-07-18 16:29:19","content_id":"4e59c8b75bb24ae786508c502726ec78","video_path":"","timing":"2017-07-19 14:45:24","b_head_photo":"","content_type":3,"send_addres":"","video_first_path":""},{"b_content_sex":"2","b_names":"","timeStamp":"1500366736094","dynamic_type":2,"contenting":"送花","member_id":"2071a1d849094899a04404d566947478","b_use_id":"","photos":"{\"list\":[]}","content":"新版本太惊艳了！","send_time":"2017-07-18 16:29:19","content_id":"4e59c8b75bb24ae786508c502726ec78","video_path":"","timing":"2017-07-18 16:32:16","b_head_photo":"","content_type":3,"send_addres":"","video_first_path":""},{"b_content_sex":"2","b_names":"","timeStamp":"1500366727754","dynamic_type":2,"contenting":"送花","member_id":"2071a1d849094899a04404d566947478","b_use_id":"","photos":"{\"list\":[]}","content":"新版本太惊艳了！","send_time":"2017-07-18 16:29:19","content_id":"4e59c8b75bb24ae786508c502726ec78","video_path":"","timing":"2017-07-18 16:32:08","b_head_photo":"","content_type":3,"send_addres":"","video_first_path":""}]
         * firstPage : true
         * totalRow : 5
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
             * b_content_sex : 2
             * b_names :
             * timeStamp : 1500464099362
             * dynamic_type : 2
             * contenting : 送花
             * member_id : 2071a1d849094899a04404d566947478
             * b_use_id :
             * photos : {"list":[]}
             * content : 新版本太惊艳了！
             * send_time : 2017-07-18 16:29:19
             * content_id : 4e59c8b75bb24ae786508c502726ec78
             * video_path :
             * timing : 2017-07-19 19:34:59
             * b_head_photo :
             * content_type : 3
             * send_addres :
             * video_first_path :
             */

            private String b_content_sex;
            private String b_names;
            private String timeStamp;
            private int dynamic_type;
            private String contenting;
            private String member_id;
            private String b_use_id;
            private String photos;
            private String content;
            private String send_time;
            private String content_id;
            private String video_path;
            private String timing;
            private String b_head_photo;
            private int content_type;
            private String send_addres;
            private String video_first_path;

            public String getB_content_sex() {
                return b_content_sex;
            }

            public void setB_content_sex(String b_content_sex) {
                this.b_content_sex = b_content_sex;
            }

            public String getB_names() {
                return b_names;
            }

            public void setB_names(String b_names) {
                this.b_names = b_names;
            }

            public String getTimeStamp() {
                return timeStamp;
            }

            public void setTimeStamp(String timeStamp) {
                this.timeStamp = timeStamp;
            }

            public int getDynamic_type() {
                return dynamic_type;
            }

            public void setDynamic_type(int dynamic_type) {
                this.dynamic_type = dynamic_type;
            }

            public String getContenting() {
                return contenting;
            }

            public void setContenting(String contenting) {
                this.contenting = contenting;
            }

            public String getMember_id() {
                return member_id;
            }

            public void setMember_id(String member_id) {
                this.member_id = member_id;
            }

            public String getB_use_id() {
                return b_use_id;
            }

            public void setB_use_id(String b_use_id) {
                this.b_use_id = b_use_id;
            }

            public String getPhotos() {
                return photos;
            }

            public void setPhotos(String photos) {
                this.photos = photos;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
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

            public String getVideo_path() {
                return video_path;
            }

            public void setVideo_path(String video_path) {
                this.video_path = video_path;
            }

            public String getTiming() {
                return timing;
            }

            public void setTiming(String timing) {
                this.timing = timing;
            }

            public String getB_head_photo() {
                return b_head_photo;
            }

            public void setB_head_photo(String b_head_photo) {
                this.b_head_photo = b_head_photo;
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

            public String getVideo_first_path() {
                return video_first_path;
            }

            public void setVideo_first_path(String video_first_path) {
                this.video_first_path = video_first_path;
            }
        }
    }
}
