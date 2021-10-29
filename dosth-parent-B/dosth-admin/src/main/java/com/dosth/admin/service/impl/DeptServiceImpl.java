package com.dosth.admin.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cnbaosi.dto.admin.FeignDept;
import com.dosth.admin.common.exception.BusinessException;
import com.dosth.admin.constant.state.ManagerStatus;
import com.dosth.admin.entity.Dept;
import com.dosth.admin.repository.DeptRepository;
import com.dosth.admin.service.DeptService;
import com.dosth.common.db.Criteria;
import com.dosth.common.db.Restrictions;
import com.dosth.common.exception.DoSthException;
import com.dosth.common.node.ZTreeNode;
import com.dosth.toolcabinet.dto.DeptInfo;
import com.dosth.util.OpTip;

/**
 * 部门Service实现
 * 
 * @author liweifeng
 *
 */
@Service
@Transactional
public class DeptServiceImpl implements DeptService {

	@Autowired
	private DeptRepository deptRepository;

	@Override
	public void save(Dept dept) throws DoSthException {
		dept.setDeptName(dept.getDeptName());
		dept.setpId(dept.getpId());
		dept.setStatus(ManagerStatus.OK);
		this.deptRepository.save(dept);
		dept.setFullIds(dept.getpId() + "," + dept.getId());
		this.deptRepository.flush();
	}

	@Override
	public Dept update(Dept dept) throws DoSthException {
		return this.deptRepository.saveAndFlush(dept);
	}

	@Override
	public void delete(Dept dept) throws DoSthException {
		this.deptRepository.delete(dept);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public Dept get(Serializable id) throws DoSthException {
		Dept dept = this.deptRepository.findOne(id);
		return dept;
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public List<ZTreeNode> tree(Map<String, String> paramMap) throws BusinessException {
		List<Dept> deptList = this.deptRepository.findAll(new Specification<Dept>() {
			@Override
			public Predicate toPredicate(Root<Dept> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return null;
			}
		});
		ZTreeNode node;

		List<ZTreeNode> treeNode = new ArrayList<>();
		for (Dept dept : deptList) {
			// 冻结部门忽略
			if (ManagerStatus.DELETED.equals(dept.getStatus()) || ManagerStatus.FREEZED.equals(dept.getStatus())) {
				continue;
			}
			node = new ZTreeNode("d_" + dept.getId(), "d_" + (dept.getpId() != null ? dept.getpId() : dept.getpId()),
					dept.getDeptName(), dept.getpId() != dept.getpId());
			node.setIcon("/static/img/part.png");
			if (dept.getpId() == null || "".equals(dept.getpId())) {
				node.setIcon("/static/img/comp.png");
			}
			treeNode.add(node);
		}
		return treeNode;
	}

	@Override
	public Page<Dept> getPage(int pageNo, int pageSize, String name) throws BusinessException {
		Criteria<Dept> criteria = new Criteria<Dept>();
		criteria.add(Restrictions.eq("status", ManagerStatus.OK, true));
		if (name != null && !"".equals(name)) {
			criteria.add(Restrictions.like("deptName", name, true));
		}
		Page<Dept> page = deptRepository.findAll(criteria, new PageRequest(pageNo, pageSize));
		// 检索后总page数小于当前pageNo时，表示为检索后最大pageNo
		if (page.getTotalPages() > 0 && page.getTotalPages() < (page.getNumber() + 1)) {
			pageNo = page.getTotalPages() - 1;
			page = this.deptRepository.findAll(criteria, new PageRequest(pageNo, pageSize));
		}
		return page;
	}

	@Override
	public Dept findDeptByDeptName(String deptName) {
		return this.deptRepository.findDeptByDeptName(deptName);
	}

	@Override
	public List<DeptInfo> getDeptInfo() {
		List<DeptInfo> infoList = new ArrayList<>();
		List<Dept> infos = this.deptRepository.findAll();
		for (Dept info : infos) {
			infoList.add(new DeptInfo(info.getId(), info.getDeptName()));
		}
		return infoList;
	}

	@Override
	public OpTip syncFeignDept(List<FeignDept> deptList) {
		OpTip tip = new OpTip(200, "部门同步成功");
		try {
			for (FeignDept feignDept : deptList) {
				if (StringUtils.isBlank(feignDept.getDeptNo())) {
					tip.setCode(201);
					tip.setMessage("部门编号不能为空");
					return tip;
				} else {

				}
				if (StringUtils.isBlank(feignDept.getDeptName())) {
					tip.setCode(201);
					tip.setMessage("部门名称不能为空");
					return tip;
				}
				List<Dept> deptNoList = this.deptRepository.getDeptListByDeptNo(feignDept.getDeptNo());
				if (CollectionUtils.isNotEmpty(deptNoList)) {
					tip.setCode(201);
					tip.setMessage("部门编号不能重复");
					return tip;
				}
				if (StringUtils.isNotBlank(feignDept.getParentDeptNo())) {
					List<Dept> parentDeptNoList = this.deptRepository.getDeptListByDeptNo(feignDept.getParentDeptNo());
					if (CollectionUtils.isEmpty(parentDeptNoList)) {
						tip.setCode(201);
						tip.setMessage("未匹配到上级部门编号");
						return tip;
					}
				}
				saveFeignDept(feignDept);
			}
		} catch (Exception e) {
			tip.setCode(201);
			tip.setMessage("部门同步失败");
		}
		return tip;
	}

	@Override
	public Dept saveFeignDept(FeignDept feignDept) {
		List<Dept> deptList;
		Dept dept;
		String parentDeptId = null;
		if (StringUtils.isNotBlank(feignDept.getDeptNo())) {
			deptList = this.deptRepository.getDeptListByDeptNo(feignDept.getDeptNo());
		} else {
			deptList = this.deptRepository.getDeptListByDeptName(feignDept.getDeptName());
		}
		if (deptList != null && deptList.size() > 0) {
			dept = deptList.get(0);
			dept.setDeptName(feignDept.getDeptName());
		} else {
			dept = new Dept();
			dept.setDeptNo(feignDept.getDeptNo());
			dept.setDeptName(feignDept.getDeptName());
			dept.setStatus(ManagerStatus.OK);
			dept = this.deptRepository.save(dept);
		}
		if ((feignDept.getParentDeptNo() != null && !"".equals(feignDept.getParentDeptNo()))
				|| (feignDept.getParentDeptName() != null && !"".equals(feignDept.getParentDeptName()))) { // 存在上级
			List<Dept> pDeptList = null;
			if (feignDept.getParentDeptNo() != null) {
				pDeptList = this.deptRepository.getDeptListByDeptNo(feignDept.getParentDeptNo());
			} else {
				pDeptList = this.deptRepository.getDeptListByDeptName(feignDept.getParentDeptName());
			}
			if (pDeptList != null && pDeptList.size() > 0) {
				parentDeptId = pDeptList.get(0).getId();
			} else {
				pDeptList = this.deptRepository.findAll();
				pDeptList = pDeptList.stream().filter(
						d -> ManagerStatus.OK.equals(d.getStatus()) && (d.getpId() == null || "".equals(d.getpId())))
						.collect(Collectors.toList());
				if (pDeptList != null && pDeptList.size() > 0) {
					parentDeptId = pDeptList.get(0).getId();
				}
			}
			dept.setpId(parentDeptId);
			dept.setFullIds(parentDeptId + "," + dept.getId());
		}
		dept = this.deptRepository.saveAndFlush(dept);
		return dept;
	}

	@Override
	public List<Dept> getDeptByPid(String pId) {
		return this.deptRepository.getDeptByPid(pId);
	}

}