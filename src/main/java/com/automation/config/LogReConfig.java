package com.automation.config;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import com.automation.listener.PropertiesFileReader;

public class LogReConfig {
	public static Properties getLog4jConfig(String caseName)
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
				pro.put("log4j.appender.R.Append", "false");
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
			pro.put("log4j.appender.R.Append", "false");
			pro.put("log4j.appender.R.MaxFileSize", "2000KB");
			pro.put("log4j.appender.R.MaxBackupIndex", "20");
			pro.put("log4j.appender.R.Threshold", "DEBUG");
			pro.put("log4j.appender.R.layout", "org.apache.log4j.PatternLayout");
			pro.put("log4j.appender.R.layout.ConversionPattern","%n[%d{yyyy-MM-dd HH:mm:ss}] [%p] %m");
		}
		pro.put("log4j.appender.R.File", getLogPath(caseName));
		return pro;
	}

	public static String getLogPath(String caseName){
		String logFile = "";
		try{
			Date dateTimeNow = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String folder = System.getProperty("user.dir") + File.separator + "log"
					+ File.separator + sdf.format(dateTimeNow) + File.separator + caseName;
			File dateFolder = new File(folder);
			if(!dateFolder.exists()) {
				dateFolder.mkdirs();
			}
			
			logFile = dateFolder + File.separator + "testLog.log";
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return logFile;
	}
}
