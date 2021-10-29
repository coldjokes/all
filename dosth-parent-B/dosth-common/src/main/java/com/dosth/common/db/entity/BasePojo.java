package com.dosth.common.db.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

import com.dosth.common.annotion.PageTableTitle;

/**
 * @DESC 实体基础类
 * 
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
@MappedSuperclass
public class BasePojo implements Serializable {
	@Id
	@GenericGenerator(name = "jpa-uuid", strategy = "uuid")
	@GeneratedValue(generator = "jpa-uuid")
	@PageTableTitle(value = "id", isId = true, isVisible = false)
	@Column(name = "id", columnDefinition = "varchar(36) COMMENT 'ID'")
	protected String id;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof BasePojo)) {
			return false;
		}
		BasePojo other = (BasePojo) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}
}