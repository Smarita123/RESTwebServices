package com.rest.sample.employee.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import com.rest.sample.employee.model.APIResponse;
import com.rest.sample.employee.model.Address;

import com.rest.sample.employee.model.GenericResponse;
import com.rest.sample.employee.model.PaginationResponse;
import com.rest.sample.employee.model.User;


@Service
public class UserService {

	@Autowired
	MessageSource messageSource;


	@Value ("${db.url}")
	private  String JDBC_URL;
	@Value("${db.username}")
	private  String USER_NAME;
	@Value("${db.password}")
	private  String PASSWORD;
	@Value("${db.database.name}")
	private String DB_Name;
	Connection connection;

	private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
	GenericResponse genericResponse = null;
	List<String> suceessMessageList = new ArrayList<>();
	List<String> errorMessageList = new ArrayList<>();
	TimeZone zone ;
	ZonedDateTime zonedDateTime;
	DateFormat requiredFormat ;
	SimpleDateFormat simpleDateTimeFormat;

	public GenericResponse createUser(User user) {

		try {
			System.out.println(JDBC_URL);
			System.out.println(USER_NAME);
			System.out.println(PASSWORD);
			System.out.println(DB_Name);
			connection =DriverManager.getConnection(JDBC_URL, USER_NAME, PASSWORD);
			PreparedStatement preparedStatement=connection.prepareStatement("insert into User (id, name, email, role) values(?,?,?,?);");
			preparedStatement.setInt(1, user.getId());
			preparedStatement.setString(2, user.getName());
			preparedStatement.setString(3, user.getEmailaddress());
			preparedStatement.setString(4, user.getRole().toString());
			LOG.info("***Role: "+user.getRole().toString());
			preparedStatement.execute();
			preparedStatement.close();

		} catch (Exception e) {
			
			// TODO Auto-generated catch block
			String errormessage = messageSource.getMessage("failuremsg", new Object[]{ user.getId(), user.getName() } , new Locale("el"));
			LOG.info("failedmessage {}",errormessage);
			errorMessageList.add(errormessage);
			genericResponse = new GenericResponse(400, "FAILED", errorMessageList);
			e.printStackTrace();
			return genericResponse;
		}

		GenericResponse genericResponse = null;	
		//LOG.info("Userid="+user.getId());
		//LOG.info("UserName="+user.getName());
		//LOG.info("Email Address="+user.getEmailaddress());
		LOG.info(user.toString());		
		String successmessage = messageSource.getMessage("successmsg", new Object[]{ user.getId(), user.getName() } , new Locale("el"));
		LOG.info("successmessage {}",successmessage);
		suceessMessageList.add(successmessage);

		genericResponse = new GenericResponse(201, "SUCCESS", suceessMessageList);

		return genericResponse;

	}



	public ResponseEntity<Collection<User>> getAllUsers() {
		APIResponse apiResponse = null;
		LOG.info("getAllUsers() in Service class called");
		HashMap<Integer, User> map = new HashMap<>();
		Collection<User> listofUsers ;
		try {
			Connection connection =DriverManager.getConnection(JDBC_URL, USER_NAME, PASSWORD);

			String query ="select id, Name, email from user;";
			PreparedStatement statement=connection.prepareStatement(query) ;		
			ResultSet resultSet =statement.executeQuery();

			while (resultSet.next()) {
				User user= new User();
				System.out.println(resultSet.getInt("id"));
				user.setId(resultSet.getInt("id"));
				user.setName(resultSet.getString("Name"));
				user.setEmailaddress(resultSet.getString("email"));				

				map.put(user.getId(), user);
			}
			connection.close();
			listofUsers = map.values(); //all values in hashmap are copied to the 'listofUsers' collection

			return new ResponseEntity<> (listofUsers, HttpStatus.OK);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			String failuremessage = "Error retrieving record from DB";
			apiResponse.setStatusCode("400");
			apiResponse.setFailureMessage(failuremessage);
			return null;
		}



	}



