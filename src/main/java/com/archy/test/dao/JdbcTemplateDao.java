package com.archy.test.dao;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.archy.test.meta.Person;

@Repository
public class JdbcTemplateDao {
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public Person getPerson(String userName, String password) {
		
		return null;
	}
	
	public int count() {
		return jdbcTemplate.queryForObject("select count(*) from person", Integer.class);
	}
}
