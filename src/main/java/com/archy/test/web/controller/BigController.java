package com.archy.test.web.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
