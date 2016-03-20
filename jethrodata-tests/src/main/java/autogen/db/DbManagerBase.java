package autogen.db;
import junit.framework.SystemTestCase;
import il.co.topq.jsystem.DataBase;
/**
 * Auto generate management object.
 * Managed object class: il.co.topq.jsystem.DataBase
 * This file <b>shouldn't</b> be changed, to overwrite methods behavier
 * change: DbManager.java
 */
public abstract class DbManagerBase extends SystemTestCase{
	protected DataBase db = null;
	public void setUp() throws Exception {
		db = (DataBase)system.getSystemObject("db");
	}
}
