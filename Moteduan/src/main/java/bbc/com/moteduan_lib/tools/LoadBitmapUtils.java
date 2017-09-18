package bbc.com.moteduan_lib.tools;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import bbc.com.moteduan_lib.bean.Picture;
import bbc.com.moteduan_lib.bean.PictureGroup;
import bbc.com.moteduan_lib.constant.FilePathContent;
import bbc.com.moteduan_lib.log.LogDebug;

/**
 * 加载本地 媒体库的图片
 *
 * @author Administrator
 *
 */
public class LoadBitmapUtils {

	//	private static Context context;
//
//	public LoadBitmapUtils(Context context) {
//		this.context = context;
//	}
	/**
	 * 是否获取缓存图
	 */
	private static boolean isGetThumbnail = false;
	/**
	 * 是否 分组返回
	 */
	private static boolean isGroup = false;
	private static Context context;
	private static Handler handler;

	private static final String TAG = LoadBitmapUtils.class.getSimpleName();

	//	查找字段
	public static String[] projection = {
			MediaStore.Images.Media._ID,//id
			MediaStore.Images.Media.DISPLAY_NAME,//带格式后缀的名字
			MediaStore.Images.Media.TITLE,//不带格式后缀的名字
			MediaStore.Images.Media.WIDTH,//像素宽
			MediaStore.Images.Media.HEIGHT,//像素高
			MediaStore.Images.Media.SIZE,//图大小
			MediaStore.Images.Media.DATA,//完整的物理路径
			MediaStore.Images.Media.DATE_ADDED,//加入相册时间
			MediaStore.Images.Media.DATE_MODIFIED,//修改时间
			MediaStore.Images.Media.BUCKET_DISPLAY_NAME//所在文件夹名称
	};

	/**
	 *
	 * @param handler
	 * @param context
	 * @param isGroup 是否以文件夹分组返回图片(true:List (com.bbc.lm.beans.PictureGroup) false:List (com.bbc.lm.beans.Picture))
	 * @param isGetThumbnail 是否生成缩略图
	 * @param width  生成缩略图的宽高，为null时 有默认值
     * @param hight
     */
	public static void AsynLoadImage(Handler handler, Context context, boolean isGroup, boolean isGetThumbnail, Integer width, Integer hight) {
		LoadBitmapUtils.isGetThumbnail = isGetThumbnail;
		LoadBitmapUtils.isGroup = isGroup;
		LoadBitmapUtils.handler = handler;
		LoadBitmapUtils.context = context;
		new Thread(new AsynSearch(width, hight)).start();
	}

	static class AsynSearch implements Runnable {

		Integer width;
		Integer hight;

		AsynSearch(Integer width, Integer hight){
			this.width = width;
			this.hight = hight;
		}

		@Override
		public void run() {
			Message message = handler.obtainMessage();
			message.obj = getImage(context,width, hight);
			message.what = 1;
			handler.sendMessage(message);
		}

	}

	/**
	 * 使用系统媒体库查找所有图片,包含地址，名称，等信息
	 * @param context
	 * @param width 生成缩略图的宽高，为null时 有默认值
	 * @param hight
     * @return
     */
	public static List<?> getImage(Context context, Integer width, Integer hight) {

		LoadBitmapUtils.context = context;

		Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		Cursor cursor = context.getContentResolver().query(uri, projection, null,null, null);//"_data desc"

		List<PictureGroup> listGroup = null;
		List<Picture> list = new ArrayList<Picture>();
		if (cursor != null) {

			LogDebug.print( "找到的数据总数:" + cursor.getCount());
			Set<String> groupSet = null;
			if(isGroup){
				listGroup = new ArrayList<PictureGroup>();
				groupSet = new HashSet<String>();
			}

//			int count = cursor.getCount();
//			int i = 0;
			while (cursor.moveToNext()) {
//				所有分组
				if(isGroup)groupSet.add(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)));

//				封装所有图片信息
				Picture pic = new Picture(
						cursor.getString(cursor.getColumnIndex(projection[0])),
						cursor.getString(cursor.getColumnIndex(projection[1])),
						cursor.getString(cursor.getColumnIndex(projection[2])),
						cursor.getString(cursor.getColumnIndex(projection[3])),
						cursor.getString(cursor.getColumnIndex(projection[4])),
						cursor.getString(cursor.getColumnIndex(projection[5])),
						cursor.getString(cursor.getColumnIndex(projection[6])),
						cursor.getString(cursor.getColumnIndex(projection[7])),
						cursor.getString(cursor.getColumnIndex(projection[8])),
						cursor.getString(cursor.getColumnIndex(projection[9])));

				list.add(pic);
//				i++;
			}
//			System.out.println(i);
			cursor.close();

