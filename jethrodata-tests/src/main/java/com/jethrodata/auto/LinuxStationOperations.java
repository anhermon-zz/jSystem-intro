package com.jethrodata.auto;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import jsystem.framework.ParameterProperties;
import jsystem.framework.TestProperties;
import junit.framework.Assert;
import junit.framework.SystemTestCase4;

public class LinuxStationOperations extends SystemTestCase4 {

	private String folderName;
	
	private String fileName;
	
	private int expectedFileSize;

	private String query;
	
	private Lab lab;

	@Before
	public void setup() throws Exception {
		lab = (Lab) system.getSystemObject("lab");
	}

	@Test
	@TestProperties(name = "List folder ${folderName}", paramsInclude = { "folderName" })
	public void listDir() throws Exception {
		lab.station.listDir(folderName);
	}
	
	@Test
	@TestProperties(name = "Assert that file size of file ${folderName}/${fileName} is ${expectedFileSize}")
	public void assertFileSize() throws Exception{
		int result = lab.station.getFileSize(folderName, fileName);
		if (result != expectedFileSize){
			throw new AssertionError("Not equals!!!");
		}
	}
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getExpectedFileSize() {
		return expectedFileSize;
	}

	public void setExpectedFileSize(int expectedFileSize) {
		this.expectedFileSize = expectedFileSize;
	}

	public Lab getLab() {
		return lab;
	}

	public void setLab(Lab lab) {
		this.lab = lab;
	}

	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}
}
