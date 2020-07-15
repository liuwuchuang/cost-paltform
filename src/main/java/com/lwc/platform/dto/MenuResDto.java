package com.lwc.platform.dto;

import java.util.List;

public class MenuResDto {
	
	private String menuName;
	
	private String className;//样式

	private List<MenuItemDto> itemList;

	public MenuResDto() {
		super();
	}

	public MenuResDto(String menuName, String className) {
		super();
		this.menuName = menuName;
		this.className = className;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public List<MenuItemDto> getItemList() {
		return itemList;
	}

	public void setItemList(List<MenuItemDto> itemList) {
		this.itemList = itemList;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
}
