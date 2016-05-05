package com.jethrodata.rest.services.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

import org.springframework.stereotype.Service;

import com.jethrodata.JethroConnection;
import com.jethrodata.rest.services.interfaces.JetheoJdbcExecutorService;

@Service
public class JetheoJdbcExecutorServiceImpl implements JetheoJdbcExecutorService {

	private static final Map<String, Future<String>> queue = new ConcurrentHashMap<>();
	@Override
	public String execute(String hosts, String instanceName, String user, String password, String query) {
		String jobID = UUID.randomUUID().toString();
		List<List<String>> data = new LinkedList<>();
		try(Connection conn = initConnection(hosts, instanceName, user, password)) {
			PreparedStatement prepareStatement = conn.prepareStatement(query);
			try(ResultSet rs = prepareStatement.executeQuery()) {
				ResultSetMetaData metaData = rs.getMetaData();
				while (rs.next()) {
					List<String> row = new LinkedList<>();
					for (int i = 0; i < metaData.getColumnCount(); i++) {
						row.add(rs.getString(i));
					}
					data.add(row);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return query;
	}
	
	private JethroConnection initConnection(String hosts, String instanceName, String user, String password) throws SQLException {
		return new JethroConnection(hosts, instanceName, "", user, password);
	}

	@Override
	public void setConcurrent(int concurrent) {
		// TODO Auto-generated method stub
		
	}
	
}
