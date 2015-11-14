package org.maff.utilities.Reader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class ReadEhdfLogs {

	static final Logger logger = Logger.getLogger(ReadEhdfLogs.class);
	
	//opens eHDF logs
	public List<String[]> openEhdfLog(String fileName, String date){
		
		List<String []> storeEntries = new ArrayList<String[]>();
		
		logger.info("Reading file "+ fileName);
		
		try(BufferedReader buffLine = new BufferedReader(new FileReader(fileName)) ) {	
			
			String line = buffLine.readLine();
			
			while ( (line = buffLine.readLine()) != null ){
				
				String [] split = line.split(",");
				
				if (date.equals(split[2])){
				
					if ("estatement@najmcards.ae".equals(split[6])){
						//stores receiver, status
						storeEntries.add( new String[]{ split[7],split[12] });
					}
				}	
			}
		}catch(IOException e){
			
			logger.error(e);
		}	
		
		return storeEntries;
	}	
	
}
