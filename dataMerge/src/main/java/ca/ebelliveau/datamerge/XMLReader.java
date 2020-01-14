package ca.ebelliveau.datamerge;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.json.JSONException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.commons.io.IOUtils;

public class XMLReader 
{

	/*
		XML Format to read:
		<?xml version="1.0"?>
		<records>
			<report>
				<packets-serviced>10</packets-serviced>
				<client-guid>0ab47f58-2c17-4b8d-9bd1-20ca3b924877</client-guid>
				<packets-requested>7</packets-requested>
				<service-guid>3cc76b74-7d16-4651-9699-34332a56f6e7</service-guid>
				<retries-request>8</retries-request>
				<request-time>2016-06-28 17:05:59 ADT</request-time>
				<client-address>100.244.243.255</client-address>
				<max-hole-size>6</max-hole-size>
			</report>
	*/

	public JSONArray readFile(String csvFile) throws JSONException, FileNotFoundException, IOException {
		// Reads input from the file and converts to JSON
		String input = "";
		try {
			System.out.println("Reading " + csvFile);
			FileInputStream inputStream = new FileInputStream(csvFile);
		    input = IOUtils.toString(inputStream, "UTF-8");
		    inputStream.close();
		}catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		JSONObject obj = (JSONObject)XML.toJSONObject(input);
		JSONArray toRet = obj.getJSONObject("records").getJSONArray("report");

		return toRet;
	}	

}