package com.rest.sample.employee.model;

import java.util.Date;

public class ValidationResponse {
	
	private String field;
	private String error;
	private int statusCode;
	private Date timestamp;
	
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}

	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int i) {
		this.statusCode = i;
	} 

}
