package bbc.com.moteduan_lib.ReleaseDate.bean;

import java.util.List;

/**
 * Created by zhang on 2016/12/9.
 */
public class HatAreaBean {

    /**
     * HatAreaBean
     * data : [{"Lng":113.611572,"Sort":12,"ShortName":"中原","AreaName":"中原区","ID":410102,"Lat":34.748287,"Level":3,"ParentID":410100},{"Lng":113.645424,"Sort":2,"ShortName":"二七","AreaName":"二七区","ID":410103,"Lat":34.730934,"Level":3,"ParentID":410100},{"Lng":113.68531,"Sort":4,"ShortName":"管城回族","AreaName":"管城回族区","ID":410104,"Lat":34.746452,"Level":3,"ParentID":410100},{"Lng":113.686035,"Sort":6,"ShortName":"金水","AreaName":"金水区","ID":410105,"Lat":34.775837,"Level":3,"ParentID":410100},{"Lng":113.298279,"Sort":7,"ShortName":"上街","AreaName":"上街区","ID":410106,"Lat":34.808689,"Level":3,"ParentID":410100},{"Lng":113.618362,"Sort":5,"ShortName":"惠济","AreaName":"惠济区","ID":410108,"Lat":34.82859,"Level":3,"ParentID":410100},{"Lng":114.022522,"Sort":11,"ShortName":"中牟","AreaName":"中牟县","ID":410122,"Lat":34.721977,"Level":3,"ParentID":410100},{"Lng":112.982826,"Sort":3,"ShortName":"巩义","AreaName":"巩义市","ID":410181,"Lat":34.752178,"Level":3,"ParentID":410100},{"Lng":113.391525,"Sort":8,"ShortName":"荥阳","AreaName":"荥阳市","ID":410182,"Lat":34.789078,"Level":3,"ParentID":410100},{"Lng":113.380615,"Sort":9,"ShortName":"新密","AreaName":"新密市","ID":410183,"Lat":34.537846,"Level":3,"ParentID":410100},{"Lng":113.73967,"Sort":10,"ShortName":"新郑","AreaName":"新郑市","ID":410184,"Lat":34.394218,"Level":3,"ParentID":410100},{"Lng":113.037766,"Sort":1,"ShortName":"登封","AreaName":"登封市","ID":410185,"Lat":34.459938,"Level":3,"ParentID":410100}]
     * tips : 成功
     * code : 201
     */

    private String tips;
    private String code;
    /**
     * Lng : 113.611572
     * Sort : 12
     * ShortName : 中原
     * AreaName : 中原区
     * ID : 410102
     * Lat : 34.748287
     * Level : 3
     * ParentID : 410100
     */

    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private double Lng;
        private int Sort;
        private String ShortName;
        private String AreaName;
        private int ID;
        private double Lat;
        private int Level;
        private int ParentID;

        public double getLng() {
            return Lng;
        }

        public void setLng(double Lng) {
            this.Lng = Lng;
        }

        public int getSort() {
            return Sort;
        }

        public void setSort(int Sort) {
            this.Sort = Sort;
        }

        public String getShortName() {
            return ShortName;
        }

        public void setShortName(String ShortName) {
            this.ShortName = ShortName;
        }

        public String getAreaName() {
            return AreaName;
        }

        public void setAreaName(String AreaName) {
            this.AreaName = AreaName;
        }

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public double getLat() {
            return Lat;
        }

        public void setLat(double Lat) {
            this.Lat = Lat;
        }

        public int getLevel() {
            return Level;
        }

        public void setLevel(int Level) {
            this.Level = Level;
        }

        public int getParentID() {
            return ParentID;
        }

        public void setParentID(int ParentID) {
            this.ParentID = ParentID;
        }
    }
}
