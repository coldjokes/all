package com.cnbaosi.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javazoom.jl.player.Player;

/**
 * @description 声音播放工具类
 * @author guozhidong
 *
 */
public class SoundPlayUtil {

	private static final Logger logger = LoggerFactory.getLogger(SoundPlayUtil.class);

	/**
	 * @description 播放路径下音频文件
	 * @param filePath 音频文件路径
	 */
	public static void play(String filePath) {
		try {
			logger.info("播放音频路径:" + filePath);
			BufferedInputStream buffer = new BufferedInputStream(new FileInputStream(filePath));
			Player player = new Player(buffer);
			player.play();
		} catch (Exception e) {
			logger.error("音频播放失败:" + e.getMessage());
			e.printStackTrace();
		}
	}
}