package com.rest.sample.employee.repository;

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

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import com.rest.sample.employee.model.APIResponse;
import com.rest.sample.employee.model.Company;
import com.rest.sample.employee.model.GenericResponse;

@Repository
public class MyRepoSQL {
	
	List<String> suceessMessageList = new ArrayList<>();
	List<String> errorMessageList = new ArrayList<>();
	
	public GenericResponse addCompanyIntoTable(String name, String address)  {
		GenericResponse genericResponse= null;
		System.out.println("Creating a new company >>" + name);
		System.out.println("Company Name="+name+"  Address="+address);
		try {
		Connection connection =DriverManager.getConnection("jdbc:mysql://localhost:3306/empschema", "root", "root");
		//Inserting into "company" table				
		PreparedStatement statement1;
		String query = "insert into company (name, address) values (?,?);";	
			statement1 = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			statement1.setString(1, name);
			statement1.setString(2, address);
			statement1.execute();
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			String failuremessage = "Error inserting record in DB";
			errorMessageList.add(failuremessage);
			genericResponse = new GenericResponse(400, "FAILED", errorMessageList);
			return genericResponse;
	
		}catch(Exception ex) {
			String failuremessage = "Exception occured while creating a company record. so couldnt process";
			errorMessageList.add(failuremessage);
			genericResponse = new GenericResponse(400, "FAILED", errorMessageList);
			return genericResponse;
		}

		String successmessage = "New company created with name: "+name;
		suceessMessageList.add(successmessage);
		genericResponse = new GenericResponse(201, "SUCCESS", suceessMessageList);
		return genericResponse;
		
	}
	
	
	public ResponseEntity<Collection<Company>> getCompanyList(){
		HashMap<Integer, Company> map = new HashMap<>();
		Collection<Company> listofCompanies ;
		try {
			Connection connection =DriverManager.getConnection("jdbc:mysql://localhost:3306/empschema", "root", "root");
			//view "company" table
			String query1 ="select id,name,address from company ;";					
			Statement statement1=connection.createStatement() ;
			ResultSet rs = statement1.executeQuery(query1);
			while(rs.next()){
				//Retrieve by column name
				int id  = rs.getInt("id");
				String name = rs.getString("name");
				String address = rs.getString("address");

				Company company = new Company();
				company.setName(name);
				company.setAddress(address);
				map.put(id, company);
			}
			connection.close();   
			listofCompanies=map.values();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
		}catch(Exception ex) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "123");
		return new ResponseEntity<>(listofCompanies,headers,HttpStatus.OK);							
	}
	
	public ResponseEntity<APIResponse> deleteCompanyUsingid(int id) {
		System.out.println ("Deleting Company with id="+id);
		APIResponse apiResponse = new APIResponse();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "123");

			try {
				Connection connection =DriverManager.getConnection("jdbc:mysql://localhost:3306/empschema", "root", "root");
				//delete "company" with id 
				String query1 ="delete from company where id= (?);";					
				PreparedStatement statement1=connection.prepareStatement(query1, Statement.RETURN_GENERATED_KEYS) ;
				statement1.setInt(1, id);
				statement1.execute();
				connection.close();   
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				apiResponse.setFailureMessage("SQL Exception while deleting");
				apiResponse.setStatusCode("500");
				return new ResponseEntity<>(apiResponse, headers,HttpStatus.INTERNAL_SERVER_ERROR);
			}catch(Exception ex) {
				apiResponse.setFailureMessage("Exception while deleting");
				apiResponse.setStatusCode("500");
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			apiResponse.setSucessMessage("Company with id=" +id +" has been deleted");
			apiResponse.setStatusCode("201");
			return new ResponseEntity<>(apiResponse,headers,HttpStatus.OK);	
		
	}
	

}
