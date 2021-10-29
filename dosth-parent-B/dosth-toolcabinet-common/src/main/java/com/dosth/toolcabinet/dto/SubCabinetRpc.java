package com.dosth.toolcabinet.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.dosth.comm.LockPara;

/**
 * 
 * @description 副柜远程封装对象
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
public class SubCabinetRpc implements Serializable {
	private String subCabinetId; // 柜子Id
	private String cabinetName; // 柜子名称
	private LockPara lockPara; // 锁控板通讯封装对象
	
	private List<SubMatDetail> detailList; // 暂存物料信息集合	

	public SubCabinetRpc() {
	}

	public SubCabinetRpc(String subCabinetId, String cabinetName) {
		this.subCabinetId = subCabinetId;
		this.cabinetName = cabinetName;
	}

	public SubCabinetRpc(String subCabinetId, String cabinetName, LockPara lockPara) {
		this.subCabinetId = subCabinetId;
		this.cabinetName = cabinetName;
		this.lockPara = lockPara;
	}

	public String getSubCabinetId() {
		return this.subCabinetId;
	}

	public void setSubCabinetId(String subCabinetId) {
		this.subCabinetId = subCabinetId;
	}

	public String getCabinetName() {
		return this.cabinetName;
	}

	public void setCabinetName(String cabinetName) {
		this.cabinetName = cabinetName;
	}

	public LockPara getLockPara() {
		return this.lockPara;
	}

	public void setLockPara(LockPara lockPara) {
		this.lockPara = lockPara;
	}

	public List<SubMatDetail> getDetailList() {
		if (this.detailList == null) {
			this.detailList = new ArrayList<>();
		}
		return this.detailList;
	}

	public void setDetailList(List<SubMatDetail> detailList) {
		this.detailList = detailList;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((subCabinetId == null) ? 0 : subCabinetId.hashCode());
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
		SubCabinetRpc other = (SubCabinetRpc) obj;
		if (subCabinetId == null) {
			if (other.subCabinetId != null)
				return false;
		} else if (!subCabinetId.equals(other.subCabinetId))
			return false;
		return true;
	}
}