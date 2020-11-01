package com.rest.sample.employee.exception;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.rest.sample.employee.model.APIResponse;
import com.rest.sample.employee.model.ValidationResponse;

@ControllerAdvice
public class MethodArgumentExceptionHandler extends ResponseEntityExceptionHandler {
	
	
	@Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {

		System.out.println("handleMethodArgumentNotValid called");
		
		ValidationResponse validationResponse = new ValidationResponse();


		Map<String, Object> body = new LinkedHashMap();
		
        body.put("timestamp", new Date());
        body.put("status", status.value());

        //Get all errors
        /*List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());*/

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        //List<String> errormesssageList = new ArrayList<String>();
        List <ValidationResponse> list= new ArrayList<ValidationResponse>();
        
       for(FieldError fielderror : fieldErrors) {
    	   validationResponse= new ValidationResponse();
        	validationResponse.setField(fielderror.getField());;
        	validationResponse.setError(fielderror.getDefaultMessage());
        	validationResponse.setTimestamp(new Date());
        	validationResponse.setStatusCode(status.value());
        	list.add(validationResponse);
        	System.out.println(validationResponse.getField() + validationResponse.getError());
 
        }
        
        return new ResponseEntity<>(list,HttpStatus.BAD_REQUEST);
    }


}
