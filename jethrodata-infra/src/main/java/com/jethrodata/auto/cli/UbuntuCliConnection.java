package com.jethrodata.auto.cli;

import java.util.ArrayList;

import com.aqua.sysobj.conn.LinuxDefaultCliConnection;

import systemobject.terminal.Prompt;

public class UbuntuCliConnection extends LinuxDefaultCliConnection {

	@Override
	public Prompt[] getPrompts() {
		ArrayList<Prompt> prompts = new ArrayList<Prompt>();		
		Prompt p = new Prompt();
		p.setCommandEnd(true);
		p.setPrompt("# ");
		prompts.add(p);

		p = new Prompt();
		p.setCommandEnd(true);
		p.setPrompt("$ ");
		prompts.add(p);
		
		p = new Prompt();
		p.setPrompt("login: ");
		p.setStringToSend(getUser());
		prompts.add(p);

		p = new Prompt();
		p.setPrompt("Password: ");
		p.setStringToSend(getPassword());
		prompts.add(p);
		return prompts.toArray(new Prompt[prompts.size()]);
	}

	
}
