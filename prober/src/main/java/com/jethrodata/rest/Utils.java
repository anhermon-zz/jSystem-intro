package com.jethrodata.rest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Utils {
	private static final String EOL = "\n";
	
		/**
		 * Execute shell command
		 * @param command - command to execute
		 * @return stdout
		 */
		public static final String shell(String command) {
		     StringBuilder stdout = new StringBuilder();
		     String line;
		     Process p;
		     try {
		            p = Runtime.getRuntime().exec(command);
		            BufferedReader br = new BufferedReader(
		                new InputStreamReader(p.getInputStream()));
		            while ((line = br.readLine()) != null) {
		                //System.out.println("line: " + line);
		            	if(stdout.length() > 0) {
		            		stdout.append(EOL);
		            	}
		                stdout.append(line);
		            }
		            p.waitFor();
		            p.destroy();
		            return stdout.toString();
		        } catch (Exception e) {
		        	e.printStackTrace();
		        	throw new RuntimeException(e);
		        }
		}
		
		public static List<Map<String, String>> ps() {	
			List<String> headers = null;
			List<Map<String, String>> out = new LinkedList<>();
			for(String line : Utils.shell("ps aux").split("\n")) {
				String[] split = line.split("\\s+");
				if(headers == null) {
					headers = Arrays.asList(split);
					continue;
				}
				Map<String, String> p = new HashMap<>();
				for (int i = 0; i < Math.min(split.length, headers.size()); i++) {
					p.put(headers.get(i), split[i]);
				}
				out.add(p);
			}
			return out;
		}
}
