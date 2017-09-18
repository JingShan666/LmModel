package bbc.com.moteduan_lib2.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/6/12 0012.
 */


public class AlbumBean {

    /**
     * maps : [{"ablumss":[{"send_time":"2017-06-12 10:27:27","photos_url":"https://liemo-test.oss-cn-qingdao.aliyuncs.com/dynamic_image/lm_img_1497234444276","height":"690","width":"1280","albums_id":"805175fbfe334f1eae650f1508abe9fc"}],"moth":"2017-06-12"}]
     * tips : 成功!
     * code : 200
     */

    private String tips;
    private String code;
    private List<MapsBean> maps;
    private long timeStamp;
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

    public List<MapsBean> getMaps() {
        return maps;
    }

    public void setMaps(List<MapsBean> maps) {
        this.maps = maps;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public static class MapsBean {
        /**
         * ablumss : [{"send_time":"2017-06-12 10:27:27","photos_url":"https://liemo-test.oss-cn-qingdao.aliyuncs.com/dynamic_image/lm_img_1497234444276","height":"690","width":"1280","albums_id":"805175fbfe334f1eae650f1508abe9fc"}]
         * moth : 2017-06-12
         */

        private String moth;
        private List<AblumssBean> ablumss;

        public String getMoth() {
            return moth;
        }

        public void setMoth(String moth) {
            this.moth = moth;
        }

        public List<AblumssBean> getAblumss() {
            return ablumss;
        }

        public void setAblumss(List<AblumssBean> ablumss) {
            this.ablumss = ablumss;
        }

        public static class AblumssBean {
            /**
             * send_time : 2017-06-12 10:27:27
             * photos_url : https://liemo-test.oss-cn-qingdao.aliyuncs.com/dynamic_image/lm_img_1497234444276
             * height : 690
             * width : 1280
             * albums_id : 805175fbfe334f1eae650f1508abe9fc
             */

            private String send_time;
            private String photos_url;
            private String height;
            private String width;
            private String albums_id;
            private boolean isSelected;
            private boolean isShowSelectBox;

            public boolean isSelected() {
                return isSelected;
            }

            public void setSelected(boolean selected) {
                isSelected = selected;
            }

            public boolean isShowSelectBox() {
                return isShowSelectBox;
            }

            public void setShowSelectBox(boolean showSelectBox) {
                isShowSelectBox = showSelectBox;
            }

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
    }
}
