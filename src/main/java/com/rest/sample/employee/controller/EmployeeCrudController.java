package com.rest.sample.employee.controller;

import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.sample.employee.model.Employee;
import com.rest.sample.employee.model.GenericResponse;
import com.rest.sample.employee.service.EmployeeCrudService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import springfox.documentation.annotations.ApiIgnore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Api (description="EmployeeCrudController class used to create, read, update and delete employee objects in database")
@RestController
@RequestMapping("/employees")
public class EmployeeCrudController {

	private static final Logger LOG= LoggerFactory.getLogger(EmployeeCrudController.class);

	@Autowired
	private EmployeeCrudService empCrudService;
	@ApiResponses( value = {@ApiResponse (code=201, message="a new resource created"),
							@ApiResponse (code=400, message="Bad request"),
							@ApiResponse (code=500, message="Internal Server Error")}
	)
	// code and message to be returned
	@ApiOperation(value="/createNewEmployees creates multiple employee records in db")
	//@ApiIgnore //will ignore the api in swagger docs
	@PostMapping(value="/createNewEmployees",
			consumes = { 
					MediaType.APPLICATION_XML_VALUE},
			produces = { 
					MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<GenericResponse> createMultipleEmployee( @RequestHeader("Authorization") String authorization,@RequestHeader("Role") String role, @Valid @RequestBody List<Employee> employeeList) {
		
		LOG.info("First Line****");
		if (role.equalsIgnoreCase("admin")) {
			
		LOG.info("Processing employee records for Admin role");
			//calling service class to process the employee records
			final GenericResponse genericResponse = empCrudService.createMultipleEmployees(employeeList);

			final HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", "123");
			LOG.info("Generics response {"+genericResponse+"} " );
			try {
				final String json= new ObjectMapper().writeValueAsString(genericResponse);
			LOG.info(json);
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return new ResponseEntity<>(genericResponse,headers,HttpStatus.CREATED);
			
		} else {
			//log.info(" Cannot process for NonAdmin role");
			String errorMessage = "Unauthorized to create employees. Only Admin can create Employees";
			List<String> errorMessageList = new ArrayList<>();
			errorMessageList.add(errorMessage);
			GenericResponse genericResponse = new GenericResponse(401,errorMessageList);
			return new ResponseEntity<>(genericResponse, HttpStatus.UNAUTHORIZED);

		}
	}
}

