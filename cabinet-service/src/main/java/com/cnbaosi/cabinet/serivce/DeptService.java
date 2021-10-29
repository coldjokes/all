package com.cnbaosi.cabinet.serivce;

import com.baomidou.mybatisplus.service.IService;
import com.cnbaosi.cabinet.entity.criteria.DeptCriteria;
import com.cnbaosi.cabinet.entity.modal.Dept;
import com.cnbaosi.cabinet.entity.modal.vo.TreeNodes;

import java.util.List;

public interface DeptService  extends IService<Dept> {

    /**
     * 新增部门
     * @param dept
     * @return
     */
    Boolean addDept( Dept dept);

    /**
     * 删除部门
     * @param id
     * @return
     */
    String deleteDept(String id);

    /**
     * 修改部门
     * @param dept
     * @return
     */
    Boolean updateDept( Dept dept);

    /**
     * 获取部门列表
     * @param deptCriteria
     * @return
     */
    List<Dept> getDept(DeptCriteria deptCriteria);

    /**
     * 获取部门树
     * @return
     */
    List<TreeNodes> getUserDeptTree();
    /**
     * 获取部门子节点
     * @return
     */
    List<TreeNodes> getChildrenNode(String id, List<Dept> source);

    List<Dept> getAllSonDept(String id);
}
