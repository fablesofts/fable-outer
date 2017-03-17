package com.fable.outer.dmo.system.resource;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.fable.dsp.core.entity.AuditEntity;

/**
 * 系统菜单
 * 
 * @author 汪朝 20140321
 */
@Entity
@Table(name = "SYS_MENU_INFO")
public class MenuInfo extends AuditEntity {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 2842961839942962726L;

	@TableGenerator(name = "sysMenuGen", table = "SYS_ID_GEN", pkColumnName = "GEN_KEY", valueColumnName = "GEN_VALUE", pkColumnValue = "SYS_MENU_ID", allocationSize = 1, initialValue = 50)
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "sysMenuGen")
	private Long id;

	/**
	 * 菜单名称 注解@JsonProperty("text")把json字符串中的属性名menuName改为text
	 */
	@Column(name = "MENU_NAME", length = 32, unique = true)
	@JsonProperty("text")
	private String menuName;

	/** 菜单访问的路径-URL */
	@Column(name = "URL", length = 128)
	private String url;

	/** 菜单对应图标的访问的路径-URL */
	@Column(name = "ICON_URL", length = 128)
	@JsonProperty("iconCls")
	private String iconUrl;

	/** 排位 ，默认值为1 */
	@Column(name = "SORT_ORDER", nullable = false)
	protected Integer sortOrder = 1;

	/** 删除标志,1表示删除,0表示不删除 */
	@Column(name = "DEL_FLAG", length = 1)
	protected char delFlag = '0';

	/** 描述 */
	@Column(name = "DESCRIPTION", length = 512)
	protected String description;

	/** 菜单级别 */
	@Column(name = "MENU_LEVEL", nullable = false)
	private Integer menuLevel;

	/** 上一级菜单 */
	@ManyToOne(fetch = FetchType.LAZY)
	@LazyCollection(LazyCollectionOption.FALSE)
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "PID")
	private MenuInfo parentMenu;

	/**
	 * 子菜单列表 注解@JsonProperty("children")把json字符串中的属性名childMenus改为children
	 */
	@OneToMany(cascade = { CascadeType.ALL }, mappedBy = "parentMenu", fetch = FetchType.LAZY)
	@LazyCollection(LazyCollectionOption.FALSE)
	@Fetch(FetchMode.SUBSELECT)
	@OrderBy("sortOrder ASC")
	@JsonProperty("children")
	private List<MenuInfo> childMenus;

	/** 菜单所对应的资源列表 */
	@ManyToMany
	@LazyCollection(LazyCollectionOption.FALSE)
	@Fetch(FetchMode.SUBSELECT)
	@JoinTable(name = "SYS_MENU_RES", joinColumns = { @JoinColumn(name = "MENU_ID") }, inverseJoinColumns = { @JoinColumn(name = "RESOURCE_ID") })
	private List<ResourceInfo> resources;

	/** 菜单所对应的权限列表 */
	@ManyToMany(mappedBy = "menus", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@Fetch(FetchMode.SUBSELECT)
	private List<RoleInfo> roles;

	/******************************************** GETTER/SETTER *************************************************/

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the menuName
	 */
	public String getMenuName() {
		return menuName;
	}

	/**
	 * @param menuName
	 *            the menuName to set
	 */
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the iconUrl
	 */
	public String getIconUrl() {
		return iconUrl;
	}

	/**
	 * @param iconUrl
	 *            the iconUrl to set
	 */
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	/**
	 * @return the sortOrder
	 */
	public Integer getSortOrder() {
		return sortOrder;
	}

	/**
	 * @param sortOrder
	 *            the sortOrder to set
	 */
	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	/**
	 * @return the delFlag
	 */
	public char getDelFlag() {
		return delFlag;
	}

	/**
	 * @param delFlag
	 *            the delFlag to set
	 */
	public void setDelFlag(char delFlag) {
		this.delFlag = delFlag;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the parentMenu
	 */
	@JsonIgnore
	public MenuInfo getParentMenu() {
		return parentMenu;
	}

	/**
	 * @param parentMenu
	 *            the parentMenu to set
	 */
	public void setParentMenu(MenuInfo parentMenu) {
		this.parentMenu = parentMenu;
	}

	/**
	 * @return the childMenus
	 */
	public List<MenuInfo> getChildMenus() {
		return childMenus;
	}

	/**
	 * @param childMenus
	 *            the childMenus to set
	 */
	public void setChildMenus(List<MenuInfo> childMenus) {
		this.childMenus = childMenus;
	}

	/**
	 * @return the resources
	 */
	public List<ResourceInfo> getResources() {
		return resources;
	}

	/**
	 * @param resources
	 *            the resources to set
	 */
	public void setResources(List<ResourceInfo> resources) {
		this.resources = resources;
	}

	/**
	 * @return the roles
	 */
	@JsonIgnore
	public List<RoleInfo> getRoles() {
		return roles;
	}

	/**
	 * @param roles
	 *            the roles to set
	 */
	public void setRoles(List<RoleInfo> roles) {
		this.roles = roles;
	}

	public Integer getMenuLevel() {
		return menuLevel;
	}

	public void setMenuLevel(Integer menuLevel) {
		this.menuLevel = menuLevel;
	}

}