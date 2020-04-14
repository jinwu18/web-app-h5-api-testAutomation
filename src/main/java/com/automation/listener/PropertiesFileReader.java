package com.automation.listener;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesFileReader {
	private static PropertiesFileReader INSTANCE = null;
	private static final String SELENIUM_FILE = "selenium.properties";
	private static final String REPORT_FILE = "report.properties";
	private static final String LOG_FILE="log4j.properties";

	static Properties properties = new Properties();
	static InputStream in = null;
	static InputStream report = null;
	static InputStream log = null;

	static {
		try {
			in = PropertiesFileReader.class.getResourceAsStream("/" + SELENIUM_FILE);
			log = PropertiesFileReader.class.getResourceAsStream("/" + LOG_FILE);
			report = PropertiesFileReader.class.getResourceAsStream("/" + REPORT_FILE);
			
			properties.load(in);
			properties.load(log);
			properties.load(report);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private PropertiesFileReader() {
		
	}

	public static synchronized PropertiesFileReader getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new PropertiesFileReader();
		}
		return INSTANCE;
	}

	public static Properties readProperties(String name) throws IOException {
		if (null != name) {
			if (properties.isEmpty()) {
				in = properties.getClass().getResourceAsStream("/" + name);
				properties.load(in);
			}
		}
		return properties;
	}

	public static String getProperty(String key) {
		String value = null;
		if (null != key && !"".equalsIgnoreCase(key)) {
			value = (String) properties.get(key);
		}
		return value;
	}

	public static int getPropertyInt(String key) {
		String value = null;
		if (null != key && !"".equalsIgnoreCase(key)) {
			value = (String) properties.get(key);
		}
		return Integer.valueOf(value);
	}

	public static String getQuery(String query) {
		String value = null;
		if (null != query && !"".equalsIgnoreCase(query)) {
			value = (String) properties.get(query);
		}
		return value;
	}

	public static boolean setProperty(String key, String value) {
		properties.setProperty(key, value);
		return true;

	}
}
