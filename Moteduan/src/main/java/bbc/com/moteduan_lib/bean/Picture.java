package bbc.com.moteduan_lib.bean;

import android.os.Parcelable;

/**
 * 图片实体类
 *
 * @author Administrator
 *         MediaStore.Images.Media._ID,//id
 *         MediaStore.Images.Media.DISPLAY_NAME,//带格式后缀的名字
 *         MediaStore.Images.Media.TITLE,//不带格式后缀的名字
 *         MediaStore.Images.Media.WIDTH,//像素宽
 *         MediaStore.Images.Media.HEIGHT,//像素高
 *         MediaStore.Images.Media.SIZE,//图大小
 *         MediaStore.Images.Media.DATA,//完整的物理路径
 *         MediaStore.Images.Media.DATE_ADDED,//加入相册时间
 *         MediaStore.Images.Media.DATE_MODIFIED,//修改时间
 *         MediaStore.Images.Media.BUCKET_DISPLAY_NAME//所在文件夹名称
 */
public class Picture implements Parcelable {

    private String thumbnailPath;//缩略图 ， 使用xutils框架加载和处理 缩略图，这个给融云发送图片消息使用
    private boolean isCheck;//是否选中
    private String _id;
    private String display_name;
    private String title;
    private String width;
    private String height;
    private String size;
    /**
     * 本地url
     */
    private String data;

    /**
     * 发送过在服务器端图片的url
     */
    private String RemoteUrl;

    private String date_added;
    private String date_modified;
    private String bucket_display_name;

    public Picture() {
    }

    public Picture(String thumbnailPath, String localPath, String remotePath) {

        this.thumbnailPath = thumbnailPath;
        this.data = localPath;
        this.RemoteUrl = remotePath;

    }

    public Picture(String _id, String display_name, String title,
                   String width, String height, String size, String data,
                   String date_added, String date_modified, String bucket_display_name) {
        super();
        this._id = _id;
        this.display_name = display_name;
        this.title = title;
        this.width = width;
        this.height = height;
        this.size = size;
        this.data = data;
        this.date_added = date_added;
        this.date_modified = date_modified;
        this.bucket_display_name = bucket_display_name;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public Picture setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
        return this;
    }

    public String getRemoteUrl() {
        return RemoteUrl;
    }

    public void setRemoteUrl(String remoteUrl) {
        RemoteUrl = remoteUrl;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDate_added() {
        return date_added;
    }

    public void setDate_added(String date_added) {
        this.date_added = date_added;
    }

    public String getDate_modified() {
        return date_modified;
    }

    public void setDate_modified(String date_modified) {
        this.date_modified = date_modified;
    }

    public String getBucket_display_name() {
        return bucket_display_name;
    }

    public void setBucket_display_name(String bucket_display_name) {
        this.bucket_display_name = bucket_display_name;
    }

    @Override
    public String toString() {
        return "Picture{" +
                "thumbnailPath='" + thumbnailPath + '\'' +
                ", isCheck=" + isCheck +
                ", _id='" + _id + '\'' +
                ", display_name='" + display_name + '\'' +
                ", title='" + title + '\'' +
                ", width='" + width + '\'' +
                ", height='" + height + '\'' +
                ", size='" + size + '\'' +
                ", data='" + data + '\'' +
                ", RemoteUrl='" + RemoteUrl + '\'' +
                ", date_added='" + date_added + '\'' +
                ", date_modified='" + date_modified + '\'' +
                ", bucket_display_name='" + bucket_display_name + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeString(this.thumbnailPath);
        dest.writeByte(isCheck ? (byte) 1 : (byte) 0);
        dest.writeString(this._id);
        dest.writeString(this.display_name);
        dest.writeString(this.title);
        dest.writeString(this.width);
        dest.writeString(this.height);
        dest.writeString(this.size);
        dest.writeString(this.data);
        dest.writeString(this.RemoteUrl);
        dest.writeString(this.date_added);
        dest.writeString(this.date_modified);
        dest.writeString(this.bucket_display_name);
    }

    protected Picture(android.os.Parcel in) {
        this.thumbnailPath = in.readString();
        this.isCheck = in.readByte() != 0;
        this._id = in.readString();
        this.display_name = in.readString();
        this.title = in.readString();
        this.width = in.readString();
        this.height = in.readString();
        this.size = in.readString();
        this.data = in.readString();
        this.RemoteUrl = in.readString();
        this.date_added = in.readString();
        this.date_modified = in.readString();
        this.bucket_display_name = in.readString();
    }

    public static final Creator<Picture> CREATOR = new Creator<Picture>() {
        @Override
        public Picture createFromParcel(android.os.Parcel source) {
            return new Picture(source);
        }

        @Override
        public Picture[] newArray(int size) {
            return new Picture[size];
        }
    };
}
