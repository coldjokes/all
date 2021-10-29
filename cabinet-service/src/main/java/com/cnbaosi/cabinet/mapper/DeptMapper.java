package com.cnbaosi.cabinet.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.cnbaosi.cabinet.entity.modal.Dept;
import com.cnbaosi.cabinet.entity.modal.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface DeptMapper extends BaseMapper<Dept> {

    boolean setDeptId(User user);

    List<String> selectByUserId(@Param("userId")String userId);

    // 判断部门是否有上级或下级部门
    List<String> selectByDeptId(@Param("deptId")String deptId);

}