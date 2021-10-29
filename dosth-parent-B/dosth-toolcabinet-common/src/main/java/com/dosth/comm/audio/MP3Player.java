package com.dosth.comm.audio;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javazoom.jl.player.Player;

/**
 * 
 * @description MP3播放器
 * @author guozhidong
 *
 */
public class MP3Player {
	public static final Logger logger = LoggerFactory.getLogger(MP3Player.class);
    private static String Audio_Config_Folder = "D:\\AudioConfig";
    
    public static void play(String fileName) {
		try {
            BufferedInputStream buffer = new BufferedInputStream(new FileInputStream(Audio_Config_Folder + File.separator + fileName));
            Player player = new Player(buffer);
            player.play();
        } catch (Exception e) {
        	e.printStackTrace();
        }
	}
}