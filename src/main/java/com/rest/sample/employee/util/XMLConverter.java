package com.rest.sample.employee.util;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;

import com.rest.sample.employee.model.User;

public class XMLConverter {

	public static void main(String[] args) {

		//System.out.println("Inside XMLConverter  ");	

		try {
			JAXBContext context = JAXBContext.newInstance(User.class);
			Marshaller marshaller = context.createMarshaller();
			try {
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			} catch (PropertyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// write XML to `StringWriter`
			StringWriter sw = new StringWriter();
			User pojo = new User();
			pojo.setId(100);
			pojo.setName("Ramesh");
			pojo.setEmailaddress("smarita@domain.com");
			marshaller.marshal(pojo, sw);

			// print the XML
			System.out.println(sw.toString());
		}catch(JAXBException e) {

		}


	}
	
	public String ConvertToXML(User user) {
		
		try {
			JAXBContext context = JAXBContext.newInstance(User.class);
			Marshaller marshaller = context.createMarshaller();
			try {
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			} catch (PropertyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// write XML to `StringWriter`
			StringWriter sw = new StringWriter();
			marshaller.marshal(user, sw);
			// print the XML
			System.out.println(sw.toString());
		}catch(JAXBException e) {

		}

		
		return null;
	}

}