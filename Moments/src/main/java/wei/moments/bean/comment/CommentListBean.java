package wei.moments.bean.comment;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2017/5/5 0005.
 */

public class CommentListBean implements Parcelable {


    /**
     * list : [{"comment":[{"comment_id":"1dae36a6c3df489f8df5229ab536f060","hui_sex":1,"b_hui_sex":2,"parid":"1dae36a6c3df489f8df522564236f060","comment_type":1,"hui_name":"那一年疯狂的夏日","photo":"","content":"sdfasdf","hui_id":"2b9e1896cfc2e45bb9d4261d271eba65","content_id":"1dae36a6c3df489f8df5229ab536f06d","b_hui_name":"！","comment_date":" 2017-05-05 13:54:49","b_hui_id":2}],"conmmentOne":{"comment_id":"1dae36a6c3df489f8df5229ab536f060","hui_sex":1,"b_hui_sex":2,"parid":"1dae36a6c3df489f8df522564236f060","comment_type":1,"hui_name":"那一年疯狂的夏日","photo":"","content":"sdfasdf","hui_id":"2b9e1896cfc2e45bb9d4261d271eba65","content_id":"1dae36a6c3df489f8df5229ab536f06d","b_hui_name":"！","comment_date":" 2017-05-05 13:54:49","b_hui_id":2}}]
     * tips : 请求数据成功！
     * code : 200
     */

    private String tips;
    private String code;
    private List<ListBean> list;
    private long timeStamp;
    protected CommentListBean(Parcel in) {
        tips = in.readString();
        code = in.readString();
        timeStamp = in.readLong();
    }

