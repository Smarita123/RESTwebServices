package com.rest.sample.employee.entity;


//@Entity
//@Table (name="company")
public class CompanyEntity {
	
	//@Id
	//@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	String name;
	String address;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

}