			if(isGroup){
//			对应分组的图片  加入  到图片组
				for(String groupName:groupSet){
					PictureGroup group = new PictureGroup();
					List<Picture> listPic = new ArrayList<Picture>();

					boolean first = true;
					for(Picture pic:list){
						if(pic.getBucket_display_name().equals(groupName)){

							if(isGetThumbnail){
								//获取缩略图
								LogDebug.print("-----------开始获取缩略图");
								String thumb = getThumbnail(context,pic.getData(),pic.get_id(),width,hight);
								LogDebug.print("-----------结束获取缩略图");
								pic.setThumbnailPath(thumb);
							}
							listPic.add(pic);
						}
					}

					if(!listPic.isEmpty()){
						group.setGroupName(groupName);
						group.setListPic(listPic);
						group.setCount(listPic.size());
						if(first){
//						获取原图绝对路径
							group.setThumbnailPath(listPic.get(0).getData());
							first = false;
						}
						listGroup.add(group);
					}

				}
			}

			LogDebug.print( "封装的图片总数 = " + list.size() + "");

			if(LogDebug.getIsDebug() && groupSet != null){
				for(String groupName2:groupSet){
					LogDebug.print("图片组  ： "+groupName2);
				}
				for(PictureGroup p:listGroup){
					LogDebug.print( "组内容  ： "+p.toString());
				}
			}

			if(isGroup)
				return listGroup;
			return list;
		}
		return null;

	}

	/**
	 * 根据路径中的图 生成缩略图路径返回
	 * @param imagePath 原图路径（如果没有缩略图，就按照该路劲  生成新的缩略图）
	 * @param name 生成文件名称
	 * @param width 生成缩略图的宽高，为null时 有默认值
	 * @param hight
	 * @return 返回保存的路径，如果返回null，缩略图生成出错
	 */
//	private static FileUtils filetool;
	private static IO_Utils ioTool;
	public static String getThumbnail(Context context, String imagePath, String name, Integer width, Integer hight) {

		String path = null;
		Bitmap bmp = null;

//		if(filetool == null)filetool = new FileUtils(context);
//		java.io.File f = new java.io.File(FileUtils.getExternalCache(context,"thumbPic"),name);
//		if(f == null){
//			f = new java.io.File(FileUtils.getCustomerCacheDir(context,"thumbPic"),name);
//		}

		File f = FilePathContent.getSengImageThumbnailPath(context,name);

		if(f.exists()){
			LogDebug.print( "缓存已经存在");
			return f.getAbsolutePath();
		}

		LogDebug.print( "缓存不存在，创建新缩略图！");
		try {
			bmp = BitMapUtils.decodeScaleBitmapFromFilePath(imagePath,width,hight);
			if(ioTool == null)ioTool = new IO_Utils();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		if(bmp != null){

			LogDebug.print( "缩略图生成成功");
//			if(filetool.isUsable()){
//				com.bbc.lm.log.LogDebug.print( "内置存储可用");
				path = ioTool.saveBitmapCache(bmp,f);
				LogDebug.print( "缓存路径 = "+f.getAbsolutePath());
//			}else{
//				com.bbc.lm.log.LogDebug.print( "内置存储不可用！");
//			}
		}else{
			LogDebug.print( "缩略图生成____失败");
		}

		return path;
	}


	/**
	 * 从媒体库中获取！！所有的！！指定后缀的文件列表
	 *
	 * @param searchFileSuffix
	 *            文件后缀列表，eg: new String[]{"epub","mobi","pdf","txt"};
	 * @return 指定后缀的文件列表
	 * */
	public static ArrayList<String> getSupportFileList(Context context,
													   String[] searchFileSuffix) {

		ArrayList<String> searchFileList = null;
		if (null == context || null == searchFileSuffix
				|| searchFileSuffix.length == 0) {
			return null;
		}

		String searchPath = "";
		int length = searchFileSuffix.length;

		for (int index = 0; index < length; index++) {
			searchPath += (MediaStore.Files.FileColumns.DATA + " LIKE '%"
					+ searchFileSuffix[index] + "' ");
			if ((index + 1) < length) {
				searchPath += "or ";
			}
		}

		searchFileList = new ArrayList<String>();
		Uri uri = MediaStore.Files.getContentUri("external");

		System.out.println("uri = "+uri);

		Cursor cursor = context.getContentResolver().query(
				uri,
				new String[] { MediaStore.Files.FileColumns.DATA,
						MediaStore.Files.FileColumns.SIZE }, searchPath, null,
				null);

		if (cursor == null) {
			System.out.println("Cursor 获取失败!");
		} else {

			if (cursor.moveToFirst()) {
				System.out.println("总数 = "+cursor.getCount());
				do {
					String filepath = cursor.getString(cursor
							.getColumnIndex(MediaStore.Files.FileColumns.DATA));
					// if (isFileExist(filepath)) {
					try {

						searchFileList.add(new String(filepath.getBytes("UTF-8")));

						System.out.println("找到的文件 = "+ new String(filepath.getBytes("UTF-8")));

					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					// }
				} while (cursor.moveToNext());
			}
			if (!cursor.isClosed()) {
				cursor.close();
			}
		}
		return searchFileList;
	}


}
