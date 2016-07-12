package com.archy.test.web.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitterReturnValueHandler;

import com.archy.test.dao.JdbcTemplateDao;
import com.archy.test.meta.Person;
import com.archy.test.utils.MD5;

@Controller
public class BigController {

	@RequestMapping("/")
	public String indexView() {
		return "index";
	}
	
	@RequestMapping("/login")
	public String loginView() {
		return "login";
	}
	
	@RequestMapping("/api/login")
	public String login(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		String userName = request.getParameter("userName");
		String password = MD5.jkdMD5(request.getParameter("password"));  // 获取经过MD5加密后的密码和数据库中的密码作比对

		ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
		JdbcTemplateDao dao = context.getBean("jdbcTemplateDao", JdbcTemplateDao.class);
		Person person = dao.getPerson(userName, password);
		
		//try {
			if (person != null) {
				// 获取数据库数据，验证成功
				request.getSession().setAttribute("user", person);
				return "index";
			} else {
				// 登录失败！
				return "login";
			}
		//}
			//catch (IOException e) {
//			e.printStackTrace();
//		}
	}
	
	@RequestMapping("/count")
	public void count(HttpServletResponse response) {
		response.setCharacterEncoding("utf8");
		ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
		
		JdbcTemplateDao dao = context.getBean("jdbcTemplateDao", JdbcTemplateDao.class);
		int number = dao.count();
		
		try {
			response.getWriter().write("数据库中共有: " + number + " 条数据。");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		((ClassPathXmlApplicationContext) context).close();
	}
	
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request) {
		request.getSession().invalidate();
		return "login";
	}
	
	@RequestMapping("/public")
	public String createGood() {
		return "public";
	}
	
	@RequestMapping(value="/publicSubmit" , method=RequestMethod.POST)
	public String publicSubmit(HttpServletRequest request) {
		int price = Integer.getInteger(request.getParameter("price"));
		String title = request.getParameter("title");
		String icon = request.getParameter("url");
		String summary =request.getParameter("summary");
		String text = request.getParameter("detail");
		return "editSubmit";
	}
}
