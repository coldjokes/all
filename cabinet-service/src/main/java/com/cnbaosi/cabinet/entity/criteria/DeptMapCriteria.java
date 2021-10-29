package com.cnbaosi.cabinet.entity.criteria;

import java.util.List;

public class DeptMapCriteria {
    private String userId;//用户id
    private String deptId;//部门id
    private List<String> userIdList; //用户id list
    private List<String> deptIdList; //部门id list

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public List<String> getUserIdList() {
        return userIdList;
    }

    public void setUserIdList(List<String> userIdList) {
        this.userIdList = userIdList;
    }

    public List<String> getDeptIdList() {
        return deptIdList;
    }

    public void setDeptIdList(List<String> deptIdList) {
        this.deptIdList = deptIdList;
    }
}
