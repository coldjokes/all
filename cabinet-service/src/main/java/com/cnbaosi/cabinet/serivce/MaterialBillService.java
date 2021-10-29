package com.cnbaosi.cabinet.serivce;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.cnbaosi.cabinet.entity.criteria.MaterialBillCriteria;
import com.cnbaosi.cabinet.entity.criteria.common.DateRangeCriteria;
import com.cnbaosi.cabinet.entity.criteria.common.PageCriteria;
import com.cnbaosi.cabinet.entity.modal.MaterialBill;

/**
 * 物料流水相关方法
 * 
 * @author Yifeng Wang
 */
public interface MaterialBillService extends IService<MaterialBill> {

	/**
	 * 新增流水
	 * 
	 * @param materialBill
	 * @return
	 */
	boolean addBills(List<MaterialBill> materialBillList);

	/**
	 * 导出记录
	 */
	void exportMaterialBillRecord(MaterialBillCriteria matBillCriteria, DateRangeCriteria dateRangeCriteria, HttpServletRequest request, HttpServletResponse response);

	/**
	 * 获取流水记录（分页）
	 * @param matBillCriteria
	 * @param pageCriteria
	 * @param dateRangeCriteria
	 * @return
	 */
	Page<MaterialBill> getPagedBillRecordList(MaterialBillCriteria matBillCriteria, PageCriteria pageCriteria, DateRangeCriteria dateRangeCriteria);
	
	/**
	 * 获取流水记录
	 * @param matBillCriteria
	 * @param dateRangeCriteria
	 * @return
	 */
	List<MaterialBill> getBillRecordList(MaterialBillCriteria matBillCriteria, DateRangeCriteria dateRangeCriteria);
}
