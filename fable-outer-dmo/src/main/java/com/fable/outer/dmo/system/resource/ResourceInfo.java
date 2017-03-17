package com.fable.outer.dmo.system.resource;


import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fable.dsp.core.entity.AuditEntity;

/**
 * 资源
 * 
 * @author 汪朝 20140319
 * 
 */
@Entity
@Table(name = "SYS_RESOURCE_INFO")
public class ResourceInfo extends AuditEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8233931655749104967L;

	@TableGenerator(name = "sysResourceGen", table = "SYS_ID_GEN", pkColumnName = "GEN_KEY", valueColumnName = "GEN_VALUE", pkColumnValue = "SYS_RESOURCE_ID", allocationSize = 1, initialValue = 31)
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "sysResourceGen")
	private Long id;
	/** 资源名 */
	@Column(name = "RES_NAME", length = 32)
	private String resourceName;
	/** 资源url映射 */
	@Column(name = "URL", unique = true)
	private String url;
	/** 资源类型 */
	@Column(name = "RES_TYPE", length = 2)
	private String type;
	/** 是否生效果 */
	@Column(name = "ENABLED", length = 1)
	private String enabled;

	/** 删除标志,1表示删除,0表示不删除 */
	@Column(name = "DEL_FLAG", length = 1)
	protected char delFlag = '0';

	/** 描述 */
	@Column(name = "DESCRIPTION", length = 512)
	protected String description;

	/** 关联菜单列表 */
	@ManyToMany(mappedBy = "resources", fetch = FetchType.LAZY)
	@Fetch(FetchMode.SUBSELECT)
	private List<MenuInfo> menus;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	public char getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(char delFlag) {
		this.delFlag = delFlag;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the menus
	 */
	@JsonIgnore
	public List<MenuInfo> getMenus() {
		return menus;
	}

	/**
	 * @param menus
	 *            the menus to set
	 */
	public void setMenus(List<MenuInfo> menus) {
		this.menus = menus;
	}

}
