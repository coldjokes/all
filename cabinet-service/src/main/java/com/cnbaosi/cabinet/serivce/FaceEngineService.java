package com.cnbaosi.cabinet.serivce;

import com.arcsoft.face.toolkit.ImageInfo;
import com.cnbaosi.cabinet.entity.modal.User;
import com.arcsoft.face.FaceInfo;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface FaceEngineService {

	List<FaceInfo> detectFaces(ImageInfo imageInfo);

	/**
	 * 人脸特征
	 */
	public byte[] extractFaceFeature(ImageInfo imageInfo) throws InterruptedException;

	/**
	 * 人脸比对
	 */
	List<User> compareFaceFeature(byte[] faceFeature) throws InterruptedException, ExecutionException;

}
