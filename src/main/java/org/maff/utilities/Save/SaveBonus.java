package org.maff.utilities.Save;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

public class SaveBonus {

	private static Logger logger = Logger.getLogger(SaveBonus.class);
	
	public void saveBonus(List<String[]> bonusPtsList, String fileName){
		
		logger.info("Start writing Bonus File ");
		
		try (BufferedWriter write = new BufferedWriter(new FileWriter(fileName))){
			
			logger.info("Saving entries line by line ");
			for (String [] entries: bonusPtsList){
				
				write.write(entries[0] + ","+ entries[1] + ","+ entries[2]);
				write.newLine();
			}
			
		}catch(IOException ioE){
			
			logger.error(ioE);
	
		}
	}
	
	//Monitor if file to be saved exists
	public boolean checkFileExist(String fileName){
		boolean bool = false;
		
		File file = new File(fileName);
		if (file.isFile() && !(file.isDirectory()) ){
			bool = true;
		}
		
		return bool;
	}
}
