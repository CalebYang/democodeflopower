package org.flowenergy.csv;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.flowenergy.data.IntervalDataRecord;
import org.flowenergy.data.MeterRecord;
import org.flowenergy.data.NmiDataRecord;

public class DataParser {

	public static final int INVALID_TYPE_DETECTED = -1;
	public static final int READ_HEADER = 0;
	public static final int READ_NEW_NMI_RECORD = 1;
	public static final int READ_INTERVAL_DATA_RECORD = 2;

	public static final String HEADER_RECORD = "100";
	public static final String NMI_DATA_RECORD = "200";
	public static final String INTERVAL_DATA_RECORD = "300";
	public static final String END_DATA_RECORD = "900";

	public static final int MINIMUM_NUMBER_OF_RECORDS = 4;
	public static final int RECORD_TYPE_INDEX = 0;

	// Relevant NMI Fields
	public static final int NMI_INDEX = 1;
	public static final int INTERVAL_LENGTH_INDEX = 8;

	// Relevant INTERVAL_DATA_RECORD
	public static final int ID_INTERVAL_DATE_INDEX = 1;
	public static final int READING_START_INDEX = 2;

	public static final int TOTAL_MINUTES_IN_DAY = 1440;

	public static final String SQL_QUERY = "INSERT INTO meter_readings(nmi,timestamp,consumption) values('{0}',to_timestamp('{1}','YYYYMMDD'),{2})";

	public static List<String> getInsertSQLs(String filename) {
		return getInsertSQLsFromNmiRecords(getNmiRecords(getRawData(filename)));
	}

	public static List<String> getInsertSQLsFromNmiRecords(List<NmiDataRecord> nmiRecords) {
		List<String> sqls = new ArrayList<String>();
		for (NmiDataRecord nmiRecord : nmiRecords) {
			for (IntervalDataRecord intervalRecord : nmiRecord.getIntervalDataRecords()) {
				String query = new String(SQL_QUERY);
				query = query.replace("{0}", nmiRecord.getNmi()).replace("{1}", intervalRecord.getIntervalDate())
						.replace("{2}", "" + intervalRecord.getMeterSum());
				sqls.add(query);
			}
		}

		return sqls;
	}

	public static List<NmiDataRecord> getNmiRecords(List<String[]> rawDataList) {

		if (!validRawRecords(rawDataList)) {
			throw new RuntimeException(
					"Error with file, check that the file has a minumum of a 100,200,300 and 900 record");
		}

		List<NmiDataRecord> nmiDataRecords = new ArrayList<>();
		NmiDataRecord nmiData = null;

		for (int i = 1; i < rawDataList.size(); i++) {
			String[] rawDataRow = rawDataList.get(i);

			if (rawDataRow[RECORD_TYPE_INDEX].equals(NMI_DATA_RECORD)) {
				nmiData = new NmiDataRecord();
				nmiData.setNmi(rawDataRow[NMI_INDEX]);
				nmiData.setIntervalLength(Integer.parseInt(rawDataRow[INTERVAL_LENGTH_INDEX]));
				nmiDataRecords.add(nmiData);
			}

			else if (rawDataRow[RECORD_TYPE_INDEX].equals(INTERVAL_DATA_RECORD)) {
				IntervalDataRecord intervalRecord = new IntervalDataRecord();
				intervalRecord.setIntervalDate(rawDataRow[ID_INTERVAL_DATE_INDEX]);
				intervalRecord.setMeterSum(
						extractSumOfReadingsFromIntervalDataRecord(rawDataRow, nmiData.getIntervalLength()));
				nmiData.getIntervalDataRecords().add(intervalRecord);
			}
		}

		return nmiDataRecords;
	}

	public static BigDecimal extractSumOfReadingsFromIntervalDataRecord(String[] rawDataRow, int intervalLength) {
		int endIndex = READING_START_INDEX + (TOTAL_MINUTES_IN_DAY / intervalLength);
		BigDecimal currentSum = new BigDecimal(0L);
		for (int i = READING_START_INDEX; i < endIndex; i++) {
			currentSum = currentSum.add(new BigDecimal(rawDataRow[i]));
		}

		return currentSum;
	}

	public static boolean validRawRecords(List<String[]> rawDataList) {
		// check simply that there i a minimum of a header, end data record, nmi and
		// interval record before we proceed with processing the data
		return rawDataList.size() >= MINIMUM_NUMBER_OF_RECORDS
				&& rawDataList.get(0)[RECORD_TYPE_INDEX].equals(HEADER_RECORD)
				&& rawDataList.get(1)[RECORD_TYPE_INDEX].equals(NMI_DATA_RECORD)
				&& rawDataList.get(2)[RECORD_TYPE_INDEX].equals(INTERVAL_DATA_RECORD)
				&& rawDataList.getLast()[RECORD_TYPE_INDEX].equals(END_DATA_RECORD);
	}

	public static List<String[]> getRawData(String filename) {
		List<String[]> rawRecords = new ArrayList<String[]>();
		try {
			BufferedReader fromFile = new BufferedReader(new FileReader(filename));
			String data = null;
			while ((data = fromFile.readLine()) != null) {
				rawRecords.add(data.split(","));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return rawRecords;
	}
	
	public static void main (String [] args) {
		List<String> sqls = getInsertSQLs(args[0]);
		if(args.length !=1) {
			System.out.println("Usage java -jar meter_reader.jar DataParser <path_to_source_file>");
		}
		for(String sql: sqls) {
			System.out.println(sql);
		}
	}

}
