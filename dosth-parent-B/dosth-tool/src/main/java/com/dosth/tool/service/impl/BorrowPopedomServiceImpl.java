package com.dosth.tool.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.dosth.common.constant.UsingStatus;
import com.dosth.common.exception.DoSthException;
import com.dosth.common.node.TypeNode;
import com.dosth.common.node.ZTreeNode;
import com.dosth.tool.common.config.ToolProperties;
import com.dosth.tool.common.state.BorrowVoucher;
import com.dosth.tool.entity.BorrowPopedom;
import com.dosth.tool.entity.Cart;
import com.dosth.tool.entity.DailyLimit;
import com.dosth.tool.entity.DeptBorrowPopedom;
import com.dosth.tool.entity.MatCategory;
import com.dosth.tool.entity.MatCategoryTree;
import com.dosth.tool.entity.MatEquInfo;
import com.dosth.tool.repository.BorrowPopedomRepository;
import com.dosth.tool.repository.CartRepository;
import com.dosth.tool.repository.DailyLimitRepository;
import com.dosth.tool.repository.DeptBorrowPopedomRepository;
import com.dosth.tool.repository.DeptRepository;
import com.dosth.tool.repository.MatCategoryRepository;
import com.dosth.tool.repository.MatCategoryTreeRepository;
import com.dosth.tool.repository.MatEquInfoRepository;
import com.dosth.tool.service.AdminService;
import com.dosth.tool.service.BorrowPopedomService;
import com.dosth.tool.service.DailyLimitService;
import com.dosth.tool.service.MatCategoryService;
import com.dosth.tool.vo.ViewDept;
import com.dosth.toolcabinet.dto.RpcBorrowType;
import com.dosth.toolcabinet.dto.UserInfo;
import com.dosth.toolcabinet.enums.EnumBorrowType;

/**
 * 
 * @description 借出权限service实现
 * @author guozhidong
 *
 */
@Service
@Transactional
public class BorrowPopedomServiceImpl implements BorrowPopedomService {

	@Autowired
	private AdminService adminService;
	@Autowired
	private DeptRepository deptRepository;
	@Autowired
	private CartRepository cartRepository;
	@Autowired
	private MatEquInfoRepository matEquInfoRepository;
	@Autowired
	private BorrowPopedomRepository borrowPopedomRepository;
	@Autowired
	private DailyLimitService dailyLimitService;
	@Autowired
	private MatCategoryService matCategoryService;
	@Autowired
	private MatCategoryRepository matCategoryRepository;
	@Autowired
	private MatCategoryTreeRepository matCategoryTreeRepository;
	@Autowired
	private DeptBorrowPopedomRepository deptBorrowPopedomRepository;
	@Autowired
	private DailyLimitRepository dailyLimitRepository;
	@Autowired
	private ToolProperties toolProperties;

