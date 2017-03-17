package com.fable.outer.dto;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * 菜单Dto
 * 
 * @author liuz
 * 
 */
public class MenuInfoDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3488606249983127666L;
	/** 菜单ID */
	private Long id;
	/** 菜单名 */
	@JsonProperty("text")
	private String menuName;
	/** 菜单访问的路径 */
	private String url;
	/** 菜单图标 */
	@JsonProperty("iconCls")
	private String iconUrl;
	/** 子菜单 */
	@JsonProperty("children")
	private List<MenuInfoDto> childMenus;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public List<MenuInfoDto> getChildMenus() {
		return childMenus;
	}

	public void setChildMenus(List<MenuInfoDto> childMenus) {
		this.childMenus = childMenus;
	}

}
