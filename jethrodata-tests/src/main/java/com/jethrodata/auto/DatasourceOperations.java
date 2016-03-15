package com.jethrodata.auto;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import jsystem.framework.ParameterProperties;
import jsystem.framework.TestProperties;
import junit.framework.Assert;
import junit.framework.SystemTestCase4;

public class DatasourceOperations extends SystemTestCase4 {
	private Lab lab;
	private String query;
	private String fields;
	private String table;
	private String where;
	@Before
	public void setup() throws Exception {
		lab = (Lab) system.getSystemObject("lab");
	}
	
	@Test
	@TestProperties(name = "Execute query against a datasource", paramsInclude = {"query"})
	public void manualQuery() throws Exception{
		List result = lab.dataSource.queryForList(query);
		report.report(result.toString());
		Assert.assertNotNull(result);
		Assert.assertTrue(result.size() > 0);
	}
	
	@Test
	@TestProperties(name = "Execute query against a datasource", paramsInclude = {"fields, table, where"})
	public void composedQuery() throws Exception{
		query = "SELECT " + (fields != null ? fields : "*") + " FROM " + table + (where != null ? " WHERE " + where : "");  
		manualQuery();
	}
	
	public String getQuery() {
		return query;
	}
	@ParameterProperties(description = "Query to execute")
	public void setQuery(String query) {
		this.query = query;
	}

	public String getWhere() {
		return where;
	}
	@ParameterProperties(description = "Where clause of composed query")
	public void setWhere(String where) {
		this.where = where;
	}
	public String getFields() {
		return fields;
	}
	@ParameterProperties(description = "return fields of co,posed query")
	public void setFields(String fields) {
		this.fields = fields;
	}

	public String getTable() {
		return table;
	}
	
	@ParameterProperties(description = "Table to query for composed query")
	public void setTable(String table) {
		this.table = table;
	}
}
