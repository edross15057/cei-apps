package com.cei.common;



import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;


public class DashboardCommandLineParser {

	
	public static final String REPORTS_DIR="ReportsDir";
	public static final String CSV_NAME="CSV";
	
	public DashboardCommandLineParser(String[] args)  {
		
		if(args==null || args.length==0)
		{
			System.out.println("you must specify the reports location and the name of the scheduler");
			System.out.println(String.format("example  CEI-dashboard -%s x:/dirOfReports -%s/ fullPathAndNameOfSchedudler.csv",REPORTS_DIR,CSV_NAME));
			System.exit(-2);
		}
		CommandLineParser parser = new DefaultParser();
		Options options=new Options();
		options.addOption(REPORTS_DIR, true, "file directory of reports");
		options.addOption(CSV_NAME,true,"full path of the csv file");
		try {
			
			CommandLine cmd=parser.parse(options, args);
			String reports=cmd.getOptionValue(REPORTS_DIR);
			String csv=cmd.getOptionValue(CSV_NAME);
			System.setProperty(REPORTS_DIR, reports);
			System.setProperty(CSV_NAME, csv);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			System.out.println("must specify the locatioin of the reports directory using -"+REPORTS_DIR+" /dirname");
			System.out.println("and the location of the CSV file -"+CSV_NAME+" /fullpathandfilename");
			System.exit(-1);
		}
	
		
		
	}

	
	
}
