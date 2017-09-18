package bbc.com.moteduan_lib.tools;

import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import bbc.com.moteduan_lib.constant.Constants;

/*
*<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
		<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
		从API 19/Andorid4.4/KITKAT开始，
		不再需要显式声明这两个权限，除非要读写其他应用的应用数据($appDataDir)
		*/
public class FileUtils {

    private android.content.Context context;
    private String TAG = FileUtils.class.getSimpleName();

    public FileUtils() {
    }

    public FileUtils(android.content.Context context) {
        this.context = context;
    }


    /**
     * file --> byte[]
     *
     * @param file
     * @return
     */
    public static byte[] getFileToByte(File file) {
        byte[] by = new byte[(int) file.length()];
        try {
            java.io.InputStream is = new java.io.FileInputStream(file);
            java.io.ByteArrayOutputStream bytestream = new java.io.ByteArrayOutputStream();
            byte[] bb = new byte[512];
            int ch;
            ch = is.read(bb);
            while (ch != -1) {
                bytestream.write(bb, 0, ch);
                ch = is.read(bb);
            }
            by = bytestream.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return by;
    }

    /**
     * 使用系統提供的 方法 直接保存到 本机目录的 data/data/应用名/file/xx.txt
     * <p>
     * 读取有context.openFileInput(arg0)
     *
     * @param fileName
     * @param msg
     * @throws IOException
     */
    public void openFileOutputStream(String fileName, String msg) throws IOException {

        java.io.FileOutputStream outputStream = context.openFileOutput(fileName, android.app.Activity.MODE_PRIVATE);
        outputStream.write(msg.getBytes());
        outputStream.flush();
        outputStream.close();

    }

    /**
     * 判断  外置 内存卡  是否装载可用（内置sd卡无法判断）
     *
     * @return true_chat 是可用
     */
    public static boolean isUsable() {

        if (android.os.Environment.MEDIA_MOUNTED == android.os.Environment.getExternalStorageState()) {
            return true;
        }
        return false;
    }

    /**
     * 得到外置 sd卡 的路径（  非 内置sd卡  ）
     *
     * @return
     */
    public String getExternalSDPath() {

        String sdcard_path = null;
        String sd_default = android.os.Environment.getExternalStorageDirectory()
                .getAbsolutePath();
        android.util.Log.d("tab", sd_default);
        if (sd_default.endsWith("/")) {
            sd_default = sd_default.substring(0, sd_default.length() - 1);
        }

        // 得到路径
        try {
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec("mount");
            java.io.InputStream is = proc.getInputStream();
            java.io.InputStreamReader isr = new java.io.InputStreamReader(is);
            String line;
            java.io.BufferedReader br = new java.io.BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                if (line.contains("secure"))
                    continue;
                if (line.contains("asec"))
                    continue;
                if (line.contains("fat") && line.contains("/mnt/")) {
                    String columns[] = line.split(" ");
                    if (columns != null && columns.length > 1) {
                        if (sd_default.trim().equals(columns[1].trim())) {
                            continue;
                        }
                        sdcard_path = columns[1];
                    }
                } else if (line.contains("fuse") && line.contains("/mnt/")) {
                    String columns[] = line.split(" ");
                    if (columns != null && columns.length > 1) {
                        if (sd_default.trim().equals(columns[1].trim())) {
                            continue;
                        }
                        sdcard_path = columns[1];
                    }
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        android.util.Log.d("tab", sdcard_path);
        return sdcard_path;
    }

    /**
     * 得到手机内存目录
     *
     * @return
     */
    @android.annotation.SuppressLint("NewApi")
    public String testGetDir() {

//	手机本机内的根目录：
//		/data
        android.util.Log.i(TAG, android.os.Environment.getDataDirectory().getAbsolutePath());
//		/cache
        android.util.Log.i(TAG, android.os.Environment.getDownloadCacheDirectory().getAbsolutePath());
//		/system
        android.util.Log.i(TAG, android.os.Environment.getRootDirectory().getAbsolutePath());

//	外置内存卡目录
//		根目录：  /storage/emulate/0
        android.util.Log.w(TAG, android.os.Environment.getExternalStorageDirectory().getAbsolutePath());
        android.util.Log.w(TAG, android.os.Environment.getExternalStoragePublicDirectory("").getAbsolutePath());

//		通过 Environment得到 指定目录
//		/storage/emulate/0/365
        android.util.Log.w(TAG, android.os.Environment.getExternalStoragePublicDirectory("365").getAbsolutePath());

//	通过context得到 应用包下 外置卡的 指定的目录(自动生成)
//		/storage/emulated/0/android/data/ com.example.tools /files/alarms或dcim或documents或。。。。

		/*
        /storage/emulated/0/Android/data/im.bbc.com.rongyunim/files/Music
		/storage/emulated/0/Android/data/im.bbc.com.rongyunim/files/Movies
		/storage/emulated/0/Android/data/im.bbc.com.rongyunim/files/Pictures
		 /storage/emulated/0/Android/data/im.bbc.com.rongyunim/files/Download
		/storage/emulated/0/Android/data/im.bbc.com.rongyunim/files/Ringtones
		 /storage/emulated/0/Android/data/im.bbc.com.rongyunim/files/Alarms
		/storage/emulated/0/Android/data/im.bbc.com.rongyunim/files/DCIM
		/storage/emulated/0/Android/data/im.bbc.com.rongyunim/files/Documents
		 /storage/emulated/0/Android/data/im.bbc.com.rongyunim/files/Notifications
		 /storage/emulated/0/Android/data/im.bbc.com.rongyunim/files/Podcasts
		 */
        android.util.Log.w(TAG, context.getExternalFilesDir(android.os.Environment.DIRECTORY_MUSIC).getAbsolutePath());
        android.util.Log.w(TAG, context.getExternalFilesDir(android.os.Environment.DIRECTORY_MOVIES).getAbsolutePath());
        android.util.Log.w(TAG, context.getExternalFilesDir(android.os.Environment.DIRECTORY_PICTURES).getAbsolutePath());
        android.util.Log.w(TAG, context.getExternalFilesDir(android.os.Environment.DIRECTORY_DOWNLOADS).getAbsolutePath());
        android.util.Log.w(TAG, context.getExternalFilesDir(android.os.Environment.DIRECTORY_RINGTONES).getAbsolutePath());
        android.util.Log.w(TAG, context.getExternalFilesDir(android.os.Environment.DIRECTORY_ALARMS).getAbsolutePath());
        android.util.Log.w(TAG, context.getExternalFilesDir(android.os.Environment.DIRECTORY_DCIM).getAbsolutePath());
        android.util.Log.w(TAG, context.getExternalFilesDir(android.os.Environment.DIRECTORY_DOCUMENTS).getAbsolutePath());
        android.util.Log.w(TAG, context.getExternalFilesDir(android.os.Environment.DIRECTORY_NOTIFICATIONS).getAbsolutePath());
        android.util.Log.w(TAG, context.getExternalFilesDir(android.os.Environment.DIRECTORY_PODCASTS).getAbsolutePath());

//	内部储存    应用包下的文件夹
//		/data/data/应.用.包.名/cache
        android.util.Log.d(TAG, context.getCacheDir().getAbsolutePath());
//		/data/data/应.用.包.名/files
        android.util.Log.d(TAG, context.getFilesDir().getAbsolutePath());
//		数据库temp的路径     /data/data/应.用.包.名/databases/temp
        android.util.Log.d(TAG, context.getDatabasePath("temp").getAbsolutePath());

//		取得自身的apk安装后的路径，名字可能加有后缀：   /data/data/com.example.androidtools-5.apk
        android.util.Log.d(TAG, context.getPackageCodePath().toString());
        android.util.Log.d(TAG, context.getPackageResourcePath().toString());

//应用在外置卡的缓存目录： /storage/emulated/0/android/data/com.example.androidtools/cache
        android.util.Log.d(TAG, context.getExternalCacheDir().toString());

//		外置卡android文件夹下obb数据包目录(若无会以包名创建) :
//		/storage/emulated/0/android/obb/com.example.androidtools
        android.util.Log.d(TAG, context.getObbDir().toString());

        return null;
    }

    /**
     * 得到外部储存根目录
     *
     * @return
     */
    public File getExternalRoot() {

        File file = android.os.Environment.getExternalStorageDirectory();
        return file;
    }

    /**
     * 返回外部的公开目录对象，如果 dirName 文件夹不存在 不会自动创建，需要手动 file.mkdirs()
     *
     * @param dirName
     * @return
     */
    public File getExternalPublicDirByName(String dirName) {

        return android.os.Environment.getExternalStoragePublicDirectory(dirName);
    }

    /**
     * 得到应用的外部 chche目录
     *
     * @return
     * @String... 为null 时 getExternalCacheDir   不为null ： /dirname
     */
    public static File getExternalCache(android.content.Context context, String... dirName) {

        if (dirName == null || dirName[0].equals("")) {
            return context.getExternalCacheDir();
        } else {
            File f = new File(context.getExternalCacheDir(), dirName[0]);
            if (!f.exists()) {
                f.mkdirs();
            }
            return f;
        }

    }

    /**
     * 得到应用的外部 file目录
     *
     * @return
     */
    public File getExternalFilesDir() {
//		return context.getExternalFilesDir(null);
        return context.getExternalFilesDir(android.os.Environment.DIRECTORY_MUSIC);
    }

    /**
     * 得到应用的外置 obb数据目录
     *
     * @return
     */
    public File getObbDir() {

        return context.getObbDir();
    }


    /**
     * 得到应用的内部 chche目录
     *
     * @return
     */
    public static File getCacheDir(android.content.Context context) {

        return context.getCacheDir();
    }

    /**
     * 得到应用的内部 chche/dirName
     *
     * @param dirName thumbPic
     * @return
     */
    public static File getCustomerCacheDir(android.content.Context context, String dirName) {

        File f = new File(getCacheDir(context).getAbsolutePath(), dirName);
        if (!f.exists()) {
            f.mkdirs();
        }
        return f;
    }

    /**
     * 得到应用的内部 file目录
     *
     * @return
     */
    public File getFilesDir() {

        return context.getFilesDir();
    }

    /**
     * 得到应用的内置 数据库 DB_name 目录
     *
     * @return
     */
    public File getDatabaseDir(String DB_name) {

        return context.getDatabasePath(DB_name);
    }

    /**
     * fileName 可以自定义  或  使用系统提供的常量 Environment.DIRECTORY_MUSIC
     * 自动创建 在 内置应用文件夹内 file 下创建
     *
     * @param fileName
     * @return
     */
    @android.annotation.SuppressLint("NewApi")
    public File getExternalFileDir(String fileName) {//

        return context.getExternalFilesDir(fileName);
    }

    /**
     * 重新命名
     *
     * @param oldFile
     * @param newName
     */
    public void rename(File oldFile, String newName) {

        File newFile = new File(oldFile.getParent(), newName);
        oldFile.renameTo(newFile);
    }

    /**
     * 删除文件夹或文件
     *
     * @param file
     */
    public void deletaFile(File file) {

        file.delete();//立即删除

        //file.deleteOnExit();//程序退出后删除，只有正常退出才会删除
    }


    private static FileUtils mFileUtils;

    public static FileUtils getInstance() {
        if (mFileUtils == null) {
            mFileUtils = new FileUtils();
        }
        return mFileUtils;
    }

    public boolean removeFile(String path) {
        android.util.Log.i("delete", "delete");
        File file = new File(path);
        if (file.exists()) {
            boolean bool = file.delete();
            android.util.Log.i("deletebool", "delete" + bool);
            return bool;
        }
        return false;
    }

    public boolean fileExist(String path) {
        File file = new File(path);
        boolean bool = file.exists();
        android.util.Log.i("bool", bool + "");
        return bool;
    }

    public synchronized boolean reName(String oldFullName, String newFullName) {
        android.util.Log.i("rename", oldFullName);
        android.util.Log.i("rename", newFullName);
        File file = new File(oldFullName);
        if (!file.exists()) {
            return false;
        }
        File newfile = new File(newFullName);
        if (newfile.exists()) {
            newfile.delete();
        }

        boolean bool = file.renameTo(newfile);
        android.util.Log.i("renamebool", "rename" + bool);
        return bool;
    }

    public synchronized void openFile(android.content.Context context, String fileFullName) {
        File file = new File(fileFullName);
        String type = getMIMEType(file);
        android.content.Intent intent = new android.content.Intent();
        intent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(android.net.Uri.fromFile(file), type);
        context.startActivity(intent);
    }

    public String getMIMEType(File f) {
        String type = "";
        String fName = f.getName();
        String end = fName
                .substring(fName.lastIndexOf(".") + 1, fName.length())
                .toLowerCase();

        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid")
                || end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
            type = "audio";
        } else if (end.equals("3gp") || end.equals("mp4")) {
            type = "video";
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
                || end.equals("jpeg") || end.equals("bmp")) {
            type = "image";
        } else if (end.equals("apk")) {
            type = "application/vnd.android.package-archive";
        } else {
            type = "*";
        }
        if (!end.equals("apk")) {
            type += "/*";
        }
        return type;

    }

    public final String getFilePath(final String fileFullName) {
        return fileFullName.substring(0, fileFullName.lastIndexOf('/') + 1);
    }

    public final String getFileExe(String fileName) {
        int index = fileName.lastIndexOf('.');
        if (index == -1) {
            return null;
        }
        return fileName.substring(index + 1);
    }

    public final String getFileName(final String fileFullName) {
        return fileFullName.substring(fileFullName.lastIndexOf('/') + 1);
    }

    public synchronized boolean saveFile(String fileFullName, byte[] bytes,
                                         long offset) {
        android.util.Log.i("saveFile11111111111111", fileFullName + "-" + offset);
        java.io.FileOutputStream fos = null;

        try {
            fos = new java.io.FileOutputStream(fileFullName, true);
            fos.write(bytes, 0, bytes.length);
            fos.close();
            fos = null;
            File file = new File(fileFullName);

            android.util.Log.i("filesize", file.length() + "");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    /**
     * 删除文件夹下文件
     *
     * @param dirPath
     */
    public void delAllFile(String dirPath) {
        File dirFile = new File(dirPath);
        if (dirFile.exists() && dirFile.isDirectory()) {
            File files[] = dirFile.listFiles();
            for (File file : files) {
                file.delete();
            }
        }
    }

    /**
     * 获取文件后缀名
     */
    public String getPreFix(String path) {
        File f = new File(path);
        String fileName = f.getName();
        String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
        return prefix;
    }

    /**
     * 生成视频保存路径
     */
    public String genLocalVideoPath(android.content.Context con) {
        if (!DeviceUtils.hasSDcard()) {
            Toast.makeText(con, "请插入SD卡!", Toast.LENGTH_SHORT).show();
            return "";
        }
        File mVideoDir = new File(Constants.ROOT_PATH + "Video");
        if (!mVideoDir.exists()) {
            mVideoDir.mkdirs();
        }
        StringBuilder builder = new StringBuilder();
        builder.append(TimeDateUtils.getSystemTime("yyyyMMdd_HHmmss"))
                .append(".mp4");
        String fileName = builder.toString();
        File saveFile = new File(mVideoDir, fileName);
        return saveFile.getAbsolutePath();
    }

    /**
     * 生成保存到云的文件名
     */
    public String genServerVideoName() {
        java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyyMMdd_HHmmss");
        java.util.Date curDate = new java.util.Date(System.currentTimeMillis());
        return formatter.format(curDate) + "_" + genRandomStr(4);
    }

    /**
     * 生成随机字符串
     *
     * @param length 生成字符串的长度
     */
    public String genRandomStr(int length) { //length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        java.util.Random random = new java.util.Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 保存视频的缩略图
     * 先通过ThumbnailUtils来创建一个视频的缩略图，然后再利用ThumbnailUtils来生成指定大小的缩略图。
     * 如果想要的缩略图的宽和高都小于MICRO_KIND，则类型要使用MICRO_KIND作为kind的值，这样会节省内存。
     *
     * @param videoPath 视频的路径
     * @param width     指定输出视频缩略图的宽度
     * @param height    指定输出视频缩略图的高度度
     *                  kind 参照MediaStore.Images.Thumbnails类中的常量MINI_KIND和MICRO_KIND。
     *                  其中，MINI_KIND: 512 x 384，MICRO_KIND: 96 x 96
     * @return 指定大小的视频缩略图
     */
    public boolean saveVideoThumbnail(String videoPath, int width, int height) {
        try {
            android.graphics.Bitmap bitmap = null;
            // 获取视频的缩略图
            bitmap = android.media.ThumbnailUtils.createVideoThumbnail(videoPath, android.provider.MediaStore.Images.Thumbnails.MICRO_KIND);

            bitmap = android.media.ThumbnailUtils.extractThumbnail(bitmap, width, height);
            if (bitmap == null) {
                return false;
            }

            // 缩略图名，跟视频一致
            String imgPath = videoPath.substring(0, videoPath.lastIndexOf(".")) + ".png";
            File f = new File(imgPath);
            f.createNewFile();
            java.io.FileOutputStream fOut = new java.io.FileOutputStream(f);
            bitmap.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 删除文件
     *
     * @param filePath
     */
    public void deleteFile(String filePath) {
        if (StringUtils.isEmpty(filePath)) {
            return;
        }
        File file = new File(filePath);
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                file.delete(); // delete()方法 你应该知道 是删除的意思;
            } else if (file.isDirectory()) { // 否则如果它是一个目录
                File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                    deleteFile(files[i].getAbsolutePath()); // 把每个文件 用这个方法进行迭代
                }
            }
            file.delete();
        } else {
            // Constants.Logdada("文件不存在！" + "\n");
        }
    }

    /**
     * 文件是否存在
     *
     * @param path
     * @return
     */
    public boolean isFileExit(String path) {
        if (StringUtils.isEmpty(path)) {
            return false;
        }
        try {
            File f = new File(path);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public void createFileDir(String path) {
        File destDir = new File(path);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
    }

    /**
     * 创建文件
     *
     * @param destFileName
     * @return
     */
    public boolean createFile(String destFileName) {
        File file = new File(destFileName);
        if (file.exists()) {
            System.out.println("创建单个文件" + destFileName + "失败，目标文件已存在！");
            return false;
        }
        if (destFileName.endsWith(File.separator)) {
            System.out.println("创建单个文件" + destFileName + "失败，目标文件不能为目录！");
            return false;
        }
        //判断目标文件所在的目录是否存在
        if (!file.getParentFile().exists()) {
            //如果目标文件所在的目录不存在，则创建父目录
            System.out.println("目标文件所在目录不存在，准备创建它！");
            if (!file.getParentFile().mkdirs()) {
                System.out.println("创建目标文件所在目录失败！");
                return false;
            }
        }
        //创建目标文件
        try {
            if (file.createNewFile()) {
                System.out.println("创建单个文件" + destFileName + "成功！");
                return true;
            } else {
                System.out.println("创建单个文件" + destFileName + "失败！");
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("创建单个文件" + destFileName + "失败！" + e.getMessage());
            return false;
        }
    }

    /**
     * 删除dirPath下的文件
     */
    public void deleteFileDir(String dirPath) {
        File dirFile = new File(dirPath);
        if (!dirFile.exists()) {
            return;
        }
        if (dirFile.isDirectory()) {
            String[] children = dirFile.list();
            for (int i = 0; i < children.length; i++) {
                new File(dirFile, children[i]).delete();
            }
        }
//		dirFile.delete();
    }

    /**
     * 通过URL获取下载文件名
     *
     * @param url
     * @return
     */
    public String getDownloadName(String url) {
        String downloadName = "";
        try {
            downloadName = url.substring(url.lastIndexOf("/") + 1);
        } catch (Exception e) {
            downloadName = "";
        }
        return downloadName;
    }

    /**
     * 从assets目录中复制整个文件夹内容
     *
     * @param context Context 使用CopyFiles类的Activity
     * @param oldPath String  原文件路径  如：/aa
     * @param newPath String  复制后路径  如：xx:/bb/cc
     */
    public void copyFilesFassets(android.content.Context context, String oldPath, String newPath) {
        try {
            String fileNames[] = context.getAssets().list(oldPath);//获取assets目录下的所有文件及目录名
            if (fileNames.length > 0) {//如果是目录
                File file = new File(newPath);
                file.mkdirs();//如果文件夹不存在，则递归
                for (String fileName : fileNames) {
                    copyFilesFassets(context, oldPath + "/" + fileName, newPath + "/" + fileName);
                }
            } else {//如果是文件
                java.io.InputStream is = context.getAssets().open(oldPath);
                java.io.FileOutputStream fos = new java.io.FileOutputStream(new File(newPath));
                byte[] buffer = new byte[1024];
                int byteCount = 0;
                while ((byteCount = is.read(buffer)) != -1) {//循环从输入流读取 buffer字节
                    fos.write(buffer, 0, byteCount);//将读取的输入流写入到输出流
                }
                fos.flush();//刷新缓冲区
                is.close();
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int copy(String fromFile, String toFile) {
        //要复制的文件目录
        File[] currentFiles;
        File root = new File(fromFile);
        //如同判断SD卡是否存在或者文件是否存在
        //如果不存在则 return出去
        if (!root.exists()) {
            return -1;
        }
        //如果存在则获取当前目录下的全部文件 填充数组
        currentFiles = root.listFiles();
        //目标目录
        File targetDir = new File(toFile);
        //创建目录
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }
        //遍历要复制该目录下的全部文件
        for (int i = 0; i < currentFiles.length; i++) {
            if (currentFiles[i].isDirectory())//如果当前项为子目录 进行递归
            {
                copy(currentFiles[i].getPath() + "/", toFile + currentFiles[i].getName() + "/");
            } else//如果当前项为文件则进行文件拷贝
            {
                CopySdcardFile(currentFiles[i].getPath(), toFile + currentFiles[i].getName());
            }
        }
        return 0;
    }

    //文件拷贝
    //要复制的目录下的所有非子目录(文件夹)文件拷贝
    public static int CopySdcardFile(String fromFile, String toFile) {
        try {
            java.io.InputStream fosfrom = new java.io.FileInputStream(fromFile);
            java.io.OutputStream fosto = new java.io.FileOutputStream(toFile);
            byte bt[] = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0) {
                fosto.write(bt, 0, c);
            }
            fosfrom.close();
            fosto.close();
            return 0;
        } catch (Exception ex) {
            return -1;
        }
    }

    /**
     * 获取图片旋转角度
     *
     * @param imagePath
     * @return
     */
    public int getImageRotation(String imagePath) {
        android.media.ExifInterface exif = null;
        try {
            exif = new android.media.ExifInterface(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
            exif = null;
        }
        int digree = -1;
        if (exif != null) {
			/*
			ffmpeg 旋转
			语法：transpose={0,1,2,3}
			0:逆时针旋转90°然后垂直翻转
			1:顺时针旋转90°
			2:逆时针旋转90°
			3:顺时针旋转90°然后水平翻转
			*/
            // 读取图片中相机方向信息
            int ori = exif.getAttributeInt(android.media.ExifInterface.TAG_ORIENTATION,
                    android.media.ExifInterface.ORIENTATION_UNDEFINED);
            // 计算旋转角度
            switch (ori) {
                case android.media.ExifInterface.ORIENTATION_ROTATE_90:
                    digree = 90;
                    break;
                case android.media.ExifInterface.ORIENTATION_ROTATE_180:
                    digree = 180;
                    break;
                case android.media.ExifInterface.ORIENTATION_ROTATE_270:
                    digree = 270;
                    break;
            }
        }
        return digree;
    }

    public boolean getImageWidthHeight(String path) {
        android.graphics.BitmapFactory.Options options = new android.graphics.BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        android.graphics.BitmapFactory.decodeFile(path, options); // 此时返回的bitmap为null
        return (((float) (options.outWidth)) / ((float) (options.outHeight))) >= (3.0 / 4.0);
    }


}






