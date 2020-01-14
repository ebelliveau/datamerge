package ca.ebelliveau.datamerge;

import ca.ebelliveau.datamerge.JSONReader;
import ca.ebelliveau.datamerge.CSVReader;
import ca.ebelliveau.datamerge.XMLReader;
import ca.ebelliveau.datamerge.CSVWriter;

import java.util.*;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.math.BigInteger;


import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.CDL;


public class Submission 
{

	private JSONReader jsonReader;
	private CSVReader csvReader;
	private CSVWriter csvWriter;
	private XMLReader xmlReader;
	private JSONObject guidStats;


	private String formatTime(Long input) {
		// Convert the epoch time encountered in the JSON output to a well-formatted TZ string
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
		return sdf.format(new Date(input));
	}

	private BigInteger getEpochTime(String input) throws ParseException {
		// Convert the TZ-formatted string into its epoch equivalent for sorting
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
		Date theDate = sdf.parse(input);
		BigInteger toRet = BigInteger.valueOf(theDate.getTime());
		return toRet;

	}

	private void updateStats(JSONObject input) {

		if(!this.guidStats.has(input.getString("service-guid"))) {
			this.guidStats.put(input.getString("service-guid"), input.getInt("packets-serviced"));
		}else {
			this.guidStats.put(input.getString("service-guid"), input.getInt("packets-serviced") + this.guidStats.getInt(input.getString("service-guid")));
		}

	}

	private void run() throws Exception {
		this.jsonReader = new JSONReader();
    	this.csvReader = new CSVReader();
    	this.csvWriter = new CSVWriter();
    	this.xmlReader = new XMLReader();
    	this.guidStats = new JSONObject();

    	/*
			For reference:

			CSV column order:  

			client-address,client-guid,request-time,service-guid,retries-request,packets-requested,packets-serviced,max-hole-size
    	*/

		ArrayList<JSONObject> output_list = new ArrayList<JSONObject>();

    	try {
    		// Gather the input
	    	JSONArray json_input = this.jsonReader.readFile("../../reports.json");
	    	JSONArray csv_input = this.csvReader.readFile("../../reports.csv"); // Regarding the type <JSONArray> This will make sense later for sorting
	    	JSONArray xml_input = this.xmlReader.readFile("../../reports.xml"); // Ditto.

	    	// Iterate over the JSON input
	    	for(int x = 0; x < json_input.length(); x++) {
	    		JSONObject entity = json_input.getJSONObject(x);
	    		if(entity.getInt("packets-serviced") > 0) {
	    			// Correct the request-time formatting from epoch to TZ
	    			entity.put("request-time", this.formatTime(entity.getLong("request-time")));
		    		output_list.add(entity);
		    		this.updateStats(entity); 			
	    		}

	    	}

	    	// Iterate over the CSV input:

	    	for(int x = 0; x < csv_input.length(); x++) {
	    		JSONObject entity = csv_input.getJSONObject(x);
	    		if(entity.getInt("packets-serviced") > 0) {
	    			output_list.add(entity);
	    			this.updateStats(entity);
	    		}
	    	}

	    	// Iterate over the XML input:

	    	for(int x = 0; x < xml_input.length(); x++) {
	    		JSONObject entity = xml_input.getJSONObject(x);
	    		if(entity.getInt("packets-serviced") > 0) {
	    			output_list.add(entity);
	    			this.updateStats(entity);
	    		}
	    	}

	    	// Sort the output list via custom epoch comparator derived from TZ-formatted data:

	    	Collections.sort(output_list, new Comparator<JSONObject>() {
	    		@Override
	    		public int compare(JSONObject o1, JSONObject o2) {
	    			//Get and compare the epoch time from the JSONObject's request-time field:
	    			try {
		    			BigInteger b1 = getEpochTime(o1.getString("request-time"));
		    			BigInteger b2 = getEpochTime(o2.getString("request-time"));	
		    			return b1.compareTo(b2);	    				
	    			}catch (Exception ex) {
	    				//System.out.println("Exception caught parsing epoch time");
	    				//System.out.println(o1.getString("request-time"));
	    				return 0;
	    			}

	    		}
	    	});

	    	// Output the final sorted CSV data to reports-output.csv in the top-level directory:
	    	this.csvWriter.writeCSV(output_list, "../../reports-output.csv");

	    	// Output the service-guid stats:
	    	Iterator<Object> statsIterator = this.guidStats.names().iterator();

	    	while(statsIterator.hasNext()) {
	    		String label = statsIterator.next().toString();
	    		System.out.println(label + "\t" + this.guidStats.getInt(label));
	    	}

    	}catch (Exception e) {
    		System.out.println("Exception in main");
    		e.printStackTrace();
    		throw e;
    	}
	}

    public static void main( String[] args ) throws Exception{

		Submission submission = new Submission();

    	submission.run();

    }
}
