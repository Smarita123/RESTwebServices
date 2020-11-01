package com.rest.sample.employee.util;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.sample.employee.model.Address;
import com.rest.sample.employee.model.Employee;
import com.rest.sample.employee.model.Transaction;
import com.rest.sample.employee.model.User;

public class JsonCreator {
	

	public static void main(String[] args) {
		String json=null;
		// TODO Auto-generated method stub
		//Transaction transaction = new Transaction();
		//transaction.setAmount(50.00);
		//transaction.setId(17357);
		Employee emp1 = new Employee();
		Address address1 = new Address();
		emp1.setEmpId(8);
		emp1.setName("Shreedhar");
		emp1.setRole("Teacher");
		emp1.setMobilenumber("9876512432");
		address1.setBuildingName("ABC Housing Society");
		address1.setCity("Mumbai");
		address1.setState("Maharashtra");
		address1.setZipcode(432101);
		emp1.setAddress(address1);
		
		Employee emp2 = new Employee();
		Address address2 = new Address();
		emp2.setEmpId(9);
		emp2.setName("Tara");
		emp2.setRole("Teacher");
		emp2.setMobilenumber("9876509876");
		address2.setBuildingName("XYZ Housing Society");
		address2.setCity("Bangalore");
		address2.setState("Karnataka");
		address2.setZipcode(432101);
		emp2.setAddress(address2);
				
		List<Employee> list = new ArrayList<Employee>();
		list.add(emp1);
		list.add(emp2);
		
		try {
			json= new ObjectMapper().writeValueAsString(list);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(json);
					
	}
	
	public String convertToJsonString(User user) {
		
		String json=null;	
		try {
			json= new ObjectMapper().writeValueAsString(user);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(json);
		return json;
					
		
	}
	
	

}