	public ResponseEntity<Collection<User>> getAllUsersPageWise(Integer pageNo, Integer limit) {
		// TODO Auto-generated method stub
		APIResponse apiResponse = null;
		LOG.info("getAllUsersPageWise() in Service class called");
		HashMap<Integer, User> map = new HashMap<>();
		Collection<User> listofUsers ;
		try {
			Connection connection =DriverManager.getConnection(JDBC_URL, USER_NAME, PASSWORD);

			String query ="select id, Name, email from user;";
			PreparedStatement statement=connection.prepareStatement(query) ;		
			ResultSet resultSet =statement.executeQuery();
			int fromRecord= pageNo*limit+1;
			int toRecord= pageNo*limit+limit;	
			while (resultSet.next()) {
				User user= new User();			
				System.out.println(resultSet.getInt("id"));
				user.setId(resultSet.getInt("id"));
				user.setName(resultSet.getString("Name"));
				user.setEmailaddress(resultSet.getString("email"));		
				if (resultSet.getRow() >= fromRecord) {
					if(resultSet.getRow()<=toRecord ) {
						map.put(user.getId(), user);
					}
				}

			}
			connection.close();
			listofUsers = map.values(); //all values in hashmap are copied to the 'listofUsers' collection

			return new ResponseEntity<> (listofUsers, HttpStatus.OK);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			String failuremessage = "Error retrieving record from DB";
			apiResponse.setStatusCode("400");
			apiResponse.setFailureMessage(failuremessage);
			return null;
		}
	}

	public ResponseEntity<PaginationResponse> getAllUsersPageWiseNew(Integer pageNo, Integer limit) {
		// TODO Auto-generated method stub
		APIResponse apiResponse = null;
		LOG.info("getAllUsersPageWiseNew() in Service class called");
		HashMap<Integer, User> map = new HashMap<>();
		ArrayList<User> listofUsers = null  ;
		listofUsers= new ArrayList();
		PaginationResponse paginationResponse = new PaginationResponse();
		try {

			Connection connection =DriverManager.getConnection(JDBC_URL, USER_NAME, PASSWORD);
			int fromRecord= pageNo*limit+1;
			//			int toRecord= fromRecord+limit;	
			String query1 ="select id, Name, email, creationDate from user order by creationDate limit ?,?;";
			PreparedStatement preparedStatement1=connection.prepareStatement(query1) ;		
			preparedStatement1.setInt(1, fromRecord-1);
			preparedStatement1.setInt(2, limit);

			ResultSet resultSet1 =preparedStatement1.executeQuery();
			int pageRecords = 0;

			while (resultSet1.next()) {
				User user= new User();			
				System.out.println(resultSet1.getInt("id"));
				user.setId(resultSet1.getInt("id"));
				user.setName(resultSet1.getString("Name"));
				user.setEmailaddress(resultSet1.getString("email"));	
				//user.setCreationDate(resultSet1.getDate("creationDate"));
				user.setCreationDate(resultSet1.getTimestamp("creationDate").toString());
				listofUsers.add(user);
				pageRecords=resultSet1.getRow();
			}
			//Collection.sort(listofUsers);
			paginationResponse.setUserList(listofUsers);
			String query2= "select count(*) from user;" ;
			PreparedStatement preparedStatement2 = connection.prepareStatement(query2);
			ResultSet resultSet2 = preparedStatement2.executeQuery();
			//int total=0;
			while (resultSet2.next()) {
				//total=resultSet2.getInt("count(*)");
				paginationResponse.setTotalRecords(resultSet2.getInt("count(*)"));
			}
			String successmessage = messageSource.getMessage("pageSuccessMessage", new Object[]{ pageRecords, paginationResponse.getTotalRecords() } , new Locale("el"));

			paginationResponse.setMessage(successmessage);
			LOG.info("successmessage {}",successmessage);

			connection.close();

			return new ResponseEntity<> (paginationResponse, HttpStatus.OK);
			//return new ResponseEntity<> (listofUsers, HttpStatus.OK);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			String failuremessage = "Error retrieving record from DB";
			apiResponse.setStatusCode("400");
			apiResponse.setFailureMessage(failuremessage);
			return null;
		}
	}



	public ResponseEntity<Collection<User>> getAllUsersSortByName() {
		APIResponse apiResponse = null;
		LOG.info("getAllUsersSortByName() in Service class called");
		List<User> listofUsers = new ArrayList<User>() ;
		try {
			Connection connection =DriverManager.getConnection(JDBC_URL, USER_NAME, PASSWORD);

			String query ="select id, Name, email, creationDate from user;";
			PreparedStatement statement=connection.prepareStatement(query) ;		
			ResultSet resultSet =statement.executeQuery();

			while (resultSet.next()) {
				User user= new User();			
				System.out.println(resultSet.getInt("id"));
				user.setId(resultSet.getInt("id"));
				user.setName(resultSet.getString("Name"));
				user.setEmailaddress(resultSet.getString("email"));	
				user.setCreationDate(resultSet.getDate("creationDate").toString());
				listofUsers.add(user);
			}
			connection.close();
			//sort user list using Comparator implemented in User class
			Collections.sort(listofUsers, User.UserNameComparator);
			
			//sort user list using NameComparator class
			//Collections.sort(listofUsers, new NameComparator());
			return new ResponseEntity<> (listofUsers, HttpStatus.OK);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			String failuremessage = "Error retrieving record from DB";
			apiResponse.setStatusCode("400");
			apiResponse.setFailureMessage(failuremessage);
			return null;
		}
	}
	
