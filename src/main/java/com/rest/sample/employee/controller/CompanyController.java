package com.rest.sample.employee.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rest.sample.employee.model.APIResponse;
import com.rest.sample.employee.model.Company;
import com.rest.sample.employee.model.Employee;
import com.rest.sample.employee.model.GenericResponse;
import com.rest.sample.employee.model.GenericResponse.STATUS;
import com.rest.sample.employee.model.RequestPayload;
import com.rest.sample.employee.service.CompanyService;

@RestController
@RequestMapping({"/company"})
public class CompanyController {

	private String dburl= "jdbc:mysql://localhost:3306/empschema";
	String username="root";
	String password="root";
	ArrayList errorMessageList= new ArrayList<>();
	@Autowired
	CompanyService companyservice;


	@PostMapping(value="/CreateCompany", 
			consumes = { MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE},
			produces = { MediaType.APPLICATION_JSON_VALUE, 
					MediaType.APPLICATION_XML_VALUE})
	public GenericResponse createCompany( @RequestHeader("Authorization") String authorization,@RequestHeader("Role") String role, @Valid @RequestBody Company company) {

		GenericResponse genericResponse;
		System.out.println("Authorization: "+authorization);
		System.out.println("Role: "+role);
		if (role.equalsIgnoreCase("admin")) {
			genericResponse=companyservice.createCompanyService(company);
			return genericResponse;
		}
		else {
			String failuremessage = "Unauthorized";
			errorMessageList.add(failuremessage);
			genericResponse = new GenericResponse(401, "FAILED", errorMessageList);
			return genericResponse;
		}
	}

	@GetMapping(value="/ViewCompanies", 
			consumes = { MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE},
			produces = { MediaType.APPLICATION_JSON_VALUE, 
					MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<Collection<Company>> viewCompany( @RequestHeader("Authorization") String authorization,@RequestHeader("Role") String role) {
		System.out.println("Authorization: "+authorization);
		System.out.println("Role: "+role);

		HashMap<Integer, Company> map = new HashMap<>();
		Collection<Company> listofCompanies ;


		if (role.equalsIgnoreCase("admin")) {
			return companyservice.viewCompanyService();
		}
		else {

			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);							
		}		
	}
	@DeleteMapping(value="/DeleteCompany", 
			consumes = { MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE},
			produces = { MediaType.APPLICATION_JSON_VALUE, 
					MediaType.APPLICATION_XML_VALUE})
	//Delete using Request param
	public ResponseEntity<APIResponse> deleteCompany( @RequestHeader("Authorization") String authorization,@RequestHeader("Role") String role, @RequestParam int id) {
		System.out.println("Authorization: "+authorization);
		System.out.println("Role: "+role);


		if (role.equalsIgnoreCase("admin")) {
			return companyservice.deleteCompanyById(id);	
		}
		else {

			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);							
		}	
	}

	@DeleteMapping(value="/DeleteCompanyUsingPathVariable/{id}", 
			consumes = { MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE},
			produces = { MediaType.APPLICATION_JSON_VALUE, 
					MediaType.APPLICATION_XML_VALUE})
	//Delete by passing Path variable
	public ResponseEntity<APIResponse> deleteCompanyPathVariable( @RequestHeader("Authorization") String authorization,@RequestHeader("Role") String role, @PathVariable("id") int id) {
		System.out.println("Authorization: "+authorization);
		System.out.println("Role: "+role);


		if (role.equalsIgnoreCase("admin")) {
			return companyservice.deleteCompanyById(id);	
		}
		else {

			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);							
		}	
	}


}
