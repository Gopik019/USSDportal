package com.hdsoft.utils;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class ApplicationLogger {

	private Logger logger;
	private static ApplicationLogger instance;
	private String loggerName = null;
	static {
		Properties properties = new Properties();
		try {
			properties.load(ApplicationLogger.class
					.getResourceAsStream("log4j.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		PropertyConfigurator.configure(properties);

	}

	private ApplicationLogger(String loggerName) {
		this.loggerName = loggerName;
		logger = Logger.getLogger("DatavisionLog");
	}

	public static ApplicationLogger getInstance(String loggerName) {
		instance = new ApplicationLogger(loggerName);
		System.out.println(loggerName);
		return instance;
	}

	private Logger getLogger() {
		return logger;
	}

	public void logFatal(String message) {
		getLogger().fatal("[" + loggerName + "] " + message);
		System.out.println(message);
	}

	public void logError(String message) {
		getLogger().error("[" + loggerName + "] " + message);
		System.out.println(message);
	}

	public void logDebug(String message) {
		getLogger().debug("[" + loggerName + "] " + message);
		System.out.println(message);
	}

	public void logInfo(String message) {
		getLogger().info("[" + loggerName + "] " + message);
		System.out.println(message);
	}

}
