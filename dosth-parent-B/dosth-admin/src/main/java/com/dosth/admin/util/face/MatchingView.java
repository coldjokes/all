package com.dosth.admin.util.face;

import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;

/**
 * 计算并输入结果
 * 
 * @author guozhidong
 *
 */
public interface MatchingView {
	/**
	 * 设置目标图片
	 */
	public void setDstPic(String dstPath);

	/**
	 * 设置源图片
	 */
	public void setSrcPic(String srcPath);

	/**
	 * 
	 * @param matches 
	 * @param srcKP 源匹配点
	 * @param dstKP 目标匹配点
	 * @return
	 */
	public int showView(MatOfDMatch matches, MatOfKeyPoint srcKP, MatOfKeyPoint dstKP);
}
