package com.jethrodata.rest.services.interfaces;

import org.springframework.stereotype.Service;

@Service
public interface JetheoJdbcExecutorService {
	
	public String execute(String hosts, String isntanceName, String user, String password, String query);

	public void setConcurrent(int concurrent);
}
