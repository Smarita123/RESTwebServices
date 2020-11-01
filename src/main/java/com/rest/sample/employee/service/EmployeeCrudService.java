package com.rest.sample.employee.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.rest.sample.employee.model.Employee;
import com.rest.sample.employee.model.GenericResponse;

@Service
public class EmployeeCrudService {
	
	private static final Logger LOG = LoggerFactory.getLogger(EmployeeCrudService.class);
	
	private static final String EMPLOYEE_QUERY = "insert into employee (name, role, mobilenumber) values (?,?,?)" ;
	private static final String LAST_INSERT_QUERY="select last_insert_id()";
	private static final String ADDRESS_QUERY ="insert into address (empid, name, buildingName, state, city, zipcode) values (?,?,?,?,?,?)";
	
	//private static final String JDBC_URL = "jdbc:mysql://localhost:3306/empschema";
	
	//private static final String USER_NAME = "root";
	//private static final String PASSWORD = "root";
	@Value("${db.url}")
	private  String JDBC_URL;
	@Value("${db.username}")
	private  String USER_NAME;
	@Value("${db.password}")
	private  String PASSWORD;
	@Autowired
	private MessageSource messageSource;
	public GenericResponse createMultipleEmployees(List<Employee> employeeList) {
		LOG.info("createMultipleEmployees in EmployeeCrudService class is called");
		GenericResponse genericResponse = null;
		List<String> suceessMessageList = new ArrayList<>();
		List<String> errorMessageList = new ArrayList<>();
		Connection connection = null;
		try {
			LOG.info("DBURL- "+JDBC_URL);
			LOG.info("Username- "+USER_NAME);
			LOG.info("Password- "+PASSWORD);
		    connection =DriverManager.getConnection(JDBC_URL, USER_NAME, PASSWORD);
			for(Employee employee: employeeList) {
				LOG.info("Processing employee record with id {}", employee.getEmpId());
				//Executing employee query
				processEmployeeRecord(connection, employee);
				//Retrieve last inserted empid from the employee table
				fetchLastInsertedRecord(connection, employee);
				//process address record
				processAddressRecord(connection, employee);

				String successmessage = messageSource.getMessage("successmsg", new Object[]{ employee. getEmpId(), employee.getName() } , new Locale("el"));
				LOG.info("successmessage {}",successmessage);
				suceessMessageList.add(successmessage);
			}
			genericResponse = new GenericResponse(201, "SUCCESS", suceessMessageList);

		} catch(SQLException ex) {
			LOG.error("SQLException occured while processing records");
			String failuremessage = messageSource.getMessage("failuremsgSql", null, new Locale("el"));
			errorMessageList.add(failuremessage);
			genericResponse = new GenericResponse(406, "FAILED", errorMessageList);
			ex.printStackTrace();
		} catch(Exception exception) {
			String failuremessage = messageSource.getMessage("failuremsg", null, new Locale("el"));
			errorMessageList.add(failuremessage);
			genericResponse = new GenericResponse(500, "FAILED", errorMessageList);
			exception.printStackTrace();
		} finally {
			if(connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return genericResponse;
	}
	    
	private void processEmployeeRecord(Connection connection, Employee employee) throws SQLException{
		//Executing employee query
		PreparedStatement empStatement=connection.prepareStatement(EMPLOYEE_QUERY, Statement.RETURN_GENERATED_KEYS) ;
		empStatement.setString(1, employee.getName());
		empStatement.setString(2, employee.getRole());
		empStatement.setString(3, employee.getMobilenumber());
		empStatement.execute();
		empStatement.close();
	}
	    
	private void fetchLastInsertedRecord(Connection connection, Employee employee) throws SQLException{
		//fetching last inserted records
		Statement lastInsertRecordStatement= connection.createStatement();
		ResultSet rs = lastInsertRecordStatement.executeQuery(LAST_INSERT_QUERY);
		while (rs.next()) {
			int lastEmpid = rs.getInt("last_insert_id()");
			LOG.info("last employee id retrieved {}",lastEmpid);
			employee.setEmpId(lastEmpid);
		}
		rs.close();
		lastInsertRecordStatement.close();
	}
	    
	private void processAddressRecord(Connection connection, Employee employee) throws SQLException {
		PreparedStatement addressStatement=connection.prepareStatement(ADDRESS_QUERY, Statement.RETURN_GENERATED_KEYS) ;
		LOG.info("Employee Name= {} AutoGenerated Emp id, Address= {} ", employee.getName(), employee.getAddress());
		addressStatement.setInt(1, employee.getEmpId());
		addressStatement.setString(2, employee.getName());
		addressStatement.setString(3, employee.getAddress().getBuildingName());
		addressStatement.setString(4, employee.getAddress().getState());
		addressStatement.setString(5, employee.getAddress().getCity());
		addressStatement.setInt(6, employee.getAddress().getZipcode());
		addressStatement.execute();
		addressStatement.close();
	}
}