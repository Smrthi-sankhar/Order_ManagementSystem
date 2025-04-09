package util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnUtil {
	
	 private static final String fileName = "db.properties";

	    public static Connection getDbConnection() {
	        Connection con = null;
	        String connString=null;
	        try {
	            connString = DBPropertyUtil.getConnectionString(fileName); // Get URL from properties
	        } catch (IOException e) {
	            System.out.println("Connection string could not be retrieved.");
	            e.printStackTrace();
	        }
	        if (connString != null) {
	        	try {
	                con = DriverManager.getConnection(connString); // Get actual Connection object
	        	}
	        	catch (SQLException e) {
	            System.out.println("Database connection failed.");
	            e.printStackTrace();
	        }
	        }
	        return con;
	    }

}
