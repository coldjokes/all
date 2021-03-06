package com.dosth.tool.common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dosth.common.constant.UsingStatus;
import com.dosth.common.db.Criteria;
import com.dosth.common.db.Restrictions;
import com.dosth.tool.common.config.ToolProperties;
import com.dosth.tool.common.state.NoticeType;
import com.dosth.tool.common.state.OnOrOff;
import com.dosth.tool.entity.EquDetailSta;
import com.dosth.tool.entity.EquSetting;
import com.dosth.tool.entity.Manufacturer;
import com.dosth.tool.entity.ManufacturerCustom;
import com.dosth.tool.entity.MatEquInfo;
import com.dosth.tool.entity.NoticeMgr;
import com.dosth.tool.repository.EquDetailStaRepository;
import com.dosth.tool.repository.ManufacturerCustomRepository;
import com.dosth.tool.repository.NoticeMgrRepository;
import com.dosth.tool.service.EquSettingService;
import com.dosth.tool.service.UserService;
import com.dosth.tool.vo.ViewUser;
import com.dosth.util.DateUtil;

@Component
public class StockEmailUtil {

	public static final Logger logger = LoggerFactory.getLogger(FeedingEmailUtil.class);

	@Autowired
	private ToolProperties toolProperties;
	@Autowired
	private EquSettingService equSettingService;
	@Autowired
	private EquDetailStaRepository equDetailStaRepository;
	@Autowired
	private ManufacturerCustomRepository manufacturerCustomRepository;
	@Autowired
	private EmailUtil emailUtil;
	@Autowired
	private NoticeMgrRepository noticeMgrRepository;
	@Autowired
	private UserService userService;

