package bbc.com.moteduan_lib.bean;

import java.util.List;

/**
 * 每个文件夹内的图片 为 一组
 */
public class PictureGroup {

	private List<Picture> listPic;//图片信息实体
	private String groupName;//组名

	//	//缩略图 ， 使用xutils框架加载和处理 缩略图，这个属性暂时不用
	private String thumbnailPath;//组的缩略图，取期内一张图片的缩略图

	private int count;//图片组 数目

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<Picture> getListPic() {
		return listPic;
	}

	public void setListPic(List<Picture> listPic) {
		this.listPic = listPic;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getThumbnailPath() {
		return thumbnailPath;
	}

	public void setThumbnailPath(String thumbnailPath) {
		this.thumbnailPath = thumbnailPath;
	}

	@Override
	public String toString() {
		return "PictureGroup [listPicNums=" + listPic.size() + ", groupName=" + groupName
				+ ", thumbnailPath=" + thumbnailPath + ", count=" + count + "]";
	}

}
