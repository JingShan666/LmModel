package wei.moments.bean.comment;

/**
 * Created by Administrator on 2017/5/8 0008.
 */

public class CommentReplyBean {
    private String commentType;
    private String contentId;
    private String useId;
    private String contentSex;
    private String commentContent;
    private String toUseId;
    private String toUseSex;
    private String toName;
    private String parid;

    public String getToName() {
        return toName;
    }
    public void setToName(String toName) {
        this.toName = toName;
    }

    public String getCommentType() {
        return commentType;
    }

    public void setCommentType(String commentType) {
        this.commentType = commentType;
    }

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getUseId() {
        return useId;
    }

    public void setUseId(String useId) {
        this.useId = useId;
    }

    public String getContentSex() {
        return contentSex;
    }

    public void setContentSex(String contentSex) {
        this.contentSex = contentSex;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getToUseId() {
        return toUseId;
    }

    public void setToUseId(String toUseId) {
        this.toUseId = toUseId;
    }

    public String getToUseSex() {
        return toUseSex;
    }

    public void setToUseSex(String toUseSex) {
        this.toUseSex = toUseSex;
    }

    public String getParid() {
        return parid;
    }

    public void setParid(String parid) {
        this.parid = parid;
    }
}
