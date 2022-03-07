package com.cei.simple;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Properties;

import org.junit.jupiter.api.Test;

import com.cei.single.CommandLineProcessor;

class TestPropertiesFile {

	@Test
	void test() {
		CommandLineProcessor clp = new CommandLineProcessor();
		
		Properties prop = new Properties();
		prop.put(clp.PF_NUMBER_PARAMS, "4");
		putItem(prop,1,"name","prompt date 1","Date","today");
		putItem(prop,2,"name","prompt date 2","Date","-7");
		putItem(prop,3,"name","prompt integer","Integer","3");
		putItem(prop,4,"name","prompt double","Double","4.5");
		
		clp.fillProperties(prop);
		
		
	}
	
	private void  putItem(Properties prop, int item, String name, String prompt,  String type,String defaultValue) {
		prop.put(String.format(CommandLineProcessor.PF_PARAM_X_NAME,item),name);
		prop.put(String.format(CommandLineProcessor.PF_PARAM_X_PROMPT,item),prompt);
		prop.put(String.format(CommandLineProcessor.PF_PARAM_X_TYPE,item),type);
		prop.put(String.format(CommandLineProcessor.PF_PARAM_X_DEFAULT_VALUE,item),defaultValue);
		
	}

}
