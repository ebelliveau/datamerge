package ca.ebelliveau.datamerge;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.IOUtils;


public class JSONReader 
{
	
	public JSONArray readFile(String jsonFile) throws JSONException, FileNotFoundException, IOException {
		// Reads input from the file and converts to JSON
		String input = "";
		try {
			System.out.println("Reading " + jsonFile);
			FileInputStream inputStream = new FileInputStream(jsonFile);
		    input = IOUtils.toString(inputStream, "UTF-8");
		    inputStream.close();
		}catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		return new JSONArray(input);
	}

}