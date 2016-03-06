package org.maff.utilities.Save;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

public class SaveBouncedCampaign {

	final static Logger logger = Logger.getLogger(SaveBouncedCampaign.class);
	/**
	 * Saves all the success details taken from statement DB
	 * 
	 * @author Lee
	 * Modified Date 2016-01-17
	 * */
	public void saveSuccessBouncedFile(List<String[]> listOfSuccessEntry, String fileName){
		
		try(BufferedWriter buf = new BufferedWriter(new FileWriter(fileName))){
			
			//Write file header
			buf.write("CIF| "+ "Campaign ID|"+ "Account Number|"+ "Card Number|"+ "Mobile No|"+ "Email");
		
			for (String[] arrayEntry: listOfSuccessEntry){
				
				//write bounced campaign file contents
				buf.write(arrayEntry[0]+"|"+arrayEntry[1]+"|"+arrayEntry[2]+"|"+arrayEntry[3]+"|"+arrayEntry[4]+
						"|"+arrayEntry[5]);
			
			}
			
		}catch(IOException ioE){
			logger.error(ioE);
		}
		
	}
}
