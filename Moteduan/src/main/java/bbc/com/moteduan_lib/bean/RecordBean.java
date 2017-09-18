package bbc.com.moteduan_lib.bean;

import java.util.List;

/**
 * Created by zhang on 2017/1/6.
 */
public class RecordBean {

    /**
     * data : [{"id":53,"user_name":"张明","time":"2017-01-06 17:05:25","bank_name":"工商银行","bank_card":"4225552366533699565","model_money":125,"money":100,"do_state":0,"member_id":"9fe8544143e447a58286772e12b5884a"},{"id":54,"user_name":"张明","time":"2017-01-06 17:49:42","bank_name":"中国工商银行","bank_card":"5555556366999985665","model_money":318.75,"money":255,"do_state":0,"member_id":"9fe8544143e447a58286772e12b5884a"},{"id":55,"user_name":"张明","time":"2017-01-06 17:50:56","bank_name":"快来咯馍","bank_card":"4128256666999633996","model_money":9957.5,"money":7966,"do_state":0,"member_id":"9fe8544143e447a58286772e12b5884a"}]
     * code : 200
     * tips : 数据返回成功
     */

    private String code;
    private String tips;
    /**
     * id : 53
     * user_name : 张明
     * time : 2017-01-06 17:05:25
     * bank_name : 工商银行
     * bank_card : 4225552366533699565
     * model_money : 125
     * money : 100
     * do_state : 0
     * member_id : 9fe8544143e447a58286772e12b5884a
     */

    private List<DataBean> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private int id;
        private String user_name;
        private String time;
        private String bank_name;
        private String bank_card;
        private int model_money;
        private int money;
        private int do_state;
        private String member_id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getBank_name() {
            return bank_name;
        }

        public void setBank_name(String bank_name) {
            this.bank_name = bank_name;
        }

        public String getBank_card() {
            return bank_card;
        }

        public void setBank_card(String bank_card) {
            this.bank_card = bank_card;
        }

        public int getModel_money() {
            return model_money;
        }

        public void setModel_money(int model_money) {
            this.model_money = model_money;
        }

        public int getMoney() {
            return money;
        }

        public void setMoney(int money) {
            this.money = money;
        }

        public int getDo_state() {
            return do_state;
        }

        public void setDo_state(int do_state) {
            this.do_state = do_state;
        }

        public String getMember_id() {
            return member_id;
        }

        public void setMember_id(String member_id) {
            this.member_id = member_id;
        }
    }
}
