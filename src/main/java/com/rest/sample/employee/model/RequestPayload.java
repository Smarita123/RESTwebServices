package com.rest.sample.employee.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class RequestPayload {
	
	@NotBlank @NotNull (message="Cannot be blank/null")
	private String empName;
	@NotBlank @NotNull (message="Cannot be blank/null")
	private String  id;
	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
