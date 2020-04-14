package com.automation.listener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.PropertyConfigurator;

import com.automation.config.LogConfig;
import com.automation.config.LogReConfig;
import com.automation.framework.AbastractBase;

public class Log4jLogger {
    private static Logger logger = null;
	private String path;
    /** whether print the log */
    private final boolean enabled = true;
    /** whether show the location of log happened */
    private boolean showLocSrc = true;
    private final String thisClassName = Log4jLogger.class.getName();
    /** default log level */
    private int level = 1;
    /** log level:info */
    private final int info = 1;
    /** log level:debug */
    private final int debug = 2;
    /** log level:warn */
    private final int warn = 3;
    /** log level:error */
    private final int error = 4;
    static {
    	PropertyConfigurator.configure(LogConfig.getLog4jConfig());
        logger = Logger.getLogger(Log4jLogger.class);
        
    }

    @SuppressWarnings("static-access")
	private void log(int level, Object message, StackTraceElement[] ste) {
    	AbastractBase base = new AbastractBase();
    	if (ste != null) {
            message = getStackMsg(ste) + ":" + message;
        }
		String testCaseName = base.caseName;
    	if(!testCaseName.equalsIgnoreCase("")){
    		PropertyConfigurator.configure(LogReConfig.getLog4jConfig(base.caseName));
    		logger = Logger.getLogger(Log4jLogger.class);
    	}
        switch (level) {
        case info:
            logger.info(message);
            break;
        case debug:
            logger.debug(message);
            break;
        case warn:
            logger.warn(message);
            break;
        case error:
            logger.error(message);
            break;
        default:
            logger.debug(message);
        }
    }

    private String getStackMsg(StackTraceElement[] ste) {
        if (ste == null)
            return null;
        boolean srcFlag = false;
        for (int i = 0; i < ste.length; i++) {
            StackTraceElement s = ste[i];
            if (srcFlag) {
                return s == null ? "" : s.toString();
            }

            if (thisClassName.equals(s.getClassName())) {
                srcFlag = true;
            }
        }
        return null;
    }

    /**
     * write error information into standard output. Normally used to record the framework runtime info.
     * @param message:message content.
     */
    public void info(Object message) {
        if (!enabled || info < level)
            return;

        if (showLocSrc) {
            log(info, message, Thread.currentThread().getStackTrace());
        } else {
            log(info, message, null);
        }
    }

    /**
     * write error information into standard output. Normally used to print the value of variables.
     * @param message:message content.
     */
    public void debug(Object message) {
        if (!enabled || debug < level)
            return;
        if (showLocSrc) {
            log(debug, message, Thread.currentThread().getStackTrace());
        } else {
            log(debug, message, null);
        }
    }

    /**
     * write error information into standard output. Normally used in known exception occurred.
     * @param message:message content.
     */
    public void warn(Object message) {
        if (!enabled || warn < level)
            return;
        if (showLocSrc) {
            log(warn, message, Thread.currentThread().getStackTrace());
        } else {
            log(warn, message, null);
        }
    }

    /**
     * write error information into standard output. Normally used in unknown exception occurred.
     * @param message:error content.
     */
    public void error(Object message) {
        if (!enabled || error < level)
            return;

        if (showLocSrc) {
            log(error, message, Thread.currentThread().getStackTrace());
        } else {
            log(error, message, null);
        }
    }
    
    /**
     * write error information into error.log and all info into all_log.log.
     * @author huimin
     */
    
    public Logger setLogFileName() {
		DailyRollingFileAppender appender = (DailyRollingFileAppender) Logger.getRootLogger().getAppender("R");
		appender.setFile(path +File.separator + "all_log.log");
		appender.activateOptions();
		return logger;
	}
    
    public Logger setLogFileName2() {
		DailyRollingFileAppender appender = (DailyRollingFileAppender) Logger.getRootLogger().getAppender("E");
		appender.setFile(path +File.separator + "error.log");
		appender.activateOptions();
		return logger;
	}
    
    /**
	 * set template of log
	 * @param tName
	 * @return
	 */
	public Logger getLogger(String tName) {

		logger.removeAllAppenders();
		logger.setLevel(Level.INFO);
		logger.setAdditivity(true);

		FileAppender appender = new FileAppender();

		PatternLayout layout = new PatternLayout();
		String conversionPattern = "%d{[yyyy-MM-dd hh:mm:ss]}[%p][%c][%F][%L] - %m%n";
		layout.setConversionPattern(conversionPattern);
		appender.setLayout(layout);

		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		appender.setFile(path + File.separator + sf.format(new Date()) + "_"	+ tName + ".log");
		appender.setEncoding("UTF-8");

		appender.setAppend(true);
		appender.activateOptions();
		logger.addAppender(appender);

		return logger;
	}
}
