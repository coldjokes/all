package com.dosth.admin.util.face;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.highgui.Highgui;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 特征匹配
 * 
 * @author guozhidong
 *
 */
public class FeatureMatchingUtil {

	private static final Logger logger = LoggerFactory.getLogger(FeatureMatchingUtil.class);

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}
	
	private static MatOfKeyPoint srcKeyPoints = new MatOfKeyPoint();
	private static Mat srcDes = new Mat();

	private static FeatureDetector detector = FeatureDetector.create(FeatureDetector.SURF);
	private static DescriptorExtractor extractor = DescriptorExtractor.create(DescriptorExtractor.SURF);
	private static DescriptorMatcher matcher = DescriptorMatcher.create(DescriptorMatcher.FLANNBASED);

	private static MatchingView view = new ConsoleView();

	/**
	 * 匹配
	 * 
	 * @param srcPath
	 *            源图片路径
	 * @param dstPath
	 *            目标图片信息
	 * @return 匹配度
	 */
	public static int doMaping(String srcPath, String dstPath) {
		setSource(srcPath);
		view.setDstPic(dstPath);
		// 读入待测图像
		Mat dst = Highgui.imread(dstPath);
		logger.info("DST W: {}  H: {}", new Object[] { dst.cols(), dst.rows()});
		// 待测图像的关键点
		MatOfKeyPoint dstKeyPoints = new MatOfKeyPoint();
		detector.detect(dst, dstKeyPoints);
		// 待测图像的特征矩阵
		Mat dstDes = new Mat();
		extractor.compute(dst, dstKeyPoints, dstDes);
		// 与原图匹配
		MatOfDMatch matches = new MatOfDMatch();
		matcher.match(dstDes, srcDes, matches);
		// 将结果输入到视图,并得到"匹配度"
		return view.showView(matches, srcKeyPoints, dstKeyPoints);
	}

	/**
	 * 设置源图片
	 * 
	 * @param srcPath
	 */
	private static void setSource(String srcPath) {
		view.setSrcPic(srcPath);
		// 读取图像,写入矩阵
		Mat src = Highgui.imread(srcPath);
		logger.info("SRC W: {}  H: {}", new Object[] { src.cols(), src.rows()});
		// 检测关键点
		detector.detect(src, srcKeyPoints);
		// 根据源图像、关键点产生特征矩阵数值
		extractor.compute(src, srcKeyPoints, srcDes);
	}
}