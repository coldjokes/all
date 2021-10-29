package com.cnbaosi.cabinet.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cnbaosi.cabinet.entity.modal.Dept;
import com.cnbaosi.cabinet.entity.modal.Jurisdiction;
import com.cnbaosi.cabinet.entity.modal.MaterialCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface JurisdictionMapper extends BaseMapper<Jurisdiction> {
    List<Dept> getMaterials(List<MaterialCategory> categories);

    List<Dept> getUsers(List<Dept> depts);

    // 判断用户是否有取料权限
    List<String> getRoles(List<String> ids);

    // 获取用户上级部门ID
    String getPids(@Param(value="userId")String userId);

    // 获取部门上级部门ID
    String getDeptPId(@Param(value="deptId")String deptId);
}
