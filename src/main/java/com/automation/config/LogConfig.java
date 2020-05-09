package com.automation.config;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import com.automation.listener.PropertiesFileReader;

public class LogConfig {
	public static Properties getLog4jConfig() throws IOException
	{
		Properties pro;
		pro = PropertiesFileReader.readProperties("log4j.properties");
		pro.put("log4j.appender.R.File", getLogPath());
		return pro;
	}

	public static String getLogPath(){
		String folder = System.getProperty("user.dir") + File.separator + "log";
		File logFolder = new File(folder);
		if(!logFolder.exists()) {
			logFolder.mkdirs();
		}
		return folder + File.separator + "app.log";
	}
}
