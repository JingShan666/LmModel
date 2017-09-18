package wei.moments.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2017/4/25 0025.
 */

public class MomentListItemBean implements Parcelable {
    private String use_id;
    private String use_age;
    private String content_type;
    private String secret_type;
    private String gift_num;
    private String pin_num;
    private String content_sex;
    private String content;
    private String photos;
    private String head_photo;
    private String send_time;
    private String content_id;
    private String video_path;
    private String names;
    private String zan_num;
    private String send_addres;
    private String guanz_Y;
    private String zan_Y;
    private String video_first_path;
    private long timeStamp;
    private int shedule_num;
    private boolean isPlay;
    private int videoCurrentPosition;

    public MomentListItemBean(){
        super();
    }
    protected MomentListItemBean(Parcel in) {
        use_id = in.readString();
        content_type = in.readString();
        secret_type = in.readString();
        gift_num = in.readString();
        pin_num = in.readString();
        content_sex = in.readString();
        content = in.readString();
        photos = in.readString();
        head_photo = in.readString();
        send_time = in.readString();
        content_id = in.readString();
        video_path = in.readString();
        names = in.readString();
        zan_num = in.readString();
        send_addres = in.readString();
        use_age = in.readString();
        guanz_Y = in.readString();
        zan_Y = in.readString();
        isPlay = in.readByte() != 0;
        videoCurrentPosition = in.readInt();
        video_first_path = in.readString();
        timeStamp = in.readLong();
        shedule_num = in.readInt();
    }

    public static final Creator<MomentListItemBean> CREATOR = new Creator<MomentListItemBean>() {
        @Override
        public MomentListItemBean createFromParcel(Parcel in) {
            return new MomentListItemBean(in);
        }

        @Override
        public MomentListItemBean[] newArray(int size) {
            return new MomentListItemBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public int getVideoCurrentPosition() {
        return videoCurrentPosition;
    }

    public void setVideoCurrentPosition(int videoCurrentPosition) {
        this.videoCurrentPosition = videoCurrentPosition;
    }

    public String getVideo_first_path() {
        return video_first_path;
    }

    public void setVideo_first_path(String video_first_path) {
        this.video_first_path = video_first_path;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getShedule_num() {
        return shedule_num;
    }

    public void setShedule_num(int shedule_num) {
        this.shedule_num = shedule_num;
    }

    public boolean isPlay() {
        return isPlay;
    }

    public void setPlay(boolean play) {
        isPlay = play;
    }

    public String getUse_id() {
        return use_id;
    }

    public void setUse_id(String use_id) {
        this.use_id = use_id;
    }

    public String getContent_type() {
        return content_type;
    }

    public void setContent_type(String content_type) {
        this.content_type = content_type;
    }

    public String getSecret_type() {
        return secret_type;
    }

    public void setSecret_type(String secret_type) {
        this.secret_type = secret_type;
    }

    public String getGift_num() {
        return gift_num;
    }

    public void setGift_num(String gift_num) {
        this.gift_num = gift_num;
    }

    public String getPin_num() {
        return pin_num;
    }

    public void setPin_num(String pin_num) {
        this.pin_num = pin_num;
    }

    public String getContent_sex() {
        return content_sex;
    }

    public void setContent_sex(String content_sex) {
        this.content_sex = content_sex;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public String getHead_photo() {
        return head_photo;
    }

    public void setHead_photo(String head_photo) {
        this.head_photo = head_photo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSend_time() {
        return send_time;
    }

    public void setSend_time(String send_time) {
        this.send_time = send_time;
    }

    public String getContent_id() {
        return content_id;
    }

    public void setContent_id(String content_id) {
        this.content_id = content_id;
    }

    public String getVideo_path() {
        return video_path;
    }

    public void setVideo_path(String video_path) {
        this.video_path = video_path;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getZan_num() {
        return zan_num;
    }

    public void setZan_num(String zan_num) {
        this.zan_num = zan_num;
    }

    public String getSend_addres() {
        return send_addres;
    }

    public void setSend_addres(String send_addres) {
        this.send_addres = send_addres;
    }

    public String getUse_age() {
        return use_age;
    }

    public void setUse_age(String use_age) {
        this.use_age = use_age;
    }

    public String getGuanz_Y() {
        return guanz_Y;
    }

    public void setGuanz_Y(String guanz_Y) {
        this.guanz_Y = guanz_Y;
    }

    public String getZan_Y() {
        return zan_Y;
    }

    public void setZan_Y(String zan_Y) {
        this.zan_Y = zan_Y;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(use_id);
        dest.writeString(content_type);
        dest.writeString(secret_type);
        dest.writeString(gift_num);
        dest.writeString(pin_num);
        dest.writeString(content_sex);
        dest.writeString(content);
        dest.writeString(photos);
        dest.writeString(head_photo);
        dest.writeString(send_time);
        dest.writeString(content_id);
        dest.writeString(video_path);
        dest.writeString(names);
        dest.writeString(zan_num);
        dest.writeString(send_addres);
        dest.writeString(use_age);
        dest.writeString(guanz_Y);
        dest.writeString(zan_Y);
        dest.writeByte((byte) (isPlay ? 1 : 0));
        dest.writeInt(videoCurrentPosition);
        dest.writeString(video_first_path);
        dest.writeInt(shedule_num);
        dest.writeLong(timeStamp);
    }


    public static class PhotoList implements Parcelable {
        private List<PhotoListBean> list;

        protected PhotoList(Parcel in) {
        }

        public static final Creator<PhotoList> CREATOR = new Creator<PhotoList>() {
            @Override
            public PhotoList createFromParcel(Parcel in) {
                return new PhotoList(in);
            }

            @Override
            public PhotoList[] newArray(int size) {
                return new PhotoList[size];
            }
        };

        public List<PhotoListBean> getList() {
            return list;
        }

        public void setList(List<PhotoListBean> list) {
            this.list = list;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
        }
    }

    public static class PhotoListBean implements Parcelable {
        private String width;
        private String height;
        private String url;

        protected PhotoListBean(Parcel in) {
            width = in.readString();
            height = in.readString();
            url = in.readString();
        }

        public static final Creator<PhotoListBean> CREATOR = new Creator<PhotoListBean>() {
            @Override
            public PhotoListBean createFromParcel(Parcel in) {
                return new PhotoListBean(in);
            }

            @Override
            public PhotoListBean[] newArray(int size) {
                return new PhotoListBean[size];
            }
        };

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

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(width);
            dest.writeString(height);
            dest.writeString(url);
        }
    }
}
