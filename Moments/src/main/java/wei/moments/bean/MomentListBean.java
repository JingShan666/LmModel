package wei.moments.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2017/4/25 0025.
 */

public class MomentListBean implements Parcelable {
    private String code;
    private String tips;
    private MomentListContentBean contents;

    protected MomentListBean(Parcel in) {
        code = in.readString();
        tips = in.readString();
        contents = in.readParcelable(MomentListContentBean.class.getClassLoader());
    }

    public static final Creator<MomentListBean> CREATOR = new Creator<MomentListBean>() {
        @Override
        public MomentListBean createFromParcel(Parcel in) {
            return new MomentListBean(in);
        }

        @Override
        public MomentListBean[] newArray(int size) {
            return new MomentListBean[size];
        }
    };

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

    public MomentListContentBean getContents() {
        return contents;
    }

    public void setContents(MomentListContentBean contents) {
        this.contents = contents;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
        dest.writeString(tips);
        dest.writeParcelable(contents, flags);
    }

}
