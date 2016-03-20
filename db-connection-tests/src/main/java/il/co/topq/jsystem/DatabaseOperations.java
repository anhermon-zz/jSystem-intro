package il.co.topq.jsystem;

import jsystem.extensions.analyzers.compare.NumberCompare;
import jsystem.extensions.analyzers.compare.NumberCompare.compareOption;
import jsystem.framework.ParameterProperties;
import jsystem.framework.TestProperties;
import junit.framework.SystemTestCase4;

import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DataAccessResourceFailureException;


public class DatabaseOperations extends SystemTestCase4 {
	
	private DataBase db;
	
	private String tableName;
	
	private String ColumnName;
	
	private String value;
	
	private int expNumOfRowsInResultset = 0;
	
	private int expNumOfRowsAffected = 0;
	
	private String whereColumnsAndValues;
	
	private String setColumnsAndValues;
	
	private String columns;
	
	private String values;
	
	private boolean and = true;
	
	private String query;
	
	private String tablesNames;
	
	private boolean distinct;
	
	private compareOption compareMethod = compareOption.GREATER;
	
	private String resultSetPropertyName = "db.resultSet";
	
	@Before
	public void setUp() throws Exception {
		db = (DataBase) system.getSystemObject("db");
	}
	
	@Test
	public void testConnectDb() throws Exception {
		
	}
	
	/**
	 * UPDATE ${tableName} SET ${setColumnsAndValues} WHERE ${whereColumnsAndValues} with ${and} logic between ${whereColumnsAndValues} and
	 * asserts that actual number of rows affected is ${compareMethod} than/to ${expNumOfRowsAffected}. <br>
	 * Use \; to disable delimiters
	 * 
	 * @throws Exception
	 */
	@TestProperties(name = "UPDATE ${tableName} SET ${setColumnsAndValues} WHERE ${whereColumnsAndValues}", paramsInclude = { "tableName",
			"whereColumnsAndValues", "setColumnsAndValues", "and", "expNumOfRowsAffected", "compareMethod" })
	@Test
	public void testUpdateQuery() throws Exception {
		db.update(tableName, whereColumnsAndValues, setColumnsAndValues, and);
		db.analyze(new NumberCompare(compareMethod, expNumOfRowsAffected, 0));
	}
	
	/**
	 * SELECT ${distinct} ${columns} FROM ${tablesNames} WHERE ${whereColumnsAndValues} with ${and} logic between ${whereColumnsAndValues}
	 * and asserts that actual number of rows returned is ${compareMethod} than/to ${expNumOfRowsInResultset}.<br>
	 * <br>
	 * Use \; to disable delimiters<br>
	 * Returned the resultSet without the headers as string.<br>
	 * <br>
	 * <b>Returned Property</b><br>
	 * db.resultSet
	 * 
	 * @throws Exception
	 */
	@TestProperties(name = "SELECT ${columns} FROM ${tablesNames} WHERE ${whereColumnsAndValues}", paramsInclude = { "columns", "distinct",
			"tablesNames", "whereColumnsAndValues", "expNumOfRowsInResultset", "and", "compareMethod", "resultSetPropertyName" })
	@Test
	public void testSelectQuery() throws Exception {
		db.select(tablesNames, columns, whereColumnsAndValues, and, distinct);
		db.analyze(new NumberCompare(compareMethod, expNumOfRowsInResultset, 0));
		report.report(db.getResultSetString());
	}
	
	/**
	 * DELETE FROM ${tableName} WHERE ${whereColumnsAndValues} with ${and} logic between ${whereColumnsAndValues} and asserts that actual
	 * number of rows affected is ${compareMethod} than/to ${expNumOfRowsAffected}. <br>
	 * Use \; to disable delimiters
	 * 
	 * @throws Exception
	 */
	@TestProperties(name = "DELETE FROM ${tableName} WHERE ${whereColumnsAndValues}", paramsInclude = { "tableName",
			"whereColumnsAndValues", "and", "expNumOfRowsAffected", "compareMethod" })
	@Test
	public void testDeleteQuery() throws Exception {
		db.delete(tableName, whereColumnsAndValues, and);
		db.analyze(new NumberCompare(compareMethod, expNumOfRowsAffected, 0));
	}
	