	public void stockEmail(String cabinetId) {
		List<NoticeMgr> mgrList = this.noticeMgrRepository.getNoticeMgrStatus(cabinetId, NoticeType.STOCK);
		NoticeMgr noticeMgr = null;
		if (mgrList != null && mgrList.size() > 0) {
			noticeMgr = mgrList.get(0);
		} else {
			EquSetting setting = this.equSettingService.get(cabinetId);
			mgrList = this.noticeMgrRepository.getNoticeMgrStatus(setting.getEquSettingParentId(), NoticeType.STOCK);
			if (mgrList != null && mgrList.size() > 0) {
				noticeMgr = mgrList.get(0);
			}
		}
		if (noticeMgr != null) {
			// ????????????????????????????????????????????????
			if (OnOrOff.ON.equals(noticeMgr.getStatus())) {
				OutputStream os = null;
				HSSFWorkbook workbook = null;
				HSSFSheet sheet = null;
				try {
					// ?????????????????????
					Criteria<Manufacturer> criteria = new Criteria<>();
					criteria.add(Restrictions.eq("status", UsingStatus.ENABLE, true));
					// ???????????????ID??????????????????
					List<EquDetailSta> equDetailStaList = this.equDetailStaRepository
							.getEquDetailStaListByCabinetId(cabinetId);
					// ????????????????????????????????????
					Map<String, List<EquDetailSta>> map = new HashMap<>();

					Map<MatEquInfo, List<EquDetailSta>> matGroup = equDetailStaList.stream()
							.filter(sta -> sta.getMatInfo() != null)
							.collect(Collectors.groupingBy(EquDetailSta::getMatInfo));
					// ??????????????????
					long curSum;
					List<EquDetailSta> staList;
					Boolean sendFlag = false;
					for (Entry<MatEquInfo, List<EquDetailSta>> mat : matGroup.entrySet()) {
						curSum = mat.getValue().stream().collect(Collectors.summarizingInt(EquDetailSta::getCurNum))
								.getSum();
						staList = map.get(mat.getKey().getManufacturerId());
						if (staList == null) {
							staList = new ArrayList<>();
						}
						for (EquDetailSta sta : mat.getValue()) {
							staList.add(sta);
						}
						map.put(mat.getKey().getManufacturerId(), staList);
						if (curSum <= mat.getKey().getLowerStockNum()) {
							sendFlag = true;
						}
					}
					if (sendFlag) {
						HSSFRow row;
						HSSFCellStyle cellStyle;
						HSSFFont redFont;

						int colNo;
						List<String> mailList;
						List<ManufacturerCustom> customList;
						int rowNo;

						for (Entry<String, List<EquDetailSta>> entry : map.entrySet()) {
							workbook = new HSSFWorkbook();
							sheet = workbook.createSheet("??????????????????");
							row = sheet.createRow(0);
							cellStyle = workbook.createCellStyle();
							redFont = workbook.createFont();

							redFont.setColor(HSSFColor.HSSFColorPredefined.RED.getIndex());
							cellStyle.setFont(redFont);

							colNo = 0;
							row.createCell(colNo).setCellValue("???????????????");
							row.createCell(++colNo).setCellValue("????????????");
							row.createCell(++colNo).setCellValue("????????????");
							row.createCell(++colNo).setCellValue("????????????");
							row.createCell(++colNo).setCellValue("????????????");
							row.createCell(++colNo).setCellValue("????????????");
							row.createCell(++colNo).setCellValue("????????????");
							row.createCell(++colNo).setCellValue("??????????????????");

							// ???????????????????????????
							mailList = new ArrayList<>();
							customList = this.manufacturerCustomRepository.getCustomListByManufacturerId(entry.getKey(),
									UsingStatus.ENABLE);
							for (ManufacturerCustom custom : customList) {
								if (custom.getMailAddress() == null || "".equals(custom.getMailAddress())) {
									continue;
								}
								mailList.add(custom.getMailAddress());
							}

							// ????????????????????????
							rowNo = 1;
							HSSFCell cell;
							List<EquDetailSta> list = entry.getValue().stream().sorted(new Comparator<EquDetailSta>() {
								@Override
								public int compare(EquDetailSta o1, EquDetailSta o2) {
									if (o1.getEquDetail().getRowNo() > o2.getEquDetail().getRowNo()) {
										return -1;
									} else if (o1.getEquDetail().getRowNo() < o2.getEquDetail().getRowNo()) {
										return 1;
									} else if (o1.getColNo() > o2.getColNo()) {
										return 1;
									} else {
										return -1;
									}
								}
							}).collect(Collectors.toList());

							for (EquDetailSta sta : list) {
								colNo = 0;
								row = sheet.createRow(rowNo);
								row.createCell(colNo)
										.setCellValue(sta.getEquDetail().getEquSetting().getEquSettingName() + "("
												+ sta.getEquDetail().getRowNo() + "--" + sta.getColNo() + ")");
								row.createCell(++colNo).setCellValue(sta.getMatInfo().getMatEquName());
								row.createCell(++colNo)
										.setCellValue(sta.getMatInfo() != null ? sta.getMatInfo().getBarCode() : "");
								row.createCell(++colNo)
										.setCellValue(sta.getMatInfo() != null ? sta.getMatInfo().getSpec() : "");
								row.createCell(++colNo)
										.setCellValue(sta.getMatInfo() != null ? sta.getMatInfo().getNum() : 0);
								row.createCell(++colNo).setCellValue(
										sta.getMatInfo() != null ? sta.getMatInfo().getBorrowType().getMessage() : "");
								cell = row.createCell(++colNo);
								cell.setCellValue(sta.getCurNum());
								row.createCell(++colNo).setCellValue(sta.getMaxReserve() - sta.getCurNum());

								if (sta.getCurNum() <= sta.getWarnVal()) {
									cell.setCellStyle(cellStyle);
								}

								rowNo++;
							}

							String filePath = this.toolProperties.getTmpUploadPath() + "??????????????????-" + DateUtil.getAllTime()
									+ ".xls";
							File file = new File(filePath);
							os = new FileOutputStream(file);
							workbook.write(os);
							if (!file.exists()) {
								logger.error("??????????????????????????????,??????????????????!");
							}

							List<NoticeMgr> noticeMgrList = this.noticeMgrRepository.findAll().stream()
									.filter(mgr -> NoticeType.STOCK.equals(mgr.getNoticeType()))
									.collect(Collectors.toList());

							if (CollectionUtils.isNotEmpty(noticeMgrList)) {
								ViewUser viewUser;
								NoticeMgr notice = noticeMgrList.get(0);
								if (notice != null && StringUtils.isNotBlank(notice.getAccountId())) {
									String accountIds[] = notice.getAccountId().split(",");
									for (int i = 0; i < accountIds.length; i++) {
										viewUser = this.userService.getViewUser(accountIds[i]);
										if (viewUser != null && StringUtils.isNotBlank(viewUser.getEmail())) {
											mailList.add(viewUser.getEmail());
										}
									}
								}
							}

							String mailContent = "<p>??????:<br/>&nbsp;&nbsp;&nbsp;&nbsp;??????????????????????????????,?????????????????????????????????(???????????????????????????),??????!</p>";
							this.emailUtil.sendEmail(mailContent, "??????????????????", filePath, "??????????????????", mailList);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (os != null) {
						try {
							os.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (workbook != null) {
						try {
							workbook.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
}