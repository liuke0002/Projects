package com.liuke.mobilesafe.bean;

public class BlackNumInfo {
	
	private String number;
	private String mode;
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public BlackNumInfo(String number, String mode) {
		super();
		this.number = number;
		this.mode = mode;
	}
	
}
