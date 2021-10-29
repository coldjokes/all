package com.dosth.tool.service.impl;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dosth.common.constant.UsingStatus;
import com.dosth.common.constant.YesOrNo;
import com.dosth.common.db.Criteria;
import com.dosth.common.db.Restrictions;
import com.dosth.common.util.ExcelUtil;
import com.dosth.enums.CabinetType;
import com.dosth.tool.common.config.ToolProperties;
import com.dosth.tool.common.state.BorrowType;
import com.dosth.tool.entity.EquSetting;
import com.dosth.tool.entity.Manufacturer;
import com.dosth.tool.entity.MatEquInfo;
import com.dosth.tool.repository.EquSettingRepository;
import com.dosth.tool.repository.ManufacturerRepository;
import com.dosth.tool.repository.MatEquInfoRepository;
import com.dosth.tool.service.ImportService;
import com.dosth.util.OpTip;

import net.coobird.thumbnailator.Thumbnails;

@Service
@Transactional
public class ImportServiceImpl implements ImportService {

	@Autowired
	private ToolProperties toolProperties;
	@Autowired
	private EquSettingRepository equSettingRepository;
	@Autowired
	private MatEquInfoRepository matEquInfoRepository;
	@Autowired
	private ManufacturerRepository manufacturerRepository;

