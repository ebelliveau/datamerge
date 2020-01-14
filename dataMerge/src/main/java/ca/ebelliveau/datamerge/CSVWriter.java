package ca.ebelliveau.datamerge;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.io.output.FileWriterWithEncoding;


public class CSVWriter 
{
	
	public void writeCSV(ArrayList<JSONObject> inputData, String fileName) throws Exception{

		try { 
			CSVPrinter printer = new CSVPrinter(new FileWriterWithEncoding(fileName, "UTF-8"), CSVFormat.EXCEL);

			printer.printRecord("client-address","client-guid","request-time","service-guid","retries-request","packets-requested","packets-serviced","max-hole-size");
			
			for(int x = 0; x < inputData.size(); x++) {
				JSONObject record = inputData.get(x);

				printer.printRecord(record.getString("client-address"), record.getString("client-guid"), record.getString("request-time"), record.getString("service-guid"), record.getInt("retries-request"), record.getInt("packets-requested"), record.getInt("packets-serviced"), record.getInt("max-hole-size"));
				
			}
			System.out.println("Output " + String.valueOf(inputData.size()) + " records to " + fileName);

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}

	}
}