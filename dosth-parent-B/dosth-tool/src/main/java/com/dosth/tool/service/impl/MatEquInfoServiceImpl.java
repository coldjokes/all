package com.dosth.tool.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cnbaosi.dto.OpTip;
import com.cnbaosi.dto.tool.FeignMat;
import com.dosth.common.constant.UsingStatus;
import com.dosth.common.constant.YesOrNo;
import com.dosth.common.db.Criteria;
import com.dosth.common.db.Criterion;
import com.dosth.common.db.Restrictions;
import com.dosth.common.exception.DoSthException;
import com.dosth.common.exception.DoSthExceptionEnum;
import com.dosth.common.node.ZTreeNode;
import com.dosth.common.util.DateUtil;
import com.dosth.common.util.FileUtil;
import com.dosth.common.util.NumUtil;
import com.dosth.dto.Lattice;
import com.dosth.dto.MatInfo;
import com.dosth.tool.common.config.ToolProperties;
import com.dosth.tool.common.state.BorrowType;
import com.dosth.tool.common.state.MatEqu;
import com.dosth.tool.common.util.ListUtil;
import com.dosth.tool.common.util.MatInfoUtil;
import com.dosth.tool.entity.EquDetailSta;
import com.dosth.tool.entity.Manufacturer;
import com.dosth.tool.entity.MatEquInfo;
import com.dosth.tool.repository.ManufacturerRepository;
import com.dosth.tool.repository.MatCategoryRepository;
import com.dosth.tool.repository.MatEquInfoRepository;
import com.dosth.tool.service.EquDetailStaService;
import com.dosth.tool.service.MatEquInfoService;

import net.coobird.thumbnailator.Thumbnails;

/**
 * 物料/设备信息Service实现
 * 
 * @author guozhidong
 *
 */
@Service
@Transactional
public class MatEquInfoServiceImpl implements MatEquInfoService {

	@Autowired
	private MatEquInfoRepository matEquInfoRepository;
	@Autowired
	private ToolProperties toolProperties;
	@Autowired
	private EquDetailStaService equDetailStaService;
	@Autowired
	private ManufacturerRepository manufacturerRepository;
	@Autowired
	private MatCategoryRepository matCategoryRepository;

