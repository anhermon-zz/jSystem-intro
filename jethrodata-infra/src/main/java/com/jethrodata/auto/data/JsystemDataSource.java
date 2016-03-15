package com.jethrodata.auto.data;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import jsystem.framework.system.SystemObjectImpl;

public class JsystemDataSource extends SystemObjectImpl {
	
	private String driverClassName;
	private String host;
	private String username;
	private String password;
	
	@Override
	public void init() throws Exception {
		super.init();
		DataSource dataSource = new DriverManagerDataSource(driverClassName, host, username, password);
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		this.setJdbcTemplate(jdbcTemplate);
	}
	
	public String getDriverClassName() {
		return driverClassName;
	}
	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	private JdbcTemplate jdbcTemplate;
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public List queryForList(String query) {
		List queryResult = jdbcTemplate.queryForList(query);
		return queryResult;
	};
}
