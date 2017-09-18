package wei.moments.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/4/28 0028.
 */

public class PoiItemBean implements Parcelable {

    private boolean isSelected;
    private String cityName;
    private String cityCode;
    private String adName;
    private String adCode;
    private String businessArea;
    private String snippet;
    private String title;

    public PoiItemBean(){
        super();
    }

    protected PoiItemBean(Parcel in) {
        isSelected = in.readByte() != 0;
        cityName = in.readString();
        cityCode = in.readString();
        adName = in.readString();
        adCode = in.readString();
        businessArea = in.readString();
        snippet = in.readString();
        title = in.readString();
    }

    public static final Creator<PoiItemBean> CREATOR = new Creator<PoiItemBean>() {
        @Override
        public PoiItemBean createFromParcel(Parcel in) {
            return new PoiItemBean(in);
        }

        @Override
        public PoiItemBean[] newArray(int size) {
            return new PoiItemBean[size];
        }
    };

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBusinessArea() {
        return businessArea;
    }

    public void setBusinessArea(String businessArea) {
        this.businessArea = businessArea;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getAdName() {
        return adName;
    }

    public void setAdName(String adName) {
        this.adName = adName;
    }

    public String getAdCode() {
        return adCode;
    }

    public void setAdCode(String adCode) {
        this.adCode = adCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isSelected ? 1 : 0));
        dest.writeString(cityName);
        dest.writeString(cityCode);
        dest.writeString(adName);
        dest.writeString(adCode);
        dest.writeString(businessArea);
        dest.writeString(snippet);
        dest.writeString(title);
    }
}
