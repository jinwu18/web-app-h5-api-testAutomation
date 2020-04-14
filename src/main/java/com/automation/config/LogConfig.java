package com.automation.config;

import java.io.File;
import java.util.Properties;

import com.automation.listener.PropertiesFileReader;

public class LogConfig {
	public static Properties getLog4jConfig()
	{
		Properties pro;
		try{
			pro = PropertiesFileReader.readProperties("log4j.properties");
			if(pro.getProperty("log4j.rootLogger")==null || pro.getProperty("log4j.rootLogger").trim()==""){
				pro = new Properties();
				pro.put("log4j.rootLogger", "info,C,R");
				pro.put("log4j.appender.C", "org.apache.log4j.ConsoleAppender");
				pro.put("log4j.appender.C.Target","System.out");
				pro.put("log4j.appender.C.Threshold", "INFO");
				pro.put("log4j.appender.C.layout", "org.apache.log4j.PatternLayout");
				pro.put("log4j.appender.C.layout.ConversionPattern", "%n %m");
				pro.put("log4j.appender.R", "org.apache.log4j.RollingFileAppender");
				pro.put("log4j.appender.R.MaxFileSize", "2000KB");
				pro.put("log4j.appender.R.MaxBackupIndex", "20");
				pro.put("log4j.appender.R.Threshold", "DEBUG");
				pro.put("log4j.appender.R.layout", "org.apache.log4j.PatternLayout");
				pro.put("log4j.appender.R.layout.ConversionPattern","%n[%d{yyyy-MM-dd HH:mm:ss}] [%p] %m");
			}

		} catch (Exception e) {
			pro = new Properties();
			pro.put("log4j.rootLogger", "info,C,R");
			pro.put("log4j.appender.C", "org.apache.log4j.ConsoleAppender");
			pro.put("log4j.appender.C.Target","System.out");
			pro.put("log4j.appender.C.Threshold", "INFO");
			pro.put("log4j.appender.C.layout", "org.apache.log4j.PatternLayout");
			pro.put("log4j.appender.C.layout.ConversionPattern", "%n %m");
			pro.put("log4j.appender.R", "org.apache.log4j.RollingFileAppender");
			pro.put("log4j.appender.R.MaxFileSize", "2000KB");
			pro.put("log4j.appender.R.MaxBackupIndex", "20");
			pro.put("log4j.appender.R.Threshold", "DEBUG");
			pro.put("log4j.appender.R.layout", "org.apache.log4j.PatternLayout");
			pro.put("log4j.appender.R.layout.ConversionPattern","%n[%d{yyyy-MM-dd HH:mm:ss}] [%p] %m");
		}
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
