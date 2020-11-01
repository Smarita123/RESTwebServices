package com.rest.sample.employee.model;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Pattern.Flag;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModelProperty;

@XmlRootElement
public class Address {
	@ApiModelProperty (notes="Name of the Building")
	String buildingName;
	@ApiModelProperty (notes="Name of the State")
	String state;
	@ApiModelProperty (notes="Name of the City")
	@Pattern(regexp = "Mumbai|Bangalore", flags=Flag.CASE_INSENSITIVE, message="City should be either Mumbai or Bangalore")
	String city;
	@ApiModelProperty (notes="Zipcode for address location")
	int zipcode;
	
	
	public void setBuildingName(String buildingName) {
		this.buildingName=buildingName;
	}
	public String getBuildingName() {
		return buildingName;
	}
	public void setState(String state) {
		this.state=state;
	}
	public String getState() {
		return state;
	}
	public void setCity(String city) {
		this.city=city;
	}
	public String getCity() {
		return city;
	}
	public void setZipcode(int zipcode) {
		this.zipcode=zipcode;
	}
	public int getZipcode() {
		return zipcode;
	}

}
