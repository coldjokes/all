package com.cnbaosi.cabinet.faceUtil.factory;
 
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.arcsoft.face.EngineConfiguration;
import com.arcsoft.face.FaceEngine;
 
public class FaceEngineFactory extends BasePooledObjectFactory<FaceEngine> {
 
    public final static Logger logger = LoggerFactory.getLogger(FaceEngineFactory.class);
 
    private String appId;
    private String sdkKey;
    private String sdkLibPath;
    private EngineConfiguration engineConfiguration;
//    private Integer detectFaceMaxNum = 10;
//    private Integer detectFaceScaleVal = 16;
//    private DetectMode detectMode = DetectMode.ASF_DETECT_MODE_IMAGE;
//    private DetectOrient detectFaceOrientPriority = DetectOrient.ASF_OP_0_ONLY;
 
    public FaceEngineFactory(String sdkLibPath, String appId, String sdkKey, EngineConfiguration engineConfiguration) {
        this.sdkLibPath = sdkLibPath;
        this.appId = appId;
        this.sdkKey = sdkKey;
        this.engineConfiguration = engineConfiguration;
 
    }
 
    @Override
    public FaceEngine create() throws Exception {
        FaceEngine faceEngine = new FaceEngine(sdkLibPath);
        logger.info("===============人脸识别初始化===============");
        int activeCode = faceEngine.activeOnline(appId, sdkKey);
        logger.info("在线激活SDK：" + activeCode);
        int initCode = faceEngine.init(engineConfiguration);
        logger.info("初始化人脸引擎：" + initCode);
        return faceEngine;
    }
 
    @Override
    public PooledObject<FaceEngine> wrap(FaceEngine faceEngine) {
        return new DefaultPooledObject<>(faceEngine);
    }
 
    @Override
    public void destroyObject(PooledObject<FaceEngine> p) throws Exception {
        FaceEngine faceEngine = p.getObject();
        int unInitCode = faceEngine.unInit();
        logger.error("unInitCode：" + unInitCode);
        super.destroyObject(p);
    }
}