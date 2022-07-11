package com.dnd.mountclim.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

	private static Logger logger = LoggerFactory.getLogger(AppConfig.class);
	
	@Bean
	@SuppressWarnings("deprecation")
	public PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() throws IOException {
		PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
		Map<String, String> rootConfigurations = null;
		try {
			Properties properties = new Properties();
			
	        rootConfigurations = (Map<String, String>) properties.keySet().stream()
					.collect(Collectors.toMap(s -> s.toString(), s -> properties.get(s.toString()).toString()));		
			String applicationConfigurationPath = Paths.get(getOsPath("dnd"), new String[]{"config"}).toString();
	        File directory = new File(applicationConfigurationPath);
	        File[] files = directory.listFiles(new FilenameFilter() { 	
				@Override 
				public boolean accept(File dir, String name) { 
					return name.endsWith("properties"); 
				}
			});
	        for (int i = 0; i < files.length; ++i) {
				if (files[i].canRead()) {
					FileInputStream fs = new FileInputStream(files[i].getPath());
					properties.load(fs);
					rootConfigurations = (Map<String, String>) properties.keySet().stream()
							.collect(Collectors.toMap(s -> s.toString(), s -> properties.get(s.toString()).toString()));
					for(String key : rootConfigurations.keySet()) {
						System.setProperty(key, rootConfigurations.get(key));
						logger.info(key + " : " + rootConfigurations.get(key));
					}
				}
			}
		} catch(Exception e) {
			throw e;
		}
		return ppc;
	}
	
	public static String getOsPath(String appHome) {
		if (isWindow())
			return (new File(String.format("%s:\\%s", new Object[]{getSystemDriver(), appHome}))).getPath();
		if (isMacOS())
			return (new File(String.format("/%s/%s", new Object[]{"Users", appHome}))).getPath();
		if (isLinux())
			return (new File(String.format("/%s/%s", new Object[]{"home", appHome}))).getPath();
		return (new File(String.format("/%s", new Object[]{appHome}))).getPath();
	}
	
	public static String getUserHomeDir() {
		return System.getProperty("user.home");
	}
	
	public static String getSystemDriver() {
		String driverName = null;
		if (isWindow()) {
			String userDir = getUserHomeDir();
			driverName = userDir.substring(0, 1);
		}
		return driverName;
	}
	
	public static boolean isWindow() {
		return (System.getProperty("os.name").toLowerCase().indexOf("win") >= 0);
	}
	
	public static boolean isMacOS() {
		return (System.getProperty("os.name").toLowerCase().indexOf("mac") >= 0);
	}
	
	public static boolean isLinux() {
		return (System.getProperty("os.name").toLowerCase().indexOf("linux") >= 0);
	}
}
