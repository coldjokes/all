package com.cnbaosi.cabinet.entity.modal;

/**
 *  物料提醒
 * 
 * @author Yifeng Wang  
 */
public class MaterialRemind extends BaseModel<MaterialRemind> {
	private static final long serialVersionUID = 1L;

	private String materialId; //物料id
	private String name; //物料名称
	private String no; //物料编号
	private String spec; //物料规格
	private String remark; //备注
	
	private String createdUserFullname; //创建人

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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCreatedUserFullname() {
		return createdUserFullname;
	}

	public void setCreatedUserFullname(String createdUserFullname) {
		this.createdUserFullname = createdUserFullname;
	}

	public String getMaterialId() {
		return materialId;
	}

	public void setMaterialId(String materialId) {
		this.materialId = materialId;
	}
}


