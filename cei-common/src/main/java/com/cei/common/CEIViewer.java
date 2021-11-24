package com.cei.common;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.swing.JRViewer;

public class CEIViewer extends JRViewer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2578545801798451360L;

	public CEIViewer(JasperPrint jrPrint) {
		super(jrPrint);
		// TODO Auto-generated constructor stub
	}

	public boolean nextPage(int idx) {
		int pages = viewerContext.getPageCount();
		if (idx > pages)
			return false;
		viewerContext.setPageIndex(idx);
		return true;
	}
}
