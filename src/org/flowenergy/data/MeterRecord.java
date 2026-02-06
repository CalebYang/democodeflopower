package org.flowenergy.data;

import java.math.BigDecimal;
import java.util.Date;

public class MeterRecord {
	private String nmi;
	private String timestamp;
	private BigDecimal total;
	
	public String getNmi() {
		return nmi;
	}
	public void setNmi(String nmi) {
		this.nmi = nmi;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	
	

}
