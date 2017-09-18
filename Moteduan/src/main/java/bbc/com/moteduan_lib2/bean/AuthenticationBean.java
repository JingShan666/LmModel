package bbc.com.moteduan_lib2.bean;

/**
 * Created by Administrator on 2017/6/26 0026.
 */

public class AuthenticationBean {

    /**
     * authen : {"auth_model_card_state":2,"auth_video_first":"https://liemo-test.oss-cn-qingdao.aliyuncs.com/dynamic_image/lm_img_1496927171654","auth_model_live":"https://liemo-test.oss-cn-qingdao.aliyuncs.com/dynamic_image/lm_img_1496927146309","auth_video_state":2,"auth_model_card":"https://liemo-test.oss-cn-qingdao.aliyuncs.com/dynamic_image/lm_img_1496927162412","auth_video":"https://liemo-test.oss-cn-qingdao.aliyuncs.com/dynamic_video/lm_video_1496927171871","auth_model_live_state":2,"auth_model_art":"https://liemo-test.oss-cn-qingdao.aliyuncs.com/dynamic_image/lm_img_1496927155250","auth_model_art_state":2}
     * tips : 成功!
     * code : 200
     */

    private AuthenBean authen;
    private String tips;
    private String code;
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

    public static class AuthenBean {
        /**
         * auth_model_card_state : 2
         * auth_video_first : https://liemo-test.oss-cn-qingdao.aliyuncs.com/dynamic_image/lm_img_1496927171654
         * auth_model_live : https://liemo-test.oss-cn-qingdao.aliyuncs.com/dynamic_image/lm_img_1496927146309
         * auth_video_state : 2
         * auth_model_card : https://liemo-test.oss-cn-qingdao.aliyuncs.com/dynamic_image/lm_img_1496927162412
         * auth_video : https://liemo-test.oss-cn-qingdao.aliyuncs.com/dynamic_video/lm_video_1496927171871
         * auth_model_live_state : 2
         * auth_model_art : https://liemo-test.oss-cn-qingdao.aliyuncs.com/dynamic_image/lm_img_1496927155250
         * auth_model_art_state : 2
         */

        private int auth_model_card_state;
        private String auth_video_first;
        private String auth_model_live;
        private int auth_video_state;
        private String auth_model_card;
        private String auth_video;
        private int auth_model_live_state;
        private String auth_model_art;
        private int auth_model_art_state;

        public int getAuth_model_card_state() {
            return auth_model_card_state;
        }

        public void setAuth_model_card_state(int auth_model_card_state) {
            this.auth_model_card_state = auth_model_card_state;
        }

        public String getAuth_video_first() {
            return auth_video_first;
        }

        public void setAuth_video_first(String auth_video_first) {
            this.auth_video_first = auth_video_first;
        }

        public String getAuth_model_live() {
            return auth_model_live;
        }

        public void setAuth_model_live(String auth_model_live) {
            this.auth_model_live = auth_model_live;
        }

        public int getAuth_video_state() {
            return auth_video_state;
        }

        public void setAuth_video_state(int auth_video_state) {
            this.auth_video_state = auth_video_state;
        }

        public String getAuth_model_card() {
            return auth_model_card;
        }

        public void setAuth_model_card(String auth_model_card) {
            this.auth_model_card = auth_model_card;
        }

        public String getAuth_video() {
            return auth_video;
        }

        public void setAuth_video(String auth_video) {
            this.auth_video = auth_video;
        }

        public int getAuth_model_live_state() {
            return auth_model_live_state;
        }

        public void setAuth_model_live_state(int auth_model_live_state) {
            this.auth_model_live_state = auth_model_live_state;
        }

        public String getAuth_model_art() {
            return auth_model_art;
        }

        public void setAuth_model_art(String auth_model_art) {
            this.auth_model_art = auth_model_art;
        }

        public int getAuth_model_art_state() {
            return auth_model_art_state;
        }

        public void setAuth_model_art_state(int auth_model_art_state) {
            this.auth_model_art_state = auth_model_art_state;
        }
    }
}
