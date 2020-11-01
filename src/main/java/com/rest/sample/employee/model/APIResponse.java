package com.rest.sample.employee.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)

@JsonInclude(Include.NON_NULL)
public class APIResponse {
	
	private String statusCode;
	private String successMessage;
	private String failureMessage;
	private Date timestamp;

	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public String getSuccessMessage() {
		return successMessage;
	}
	public void setSucessMessage(String sucessMessage) {
		this.successMessage = sucessMessage;
	}
	public String getFailureMessage() {
		return failureMessage;
	}
	public void setFailureMessage(String failureMessage) {
		this.failureMessage = failureMessage;
	}
}
