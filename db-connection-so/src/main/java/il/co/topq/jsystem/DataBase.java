package il.co.topq.jsystem;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import jsystem.framework.system.SystemObjectImpl;

/**
 * Allows different operations on data bases.<br>
 * Exposes to the automation all the basic SQL quieries types like:<br>
 * SELECT, INSERT, UPDATE and DELETE.<br>
 * Also, it allowed to create and run open queries.
 * 
 * @author itai
 */
public class DataBase extends SystemObjectImpl {
	
	public static enum DbType {
		MySql, Oracle, SyBase, MsSql
	}
	
	private static final String EOL = System.getProperty("line.separator");
	
	private DbType type;
	
	private boolean connectOnInit = true;
	
	private String user = "";
	
	private String password = "";
	
	private String host;
	
	private String port;
	
	private String sid;
	
	private String schema;
	
	private String resultSetString;
	
	protected Connection conn;
	
	@Override
	public void init() throws Exception {
		super.init();
		if(isConnectOnInit()){
			connect();
		}
	}
	
	public void connect() throws Exception{
		
		DriverManager.setLoginTimeout(60);
		String connectionStr = null;
		
		switch (type) {
			case Oracle:
				DriverManager.registerDriver( (Driver) Class.forName("oracle.jdbc.driver.OracleDriver").newInstance());
				connectionStr = String.format("jdbc:oracle:thin:@%s:%s:%s", host, port, sid);
				break;
			case MySql:
				DriverManager.registerDriver( (Driver) Class.forName("com.mysql.jdbc.Driver").newInstance());
//				jdbc:mysql://192.168.150.89:3306/
				connectionStr = new String("jdbc:mysql://"+host+":"+port+"/" + schema);
				break;
			case SyBase:
				DriverManager.registerDriver( (Driver) Class.forName("com.sybase.jdbc3.jdbc.SybDriver").newInstance());
				connectionStr = "jdbc:sybase:Tds:" + getHost() + ":" + getPort();
				break;
			case MsSql:
				DriverManager.registerDriver( (Driver) Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance());
				connectionStr = String.format("jdbc:sqlserver://%s:%s;database=%s;user=%s;password=%s", getHost(), getPort(), getSchema(), getUser(), getPassword());
				break;
		}
		// Attempt to connect to a driver.
		report(connectionStr);
		conn = DriverManager.getConnection(connectionStr, user, password);
		if (!conn.isClosed()) {
			report.report("Successfully connected to SQL Server");
		} else {
			throw new Exception("Failed to connect to SQL Server");
		}
	}
	
