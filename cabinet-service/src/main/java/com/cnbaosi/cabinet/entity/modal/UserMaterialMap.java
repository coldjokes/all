package com.cnbaosi.cabinet.entity.modal;

import java.util.Date;

public class UserMaterialMap extends BaseModel<UserMaterialMap> {
    private String selectId;                         // key: userId/deptId
    private String value;                       // value: materialId/categoryId
    private String type;                        // type: '0': dept,'1': user
    private Date updateTime;
    private Date deleteTime;

    public String getSelectId() {
        return selectId;
    }

    public void setSelectId(String selectId) {
        this.selectId = selectId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
    }
}
