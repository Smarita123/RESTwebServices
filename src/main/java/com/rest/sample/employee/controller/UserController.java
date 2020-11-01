package com.rest.sample.employee.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Email;

import org.apache.tomcat.jni.Time;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.sample.employee.model.APIResponse;
import com.rest.sample.employee.model.Emailaddress;
import com.rest.sample.employee.model.GenericResponse;
import com.rest.sample.employee.model.PaginationResponse;
import com.rest.sample.employee.model.User;
import com.rest.sample.employee.service.UserService;
import com.rest.sample.employee.util.JsonCreator;
import com.rest.sample.employee.util.XMLConverter;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api (description="UserController class used to create users in database")
@RestController
@RequestMapping ("/users")
public class UserController {
	
	private static final Logger LOG= LoggerFactory.getLogger(UserController.class);
	@Autowired
	UserService userService;
	private final Path root= Paths.get("D:\\Smarita\\Automation\\FileUpload");
			
			@PostMapping(value="/createUser",
			consumes = { 
					MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
			produces = { 
					MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<GenericResponse> createUsers(@Valid @RequestBody User user) {
		LOG.info("***User creation ");		
		LOG.info("User :"+user.getEmailaddress(),user.getId(), user.getName(), user.getRole());
			//calling service class to process the employee records
		XMLConverter xmlconverter= new XMLConverter();
		LOG.info("****XML request****");
		LOG.info(xmlconverter.ConvertToXML(user));
		
		JsonCreator jsonCreator=new JsonCreator();
		LOG.info("****JSON String****");
		LOG.info(jsonCreator.convertToJsonString(user));
			final GenericResponse genericResponse = userService.createUser(user);
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
			
		}
			
			/**
			 * This api is used to retrieve all users stored in db
			 * Returns 200 
			 * @return
			 */
			@GetMapping(value="/getAllUsers",
					consumes = { MediaType.APPLICATION_JSON_VALUE, 
							MediaType.APPLICATION_XML_VALUE}, 
					produces = { MediaType.APPLICATION_JSON_VALUE,
							MediaType.APPLICATION_XML_VALUE})
			public ResponseEntity<Collection<User>> getAllUsers(@RequestParam(defaultValue = "0") Integer pageNo, 
                    @RequestParam(defaultValue = "5") Integer pageSize,
                    @RequestParam(defaultValue = "id") String sortBy) {
				
				LOG.info("getAllUsers() in Controller class called");
				
				final ResponseEntity<Collection<User>> responseEntity = userService.getAllUsers();
				return responseEntity;
				
			}

			/**
			 * This api is used to retrieve all users Page wise
			 * Returns 200 
			 */
			@GetMapping(value="/getAllUsersPageWise",
					consumes = { MediaType.APPLICATION_JSON_VALUE, 
							MediaType.APPLICATION_XML_VALUE}, 
					produces = { MediaType.APPLICATION_JSON_VALUE,
							MediaType.APPLICATION_XML_VALUE})
			public ResponseEntity<PaginationResponse> getAllUsersPage(@RequestParam(defaultValue = "0") Integer pageNo, 
                    @RequestParam(defaultValue = "10") Integer limit) {
				
				LOG.info("getAllUsersPageWise() in Controller class called");
				
				final ResponseEntity<PaginationResponse> responseEntity = userService.getAllUsersPageWiseNew(pageNo, limit);
				return responseEntity;
				
			}
			
			/**
			 * This api is used to retrieve all users stored in db
			 * Returns 200 
			 * @return
			 */
			@GetMapping(value="/getAllUsersSortbyName",
					consumes = { MediaType.APPLICATION_JSON_VALUE, 
							MediaType.APPLICATION_XML_VALUE}, 
					produces = { MediaType.APPLICATION_JSON_VALUE,
							MediaType.APPLICATION_XML_VALUE})
			public ResponseEntity<Collection<User>> getAllUsersSort() {
				
				LOG.info("getAllUsers() in Controller class called");
				
				final ResponseEntity<Collection<User>> responseEntity = userService.getAllUsersSortByName();
				return responseEntity;
				
			}
			
			@GetMapping(value="/getAllUsersSortByNameUseComparable",
			consumes = { MediaType.APPLICATION_JSON_VALUE, 
					MediaType.APPLICATION_XML_VALUE}, 
			produces = { MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<Collection<User>> getAllUsersSortByNameUseComparable() {
		
		LOG.info("getAllUsersSortByNameUseComparable() in Controller class called");
		
		final ResponseEntity<Collection<User>> responseEntity = userService.getAllUsersSortByNameUseComparable();
		return responseEntity;
		
	}
			

		
			@PostMapping(value="/createUserNew",
					consumes = { 
							MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
					produces = { 
							MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE})
			public ResponseEntity<GenericResponse> createUsersNew(@Valid @RequestBody User user ) {
				LOG.info("***User creation ");	
				
				//User user=new User();
				//user.setId(id);
				//user.setEmailaddress(emailaddress);
				//user.setName(name);
				//user.setRole(role);
								
				LOG.info("User :"+user.getEmailaddress(),user.getId(), user.getName(), user.getRole());
				
					final GenericResponse genericResponse = userService.createUser(user);
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
					
				}
			//*****using PATH VARIABLE*****
			@PostMapping(value="/printEmail/{email}",
					consumes = { 
							MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
					produces = { 
							MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
			public APIResponse printEmail(@PathVariable("email")  String email ) {	
				LOG.info("Email:"+email);
					final APIResponse apiResponse = new APIResponse();;
					apiResponse.setSucessMessage("Email address "+email);
					apiResponse.setStatusCode("201");
					Date date = new Date();  
	                Timestamp ts=new Timestamp(date.getTime());  
	                System.out.println(ts);                     
					apiResponse.setTimestamp(ts);
					return apiResponse;
				}
			//****using Query Param*****
			@PostMapping(value="/printEmail",
					consumes = { 
							MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
					produces = { 
							MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
			public APIResponse printEmailaddress(@RequestParam  String email ) {	
				LOG.info("Email:"+email);
					final APIResponse apiResponse = new APIResponse();;
					apiResponse.setSucessMessage("Email address "+email);
					apiResponse.setStatusCode("201");
					Date date = new Date();  
	                Timestamp ts=new Timestamp(date.getTime());  
	                System.out.println(ts);                     
					apiResponse.setTimestamp(ts);
					return apiResponse;
				}
			//***using Request Payload****
			@PostMapping(value="/printEmailId",
					consumes = { 
							MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
					produces = { 
							MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
			public APIResponse printEmailPayload(@RequestBody User user ) {	
				LOG.info("Email:"+user.getEmailaddress());
					final APIResponse apiResponse = new APIResponse();;
					apiResponse.setSucessMessage("Email address "+user.getEmailaddress());
					apiResponse.setStatusCode("201");
					Date date = new Date();  
	                Timestamp ts=new Timestamp(date.getTime());  
	                System.out.println(ts);                     
					apiResponse.setTimestamp(ts);
					return apiResponse;
				}
			@ApiResponses( value = {@ApiResponse (code=201, message="File name \"+file.getName()"),
					@ApiResponse (code=400, message="Bad request"),
					@ApiResponse (code=500, message="Internal Server Error")}
)
			@ApiOperation(value="/fileUpload used to upload files")
			@PostMapping(value="/file/upload")
			public APIResponse fileUpload(@RequestParam("file")  MultipartFile file ) {	
				LOG.info("file {}", file.getOriginalFilename());
				System.out.println("file {}" +file.getOriginalFilename());
				LOG.info("File object:"+file);
				try {
					Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
				} catch (IOException e) {
					e.printStackTrace();
					throw new RuntimeException ("Could not store the file, Error " +e.getMessage());
				}
					final APIResponse apiResponse = new APIResponse();;
					apiResponse.setSucessMessage("File name "+file.getOriginalFilename());
					apiResponse.setStatusCode("201");
					Date date = new Date();  
	                Timestamp ts=new Timestamp(date.getTime());  
	                System.out.println(ts);                     
					apiResponse.setTimestamp(ts);
					return apiResponse;
				}


}
