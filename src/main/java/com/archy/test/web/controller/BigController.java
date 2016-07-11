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

import com.archy.test.dao.JdbcTemplateDao;
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
	public void login(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		String userName = request.getParameter("userName");
		String password = MD5.jkdMD5(request.getParameter("password"));  // 获取经过MD5加密后的密码和数据库中的密码作比对
		
		try {
			response.getWriter().write("用户名：" + userName + "\n" + "密码：" + password);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
}
