package wei.moments.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2017/4/25 0025.
 */

public  class MomentListContentBean implements Parcelable {
    private boolean lastPage;
    private boolean firstPage;
    private String totalRow;
    private String totalPage;
    private String pageSize;
    private String pageNumber;
    private List<MomentListItemBean> list;

    protected MomentListContentBean(Parcel in) {
        lastPage = in.readByte() != 0;
        firstPage = in.readByte() != 0;
        totalRow = in.readString();
        totalPage = in.readString();
        pageSize = in.readString();
        pageNumber = in.readString();
        list = in.createTypedArrayList(MomentListItemBean.CREATOR);
    }

    public static final Creator<MomentListContentBean> CREATOR = new Creator<MomentListContentBean>() {
        @Override
        public MomentListContentBean createFromParcel(Parcel in) {
            return new MomentListContentBean(in);
        }

        @Override
        public MomentListContentBean[] newArray(int size) {
            return new MomentListContentBean[size];
        }
    };

    public boolean isLastPage() {
        return lastPage;
    }

    public void setLastPage(boolean lastPage) {
        this.lastPage = lastPage;
    }

    public boolean isFirstPage() {
        return firstPage;
    }

    public void setFirstPage(boolean firstPage) {
        this.firstPage = firstPage;
    }

    public String getTotalRow() {
        return totalRow;
    }

    public void setTotalRow(String totalRow) {
        this.totalRow = totalRow;
    }

    public String getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(String totalPage) {
        this.totalPage = totalPage;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(String pageNumber) {
        this.pageNumber = pageNumber;
    }

    public List<MomentListItemBean> getList() {
        return list;
    }

    public void setList(List<MomentListItemBean> list) {
        this.list = list;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (lastPage ? 1 : 0));
        dest.writeByte((byte) (firstPage ? 1 : 0));
        dest.writeString(totalRow);
        dest.writeString(totalPage);
        dest.writeString(pageSize);
        dest.writeString(pageNumber);
        dest.writeTypedList(list);
    }
}