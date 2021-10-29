package com.dosth.admin.faceUtil.service;

import com.arcsoft.face.toolkit.ImageInfo;
import com.dosth.admin.entity.User;
import com.arcsoft.face.FaceInfo;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface FaceEngineService {

	List<FaceInfo> detectFaces(ImageInfo imageInfo);

	/**
	 * 人脸特征
	 * 
	 * @param imageInfo
	 * @return
	 */
	public byte[] extractFaceFeature(ImageInfo imageInfo) throws InterruptedException;

	/**
	 * 人脸比对
	 * 
	 * @param groupId
	 * @param faceFeature
	 * @return
	 */
	List<User> compareFaceFeature(byte[] faceFeature)
			throws InterruptedException, ExecutionException;

}
