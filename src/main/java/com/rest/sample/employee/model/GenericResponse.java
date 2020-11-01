package com.rest.sample.employee.model;

import java.io.Serializable;
import java.util.List;

public class GenericResponse  implements Serializable{
	
	public enum STATUS {
		SUCCESS,
		FAILED
	}
	
	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public List<String> getMessages() {
		return messages;
	}

	public void setMessages(List<String> messages) {
		this.messages = messages;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int statusCode;
	
	private List<String> messages;
	private STATUS status;
	
	
	public GenericResponse(int statusCode, String status, List<String> messages) {
		 this.statusCode = statusCode;
		 this.messages = messages;
		 
		 if ("SUCCESS".equals(status)) {
			 this.status=STATUS.SUCCESS;
		 }else if ("FAILED".equals(status)) {
			 this.status=STATUS.FAILED;
		 }
		 
	}
	
	@Override
	public String toString() {
		String str="statusCode: " +statusCode+",  messages: "+messages+" status: "+status;
		return str;
		
	}

	public STATUS getStatus() {
		return status;
	}

	public void setStatus(STATUS status) {
		this.status = status;
	}
	

}
