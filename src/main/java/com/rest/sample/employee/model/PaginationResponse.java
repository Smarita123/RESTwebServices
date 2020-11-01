package com.rest.sample.employee.model;

import java.util.ArrayList;

public class PaginationResponse {
	
	ArrayList<User> userList;
	int totalRecords;
	String message;
	
	public ArrayList<User> getUserList() {
		return userList;
	}
	public void setUserList(ArrayList<User> userList) {
		this.userList = userList;
	}
	public int getTotalRecords() {
		return totalRecords;
	}
	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	

}
