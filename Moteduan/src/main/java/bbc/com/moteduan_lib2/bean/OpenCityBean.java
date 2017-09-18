package bbc.com.moteduan_lib2.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/8/15 0015.
 */

public class OpenCityBean {

    /**
     * opend_city : [{"Lng":113.665413,"current_city":"郑州市","Lat":34.757977,"current_time":"2017-08-01 14:43:57","opened_city_id":"1d8b281e86ee465592c7460411ed673"},{"Lng":116.405289,"current_city":"北京市","Lat":39.904987,"current_time":"2017-08-01 14:43:57","opened_city_id":"2sdf1d8b281e86ee465592c7460ed6730"},{"Lng":117.190186,"current_city":"天津市","Lat":39.125595,"current_time":"2017-08-01 14:43:57","opened_city_id":"sd2sdf1d8b281e86ee465592c7460ed67"},{"Lng":108.948021,"current_city":"西安市","Lat":34.263161,"current_time":"2017-08-01 14:43:57","opened_city_id":"sdf1d8b281e86ee465592c7460ed673"}]
     * tips : 成功
     * code : 200
     */

    private String tips;
    private String code;
    private List<OpendCityBean> opend_city;

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

    public List<OpendCityBean> getOpend_city() {
        return opend_city;
    }

    public void setOpend_city(List<OpendCityBean> opend_city) {
        this.opend_city = opend_city;
    }

    public static class OpendCityBean {
        /**
         * Lng : 113.665413
         * current_city : 郑州市
         * Lat : 34.757977
         * current_time : 2017-08-01 14:43:57
         * opened_city_id : 1d8b281e86ee465592c7460411ed673
         */

        private double Lng;
        private String current_city;
        private double Lat;
        private String current_time;
        private String opened_city_id;

        public double getLng() {
            return Lng;
        }

        public void setLng(double Lng) {
            this.Lng = Lng;
        }

        public String getCurrent_city() {
            return current_city;
        }

        public void setCurrent_city(String current_city) {
            this.current_city = current_city;
        }

        public double getLat() {
            return Lat;
        }

        public void setLat(double Lat) {
            this.Lat = Lat;
        }

        public String getCurrent_time() {
            return current_time;
        }

        public void setCurrent_time(String current_time) {
            this.current_time = current_time;
        }

        public String getOpened_city_id() {
            return opened_city_id;
        }

        public void setOpened_city_id(String opened_city_id) {
            this.opened_city_id = opened_city_id;
        }
    }
}