	@Override
	public void save(BorrowPopedom popedom) throws DoSthException {
		this.borrowPopedomRepository.save(popedom);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
	public BorrowPopedom get(Serializable id) throws DoSthException {
		return this.borrowPopedomRepository.findOne(id);
	}

	@Override
	public BorrowPopedom update(BorrowPopedom popedom) throws DoSthException {
		return this.borrowPopedomRepository.saveAndFlush(popedom);
	}

	@Override
	public void delete(BorrowPopedom popedom) throws DoSthException {
		this.borrowPopedomRepository.delete(popedom);
	}

	@Override
	public List<BorrowPopedom> getPopedomList(String accountId) {
		return this.borrowPopedomRepository.getPopedomList(accountId);
	}

	@Override
	public void deletePopedomListByAccountId(String accountId) {
		this.borrowPopedomRepository.delete(this.borrowPopedomRepository.getPopedomList(accountId));
	}

	@Override
	public Boolean bindBorrowPopedoms(String opObjId, String popedoms) {
		try {
			if (opObjId.startsWith("d_")) { // 绑定部门
				ViewDept viewDept = this.deptRepository.getOne(opObjId.substring("d_".length()));
				List<ViewDept> deptList = this.getAllViewDept(viewDept, new ArrayList<>());
				List<DeptBorrowPopedom> dbpList;
				for (ViewDept dept : deptList) {
					dbpList = this.deptBorrowPopedomRepository.getPopedomListByDeptId(dept.getDeptId());
					this.deptBorrowPopedomRepository.delete(dbpList);
				}
				String[] arr = popedoms.split(";");
				List<String> accountIdList;
				for (String popedom : arr) {
					if (popedom.indexOf("ROOT") != -1) {
						continue;
					}
					// 添加新权限
					String[] popedomArr = popedom.split(":");
					for (ViewDept dept : deptList) {
						this.deptBorrowPopedomRepository.	save(new DeptBorrowPopedom(dept.getDeptId(), EnumBorrowType.valueOf(popedomArr[0]),
								popedomArr.length < 2 ? popedomArr[0] : popedomArr[1]));
					}
				}
				accountIdList = this.adminService.getAccountIdListByDeptId(opObjId.substring("d_".length()));
				for (String accountId : accountIdList) {
					this.bindBorrowPopedoms(accountId, popedoms);
				}
			} else { // 绑定个人
				this.bindSingleBorrowPopedoms(opObjId, popedoms);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * 
	 * @param dept
	 * @param deptList
	 * @return
	 */
	private List<ViewDept> getAllViewDept(ViewDept dept, List<ViewDept> deptList) {
		if (!deptList.contains(dept) && "OK".equals(dept.getDeptStatus())) {
			deptList.add(dept);
		}
		List<ViewDept> childDeptList = this.deptRepository.getChildDeptList(dept.getDeptId());
		if (childDeptList != null && childDeptList.size() > 0) {
			for (ViewDept childDept : childDeptList) {
				getAllViewDept(childDept, deptList);
			}
		}
		return deptList;
	}
	
	/**
	 * @description 绑定个人账户权限
	 * @param accountId 账户Id
	 * @param popedoms 权限
	 */
	private void bindSingleBorrowPopedoms(String accountId, String popedoms) {
		// 删除购物车
		List<Cart> cartList = this.cartRepository.selectCartByAccount(accountId);
		if (CollectionUtils.isNotEmpty(cartList)) {
			for (Cart cart : cartList) {
				this.cartRepository.delete(cart);
			}
		}
		// 删除历史权限
		deletePopedomListByAccountId(accountId);
//		this.dailyLimitService.deleteByAccountId(accountId);
		String[] arr = popedoms.split(";");
		// 遍历所有权限
		String ids;
		Set<String> set = new HashSet<>();

		for (String popedom : arr) {
			if (popedom.indexOf("ROOT") != -1) {
				continue;
			}
			// 添加新权限
			String[] popedomArr = popedom.split(":");
			save(new BorrowPopedom(accountId, EnumBorrowType.valueOf(popedomArr[0]),
					popedomArr.length < 2 ? popedomArr[0] : popedomArr[1]));
			if (EnumBorrowType.valueOf(popedom.split(":")[0]) == EnumBorrowType.GRID) {// 全部
				List<MatEquInfo> matTypeList = this.matEquInfoRepository.findAll();
				for (MatEquInfo matInfo : matTypeList) {
					set.add(matInfo.getId());
				}
			} else { // 设备，工序，零件，自定义
				ids = popedomArr.length < 2 ? popedomArr[0] : popedomArr[1];
				String[] id = ids.split(",");
				if (id[0].equals(popedomArr[0])) {// 一级菜单全选的场合
					List<MatCategoryTree> treeList = this.matCategoryTreeRepository
							.findByType(EnumBorrowType.valueOf(popedomArr[0]), UsingStatus.ENABLE);
					for (MatCategoryTree tree : treeList) {
						List<MatCategory> categoryList = this.matCategoryRepository.findByNode(tree.getId());
						for (MatCategory category : categoryList) {
							set.add(category.getMatInfoId());
						}
					}
				} else {// 一级菜单没有全选，选择子级菜单
					for (int i = 0; i < id.length; i++) {
						// 获取该节点所有子级菜单对象
						List<MatCategoryTree> treeList = this.matCategoryTreeRepository.findPathNode(id[i], UsingStatus.ENABLE);
						if(treeList == null || treeList.size() <= 0) {// 判断该节点是否存在子级菜单
							// 不存在子级菜单（本身即为最低级）
							List<MatCategory> categoryList = this.matCategoryRepository.findByNode(id[i]);
							for (MatCategory category : categoryList) {
								set.add(category.getMatInfoId());
							}
						}else {// 该节点菜单包含子级菜单的场合
							// 循环获取所有子级菜单绑定的物料
							for(MatCategoryTree tree : treeList) {
								List<MatCategory> categoryList = this.matCategoryRepository.findByNode(tree.getId());
								for (MatCategory category : categoryList) {
									set.add(category.getMatInfoId());
								}
							}
						}
					}
				}
			}
		}
		// 获取已存在限额设置的物料list
		List<String> matIds = this.dailyLimitRepository.findMatIdByAccountId(accountId);
		for (String s : set) {
			if(matIds != null && matIds.contains(s)) {// 已被限额设置的物料排除，未被设置的insert
				continue;
			}
			this.dailyLimitService.save(new DailyLimit(accountId, s, 0, 0, 0));
		}
	}

	@Override
	public List<ZTreeNode> initBorrowPopedomTree(String opObjId) {
		List<ZTreeNode> borrowPopedomTree = new ArrayList<>();
		ZTreeNode node;
		// 取出树的所有节点
		node=new ZTreeNode("ROOT", "", "取料权限");
		node.setLevel(0);
		node.setIcon("/static/img/one.png");
		borrowPopedomTree.add(0,node);
		for (EnumBorrowType type : EnumBorrowType.values()) {
			if (EnumBorrowType.RETURN.equals(type) || EnumBorrowType.ADMIN.equals(type)) {
				continue;
			}
			node=new ZTreeNode(type.name(), "ROOT", type.getDesc());
			node.setIcon("/static/img/two.png");
			borrowPopedomTree.add(node);
		}
		// 添加物料类型树
//		borrowPopedomTree.addAll(this.getMatTypeTree());
		// 添加其它类别树（类型，设备，工序，零件，自定义）
		
		borrowPopedomTree.addAll(this.getCategoryTree());

		if ("-1".equals(opObjId)) {
			return borrowPopedomTree;
		} else if (opObjId.startsWith("d_")) { // 部门权限
			Map<String, String[]> map = new HashMap<String, String[]>();
			
			ViewDept viewDept = this.deptRepository.getOne(opObjId.substring("d_".length()));
			List<ViewDept> deptList = this.getAllParentDept(viewDept, new ArrayList<>());
			
			// 获取该人员所有已获得的领取权限
			List<DeptBorrowPopedom> borrowPopedoms = new ArrayList<>();
			for (ViewDept dept : deptList) {
				borrowPopedoms.addAll(this.deptBorrowPopedomRepository.getPopedomListByDeptId(dept.getDeptId()));
			}

			if (borrowPopedoms.size() > 0 && borrowPopedoms != null) {
				map.put("ROOT", null);
				// map<类型，权限ID集合>
				for (DeptBorrowPopedom borrowPopedom : borrowPopedoms) {
					String popedom = borrowPopedom.getBorrowPopedom().toString();
					if (borrowPopedom.getPopedoms() != null) {
						String[] popedomIds = borrowPopedom.getPopedoms().split(",");
						StringBuilder build = new StringBuilder();
						for (String id : popedomIds) {
							build.append(id + ",");
							// 获取子节点
							build = getChildIds(id, build);

						}
						map.put(popedom, build.toString().split(","));
					} else {
						map.put(popedom, null);
					}
				}
			}
			String type = null;
			for (ZTreeNode tree : borrowPopedomTree) {
				tree.setIsOpen(true);
				if (tree.getEquType() == null) {
					for (Map.Entry<String, String[]> entry : map.entrySet()) {
						if (entry.getKey().equals(tree.getId())) {
							tree.setChecked(true);
						}
						if (entry.getKey().equals(tree.getpId())) {
							if (entry.getValue() != null) {
								for (String val : entry.getValue()) {
									if (val.equals(tree.getId())) {
										tree.setChecked(true);
									}
								}
							}
						}
					}
				} else {
					for (Entry<String, String[]> entry : map.entrySet()) {
						if (entry.getKey().equals(tree.getEquType())) {
							if (tree.getEquType().equals(type)) {
								tree.setChecked(true);
								continue;
							}
							if (entry.getValue()[0].equals(entry.getKey())) {
								type = tree.getEquType();
								tree.setChecked(true);
							} else {
								if (entry.getValue() != null) {
									for (String val : entry.getValue()) {
										if (val.equals(tree.getId())) {
											tree.setChecked(true);
										}
									}
								}
							}
						}
					}
				}
			}
			return borrowPopedomTree;
		} else { // 个人权限
			Map<String, String[]> map = new HashMap<String, String[]>();
			// 获取该人员所有已获得的领取权限
			List<BorrowPopedom> borrowPopedoms = this.borrowPopedomRepository.getPopedomIds(opObjId);

			if (borrowPopedoms.size() > 0 && borrowPopedoms != null) {
				map.put("ROOT", null);
				// map<类型，权限ID集合>
				for (BorrowPopedom borrowPopedom : borrowPopedoms) {
					String popedom = borrowPopedom.getBorrowPopedom().toString();
					if (borrowPopedom.getPopedoms() != null) {
						String[] popedomIds = borrowPopedom.getPopedoms().split(",");
						StringBuilder build = new StringBuilder();
						for (String id : popedomIds) {
							build.append(id + ",");
							// 获取子节点
							build = getChildIds(id, build);
						}
						map.put(popedom, build.toString().split(","));
					} else {
						map.put(popedom, null);
					}
				}
			}

			String type = null;
			for (ZTreeNode tree : borrowPopedomTree) {
				tree.setIsOpen(true);
				if (tree.getEquType() == null) {
					for (Map.Entry<String, String[]> entry : map.entrySet()) {
						if (entry.getKey().equals(tree.getId())) {
							tree.setChecked(true);
						}
						if (entry.getKey().equals(tree.getpId())) {
							if (entry.getValue() != null) {
								for (String val : entry.getValue()) {
									if (val.equals(tree.getId())) {
										tree.setChecked(true);
									}
								}
							}
						}
					}
				} else {
					for (Map.Entry<String, String[]> entry : map.entrySet()) {
						if (entry.getKey().equals(tree.getEquType())) {
							if (tree.getEquType().equals(type)) {
								tree.setChecked(true);
								continue;
							}
							if (entry.getValue()[0].equals(entry.getKey())) {
								type = tree.getEquType();
								tree.setChecked(true);
							} else {
								if (entry.getValue() != null) {
									for (String val : entry.getValue()) {
										if (val.equals(tree.getId())) {
											tree.setChecked(true);
										}
									}
								}
							}
						}
					}
				}
			}
			return borrowPopedomTree;
		}
	}
	
	/**
	 * @description 递归上级部门
	 * @param viewDept 部门
	 * @param deptList 部门列表
	 * @return
	 */
	private List<ViewDept> getAllParentDept(ViewDept viewDept, List<ViewDept> deptList) {
		if (!deptList.contains(viewDept) && "OK".equals(viewDept.getDeptStatus())) {
			deptList.add(viewDept);
		}
		// 上级部门为空或部门Id与上级Id相同均属于顶级部门
		if (viewDept.getDeptPId() != null && !"".equals(viewDept.getDeptPId()) && !viewDept.getDeptId().equals(viewDept.getDeptPId())) {
			ViewDept dept = this.deptRepository.getOne(viewDept.getDeptPId());
			getAllParentDept(dept, deptList);
		}
		return deptList;
	}

	/**
	 * @description 获取分类树
	 * @param cabinetId 柜子Id
	 * @return
	 */
	private List<ZTreeNode> getCategoryTree() {
		List<ZTreeNode> tree = this.matCategoryService.categoryTree();
		List<ZTreeNode> categoryTree = new ArrayList<>();
		for (ZTreeNode node : tree) {
			if (node.getpId() == null || "".equals(node.getpId()) || node.getpId().equals(node.getId())) {// 过滤掉主分支，将次级分支加到权限树中
				continue;
			} else if (node.getpId().equals("1")) {// 按照原父级ID添加到各分支树中
				node.setpId(EnumBorrowType.MATTYPE.name());
			} else if (node.getpId().equals("2")) {
				node.setpId(EnumBorrowType.REQREF.name());
			} else if (node.getpId().equals("3")) {
				node.setpId(EnumBorrowType.PROCREF.name());
			} else if (node.getpId().equals("4")) {
				node.setpId(EnumBorrowType.PARTS.name());
			} else if (node.getpId().equals("5")) {
				node.setpId(EnumBorrowType.CUSTOM.name());
			}
			categoryTree.add(node);
		}
		return categoryTree;
	}

	@Override
	public List<RpcBorrowType> getBorrowTypeList(String cabinetId, String accountId) {
		List<RpcBorrowType> typeList = new ArrayList<>();

		// 根据帐号ID获取用户角色
		UserInfo userRole = this.adminService.getUserRole(accountId);
		
		String applyVoucherUrl = this.toolProperties.getApplyVoucherUrl();
		// 柜子管理员获取所有权限
		if (userRole != null && (userRole.getRoleId().equals("101") || userRole.getRoleId().equals("102"))) {
			typeList.add(new RpcBorrowType("GRID", "全部"));
			for (BorrowVoucher voucher : BorrowVoucher.values()) {
				if (BorrowVoucher.CUSTOM.equals(voucher)) {
					if (applyVoucherUrl != null && !"".equals(applyVoucherUrl)) {
						continue;
					}
				}
				if (BorrowVoucher.APPLYVOUCHER.equals(voucher)) {
					if (applyVoucherUrl == null || "".equals(applyVoucherUrl)) {
						continue;
					}
				}
				typeList.add(new RpcBorrowType(voucher.name(), voucher.getMessage()));
			}
			typeList.add(new RpcBorrowType("RETURN", "暂存/归还"));
			typeList.add(new RpcBorrowType("ADMIN", "管理员"));
		} else { // 非管理员获取分配权限
			List<BorrowPopedom> popedomList = getPopedomList(accountId);
			for (BorrowPopedom popedom : popedomList) {
				if (BorrowVoucher.CUSTOM.name().equals(popedom.getBorrowPopedom().name())) {
					if (applyVoucherUrl != null && !"".equals(applyVoucherUrl)) {
						continue;
					}
				}
				typeList.add(
						new RpcBorrowType(popedom.getBorrowPopedom().name(), popedom.getBorrowPopedom().getDesc()));
			}
			if (applyVoucherUrl != null && !"".equals(applyVoucherUrl)) {
				typeList.add(
						new RpcBorrowType(BorrowVoucher.APPLYVOUCHER.name(), BorrowVoucher.APPLYVOUCHER.getMessage()));
			}
			typeList.add(new RpcBorrowType("RETURN", "暂存/归还"));
		}
		return typeList;
	}

	@Override
	public List<TypeNode> getCategoryTree(String cabinetId, String accountId, String type) {
		List<TypeNode> categoryTree = new ArrayList<>();

		// 根据帐号ID获取用户角色
		UserInfo userRole = this.adminService.getUserRole(accountId);

		// 取出树的所有节点
		List<ZTreeNode> categoryList = this.matCategoryService.categoryTree();
		// 柜子管理员获取所有权限
		List<BorrowPopedom> popedomList = new ArrayList<BorrowPopedom>();

		if (userRole != null && (userRole.getRoleId().equals("101") || userRole.getRoleId().equals("102"))) {
			popedomList.add(new BorrowPopedom(accountId, EnumBorrowType.valueOf(type), type));
		} else {
			popedomList = this.borrowPopedomRepository.getPopedomListByBorrowType(accountId,
					EnumBorrowType.valueOf(type));
		}
		
//		EquSetting setting = this.equSettingService.get(cabinetId);
//		if (setting.getAccountId() != null && accountId.equals(setting.getAccountId())) {
//			popedomList.add(new BorrowPopedom(accountId, EnumBorrowType.valueOf(type), type));
//		} else {
//			popedomList = this.borrowPopedomRepository.getPopedomListByBorrowType(accountId,
//					EnumBorrowType.valueOf(type));
//		}

		// 获取绑定权限的所有路径
		StringBuilder buildChild = new StringBuilder();
		String parentStr = new String();
		for (BorrowPopedom popedom : popedomList) {
			String[] popedomArr = popedom.getPopedoms().split(",");
			for (String arr : popedomArr) {
				// 获取子节点
				buildChild = getChildIds(arr, buildChild);
				// 获取父节点
				for (ZTreeNode node : categoryList) {
					if (node.getId().equals(arr)) {
						parentStr += arr + ",";
						parentStr += node.getPath();
					}
				}
			}
		}
		String ids = parentStr + buildChild.toString();

		// 获取选择的类型Id
		String typeId = EnumBorrowType.valueOf(type).getCode();
		for (ZTreeNode node : categoryList) {
			// 筛选出选择的类型节点包括子节点
			if (node.getId().equals(typeId)) {
				TypeNode typeNode = new TypeNode(typeId, node.getName());
				typeNode.setChildren(getChildrenNode(node.getId(), categoryList, popedomList, ids));
				categoryTree.add(typeNode);
			}
		}
		return categoryTree;
	}

	private List<TypeNode> getChildrenNode(String id, List<ZTreeNode> categoryList, List<BorrowPopedom> popedomList,
			String ids) {
		List<TypeNode> newTrees = new ArrayList<>();
		for (ZTreeNode category : categoryList) {
			// 过滤领取权限
			for (BorrowPopedom popedom : popedomList) {
				if (!popedom.getPopedoms().split(",")[0].equals(popedom.getBorrowPopedom().toString())) {
					if (ids.indexOf(category.getId()) != -1) {
						if (category.getpId() != null && category.getpId().equals(id) && !category.getId().equals(id)) {
							TypeNode tn = new TypeNode(category.getId(), category.getpId(), category.getName());
							// 递归获取子节点下的子节点，即设置树控件中的children
							tn.setChildren(getChildrenNode(category.getId(), categoryList, popedomList, ids));
							newTrees.add(tn);
						}
					}
				} else {
					if (category.getpId() != null && category.getpId().equals(id) && !category.getId().equals(id)) {
						TypeNode tn = new TypeNode(category.getId(), category.getpId(), category.getName());
						// 递归获取子节点下的子节点，即设置树控件中的children
						tn.setChildren(getChildrenNode(category.getId(), categoryList, popedomList, ids));
						newTrees.add(tn);
					}
				}
			}
		}
		return newTrees;
	}

	// 获取节点的所有子节点
	private StringBuilder getChildIds(String id, StringBuilder build) {
		// 获取子节点(节点的父节点path中包含该节点Id,即等于属于该节点的子节点)
		List<MatCategoryTree> treeList = this.matCategoryTreeRepository.findPathNode(id, UsingStatus.ENABLE);
		for(MatCategoryTree tree : treeList) {
			build.append(tree.getId() + ",");
		}
		return build;
	}
//	/**
//	 * @description 过滤权限
//	 * @param cabinetId  柜子Id
//	 * @param accountId  帐户Id
//	 * @param borrowType 借出类型
//	 * @param list       原始权限列表
//	 * @return
//	 */
//	private List<TypeNode> filterPopedom(String cabinetId, String accountId, EnumBorrowType borrowType,
//			List<TypeNode> list) {
//		EquSetting setting = this.equSettingService.get(cabinetId);
//		// 柜子管理员获取所有权限
//		if (setting.getAccountId() != null && accountId.equals(setting.getAccountId())) {
//			return list;
//		}
//		List<TypeNode> result = new ArrayList<>();
//		List<BorrowPopedom> popedomList = this.borrowPopedomRepository.getPopedomListByBorrowType(accountId,
//				borrowType);
//		for (TypeNode method : list) {
//			for (BorrowPopedom popedom : popedomList) {
//				// 全选的场合表示全部
//				if(popedom.getPopedoms().indexOf("MATTYPE") == 0) {
//					return list;
//				}
//				// 已分配权限中不存在当前权限忽略
//				if (popedom.getPopedoms().indexOf(method.getId()) == -1) {
//					continue;
//				}
//				// 新增不存在的权限
//				if (!result.contains(method)) {
//					result.add(method);
//				}
//			}
//		}
//		return result;
//	}

}