	@Override
	public void save(MatEquInfo matEquInfo) throws DoSthException {
		File source = new File(new StringBuilder(toolProperties.getTmpUploadPath()).append(File.separator)
				.append(matEquInfo.getIcon()).toString());

		int lastStr = source.getName().lastIndexOf(".");
		if (lastStr > 0) {
			try {
				Thumbnails.of(source.getAbsolutePath()).size(600, 450)
						.toFile(toolProperties.getUploadPath() + source.getName().substring(0, lastStr) + ".jpg");
			} catch (IOException e) {
				throw new DoSthException(DoSthExceptionEnum.SERVER_ERROR);
			}
		}
		this.matEquInfoRepository.save(matEquInfo);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public MatEquInfo get(Serializable id) throws DoSthException {
		MatEquInfo matEquInfo = this.matEquInfoRepository.findOne(id);
		return matEquInfo;
	}

	@Override
	public MatEquInfo update(MatEquInfo matEquInfo) throws DoSthException {
		return this.matEquInfoRepository.saveAndFlush(matEquInfo);
	}

	@Override
	public void delete(MatEquInfo matEquInfo) throws DoSthException {
		matEquInfo.setStatus(UsingStatus.DISABLE);
		update(matEquInfo);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public Page<MatEquInfo> getPage(int pageNo, int pageSize, String name, String status) throws DoSthException {
		Criteria<MatEquInfo> criteria = new Criteria<MatEquInfo>();
		if (name != null && !"".equals(name)) {
			criteria.add(Restrictions.or(new Criterion[] { Restrictions.like("barCode", name.trim(), true),
					Restrictions.like("matEquName", name.trim(), true) }));
		}
		if (status != null && !"".equals(status) && !"-1".equals(status)) {
			criteria.add(Restrictions.eq("status", UsingStatus.valueOf(status), true));
		}
		Page<MatEquInfo> page = this.matEquInfoRepository.findAll(criteria, new PageRequest(pageNo, pageSize));
		// 检索后总page数小于当前pageNo时，表示为检索后最大pageNo
		if (page.getTotalPages() > 0 && page.getTotalPages() < (page.getNumber() + 1)) {
			pageNo = page.getTotalPages() - 1;
			page = this.matEquInfoRepository.findAll(criteria, new PageRequest(pageNo, pageSize));
		}
		for (MatEquInfo matEquInfo : page.getContent()) {
			matEquInfo.setManufacturerName(
					matEquInfo.getManufacturer() != null ? matEquInfo.getManufacturer().getManufacturerName() : "");
		}
		return page;
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public List<ZTreeNode> tree(Map<String, String> params) throws DoSthException {
		MatEqu matEqu = null;
		Criteria<MatEquInfo> criteria = new Criteria<MatEquInfo>();
		if (params.get("type") != null && !"".equals(params.get("type"))) {
			matEqu = MatEqu.valueOf(params.get("type").toUpperCase());
			criteria.add(Restrictions.eq("matEquType.matEqu", matEqu, true));
		}
		criteria.add(Restrictions.eq("status", UsingStatus.ENABLE, true));
		List<MatEquInfo> list = this.matEquInfoRepository.findAll(criteria);
		List<ZTreeNode> treeList = new ArrayList<>();
		if (matEqu != null && params.get("isRoot") != null && "1".equals(params.get("isRoot"))) {
			treeList.add(new ZTreeNode(matEqu.name(), matEqu.name(), matEqu.getMessage()));
		}
		for (MatEquInfo info : list) {
			treeList.add(new ZTreeNode(String.valueOf(info.getId()), matEqu.name(), info.getMatEquName()));
		}
		return treeList;
	}

	@Override
	public MatEquInfo edit(MatEquInfo matEquInfo) throws DoSthException {
		try {
			File source = new File(new StringBuilder(toolProperties.getTmpUploadPath()).append(File.separator)
					.append(matEquInfo.getIcon()).toString());
			int lastStr = source.getName().lastIndexOf(".");
			if (lastStr > 0) {
				Thumbnails.of(source.getAbsolutePath()).size(600, 450)
						.toFile(toolProperties.getUploadPath() + source.getName().substring(0, lastStr) + ".jpg");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this.matEquInfoRepository.saveAndFlush(matEquInfo);
	}

	@Override
	public String exportExcel(HttpServletRequest request, HttpServletResponse response, String name, String status)
			throws IOException {
		Criteria<MatEquInfo> criteria = new Criteria<MatEquInfo>();
		if (name != null && !"".equals(name) && !"-1".equals(name)) {
			criteria.add(Restrictions.or(new Criterion[] { Restrictions.like("barCode", name, true),
					Restrictions.like("matEquName", name, true) }));
		}
		if (status != null && !"".equals(status) && !"-1".equals(status)) {
			criteria.add(Restrictions.eq("status", UsingStatus.valueOf(status), true));
		}
		List<MatEquInfo> matEquInfoList = this.matEquInfoRepository.findAll(criteria);

		OutputStream output = null;
		HSSFWorkbook workbook = new HSSFWorkbook();
		try {
			HSSFSheet sheet = workbook.createSheet("物料录入信息");

			sheet.setDefaultRowHeightInPoints(20);
			sheet.setDefaultColumnWidth(20);

			HSSFCellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

			HSSFFont fontStyle = workbook.createFont();
			fontStyle.setFontName("宋体");
			cellStyle.setFont(fontStyle);

			HSSFRow row1 = sheet.createRow(0);
			int col = 0;
			row1.createCell(col++).setCellValue("物料编号");
			row1.createCell(col++).setCellValue("物料名称");
			row1.createCell(col++).setCellValue("包装数量");
			row1.createCell(col++).setCellValue("供应商");
			row1.createCell(col++).setCellValue("品牌");
			row1.createCell(col++).setCellValue("物料型号");
			row1.createCell(col++).setCellValue("库存成本");
			row1.createCell(col++).setCellValue("最低库存");
			row1.createCell(col++).setCellValue("借出类型");
			row1.createCell(col++).setCellValue("以旧换新");
			row1.createCell(col++).setCellValue("备注");
			row1.createCell(col++).setCellValue("状态");

			for (int i = 0; i < matEquInfoList.size(); i++) {
				HSSFRow row2 = sheet.createRow(i + 1);
				MatEquInfo matEquInfo = matEquInfoList.get(i);
				col = 0;
				if (matEquInfo.getBarCode() != null && !"".equals(matEquInfo.getBarCode())) {
					String barCode = matEquInfo.getBarCode();
					row2.createCell(col++).setCellValue(barCode);
				} else {
					row2.createCell(col++).setCellValue("");
				}

				if (matEquInfo.getMatEquName() != null && !"".equals(matEquInfo.getMatEquName())) {
					String matEquName = matEquInfo.getMatEquName();
					row2.createCell(col++).setCellValue(matEquName);
				} else {
					row2.createCell(col++).setCellValue("");
				}

				if (matEquInfo.getNum() != null) {
					int num = matEquInfo.getNum();
					row2.createCell(col++).setCellValue(num);
				} else {
					row2.createCell(col++).setCellValue(0);
				}

				if (matEquInfo.getManufacturer() != null
						&& !"".equals(matEquInfo.getManufacturer().getManufacturerName())) {
					String manufacturerName = matEquInfo.getManufacturer().getManufacturerName();
					row2.createCell(col++).setCellValue(manufacturerName);
				} else {
					row2.createCell(col++).setCellValue("");
				}

				if (matEquInfo.getBrand() != null && !"".equals(matEquInfo.getBrand())) {
					String brand = matEquInfo.getBrand();
					row2.createCell(col++).setCellValue(brand);
				} else {
					row2.createCell(col++).setCellValue("");
				}

				if (matEquInfo.getSpec() != null && !"".equals(matEquInfo.getSpec())) {
					String spec = matEquInfo.getSpec();
					row2.createCell(col++).setCellValue(spec);
				} else {
					row2.createCell(col++).setCellValue("");
				}

				if (matEquInfo.getStorePrice() != null) {
					row2.createCell(col++).setCellValue(NumUtil.keep2Point(matEquInfo.getStorePrice()));
				} else {
					row2.createCell(col++).setCellValue(0);
				}

				if (matEquInfo.getLowerStockNum() != null) {
					row2.createCell(col++).setCellValue(matEquInfo.getLowerStockNum());
				} else {
					row2.createCell(col++).setCellValue("");
				}

				if (matEquInfo.getBorrowType() != null) {
					String borrowType = matEquInfo.getBorrowType().getMessage();
					row2.createCell(col++).setCellValue(borrowType);
				} else {
					row2.createCell(col++).setCellValue("");
				}

				if (matEquInfo.getOldForNew() != null) {
					String oldForNew = matEquInfo.getOldForNew().getMessage();
					row2.createCell(col++).setCellValue(oldForNew);
				} else {
					row2.createCell(col++).setCellValue("");
				}

				if (matEquInfo.getRemark() != null && !"".equals(matEquInfo.getRemark())) {
					String remark = matEquInfo.getRemark();
					row2.createCell(col++).setCellValue(remark);
				} else {
					row2.createCell(col++).setCellValue("");
				}

				if (matEquInfo.getStatus() != null) {
					row2.createCell(col++).setCellValue(matEquInfo.getStatus().getMessage());
				} else {
					row2.createCell(col++).setCellValue("");
				}
			}

			String fileName = FileUtil.processFileName(request, "物料录入信息" + DateUtil.getDays());
			output = response.getOutputStream();
			response.reset();
			response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".xls");
			response.setContentType("application/msexcel");
			response.setCharacterEncoding("utf-8");
			workbook.write(output);
		} finally {
			if (output != null) {
				output.flush();
				output.close();
			}
			if (workbook != null) {
				workbook.close();
			}
		}
		return null;
	}

	@Override
	public Lattice getPro(String matId, String latticeId) throws DoSthException {
		EquDetailSta sta = this.equDetailStaService.get(latticeId);
		MatInfo info = MatInfoUtil.createMatInfo(sta.getMatInfo());
		Lattice lattice = new Lattice(sta.getId());
		info.setIcon(FileUtil.convertImageToBase64Data(new File(this.toolProperties.getUploadPath() + info.getIcon())));
		lattice.setMatInfo(info);
		lattice.setHost(sta.getEquDetail().getIp());
		lattice.setPort(sta.getEquDetail().getPort());
		lattice.setColNo(sta.getColNo());
		lattice.setLockIndex(sta.getLockIndex());
		lattice.setBoardNo(sta.getBoardNo());
		lattice.setLevelHeight(sta.getEquDetail().getLevelHt());
		return lattice;
	}

	@Override
	public List<MatEquInfo> findByMatId(String matId) {
		return this.matEquInfoRepository.findByMatId(matId);
	}

	@Override
	public Page<MatEquInfo> getAllMatInfo(int pageNo, int pageSize, String params) {
		Criteria<MatEquInfo> criteria = new Criteria<MatEquInfo>();
		criteria.add(Restrictions.eq("status", UsingStatus.ENABLE, true));
		if (params != null && !"".equals(params)) {
			criteria.add(Restrictions.or(new Criterion[] { Restrictions.like("matEquName", params, true),
					Restrictions.like("barCode", params, true), Restrictions.like("spec", params, true) }));
		}
		List<MatEquInfo> list = this.matEquInfoRepository.findAll(criteria);
		Pageable pageable = new PageRequest(pageNo, pageSize);
		return ListUtil.listConvertToPage(list, pageable);
	}

	@Override
	public List<MatEquInfo> getMatInfoList(String matNameBarCodeSpec) {
		List<MatEquInfo> matList = this.matEquInfoRepository.findAll();
		matList = matList.stream().filter(mat -> UsingStatus.ENABLE.equals(mat.getStatus())
				&& (mat.getMatEquName().indexOf(matNameBarCodeSpec.trim().toLowerCase()) != -1
						|| mat.getMatEquName().indexOf(matNameBarCodeSpec.trim().toUpperCase()) != -1
						|| (mat.getBarCode() != null
								&& mat.getBarCode().indexOf(matNameBarCodeSpec.trim().toLowerCase()) != -1)
						|| (mat.getBarCode() != null
								&& mat.getBarCode().indexOf(matNameBarCodeSpec.trim().toUpperCase()) != -1)
						|| (mat.getSpec() != null && mat.getSpec().indexOf(matNameBarCodeSpec.trim().toLowerCase()) != -1)
						|| (mat.getSpec() != null && mat.getSpec().indexOf(matNameBarCodeSpec.trim().toUpperCase()) != -1)))
				.collect(Collectors.toList());
		return matList;
	}

	@Override
	public MatEquInfo getMatDetailByBarCode(String barCode) {
		List<MatEquInfo> infoList = this.matEquInfoRepository.selectByBarCode(barCode);
		if (infoList != null && infoList.size() > 0) {
			return infoList.get(0);
		}
		return null;
	}

	@Override
	public OpTip syncMat(List<FeignMat> matList) {
		OpTip tip = new OpTip(200, "物料同步成功");
		MatEquInfo info;
		List<Manufacturer> manufacturerList;
		try {
			for (FeignMat mat : matList) {
				info = getMatDetailByBarCode(mat.getBarCode());
				if (info == null) {
					info = new MatEquInfo();
					info.setStatus(UsingStatus.ENABLE);
					info.setLowerStockNum(3);
					info.setOldForNew(YesOrNo.YES);
				}
				info.setBarCode(mat.getBarCode());
				info.setMatEquName(mat.getMatName());
				info.setSpec(mat.getSpec());
				info.setNum(mat.getPackNum());
				info.setPackUnit("盒");
				info.setStorePrice(mat.getStotePrice());
				info.setBrand(mat.getBrand());
				info.setIcon(mat.getIcon());
				info.setBorrowType(BorrowType.PACK);
				if (!BorrowType.PACK.getMessage().equals(mat.getBorrowType())) {
					info.setBorrowType(BorrowType.METER);
				}
				info.setManufacturerId("001");
				manufacturerList = this.manufacturerRepository.getManufacturerByName(mat.getManufacturerName());
				if (manufacturerList != null && manufacturerList.size() > 0) {
					info.setManufacturerId(manufacturerList.get(0).getId());
				}
				this.matEquInfoRepository.saveAndFlush(info);
			}
		} catch (Exception e) {
			tip.setCode(201);
			tip.setMessage("物料同步失败");
		}
		return tip;
	}

	@Override
	public void updateStatus(String infoId) throws DoSthException {
		List<EquDetailSta> staList = this.equDetailStaService.findEquDetailStaListByMatInfoId(infoId);
		if (staList != null && staList.size() > 0) {
			throw new DoSthException(201, "当前物料正在使用中,不能作此操作", null);
		}
		MatEquInfo matEquInfo = this.matEquInfoRepository.getOne(infoId);
		if (matEquInfo.getStatus().equals(UsingStatus.ENABLE)) {
			matEquInfo.setStatus(UsingStatus.DISABLE);
			this.matCategoryRepository.deleteBymatId(infoId);
		} else {
			matEquInfo.setStatus(UsingStatus.ENABLE);
		}
		this.matEquInfoRepository.saveAndFlush(matEquInfo);
	}
}