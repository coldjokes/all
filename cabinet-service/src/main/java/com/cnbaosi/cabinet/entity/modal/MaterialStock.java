package com.cnbaosi.cabinet.entity.modal;

/**
 * 物料库存
 * 
 * @author Yifeng Wang
 */

public class MaterialStock extends BaseModel<MaterialStock> {

	private static final long serialVersionUID = 1L;

	private String materialId; // 物料id
	private Integer amount; // 数量
	private String billId; // 物料出入库流水id
	private String billHookId; //领料记录互相对应id（领-存）
	public MaterialStock() {
		super();
	}

	public MaterialStock(String id) {
		super(id);
	}

	public MaterialStock(String materialId, Integer amount) {
		super();
		this.materialId = materialId;
		this.amount = amount;
	}

	public String getMaterialId() {
		return materialId;
	}

	public void setMaterialId(String materialId) {
		this.materialId = materialId;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public String getBillId() {
		return billId;
	}

	public void setBillId(String billId) {
		this.billId = billId;
	}

	public String getBillHookId() {
		return billHookId;
	}

	public void setBillHookId(String billHookId) {
		this.billHookId = billHookId;
	}
}
