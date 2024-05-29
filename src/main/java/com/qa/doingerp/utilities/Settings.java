package com.qa.doingerp.utilities;

import com.qa.doingerp.exception.UnExpectedException;
import com.qa.doingerp.parameters.FrameworkParameters;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Settings {
	private static Properties properties;

	public static synchronized Properties getInstance() {
		if (properties == null) {
			loadFromPropertiesFile();
			//overWriteFromSystem();
		}

		return properties;
	}

	

	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	private static void loadFromPropertiesFile() {
		FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();

		if (frameworkParameters.getRelativePath() == null) {
			throw new UnExpectedException("FrameworkParameters.relativePath is not set!");
		}

		properties = new Properties();
		try {
			String fileName = "config"+ frameworkParameters.fileSeparator + "test"+ frameworkParameters.fileSeparator +"GlobalSettings.properties";
			String fileName_= System.getProperty("user.dir")+frameworkParameters.fileSeparator + "target"+ frameworkParameters.fileSeparator +"classes"+frameworkParameters.fileSeparator+"GlobalSettings.properties";
			properties.load(new FileInputStream(fileName_));
			//properties.load(Settings.class.getClassLoader().getResourceAsStream(fileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new UnExpectedException("FileNotFoundException while loading the Global Settings file");
		} catch (IOException e) {
			e.printStackTrace();
			throw new UnExpectedException("IOException while loading the Global Settings file");
		}
	}


}