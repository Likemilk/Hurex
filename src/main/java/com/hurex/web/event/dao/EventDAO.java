package com.hurex.web.event.dao;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.hurex.web.event.vo.EventVO;

public interface EventDAO {
	JSONObject setEventSubmit(EventVO vo,HttpServletRequest request);
	JSONObject getEmailFromAccount();
	JSONObject getEmailToAddress();
	JSONObject getEmailTitle();
	JSONObject getEmailContent();
	
	JSONObject setUserData(EventVO vo);
	JSONArray getUserList(EventVO vo);
}

