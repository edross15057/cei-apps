package com.cei.common;

import static org.junit.jupiter.api.Assertions.*;

import java.util.MissingFormatArgumentException;

import org.junit.jupiter.api.Test;

import com.cei.common.DashboardCommandLineParser;

class DashboardCommandLineTest {
	
	@Test
	void testWithDash() {
		String[] args = {"-"+DashboardCommandLineParser.REPORTS_DIR,"/reportLocation","-"+DashboardCommandLineParser.CSV_NAME,"csvName", DashboardCommandLineParser.ANNOUNCE_FILE, "Annouce file"};
		DashboardCommandLineParser clp = new DashboardCommandLineParser(args);
		assertEquals("/reportLocation", System.getProperty(DashboardCommandLineParser.REPORTS_DIR));
		assertEquals("csvName", System.getProperty(DashboardCommandLineParser.CSV_NAME));
	}
	
	@Test
	void testWithEquals() {
		String[] args = {"-"+DashboardCommandLineParser.REPORTS_DIR+"=/reportLocation","-"+DashboardCommandLineParser.CSV_NAME+"=csvName",DashboardCommandLineParser.ANNOUNCE_FILE+ "=Annouce file"};
		DashboardCommandLineParser clp = new DashboardCommandLineParser(args);
		assertEquals("/reportLocation", System.getProperty(DashboardCommandLineParser.REPORTS_DIR));
		assertEquals("csvName", System.getProperty(DashboardCommandLineParser.CSV_NAME));
	}
	
	@Test
	void testWithoutReportsDir() {
		String[] args = {"-"+DashboardCommandLineParser.CSV_NAME,"csvName",DashboardCommandLineParser.ANNOUNCE_FILE, "Annouce file"};
		assertThrows(MissingFormatArgumentException.class, ()->{new DashboardCommandLineParser(args);});
	//	DashboardCommandLineParser clp = new DashboardCommandLineParser(args);
	//	assertEquals("/reportLocation", System.getProperty(DashboardCommandLineParser.REPORTS_DIR));
	//	assertEquals("csvName", System.getProperty(DashboardCommandLineParser.CSV_NAME));
	}

}
