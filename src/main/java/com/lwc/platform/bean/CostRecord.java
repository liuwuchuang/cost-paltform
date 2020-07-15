package com.lwc.platform.bean;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "cost_record")
public class CostRecord implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	private String id;
	
	@Field("user_id")
	private String userId;
	
	@Field("create_time")
	private Date createTime;
	
	private Double money;
	
	@Field("dict_id")
	private String dictId;
	
	private Integer type;//-1表示支出，1表示收入
	
	private String comments;

	public CostRecord() {
		super();
	}

	public CostRecord(String userId, Date createTime, Double money, String dictId, Integer type,String comments) {
		super();
		this.userId = userId;
		this.createTime = createTime;
		this.money = money;
		this.dictId = dictId;
		this.type=type;
		this.comments = comments;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public String getDictId() {
		return dictId;
	}

	public void setDictId(String dictId) {
		this.dictId = dictId;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
}
