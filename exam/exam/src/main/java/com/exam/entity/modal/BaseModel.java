package com.exam.entity.modal;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;

/**
 *  Entity扩展类，继承mybatisPlus的model
 * 
 * @author Yifeng Wang  
 */
public abstract class BaseModel<T> extends Model<BaseModel<T>> {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.UUID)
    protected String id; //id

	private Date createTime; //创建时间

    public BaseModel() {}

    public BaseModel(String id) {
        this();
        this.id = id;
    }
    
    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
    protected Serializable pkVal() {
        return this.id;
    }
}
