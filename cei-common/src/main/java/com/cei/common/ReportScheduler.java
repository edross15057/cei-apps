package com.cei.common;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.management.InstanceNotFoundException;
import javax.sql.DataSource;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.jfree.util.Log;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.zaxxer.hikari.pool.HikariPool;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;

@Component
public class ReportScheduler {


	
	
	
	@Autowired
	DataSource datasource;
	@Autowired ReportFrame frame;
	
	int currentIndex=-1;
	private static final org.slf4j.Logger log = LoggerFactory.getLogger(ReportScheduler.class);
	private String[] HEADERS = { "screen","zoom", "seconds", "parameter_name","parameter_value","parameter_name2", "parameter_value2" };
	// private List<Screen> scrList = new ArrayList<>();
	List<Screen> scrList = new ArrayList<>();

	@PostConstruct
	public void setupSchedule() {
		Reader in;
		try {
			String fileName = System.getProperty(DashboardCommandLineParser.CSV_NAME);
			in = new FileReader(fileName);

			Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().withHeader(HEADERS).parse(in);

			for (CSVRecord record : records) {
				Screen scr = new Screen(record.get("screen"), record.get("zoom"), record.get("seconds"), record.get("parameter_name"),record.get("parameter_value"), record.get("parameter_name2"), record.get("parameter_value2"));
				String reportFileName=String.format("%s\\%s.jrxml",System.getProperty(DashboardCommandLineParser.REPORTS_DIR),scr.getName());
				InputStream reportStream = getClass().getResourceAsStream(reportFileName);
				JasperReport jasperReport = JasperCompileManager.compileReport(reportFileName);
				scr.setReport(jasperReport);
				scrList.add(scr);
				System.out.println("screen: " + scr.name);

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void showReports() {
		scrList.forEach(i->System.out.println("Screen Name: "+i.name));
	}	
	
	public float getZoom() {
		return (float)scrList.get(currentIndex).getZoomLevel();
	}
	
	Screen getIndex(String screenName) throws InstanceNotFoundException {
		Optional<Screen> oScreen= scrList.stream().filter(r->r.name.equalsIgnoreCase(screenName)).findFirst();
		if(oScreen.isEmpty()) {
			throw new InstanceNotFoundException();
		}
		return oScreen.get();
	}
	
	public int getNextIdx() {
		 
		 if(currentIndex==scrList.size()-1) {
			 currentIndex=-1;
		 }
		 currentIndex++;
		 return currentIndex;
	}
	public int getSleepSeconds(int idx) {
		return scrList.get(idx).getSeconds();
	}
	public JasperReport getJasperReport(int idx) {
		return scrList.get(idx).getReport();
	}
	public JasperPrint getResults(int idx) throws JRException, SQLException{
		log.debug("Index = "+idx);
		Screen scr = scrList.get(idx);
		String name = scr.getName(); 
		log.debug("Showing schreen: "+name);
		JasperReport jr=scr.getReport();
		
		Map<String, Object> params=new HashMap<>();
		params.put(scr.getParameterName(), scr.getParameterValue());
		params.put(scr.getParameterName2(),scr.getParameterValue2());
		
		Log.info(String.format(" Runing report : %s, param1 %s, parmaValue %s",scr.getName(),scr.getParameterValue(),scr.getParameterValue2() ));
		if(scr.name.toLowerCase().startsWith("annou")){
			params.putAll(readAnnouceFile());
		}
		Connection con = datasource.getConnection();
		JasperPrint jp= JasperFillManager.fillReport(jr,params,con);
		
	
		
		
		con.close();
		return jp;
		
		
	}

	protected Map<String,String> readAnnouceFile() {
		String fileName=System.getProperty(DashboardCommandLineParser.ANNOUNCE_FILE);
		 List<String> lines = Collections.emptyList();
		 Map<String, String>announceParams=new HashMap<>();
		    try
		    {
		    	
		       String imageFile="";
		      lines =
		       Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
		       announceParams.put("Heading",  (lines.size()>0 ) ? lines.get(0):"Header not defined");
		       announceParams.put("Headering2", (lines.size()>1 ) ? lines.get(1):"");
		       announceParams.put("Detail", (lines.size()>2 ) ? lines.get(2):"");
		       if((lines.size()>3) && (lines.get(3).startsWith("Image="))){
		    	   String tmp=lines.get(3);
		    	   imageFile=tmp.substring(tmp.indexOf("=")+1);
		       
		       }
		       announceParams.put("image", imageFile);
		       return announceParams;
		    }
		      
		      
		    
		    catch (IOException e)
		    {
		 log.error("unable to process accouncement file "+fileName,e);
				 // do something
		     
		    }
		    return new HashMap<>();
		    
	}
	

}
