package com.dosth.tool.service.impl;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dosth.common.db.Criteria;
import com.dosth.common.db.Restrictions;
import com.dosth.common.exception.DoSthException;
import com.dosth.tool.common.state.NoticeType;
import com.dosth.tool.common.state.OnOrOff;
import com.dosth.tool.common.util.EmailUtil;
import com.dosth.tool.common.util.ListUtil;
import com.dosth.tool.entity.NoticeMgr;
import com.dosth.tool.repository.NoticeMgrRepository;
import com.dosth.tool.service.NoticeMgrService;
import com.dosth.tool.service.UserService;
import com.dosth.tool.vo.ViewUser;
import com.dosth.toolcabinet.dto.NoticeMgrInfo;

/**
 * @description 通知管理Service实现
 * @author Weifeng Li
 *
 */
@Service
@Transactional
public class NoticeMgrServiceImpl implements NoticeMgrService {

	@Autowired
	private EmailUtil emailUtil;
	@Autowired
	private NoticeMgrRepository noticeMgrRepository;
	@Autowired
	private UserService userService;

	@Override
	public void save(NoticeMgr entity) throws DoSthException {
		this.noticeMgrRepository.save(entity);
	}

	@Override
	public NoticeMgr get(Serializable id) throws DoSthException {
		return this.noticeMgrRepository.findOne(id);
	}

	@Override
	public NoticeMgr update(NoticeMgr entity) throws DoSthException {
		return this.noticeMgrRepository.saveAndFlush(entity);
	}

	@Override
	public void delete(NoticeMgr entity) throws DoSthException {
		this.noticeMgrRepository.delete(entity);
	}

	@Override
	public Page<NoticeMgr> getPage(int pageNo, int pageSize, String name, String noticeType) {
		Criteria<NoticeMgr> criteria = new Criteria<>();
		if (name != null && !"".equals(name) && !name.equals("-1")) {
			criteria.add(Restrictions.eq("equSettingName", name, true));
		}
		if (noticeType != null && !"".equals(noticeType) && !noticeType.equals("-1")) {
			criteria.add(Restrictions.eq("noticeType", NoticeType.valueOf(noticeType), true));
		}
		List<NoticeMgr> list = this.noticeMgrRepository.findAll(criteria);
		Pageable pageable = new PageRequest(pageNo, pageSize);
		return ListUtil.listConvertToPage(list, pageable);
	}

