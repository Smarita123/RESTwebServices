package com.rest.sample.employee.service;

import java.util.Comparator;

import com.rest.sample.employee.model.User;

public class NameComparator implements Comparator{
	
	@Override
	public int compare(Object o1, Object o2) {
		
		User user1= (User) o1;
		User user2= (User) o2;
		String username1= user1.getName();
		String username2= user2.getName();
		
		// TODO Auto-generated method stub
		return username1.compareTo(username2);
	}

}
