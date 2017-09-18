package bbc.com.moteduan_lib.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangming on 2016/9/30 0030.
 */
public class WeiXinLoginGetUserinfoBean implements Serializable {

    /**
     * openid : o_qOEwv_1hM-lW2Y-VU3sQg6PBFU
     * nickname : é¿å°åæ¯çå¹¸ç¦930
     * sex : 1
     * language : zh_CN
     * city :
     * province :
     * country : CN
     * headimgurl : http://wx.qlogo.cn/mmopen/ajNVdqHZLLCnpd5P29cu8Z8nXibX95wUbyibnc7cNzGRgibTic0DoLPBlMfjqXGDwuO6ovibNNZU05M3WsS5qm8KvWg/0
     * privilege : []
     * unionid : o-NbXv4y3k4uYErYzl93aLj6jK4c
     */

    private String openid;
    private String nickname;
    private int sex;
    private String language;
    private String city;
    private String province;
    private String country;
    private String headimgurl;
    private String unionid;
    private List<?> privilege;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public List<?> getPrivilege() {
        return privilege;
    }

    public void setPrivilege(List<?> privilege) {
        this.privilege = privilege;
    }
}