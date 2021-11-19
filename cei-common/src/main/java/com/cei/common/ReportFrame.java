package com.cei.common;

import java.awt.Dimension;

import javax.annotation.PostConstruct;
import javax.swing.JFrame;

import org.springframework.stereotype.Component;
@Component
public class ReportFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
@PostConstruct
public void configure() {
	setExtendedState(JFrame.MAXIMIZED_BOTH); 
	//setUndecorated(true);
//	setSize(new Dimension(500, 400));
//    setLocationRelativeTo(null);
	//setVisible(true);
}
}
