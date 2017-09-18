package bbc.com.moteduan_lib2.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/5/31 0031.
 */

public class InviteTypeBean implements Parcelable {
    private boolean isSelected;
    private String label;
    private String id;

    public InviteTypeBean() {
        super();
    }

    public InviteTypeBean(String label,String id,boolean isSelected) {
        this.label = label;
        this.isSelected = isSelected;
        this.id = id;
    }

    protected InviteTypeBean(Parcel in) {
        isSelected = in.readByte() != 0;
        label = in.readString();
        id = in.readString();
    }

    public static final Creator<InviteTypeBean> CREATOR = new Creator<InviteTypeBean>() {
        @Override
        public InviteTypeBean createFromParcel(Parcel in) {
            return new InviteTypeBean(in);
        }

        @Override
        public InviteTypeBean[] newArray(int size) {
            return new InviteTypeBean[size];
        }
    };

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isSelected ? 1 : 0));
        dest.writeString(label);
        dest.writeString(id);
    }
}