	/**
	 * INSERT INTO ${tableName} (${columns}) VALUES (${values}) and asserts that actual number of rows affected is ${compareMethod} than/to
	 * ${expNumOfRowsAffected}. <br>
	 * Use \; to disable delimiters
	 * 
	 * @throws Exception
	 */
	@TestProperties(name = "INSERT INTO ${tableName} (${columns}) VALUES (${values})", paramsInclude = { "tableName", "columns", "values",
			"expNumOfRowsAffected", "compareMethod" })
	@Test
	public void testInsertQuery() throws Exception {
		db.insert(tableName, columns, values);
		db.analyze(new NumberCompare(compareMethod, expNumOfRowsAffected, 0));
	}
	
	/**
	 * Execute Open SQL Query ${query}. and asserts that actual number of rows affected is ${compareMethod} than/to ${expNumOfRowsAffected}. <br>
	 * Use \; to disable delimiters
	 * 
	 * @throws Exception
	 */
	@TestProperties(name = "OPEN SQL QUERY ${query}", paramsInclude = { "query", "expNumOfRowsAffected", "compareMethod" })
	@Test
	public void testQuery() throws Exception {
		db.query(query);
		db.analyze(new NumberCompare(compareMethod, expNumOfRowsAffected, 0));
	}
	
	public String getTableName() {
		return tableName;
	}
	
	@ParameterProperties(description = "Table name")
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public String getColumnName() {
		return ColumnName;
	}
	
	public void setColumnName(String columnName) {
		ColumnName = columnName;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public int getExpNumOfRowsInResultset() {
		return expNumOfRowsInResultset;
	}
	
	@ParameterProperties(description = "Expected number of rows in resultset")
	public void setExpNumOfRowsInResultset(int expNumOfRowsInResultset) {
		this.expNumOfRowsInResultset = expNumOfRowsInResultset;
	}
	
	public String getWhereColumnsAndValues() {
		return whereColumnsAndValues;
	}
	
	@ParameterProperties(description = "column1='value1';column2='value2'")
	public void setWhereColumnsAndValues(String whereColumnsAndValues) {
		this.whereColumnsAndValues = whereColumnsAndValues;
	}
	
	public String getSetColumnsAndValues() {
		return setColumnsAndValues;
	}
	
	@ParameterProperties(description = "column1='value1';column2='value2'")
	public void setSetColumnsAndValues(String setColumnsAndValues) {
		this.setColumnsAndValues = setColumnsAndValues;
	}
	
	public String getColumns() {
		return columns;
	}
	
	@ParameterProperties(description = "column1;column2")
	public void setColumns(String columns) {
		this.columns = columns;
	}
	
	public String getValues() {
		return values;
	}
	
	@ParameterProperties(description = "value1;value2")
	public void setValues(String values) {
		this.values = values;
	}
	
	public boolean isAnd() {
		return and;
	}
	
	@ParameterProperties(description = "logic: true=and false=or")
	public void setAnd(boolean and) {
		this.and = and;
	}
	
	public String getQuery() {
		return query;
	}
	
	@ParameterProperties(description = "SQL Expression")
	public void setQuery(String query) {
		this.query = query;
	}
	
	public int getExpNumOfRowsAffected() {
		return expNumOfRowsAffected;
	}
	
	@ParameterProperties(description = "Expected number of rows affected")
	public void setExpNumOfRowsAffected(int expNumOfRowsAffected) {
		this.expNumOfRowsAffected = expNumOfRowsAffected;
	}
	
	public String getTablesNames() {
		return tablesNames;
	}
	
	@ParameterProperties(description = "table1;table2")
	public void setTablesNames(String tablesNames) {
		this.tablesNames = tablesNames;
	}
	
	public boolean isDistinct() {
		return distinct;
	}
	
	@ParameterProperties(description = "Select distinct - list only the different values")
	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}
	
	@ParameterProperties(description = "Relation between actual and expected")
	public void setCompareMethod(compareOption compareMethod) {
		this.compareMethod = compareMethod;
	}
	
	public compareOption getCompareMethod() {
		return compareMethod;
	}
	
	public String getResultSetPropertyName() {
		return resultSetPropertyName;
	}
	
	public void setResultSetPropertyName(String resultSetPropertyName) {
		this.resultSetPropertyName = resultSetPropertyName;
	}
	
}