    public static final Creator<CommentListBean> CREATOR = new Creator<CommentListBean>() {
        @Override
        public CommentListBean createFromParcel(Parcel in) {
            return new CommentListBean(in);
        }

        @Override
        public CommentListBean[] newArray(int size) {
            return new CommentListBean[size];
        }
    };

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

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tips);
        dest.writeString(code);
        dest.writeLong(timeStamp);
    }

    public static class ListBean implements Parcelable {
        /**
         * comment : [{"comment_id":"1dae36a6c3df489f8df5229ab536f060","hui_sex":1,"b_hui_sex":2,"parid":"1dae36a6c3df489f8df522564236f060","comment_type":1,"hui_name":"那一年疯狂的夏日","photo":"","content":"sdfasdf","hui_id":"2b9e1896cfc2e45bb9d4261d271eba65","content_id":"1dae36a6c3df489f8df5229ab536f06d","b_hui_name":"！","comment_date":" 2017-05-05 13:54:49","b_hui_id":2}]
         * conmmentOne : {"comment_id":"1dae36a6c3df489f8df5229ab536f060","hui_sex":1,"b_hui_sex":2,"parid":"1dae36a6c3df489f8df522564236f060","comment_type":1,"hui_name":"那一年疯狂的夏日","photo":"","content":"sdfasdf","hui_id":"2b9e1896cfc2e45bb9d4261d271eba65","content_id":"1dae36a6c3df489f8df5229ab536f06d","b_hui_name":"！","comment_date":" 2017-05-05 13:54:49","b_hui_id":2}
         */

        private CommentBean conmmentOne;
        private List<CommentBean> comment;

        protected ListBean(Parcel in) {
        }

        public static final Creator<ListBean> CREATOR = new Creator<ListBean>() {
            @Override
            public ListBean createFromParcel(Parcel in) {
                return new ListBean(in);
            }

            @Override
            public ListBean[] newArray(int size) {
                return new ListBean[size];
            }
        };

        public CommentBean getConmmentOne() {
            return conmmentOne;
        }

        public void setConmmentOne(CommentBean conmmentOne) {
            this.conmmentOne = conmmentOne;
        }

        public List<CommentBean> getComment() {
            return comment;
        }

        public void setComment(List<CommentBean> comment) {
            this.comment = comment;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
        }
    }

    public static class CommentBean implements Parcelable {
        /**
         * comment_id : 1dae36a6c3df489f8df5229ab536f060
         * hui_sex : 1
         * b_hui_sex : 2
         * parid : 1dae36a6c3df489f8df522564236f060
         * comment_type : 1
         * hui_name : 那一年疯狂的夏日
         * photo :
         * content : sdfasdf
         * hui_id : 2b9e1896cfc2e45bb9d4261d271eba65
         * content_id : 1dae36a6c3df489f8df5229ab536f06d
         * b_hui_name : ！
         * comment_date :  2017-05-05 13:54:49
         * b_hui_id : 2
         */

        private String comment_id;
        private int hui_sex;
        private int b_hui_sex;
        private String parid;
        private int comment_type;
        private String hui_name;
        private String photo;
        private String hui_head_photo;
        private String b_hui_head_photo;
        private String content;
        private String hui_id;
        private String content_id;
        private String b_hui_name;
        private String comment_date;
        private String b_hui_id;
        public CommentBean(){super();}
        protected CommentBean(Parcel in) {
            comment_id = in.readString();
            hui_sex = in.readInt();
            b_hui_sex = in.readInt();
            parid = in.readString();
            comment_type = in.readInt();
            hui_name = in.readString();
            photo = in.readString();
            content = in.readString();
            hui_id = in.readString();
            content_id = in.readString();
            b_hui_name = in.readString();
            comment_date = in.readString();
            b_hui_id = in.readString();
            hui_head_photo = in.readString();
            b_hui_head_photo = in.readString();
        }

        public static final Creator<CommentBean> CREATOR = new Creator<CommentBean>() {
            @Override
            public CommentBean createFromParcel(Parcel in) {
                return new CommentBean(in);
            }

            @Override
            public CommentBean[] newArray(int size) {
                return new CommentBean[size];
            }
        };

        public String getB_hui_head_photo() {
            return b_hui_head_photo;
        }

        public void setB_hui_head_photo(String b_hui_head_photo) {
            this.b_hui_head_photo = b_hui_head_photo;
        }

        public String getHui_head_photo() {
            return hui_head_photo;
        }

        public void setHui_head_photo(String hui_head_photo) {
            this.hui_head_photo = hui_head_photo;
        }

        public String getComment_id() {
            return comment_id;
        }

        public void setComment_id(String comment_id) {
            this.comment_id = comment_id;
        }

        public int getHui_sex() {
            return hui_sex;
        }

        public void setHui_sex(int hui_sex) {
            this.hui_sex = hui_sex;
        }

        public int getB_hui_sex() {
            return b_hui_sex;
        }

        public void setB_hui_sex(int b_hui_sex) {
            this.b_hui_sex = b_hui_sex;
        }

        public String getParid() {
            return parid;
        }

        public void setParid(String parid) {
            this.parid = parid;
        }

        public int getComment_type() {
            return comment_type;
        }

        public void setComment_type(int comment_type) {
            this.comment_type = comment_type;
        }

        public String getHui_name() {
            return hui_name;
        }

        public void setHui_name(String hui_name) {
            this.hui_name = hui_name;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getHui_id() {
            return hui_id;
        }

        public void setHui_id(String hui_id) {
            this.hui_id = hui_id;
        }

        public String getContent_id() {
            return content_id;
        }

        public void setContent_id(String content_id) {
            this.content_id = content_id;
        }

        public String getB_hui_name() {
            return b_hui_name;
        }

        public void setB_hui_name(String b_hui_name) {
            this.b_hui_name = b_hui_name;
        }

        public String getComment_date() {
            return comment_date;
        }

        public void setComment_date(String comment_date) {
            this.comment_date = comment_date;
        }

        public String getB_hui_id() {
            return b_hui_id;
        }

        public void setB_hui_id(String b_hui_id) {
            this.b_hui_id = b_hui_id;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(comment_id);
            dest.writeInt(hui_sex);
            dest.writeInt(b_hui_sex);
            dest.writeString(parid);
            dest.writeInt(comment_type);
            dest.writeString(hui_name);
            dest.writeString(photo);
            dest.writeString(content);
            dest.writeString(hui_id);
            dest.writeString(content_id);
            dest.writeString(b_hui_name);
            dest.writeString(comment_date);
            dest.writeString(b_hui_id);
            dest.writeString(hui_head_photo);
            dest.writeString(b_hui_head_photo);
        }
    }
}
