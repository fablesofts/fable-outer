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
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.security.core.GrantedAuthority;

import com.fable.dsp.core.entity.AuditEntity;

/**
 * 角色
 * 
 * @author 汪朝 20140319
 * 
 */
@Entity
@Table(name = "SYS_ROLE_INFO")
public class RoleInfo extends AuditEntity implements GrantedAuthority {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4632432968375427624L;

	@TableGenerator(name = "sysRoleGen", table = "SYS_ID_GEN", pkColumnName = "GEN_KEY", valueColumnName = "GEN_VALUE", pkColumnValue = "SYS_ROLE_ID", allocationSize = 1, initialValue = 20)
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "sysRoleGen")
	private Long id;

	/** 角色名 */
	@Column(name = "ROLE_NAME", nullable = false, unique = true, length = 32)
	private String roleName;

	/** 删除标志,1表示删除,0表示不删除 */
	@Column(name = "DEL_FLAG", length = 1)
	protected char delFlag = '0';

	/** 描述 */
	@Column(name = "DESCRIPTION", length = 512)
	protected String description;

	@ManyToMany(mappedBy = "roles", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	@Fetch(FetchMode.SUBSELECT)
	private List<UserInfo> users;

	/** 关联菜单列表 */
	@ManyToMany(cascade = CascadeType.MERGE)
	@LazyCollection(LazyCollectionOption.FALSE)
	@Fetch(FetchMode.SUBSELECT)
	@JoinTable(name = "SYS_ROLE_MENU", joinColumns = { @JoinColumn(name = "ROLE_ID") }, inverseJoinColumns = { @JoinColumn(name = "MENU_ID") })
	private List<MenuInfo> menus;

	/** 添加一个属性，用于在角色列表中显示角色拥有的所有菜单的名字 */
	@Transient
	private String menuNames;

	/******************************************** GETTER/SETTER *************************************************/
	/** 获取角色拥有的所有的菜单名称 */
	public String getMenuNames() {
		if (menus == null || menus.size() == 0)
			return "";
		// 遍历关联的菜单列表，把菜单名称连接为一个字符串
		StringBuffer names = new StringBuffer("");
		for (MenuInfo menu : menus) {
			names.append("," + menu.getMenuName());
		}
		return names.replace(0, 1, "").toString();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the roleName
	 */
	public String getRoleName() {
		return roleName;
	}

	/**
	 * @param roleName
	 *            the roleName to set
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/** 用户 */
	@JsonIgnore
	public List<UserInfo> getUsers() {
		return users;
	}

	public void setUsers(List<UserInfo> users) {
		this.users = users;
	}

	/**
	 * @return the menus
	 */
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

	/** 关键的方法，在decide时使用 */
	@Override
	public String getAuthority() {
		return roleName;
	}
}
