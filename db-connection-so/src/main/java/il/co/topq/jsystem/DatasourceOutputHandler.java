package il.co.topq.jsystem;

import java.sql.ResultSet;

import jsystem.framework.system.SystemObjectImpl;

public abstract class DatasourceOutputHandler extends SystemObjectImpl {

	public abstract void handle(ResultSet rset);

}
