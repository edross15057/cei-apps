package com.cei.single;

import java.awt.Dimension;
import java.util.Map;

import javax.sql.DataSource;
import javax.swing.JFrame;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.ComponentScan;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.swing.JRViewer;
import net.sf.jasperreports.view.JasperViewer;

@ComponentScan("com.cei")
@SpringBootApplication
public class CEIReport implements CommandLineRunner {
	private static final org.slf4j.Logger log = LoggerFactory.getLogger(CEIReport.class);

	@Autowired
	DataSource datasource;

//	@Autowired
//	ReportFrame frame;
//	@Autowired
//	ReportScheduler rs;

	@Autowired
	BuildProperties buildProperties;

	static Map<String, Object> paramsMap;
	static String reportFilefileName;

	public static void main(String[] args) {
		try {
			CommandLineProcessor parser = new CommandLineProcessor(args);
			reportFilefileName = parser.getReportFileName();
			paramsMap = parser.getMap();
			reportFilefileName = parser.reportFileName;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.exit(-1);
		}

		SpringApplication sa = new SpringApplication(CEIReport.class);
		sa.setHeadless(false);
		sa.run(" ");

	}

	// @Override
	public void run(String... strings) throws Exception {
		String info = String.format("Running application %s, verion %s built %s", buildProperties.getName(),
				buildProperties.getVersion(), buildProperties.getTime());
		log.info(info);
		System.out.println(info);
//		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//		List<JasperPrint> jpList = new ArrayList();
//		List<CEIViewer> viewerList = new ArrayList();
//		rs.currentIndex = -1;
//		frame.setSize(new Dimension(500, 400));
//		GridLayout flow = new GridLayout(1, 0);
//
//		frame.setLayout(flow);
//		Panel panel = new Panel();
//		panel.setLayout(flow);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.add(panel);
//		
//		while (true) {
//			panel.removeAll();
//			panel.setLayout(flow);
//			rs.currentIndex = -1;
//			for (int x = 0; x < rs.getReportCount(); x++) {
//				rs.getNextIdx();
//				JasperPrint jp = rs.getResults(rs.currentIndex);
//				CEIViewer viewer = new CEIViewer(jp);
//				viewer.setZoomRatio(rs.getZoom());
//				panel.add(viewer, x);
//				log.debug("adding viewer {} with name: {}", x, jp.getName());
//			}
//			// JasperPrint jp = rs.getResults(rs.getNextIdx());
//			// List<JRPrintPage> pages = jp.getPages();
//
//			try {
//
//				// int pageCount = jp.getPages().size();
//				// frame.add(panel);
//
//				frame.setVisible(true);
//
//				System.out.println("after frame");
//
//				frame.setVisible(true);
//				int sleepSeconds = 60;
//				try {
//					sleepSeconds = Integer.parseInt(System.getProperty(DashboardCommandLineParser.REFRESH));
//				} catch (Exception e) {
//					log.error("error parsing refresh rate: %s", System.getProperty(DashboardCommandLineParser.REFRESH));
//				}
//				TimeUnit.SECONDS.sleep(sleepSeconds);
//				// TimeUnit.SECONDS.sleep(rs.getSleepSeconds(rs.currentIndex));
//
//			} catch (Exception e) {
//				log.error(" error while trying to show report {}, waiting 10 seconds before retry", e.getCause(), e);
//
//			}
//
//		}
		JasperReport jasperReport = JasperCompileManager.compileReport(reportFilefileName);
		System.out.println("Report name: " + reportFilefileName);
		JasperPrint jp = JasperFillManager.fillReport(jasperReport, paramsMap, datasource.getConnection());
		// JasperPrint jp = JasperFillManager.fillReport(jasperReport,paramsMap);
		JFrame frame = new JFrame("CEI Reports");
		
		JRViewer viewer = new JRViewer(jp);
		try {
			frame.add(viewer);
		} catch (Exception e) {
			e.printStackTrace();
		}
		frame.setSize(new Dimension(750, 650));
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	private void viewReport() throws JRException {

		JasperReport jasperReport = JasperCompileManager.compileReport(System.getProperty("ReportName"));
	}
}
