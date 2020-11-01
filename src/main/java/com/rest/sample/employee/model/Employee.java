package com.rest.sample.employee.model;
import java.io.IOException;
import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.Length;

import com.rest.sample.employee.model.Address;

import io.swagger.annotations.ApiModelProperty;


@XmlRootElement
public class Employee {
	
	@ApiModelProperty (notes="Employee id of the employee")
	@NotNull(message = "Value cannot be blank")
	private int empId;
	@NotNull(message = "Value cannot be blank")
	//@Length(max=10, message = "Invalid name, with max length of 10 chars")
	@Pattern(regexp = "^[a-zA-Z]+{10}", message = "Invalid name, with max length of 10 chars")
	@ApiModelProperty (notes="Name of the employee")
	private String name;
	@ApiModelProperty (notes="Employee Address")
	@Valid
	private Address address;
	@ApiModelProperty (notes="Employee role")
	private String role;
	
	//@Length(min = 9, max=12, message="mobilenumber must be at least 9 digits long with max-length 12 digits")
	@Pattern(regexp = "^[0-9]{9,12}", message= "Invalid mobile number, mobilenumber must be at least 9 digits long with max-length 12 digits")
	private String mobilenumber;

	public String getMobilenumber() {
		return mobilenumber;
	}

	public void setMobilenumber(String mobilenumber) {
		this.mobilenumber = mobilenumber;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	private Address homeaddress;
	private Address officeaddress;

	public Employee() {
		 //TODO Auto-generated constructor stub
	}
		
	public String getName() {
		return name;		
	}

	public void setName(String name) {
	this.name=name;
	
	}
	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
	}

	public Address getHomeaddress() {
		return homeaddress;
	}

	public void setHomeaddress(Address homeaddress) {
		this.homeaddress = homeaddress;
	}

	public Address getOfficeaddress() {
		return officeaddress;
	}

	public void setOfficeaddress(Address officeaddress) {
		this.officeaddress = officeaddress;
	}

}
