package com.dosth.tool.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dosth.common.constant.UsingStatus;
import com.dosth.common.db.Criteria;
import com.dosth.common.db.Restrictions;
import com.dosth.common.exception.DoSthException;
import com.dosth.common.exception.DoSthExceptionEnum;
import com.dosth.common.node.ZTreeNode;
import com.dosth.common.util.DateUtil;
import com.dosth.common.util.FileUtil;
import com.dosth.enums.CabinetType;
import com.dosth.tool.common.config.ToolProperties;
import com.dosth.tool.common.state.InventoryStatus;
import com.dosth.tool.common.util.EmailUtil;
import com.dosth.tool.common.util.ListUtil;
import com.dosth.tool.common.util.ViewUserUtil;
import com.dosth.tool.entity.EquDetailSta;
import com.dosth.tool.entity.EquSetting;
import com.dosth.tool.entity.Inventory;
import com.dosth.tool.entity.SubBox;
import com.dosth.tool.entity.SubBoxAccountRef;
import com.dosth.tool.entity.SubCabinetDetail;
import com.dosth.tool.entity.TimeTaskDetail;
import com.dosth.tool.repository.EquSettingRepository;
import com.dosth.tool.repository.InventoryRepository;
import com.dosth.tool.repository.TimeTaskDetailRepository;
import com.dosth.tool.service.EquDetailStaService;
import com.dosth.tool.service.InventoryService;
import com.dosth.tool.service.SubBoxAccountRefService;
import com.dosth.tool.service.SubBoxService;
import com.dosth.tool.service.SubCabinetDetailService;
import com.dosth.tool.service.UserService;
import com.dosth.tool.vo.InventoryInfo;
import com.dosth.tool.vo.MatInfo;
import com.dosth.tool.vo.ViewUser;
import com.google.common.collect.Lists;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @description 盘点Service实现
 * @author guozhidong
 *
 */
@Service
@Transactional
public class InventoryServiceImpl implements InventoryService {

	public static final Logger logger = LoggerFactory.getLogger(InventoryServiceImpl.class);

	@Autowired
	private InventoryRepository inventoryRepository;
	@Autowired
	private EquSettingRepository equSettingRepository;
	@Autowired
	private EquDetailStaService equDetailStaService;
	@Autowired
	private SubCabinetDetailService subCabinetDetailService;
	@Autowired
	private SubBoxService subBoxService;
	@Autowired
	private SubBoxAccountRefService subBoxAccountRefService;
	@Autowired
	private ToolProperties toolProperties;
	@Autowired
	private TimeTaskDetailRepository timeTaskDetailRepository;
	@Autowired
	private UserService userService;
	@Autowired
	private EmailUtil emailUtil;

