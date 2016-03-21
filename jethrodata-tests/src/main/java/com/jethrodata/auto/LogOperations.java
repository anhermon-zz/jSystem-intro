package com.jethrodata.auto;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import com.jethrodata.auto.logs.AbstractLogCollector;

import jsystem.framework.TestProperties;
import junit.framework.SystemTestCase4;

public class LogOperations extends SystemTestCase4 {
	private AbstractLogCollector logCollector;
	private File source;
	@Before
	public void setup() throws Exception {
		logCollector = (AbstractLogCollector) system.getSystemObject("logCollector");
	}
	
	@Test
	@TestProperties(name = "Collect logs from ${source}", paramsInclude = { "source" })
	public void testLogCollection() {
		System.out.println(logCollector.collect(source, 0));
	}

	public File getSource() {
		return source;
	}

	public void setSource(File source) {
		this.source = source;
	}

}
