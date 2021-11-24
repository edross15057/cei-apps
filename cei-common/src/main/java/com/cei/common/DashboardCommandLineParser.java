package com.cei.common;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class DashboardCommandLineParser {

	public static final String REPORTS_DIR = "ReportsDir";
	public static final String CSV_NAME = "CSV";
	public static final String ANNOUNCE_FILE = "AnnounceFile";
	public static final String REFRESH = "Refresh";

	public DashboardCommandLineParser(String[] args) throws ParseException,MissingOptionException {

		if (args == null || args.length == 0) {
			showhelp();
			;
			System.exit(-2);
		}
		CommandLineParser parser = new DefaultParser();
		Options options = new Options();
		options.addRequiredOption(REPORTS_DIR, REPORTS_DIR, true, "file directory of reports");
		options.addRequiredOption(CSV_NAME, CSV_NAME, true, "full path of the csv file");
		options.addOption(ANNOUNCE_FILE, true,"file containing annouce text");
		options.addOption(REFRESH, true, "optional refresh time  - default is 60 seconds");
		try {

			CommandLine cmd = parser.parse(options, args);
			String reports = cmd.getOptionValue(REPORTS_DIR);
			String csv = cmd.getOptionValue(CSV_NAME);
			String announceFile = "none";
			if (cmd.hasOption(ANNOUNCE_FILE)) {
				announceFile = cmd.getOptionValue(ANNOUNCE_FILE);
				System.setProperty(ANNOUNCE_FILE, announceFile);
			}
			if (cmd.hasOption(REFRESH)) {
				String refresh = cmd.getOptionValue(REFRESH);
				System.setProperty(REFRESH, refresh);

			} else {
				System.setProperty(REFRESH, "60");
			}

			System.setProperty(REPORTS_DIR, reports);
			System.setProperty(CSV_NAME, csv);
			

		} catch (MissingOptionException e) {
			// TODO Auto-generated catch block
			showhelp();
			throw e;

			//System.exit(-1);
		} catch (ParseException e) {
			showhelp();
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		catch (Exception e) {
			showhelp();
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}

	}

	private void showhelp() {
		System.out.println("you must specify the reports location and the name of the scheduler");
		System.out.println(String.format(
				"example  CEI-dashboard -%s x:/dirOfReports -%s/ fullPathAndNameOfSchedudler.csv -%s x:/announce.txt -%s xx where xx is number of seconds between refresh",
				REPORTS_DIR, CSV_NAME,ANNOUNCE_FILE,REFRESH));

	}

}
