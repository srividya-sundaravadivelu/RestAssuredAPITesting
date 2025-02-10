package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

	private static Properties properties = new Properties();

	static {
		try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream("config/config.properties")) {
			if (input == null) {
				throw new RuntimeException("config.properties file not found");
			}
			properties.load(input);
		} catch (IOException ex) {
			throw new RuntimeException("Error loading config.properties file", ex);
		}
	}


	public static String getBaseUri() {
		return properties.getProperty("baseURI");
	}
	
	public static String getUserName() {
		return properties.getProperty("userName");
	}
	
	public static String getPassword() {
		return properties.getProperty("password");
	}
	
	public static String getCsvFile() {
		return properties.getProperty("csvFile");
	}
}
