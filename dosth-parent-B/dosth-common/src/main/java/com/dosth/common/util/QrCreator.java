package com.dosth.common.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

/**
 * @description 二维码生成器
 * @author guozhidong
 *
 */
public final class QrCreator {
	
	private static QrCreator creator = new QrCreator();
	private static String filePath;
	
	private static final int BLACK = 0xFF000000;
	private static final int WHITE = 0xFFFFFFFF;

	private QrCreator() {
	}
	
	public static QrCreator getInstance(String tmpFilePath) {
		filePath = tmpFilePath;
		return creator;
	}

	public BufferedImage toBufferedImage(BitMatrix matrix) {
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
			}
		}
		return image;
	}

	public void writeToFile(BitMatrix matrix, String format, File file) throws IOException {
		BufferedImage image = toBufferedImage(matrix);
		if (!ImageIO.write(image, format, file)) {
			throw new IOException("Could not write an image of format " + format + " to " + file);
		}
	}

	public void writeToStream(BitMatrix matrix, String format, OutputStream stream) throws IOException {
		BufferedImage image = toBufferedImage(matrix);
		if (!ImageIO.write(image, format, stream)) {
			throw new IOException("Could not write an image of format " + format);
		}
	}
	
	/**
	 * @description 创建二维码图片
	 * @param content 二维码内容
	 * @param fileName 文件名称
	 */
	public void createImage(String content, String fileName) {
		 try {
			String imageFormat = "jpg";
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter(); 
            Map<EncodeHintType, String> hints = new HashMap<>();     
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");  
            BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, 400, 400,hints);   
            File file1 = new File(filePath, new StringBuilder(fileName).append(".").append(imageFormat).toString()); 
            writeToFile(bitMatrix, imageFormat, file1);
        } catch (IOException | WriterException e) {
            e.printStackTrace();
        } 
	}
	
	/**
	 * @description 创建二维码图片
	 * @param content 二维码内容
	 * @param path 图片路径
	 */
	public String convertQrToBase64Data(String content) {
		 try {
			String imageFormat = "jpg";
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter(); 
            Map<EncodeHintType, String> hints = new HashMap<>();     
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");  
            BitMatrix bitMatrix = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, 400, 400,hints);   
            File file = new File(filePath, 
            		new StringBuilder(UUID.randomUUID().toString()).append(".").append(imageFormat).toString()); 
            writeToFile(bitMatrix, imageFormat, file);
            if (file.exists()) {
            	return FileUtil.convertImageToBase64Data(file);
            }
        } catch (IOException | WriterException e) {
            e.printStackTrace();
        } 
        return null;
	}
}