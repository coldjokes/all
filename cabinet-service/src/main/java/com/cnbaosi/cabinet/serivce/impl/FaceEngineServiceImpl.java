package com.cnbaosi.cabinet.serivce.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.arcsoft.face.EngineConfiguration;
import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.FaceFeature;
import com.arcsoft.face.FaceInfo;
import com.arcsoft.face.FaceSimilar;
import com.arcsoft.face.FunctionConfiguration;
import com.arcsoft.face.LivenessInfo;
import com.arcsoft.face.enums.DetectMode;
import com.arcsoft.face.enums.DetectOrient;
import com.arcsoft.face.toolkit.ImageInfo;
import com.cnbaosi.cabinet.config.CabinetServiceConfig;
import com.cnbaosi.cabinet.entity.modal.User;
import com.cnbaosi.cabinet.faceUtil.factory.FaceEngineFactory;
import com.cnbaosi.cabinet.serivce.FaceEngineService;
import com.cnbaosi.cabinet.serivce.UserService;
import com.google.common.collect.Lists;

@Service
public class FaceEngineServiceImpl implements FaceEngineService {

	public final static Logger logger = LoggerFactory.getLogger(FaceEngineServiceImpl.class);

	@Autowired
	private UserService userSvc;
	@Autowired
	private CabinetServiceConfig cabinetConfig;
	
	public Integer threadPoolSize = 5;

	private GenericObjectPool<FaceEngine> faceEngineObjectPool;
	private ExecutorService executorService;

	@PostConstruct
	public void init() {
		
		executorService = Executors.newFixedThreadPool(threadPoolSize);
		GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
		poolConfig.setMaxIdle(threadPoolSize);
		poolConfig.setMaxTotal(threadPoolSize);
		poolConfig.setMinIdle(threadPoolSize);
		poolConfig.setLifo(false);

		// ????????????
		EngineConfiguration engineConfiguration = new EngineConfiguration();
		engineConfiguration.setDetectMode(DetectMode.ASF_DETECT_MODE_IMAGE);
		engineConfiguration.setDetectFaceOrientPriority(DetectOrient.ASF_OP_0_ONLY);

		// ????????????
		FunctionConfiguration functionConfiguration = new FunctionConfiguration();
		functionConfiguration.setSupportAge(true);
		functionConfiguration.setSupportFace3dAngle(true);
		functionConfiguration.setSupportFaceDetect(true);
		functionConfiguration.setSupportFaceRecognition(true);
		functionConfiguration.setSupportGender(true);
		functionConfiguration.setSupportLiveness(true);
		functionConfiguration.setSupportIRLiveness(true);
		engineConfiguration.setFunctionConfiguration(functionConfiguration);

		faceEngineObjectPool = new GenericObjectPool<FaceEngine>(
				new FaceEngineFactory(
						cabinetConfig.getFaceSdkLibPath(), 
						cabinetConfig.getFaceAppId(), 
						cabinetConfig.getFaceSdkKey(), 
						engineConfiguration), 
				poolConfig);// ????????????????????????

	}

	private int plusHundred(Float value) {
		BigDecimal target = new BigDecimal(value);
		BigDecimal hundred = new BigDecimal(100f);
		return target.multiply(hundred).intValue();

	}

	@Override
	public List<FaceInfo> detectFaces(ImageInfo imageInfo) {
		FaceEngine faceEngine = null;
		try {
			// ??????????????????
			faceEngine = faceEngineObjectPool.borrowObject();
			// ??????????????????????????????
			List<FaceInfo> faceInfoList = new ArrayList<FaceInfo>();
			// ????????????
			int detectFaces = faceEngine.detectFaces(imageInfo.getImageData(), imageInfo.getWidth(),
					imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList);
			logger.error("detectFaces-???????????????" + detectFaces);
			return faceInfoList;
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			if (faceEngine != null) {
				// ??????????????????
				faceEngineObjectPool.returnObject(faceEngine);
			}
		}
		return null;
	}

