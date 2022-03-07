package com.cei.single;

import java.awt.Frame;
import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.JFrame;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.springframework.boot.context.properties.bind.DefaultValue;

public class CommandLineProcessor {

	public static final String REPORT_NAME = "ReportName";
	public static final String PARM_FILE = "ParmameterFile";

	// property file entries
	public static final String PF_NUMBER_PARAMS = "numberOfParams";
	public static final String PF_PARAM_X_NAME = "param%dName";
	public static final String PF_PARAM_X_PROMPT = "param%dPrompt";
	public static final String PF_PARAM_X_TYPE = "param%dType";
	public static final String PF_PARAM_X_DEFAULT_VALUE = "param%xDefault";
	List<ParamItemData> params;
	String reportFileName;
	Map<String, Object> map;

	// testing constructor
	public CommandLineProcessor() {

	}

	public CommandLineProcessor(String args[]) throws ParseException, MissingOptionException {
		if (args == null || args.length == 0) {
			showHelp();
			System.exit(-1);
		}

		CommandLineParser parser = new DefaultParser();
		Options options = new Options();
		options.addRequiredOption(REPORT_NAME, REPORT_NAME, true, "Full name of report");
		options.addRequiredOption(PARM_FILE, PARM_FILE, true, "full path and name of param file");
		try {
			CommandLine cmd = parser.parse(options, args);
			reportFileName = cmd.getOptionValue(REPORT_NAME);
			String paramFileName = cmd.getOptionValue(PARM_FILE);
			File reportFile = new File(reportFileName);
			if (!reportFile.exists()) {
				System.out.println(String.format("The report file name %s does not exist", reportFileName));
				System.exit(-1);
			}

			File paramFile = new File(paramFileName);
			if (!paramFile.exists()) {
				System.out.println(String.format("The parameter file name %s does not exist", paramFileName));
				System.exit(-1);
			}

		} catch (MissingOptionException e) {
			// TODO Auto-generated catch block
			showHelp();
			throw e;

			// System.exit(-1);
		} catch (ParseException e) {
			showHelp();
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			showHelp();
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}

	private void showHelp() {
		System.out.println("you must specify report full name (path and name) \n and full name of parameter file \n\n");
		System.out.println(String.format("Example: CEI-Simple -%s=x:/reportName  -%s=x:/paramFile ",REPORT_NAME,PARM_FILE));
	}

	/**
	 * The parameter file is a properties file with the following entries
	 * numberOfParams=x // for each param there must be 3 entries
	 * param1Name=InternalReportParameterName param1.prompt=Prompt to show on screen
	 * (ex enter date in mm/dd/yyyy format param1.dataType=xxx where xxx is
	 * Integer,Double,Date or String
	 * 
	 * @param paramFile
	 */

	public void fillProperties(Properties props) {

		params = new ArrayList<>();
		Integer nbrParams = Integer.parseInt((String) props.get(PF_NUMBER_PARAMS));
		for (int x = 1; x <= nbrParams; x++) {
			ParamItemData item = new ParamItemData();
			item.paramName = props.getProperty(String.format(PF_PARAM_X_NAME, x));
			item.paramPrompt = props.getProperty(String.format(PF_PARAM_X_PROMPT, x));
			item.paramType = props.getProperty(String.format(PF_PARAM_X_TYPE, x));
			item.defaultValue = props.getProperty(String.format(PF_PARAM_X_DEFAULT_VALUE, x));

			if (item.paramType.equalsIgnoreCase("Date")) {
				if (item.defaultValue.equalsIgnoreCase("today")) {
					LocalDate today = LocalDate.now();

					String formattedDate = today.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy"));
					item.defaultValue = formattedDate;
				}
				if (item.defaultValue.startsWith("-")) {
					int y = Integer.parseInt(item.defaultValue);
					LocalDate d = LocalDate.now().plusDays(y);
					String formattedDate = d.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy"));
					item.defaultValue = formattedDate;
				}
			}
			params.add(item);
		}
		
		UserInputManager uim = new UserInputManager(params, " ");
		map=uim.getMap();
		

	}

	protected void readParamFile(File paramFile) {

		Properties props = new Properties();
		try (FileInputStream fis = new FileInputStream(paramFile)) {
			props.load(fis);
			fillProperties(props);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}

	String getReportFileName() {
		return reportFileName;
	}

	List<ParamItemData> getParams() {
		return params;
	}

	
	public Map<String, Object> getMap(){
		return map;
	}
}
