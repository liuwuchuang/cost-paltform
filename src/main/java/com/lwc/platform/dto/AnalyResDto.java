package com.lwc.platform.dto;

public class AnalyResDto {

	private String name;
	
	private Double value;

	public AnalyResDto() {
		super();
	}

	public AnalyResDto(String name, Double value) {
		super();
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}
}
