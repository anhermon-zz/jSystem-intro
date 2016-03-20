package il.co.topq.jsystem;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import jsystem.framework.ParameterProperties;

public class DatasourceToFileOutputHandler extends DatasourceOutputHandler {

	private File destination;
	private static final char delimiter = '\t';
	private static final char EOL = '\n';
	@Override
	public void handle(ResultSet rset) {
		try {
		destination.getParentFile().mkdirs();
		if(!destination.exists()) {
			destination.createNewFile();
		} else {
			destination.delete();
		}
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
		          new FileOutputStream(destination), "utf-8"))) {
			StringBuffer sb = new StringBuffer();
			ResultSetMetaData metaData = rset.getMetaData();
			int columnCount = metaData.getColumnCount();
			for (int i = 1; i <= columnCount; i++) {
				if(sb.length() != 0) {
					sb.append(delimiter);
				}
				sb.append(metaData.getColumnLabel(i));
			}
			sb.append(EOL);
			System.out.println(sb.toString());
			writer.write(sb.toString());
			while(rset.next()) {
				sb = new StringBuffer();
				for (int i = 1; i <= columnCount; i++) {
					if(sb.length() != 0) {
						sb.append(delimiter);
					}
					sb.append(rset.getString(i));
				}
				sb.append(EOL);
				writer.write(sb.toString());				
			}
		}
		report.report("Query result saved to file:" + destination.getPath() + " completed");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	public File getDestination() {
		return destination;
	}
	@ParameterProperties(description = "Destination to save query result to")
	public void setDestination(File destination) {
		this.destination = destination;
	}

}
