package com.example.demo.payment.dto;

import java.math.BigDecimal;

public class SalesReportDto {

	 private String label;     
	    private BigDecimal totalSales;
	 
	 
	 
	    public SalesReportDto() {}
	 
	    public SalesReportDto(String label, BigDecimal totalSales) {
	        this.label      = label;
	        this.totalSales = totalSales;
	    }
	 

	 
	    public String getLabel()          { return label; }
	    public BigDecimal getTotalSales() { return totalSales; }
	 

	 
	    public void setLabel(String label)            { this.label = label; }
	    public void setTotalSales(BigDecimal totalSales) { this.totalSales = totalSales; }
	
}
