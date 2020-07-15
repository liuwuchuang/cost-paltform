package com.lwc.platform.dto;

public class AnalyDbDto {
	
	private String id;
	
	private String name;
	
	private Double sum;
	
	public AnalyDbDto() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Double getSum() {
		return sum;
	}

	public void setSum(Double sum) {
		this.sum = sum;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
