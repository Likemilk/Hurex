package com.hurex.web.event.service;

import javax.servlet.http.HttpServletRequest; 

import net.sf.json.JSONObject;

import com.hurex.web.event.vo.EventVO;

public interface EventService {
	JSONObject setEventSubmit(EventVO vo,HttpServletRequest request);
	JSONObject getEmailFromAccount();
	JSONObject getEmailToAddress();
	JSONObject getEmailTitle();
	JSONObject getEmailContent();
}
