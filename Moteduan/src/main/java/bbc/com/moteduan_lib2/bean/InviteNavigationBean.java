package bbc.com.moteduan_lib2.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/5/24 0024.
 */

public class InviteNavigationBean {

    /**
     * navigation : [{"photos_url":null,"small_of_navigation":0,"small_navigation":"全部"},{"photos_url":"http://liemo-test.oss-cn-qingdao.aliyuncs.com/liemo_photos_of_type/icon_eat%402x.png","small_of_navigation":1,"small_navigation":"吃饭"},{"photos_url":"http://liemo-test.oss-cn-qingdao.aliyuncs.com/liemo_photos_of_type/icon_coffe%402x.png","small_of_navigation":2,"small_navigation":"咖啡"},{"photos_url":"http://liemo-test.oss-cn-qingdao.aliyuncs.com/liemo_photos_of_type/icon_ktv%402x.png","small_of_navigation":3,"small_navigation":"K歌"},{"photos_url":"http://liemo-test.oss-cn-qingdao.aliyuncs.com/liemo_photos_of_type/icon_movie%402x.png","small_of_navigation":4,"small_navigation":"电影"},{"photos_url":"http://liemo-test.oss-cn-qingdao.aliyuncs.com/liemo_photos_of_type/icon_bar%402x.png","small_of_navigation":5,"small_navigation":"酒吧"},{"photos_url":"http://liemo-test.oss-cn-qingdao.aliyuncs.com/liemo_photos_of_type/icon_chang%402x.png","small_of_navigation":6,"small_navigation":"暖场"},{"photos_url":"http://liemo-test.oss-cn-qingdao.aliyuncs.com/liemo_photos_of_type/icon_game%402x.png","small_of_navigation":7,"small_navigation":"电竞"},{"photos_url":"http://liemo-test.oss-cn-qingdao.aliyuncs.com/liemo_photos_of_type/icon_liyi%402x.png","small_of_navigation":8,"small_navigation":"礼仪"},{"photos_url":"http://liemo-test.oss-cn-qingdao.aliyuncs.com/liemo_photos_of_type/icon_ball%402x.png","small_of_navigation":9,"small_navigation":"台球"},{"photos_url":"http://liemo-test.oss-cn-qingdao.aliyuncs.com/liemo_photos_of_type/icon_tea%402x.png","small_of_navigation":10,"small_navigation":"下午茶"},{"photos_url":"http://liemo-test.oss-cn-qingdao.aliyuncs.com/liemo_photos_of_type/icon_shangfan%402x.png","small_of_navigation":11,"small_navigation":"商务饭局"},{"photos_url":"http://liemo-test.oss-cn-qingdao.aliyuncs.com/liemo_photos_of_type/icon_shangge%402x.png","small_of_navigation":12,"small_navigation":"商务歌局"},{"photos_url":"http://liemo-test.oss-cn-qingdao.aliyuncs.com/liemo_photos_of_type/icon_shangzhu%402x.png","small_of_navigation":13,"small_navigation":"商务助理"},{"photos_url":"http://liemo-test.oss-cn-qingdao.aliyuncs.com/liemo_photos_of_type/icon_yuepai%402x.png","small_of_navigation":14,"small_navigation":"约拍"},{"photos_url":"http://liemo-test.oss-cn-qingdao.aliyuncs.com/liemo_photos_of_type/icon_zouxiu%402x.png","small_of_navigation":15,"small_navigation":"走秀"},{"photos_url":"http://liemo-test.oss-cn-qingdao.aliyuncs.com/liemo_photos_of_type/icon_chezahn%402x.png","small_of_navigation":16,"small_navigation":"车展"}]
     * tips : 请求数据成功！
     * code : 200
     */

    private String tips;
    private String code;
    private List<NavigationBean> navigation;

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

    public List<NavigationBean> getNavigation() {
        return navigation;
    }

    public void setNavigation(List<NavigationBean> navigation) {
        this.navigation = navigation;
    }

    public static class NavigationBean {
        /**
         * photos_url : null
         * small_of_navigation : 0
         * small_navigation : 全部
         */

        private Object photos_url;
        private int type_of_navigation;
        private int small_of_navigation;
        private String small_navigation;

        public int getType_of_navigation() {
            return type_of_navigation;
        }
        public void setType_of_navigation(int type_of_navigation) {
            this.type_of_navigation = type_of_navigation;
        }
        public Object getPhotos_url() {
            return photos_url;
        }

        public void setPhotos_url(Object photos_url) {
            this.photos_url = photos_url;
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