	@Override
	public void save(Inventory inventory) throws DoSthException {
		this.inventoryRepository.save(inventory);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public Inventory get(Serializable id) throws DoSthException {
		Inventory inventory = this.inventoryRepository.getOne(id);
		return inventory;
	}

	@Override
	public Inventory update(Inventory inventory) throws DoSthException {
		return this.inventoryRepository.saveAndFlush(inventory);
	}

	@Override
	public void delete(Inventory inventory) throws DoSthException {
		this.inventoryRepository.delete(inventory);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public Page<Inventory> getPage(int pageNo, int pageSize, String beginTime, String endTime, String matInfo,
			String equName) {
		List<Inventory> inventoryList = this.inventoryRepository.findAll();
		if (beginTime != null && !"".equals(beginTime)) {
			inventoryList = inventoryList.stream().filter(inventory -> inventory.getOpDate()
					.after(DateUtil.parseTime(new StringBuilder(beginTime).append(" 00:00:00").toString()))).collect(Collectors.toList());
		}
		if (endTime != null && !"".equals(endTime)) {
			inventoryList = inventoryList.stream().filter(inventory -> inventory.getOpDate()
					.before(DateUtil.parseTime(new StringBuilder(endTime).append(" 23:59:59").toString()))).collect(Collectors.toList());
		}
		if (equName != null && !"".equals(equName) && !equName.equals("-1")) {
			if(equName.indexOf("TEM_CABINET") != -1) {
				inventoryList = inventoryList.stream().filter(inventory -> inventory.getSubBox() != null && inventory.getSubBox()
						.getEquSettingId().equals(equName.substring(0, equName.indexOf(":")))).collect(Collectors.toList());
			} else {
				inventoryList = inventoryList.stream().filter(inventory -> inventory.getEquDetailSta() != null && inventory.getEquDetailSta()
						.getEquDetail().getEquSettingId().equals(equName.substring(0, equName.indexOf(":")))).collect(Collectors.toList());
			}
		}
		if (matInfo != null && !"".equals(matInfo)) {
			inventoryList = inventoryList.stream().filter(inventory -> 
				inventory.getMatInfo().getBarCode().contains(matInfo.trim().toLowerCase())
				|| inventory.getMatInfo().getBarCode().contains(matInfo.trim().toUpperCase())
				|| inventory.getMatInfo().getMatEquName().contains(matInfo.trim().toLowerCase())
				|| inventory.getMatInfo().getMatEquName().contains(matInfo.trim().toUpperCase())
				|| inventory.getMatInfo().getSpec().contains(matInfo.trim().toLowerCase())
				|| inventory.getMatInfo().getSpec().contains(matInfo.trim().toUpperCase())
					).collect(Collectors.toList());
		}
		Pageable pageable = new PageRequest(pageNo, pageSize);
		Page<Inventory> page = ListUtil.listConvertToPage(inventoryList, pageable);

		List<Inventory> inventories = page.getContent();
		for (Inventory inventory : inventories) {
			inventory.setOwnerName(inventory.getOwner() != null ? inventory.getOwner().getUserName() : "");
			inventory.setUserName(inventory.getUser() != null ? inventory.getUser().getUserName() : "");
			inventory.setEquName(inventory.getEquDetailSta() != null ? inventory.getEquDetailSta().getEquDetail().getEquSetting().getEquSettingName() : "");
			inventory.setMatEquName(inventory.getMatInfo() != null ? inventory.getMatInfo().getMatEquName():"");
			inventory.setEquSettingName(inventory.getSubBox() != null ? inventory.getSubBox().getEquSetting().getEquSettingName() : "" );
			
			if (inventory.getInventoryNum() > inventory.getStorageNum()) {
	    		  inventory.setInventoryStatus(InventoryStatus.SURPLUS);
	    	  } else if (inventory.getStorageNum() > inventory.getInventoryNum()) {
	    		  inventory.setInventoryStatus(InventoryStatus.LOSS);
	    	  } else {
	    		  inventory.setInventoryStatus(InventoryStatus.PING);
	    	  }
		}

		return page;
	}

	@Override
	public List<ZTreeNode> treeEqu() {
		List<ZTreeNode> tree = new ArrayList<>();
		Criteria<EquSetting> criteria = new Criteria<>();
		criteria.add(Restrictions.eq("status", UsingStatus.ENABLE, true));
		criteria.add(Restrictions.ne("cabinetType", CabinetType.RECOVERY_CABINET, true));
		List<EquSetting> settingList = this.equSettingRepository.findAll(criteria);
		ZTreeNode node;
		for (EquSetting setting : settingList) {
			if (CabinetType.RECOVERY_CABINET.equals(setting.getCabinetType())) {
				continue;
			}
			node = new ZTreeNode();
			node.setName(setting.getEquSettingName());
			switch (setting.getCabinetType()) {
			case KNIFE_CABINET_C:
				node.setId("CM_" + setting.getId());
				node.setIcon("/static/img/one.png");
				break;
			case KNIFE_CABINET_C_A:
				node.setId("CA_" + setting.getId());
				node.setIcon("/static/img/one.png");
				break;
			case KNIFE_CABINET_C_B:
				node.setId("CB_" + setting.getId());
				node.setIcon("/static/img/one.png");
				break;
			case STORE_CABINET:
				node.setId("CW_" + setting.getId());
				node.setIcon("/static/img/one.png");
				break;
			case SUB_CABINET:
				node.setId("FS_" + setting.getId());
				node.setIcon("/static/img/one.png");
				break;
			case TEM_CABINET:
				node.setId("ZC_" + setting.getId());
				node.setIcon("/static/img/one.png");
				break;
			case VIRTUAL_WAREHOUSE:
				node.setId("XN_" + setting.getId());
				node.setIcon("/static/img/one.png");
				break;
			default:
				node.setId("QT_" + setting.getId());
				node.setIcon("/static/img/one.png");
				break;
			}
			if (setting.getEquSettingParentId() != null && !"".equals(setting.getEquSettingParentId())) {
				if (CabinetType.KNIFE_CABINET_C.equals(setting.getEquSettingParent().getCabinetType())) {
					node.setpId("CM_" + setting.getEquSettingParentId());
					node.setIcon("/static/img/two.png");
				} else {
					node.setpId("QT_" + setting.getEquSettingParentId());
					node.setIcon("/static/img/two.png");
				}
			}
			tree.add(node);
		}
		return tree;
	}

	@Override
	public List<InventoryInfo> getEquStockView(String equInfoId) {
		List<InventoryInfo> list = new ArrayList<>();
		InventoryInfo info;
		MatInfo mat;
		Integer num;
		if (equInfoId.startsWith("ZC_")) {
			List<SubBox> boxList = this.subBoxService.getSubBoxList(equInfoId.substring(3));
			boxList.sort((b1, b2) -> b1.getBoxIndex() - b2.getBoxIndex());
			List<SubCabinetDetail> detailList;
			SubBoxAccountRef ref;
			for (SubBox box : boxList) {
				mat = null;
				num = null;
				ref = this.subBoxAccountRefService.getAccountBySubBoxId(box.getId());
				info = new InventoryInfo(box.getId(),
						box.getEquSetting().getEquSettingName() + "[" + box.getBoxIndex() + "]", mat);
				if (ref != null) {
					info.setAccountId(ref.getAccountId());
					info.setUserName(ref.getUser().getLoginName());
					detailList = subCabinetDetailService.getSubDetailListBySubBoxId(box.getId());
					for (SubCabinetDetail detail : detailList) {
						mat = new MatInfo(detail.getMatInfo().getId(), detail.getMatInfo().getBarCode(),
								detail.getMatInfo().getMatEquName(), detail.getMatInfo().getSpec());
						info.setMatInfo(mat);
						num = detail.getNum();
						info.setStorageNum(num);
						list.add(info);
					}
				} else {
					list.add(info);
				}
			}
		} else {
			List<EquDetailSta> staList = this.equDetailStaService
					.getEquDetailStaListByCabinetId(equInfoId.substring(3));
			if (equInfoId.startsWith("CA_") || equInfoId.startsWith("CB_") || equInfoId.startsWith("QT_")) {
				staList.sort((s1, s2) -> s2.getEquDetail().getRowNo() - s1.getEquDetail().getRowNo());
			}
			for (EquDetailSta sta : staList) {
				if (sta.getMatInfo() != null) {
					mat = new MatInfo(sta.getMatInfo().getId(), sta.getMatInfo().getBarCode(),
							sta.getMatInfo().getMatEquName(), sta.getMatInfo().getSpec());
					num = sta.getCurNum();
				} else {
					mat = null;
					num = null;
				}
				info = new InventoryInfo(sta.getId(), sta.getEquDetail().getEquSetting().getEquSettingName() + "" + "["
						+ sta.getEquDetail().getRowNo() + "-" + sta.getColNo() + "]", mat);
				info.setStorageNum(num);
				list.add(info);
			}
		}
		return list;
	}

	@Override
	public void inventory(String equInfoId, String inventoryVals, String accountId) {
		JSONArray array = JSONArray.fromObject(inventoryVals);
		JSONObject obj;
		Integer inventoryNum;
		if (equInfoId.startsWith("ZC_")) { // 暂存柜
			for (int i = 0; i < array.size(); i++) {
				obj = array.getJSONObject(i);
				inventoryNum = obj.getInt("inventoryNum");
				this.inventoryRepository.save(new Inventory(null, obj.getString("detailBoxId"), obj.getString("matId"),
						obj.getInt("storageNum"), inventoryNum, obj.getString("accountId"), accountId));
			}
		} else { // 其他柜体类型
			for (int i = 0; i < array.size(); i++) {
				obj = array.getJSONObject(i);
				inventoryNum = obj.getInt("inventoryNum");
				this.inventoryRepository.save(new Inventory(obj.getString("detailBoxId"), null, obj.getString("matId"),
						obj.getInt("storageNum"), inventoryNum, null, accountId));
			}
		}
	}

	@Override
	public void exportInventory(HttpServletRequest request, HttpServletResponse response, String beginTime,
			String endTime) {
		Criteria<Inventory> criteria = new Criteria<>();
		if (beginTime != null && !"".equals(beginTime) && !"1900-01-01".equals(beginTime)) {
			criteria.add(Restrictions.gte("opDate",
					DateUtil.parseTime(new StringBuilder(beginTime).append(" 00:00:00").toString()), true));
		}
		if (endTime != null && !"".equals(endTime) && !"9999-12-31".equals(endTime)) {
			criteria.add(Restrictions.lte("opDate",
					DateUtil.parseTime(new StringBuilder(endTime).append(" 23:59:59").toString()), true));
		}

		List<Inventory> inventoryList = this.inventoryRepository.findAll(criteria);

		OutputStream output = null;
		HSSFWorkbook workbook = new HSSFWorkbook();
		try {
			HSSFSheet sheet = workbook.createSheet("补料记录");
			HSSFRow row = sheet.createRow(0);
			HSSFCell cell;

			sheet.setDefaultRowHeightInPoints(20);
			sheet.setDefaultColumnWidth(20);

			HSSFCellStyle surplusStyle = workbook.createCellStyle();
			HSSFFont surplus = workbook.createFont();
			surplus.setColor(HSSFColor.HSSFColorPredefined.GREEN.getIndex());
			surplusStyle.setFont(surplus);

			HSSFCellStyle lossStyle = workbook.createCellStyle();
			HSSFFont loss = workbook.createFont();
			loss.setColor(HSSFColor.HSSFColorPredefined.RED.getIndex());
			lossStyle.setFont(loss);

			HSSFCellStyle pingStyle = workbook.createCellStyle();
			HSSFFont ping = workbook.createFont();
//			ping.setColor(HSSFColor.HSSFColorPredefined.GREEN.getIndex());
			pingStyle.setFont(ping);

			int cellIndex = 0;
			row.createCell(cellIndex).setCellValue("主柜信息");
			row.createCell(++cellIndex).setCellValue("暂存柜信息");
			row.createCell(++cellIndex).setCellValue("物料名称");
			row.createCell(++cellIndex).setCellValue("库存数量");
			row.createCell(++cellIndex).setCellValue("盘点数量");
			row.createCell(++cellIndex).setCellValue("盘点状态");
			row.createCell(++cellIndex).setCellValue("使用人员");
			row.createCell(++cellIndex).setCellValue("操作人员");
			row.createCell(++cellIndex).setCellValue("操作时间");

			int rowNo = 1;
			HSSFRow row2;
			EquDetailSta sta;
			SubBox box;
			for (Inventory inventory : inventoryList) {
				row2 = sheet.createRow(rowNo++);
				cellIndex = 0;
				sta = inventory.getEquDetailSta();
				if (sta != null) {
					row2.createCell(cellIndex).setCellValue(sta.getEquDetail().getEquSetting().getEquSettingName() + ""
							+ "[" + sta.getEquDetail().getRowNo() + "-" + sta.getColNo() + "]");
				} else {
					cellIndex++;
				}
				box = inventory.getSubBox();
				if (box != null) {
					row2.createCell(cellIndex).setCellValue(box.getEquSetting().getEquSettingName() + "["
							+ box.getRowNo() + "-" + box.getColNo() + "]");
				} else {
					cellIndex++;
				}
				row2.createCell(++cellIndex).setCellValue(inventory.getMatInfo().getMatEquName());
				row2.createCell(++cellIndex).setCellValue(inventory.getStorageNum());
				row2.createCell(++cellIndex).setCellValue(inventory.getInventoryNum());
				cell = row2.createCell(++cellIndex);
				if (inventory.getStorageNum() > inventory.getInventoryNum()) {
					cell.setCellValue(InventoryStatus.LOSS.getMessage());
					cell.setCellStyle(lossStyle);
				} else if (inventory.getInventoryNum() > inventory.getStorageNum()) {
					cell.setCellValue(InventoryStatus.SURPLUS.getMessage());
					cell.setCellStyle(surplusStyle);
				} else {
					cell.setCellValue(InventoryStatus.PING.getMessage());
					cell.setCellStyle(pingStyle);
				}
				row2.createCell(++cellIndex).setCellValue(ViewUserUtil.createViewUserName(inventory.getOwner()));
				row2.createCell(++cellIndex).setCellValue(ViewUserUtil.createViewUserName(inventory.getUser()));
				row2.createCell(++cellIndex).setCellValue(DateUtil.getTime(inventory.getOpDate()));
			}

			String fileName = FileUtil.processFileName(request, "盘点记录" + DateUtil.getDays());
			output = response.getOutputStream();
			response.reset();
			response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xls");
			response.setContentType("application/msexcel");
			response.setCharacterEncoding("utf-8");
			workbook.write(output);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					output.close();
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

	@Override
	public void sendInventoryRecord(String beginTime, String endTime) {

		Criteria<Inventory> criteria = new Criteria<>();
		if (beginTime != null && !"".equals(beginTime) && !"1900-01-01".equals(beginTime)) {
			criteria.add(Restrictions.gte("opDate",
					DateUtil.parseTime(new StringBuilder(beginTime).append(" 00:00:00").toString()), true));
		}
		if (endTime != null && !"".equals(endTime) && !"9999-12-31".equals(endTime)) {
			criteria.add(Restrictions.lte("opDate",
					DateUtil.parseTime(new StringBuilder(endTime).append(" 23:59:59").toString()), true));
		}

		List<Inventory> inventoryList = this.inventoryRepository.findAll(criteria);

		OutputStream output = null;
		HSSFWorkbook workbook = new HSSFWorkbook();
		try {
			HSSFSheet sheet = workbook.createSheet("补料记录");
			HSSFRow row = sheet.createRow(0);
			HSSFCell cell;

			sheet.setDefaultRowHeightInPoints(20);
			sheet.setDefaultColumnWidth(20);

			HSSFCellStyle surplusStyle = workbook.createCellStyle();
			HSSFFont surplus = workbook.createFont();
			surplus.setColor(HSSFColor.HSSFColorPredefined.GREEN.getIndex());
			surplusStyle.setFont(surplus);

			HSSFCellStyle lossStyle = workbook.createCellStyle();
			HSSFFont loss = workbook.createFont();
			loss.setColor(HSSFColor.HSSFColorPredefined.RED.getIndex());
			lossStyle.setFont(loss);

			HSSFCellStyle pingStyle = workbook.createCellStyle();
			HSSFFont ping = workbook.createFont();
//			ping.setColor(HSSFColor.HSSFColorPredefined.GREEN.getIndex());
			pingStyle.setFont(ping);

			int cellIndex = 0;
			row.createCell(cellIndex).setCellValue("主柜信息");
			row.createCell(++cellIndex).setCellValue("暂存柜信息");
			row.createCell(++cellIndex).setCellValue("物料名称");
			row.createCell(++cellIndex).setCellValue("库存数量");
			row.createCell(++cellIndex).setCellValue("盘点数量");
			row.createCell(++cellIndex).setCellValue("盘点状态");
			row.createCell(++cellIndex).setCellValue("使用人员");
			row.createCell(++cellIndex).setCellValue("操作人员");
			row.createCell(++cellIndex).setCellValue("操作时间");

			int rowNo = 1;
			HSSFRow row2;
			EquDetailSta sta;
			SubBox box;
			for (Inventory inventory : inventoryList) {
				row2 = sheet.createRow(rowNo++);
				cellIndex = 0;
				sta = inventory.getEquDetailSta();
				if (sta != null) {
					row2.createCell(cellIndex).setCellValue(sta.getEquDetail().getEquSetting().getEquSettingName() + ""
							+ "[" + sta.getEquDetail().getRowNo() + "-" + sta.getColNo() + "]");
				} else {
					cellIndex++;
				}
				box = inventory.getSubBox();
				if (box != null) {
					row2.createCell(++cellIndex).setCellValue(box.getEquSetting().getEquSettingName() + "["
							+ box.getRowNo() + "-" + box.getColNo() + "]");
				} else {
					cellIndex++;
				}
				row2.createCell(++cellIndex).setCellValue(inventory.getMatInfo().getMatEquName());
				row2.createCell(++cellIndex).setCellValue(inventory.getStorageNum());
				row2.createCell(++cellIndex).setCellValue(inventory.getInventoryNum());
				cell = row2.createCell(++cellIndex);
				if (inventory.getStorageNum() > inventory.getInventoryNum()) {
					cell.setCellValue(InventoryStatus.LOSS.getMessage());
					cell.setCellStyle(lossStyle);
				} else if (inventory.getInventoryNum() > inventory.getStorageNum()) {
					cell.setCellValue(InventoryStatus.SURPLUS.getMessage());
					cell.setCellStyle(surplusStyle);
				} else {
					cell.setCellValue(InventoryStatus.PING.getMessage());
					cell.setCellStyle(pingStyle);
				}
				row2.createCell(++cellIndex).setCellValue(ViewUserUtil.createViewUserName(inventory.getOwner()));
				row2.createCell(++cellIndex).setCellValue(ViewUserUtil.createViewUserName(inventory.getUser()));
				row2.createCell(++cellIndex).setCellValue(DateUtil.getTime(inventory.getOpDate()));
			}

			String filePath = this.toolProperties.getTmpUploadPath() + "盘点记录-" + DateUtil.getAllTime() + ".xls";
			File file = new File(filePath);
			output = new FileOutputStream(file);
			workbook.write(output);
			if (!file.exists()) {
				logger.error("生成盘点记录异常,请刷新后再试!");
			}

			List<String> mailList = Lists.newArrayList();
			List<TimeTaskDetail> timeTaskDetailList = this.timeTaskDetailRepository.findAll();
			if (CollectionUtils.isNotEmpty(timeTaskDetailList)) {
				ViewUser viewUser = null;
				TimeTaskDetail timeTaskDetail = timeTaskDetailList.get(0);
				if (timeTaskDetail != null && timeTaskDetail.getAccountId() != null) {
					String accountIds[] = timeTaskDetail.getAccountId().split(",");
					for (int i = 0; i < accountIds.length; i++) {
						viewUser = this.userService.getViewUser(accountIds[i]);
						if (viewUser != null && viewUser.getEmail() != null) {
							if (viewUser.getEmail() == null || viewUser.getEmail().equals("")) {
								continue;
							}
							mailList.add(viewUser.getEmail());
						}
					}
				}
			}

			String mailContent = "<p>您好:<br/>&nbsp;&nbsp;&nbsp;&nbsp;附件为盘点记录,请查收!</p>";
			this.emailUtil.sendEmail(mailContent, "盘点记录", filePath, "盘点记录", mailList);
		} catch (Exception e) {
			throw new DoSthException(DoSthExceptionEnum.SERVER_ERROR);
		} finally {
			if (output != null) {
				try {
					output.close();
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