	public ResponseEntity<Collection<User>> getAllUsersSortByNameUseComparable() {
		APIResponse apiResponse = null;
		LOG.info("getAllUsersSortByNameUseComparable() in Service class called");
		List<User> listofUsers = new ArrayList<User>() ;
		try {
			Connection connection =DriverManager.getConnection(JDBC_URL, USER_NAME, PASSWORD);

			String query ="select id, Name, email, creationDate, role from user;";
			PreparedStatement statement=connection.prepareStatement(query) ;		
			ResultSet resultSet =statement.executeQuery();

			while (resultSet.next()) {
				User user= new User();			
				System.out.println(resultSet.getInt("id"));
				user.setId(resultSet.getInt("id"));
				user.setName(resultSet.getString("Name"));
				user.setEmailaddress(resultSet.getString("email"));	
				Date date=resultSet.getDate("creationDate");
				user.setCreationDate(date.toString());
				user.setRole(resultSet.getString("role"));
				
				//simpleDateTimeFormat= new SimpleDateFormat("dd-MM-yy HH:mm:SS z");
				simpleDateTimeFormat= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				simpleDateTimeFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
				String indiaTime= simpleDateTimeFormat.format(date);	
				user.setCreationDate(indiaTime);
				LOG.info("***indiaTime="+indiaTime);
				
				simpleDateTimeFormat.setTimeZone(TimeZone.getTimeZone("Asia/Dubai"));
				String dubaiTime= simpleDateTimeFormat.format(date);	
				LOG.info("***dubaiTime="+dubaiTime);
				
				simpleDateTimeFormat.setTimeZone(TimeZone.getTimeZone("Europe/London"));
				String ukTime= simpleDateTimeFormat.format(date);		
				LOG.info("***ukTime="+ukTime);											
				
				listofUsers.add(user);
			}
			LOG.info("Present Time Local: "+LocalDateTime.now());
			LOG.info("Present Time Dubai: "+LocalDateTime.now(ZoneId.of("Asia/Dubai")));
			
			//DateTimeFormatter formatter =DateTimeFormatter.ISO_LOCAL_DATE_TIME;
			
			
			LOG.info("Formatted Time Local: "+DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
			LOG.info("Formatted Time Local: "+DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.of(2020, 10, 30, 10, 20, 40)));
			ZoneId zoneId= ZoneId.of("Europe/London");
			LOG.info("Formatted London Time: "+DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now(zoneId)));
					
			String str1= "2020-10-02 11:12:30";
			DateTimeFormatter formatter= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			LocalDateTime localDateTime1= LocalDateTime.parse(str1, formatter);
			LOG.info("Local Date time1= "+localDateTime1);
			
			String str2= "2020-11-02 12:00:30";
			LocalDateTime localDateTime2=LocalDateTime.parse(str2, formatter);
			LOG.info("Local Date time=2 "+localDateTime2);
			//ChronoUnit.YEARS.between(localDateTime1, localDateTime2);
			LOG.info("Difference in Years= "+ChronoUnit.YEARS.between(localDateTime1, localDateTime2));
			LOG.info("Difference in Months= "+ChronoUnit.MONTHS.between(localDateTime1, localDateTime2));
			LOG.info("Difference in Days= "+ChronoUnit.DAYS.between(localDateTime1, localDateTime2));
			//LOG.info("Local Date time= "+localDateTime1.format(DateTimeFormatter.BASIC_ISO_DATE));
			connection.close();
			//sort user list using Comparable
			Collections.sort(listofUsers);
			
			return new ResponseEntity<> (listofUsers, HttpStatus.OK);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			String failuremessage = "Error retrieving record from DB";
			apiResponse.setStatusCode("400");
			apiResponse.setFailureMessage(failuremessage);
			return null;
		}
	}


}
