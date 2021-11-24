package com.cei.desktop;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Panel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;

import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.ParseException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.cei.common.CEIViewer;
import com.cei.common.DashboardCommandLineParser;
import com.cei.common.ReportFrame;
import com.cei.common.ReportScheduler;

import net.sf.jasperreports.engine.JasperPrint;

@ComponentScan("com.cei")
@SpringBootApplication
public class DesktopApplication implements CommandLineRunner {
	private static final org.slf4j.Logger log = LoggerFactory.getLogger(DesktopApplication.class);

	@Autowired
	ReportFrame frame;
	@Autowired
	ReportScheduler rs;

	public static void main(String[] args) {
		try {
			DashboardCommandLineParser parser = new DashboardCommandLineParser(args);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.exit(-1);
		} 

		SpringApplication sa = new SpringApplication(DesktopApplication.class);
		sa.setHeadless(false);
		sa.run(" ");
		// SpringApplication.run(DashboardApplication.class, args);
	}

	// @Override
	public void run(String... strings) throws Exception {
//		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		List<JasperPrint> jpList = new ArrayList<>();
		List<CEIViewer> viewerList = new ArrayList<>();
		rs.currentIndex = -1;
		frame.setSize(new Dimension(500, 400));
		GridLayout flow = new GridLayout(1, 0);

		frame.setLayout(flow);
		Panel panel = new Panel();
		panel.setLayout(flow);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(panel);
		while (true) {
			panel.removeAll();
			panel.setLayout(flow);
			rs.currentIndex = -1;
			for (int x = 0; x < rs.getReportCount(); x++) {
				rs.getNextIdx();
				JasperPrint jp = rs.getResults(rs.currentIndex);
				CEIViewer viewer = new CEIViewer(jp);
				viewer.setZoomRatio(rs.getZoom());
				panel.add(viewer, x);
				log.debug("adding viewer {} with name: {}", x, jp.getName());
			}
			// JasperPrint jp = rs.getResults(rs.getNextIdx());
			// List<JRPrintPage> pages = jp.getPages();

			try {

				// int pageCount = jp.getPages().size();
				// frame.add(panel);

				frame.setVisible(true);

				System.out.println("after frame");

				frame.setVisible(true);
				int sleepSeconds = 60;
				try {
					sleepSeconds = Integer.parseInt(System.getProperty(DashboardCommandLineParser.REFRESH));
				} catch (Exception e) {
					log.error("error parsing refresh rate: %s", System.getProperty(DashboardCommandLineParser.REFRESH));
				}
				TimeUnit.SECONDS.sleep(sleepSeconds);
				// TimeUnit.SECONDS.sleep(rs.getSleepSeconds(rs.currentIndex));

			} catch (Exception e) {
				log.error(" error while trying to show report {}, waiting 10 seconds before retry", e.getCause(), e);

			}

		}
	}
}
