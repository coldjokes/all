package com.exam.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件工具类
 * 
 * @author Yifeng Wang
 */

public class FileUtil {

	/**
	 * 保存文件
	 * 
	 * @param path
	 * @param file
	 * @return
	 */
	public static String saveFile(String path, MultipartFile file) {
		String newFileName = null;
		try {
			newFileName = getNewFileName(file.getOriginalFilename());
		} catch (Exception e) {
			e.printStackTrace();
		}
		File targetFile = new File(path);
		if (!targetFile.exists()) {
			targetFile.mkdirs();
		}
		try (FileOutputStream out = new FileOutputStream(path + newFileName)) {
			out.write(file.getBytes());
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return newFileName;
	}

	/**
	 * 获取上传后的文件名
	 * 
	 * @param sourceFileName
	 * @return
	 * @throws Exception
	 */
	public static String getNewFileName(String sourceFileName) throws Exception {
		// 获取扩展名
//        String extensionName = StringUtils.substringAfterLast(sourceFileName, ".");
		String[] nameArr = sourceFileName.split("[.]");
		if (nameArr.length < 2) {
			throw new Exception();
		}
		String newName = DateTimeUtil.format(DateTimeUtil.FMT_YMDHMS) + System.nanoTime();
		// 新的文件名 = 时间戳.扩展名
		return newName + "." + nameArr[1];
	}

	@SuppressWarnings("resource")
	public static void copyFileUsingFileChannels(File source, File dest) throws IOException {
		FileChannel inputChannel = null;
		FileChannel outputChannel = null;
		try {
			inputChannel = new FileInputStream(source).getChannel();
			outputChannel = new FileOutputStream(dest).getChannel();
			outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
		} finally {
			inputChannel.close();
			outputChannel.close();
		}
	}

	//删除文件
	public static void deleteFile(String path, String filename) {
		File file = new File(path + File.separator + filename);
		if (file.exists() && file.isFile())
			file.delete();
	}

	/**
	 * 删除目录
	 * @param path
	 */
	public static void deleteFolder(String path) {
		File dir = new File(path);
		if (dir.exists()) {
			File[] tmp = dir.listFiles();
			for (int i = 0; i < tmp.length; i++) {
				if (tmp[i].isDirectory()) {
					deleteFolder(path + File.separator + tmp[i].getName());
				} else {
					tmp[i].delete();
				}
			}
			dir.delete();
		}
	}
}
