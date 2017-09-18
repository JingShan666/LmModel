package bbc.com.moteduan_lib2.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/16 0016.
 */

public class InvitePushListBean {
    private List<ListBean> list;

    public List<ListBean> getList() {
        if (list == null) return list = new ArrayList<>();
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * id :
         * type : DD_1
         * alert : ceshi
         * time : 2017-6-17 10:01
         */

        private String id;
        private String type;
        private String alert;
        private String time;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getAlert() {
            return alert;
        }

        public void setAlert(String alert) {
            this.alert = alert;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }

}
