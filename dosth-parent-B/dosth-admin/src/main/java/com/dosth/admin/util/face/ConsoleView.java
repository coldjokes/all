package com.dosth.admin.util.face;

import java.util.LinkedList;
import java.util.List;

import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DMatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 控制台视图
 * 
 * @author guozhidong
 *
 */
public class ConsoleView implements MatchingView {
	
	private static final Logger logger = LoggerFactory.getLogger(ConsoleView.class);

	@Override
	public void setDstPic(String dstPath) {
	}

	@Override
	public void setSrcPic(String srcPath) {
	}

	@Override
	public int showView(MatOfDMatch matches, MatOfKeyPoint srcKP, MatOfKeyPoint dstKP) {
		logger.info("{} Match Point(s)", matches.rows());

		double maxDist = Double.MIN_VALUE;
		double minDist = Double.MAX_VALUE;

		DMatch[] mats = matches.toArray();
		for (int i = 0; i < mats.length; i++) {
			double dist = mats[i].distance;
			if (dist < minDist) {
				minDist = dist;
			}
			if (dist > maxDist) {
				maxDist = dist;
			}
		}
		logger.info("Min Distance: {}", minDist);
		logger.info("Max Distance: {}", maxDist);

		// 将"好"的关键点记录,即距离小于3倍最小距离,同时给定一个阀值(0.2f),这样不至于在毫不相干的图像上分析,可依据实际情况调整
		List<DMatch> goodMatch = new LinkedList<>();
		for (int i = 0; i < mats.length; i++) {
			double dist = mats[i].distance;
			if (dist < 3 * minDist && dist < 0.2f) {
				goodMatch.add(mats[i]);
			}
		}
		logger.info("{} GoodMatch Found", goodMatch.size());
		return goodMatch.size();
	}
}