package bbc.com.moteduan_lib.bean;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/3/7 0007.
 */

public class VideoBean implements Parcelable {
    private String fileName;
    private String filePath;
    private String suffix;
    private String uri;
    private boolean selected;
    private long size;
    private long duration;
    private Bitmap thumbnail;

    public VideoBean() {
    }

    protected VideoBean(Parcel in) {
        fileName = in.readString();
        filePath = in.readString();
        suffix = in.readString();
        uri = in.readString();
        selected = in.readByte() != 0;
        size = in.readLong();
        duration = in.readLong();
        thumbnail = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<VideoBean> CREATOR = new Creator<VideoBean>() {
        @Override
        public VideoBean createFromParcel(Parcel in) {
            return new VideoBean(in);
        }

        @Override
        public VideoBean[] newArray(int size) {
            return new VideoBean[size];
        }
    };

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }


    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fileName);
        dest.writeString(filePath);
        dest.writeString(suffix);
        dest.writeString(uri);
        dest.writeByte((byte) (selected ? 1 : 0));
        dest.writeLong(size);
        dest.writeLong(duration);
        dest.writeParcelable(thumbnail, flags);
    }
}