	@Override
	public OpTip getListByExcel(InputStream in, String fileName) throws Exception {
		OpTip tip = new OpTip(200, "上传成功！");
		HSSFWorkbook workbook = null;
		HSSFSheet sheet = null;
		HSSFRow row = null;
		MatEquInfo matEquInfo = null;

		boolean temCabinetFlag = false; // 是否有暂存柜
		String barCode = null; // 编码
		String matEquName = null; // 名称
		String temPackNum = null; // 临时包装数量
		Integer packNum = null; // 包装数量
		String matPackUnit = null; // 包装单位
		String manufacturerTxt = null; // 供应商文本
		Manufacturer manufacturer = null; // 供应商
		String brand = null; // 品牌
		String spec = null; // 规格型号
		String temStorePrice = null; // 临时库存成本
		Float storePrice = null; // 库存成本
		String lowerStockNum = null; // 最低库存
		String borrowType = null; // 借出类型
		String remark = null; // 备注
		String pic = null; // 图片
		String finalIconName = null;
		try {
			List<MatEquInfo> infoList = new ArrayList<>();
			// 创建Excel工作薄
			workbook = ExcelUtil.getWorkbook(in);
			sheet = ExcelUtil.getSheet(workbook, 0);
			if (sheet == null) {
				throw new RuntimeException("无法查找到工作区");
			}

			// 查询是否有暂存柜
			Criteria<EquSetting> criteria = new Criteria<>();
			criteria.add(Restrictions.eq("cabinetType", CabinetType.TEM_CABINET, true));
			List<EquSetting> equSettingList = this.equSettingRepository.findAll(criteria);
			if (CollectionUtils.isNotEmpty(equSettingList)) {
				temCabinetFlag = true;
			}

			int col;
			
			for (int j = 2; j <= sheet.getLastRowNum(); j++) {
				row = ExcelUtil.getRow(sheet, j);
				if (row == null || row.getFirstCellNum() == j || row.getRowNum() <= 1) {
					continue;
				}

				matEquInfo = new MatEquInfo();
				col = 0;
				barCode = ExcelUtil.getCellValue(ExcelUtil.getCell(row, col++)); // 编号
				matEquName = ExcelUtil.getCellValue(ExcelUtil.getCell(row, col++)); // 物料名称
				temPackNum = ExcelUtil.getCellValue(ExcelUtil.getCell(row, col++)); // 包装数量
				matPackUnit = ExcelUtil.getCellValue(ExcelUtil.getCell(row, col++)); // 包装单位
				manufacturerTxt = ExcelUtil.getCellValue(ExcelUtil.getCell(row, col++)); // 供应商文本
				brand = ExcelUtil.getCellValue(ExcelUtil.getCell(row, col++)); // 品牌
				spec = ExcelUtil.getCellValue(ExcelUtil.getCell(row, col++)); // 规格型号
				temStorePrice = ExcelUtil.getCellValue(ExcelUtil.getCell(row, col++)); // 库存成本
				lowerStockNum = ExcelUtil.getCellValue(ExcelUtil.getCell(row, col++)); // 最低库存
				borrowType = ExcelUtil.getCellValue(ExcelUtil.getCell(row, col++)); // 借出类型
				remark = ExcelUtil.getCellValue(ExcelUtil.getCell(row, col++)); // 备注
				pic = ExcelUtil.getCellValue(ExcelUtil.getCell(row, col++)); // 图片
				
				if ((barCode == null || "".equals(barCode))
						&& (matEquName == null || "".equals(matEquName))
						&& (temPackNum == null || "".equals(temPackNum))
						&& (matPackUnit == null || "".equals(matPackUnit))
						&& (manufacturerTxt == null || "".equals(manufacturerTxt))
						&& (brand == null || "".equals(brand))
						&& (spec == null || "".equals(spec))
						&& (temStorePrice == null || "".equals(temStorePrice))
						&& (lowerStockNum == null || "".equals(lowerStockNum))
						&& (borrowType == null || "".equals(borrowType))
						&& (remark == null || "".equals(remark))
						&& (pic == null || "".equals(pic))) {
					break;
				}				

				if (matPackUnit != null) {
					matEquInfo.setPackUnit(matPackUnit);
				} else {
					tip = new OpTip(201, "第" + (j + 1) + "行，包装单位导入失败！");
					return tip;
				}

				manufacturer = this.manufacturerRepository.findByName(manufacturerTxt);

				if (barCode == null || "".equals(barCode)) {
					continue;
				}

				if (manufacturer != null) {
					matEquInfo.setManufacturerId(manufacturer.getId());
				} else {
					tip = new OpTip(201, "第" + (j + 1) + "行，供应商导入失败！");
					return tip;
				}

				if (temPackNum == null || "".equals(temPackNum)) {
					tip = new OpTip(201, "第" + (j + 1) + "行，包装数量导入失败！");
					return tip;
				} else {
					packNum = Integer.valueOf(temPackNum);
				}

				if (temStorePrice == null || "".equals(temStorePrice)) {
					storePrice = (float) 0;
				} else {
					storePrice = Float.valueOf(temStorePrice);
				}
				
				if (lowerStockNum == null || "".equals(lowerStockNum)) {
					lowerStockNum = "0";
				}

				if (spec == null || "".equals(spec)) {
					tip = new OpTip(201, "第" + (j + 1) + "行，型号导入失败！");
					return tip;
				}

				if (borrowType == null || "".equals(borrowType)) {
					tip = new OpTip(201, "第" + (j + 1) + "行，借出类型导入失败！");
					return tip;
				} else {
					if (!temCabinetFlag) {
						if (BorrowType.METER.getMessage().equals(borrowType)) {
							tip = new OpTip(201, "第" + (j + 1) + "行，无暂存柜功能，借出类型填写'盒'");
							return tip;
						}
					}

					if (!BorrowType.METER.getMessage().equals(borrowType)
							&& !BorrowType.PACK.getMessage().equals(borrowType)) {
						tip = new OpTip(201, "第" + (j + 1) + "行，借出类型填写'支'或者'盒'");
						return tip;
					}
				}

				if (pic != null && !"".equals(pic)) {
					finalIconName = UUID.randomUUID().toString() + ".jpg";
					if (this.toolProperties.getUploadPath() == null) {
						tip = new OpTip(201, pic + "图片不存在！");
						return tip;
					}
					// 匹配不到图片按无图片处理
					if (new File(this.toolProperties.getIconsPath() + pic).exists()) {
						Thumbnails.of(this.toolProperties.getIconsPath() + pic).size(600, 450)
								.toFile(this.toolProperties.getUploadPath() + File.separator + finalIconName);
						matEquInfo.setIcon(finalIconName);
					}
				}

				matEquInfo.setBarCode(barCode);
				matEquInfo.setMatEquName(matEquName);
				matEquInfo.setNum(packNum);
				matEquInfo.setBrand(brand);
				matEquInfo.setSpec(spec);
				matEquInfo.setStorePrice(storePrice);
				matEquInfo.setLowerStockNum(Integer.valueOf(lowerStockNum));
				matEquInfo.setBorrowType(BorrowType.valueOfMessage(borrowType));
				matEquInfo.setOldForNew(YesOrNo.YES);
				matEquInfo.setRemark(remark);
				matEquInfo.setStatus(UsingStatus.ENABLE);
				infoList.add(matEquInfo);
			}

			MatEquInfo info;
			List<MatEquInfo> list;
			for (MatEquInfo matInfo : infoList) {
				list = this.matEquInfoRepository.selectByBarCode(matInfo.getBarCode());
				if (list != null && list.size() > 0) {
					info = list.get(0);
					info.setBarCode(matInfo.getBarCode());
					info.setNum(matInfo.getNum());
					info.setStorePrice(matInfo.getStorePrice());
					info.setLowerStockNum(matInfo.getLowerStockNum());
					info.setMatEquName(matInfo.getMatEquName());
					info.setPackUnit(matInfo.getPackUnit());
					info.setManufacturerId(matInfo.getManufacturerId());
					info.setBrand(matInfo.getBrand());
					info.setSpec(matInfo.getSpec());
					info.setBorrowType(matInfo.getBorrowType());
					info.setOldForNew(YesOrNo.YES);
					info.setRemark(matInfo.getRemark());
					info.setIcon(matInfo.getIcon());
					this.matEquInfoRepository.saveAndFlush(info);
				} else {
					this.matEquInfoRepository.save(matInfo);
				}
			}
		} finally {
			if (workbook != null) {
				workbook.close();
			}
		}
		return tip;
	}
}