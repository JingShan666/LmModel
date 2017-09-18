package wei.toolkit.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/4/22 0022.
 */

public class PhotoBean implements Parcelable {
    private String name;
    private String path;
    private String fileId;
    private String fileName;
    private String createTime;
    private boolean isSelected;

    public PhotoBean() {
    }

    protected PhotoBean(Parcel in) {
        name = in.readString();
        path = in.readString();
        fileId = in.readString();
        fileName = in.readString();
        createTime = in.readString();
        isSelected = in.readByte() != 0;
    }

    public static final Creator<PhotoBean> CREATOR = new Creator<PhotoBean>() {
        @Override
        public PhotoBean createFromParcel(Parcel in) {
            return new PhotoBean(in);
        }

        @Override
        public PhotoBean[] newArray(int size) {
            return new PhotoBean[size];
        }
    };

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(path);
        dest.writeString(fileId);
        dest.writeString(fileName);
        dest.writeString(createTime);
        dest.writeByte((byte) (isSelected ? 1 : 0));
    }

    @Override
    public String toString() {
        return "name = " + name + " , "
                + "path = " + path + " , "
                + "fileId = " + fileId + " , "
                + "fileName = " + fileName + " , "
                + "createTime = " + createTime;
    }
}
