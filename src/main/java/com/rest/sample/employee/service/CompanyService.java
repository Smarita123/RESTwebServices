package com.rest.sample.employee.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.rest.sample.employee.model.APIResponse;
import com.rest.sample.employee.model.Company;
import com.rest.sample.employee.model.GenericResponse;
import com.rest.sample.employee.model.GenericResponse.STATUS;
import com.rest.sample.employee.repository.MyRepoSQL;

@Service
public class CompanyService {
	
	List<String> suceessMessageList = new ArrayList<>();
	List<String> errorMessageList = new ArrayList<>();
	@Autowired
	MyRepoSQL myRepoSQL ;
	
	public GenericResponse createCompanyService(Company company) {
		GenericResponse genericResponse= null;
		genericResponse= myRepoSQL.addCompanyIntoTable(company.getName(), company.getAddress());
		return genericResponse;
	}
	
	
	public ResponseEntity<Collection<Company>> viewCompanyService(){
		
		return myRepoSQL.getCompanyList();			
	}
	
	public ResponseEntity<APIResponse> deleteCompanyById(int id) {
		//APIResponse apiResponse=myRepoSQL.deleteCompanyUsingid(id).getBody();
		return myRepoSQL.deleteCompanyUsingid(id);
	}

}
