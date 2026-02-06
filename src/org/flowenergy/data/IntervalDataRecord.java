package org.flowenergy.data;

import java.math.BigDecimal;
import java.util.List;

//for now, minimum for assignment
public class IntervalDataRecord {
	private String intervalDate;
	private BigDecimal meterSum;
	
	public String getIntervalDate() {
		return intervalDate;
	}
	public void setIntervalDate(String intervalDate) {
		this.intervalDate = intervalDate;
	}
	public BigDecimal getMeterSum() {
		return meterSum;
	}
	public void setMeterSum(BigDecimal meterSum) {
		this.meterSum = meterSum;
	}
	
	
	
	

}
