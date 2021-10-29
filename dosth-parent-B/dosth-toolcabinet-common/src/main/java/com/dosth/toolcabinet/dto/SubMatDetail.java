package com.dosth.toolcabinet.dto;

import java.io.Serializable;
import java.util.Date;

import com.dosth.comm.LockPara;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 
 * @description 暂存柜封装对象
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
public class SubMatDetail implements Serializable {
	// 刀具柜序列号
	private String serialNo;
	// 物料Id
	private String matId;
	// 物料名称
	private String matName;
	// 包装数量(单包装数量)
	private Integer packNum;
	// 借出数量(待归还流水数量统计)
	private Integer borrowNum;
	// 剩余数量(副柜现存数量)
	private Integer remainNum;
	// 物料图片
	private String pic;
	// 借出类型编码
	private String borrowTypeCode;
	// 借出类型名称
	private String borrowTypeName;
	// 以旧换新
	private String oldForNew;
	// 物料借出Id
	private String matUseBillId;
	// 副柜盒子Id
	private String subBoxId;
	// 借出时间
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date borrowTime;
	// 编号
	private String barCode;
	// 规格型号
	private String spec;
	// 领用类型
	private String receiveType;
	// 领用类型明细
	private String receiveInfo;
	// 柜子Id
    private String subCabinetId; 
    // 柜子名称
    private String cabinetName;
	// 锁控板通讯对象
	private LockPara lockPara;
    // 生命周期
    private Integer useLife;
	
	public SubMatDetail() {
	}

	public SubMatDetail(String matId, String matName) {
		this.matId = matId;
		this.matName = matName;
	}

	public SubMatDetail(String matUseBillId, String matId, String matName, String barCode, String spec) {
		this.matUseBillId = matUseBillId;
		this.matId = matId;
		this.matName = matName;
		this.barCode = barCode;
		this.spec = spec;
	}

	/**
	 * @description 使用历史封装
	 * @param matUseBillId   借出流水Id
	 * @param matId          物料Id
	 * @param matName        物料名称
	 * @param packNum        包装数量
	 * @param borrowNum      借出数量
	 * @param borrowTypeName 借出类型名称
	 * @param oldForNew      以旧换新
	 * @param borrowTime     借出时间
	 * @param barCode        编号
	 * @param spec           型号
	 * @param pic
	 * @param receiveType    领用类型
	 * @param receiveInfo    领用类型明细
     * @param useLife 生命周期
	 */
	public SubMatDetail(String serialNo, String matId, String matName, Integer packNum, Integer borrowNum, String pic,
			String borrowTypeName, String oldForNew, String matUseBillId, Date borrowTime, String barCode, String spec,
			String receiveType, String receiveInfo, Integer useLife) {
		super();
		this.serialNo = serialNo;
		this.matId = matId;
		this.matName = matName;
		this.packNum = packNum;
		this.borrowNum = borrowNum;
		this.pic = pic;
		this.borrowTypeName = borrowTypeName;
		this.oldForNew = oldForNew;
		this.matUseBillId = matUseBillId;
		this.borrowTime = borrowTime;
		this.barCode = barCode;
		this.spec = spec;
		this.receiveType = receiveType;
		this.receiveInfo = receiveInfo;
		this.useLife = useLife;
	}

	/**
	 * @description 副柜详情封装
	 * @param subBoxId       副柜盒子Id
	 * @param matId          物料Id
	 * @param matName        物料名称
	 * @param barCode        编号
	 * @param spec           规格型号
	 * @param packNum        包装数量
	 * @param borrowNum      借出数量
	 * @param remainNum      剩余数量
	 * @param borrowTypeCode 借出类型编码
	 * @param borrowTypeName 借出类型名称
	 * @param oldForNew      以旧换新
	 * @param pic            图片
	 * @param borrowTime     暂存时间
	 */
	public SubMatDetail(String subBoxId, String matId, String matName, String barCode, String spec, Integer packNum,
			Integer borrowNum, Integer remainNum, String borrowTypeCode, String borrowTypeName, String oldForNew,
			String pic, Date borrowTime) {
		this.subBoxId = subBoxId;
		this.matId = matId;
		this.matName = matName;
		this.barCode = barCode;
		this.spec = spec;
		this.packNum = packNum;
		this.borrowNum = borrowNum;
		this.remainNum = remainNum;
		this.borrowTypeCode = borrowTypeCode;
		this.borrowTypeName = borrowTypeName;
		this.oldForNew = oldForNew;
		this.pic = pic;
		this.borrowTime = borrowTime;
	}
	
