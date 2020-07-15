package com.lwc.platform.bean;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "role_menu")
public class RoleMenu implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	
	@Field("menu_id")
	private Integer menuId;
	
	@Field("parent_id")
	private Integer parentId;
	
	@Field("menu_name")
	private String menuName;
	
	@Field("class_name")
	private String className;
	
	@Field("menu_url")
	private String menuUrl;
	
	@Field("role_id")
	private Integer roleId;

	public RoleMenu() {
		super();
	}

	public RoleMenu(Integer menuId, Integer parentId, String menuName, String menuUrl, Integer roleId) {
		super();
		this.menuId = menuId;
		this.parentId = parentId;
		this.menuName = menuName;
		this.menuUrl = menuUrl;
		this.roleId = roleId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getMenuUrl() {
		return menuUrl;
	}

	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getMenuId() {
		return menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
}
