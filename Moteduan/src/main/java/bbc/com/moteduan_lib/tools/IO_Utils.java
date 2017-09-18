package bbc.com.moteduan_lib.tools;

import java.io.IOException;

import bbc.com.moteduan_lib.log.LogDebug;

public class IO_Utils {


	/**
	 * 保存图到应用外部的缓存目录
	 * @param bmp
	 * @param externalCacheFile  带文件名(格式jpg)的file
	 * @return 返回 存储绝对路径
	 */
	public String saveBitmapCache(android.graphics.Bitmap bmp, java.io.File externalCacheFile) {

		java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream(bmp.getByteCount());
		// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		bmp.compress(android.graphics.Bitmap.CompressFormat.JPEG, 100, out);

		java.io.FileOutputStream fos = null;
		try {
			fos = new java.io.FileOutputStream(externalCacheFile);
		} catch (java.io.FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			out.writeTo(fos);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return externalCacheFile.getAbsolutePath();
	}


	/**
	 * 保持字节到本地
	 * @param saveToFile
	 * @param name
	 * @param bytes
     * @return
     */
	public String sava(java.io.File saveToFile, String name, byte[] bytes){

		java.io.FileOutputStream out = null;
		java.io.File file = new java.io.File(saveToFile.getAbsolutePath()+"/"+name);
		try {
			out = new java.io.FileOutputStream(file);
		} catch (java.io.FileNotFoundException e) {
			e.printStackTrace();
		}

		try {
			out.write(bytes,0,bytes.length);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return file.getAbsolutePath();
	}

	/**
	 * 使用文件字节流复制（路径 课在file对象创建时 指定） 字节流和字节缓冲流 读写 方法一样
	 * @param oldFile
	 * @param newFile
	 */
	public void copyByInputStream(java.io.File oldFile, java.io.File newFile){

		try {
			java.io.FileOutputStream out = new java.io.FileOutputStream(newFile);
			java.io.FileInputStream in  = new java.io.FileInputStream(oldFile);
			try {

				/*
				 * read()
				 *  一次读取一个byte字节（8bit位），返回0到255的char类型，
				 * 不能读汉字（占1个char字符，2个字节，16位）
				 * 每次都去都会光标后移  返回 -1 是读取结束
				 */
//				System.out.println(in.read());

				byte[] bt = new byte[1024];
				int readSum = 0;

//				int readSum = read(byte[]) 一次读取 一个字节数组的数据到byte[]
				while( -1 != (readSum = in.read(bt))){
//					write(byte[],start,end) 将byte[]中数据写入，从0到该数组的长度。
					out.write(bt, 0, readSum);
				}

				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				try {
					in.close();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (java.io.FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 使用缓冲 字节流 复制文件 （（路径 课在file对象创建时 指定） 字节流和字节缓冲流 读写 方法一样）
	 * @param oldFile
	 * @param newFile
	 */
	public void copyBy_BufferedInputStream_BufferedOutputStream(java.io.File oldFile, java.io.File newFile){
		/*
		 * 不带缓冲的操作，每读一个字节就要写入一个字节，由于涉及磁盘的IO操作相比内存的操作要慢很多，
		 * 所以不带缓冲的流效率很低。带缓冲的流，可以一次读很多字节，但不向磁盘中写入，只是先放到内存里。
		 * 等凑够了缓冲区大小的时候一次性写入磁盘，
		 * 这种方式可以减少磁盘操作次数，速度就会提高很多
		 */
		try {

			java.io.FileInputStream i  = new java.io.FileInputStream(oldFile);
			java.io.BufferedInputStream in = new java.io.BufferedInputStream(i);

			java.io.FileOutputStream o = new java.io.FileOutputStream(newFile);
			java.io.BufferedOutputStream out = new java.io.BufferedOutputStream(o);

			try {

				/*
				 * read()
				 *  一次读取一个byte字节（8bit位），返回0到255的char类型，
				 * 不能读汉字（占1个char字符，2个字节，16位）
				 * 每次都去都会光标后移  返回 -1 是读取结束
				 */
//				System.out.println(in.read());

				byte[] bt = new byte[1024];
				int readSum = 0;

//				int readSum = read(byte[]) 一次读取 一个字节数组的数据到byte[]
				while( -1 != (readSum = in.read(bt))){
//					write(byte[],start,end) 将byte[]中数据写入，从0到该数组的长度。
					out.write(bt, 0, readSum);
				}

				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				try {
					in.close();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (java.io.FileNotFoundException e) {
			e.printStackTrace();
		}

	}



	/**
	 * 使用字符流   复制文本文件 （（路径 课在file对象创建时 指定） 字节流和字节缓冲流 读写 方法一样）
	 * @param oldFile
	 * @param newFile
	 * @throws IOException
	 */
	public void copyBy_reader_writer(java.io.File oldFile, java.io.File newFile) throws IOException {

		java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(oldFile));
		java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter(newFile));

		String str = null;
		int i=0;
		if( (str = reader.readLine()) != null ){

			if(i == 1){
				i = 1 ;
				writer.newLine();
			}
			writer.write( str );
		}
		reader.close();
		writer.flush();
		writer.close();

	}

	public SaveSuccessCallBackListener saveSuccess;
	public interface SaveSuccessCallBackListener{
		void onSaveSuccess(String path);
		void onFail(String errMsg);
	}

	/**
	 * 保存文件到本地自定义目录
	 * @param stream
	 * @param absolutePath xz/xx.mp4
     */
	public String saveFile(java.io.InputStream stream, String absolutePath, SaveSuccessCallBackListener saveSuccess) {

		this.saveSuccess = saveSuccess;

		byte[] bt = new byte[1024];
		java.io.FileOutputStream out = null;
		java.io.BufferedInputStream bufferInput = new java.io.BufferedInputStream(stream);
		try {
			out = new java.io.FileOutputStream(absolutePath);
			while ((bufferInput.read(bt,0,bt.length )) != -1){
				out.write(bt,0,bt.length);
			}

			if(saveSuccess != null){
				saveSuccess.onSaveSuccess(absolutePath);
			}
		} catch (IOException e) {
			e.printStackTrace();
			if(saveSuccess != null){
				saveSuccess.onFail(e.toString());
			}
		}finally {
			if(out != null){
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if(bufferInput != null){
				try {
					bufferInput.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		LogDebug.print(" 保存文件到本地置顶目录 = " + absolutePath);
		return absolutePath;
	}


}























