package org.maff.utilities.Save;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

public class CardNumbers {

	private static final Logger logger = Logger.getLogger(CardNumbers.class);
	
	/**
	 * This will create a file similar to New Card File uploaded by BI
	 * 
	 * @author Lee
	 * Created Date 2016-03-06
	 * */
	public void createNewCardFile(List<String> listOfCardNo, String fileName){
		
		logger.info("Start Creating new Card File ");
		
		try(BufferedWriter buf = new BufferedWriter(new FileWriter (fileName))){
			
			buf.write("New Card");
			for (String cardNo : listOfCardNo){
				
				buf.write(cardNo+"\n");	
			}
			
		}catch(IOException ioE){
			logger.error("Error create New card file ::: "+ ioE);
		}
		
		logger.info("File Successfully Created ::: " + fileName);
	}
}
