package com.dosth.tool.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dosth.common.util.DateUtil;
import com.dosth.tool.common.config.ToolProperties;
import com.dosth.tool.common.state.AuditStatus;
import com.dosth.tool.common.state.VerifyMode;
import com.dosth.tool.entity.BorrowInfo;
import com.dosth.tool.entity.MatReturnBack;
import com.dosth.tool.repository.MatReturnBackRepository;
import com.dosth.tool.repository.UserRepository;
import com.dosth.tool.service.ScanCabinetService;
import com.dosth.util.OpTip;

/**
 * 
 * @description 归还审核（PC端扫码枪）请求Service实现
 * @author chenlei
 *
 */
@Service
@Transactional
public class ScanCabinetServiceImpl implements ScanCabinetService {

	@Autowired
	private ToolProperties toolProperties;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private MatReturnBackRepository matReturnBackRepository;

	@Override
	public List<BorrowInfo> recycleInfo(String code) {
		List<BorrowInfo> info = new ArrayList<BorrowInfo>();
		int returnBackNo = -1;
		if (code.length() == 8 && (code.toUpperCase().indexOf("M") == 0 || code.toUpperCase().indexOf("S") == 0)) {
			returnBackNo = Integer.parseInt(code.substring(1));
		}

		// 物料归还信息取得
		List<MatReturnBack> backList = this.matReturnBackRepository.findBybarCode(code, returnBackNo);

		for (MatReturnBack back : backList) {
			info.add(new BorrowInfo(back.getMatUseBill().getMatUseRecord().getBarCode(),
					back.getMatUseBill().getMatUseRecord().getMatInfoName(),
					back.getMatUseBill().getMatUseRecord().getUserName(), back.getReturnDetailInfo(),
					DateUtil.format(back.getOpDate(),"yyyy-MM-dd HH:mm:ss"),
					String.valueOf(back.getMatUseBill().getMatUseRecord().getPackNum()),
					this.toolProperties.getImgUrlPath() + back.getMatUseBill().getMatUseRecord().getMatInfo().getIcon(),
					back.getAccountId()));
		}
		// 待审核刀具信息设定
		return info;
	}

	@Override
	@Transactional
	public OpTip recycleReject(String code, int num, String content, String userId) {
		OpTip tip = new OpTip(200, "提交成功");

		try {
			int returnBackNo = 0;
			if (code.length() == 8 && (code.toUpperCase().indexOf("M") == 0 || code.toUpperCase().indexOf("S") == 0)) {
				returnBackNo = Integer.parseInt(code.substring(1));
			}
			List<MatReturnBack> backList = matReturnBackRepository.findBybarCode(code, returnBackNo);
			for (MatReturnBack matReturnBack : backList) {
				// 数量不符以外，默认实际归还数量
				if (!content.equals("数量不符")) {
					num = matReturnBack.getNum();
				}
				matReturnBack.setConfirmUser(this.userRepository.findUserByAccountId(userId).getUserName());
				matReturnBack.setConfirmMode(VerifyMode.SCANCONF);
				matReturnBack.setConfirmDate(new Date());
				matReturnBack.setAuditStatus(AuditStatus.NOT_PASS);
				matReturnBack.setNum(num);
				matReturnBack.setRemark(content);
				matReturnBack.setBarCode("");
				this.matReturnBackRepository.saveAndFlush(matReturnBack);
			}
			return tip;
		} catch (Exception e) {
			e.printStackTrace();
			// 状态设定
			tip = new OpTip(201, "提交失败");
			return tip;
		}
	}

	@Override
	@Transactional
	public OpTip recyclePass(String code, String userId) {
		OpTip tip = new OpTip(200, "保存成功");

		try {
			int returnBackNo = 0;
			if (code.length() == 8 && (code.toUpperCase().indexOf("M") == 0 || code.toUpperCase().indexOf("S") == 0)) {
				returnBackNo = Integer.parseInt(code.substring(1));
			}
			List<MatReturnBack> backList = matReturnBackRepository.findBybarCode(code, returnBackNo);
			if(CollectionUtils.isNotEmpty(backList)) {
				for (MatReturnBack matReturnBack : backList) {
					if (matReturnBack == null) {
						return new OpTip(201, "查无此信息");
					}
					matReturnBack.setConfirmUser(this.userRepository.findUserByAccountId(userId).getUserName());
					matReturnBack.setConfirmMode(VerifyMode.SCANCONF);
					matReturnBack.setConfirmDate(new Date());
					matReturnBack.setAuditStatus(AuditStatus.PASS);
					matReturnBack.setNum(matReturnBack.getMatUseBill().getMatUseRecord().getMatInfo().getNum());
					matReturnBack.setRemark("");
					matReturnBack.setBarCode("");
					this.matReturnBackRepository.saveAndFlush(matReturnBack);
				}
			} else {
				tip.setCode(201);
				tip.setMessage("未查到归还信息");
			}

			// 状态设定
			return tip;
		} catch (Exception e) {
			e.printStackTrace();
			// 状态设定
			tip = new OpTip(201, "保存失败");
			return tip;
		}
	}
}