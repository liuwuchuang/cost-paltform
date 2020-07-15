package com.lwc.platform.dto;

public class RecordResDto {

	private String id;
	
	private String createTime;
	
	private Double money;
	
	private String dictName;
	
	private String typeName;
	
	private String comments;

	public RecordResDto() {
		super();
	}

	public RecordResDto(String id, String createTime, Double money, String dictName, String typeName,String comments) {
		super();
		this.id = id;
		this.createTime = createTime;
		this.money = money;
		this.dictName = dictName;
		this.typeName=typeName;
		this.comments = comments;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public String getDictName() {
		return dictName;
	}

	public void setDictName(String dictName) {
		this.dictName = dictName;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
}
