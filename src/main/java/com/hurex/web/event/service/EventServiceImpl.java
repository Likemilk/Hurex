package com.hurex.web.event.service;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hurex.web.content.ContentKey;
import com.hurex.web.event.dao.EventDAO;
import com.hurex.web.event.vo.EventVO;

@Service
public class EventServiceImpl implements EventService {
	
	@Autowired
	private EventDAO eventDAO;

	@Override
	public JSONObject setEventSubmit(EventVO vo, HttpServletRequest request) {
		// TODO Auto-generated method stub
		JSONObject result = new JSONObject();
		String name, tel,web;
		
		if (!(vo.getName() == null || vo.getName().equals(""))) {
			name = checkString(vo.getName());

			System.out.println(name);
			System.out.println(name);
			System.out.println(name);
		} else {
			result.put(ContentKey.RESULT_MSG, ContentKey.INPUT_NAME_ERROR);
			return result;
		}

		if (!(vo.getTel() == null || vo.getTel().equals(""))) {
			tel = checkString(vo.getTel());
			if(tel.length()>=9&&tel.length()<=12){
				try {
					System.out.println(tel);
					System.out.println(tel);
					System.out.println(tel);
					int temp = Integer.parseInt(tel);
				} catch (Exception e) {
					e.printStackTrace();
					result.put(ContentKey.RESULT_MSG, ContentKey.INPUT_TEL_ERROR);
					return result;
				}
			}else{
				result.put(ContentKey.RESULT_MSG, ContentKey.INPUT_TEL_ERROR);
				return result;
			}
		} else {
			result.put(ContentKey.RESULT_MSG, ContentKey.INPUT_TEL_ERROR);
			return result;
		}
		
		if (!(vo.getWeb() == null || vo.getWeb().equals(""))) {
			web = checkString(vo.getWeb());

		} else {
			result.put(ContentKey.RESULT_MSG, ContentKey.WEB_NAME_ERROR);
			return result;
		}
		
		
		Date nowDate = new Date();
		DateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String tempDate = sdFormat.format(nowDate);

		vo.setDate(tempDate);
		vo.setName(name);
		vo.setWeb(web);
		vo.setTel(tel);

		return eventDAO.setEventSubmit(vo, request);
	}

	@Override
	public JSONObject getEmailFromAccount() {
		// TODO Auto-generated method stub
		return eventDAO.getEmailFromAccount();
	}

	@Override
	public JSONObject getEmailToAddress() {
		// TODO Auto-generated method stub
		return eventDAO.getEmailToAddress();
	}

	@Override
	public JSONObject getEmailTitle() {
		// TODO Auto-generated method stub
		return eventDAO.getEmailTitle();
	}

	@Override
	public JSONObject getEmailContent() {
		// TODO Auto-generated method stub
		return eventDAO.getEmailContent();
	}

	// 이상한 인젝션 방어 어짜피 인젝션 안하겠지만..
	public String checkString(String arg) {
		arg.replace(" ", "");
		arg.replace("!", "");
		arg.replace("--", "");
		arg.replace("@", "");
		arg.replace("#", "");
		arg.replace("$", "");
		arg.replace("%", "");
		arg.replace("^", "");
		arg.replace("*", "");
		arg.replace("(", "");
		arg.replace(")", "");
		arg.replace("[", "");
		arg.replace("]", "");
		arg.replace("{", "");
		arg.replace("}", "");
		arg.replace(".", "");
		arg.replace(">", "");
		arg.replace("<", "");
		arg.replace(",", "");
		arg.replace("/", "");
		arg.replace("?", "");
		arg.replace("\\", "");
		arg.replace("|", "");
		arg.replace("`", "");
		arg.replace("=", "");
		arg.replace("-", "");

		return arg;
	}
}
