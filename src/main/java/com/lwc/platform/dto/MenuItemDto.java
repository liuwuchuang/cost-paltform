package com.lwc.platform.dto;

public class MenuItemDto {

	private String menuName;
	
	private String menuUrl;

	public MenuItemDto() {
		super();
	}

	public MenuItemDto(String menuName, String menuUrl) {
		super();
		this.menuName = menuName;
		this.menuUrl = menuUrl;
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
}
