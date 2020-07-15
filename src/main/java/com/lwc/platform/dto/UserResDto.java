package com.lwc.platform.dto;

public class UserResDto {

	private String id;
	
	private String username;
	
	private String password;
	
	private String role;
	
	private String createTime;

	public UserResDto() {
		super();
	}

	public UserResDto(String id, String username, String password, String role, String createTime) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.role = role;
		this.createTime = createTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
}
