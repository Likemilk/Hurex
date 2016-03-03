package com.hurex.web.event.dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.hurex.web.content.ContentKey;
import com.hurex.web.event.vo.EventVO;
import com.hurex.web.mail.WebMail;

@Repository
public class EventDAOImpl implements EventDAO {
	
	private static final String NS = "mapper.com.hurex.web.event.";
	
	@Autowired
	private SqlSession sqlSession;
	
	String pathProperty ;
	
	
	@Override
	public JSONObject setEventSubmit(EventVO vo,HttpServletRequest request) {
		// TODO Auto-generated method stub
		
		JSONObject setUser = this.setUserData(vo);
		if(!setUser.get(ContentKey.RESULT_MSG).equals(ContentKey.RESULT_SUCCESS)){
			return setUser;
		}
		
		
		pathProperty = request.getSession().getServletContext().getRealPath("/")+"/resources/property";
		
		System.out.println(pathProperty);
		
		//JSONObject 객체임.
		JSONObject result = new JSONObject();
		JSONObject emailFrom = getEmailFromAccount();
		JSONObject emailTitle = getEmailTitle();
		JSONObject emailContent = getEmailContent();
		JSONObject emailTo =  getEmailToAddress();
		
		if(!emailFrom.get(ContentKey.RESULT_MSG).equals(ContentKey.RESULT_SUCCESS)){
			return emailFrom;
		}
		if(!emailTo.get(ContentKey.RESULT_MSG).equals(ContentKey.RESULT_SUCCESS)){
			return emailTo;
		}
		if(!emailTitle.get(ContentKey.RESULT_MSG).equals(ContentKey.RESULT_SUCCESS)){
			return emailTitle;
		}
		if(!emailContent.get(ContentKey.RESULT_MSG).equals(ContentKey.RESULT_SUCCESS)){
			return emailContent;
		}
		
		String idEmailFrom = emailFrom.getString("id");
		String emailCompany[] = idEmailFrom.split("@");
		
		Properties p = new Properties(); // 정보를 담을 객체
		if(emailCompany[1].equals("gamil.com")){
			p.put("mail.smtp.host", "smtp.gmail.com"); // 네이버 SMTP
		}else if(emailCompany[1].equals("naver.com")){
			p.put("mail.smtp.host", "smtp.naver.com"); // 네이버 SMTP
		}else{
			result.put(ContentKey.RESULT_MSG, ContentKey.RESULT_EMAIL_NOT_SUPPORT);
			return result;
		}
		p.put("mail.smtp.port", "465");
		p.put("mail.smtp.starttls.enable", "true");
		p.put("mail.smtp.auth", "true");
		p.put("mail.smtp.debug", "true");
		p.put("mail.smtp.socketFactory.port", "465");
		p.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		p.put("mail.smtp.socketFactory.fallback", "false");

		try {
			Authenticator auth = new WebMail(emailCompany[0],emailFrom.getString("password"));
			Session ses = Session.getInstance(p, auth);
			ses.setDebug(true);
			MimeMessage msg = new MimeMessage(ses); // 메일의 내용을 담을 객체
			
			String title = emailTitle.getString(ContentKey.EMAIL_TITLE).replace("{0}", vo.getDate()).replace("{1}", vo.getWeb());
			
			//제목
			msg.setSubject(title);
			msg.setSubject(MimeUtility.encodeText(title, "EUC-KR", "B"));
			msg.setSubject(MimeUtility.encodeText(title, "UTF-8", "B"));
			
			
			String content = emailContent.getString(ContentKey.EMAIL_CONTENT).replace("{0}",vo.getName());
			content = content.replace("{1}",vo.getTel());
			content = content.replace("{2}",vo.getDate());	
			content = content.replace("{3}",vo.getWeb());	
			
			msg.setContent(content,"text/html;charset=UTF-8"); // 내용과 인코딩
			
			Address fromAddr = new InternetAddress(emailFrom.getString("id"));
			msg.setFrom(fromAddr); // 보내는 사람
			
			for(int i=0;i<emailTo.getJSONArray(ContentKey.EMAIL_TO).size();i++){
				Address toAddr = new InternetAddress(emailTo.getJSONArray(ContentKey.EMAIL_TO).getString(i));
				msg.addRecipient(Message.RecipientType.TO, toAddr); // 받는 사람
			}
			Transport.send(msg); // 전송
			
		} catch (Exception e) {
			e.printStackTrace();
			result.put(ContentKey.RESULT_MSG, ContentKey.RESULT_EMAIL_TRANSPORT_ERROR);
			// 오류 발생시 뒤로 돌아가도록
			return result;
		}
		
		result.put(ContentKey.RESULT_MSG, ContentKey.RESULT_SUCCESS);
		return result;
		
		
	}

