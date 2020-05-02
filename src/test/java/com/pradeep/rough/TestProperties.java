package com.pradeep.rough;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class TestProperties {
	public static void main(String[] args) throws IOException {
		
		Properties propertyConfig = new Properties();
		FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+"\\src\\test\\resources\\properties\\config.properties");
		propertyConfig.load(fis);
		
		propertyConfig.getProperty("browser");
		
		Properties propertyOR = new Properties();
		FileInputStream fisor = new FileInputStream(System.getProperty("user.dir")+"\\src\\test\\resources\\properties\\objectrepository.properties");
		propertyOR.load(fisor);
		
		propertyOR.getProperty("bmlBtn");
	}

}
