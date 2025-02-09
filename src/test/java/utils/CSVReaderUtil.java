package utils;

import java.io.*;
import java.util.*;

public class CSVReaderUtil {

	public static List<Map<String, String>> readCSVData(String fileName) {
	    List<Map<String, String>> dataList = new ArrayList<>();
	    try (InputStream inputStream = CSVReaderUtil.class.getClassLoader().getResourceAsStream(fileName);
	         BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
	        
	        String line;
	        String[] headers = reader.readLine().split(",");
	        
	        while ((line = reader.readLine()) != null) {
	            Map<String, String> dataMap = new HashMap<>();
	            String[] values = line.split(",");
	            
	            for (int i = 0; i < headers.length; i++) {
	                dataMap.put(headers[i], values[i]);
	            }
	            
	            dataList.add(dataMap);
	        }
	        
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    
	    return dataList;
	}

}
