package com.cnbaosi.cabinet.entity.modal;

public class DeptMap extends BaseModel<DeptMap> {

    private static final long serialVersionUID = 1L;

    private String deptId; //物料类别id
    private String userId; //物料id

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