	/**
	 * ????????????
	 *
	 * @param imageInfo
	 * @return
	 */
	@Override
	public byte[] extractFaceFeature(ImageInfo imageInfo) throws InterruptedException {
		FaceEngine faceEngine = null;
		try {
			// ??????????????????
			faceEngine = faceEngineObjectPool.borrowObject();
			// ??????????????????????????????
			List<FaceInfo> faceInfoList = new ArrayList<FaceInfo>();
			// ????????????
			int detectFaces = faceEngine.detectFaces(imageInfo.getImageData(), imageInfo.getWidth(),
					imageInfo.getHeight(), imageInfo.getImageFormat(), faceInfoList);
			logger.error("extractFaceFeature-???????????????" + detectFaces);

			// ????????????
			int process = faceEngine.process(imageInfo.getImageData(), imageInfo.getWidth(), imageInfo.getHeight(),
					imageInfo.getImageFormat(), faceInfoList,
					FunctionConfiguration.builder().supportLiveness(true).build());
			logger.error("extractFaceFeature-???????????????" + process);

			List<LivenessInfo> livenessInfoList = new ArrayList<LivenessInfo>();
			faceEngine.getLiveness(livenessInfoList);
			if (CollectionUtils.isNotEmpty(livenessInfoList)) {
				if (livenessInfoList.get(0).getLiveness() != 0) {
					if (CollectionUtils.isNotEmpty(faceInfoList)) {
						FaceFeature faceFeature = new FaceFeature();
						// ??????????????????
						int extractFaceFeature = faceEngine.extractFaceFeature(imageInfo.getImageData(),
								imageInfo.getWidth(), imageInfo.getHeight(), imageInfo.getImageFormat(),
								faceInfoList.get(0), faceFeature);
						logger.error("extractFaceFeature-?????????????????????" + extractFaceFeature);
						return faceFeature.getFeatureData();
					}
				}
			}
		} catch (Exception e) {
			logger.error("", e);
		} finally {
			if (faceEngine != null) {
				// ??????????????????
				faceEngineObjectPool.returnObject(faceEngine);
			}
		}
		return null;
	}

	@Override
	public List<User> compareFaceFeature(byte[] faceFeature) throws InterruptedException, ExecutionException {
		List<User> resultFaceInfoList = Lists.newLinkedList();// ????????????????????????

		FaceFeature targetFaceFeature = new FaceFeature();
		targetFaceFeature.setFeatureData(faceFeature);
		
		List<User> faceInfoList = userSvc.getAll(); // ??????????????????????????????
		List<List<User>> faceUserInfoPartList = Lists.partition(faceInfoList, 1000);// ??????1000????????????????????????
		CompletionService<List<User>> completionService = new ExecutorCompletionService<List<User>>(executorService);
		for (List<User> part : faceUserInfoPartList) {
			completionService.submit(new CompareFaceTask(part, targetFaceFeature));
		}
		for (int i = 0; i < faceUserInfoPartList.size(); i++) {
			List<User> faceUserInfoList = completionService.take().get();
			if (CollectionUtils.isNotEmpty(faceInfoList)) {
				resultFaceInfoList.addAll(faceUserInfoList);
			}
		}
		return resultFaceInfoList;
	}

	private class CompareFaceTask implements Callable<List<User>> {

		private List<User> faceUserInfoList;
		private FaceFeature targetFaceFeature;

		public CompareFaceTask(List<User> faceUserInfoList, FaceFeature targetFaceFeature) {
			this.faceUserInfoList = faceUserInfoList;
			this.targetFaceFeature = targetFaceFeature;
		}

		@Override
		public List<User> call() throws Exception {
			FaceEngine faceEngine = null;
			List<User> userList = Lists.newLinkedList();// ????????????????????????
			try {
				faceEngine = faceEngineObjectPool.borrowObject();
				for (User faceUserInfo : faceUserInfoList) {
					FaceFeature sourceFaceFeature = new FaceFeature();
					sourceFaceFeature.setFeatureData(faceUserInfo.getFaceFeature()); 
					FaceSimilar faceSimilar = new FaceSimilar();
					int compareFaceFeature = faceEngine.compareFaceFeature(targetFaceFeature, sourceFaceFeature,
							faceSimilar);
					logger.error("compareFaceFeature-???????????????" + compareFaceFeature);
					Integer similarValue = plusHundred(faceSimilar.getScore());// ???????????????
					logger.error("plusHundred-??????????????????" + similarValue);
					if (similarValue > cabinetConfig.getFacePassRate()) {// ???????????????????????????????????????????????????????????????
						User info = new User();
						info.setId(faceUserInfo.getId());
						userList.add(info);
					}
				}
			} catch (Exception e) {
				logger.error("", e);
			} finally {
				if (faceEngine != null) {
					faceEngineObjectPool.returnObject(faceEngine);
				}
			}

			return userList;
		}

	}
}
