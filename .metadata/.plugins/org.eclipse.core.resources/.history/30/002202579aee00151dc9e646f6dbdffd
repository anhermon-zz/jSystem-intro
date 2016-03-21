package com.jethrodata.auto.cli;

import com.aqua.sysobj.conn.CliCommand;
import com.aqua.sysobj.conn.CliConnectionImpl;

import jsystem.extensions.analyzers.text.FindText;

public class LinuxStation extends AbstractStation {

	public CliConnectionImpl cliConnection;

	@Override
	public void init() throws Exception {
		super.init();
	}

	@Override
	public void close() {

	}

	public void listDir(String folderName) throws Exception {
		CliCommand command = new CliCommand("ls -la " + folderName);
		cliConnection.handleCliCommand("Listing folder " + folderName, command);
	}

	@Override
	public void runCommand(String commandStr) throws Exception {

	}

	@Override
	public int getFileSize(String folder, String fileName) throws Exception {
		String[] commandStrings = new String[2];
		commandStrings[0] = "cd " + folder;
		commandStrings[1] = "ls -la | grep " + fileName;
		CliCommand command = new CliCommand();
		command.setCommands(commandStrings);
		command.addErrors("No such file or directory");

		cliConnection.handleCliCommand("Getting file size", command);
		setTestAgainstObject(cliConnection.getTestAgainstObject());

		FindText findText = new FindText("\\d+\\s\\w+\\s\\w+\\s\\s(\\d+)", true, true, 2);
		analyze(findText);
		
		String result = findText.getCounter();
		
		return Integer.parseInt(result);
		
		

	}

}
