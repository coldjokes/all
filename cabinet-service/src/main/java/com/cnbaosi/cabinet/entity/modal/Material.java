package com.cnbaosi.cabinet.entity.modal;

import java.util.Date;

/**
 *  物料
 * 
 * @author Yifeng Wang  
 */
public class Material extends BaseModel<Material> {
	private static final long serialVersionUID = 1L;

	private String name; //物料名称
	private String no; //物料编号
	private String spec; //物料规格
	private Integer warnVal; //预警值
	private Integer source; //数据来源
	private String picture; //图片
	private String blueprint; //图纸
	private String remark; //备注
	
	private Date updateTime; //更新时间
	private Date deleteTime; //删除时间
	
	private String lastStockOperateUserId; //最后库存操作人id
	private String lastStockOperateUserFullname; //最后库存操作人姓名
	private Date lastStockOperateTime; //最后库存操作时间
	
	public Material() {
		super();
	}
	public Material(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public Integer getSource() {
		return source;
	}
	public void setSource(Integer source) {
		this.source = source;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public String getBlueprint() {
		return blueprint;
	}
	public void setBlueprint(String blueprint) {
		this.blueprint = blueprint;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	public Integer getWarnVal() {
		return warnVal;
	}
	public void setWarnVal(Integer warnVal) {
		this.warnVal = warnVal;
	}
	public String getLastStockOperateUserId() {
		return lastStockOperateUserId;
	}
	public void setLastStockOperateUserId(String lastStockOperateUserId) {
		this.lastStockOperateUserId = lastStockOperateUserId;
	}
	public String getLastStockOperateUserFullname() {
		return lastStockOperateUserFullname;
	}
	public void setLastStockOperateUserFullname(String lastStockOperateUserFullname) {
		this.lastStockOperateUserFullname = lastStockOperateUserFullname;
	}
	public Date getLastStockOperateTime() {
		return lastStockOperateTime;
	}
	public void setLastStockOperateTime(Date lastStockOperateTime) {
		this.lastStockOperateTime = lastStockOperateTime;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((no == null) ? 0 : no.hashCode());
		result = prime * result + ((spec == null) ? 0 : spec.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Material other = (Material) obj;
		if (no == null) {
			if (other.no != null)
				return false;
		} else if (!no.equals(other.no))
			return false;
		if (spec == null) {
			if (other.spec != null)
				return false;
		} else if (!spec.equals(other.spec))
			return false;
		return true;
	}
}


