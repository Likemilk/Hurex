package com.hurex.web.event.controller;

import java.util.Locale;

import javax.mail.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hurex.web.content.ContentKey;
import com.hurex.web.event.service.EventService;
import com.hurex.web.event.vo.EventVO;

@Controller
public class EventController {

	private static final Logger logger = LoggerFactory.getLogger(EventController.class);

	@Autowired
	private EventService eventService;
		
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String homeEvent(Locale locale, Model model) {
		
		return "/index.html";
	}
	
	@RequestMapping(value = "/event.submit", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject submitEventJson(EventVO vo,HttpServletRequest request,HttpSession session) {
		System.out.println(vo.getName());
		System.out.println(vo.getTel());
		System.out.println(vo.getWeb());
		
		if(vo.getCheck().equals("T")){
			return eventService.setEventSubmit(vo,request);
		}else{
			return (JSONObject) new JSONObject().put(ContentKey.RESULT_MSG, "약관에 동의해주셔야 합니다.");	
		}
	}

}
