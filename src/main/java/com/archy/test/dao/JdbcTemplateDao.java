package com.archy.test.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.archy.test.meta.Product;
import com.archy.test.meta.Person;

import freemarker.core.ReturnInstruction.Return;

@Repository
public class JdbcTemplateDao {
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public Person getPerson(String userName, String password) {
		Person person = new Person();
		List<Person> persons = new ArrayList<Person>();
		
		persons = jdbcTemplate.query(String.format("select * from person where userName = '%s'", userName), new RowMapper<Person>() {
			@Override
			public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
				Person person = new Person();
				person.setId(rs.getInt("id"));
				person.setUserName(rs.getString("userName"));
				person.setPassword(rs.getString("password"));
				person.setNickName(rs.getString("nickName"));
				person.setUserType(rs.getInt("userType"));
				return person;
			}
		});
		
		// 2.判断，有对象且密码正确--》返回对象，否则返回null
		if (!persons.isEmpty()) {
			person = (Person) persons.get(0);
			if (person.getPassword().equals(password)) {
				return person;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
	
	public int count() {
		return jdbcTemplate.queryForObject("select count(*) from person", Integer.class);
	}
	
	
	public boolean insertGood(Product product) {
		
		int affected = jdbcTemplate.update("INSERT INTO product(price,title,icon,abstract,text)"
				+ " VALUES(?,?,?,?,?)", product.getPrice()
				,product.getTitle(),product.getIcon(),product.getSummary(),product.getText());
		if (affected != 0) {
			return true;
		} else {
			return false;
		}
	}
	
	
	// 读取 products 列表
	public List<Product> getProducts() {
		Product product = new Product();
		
		List<Product> products = jdbcTemplate.query("select * from product", new RowMapper<Product>() {
			@Override
			public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
				Product product = new Product();
				product.setId(rs.getInt("id"));
				product.setPrice(rs.getInt("price"));
				product.setTitle(rs.getString("title"));
				product.setIcon(rs.getString("icon"));   // 获取BLOB需要别的方法
				product.setSummary(rs.getString("abstract"));
				product.setText(rs.getString("text"));
				product.setBuy(rs.getInt("isBuy"));
				product.setSell(rs.getInt("isSell"));
				return product;
			}
		});
		
		if (!products.isEmpty()) { 
			return products;
		} else {
			return null;
		}
	}
	
	public Product getProductById(int id) {
		final Product product = new Product();
		
		jdbcTemplate.query(String.format("select * from product where id = %d", id), new RowMapper<Product>() {
			
			@Override
			public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
				product.setId(rs.getInt("id"));
				product.setPrice(rs.getInt("price"));
				product.setTitle(rs.getString("title"));
				product.setIcon(rs.getString("icon"));   
				product.setSummary(rs.getString("abstract"));
				product.setText(rs.getString("text"));
				product.setBuy(rs.getInt("isBuy"));
				product.setSell(rs.getInt("isSell"));
				return product;
			}
			
		});
		
		return product;
	}

	public Product getProductByTitle(String title) {
		Product product = new Product();
		List<Product> products = new ArrayList<Product>();
		products = jdbcTemplate.query(String.format("select * from product where title = '%s'", title), new RowMapper<Product>() {
			
			@Override
			public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
				Product product = new Product();
				product.setId(rs.getInt("id"));
				product.setPrice(rs.getInt("price"));
				product.setTitle(rs.getString("title"));
				product.setIcon(rs.getString("icon"));   
				product.setSummary(rs.getString("abstract"));
				product.setText(rs.getString("text"));
				product.setBuy(rs.getInt("isBuy"));
				product.setSell(rs.getInt("isSell"));
				return product;
			}
			
		});
		product = products.get(0);
		return product;
	}
	
	public boolean updateProduct(Product product, int id) {
		
		int affected = jdbcTemplate.update("UPDATE product SET price=?,title=?,icon=?,abstract=?,text=? WHERE id = ?", product.getPrice()
				,product.getTitle(),product.getIcon(),product.getSummary(),product.getText(), id);
		if (affected != 0) {
			return true;
		} else {
			return false;
		}
	}

	public boolean deleteProductById(int number) {
		int affected = jdbcTemplate.update("DELETE FROM `product` WHERE `id` = ?", number);
		if (affected != 0 ) {
			return true;
		} else {
			return false;
		}
	}
}
