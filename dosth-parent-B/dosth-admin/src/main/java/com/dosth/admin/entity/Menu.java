package com.dosth.admin.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.dosth.common.db.entity.BasePojo;

/**
 * 菜单表
 * 
 * @author guozhidong
 *
 */
@SuppressWarnings("serial")
@Entity
public class Menu extends BasePojo {

	@Column(name = "system_info_id", columnDefinition = "varchar(36) COMMENT '系统编号'")
	private String systemInfoId;
	@ManyToOne
	@JoinColumn(name = "system_info_id", insertable = false, updatable = false)
	private SystemInfo systemInfo;

	@Column(name = "code", columnDefinition = "varchar(50) COMMENT '菜单编号'")
	private String code;
	@ManyToOne
	@JoinColumn(name = "pcode", referencedColumnName = "code", insertable = false, updatable = false)
	private Menu pMenu;

	@Column(name = "pcode", columnDefinition = "varchar(50) COMMENT '菜单父编号'")
	private String pcode;
	@OneToMany(mappedBy = "pMenu", cascade = CascadeType.ALL)
	private List<Menu> childList;

	@Column(columnDefinition = "varchar(255) COMMENT '当前菜单的所有父菜单编号'")
	private String pcodes;

	@Column(columnDefinition = "varchar(50) COMMENT '菜单名称'")
	private String name;

	@Column(columnDefinition = "varchar(50) COMMENT '菜单图标'")
	private String icon;

	@Column(columnDefinition = "varchar(150) COMMENT 'URL地址'")
	private String url;

	@Column(columnDefinition = "int(11) COMMENT '排序'")
	private Integer num;

	@Column(columnDefinition = "int(11) COMMENT '菜单层级'")
	private Integer levels;

	@Column(columnDefinition = "bit COMMENT '是否是菜单'")
	private Boolean isMenu;

	@Column(columnDefinition = "varchar(255) COMMENT '备注'")
	private String tips;

	@Column(columnDefinition = "bit COMMENT '菜单状态 : 1.启用， 0.不启用'")
	private Boolean isUse;

	@Column(columnDefinition = "bit COMMENT '是否打开: 1.打开， 0.不打开'")
	private Boolean isOpen;

	@Transient
	private String systemUrl;

	public String getSystemInfoId() {
		return this.systemInfoId;
	}

	public void setSystemInfoId(String systemInfoId) {
		this.systemInfoId = systemInfoId;
	}

	public SystemInfo getSystemInfo() {
		return this.systemInfo;
	}

	public void setSystemInfo(SystemInfo systemInfo) {
		this.systemInfo = systemInfo;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getPcode() {
		return this.pcode;
	}

	public void setPcode(String pcode) {
		this.pcode = pcode;
	}

	public Menu getpMenu() {
		return this.pMenu;
	}

	public void setpMenu(Menu pMenu) {
		this.pMenu = pMenu;
	}

	public List<Menu> getChildList() {
		if (this.childList == null) {
			this.childList = new ArrayList<>();
		}
		return this.childList;
	}

	public void setChildList(List<Menu> childList) {
		this.childList = childList;
	}

	public String getPcodes() {
		return this.pcodes;
	}

	public void setPcodes(String pcodes) {
		this.pcodes = pcodes;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return this.icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Integer getNum() {
		return this.num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Integer getLevels() {
		return this.levels;
	}

	public void setLevels(Integer levels) {
		this.levels = levels;
	}

	public Boolean getIsMenu() {
		if (this.isMenu == null) {
			this.isMenu = true;
		}
		return this.isMenu;
	}

	public void setIsMenu(Boolean isMenu) {
		this.isMenu = isMenu;
	}

	public String getTips() {
		return this.tips;
	}

	public void setTips(String tips) {
		this.tips = tips;
	}

	public Boolean getIsUse() {
		if (this.isUse == null) {
			this.isUse = true;
		}
		return this.isUse;
	}

	public void setIsUse(Boolean isUse) {
		this.isUse = isUse;
	}

	public Boolean getIsOpen() {
		if (this.isOpen == null) {
			this.isOpen = false;
		}
		return this.isOpen;
	}

	public void setIsOpen(Boolean isOpen) {
		this.isOpen = isOpen;
	}

	public String getSystemUrl() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		String ip = "http://" + request.getServerName() + ":";
		String port = this.systemInfo.getUrl();
		this.systemUrl = new StringBuilder(ip + port).append(this.url).toString();
		return this.systemUrl;
	}

	public void setSystemUrl(String systemUrl) {
		this.systemUrl = systemUrl;
	}

	@Override
	public String toString() {
		return "Menu{" + "id=" + id + ", code=" + code + ", pcode=" + "pcode" + ", pcodes=" + pcodes + ", name=" + name
				+ ", icon=" + icon + ", url=" + url + ", num=" + num + ", levels=" + levels + ", isMenu=" + isMenu
				+ ", tips=" + tips + ", isUse=" + isUse + ", isOpen=" + isOpen + "}";
	}
}