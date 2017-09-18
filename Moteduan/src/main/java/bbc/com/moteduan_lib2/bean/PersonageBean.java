package bbc.com.moteduan_lib2.bean;

/**
 * Created by Administrator on 2017/6/2 0002.
 */

public class PersonageBean {


    /**
     * member : {"m_hips":90,"m_head_photo":"https://liemo-test.oss-cn-qingdao.aliyuncs.com/dynamic_image/lm_img_1496371392581","num":0,"m_nick_name":"！","m_model_state":0,"m_video_state":0,"m_waist":60,"m_id":"f7afcf2fd8224aa28344d4365aa438b3","m_tall":175,"m_age":22,"b_num":0,"m_bust":90,"m_weight":60}
     * tips : 成功!
     * code : 200
     */

    private MemberBean member;
    private String tips;
    private String code;
    private int sign;
    public MemberBean getMember() {
        return member;
    }

    public void setMember(MemberBean member) {
        this.member = member;
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

    public int getSign() {
        return sign;
    }

    public void setSign(int sign) {
        this.sign = sign;
    }

    public static class MemberBean {
        /**
         * m_hips : 90
         * m_head_photo : https://liemo-test.oss-cn-qingdao.aliyuncs.com/dynamic_image/lm_img_1496371392581
         * num : 0
         * m_nick_name : ！
         * m_model_state : 0
         * m_video_state : 0
         * m_waist : 60
         * m_id : f7afcf2fd8224aa28344d4365aa438b3
         * m_tall : 175
         * m_age : 22
         * b_num : 0
         * m_bust : 90
         * m_weight : 60
         */

        private int m_hips;
        private String m_head_photo;
        private int num;
        private String m_nick_name;
        private int m_model_state;
        private int m_video_state;
        private int m_waist;
        private String m_id;
        private int m_tall;
        private int m_age;
        private int b_num;
        private int m_bust;
        private int m_weight;

        public int getM_hips() {
            return m_hips;
        }

        public void setM_hips(int m_hips) {
            this.m_hips = m_hips;
        }

        public String getM_head_photo() {
            return m_head_photo;
        }

        public void setM_head_photo(String m_head_photo) {
            this.m_head_photo = m_head_photo;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public String getM_nick_name() {
            return m_nick_name;
        }

        public void setM_nick_name(String m_nick_name) {
            this.m_nick_name = m_nick_name;
        }

        public int getM_model_state() {
            return m_model_state;
        }

        public void setM_model_state(int m_model_state) {
            this.m_model_state = m_model_state;
        }

        public int getM_video_state() {
            return m_video_state;
        }

        public void setM_video_state(int m_video_state) {
            this.m_video_state = m_video_state;
        }

        public int getM_waist() {
            return m_waist;
        }

        public void setM_waist(int m_waist) {
            this.m_waist = m_waist;
        }

        public String getM_id() {
            return m_id;
        }

        public void setM_id(String m_id) {
            this.m_id = m_id;
        }

        public int getM_tall() {
            return m_tall;
        }

        public void setM_tall(int m_tall) {
            this.m_tall = m_tall;
        }

        public int getM_age() {
            return m_age;
        }

        public void setM_age(int m_age) {
            this.m_age = m_age;
        }

        public int getB_num() {
            return b_num;
        }

        public void setB_num(int b_num) {
            this.b_num = b_num;
        }

        public int getM_bust() {
            return m_bust;
        }

        public void setM_bust(int m_bust) {
            this.m_bust = m_bust;
        }

        public int getM_weight() {
            return m_weight;
        }

        public void setM_weight(int m_weight) {
            this.m_weight = m_weight;
        }
    }
}
