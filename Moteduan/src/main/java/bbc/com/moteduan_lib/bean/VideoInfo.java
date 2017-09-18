package bbc.com.moteduan_lib.bean;

/**
 * Created by Administrator on 2016/5/31 0031.
   videoInfo.setThumImage(thumbnail);
 */
public class VideoInfo implements android.os.Parcelable {

    private long id;
    private String videoName;
    private String data;
    private long duration;
    private long size;

    private android.graphics.Bitmap bmp;

    public android.graphics.Bitmap getBmp() {
        return bmp;
    }

    public void setBmp(android.graphics.Bitmap bmp) {
        this.bmp = bmp;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "VideoInfo{" +
                "id=" + id +
                ", videoName='" + videoName + '\'' +
                ", data='" + data + '\'' +
                ", duration=" + duration +
                ", size=" + size +
                ", bmp=" + bmp +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.videoName);
        dest.writeString(this.data);
        dest.writeLong(this.duration);
        dest.writeLong(this.size);
        dest.writeParcelable(this.bmp, flags);
    }

    public VideoInfo() {
    }

    protected VideoInfo(android.os.Parcel in) {
        this.id = in.readLong();
        this.videoName = in.readString();
        this.data = in.readString();
        this.duration = in.readLong();
        this.size = in.readLong();
        this.bmp = in.readParcelable(android.graphics.Bitmap.class.getClassLoader());
    }

    public static final Creator<VideoInfo> CREATOR = new Creator<VideoInfo>() {
        @Override
        public VideoInfo createFromParcel(android.os.Parcel source) {
            return new VideoInfo(source);
        }

        @Override
        public VideoInfo[] newArray(int size) {
            return new VideoInfo[size];
        }
    };
}