	@Override
	public JSONObject getEmailFromAccount() {
		// TODO Auto-generated method stub
		JSONObject data = new JSONObject();
		try {
			File f = new File(pathProperty+"/emailFrom.txt");
			FileReader reader = new FileReader(f);
			BufferedReader bufReader = new BufferedReader(reader);
			String col[];
			String line = "";
			while ((line = bufReader.readLine()) != null) {
				System.out.println("=============getEmailFromAccount()==============");
				System.out.println(line);
				System.out.println("=====================================");

				col = line.split(":");
				if (col[0].equals("id")) {
					data.put("id", col[1]);
				} else if (col[0].equals("password")) {
					data.put("password", col[1]);
				} else {
					throw new Exception("에러 발생");
				}
			}
			
			reader.close();
			bufReader.close();
		} catch (Exception e) {
			e.printStackTrace();
			data.put(ContentKey.RESULT_MSG, ContentKey.RESULT_EMAIL_FROM_ERROR);
			return data;
		}
		data.put(ContentKey.RESULT_MSG, ContentKey.RESULT_SUCCESS);
		return data;
	}

	@Override
	public JSONObject getEmailToAddress() {
		// TODO Auto-generated method stub
		JSONObject data = new JSONObject();
		JSONArray ary = new JSONArray();
		try {
			File f = new File(pathProperty+"/emailTo.txt");
			FileReader reader = new FileReader(f);
			BufferedReader bufReader = new BufferedReader(reader);
			String col[];
			String line = "";
			while ((line = bufReader.readLine()) != null) {
				System.out.println("================getEmailToAddress()================");
				System.out.println(line);
				System.out.println("=====================================");

				col = line.split(":");
				if (col[0].equals("to")) {
					ary.add(col[1]);
				} else {
					//throw new Exception("에러 발생");
				}
			}
			reader.close();
			bufReader.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			data.put(ContentKey.RESULT_MSG, ContentKey.RESULT_EMAIL_TO_ERROR);
			return data;
		}
		data.put(ContentKey.EMAIL_TO, ary);
		data.put(ContentKey.RESULT_MSG, ContentKey.RESULT_SUCCESS);
		return data;
	}

	@Override
	public JSONObject getEmailTitle() {
		// TODO Auto-generated method stub
		JSONObject data = new JSONObject();
		StringBuffer buffer = new StringBuffer();
		try {
			File f = new File(pathProperty+"/emailTitle.txt");
			BufferedReader bufReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(f), "UTF-8"));
			
			String line = "";
			
			while ((line = bufReader.readLine()) != null) {
				System.out.println("============= getEmailTitle()=============");
				System.out.println(line);
				System.out.println("=====================================");
				buffer.append(line+"\n");
			}
			
			bufReader.close();
		} catch (Exception e) {

			e.printStackTrace();
			data.put(ContentKey.RESULT_MSG, ContentKey.RESULT_EMAIL_TITLE_ERROR);
			return data;
		}
		data.put(ContentKey.RESULT_MSG, ContentKey.RESULT_SUCCESS);
		data.put(ContentKey.EMAIL_TITLE, buffer.toString());
		
		return data;
	}

	@Override
	public JSONObject getEmailContent() {
		// TODO Auto-generated method stub
		JSONObject data = new JSONObject();
		StringBuffer buffer = new StringBuffer();
		try {
			File f = new File(pathProperty+"/emailContent.html");
			
			BufferedReader bufReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(f), "UTF-8"));
			
			String line = "";
			
			while ((line = bufReader.readLine()) != null) {
				System.out.println("=============getEmailContent(=============");
				System.out.println(line);
				System.out.println("=====================================");
				buffer.append(line+"\n");
			}
			
			bufReader.close();
		} catch (Exception e) {
			e.printStackTrace();
			data.put(ContentKey.RESULT_MSG, ContentKey.RESULT_EMAIL_CONTENT_ERROR);
			return data;
		}
		data.put(ContentKey.RESULT_MSG, ContentKey.RESULT_SUCCESS);
		data.put(ContentKey.EMAIL_CONTENT, buffer.toString());
		
		return data;
	}
	

	@Override
	public JSONObject setUserData(EventVO vo) {
		// TODO Auto-generated method stub
		JSONObject data = new JSONObject();
		try{
			if(sqlSession.insert(NS+"setUserData",vo)!=1){
				data.put(ContentKey.RESULT_MSG, ContentKey.EXIST_USER);	
				return data;
			}
		}catch(Exception e){
			e.printStackTrace();
			data.put(ContentKey.RESULT_MSG, ContentKey.EXIST_USER);	
			return data;
		}
		data.put(ContentKey.RESULT_MSG, ContentKey.RESULT_SUCCESS);
		
		return data;
	}

	@Override
	public JSONArray getUserList(EventVO vo) {
		// TODO Auto-generated method stub
		return JSONArray.fromObject(sqlSession.selectList(NS+"getUserList", vo));
	}

	

}
