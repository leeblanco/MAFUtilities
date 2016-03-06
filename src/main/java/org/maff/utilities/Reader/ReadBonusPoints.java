package org.maff.utilities.Reader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class ReadBonusPoints {

	private static Logger logger = Logger.getLogger(ReadBonusPoints.class);
	
	public List<String[]> readBonusPointsFile(String fileName){
		
		List<String[]> storeBonusPts = new ArrayList<String[]>();
		
		logger.info("Begin reading Bonus Points");
		
		try(BufferedReader buffLine = new BufferedReader(new FileReader(fileName))){
			
			String line = buffLine.readLine();
			
			while ( (line = buffLine.readLine()) != null ){
				
				String [] split = line.split("|");
			
				//Store Account Number, Type of Bonus, Bonus Date, Amount
				storeBonusPts.add( new String[] { split[0], split[5], split[9], split[12]} );
				
			}
		}catch(IOException ioE){
			
			logger.error(ioE);
		}
	
		return storeBonusPts;
	}
}
