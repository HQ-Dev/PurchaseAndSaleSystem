package com.archy.test.web.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.archy.test.dao.JdbcTemplateDao;
import com.archy.test.meta.Product;
import com.archy.test.meta.Json;
import com.archy.test.meta.Person;
import com.archy.test.utils.MD5;

@Controller
public class BigController {

	@RequestMapping("/")
	public String indexView(HttpServletRequest request) {
		// 读取数据库中 product 表中的所有商品的数据，得到数组 products , 并且添加到 attribute 中。
		ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
		JdbcTemplateDao dao = context.getBean("jdbcTemplateDao", JdbcTemplateDao.class);
		
		List<Product> products = dao.getProducts();
		((ClassPathXmlApplicationContext) context).close();
		if (products != null) {
			request.getSession().setAttribute("productList", products);
			return "index";
		} else {
			request.getSession().setAttribute("productList", products);
			return "index";
		}
	}
	
	@RequestMapping("/login")
	public String loginView() {
		return "login";
	}
	
	@RequestMapping("/api/login")
	@ResponseBody
	public Json login(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		//String password = MD5.jkdMD5(request.getParameter("password"));  // 获取经过MD5加密后的密码和数据库中的密码作比对

		ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
		JdbcTemplateDao dao = context.getBean("jdbcTemplateDao", JdbcTemplateDao.class);
		Person person = dao.getPerson(userName, password);
		((ClassPathXmlApplicationContext) context).close();
		//try {
			if (person != null) {
				// 获取数据库数据，验证成功
				request.getSession().setAttribute("user", person);
				Json json  = new Json(200, "dsa", true);
				return json;
				//return "index";
			} else {
				// 登录失败！
				Json json = new Json(400, "dsad", false);
				return json;
				//return "index";
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
	public String publicSubmit(HttpServletRequest request,@ModelAttribute Product product) throws UnsupportedEncodingException {
		
		// 使用 @ModelAttribute 注解，更加方便，免去了一个个的注入请求参数
		
		ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
		JdbcTemplateDao dao = context.getBean("jdbcTemplateDao", JdbcTemplateDao.class);
		boolean isSuccessed = dao.insertGood(product);
		if (isSuccessed) {
			// 发布商品并存储到数据库成功, 然后从数据库获取数据生成 Product 对象
			try{
				product =  dao.getProductByTitle(product.getTitle());
				request.getSession().setAttribute("new_product", product);
				return "publicSubmit";
			}
			finally {
				((ClassPathXmlApplicationContext) context).close();
			}
		} else {
			((ClassPathXmlApplicationContext) context).close();
			return "publicSubmit";
		}

	}
	
	@RequestMapping("/show")
	public String showProduct(@RequestParam("id") int id, HttpServletRequest request) {
		// 从请求中获取 requestParameter.商品的ID， 通过它从数据库中获得该商品的信息 
		
		ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
		JdbcTemplateDao dao = context.getBean("jdbcTemplateDao", JdbcTemplateDao.class);
		Product product = dao.getProductById(id);
		
		request.getSession().setAttribute("product", product);
		((ClassPathXmlApplicationContext) context).close();
		return "show";
	}
	
	@RequestMapping("/edit")
	public String editProduct(@RequestParam("id") int id) {
		
		return "edit";
	}
	
	@RequestMapping("/editSubmit")
	public String editSubmit(@RequestParam("id") int id, @ModelAttribute Product product,HttpServletRequest request) {
		// 这么多重复代码，应该考虑使用AOP
		ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
		JdbcTemplateDao dao = context.getBean("jdbcTemplateDao", JdbcTemplateDao.class);
		boolean isUpdate = dao.updateProduct(product, id);
		
		if (isUpdate) {
			// 更新成功，则从数据库获取该商品，加入到属性中，然后跳转到是否编辑成功页面
			product = dao.getProductById(id);
			request.getSession().setAttribute("new_product", product);
			((ClassPathXmlApplicationContext) context).close();
			return "editSubmit";
		}else {
			// 直接跳转到 editSubmit.ftl
			((ClassPathXmlApplicationContext) context).close();
			return "editSubmit";
		}
	}
	
	@RequestMapping("/api/delete")
	@ResponseBody
	public Json deleteProduct(@RequestParam("id") Number id,HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int number = id.intValue();
		// 根据 id 删除数据库中的对应 product
		ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");
		JdbcTemplateDao dao = context.getBean("jdbcTemplateDao", JdbcTemplateDao.class);
		dao.deleteProductById(number);
		((ClassPathXmlApplicationContext) context).close();
		Json json = new Json(200, "ss", true);
		return json;
		//request.getRequestDispatcher("/").forward(request, response);
		
	}
}
