package com.automation.config;

import com.automation.listener.PropertiesFileReader;

public class WebConstant {
	public static String handleMode;
	public static String remoteURL;
	public static String browserType;
	public final static int implicitlyWait;
	public final static int implicitlyDisappearWait;
	public final static int pageLoadTimeout;
	public final static int scriptTimeout;
	public final static int waitInterval;

	static{
		handleMode = PropertiesFileReader.getProperty("web.handler.mode");
		remoteURL = PropertiesFileReader.getProperty("web.remote.url");
		browserType = PropertiesFileReader.getProperty("browser.type");
		implicitlyWait = Integer.valueOf(PropertiesFileReader.getProperty("browser.implicitlyWait"));
		implicitlyDisappearWait = Integer.valueOf(PropertiesFileReader.getProperty("browser.implicitlyDisappearWait"));
		pageLoadTimeout = Integer.valueOf(PropertiesFileReader.getProperty("browser.pageLoadTimeout"));
		scriptTimeout = Integer.valueOf(PropertiesFileReader.getProperty("browser.scriptTimeout"));
		waitInterval = Integer.valueOf(PropertiesFileReader.getProperty("browser.waitInterval"));
	}
}
