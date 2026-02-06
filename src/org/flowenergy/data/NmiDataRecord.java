package org.flowenergy.data;

import java.util.ArrayList;
import java.util.List;

// for now, only required fields for assignment to prevent over engineering
public class NmiDataRecord {
	private String nmi;
	private int intervalLength;
	private List<IntervalDataRecord> intervalDataRecords = new ArrayList<>();
	
	public String getNmi() {
		return nmi;
	}
	public void setNmi(String nmi) {
		this.nmi = nmi;
	}
	public int getIntervalLength() {
		return intervalLength;
	}
	public void setIntervalLength(int intervalLength) {
		this.intervalLength = intervalLength;
	}
	public List<IntervalDataRecord> getIntervalDataRecords() {
		return intervalDataRecords;
	}
	public void setIntervalDataRecords(List<IntervalDataRecord> intervalDataRecords) {
		this.intervalDataRecords = intervalDataRecords;
	}
	
	
	
	

}
