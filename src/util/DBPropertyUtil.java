package util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DBPropertyUtil {
	
	//this method takes the filename which contains db connection like
			//user name, pwd, port number, protocol and db name as an argument
			//and returns a connection
			public static String getConnectionString(String fileName)throws IOException {
				String connStr=null;
				Properties props=new Properties();
				FileInputStream fis=new FileInputStream(fileName);
				props.load(fis);
				
				String user = props.getProperty("user");
				String password = props.getProperty("password");
		        String port = props.getProperty("port");
		        String database = props.getProperty("database");
		        String protocol = props.getProperty("protocol");
		        String system = props.getProperty("system");
		        
		        connStr=protocol+"//"+system+":"+port+"/"+database+"?user="+user+"&password="+password;
				return connStr;
			}

		}