	/**
	 * @description 已暂存物料详情
	 * @param subBoxId       副柜盒子Id
	 * @param matId          物料Id
	 * @param matName        物料名称
	 * @param barCode        编号
	 * @param spec           规格型号
	 * @param packNum        包装数量
	 * @param borrowNum      借出数量
	 * @param remainNum      剩余数量
	 * @param borrowTypeCode 借出类型编码
	 * @param borrowTypeName 借出类型名称
	 * @param oldForNew      以旧换新
	 * @param pic            图片
	 * @param borrowTime     暂存时间
	 * @param subCabinetId   副柜Id
	 * @param cabinetName    柜体名称
	 * @param lockPara		 锁控板对象	
	 */
	public SubMatDetail(String subBoxId, String matId, String matName, String barCode, String spec, Integer packNum,
			Integer remainNum, String borrowTypeCode, String borrowTypeName, String oldForNew,
			String pic, Date borrowTime, String subCabinetId, String cabinetName, LockPara lockPara) {
		this.subBoxId = subBoxId;
		this.matId = matId;
		this.matName = matName;
		this.barCode = barCode;
		this.spec = spec;
		this.packNum = packNum;
		this.remainNum = remainNum;
		this.borrowTypeCode = borrowTypeCode;
		this.borrowTypeName = borrowTypeName;
		this.oldForNew = oldForNew;
		this.pic = pic;
		this.borrowTime = borrowTime;
		this.subCabinetId = subCabinetId;
		this.cabinetName = cabinetName;
		this.lockPara = lockPara;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getMatId() {
		return this.matId;
	}

	public void setMatId(String matId) {
		this.matId = matId;
	}

	public String getMatName() {
		return this.matName;
	}

	public void setMatName(String matName) {
		this.matName = matName;
	}

	public Integer getPackNum() {
		return this.packNum;
	}

	public void setPackNum(Integer packNum) {
		this.packNum = packNum;
	}

	public Integer getRemainNum() {
		if (this.remainNum == null) {
			this.remainNum = 0;
		}
		return this.remainNum;
	}

	public void setRemainNum(Integer remainNum) {
		this.remainNum = remainNum;
	}

	public Integer getBorrowNum() {
		if (this.borrowNum == null) {
			return 0;
		}
		return this.borrowNum;
	}

	public void setBorrowNum(Integer borrowNum) {
		this.borrowNum = borrowNum;
	}

	public String getPic() {
		return this.pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getBorrowTypeCode() {
		return this.borrowTypeCode;
	}

	public void setBorrowTypeCode(String borrowTypeCode) {
		this.borrowTypeCode = borrowTypeCode;
	}

	public String getBorrowTypeName() {
		return this.borrowTypeName;
	}

	public void setBorrowTypeName(String borrowTypeName) {
		this.borrowTypeName = borrowTypeName;
	}

	public String getOldForNew() {
		return this.oldForNew;
	}

	public void setOldForNew(String oldForNew) {
		this.oldForNew = oldForNew;
	}

	public String getMatUseBillId() {
		return this.matUseBillId;
	}

	public void setMatUseBillId(String matUseBillId) {
		this.matUseBillId = matUseBillId;
	}

	public String getSubBoxId() {
		return this.subBoxId;
	}

	public void setSubBoxId(String subBoxId) {
		this.subBoxId = subBoxId;
	}

	public LockPara getLockPara() {
		if(this.lockPara == null) {
            this.lockPara = new LockPara();
        }
        return this.lockPara;
	}

	public void setLockPara(LockPara lockPara) {
		this.lockPara = lockPara;
	}

	public Date getBorrowTime() {
		return this.borrowTime;
	}

	public void setBorrowTime(Date borrowTime) {
		this.borrowTime = borrowTime;
	}

	public String getBarCode() {
		return this.barCode;
	}

	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}

	public String getSpec() {
		return this.spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getReceiveType() {
		return receiveType;
	}

	public void setReceiveType(String receiveType) {
		this.receiveType = receiveType;
	}

	public String getReceiveInfo() {
		return receiveInfo;
	}

	public void setReceiveInfo(String receiveInfo) {
		this.receiveInfo = receiveInfo;
	}

	public String getSubCabinetId() {
		return subCabinetId;
	}

	public void setSubCabinetId(String subCabinetId) {
		this.subCabinetId = subCabinetId;
	}

	public String getCabinetName() {
		return cabinetName;
	}

	public void setCabinetName(String cabinetName) {
		this.cabinetName = cabinetName;
	}
	 
    public Integer getUseLife() {
        return this.useLife;
    }
 
    public void setUseLife(Integer useLife) {
        this.useLife = useLife;
    }
 
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((matId == null) ? 0 : matId.hashCode());
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
		SubMatDetail other = (SubMatDetail) obj;
		if (matId == null) {
			if (other.matId != null)
				return false;
		} else if (!matId.equals(other.matId))
			return false;
		return true;
	}
}