	public void close() {
		try {
			if (conn != null){
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		super.close();
	}
	
	/**
	 * open SQL query
	 * 
	 * @param query
	 * @return Number of rows affected or number of rows in resultSet
	 * @throws Exception
	 */
	public int query(String query) throws Exception {
		assertNotEmpty(query);
		if (query.toLowerCase().contains("select")) {
			executeSelectStatement(query);
			return (Integer) getTestAgainstObject();
		} else {
			return executeUpdateStatement(query);
		}
	}
	
	/**
	 * INSERT INTO ${tableName} (${columns}) VALUES (${values})
	 * 
	 * @param tableName
	 * @param columns
	 *        column1;column2
	 * @param values
	 *        value1;value2
	 * @return Number of affected Rows
	 * @throws Exception
	 */
	public int insert(String tableName, String columns, String values) throws Exception {
		assertNotEmpty(tableName);
		assertNotEmpty(values);
		String query = null;
		if (columns == null || columns.isEmpty()) {
			query = String.format("INSERT INTO %s VALUES (%s)", tableName, replaceDelimiters(values));
		} else {
			query = String.format("INSERT INTO %s (%s) VALUES (%s)", tableName, replaceDelimiters(columns), replaceDelimiters(values));
		}
		return executeUpdateStatementAndReport(tableName, query);
	}
	
	public int insert(String tableName, String values) throws Exception {
		return insert(tableName, null, values);
	}
	
	/**
	 * UPDATE ${tableName} SET ${setColumnsAndValues} WHERE ${whereColumnsAndValues} with ${and} logic between ${whereColumnsAndValues}
	 * 
	 * @param tableName
	 * @param whereColumnsAndValues
	 *        column1='value1';colume2='value2'
	 * @param setColumnsAndValues
	 *        column1='value1';colume2='value2'
	 * @param and
	 *        logic between ${whereColumnsAndValues}
	 * @return Number of rows affected
	 * @throws Exception
	 */
	public int update(String tableName, String whereColumnsAndValues, String setColumnsAndValues, boolean and) throws Exception {
		assertNotEmpty(tableName);
		assertNotEmpty(whereColumnsAndValues);
		assertNotEmpty(setColumnsAndValues);
		String query = String.format("UPDATE %s SET %s %s", tableName, replaceDelimiters(setColumnsAndValues),
				formatWhereColumnsAndValue(whereColumnsAndValues, and));
		return executeUpdateStatementAndReport(tableName, query);
	}
	
	public int update(String tableName, String whereColumnsAndValues, String setColumnsAndValues) throws Exception {
		return update(tableName, whereColumnsAndValues, setColumnsAndValues, true);
	}
	
	/**
	 * DELETE FROM ${tableName} WHERE ${whereColumnsAndValues} with ${and} logic between ${whereColumnsAndValues}
	 * 
	 * @param tableName
	 * @param whereColumnsAndValues
	 *        column1='value1';colume2='value2'
	 * @param and
	 *        logic between ${whereColumnsAndValues}
	 * @return Number of rows affected
	 * @throws Exception
	 */
	public int delete(String tableName, String whereColumnsAndValues, boolean and) throws Exception {
		assertNotEmpty(tableName);
		assertNotEmpty(whereColumnsAndValues);
		String query = new String("DELETE FROM " + tableName + " " + formatWhereColumnsAndValue(whereColumnsAndValues, and));
		return executeUpdateStatementAndReport(tableName, query);
	}
	
	public int delete(String tableName, String whereColumnsAndValues) throws Exception {
		return delete(tableName, whereColumnsAndValues, true);
	}
	
	/**
	 * SELECT ${distinct} ${columns} FROM ${tablesNames} WHERE ${whereColumnsAndValues} with ${and} logic between ${whereColumnsAndValues}
	 * 
	 * @param tablesNames
	 *        one or more tables.
	 * @param columns
	 *        Columns to include in ResultSet
	 * @param whereColumnsAndValues
	 *        column1='value1';colume2='value2'
	 * @param and
	 *        logic between ${whereColumnsAndValues}
	 * @param distinct
	 * @return Result set
	 * @throws Exception
	 */
	public String select(String tablesNames, String columns, String whereColumnsAndValues, boolean and, boolean distinct) throws Exception {
		assertNotEmpty(tablesNames);
		StringBuffer query = new StringBuffer("SELECT ");
		if (distinct) {
			query.append("DISTINCT ");
		}
		if (columns != null && !columns.isEmpty()) {
			query.append(replaceDelimiters(columns) + " ");
		} else {
			query.append("* ");
		}
		query.append("FROM " + replaceDelimiters(tablesNames) + " " + formatWhereColumnsAndValue(whereColumnsAndValues, and));
		return executeSelectStatement(query.toString());
	}
	
	public String executeSelectStatement(String query) throws SQLException {
		report("Excecuting query : " + query);
		Statement stmt = conn.createStatement();
		ResultSet rset = stmt.executeQuery(query.toString());
		int rowCount = reportResultSet(rset);
		setTestAgainstObject(rowCount);
		stmt.close();
		return resultSetString.toString().trim();
	}
	
	public ResultSet getQueryResultSet(String query) throws SQLException {
		report("Excecuting query : " + query);
		Statement stmt = conn.createStatement();
		ResultSet rset = stmt.executeQuery(query.toString());
		stmt.close();
		return rset;
	}
	
	public String select(String tableName) throws Exception {
		return select(tableName, "");
	}
	
	public String select(String tablesNames, String whereColumnsAndValues) throws Exception {
		return select(tablesNames, "* ", whereColumnsAndValues, true, false);
	}
	
	public String select(String tablesNames, String whereColumnsAndValues, boolean and) throws Exception {
		return select(tablesNames, "* ", whereColumnsAndValues, and, false);
	}
	
	public String select(String tablesNames, String whereColumnsAndValues, boolean and, boolean distinct) throws Exception {
		return select(tablesNames, "* ", whereColumnsAndValues, and, distinct);
	}
	
	/**
	 * run execute update. This method is used for every query from that changes the data base. It also reports the table before the change
	 * and after the change.
	 * 
	 * @param tableName
	 * @param query
	 * @return Number of rows affected. Also, sets the result as test against object.
	 * @throws SQLException
	 * @throws Exception
	 */
	private int executeUpdateStatementAndReport(String tableName, String query) throws SQLException, Exception {
		select(tableName);
		int result = executeUpdateStatement(query);
		select(tableName);
		setTestAgainstObject(result);
		return result;
	}
	
	private int executeUpdateStatement(String query) throws SQLException {
		report.report("Excecuting update : " + query);
		Statement stmt = conn.createStatement();
		int result = stmt.executeUpdate(query);
		stmt.close();
		setTestAgainstObject(result);
		return result;
	}
	
	/**
	 * Reports the resultSet as HTML table<br>
	 * It also fills the resultSetString.
	 * 
	 * @param rs
	 * @return Number of rows in resultSet
	 * @throws SQLException
	 */
	private int reportResultSet(ResultSet rs) throws SQLException {
		int rowCount = 0;
		StringBuffer sb = new StringBuffer();
		StringBuffer resultSetSb = new StringBuffer();
		sb.append("<P ALIGN='center'><TABLE BORDER=1>" + EOL);
		ResultSetMetaData rsmd = rs.getMetaData();
		if (rsmd == null) {
			return 0;
		}
		int columnCount = rsmd.getColumnCount();
		// table header
		sb.append("<TR>" + EOL);
		for (int i = 0; i < columnCount; i++) {
			sb.append("<TH>" + rsmd.getColumnLabel(i + 1) + "</TH>" + EOL);
		}
		sb.append("</TR>" + EOL);
		// the data
		while (rs.next()) {
			rowCount++;
			if (rowCount > 1) {
				resultSetSb.append(EOL);
			}
			sb.append("<TR>" + EOL);
			for (int i = 0; i < columnCount; i++) {
				String tdValue = rs.getString(i + 1);
				if (i > 0) {
					resultSetSb.append(" ");
				}
				resultSetSb.append(tdValue);
				if (tdValue == null || tdValue.isEmpty() || tdValue.trim().equals("null")) {
					tdValue = new String("&nbsp;");
				}
				sb.append("<TD>" + tdValue + "</TD>" + EOL);
			}
			sb.append("</TR>" + EOL);
		}
		sb.append("</TABLE></P>" + EOL);
		resultSetString = resultSetSb.toString();
		report.reportHtml("ResultSet", sb.toString(), true);
		return rowCount;
	}
	
	private String formatWhereColumnsAndValue(String whereColumnsAndValues, boolean and) {
		if (whereColumnsAndValues == null || whereColumnsAndValues.isEmpty()) {
			return " ";
		}
		if (whereColumnsAndValues.indexOf(";") == -1) {
			return new String("WHERE " + whereColumnsAndValues + " ");
		}
		StringBuffer sb = new StringBuffer("WHERE ");
		String[] whereColumnsAndValuesPairs = whereColumnsAndValues.split("(?<!\\\\);");
		sb.append(whereColumnsAndValuesPairs[0].replaceAll("\\\\;", ";") + " ");
		for (int i = 1; i < whereColumnsAndValuesPairs.length; i++) {
			sb.append((and ? "AND " : " OR "));
			sb.append(whereColumnsAndValuesPairs[i].replaceAll("\\\\;", ";") + " ");
		}
		return sb.toString();
	}
	
	private void assertNotEmpty(String str) throws Exception {
		if (str == null || str.isEmpty()) {
			throw new Exception("Variable " + str + " is null or empty");
		}
	}
	
	/**
	 * Replaces all delimiters ";" with delimiters ",". the exception is delimiter with "\" prefix. in this case, only the prefix will be
	 * deleted.
	 * 
	 * @param str
	 * @return String with replaced delimiters
	 */
	private String replaceDelimiters(String str) {
		return str.replaceAll("(?<!\\\\);", ",").replaceAll("\\\\;", ";");
		
	}
	
	public String getUser() {
		return user;
	}
	
	public void setUser(String user) {
		this.user = user;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getHost() {
		return host;
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public String getPort() {
		return port;
	}
	
	public void setPort(String port) {
		this.port = port;
	}
	
	public DbType getType() {
		return type;
	}

	public void setType(DbType type) {
		this.type = type;
	}

	public String getSid() {
		return sid;
	}
	
	public void setSid(String sid) {
		this.sid = sid;
	}
	
	public String getSchema() {
		return schema;
	}
	
	public void setSchema(String schema) {
		this.schema = schema;
	}
	
	public String getResultSetString() {
		return resultSetString;
	}

	/**
	 * @return the connectOnInit
	 */
	public boolean isConnectOnInit() {
		return connectOnInit;
	}

	/**
	 * @param connectOnInit the connectOnInit to set
	 */
	public void setConnectOnInit(boolean connectOnInit) {
		this.connectOnInit = connectOnInit;
	}
	
}
