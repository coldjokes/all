package com.dosth.admin.faceUtil.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.arcsoft.face.enums.DetectMode;
import com.arcsoft.face.enums.DetectOrient;
import com.arcsoft.face.toolkit.ImageInfo;
import com.dosth.admin.constant.state.ManagerStatus;
import com.dosth.admin.entity.User;
import com.dosth.admin.faceUtil.factory.FaceEngineFactory;
import com.dosth.admin.faceUtil.service.FaceEngineService;
import com.dosth.admin.repository.UserRepository;
import com.arcsoft.face.*;
import com.google.common.collect.Lists;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
public class FaceEngineServiceImpl implements FaceEngineService {

	public final static Logger logger = LoggerFactory.getLogger(FaceEngineServiceImpl.class);

	@Autowired
	private UserRepository userRepository;

	@Value("${dosth.sdkLibPath}")
	public String sdkLibPath;// = this.dosthProperties.getSdkLibPath();
	@Value("${dosth.appId}")
	public String appId;// = this.dosthProperties.getAppId();
	@Value("${dosth.sdkKey}")
	public String sdkKey;// = this.dosthProperties.getSdkKey();
	@Value("${dosth.passRate}")
	private Integer passRate;// = this.dosthProperties.getPassRate();
	@Value("5")
	public Integer threadPoolSize;

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
				new FaceEngineFactory(sdkLibPath, appId, sdkKey, engineConfiguration), poolConfig);// ????????????????????????

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
			if (CollectionUtil.isNotEmpty(livenessInfoList)) {
				if (livenessInfoList.get(0).getLiveness() != 0) {
					if (CollectionUtil.isNotEmpty(faceInfoList)) {
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
		List<User> faceInfoList = this.userRepository.findAll(); // ??????????????????????????????
		// ????????????????????????????????????
		faceInfoList = faceInfoList.stream().filter(face -> ManagerStatus.OK.equals(face.getAccount().getStatus())).collect(Collectors.toList());
		List<List<User>> faceUserInfoPartList = Lists.partition(faceInfoList, 1000);// ??????1000????????????????????????
		CompletionService<List<User>> completionService = new ExecutorCompletionService<List<User>>(executorService);
		for (List<User> part : faceUserInfoPartList) {
			completionService.submit(new CompareFaceTask(part, targetFaceFeature));
		}
		for (int i = 0; i < faceUserInfoPartList.size(); i++) {
			List<User> faceUserInfoList = completionService.take().get();
			if (CollectionUtil.isNotEmpty(faceInfoList)) {
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
					if (similarValue > passRate) {// ???????????????????????????????????????????????????????????????
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
