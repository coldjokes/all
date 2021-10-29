package com.dosth.tool.service.impl;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dosth.common.exception.DoSthException;
import com.dosth.tool.entity.MatEquInfo;
import com.dosth.tool.entity.SubBox;
import com.dosth.tool.entity.SubBoxAccountRef;
import com.dosth.tool.repository.SubBoxAccountRefRepository;
import com.dosth.tool.repository.SubBoxRepository;
import com.dosth.tool.repository.SubCabinetDetailRepository;
import com.dosth.tool.service.ExtraBoxNumSettingService;
import com.dosth.tool.service.SubBoxAccountRefService;
import com.dosth.util.OpTip;

/**
 * 
 * @description 副柜盒子与帐户关联Service实现
 * @author guozhidong
 *
 */
@Service
@Transactional
public class SubBoxAccountRefServiceImpl implements SubBoxAccountRefService {

	@Autowired
	private SubBoxAccountRefRepository subBoxAccountRefRepository;
	@Autowired
	private SubBoxRepository subBoxRepository;
	@Autowired
	private SubCabinetDetailRepository subCabinetDetailRepository;
	@Autowired
	private ExtraBoxNumSettingService extraBoxNumSettingService;

	@Override
	public void save(SubBoxAccountRef ref) throws DoSthException {
		this.subBoxAccountRefRepository.save(ref);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public SubBoxAccountRef get(Serializable id) throws DoSthException {
		return this.subBoxAccountRefRepository.getOne(id);
	}

	@Override
	public SubBoxAccountRef update(SubBoxAccountRef ref) throws DoSthException {
		return this.subBoxAccountRefRepository.saveAndFlush(ref);
	}

	@Override
	public void delete(SubBoxAccountRef ref) throws DoSthException {
		this.subBoxAccountRefRepository.delete(ref);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public List<SubBox> getUsingSubBoxList(String accountId) {
		return this.subBoxAccountRefRepository.getUsingSubBoxList(accountId);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public SubBoxAccountRef getAccountBySubBoxId(String subBoxId) {
		SubBoxAccountRef ref = this.subBoxAccountRefRepository.getAccountBySubBoxId(subBoxId);
		return ref;
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public OpTip checkNewSubCabinet(String matId, String accountId) {
		// 暂存柜分配分类
		// NONEED：不需要新分配抽屉或者库位；
		// NEWBOX：需要分配新的抽屉；
		// NEWSEAT：不需要新的抽屉但是要分配新的库位；
		String info = "NONEED";
        // 获取空闲暂存柜
        List<SubBox> subs = this.subBoxRepository.getUnUsedSubBoxList();
        // 判断该物料是否已存在于暂存柜
        Boolean notExistFlag = true;
        List<MatEquInfo> matInfoList = this.subCabinetDetailRepository.getSubCabinetMatList(accountId);
        for(MatEquInfo matInfo : matInfoList) {
            if(matInfo.getId().equals(matId)) {
                notExistFlag = false;
                break;
            }
        }
        
        // 判断是否要分配新的暂存柜抽屉
        Boolean newFlag = true;
        List<SubBox> boxList = this.subBoxAccountRefRepository.getUsingSubBoxList(accountId);
        if(notExistFlag) {
            for(SubBox box : boxList) {
            	// 判断抽屉是否有空余库位
                if(box.getExtraNum() > 0) {
                    newFlag = false;
                    break;
                }
            }
            // 判断用户是否有剩余使用量
            if(newFlag) {
            	// 判断暂存柜是否有未使用的抽屉
                if (subs == null || subs.size() < 1) {
                    return new OpTip(202, "没有空闲的暂存格子");
                }
                
            	// 获取该用户分配的暂存柜数量
            	Integer extraBoxNum = this.extraBoxNumSettingService.getExtraBoxNumByAccountId(accountId);
            	if (extraBoxNum == 0 || boxList.size() >= extraBoxNum) {
            		 return new OpTip(201, "你使用的暂存柜数量已达到允许最大值");
        		}
            	info = "NEWBOX";
            }else {
            	info = "NEWSEAT";
            }
        }
        return new OpTip(200, info);
	}
}