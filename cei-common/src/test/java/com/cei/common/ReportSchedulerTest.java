package com.cei.common;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.Test;

public class ReportSchedulerTest {

	@Test
	public void testAnnouceFile() {
		String fileName="AnnouceFileTest.txt";
		String fullFile=this.getClass()
        .getClassLoader()
        .getResource(fileName).getFile().substring(1);
		
		System.setProperty(DashboardCommandLineParser.ANNOUNCE_FILE, fullFile);
		ReportScheduler rs= new ReportScheduler();
		Map<String, String> response=rs.readAnnouceFile();
		assertTrue(true);
	}
	
}
