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
		List<Person> persons = new ArrayList<>();
		
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
	
	
}
