package com.hurex.web.content;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ContextLoaderListener;

import com.hurex.web.event.dao.EventDAO;
import com.hurex.web.event.dao.EventDAOImpl;
import com.hurex.web.event.service.EventService;


public class ContentListener extends ContextLoaderListener {

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		// TODO Auto-generated method stub
		super.contextDestroyed(event);
		System.out.println("===========================");
		System.out.println("=====Tomcat이 종료됩니다.=======");
		System.out.println("===========================");

	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		// TODO Auto-generated method stub
		super.contextInitialized(event);
		ServletContext app = event.getServletContext();
		String path = app.getContextPath()+"/resources";
		app.setAttribute("CONTEXT_PATH", path);
		app.setAttribute("PATH_JS",path+"/js");
		app.setAttribute("PATH_CSS",path+"/css");
		app.setAttribute("PATH_PLUGIN",path+"/plugin");
		app.setAttribute("PATH_PROPERTY",path+"/property");
		app.setAttribute("PATH_IMAGE",path+"/image");
		System.out.println(app.getAttribute("PATH_IMAGE"));
		System.out.println(app.getAttribute("PATH_IMAGE"));
		System.out.println(app.getAttribute("PATH_IMAGE"));
		System.out.println(app.getAttribute("PATH_IMAGE"));
		System.out.println(app.getAttribute("PATH_IMAGE"));
		System.out.println(app.getAttribute("PATH_IMAGE"));
		System.out.println(app.getAttribute("PATH_IMAGE"));
		
		
		app.setAttribute("PATH_BOOTSTRAP",app.getAttribute("PATH_PLUGIN")+"/bootstrap");
		app.setAttribute("PATH_JQUERY",app.getAttribute("PATH_PLUGIN")+"/jquery");
		app.setAttribute("PATH_JQUERY_MOBILE",app.getAttribute("PATH_PLUGIN")+"/jquery.mobile-1.4.5");
		
		app.setAttribute("RESULT_MSG", ContentKey.RESULT_MSG);
		app.setAttribute("RESULT_SUCCESS", ContentKey.RESULT_SUCCESS);
		app.setAttribute("RESULT_FAIL", ContentKey.RESULT_FAIL);
		app.setAttribute("AJAX_CONNECTION_ERROR", ContentKey.AJAX_CONNECTION_ERROR);
		
	}

}
