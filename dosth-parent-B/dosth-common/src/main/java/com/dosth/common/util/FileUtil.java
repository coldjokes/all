package com.dosth.common.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.dosth.common.exception.DoSthException;
import com.dosth.common.exception.DoSthExceptionEnum;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 文件工具类
 * 
 * @author guozhidong
 *
 */
@SuppressWarnings("restriction")
public class FileUtil {

	private static Logger log = LoggerFactory.getLogger(FileUtil.class);

	/**
	 * 输出名称格式化
	 * 
	 * @param request
	 * @param fileName
	 * @return
	 */
	public static String processFileName(HttpServletRequest request, String fileName) {
		String codedFileName = null;
		try {
			String agent = request.getHeader("USER-AGENT");
			if (null != agent && agent.contains("MSIE") || null != agent && agent.contains("Trident")) { // ie
				codedFileName = java.net.URLEncoder.encode(fileName, "UTF8");
			} else if (null != agent && agent.contains("Mozilla")) { // 火狐,chrome等
				codedFileName = new String(fileName.getBytes("UTF-8"), "iso-8859-1");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return codedFileName;
	}

	/**
	 * 处理文件为流
	 * 
	 * @param fileName
	 * @return
	 */
	public static byte[] toByteArray(String fileName) {
		File file = new File(fileName);
		if (!file.exists()) {
			log.error("找不到文件" + fileName);
			throw new DoSthException(DoSthExceptionEnum.FILE_NOT_FOUND);
		}
		FileChannel channel = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			channel = fis.getChannel();
			ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());
			while ((channel.read(byteBuffer)) > 0) {
				// do nothing
			}
			return byteBuffer.array();
		} catch (IOException e) {
			throw new DoSthException(DoSthExceptionEnum.FILE_READING_ERROR);
		} finally {
			try {
				channel.close();
			} catch (IOException e) {
				throw new DoSthException(DoSthExceptionEnum.FILE_READING_ERROR);
			}
			try {
				fis.close();
			} catch (IOException e) {
				throw new DoSthException(DoSthExceptionEnum.FILE_READING_ERROR);
			}
		}
	}

	/**
	 * 拷贝文件
	 * 
	 * @param source 源文件
	 * @param dest   目标文件
	 * @throws IOException
	 */
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

	/**
	 * 保存文件，直接以multipartFile形式
	 * 
	 * @param multipartFile
	 * @param path          文件保存绝对路径
	 * @return 返回文件名
	 * @throws IOException
	 */
	public static String saveImg(MultipartFile multipartFile, String path) throws IOException {
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		FileInputStream fileInputStream = (FileInputStream) multipartFile.getInputStream();
		String fileName = UUID.randomUUID() + ".png";
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(path + File.separator + fileName));
		byte[] bs = new byte[1024];
		int len;
		while ((len = fileInputStream.read(bs)) != -1) {
			bos.write(bs, 0, len);
		}
		bos.flush();
		bos.close();
		return fileName;
	}

	/**
	 * 图片转化成base64字符串
	 * 
	 * @param imgFile
	 * @return
	 */
	public static String convertImageToBase64Data(File imgFile) {
		if (imgFile == null || !imgFile.exists()) {
			return null;
		}
		InputStream in = null;
		byte[] data = null;
		// 读取图片字节数组
		try {
			in = new FileInputStream(imgFile);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 对字节数组Base64编码
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(data);// 返回Base64编码过的字节数组字符串
	}

	/**
	 * base64字符串转化成png图片
	 * 
	 * @param imgData
	 * @return
	 */
	public static String generatePngImage(String imgData, String filePath) {
		String fileName = new StringBuilder(UUID.randomUUID().toString()).append(".png").toString();
		if (imgData == null) { // 图像数据为空
			throw new RuntimeException("传输图片数据为空!");
		}
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			// Base64解码
			byte[] b = decoder.decodeBuffer(imgData);
			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {// 调整异常数据
					b[i] += 256;
				}
			}
			// 生成png图片
			OutputStream out = new FileOutputStream(new StringBuffer(filePath).append(fileName).toString());
			out.write(b);
			out.flush();
			out.close();
		} catch (Exception e) {
			throw new RuntimeException("传输图片数据为空!");
		}
		return fileName;
	}
}