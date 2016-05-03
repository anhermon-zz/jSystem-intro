package com.jethrodata.rest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.LinkedList;
import java.util.List;

import com.jethrodata.JethroConnection;

public class test {

	public static void main(String[] args) {
		try (Connection conn = new JethroConnection("cd8autoposix.dynqa.com:9111", "regression_sg", "", "jethro", "jethro")) {
			PreparedStatement prepareStatement = conn.prepareStatement("select * from sgk");
			try (ResultSet rs = prepareStatement.executeQuery()) {
				ResultSetMetaData metaData = rs.getMetaData();
				while (rs.next()) {
					List<String> row = new LinkedList<>();
					for (int i = 0; i < metaData.getColumnCount(); i++) {
							Object field = rs.getObject(i + 1);
							switch(field != null ? field.getClass().getSimpleName() : "NULL") {
								case "Integer" : System.out.println(String.format("%s is an integer", field));
									break;
								case "String" : System.out.println(String.format("%s is a String", field));
									break;
								case "Float" : System.out.println(String.format("%s is a Float", field));
									break;
								case "Double" : System.out.println(String.format("%s is a Double", field));
									break;
								case "Long" : System.out.println(String.format("%s is a Long", field));
									break;
								default: System.out.println(field != null ? field.getClass().getSimpleName() : "NULL");
							}
//							System.out.println(.getClass() + ":" + metaData.getColumnTypeName(i + 1));
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
