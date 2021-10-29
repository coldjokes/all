package com.dosth.tool.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cnbaosi.dto.ElecLock;
import com.dosth.common.constant.YesOrNo;
import com.dosth.tool.entity.MatEquInfo;
import com.dosth.tool.entity.SubBox;
import com.dosth.tool.entity.SubBoxAccountRef;
import com.dosth.tool.entity.SubCabinetBill;
import com.dosth.tool.entity.SubCabinetDetail;
import com.dosth.tool.repository.SubBoxAccountRefRepository;
import com.dosth.tool.repository.SubBoxRepository;
import com.dosth.tool.repository.SubCabinetBillRepository;
import com.dosth.tool.repository.SubCabinetDetailRepository;
import com.dosth.tool.service.AdminService;
import com.dosth.tool.service.MatEquInfoService;
import com.dosth.tool.service.SubCabinetService;
import com.dosth.toolcabinet.dto.UserInfo;

/**
 * 副柜管理Service
 * 
 * @author guozhidong
 *
 */
@Service
@Transactional
public class SubCabinetServiceImpl implements SubCabinetService {

	@Autowired
	private AdminService adminService;
	@Autowired
    private MatEquInfoService matEquInfoService;
	@Autowired
	private SubBoxRepository subBoxRepository;
	@Autowired
	private SubBoxAccountRefRepository subBoxAccountRefRepository;
	@Autowired
	private SubCabinetDetailRepository subCabinetDetailRepository;
	@Autowired
    private SubCabinetBillRepository subCabinetBillRepository;

	@Override
	public ElecLock tmpNewStor(String matId, int matNum, String storType, String accountId) {
		ElecLock lock = new ElecLock();
        // 获取空闲暂存柜
        List<SubBox> subs = this.subBoxRepository.getUnUsedSubBoxList();
        Collections.sort(subs, new Comparator<SubBox>() {
            @Override
            public int compare(SubBox o1, SubBox o2) {
                return o1.getBoxIndex() - o2.getBoxIndex();
            }
        });
        
        // 获取使用中的抽屉信息
        List<SubBox> boxList = this.subBoxAccountRefRepository.getUsingSubBoxList(accountId);
        
        // 获取人员信息
        UserInfo user = this.adminService.getUserInfo(accountId);
        
        // 暂存柜分配区分处理
        switch(storType){
        case "NEWBOX":
        	// 获取空闲暂存柜信息
            SubBox extraBox = subs.get(0);
            // 新分配暂存柜与人员关联
            this.subBoxAccountRefRepository.save(new SubBoxAccountRef(accountId, extraBox.getId()));
            // 副柜详情关联物料信息
            this.subCabinetDetailRepository.save(new SubCabinetDetail(extraBox.getId(), matId, matNum));
            // 抽屉待存数量扣减
            extraBox.setExtraNum(extraBox.getExtraNum() - 1);
            this.subBoxRepository.saveAndFlush(extraBox);
            // 更新暂存柜流水
            subCabinetBillUpdate(user, extraBox, matId, matNum);
            lock = new ElecLock(extraBox.getEquSettingId(), extraBox.getBoardNo(), 
            		extraBox.getLockIndex(), extraBox.getBoxIndex());
        	break;
        case "NEWSEAT":
        	// 随意分配有暂存空间的抽屉
            for(SubBox box : boxList) {
                if(box.getExtraNum() > 0) {
                    // 抽屉待存数量扣减
                    box.setExtraNum(box.getExtraNum() - 1);
                    this.subBoxRepository.saveAndFlush(box);
                    // 副柜详情关联物料信息
                    this.subCabinetDetailRepository.save(new SubCabinetDetail(box.getId(), matId, matNum));
                    // 更新暂存柜流水
                    subCabinetBillUpdate(user, box, matId, matNum);
                    lock = new ElecLock(box.getEquSettingId(), box.getBoardNo(), 
                    		box.getLockIndex(), box.getBoxIndex());
                    break;
                }
            }
        	break;
        default :
        	List<SubCabinetDetail> detailList = 
        		this.subCabinetDetailRepository.getSubDetailListByAccountIdAndMatId(accountId, matId);
            SubCabinetDetail detail = detailList.get(0);
            SubBox subBox = detail.getSubBox();
            // 暂存总量合计
            detail.setNum(detail.getNum() + matNum);
            this.subCabinetDetailRepository.saveAndFlush(detail);
            // 更新暂存柜流水
            subCabinetBillUpdate(user, subBox, matId, matNum);
            lock = new ElecLock(subBox.getEquSettingId(), subBox.getBoardNo(), 
            		subBox.getLockIndex(), subBox.getBoxIndex());
        }
        return lock;
	}

	private void subCabinetBillUpdate(UserInfo user, SubBox subBox, String matId, int matNum) {
        // 获取物料详情
        MatEquInfo matInfo = this.matEquInfoService.get(matId);
        // 更新暂存柜流水
        float subMoney = (float) (Math.round(matInfo.getStorePrice() * matNum * 10000)) / 10000;
        // 添加暂存柜领取信息
        this.subCabinetBillRepository.save(new SubCabinetBill(subBox.getId(),
                subBox.getEquSetting().getEquSettingName(), matInfo.getMatEquName(), 
                matInfo.getBarCode(), matInfo.getSpec(), matNum, matInfo.getBorrowType().getMessage(), 
                matInfo.getStorePrice(), subMoney, user.getAccountId(),
                user.getUserName(), YesOrNo.YES, matInfo.getId()));
    }
}