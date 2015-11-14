package org.maff.utilities.main;

import java.util.List;

import org.apache.log4j.Logger;
import org.maff.utilities.Parser.ParseEhdfLogs;
import org.maff.utilities.Reader.ReadEhdfLogs;
import org.maff.utilities.init.UtilitiesProperties;

public class UtilitiesMain {

	static final Logger logger = Logger.getLogger(UtilitiesMain.class);
	
	public static void main(String[] args) {
		
		logger.info("Start processing eHDF logs ");
		
		logger.info("Initializing properties file " );
		UtilitiesProperties utilProp = new UtilitiesProperties();
		
		utilProp.initProperties();
		
		ReadEhdfLogs logs = new ReadEhdfLogs();
		ParseEhdfLogs parse = new ParseEhdfLogs();
		
		//String fileName = "C:\\Users\\Lee\\Documents\\O\\Work\\MAF\\Test_Files\\Statements\\2015-11-06_to_11-07_small\\2015-11-06_to_11-07_small.csv";
		String fileName = utilProp.getMapProperties().get("ehdflogfilepath");
		String cycleDate = "11/6/2015";
		
		List<String[]> logsTemp = logs.openEhdfLog(fileName, cycleDate);
		
		parse.parseLogs(logsTemp);

	}

}
