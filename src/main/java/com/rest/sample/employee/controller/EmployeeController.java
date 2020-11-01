package com.rest.sample.employee.controller;

import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.rest.sample.employee.model.Address;
import com.rest.sample.employee.model.Card;
import com.rest.sample.employee.model.Cart;
import com.rest.sample.employee.model.Employee;
import com.rest.sample.employee.model.RequestPayload;
import com.rest.sample.employee.model.ResponseTransaction;
import com.rest.sample.employee.model.Student;
import com.rest.sample.employee.model.Transaction;
import com.rest.sample.employee.model.APIResponse;

@RestController
@RequestMapping({"/students","/carts","/employee"})
public class EmployeeController {

	//private String dburl= "jdbc:mysql://localhost:/3306/empschema";
	private String dburl= "jdbc:mysql://localhost:3306/empschema";

	String username="root";
	String password="root";


	private static HashMap<Integer,Employee> hmap= new HashMap<>();
	private static HashMap<Integer,Address> hmapAddress= new HashMap<>();

	@GetMapping("{cartid}/products/{productid}")
	public Cart getCart(@PathVariable("cartid") int cartid, @PathVariable("productid") int productid) { 
		Cart cart = new Cart();
		cart.setCartid(cartid);
		cart.setProductid(productid);
		return cart;
	}
	@GetMapping("/getStudents/{studentid}/{studentName}")
	//public Student getStudent(@PathVariable("studentName") String studentName, @PathVariable("studentid") int studentid) { 
	public Student getStudent(@PathVariable int studentid, @PathVariable String studentName) {
		Student student = new Student();
		student.setId(studentid);
		student.setName(studentName);
		return student; }
	@GetMapping(value="/getStudents", consumes="application/json", produces= {"application/json", "application/xml"})
	public Student getStudentInfo(@RequestParam (value="studentid",required = true) int id, @RequestParam(value="studentName",required = false, defaultValue="abc") String name) {
		Student student = new Student();
		student.setId(id);
		student.setName(name);
		return student; }
	@PostMapping("/Employee")
	public RequestPayload postEmployeeWithRequestBody(@Valid @RequestBody RequestPayload payload) {
		System.out.println(payload.getEmpName());
		System.out.println(payload.getId());
		return payload;
		//return payload; 						
	}
	@GetMapping("/Employees")
	public ResponseEntity<Employee> getEmp(int id, String name){
		Employee emp= new Employee();
		emp.setEmpId(id);
		emp.setName(name);
		return new ResponseEntity<>(emp, HttpStatus.BAD_REQUEST);

	}
	/*
	@GetMapping("/getEmployee")
	public Employee getEmployee() { 
		Employee emp= new Employee();
		emp.setEmpId(20);
		emp.setName("Mantu Patra");
		Address officeAddress = new Address();
		officeAddress.setBuildingName("Building 1");
		officeAddress.setCity("New Mumbai");
		officeAddress.setState("MH");
		emp.setOfficeaddress(officeAddress);
		Address homeAddress = new Address();
		homeAddress.setBuildingName("Building ABC");
		homeAddress.setCity("Kandivali");
		homeAddress.setState("Maharastra");
		emp.setHomeaddress(homeAddress);
		return emp;
	} */
	@GetMapping("/getTransaction")
	public ResponseTransaction getTransaction() { 
		ResponseTransaction responseTransaction= new ResponseTransaction();
		Transaction transaction= new Transaction();
		Card card = new Card();
		transaction.setId(634215);
		transaction.setAmount(50.00);
		card.setFirstsix(400000);
		card.setLastfour(0001);
		responseTransaction.setCard(card);
		responseTransaction.setTransaction(transaction);
		return responseTransaction;

	}
	@PostMapping(value="/createEmployee", 
			consumes = { MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE},
			produces = { MediaType.APPLICATION_JSON_VALUE, 
					MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<APIResponse> createEmployee( @RequestHeader("Authorization") String authorization,@RequestHeader("Role") String role, @Valid @RequestBody Employee employee) {
		System.out.println("Creating a new employee >>" + employee);
		System.out.println("Authorization: "+authorization);
		System.out.println("Role: "+role);
		System.out.println("Employee Name="+employee.getName()+" AutoGeneratedEmpId, Address="+employee.getAddress());
		APIResponse apiResponse = new APIResponse();
		if (role.equalsIgnoreCase("admin")) {
			try {
				Connection connection =DriverManager.getConnection("jdbc:mysql://localhost:3306/empschema", "root", "root");
				//Inserting into "employee" table
				String query1 ="insert into employee (name, role, mobilenumber) values (?,?,?);";					
				PreparedStatement statement1=connection.prepareStatement(query1, Statement.RETURN_GENERATED_KEYS) ;
				//statement1.setInt(1, employee.getEmpId());
				statement1.setString(1, employee.getName());
				statement1.setString(2, employee.getRole());
				statement1.setString(3, employee.getMobilenumber());
				statement1.execute();

				//Retrieve last inserted empid from the employee table
				String queryLastInsertedId = "select last_insert_id();" ;
				Statement statementLastInsertedId = connection.createStatement();
				ResultSet rs = statementLastInsertedId.executeQuery(queryLastInsertedId);
				rs.next();
				int lastEmpid = rs.getInt("last_insert_id()");					
				employee.setEmpId(lastEmpid);

				//Inserting into "address" table
				String query2 ="insert into address (empid, name, buildingName, state, city, zipcode) values (?,?,?,?,?,?);";
				PreparedStatement statement2=connection.prepareStatement(query2, Statement.RETURN_GENERATED_KEYS) ;
				System.out.println("Employee Name="+employee.getName()+" AutoGenerated Emp id, Address="+employee.getAddress());
				statement2.setInt(1, employee.getEmpId());
				statement2.setString(2, employee.getName());
				statement2.setString(3, employee.getAddress().getBuildingName());
				statement2.setString(4, employee.getAddress().getState());
				statement2.setString(5, employee.getAddress().getCity());
				statement2.setInt(6, employee.getAddress().getZipcode());
				statement2.execute();

				String successmessage = "New Resource created with id :"+ employee.getEmpId();
				apiResponse.setStatusCode("201");
				apiResponse.setSucessMessage(successmessage); 
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				String failuremessage = "Error inserting record in DB";
				apiResponse.setStatusCode("406");
				apiResponse.setFailureMessage(failuremessage);
				return new ResponseEntity<>(apiResponse,HttpStatus.NOT_ACCEPTABLE);
			}catch(Exception ex) {
				String failuremessage = "Exception occured while creating a student record. so couldnt process";
				apiResponse.setStatusCode("500");
				apiResponse.setFailureMessage(failuremessage);
				return new ResponseEntity<>(apiResponse,HttpStatus.INTERNAL_SERVER_ERROR);
			}
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", "123");
			return new ResponseEntity<>(apiResponse,headers,HttpStatus.CREATED);	
		}
		else {
			apiResponse.setStatusCode("401");
			apiResponse.setFailureMessage("Unauthorized to create employees. Only Admin can create Employees");
			return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);							
		}		


		/*
		APIResponse apiResponse = new APIResponse();
		if (role.equalsIgnoreCase("admin")){

			try {
				hmap.put(employee.getEmpId(), employee);	
				hmapAddress.put(employee.getEmpId(), employee.getAddress());
				System.out.println(employee.getName()+employee.getEmpId()+employee.getAddress());
				String successmessage = "New Resource created with id :"+ employee.getEmpId();
				apiResponse.setStatusCode("201");
				apiResponse.setSucessMessage(successmessage); 
				//returning headers
				HttpHeaders headers = new HttpHeaders();
			    headers.add("Authorization", "123");
				return new ResponseEntity<>(apiResponse,headers,HttpStatus.CREATED);	
			} catch(Exception ex) {
				String failuremessage = "Exception occured while creating a student record. so couldnt process";
				apiResponse.setStatusCode("500");
				apiResponse.setFailureMessage(failuremessage);
				return new ResponseEntity<>(apiResponse,HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}else {
			apiResponse.setStatusCode("401");
			apiResponse.setFailureMessage("Unauthorized to create employees. Only Admin can create Employees");
			return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);							
		}		*/

	}
	
	/*
	@PostMapping(value="/createNewEmployees", 
			consumes = { MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE},
			produces = { MediaType.APPLICATION_JSON_VALUE, 
					MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<List <APIResponse>> createMultipleEmployee( @RequestHeader("Authorization") String Authorization,@RequestHeader("Role") String role, @Valid @RequestBody List<Employee> employeeList) {

		//Iterator iter= employeeList.iterator();
		APIResponse apiResponse = new APIResponse();
		List <APIResponse> responseList= new ArrayList <APIResponse> ();

		if (role.equalsIgnoreCase("admin")) {

			try {
				Connection connection =DriverManager.getConnection("jdbc:mysql://localhost:3306/empschema", "root", "root");
				//Enhanced for loop to reduce extra line of codes
				for (Employee emp: employeeList) {
					//while (iter.hasNext()) {
					//emp= (Employee) iter.next();
					System.out.println("Creating a new employee >>" + emp.getName());
					System.out.println("Authorization: "+Authorization);
					System.out.println("Role: "+role);
					System.out.println("Employee Name="+emp.getName()+" AutoGeneratedEmpId, Address="+emp.getAddress());


					//Inserting into "employee" table
					String query1 ="insert into employee (name, role, mobilenumber) values (?,?,?);";					
					PreparedStatement statement1=connection.prepareStatement(query1, Statement.RETURN_GENERATED_KEYS) ;
					//statement1.setInt(1, employee.getEmpId());
					statement1.setString(1, emp.getName());
					statement1.setString(2, emp.getRole());
					statement1.setString(3, emp.getMobilenumber());
					statement1.execute();

					//Retrieve last inserted empid from the employee table
					String queryLastInsertedId = "select last_insert_id();" ;
					Statement statementLastInsertedId = connection.createStatement();
					ResultSet rs = statementLastInsertedId.executeQuery(queryLastInsertedId);
					rs.next();
					int lastEmpid = rs.getInt("last_insert_id()");					
					emp.setEmpId(lastEmpid);

					//Inserting into "address" table
					String query2 ="insert into address (empid, name, buildingName, state, city, zipcode) values (?,?,?,?,?,?);";
					PreparedStatement statement2=connection.prepareStatement(query2, Statement.RETURN_GENERATED_KEYS) ;
					System.out.println("Employee Name="+emp.getName()+" AutoGenerated Emp id, Address="+emp.getAddress());
					statement2.setInt(1, emp.getEmpId());
					statement2.setString(2, emp.getName());
					statement2.setString(3, emp.getAddress().getBuildingName());
					statement2.setString(4, emp.getAddress().getState());
					statement2.setString(5, emp.getAddress().getCity());
					statement2.setInt(6, emp.getAddress().getZipcode());
					statement2.execute();
					
					String successmessage = "New Resource created with id :"+ emp.getEmpId();
					apiResponse.setStatusCode("201");
					apiResponse.setSucessMessage(successmessage); 
					responseList.add(apiResponse);				

				}connection.close();
			}catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				String failuremessage = "Error inserting record in DB";
				apiResponse.setStatusCode("406");
				apiResponse.setFailureMessage(failuremessage);
				responseList.add(apiResponse);
				return new ResponseEntity<>(responseList,HttpStatus.NOT_ACCEPTABLE);
			}catch(Exception ex) {
				String failuremessage = "Exception occured while creating a student record. so couldnt process";
				apiResponse.setStatusCode("500");
				apiResponse.setFailureMessage(failuremessage);
				responseList.add(apiResponse);
				return new ResponseEntity<>(responseList,HttpStatus.INTERNAL_SERVER_ERROR);
			}
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", "123");
			return new ResponseEntity<>(responseList,headers,HttpStatus.CREATED);	
		}

		else {
			apiResponse.setStatusCode("401");
			apiResponse.setFailureMessage("Unauthorized to create employees. Only Admin can create Employees");
			responseList.add(apiResponse);
			return new ResponseEntity<>(responseList, HttpStatus.UNAUTHORIZED);							
		}		


	}
*/

	/**
	 * This api is used to retrieve all students stored in hashmap
	 * Returns 200 
	 * @return
	 */
	@GetMapping(value="/getAllEmp",
			consumes = { MediaType.APPLICATION_JSON_VALUE, 
					MediaType.APPLICATION_XML_VALUE}, 
			produces = { MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<Collection<Employee>> getAllEmployee() {
		APIResponse apiResponse = null;
		System.out.println("getAllEmployees called");
		HashMap<Integer, Employee> map = new HashMap<>();
		Collection<Employee> listofEmployees ;
		try {
			Connection connection =DriverManager.getConnection(dburl, username, password);

			String query ="select emp.empid, emp.name, emp.role, emp.mobilenumber, ad.buildingName, ad.state, ad.city, ad.zipcode from employee emp, address ad where emp.empid=ad.empid;";
			PreparedStatement statement=connection.prepareStatement(query) ;		
			ResultSet resultSet =statement.executeQuery();

			while (resultSet.next()) {
				Employee emp= new Employee();
				emp.setEmpId(resultSet.getInt("empid"));
				emp.setName(resultSet.getString("name"));
				emp.setRole(resultSet.getString("role"));
				emp.setMobilenumber(resultSet.getString("mobilenumber"));
				Address address = new Address();
				address.setBuildingName(resultSet.getString("buildingName"));
				address.setCity(resultSet.getString("city"));
				address.setState(resultSet.getString("state"));
				address.setZipcode(resultSet.getInt("zipcode"));
				emp.setAddress(address);
				map.put(emp.getEmpId(), emp);
			}
			connection.close();
			listofEmployees = map.values(); //all values in hashmap are copied to the 'listofEmployees' collection
			//return new ResponseEntity<APIResponse> ((APIResponse) listofEmployees, HttpStatus.OK);
			//return new ResponseEntity<Collection<Employee>> (listofEmployees, HttpStatus.OK);
			return new ResponseEntity<> (listofEmployees, HttpStatus.OK);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			String failuremessage = "Error retrieving record from DB";
			apiResponse.setStatusCode("400");
			apiResponse.setFailureMessage(failuremessage);
			//return new ResponseEntity<>(apiResponse,HttpStatus.INTERNAL_SERVER_ERROR);
			return null;
		}



	}

	@PutMapping(value="/updateEmployee",
			consumes = { MediaType.APPLICATION_JSON_VALUE, 
					MediaType.APPLICATION_XML_VALUE}, 
			produces = { MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<APIResponse> updateEmployee(@RequestHeader("Role") String role, @Valid @RequestBody Employee employee) {
		APIResponse apiResponse;
		if (role.equals("admin")) {
			System.out.println("Updating Employee ");
			Integer empid = employee.getEmpId();
			apiResponse = new APIResponse();
			try {
				if(hmap.containsKey(empid)) {
					employee = hmap.put(employee.getEmpId(),employee);

					String success= "Employee resource updated successfully for student id: "+empid;
					apiResponse.setStatusCode("200");
					apiResponse.setSucessMessage(success);
					return new ResponseEntity<>(apiResponse,HttpStatus.OK);
				} else {
					String failuremsg = "Couldn't update the Employee as resource was not found for employee id :"+empid;
					apiResponse.setStatusCode("400");
					apiResponse.setFailureMessage(failuremsg);
					return new ResponseEntity<>(apiResponse,HttpStatus.BAD_REQUEST);

				}
			} catch(Exception ex) {
				String failuremsg = "Exception occured while updating the Employee resource";
				apiResponse.setStatusCode("500");
				apiResponse.setFailureMessage(failuremsg);
				return new ResponseEntity<>(apiResponse,HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		else {
			apiResponse = new APIResponse();
			String failuremsg = "Only admin can update the employee details";
			apiResponse.setStatusCode("401");
			apiResponse.setFailureMessage(failuremsg);
			return new ResponseEntity<>(apiResponse,HttpStatus.UNAUTHORIZED);
		}				
	}

	@DeleteMapping(value="/deleteEmployee",
			consumes = { MediaType.APPLICATION_JSON_VALUE, 
					MediaType.APPLICATION_XML_VALUE}, 
			produces = { MediaType.APPLICATION_JSON_VALUE,
					MediaType.APPLICATION_XML_VALUE})
	public ResponseEntity<APIResponse> deleteEmployee( @RequestHeader ("Role") String role, @RequestParam int empId){
		APIResponse apiResponse ;
		if (role.equals("admin")) {
			System.out.println("Deleting a particular Employee resource based on Employee id ");
			Employee employee = null;
			apiResponse = new APIResponse();
			try {
				System.out.println("empId= "+empId);
				if(hmap.containsKey(empId)) {
					System.out.println("Emp id found in hashmap");
					employee = hmap.get(empId);
					hmap.remove(empId,employee);
					apiResponse.setStatusCode("200");
					apiResponse.setSucessMessage("Employee deleted successfully for emp id:"+empId);
					return new ResponseEntity<>(apiResponse,HttpStatus.OK);

				} else {
					apiResponse.setStatusCode("400");
					apiResponse.setFailureMessage("Couldn't delete the Employee as resource was not found for Emp id :"+empId);
					return new ResponseEntity<>(apiResponse,HttpStatus.BAD_REQUEST);
				}
			} catch(Exception ex) {
				apiResponse.setStatusCode("500");
				apiResponse.setFailureMessage("Exception occured while deleting Employee");
				return new ResponseEntity<>(apiResponse,HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			apiResponse = new APIResponse();
			String failuremsg = "Only admin can delete the employee record";
			apiResponse.setStatusCode("401");
			apiResponse.setFailureMessage(failuremsg);
			return new ResponseEntity<>(apiResponse,HttpStatus.UNAUTHORIZED);
		}		

	}


}
