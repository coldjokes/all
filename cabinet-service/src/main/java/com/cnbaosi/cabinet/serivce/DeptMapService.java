package com.cnbaosi.cabinet.serivce;

import com.baomidou.mybatisplus.service.IService;
import com.cnbaosi.cabinet.entity.criteria.DeptMapCriteria;
import com.cnbaosi.cabinet.entity.modal.DeptMap;

import java.util.List;

public interface DeptMapService extends IService<DeptMap> {

    /**
     * 更新部门人员关系
     * @param deptMapCriteria
     * @return
     */
    String updateDeptMap(DeptMapCriteria deptMapCriteria);

    /**
     * 批量更新映射关系
     * @param maps
     * @return
     */
    boolean addDeptMaps(List<DeptMap> maps);

    /**
     * 点击部门获取右侧人员列表
     * @param deptMapCriteria
     * @return
     */
    List<DeptMap> getMapList(DeptMapCriteria deptMapCriteria);

}