	@Override
	public List<NoticeMgrInfo> editCount(String cabinetId) {
		String array[];
		String equSettingName;
		ViewUser viewUser;
		List<String> list = new ArrayList<>();
		List<NoticeMgrInfo> noticeInfoList = new ArrayList<>();
		List<NoticeMgr> noticeMgrList = this.noticeMgrRepository.getNoticeMgrByCabinetId(cabinetId);
		if (noticeMgrList != null && noticeMgrList.size() > 0) {
			for (NoticeMgr noticeMgr : noticeMgrList) {
				if (noticeMgr.getStatus() == null || OnOrOff.OFF.equals(noticeMgr.getStatus())) {
					continue;
				}
				equSettingName = noticeMgr.getEquSetting().getEquSettingName();
				array = noticeMgr.getAccountId().split(",");

				if (equSettingName != null && array != null) {
					for (int i = 0; i < array.length; i++) {
						viewUser = this.userService.getViewUser(array[i]);
						if (viewUser != null && viewUser.getEmail() != null) {
							list.add(viewUser.getEmail());
						}
					}
				}

				if (NoticeType.PRINT.equals(noticeMgr.getNoticeType())) {
					if (noticeMgr.getCount() != null && noticeMgr.getCount() >= 1) {
						noticeMgr.setCount(noticeMgr.getCount() - 1);
					}
					noticeMgr = this.noticeMgrRepository.saveAndFlush(noticeMgr);
					noticeInfoList.add(
							new NoticeMgrInfo(noticeMgr.getEquSettingId() != null ? noticeMgr.getEquSettingId() : "",
									noticeMgr.getNum() != null ? noticeMgr.getNum() : 0,
									noticeMgr.getCount() != null ? noticeMgr.getCount() : 0,
									noticeMgr.getWarnValue() != null ? noticeMgr.getWarnValue() : 0,
									noticeMgr.getNoticeType() != null ? noticeMgr.getNoticeType().toString() : ""));

					if (noticeMgr.getCount() != null && noticeMgr.getWarnValue() != null
							&& noticeMgr.getCount() <= noticeMgr.getWarnValue()) {
						String mailTheme = "打印机缺纸提醒";
						String mailContent = "<p>你好：<br/>" + "&nbsp;&nbsp;&nbsp;&nbsp;刀具柜：" + equSettingName + "<br/>"
								+ "&nbsp;&nbsp;&nbsp;&nbsp;打印机纸剩余：" + noticeMgr.getCount() + "<br/>"
								+ "&nbsp;&nbsp;&nbsp;&nbsp;已到达预警值,请尽快更换新的打印纸</p>";

						new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									emailUtil.sendEmail(mailContent, mailTheme, null, null, list);
								} catch (UnsupportedEncodingException e) {
									e.printStackTrace();
								} catch (MessagingException e) {
									e.printStackTrace();
								}
							}
						}).start();
					}
				} else if (NoticeType.RECOVERY.equals(noticeMgr.getNoticeType())) {
					if (noticeMgr.getCount() != null) {
						noticeMgr.setCount(noticeMgr.getCount() + 1);
					}
					noticeMgr = this.noticeMgrRepository.saveAndFlush(noticeMgr);
					noticeInfoList.add(
							new NoticeMgrInfo(noticeMgr.getEquSettingId() != null ? noticeMgr.getEquSettingId() : "",
									noticeMgr.getNum() != null ? noticeMgr.getNum() : 0,
									noticeMgr.getCount() != null ? noticeMgr.getCount() : 0,
									noticeMgr.getWarnValue() != null ? noticeMgr.getWarnValue() : 0,
									noticeMgr.getNoticeType() != null ? noticeMgr.getNoticeType().toString() : ""));

					if (noticeMgr.getCount() != null && noticeMgr.getWarnValue() != null
							&& noticeMgr.getCount() >= noticeMgr.getWarnValue()) {
						String mailTheme = "回收仓满仓提醒";
						String mailContent = "<p>你好：<br/>" + "&nbsp;&nbsp;&nbsp;&nbsp;刀具柜：" + equSettingName + "<br/>"
								+ "&nbsp;&nbsp;&nbsp;&nbsp;回收仓数量：" + noticeMgr.getCount() + "<br/>"
								+ "&nbsp;&nbsp;&nbsp;&nbsp;已到达预警值,请尽快回收刀具</p>";

						new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									emailUtil.sendEmail(mailContent, mailTheme, null, null, list);
								} catch (UnsupportedEncodingException e) {
									e.printStackTrace();
								} catch (MessagingException e) {
									e.printStackTrace();
								}
							}
						}).start();
					}
				}
			}
		}
		return noticeInfoList;
	}

	@Override
	public List<NoticeMgrInfo> getNoticeMgr(String cabinetId) {
		List<NoticeMgrInfo> noticeInfoList = new ArrayList<>();
		List<NoticeMgr> noticeMgrList = this.noticeMgrRepository.getNoticeMgrByCabinetId(cabinetId);
		if (noticeMgrList != null && noticeMgrList.size() > 0) {
			for (NoticeMgr notice : noticeMgrList) {
				noticeInfoList.add(new NoticeMgrInfo(notice.getEquSettingId() != null ? notice.getEquSettingId() : "",
						notice.getNum() != null ? notice.getNum() : 0,
						notice.getCount() != null ? notice.getCount() : 0,
						notice.getWarnValue() != null ? notice.getWarnValue() : 0,
						notice.getNoticeType() != null ? notice.getNoticeType().toString() : ""));

			}
		}
		return noticeInfoList;
	}

	@Override
	public NoticeMgrInfo resetNoticeMgr(String cabinetId, int num, String noticeType) {
		NoticeMgr noticeMgr = null;
		NoticeMgrInfo noticeMgrInfo = null;
		List<NoticeMgr> noticeMgrList = this.noticeMgrRepository.getNoticeMgrByCabinetId(cabinetId);
		if (noticeMgrList != null && noticeMgrList.size() > 0) {
			for (NoticeMgr notice : noticeMgrList) {
				if (notice.getNoticeType().toString().equals(noticeType)) {
					if (noticeType.equals(NoticeType.PRINT.toString())) {
						notice.setNum(num);
						notice.setCount(num);
					} else if (noticeType.equals(NoticeType.RECOVERY.toString())) {
						notice.setNum(num);
						notice.setCount(0);
					}
					noticeMgr = this.noticeMgrRepository.saveAndFlush(notice);
				}
			}
		}

		noticeMgrInfo = new NoticeMgrInfo(noticeMgr.getEquSettingId() != null ? noticeMgr.getEquSettingId() : "",
				noticeMgr.getNum() != null ? noticeMgr.getNum() : 0,
				noticeMgr.getCount() != null ? noticeMgr.getCount() : 0,
				noticeMgr.getWarnValue() != null ? noticeMgr.getWarnValue() : 0,
				noticeMgr.getNoticeType() != null ? noticeMgr.getNoticeType().toString() : "");
		return noticeMgrInfo;
	}

	@Override
	public List<NoticeMgr> getNoticeMgrByCabinetId(String cabinetId) {
		return this.noticeMgrRepository.getNoticeMgrByCabinetId(cabinetId);
	}

	@Override
	public void delNoticeMgr(String accountId) {
		String newAccountId = "";
		String newUserName = "";
		List<NoticeMgr> noticeMgrList = this.noticeMgrRepository.findAll();
		ViewUser user = this.userService.getViewUser(accountId);
		if (noticeMgrList != null && noticeMgrList.size() > 0) {
			for (NoticeMgr noticeMgr : noticeMgrList) {
				newAccountId = noticeMgr.getAccountId().replaceAll(accountId + ",", "");
				newUserName = noticeMgr.getUserName().replaceAll(user.getUserName() + ",", "");
				noticeMgr.setAccountId(newAccountId);
				noticeMgr.setUserName(newUserName);
				this.noticeMgrRepository.saveAndFlush(noticeMgr);
			}
		}
	}

}
