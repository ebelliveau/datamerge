package ca.ebelliveau.datamerge;

import org.json.JSONArray;
import org.json.CDL;
import org.json.JSONException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.apache.commons.io.IOUtils;

public class CSVReader 
{

	public JSONArray readFile(String csvFile) throws JSONException, FileNotFoundException {
		// Reads input from the file and converts to JSON
		String input = "";
		try {
			System.out.println("Reading " + csvFile);
			FileInputStream inputStream = new FileInputStream(csvFile);
		    input = IOUtils.toString(inputStream, "UTF-8");
		    inputStream.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		JSONArray toRet = (JSONArray)CDL.toJSONArray(input);
		return toRet;
	}	

}