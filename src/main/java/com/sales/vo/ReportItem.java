package com.sales.vo;

/**
 * Value object for Product report.
 * @author Manga
 *
 */
public class ReportItem {
	
	private int noOfSales;
	private double salesTotal;
	
	public double getSalesTotal() {
		return salesTotal;
	}
	public void setSalesTotal(double salesTotal) {
		this.salesTotal = salesTotal;
	}
	public int getNoOfSales() {
		return noOfSales;
	}
	public void setNoOfSales(int noOfSales) {
		this.noOfSales = noOfSales;
	}
	

}
