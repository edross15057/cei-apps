package com.cei.common;


import lombok.Getter;
import lombok.Setter;
import net.sf.jasperreports.engine.JasperReport;
@Getter
@Setter


public class Screen {
	protected String name;
	protected int seconds;
	protected String parameterName;
	protected String parameterValue;
	protected String parameterName2;
	protected String parameterValue2;
	protected double zoomLevel;
	protected JasperReport report;

	public Screen(
			String name,
			String zoomStr,
			String strSeconds,
			String parameterName,
			String parameterValue,
			String parName2,
			String parValue2)
	{
		this.name = name;
		this.seconds = Integer.parseInt(strSeconds);
		this.parameterName = parameterName;
		this.parameterValue=parameterValue;
		this.parameterName2 = parName2;
		this.parameterValue2=parValue2;
		this.zoomLevel = Double.parseDouble(zoomStr);
	}



}