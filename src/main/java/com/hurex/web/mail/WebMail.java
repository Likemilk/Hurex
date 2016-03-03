package com.hurex.web.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;

import com.hurex.web.event.service.EventService;

public class WebMail extends Authenticator{
	@Autowired
	private EventService eventService;
	String id="";
	String password="";
	public WebMail(String id,String password){
		this.id=id;
		this.password=password;
	}
	
	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		// TODO Auto-generated method stub
		return new PasswordAuthentication(id,password);
	}

}
