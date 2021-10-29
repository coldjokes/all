package com.cnbaosi.cabinet.entity.criteria;

/**
 * 部门相关条件
 */
public class DeptCriteria {

    private String id; //id
    private String pId; //父id
    private String text; // 部门名称

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
