package com.rest.sample.employee.model;

import java.util.Comparator;
import java.util.Date;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Pattern.Flag;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement
public class User implements Comparable <User> {
	
	@JsonProperty("empid")
	private int id;
	@JsonProperty("empName")
	private String name;
	@Email (message = "Invalid email")
	@JsonProperty("email")
	private String emailaddress;
	private String creationDate;
    
	userRole role;
	
	public enum userRole {
		ADMIN,
		DEVELOPER,
		SUPPORT
	}
	
	
	public String getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
		
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmailaddress() {
		return emailaddress;
	}
	public void setEmailaddress(String emailaddress) {
		this.emailaddress = emailaddress;
	}
	 @Override
	public String toString() {
		return("Id ="+this.getId() +" Name=" +this.getName()+" Emailaddress "+this.getEmailaddress());
	}
	/*
	 /*Comparator for sorting the list by User Name */
	 public static Comparator<User> UserNameComparator = new Comparator<User>() {
		    @Override
			public int compare(User u1, User u2) {
			   String UserName1 = u1.getName().toUpperCase();
			   String UserName2 = u2.getName().toUpperCase();

			   //ascending order
			   return UserName1.compareTo(UserName2);

			   //descending order
			   //return UserName2.compareTo(UserName1);
		    }

		};



	@Override
	public int compareTo(User o) {
		// TODO Auto-generated method stub
		
		int i=this.getName().toUpperCase().compareTo(o.getName().toUpperCase());
		
		return i;
	}
	public userRole getRole() {
		return role;
	}
	public void setRole(userRole role) {
		this.role = role;
	}
	public void setRole(String strRole) {
		// TODO Auto-generated method stub
		if ("ADMIN".equals(strRole)) {
			this.setRole(userRole.ADMIN);
		}else if ("DEVELOPER".equals(strRole)) {
			this.setRole(userRole.DEVELOPER);
		}else if ("SUPPORT".equals(strRole)) {
			this.setRole(userRole.SUPPORT);
		}
	}



	

}
