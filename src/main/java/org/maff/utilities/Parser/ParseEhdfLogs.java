package org.maff.utilities.Parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;


public class ParseEhdfLogs {

	static final Logger logger = Logger.getLogger(ParseEhdfLogs.class);

	
	public void parseLogs(List<String[]> logs){
		
		logger.info("Separate Error and OK emails");
		
		HashMap<String, String> ok = new HashMap<String, String>();
		HashMap<String, String> error = new HashMap<String, String>();
		
		for (String [] entry: logs){
			
			if ("OK".equals(entry[1])){
				
				//storeSuccessfullySentMails(entry);
				ok.put(entry[0], entry[1]);
			}else{
				
				//storeErrorMails(entry);
				error.put(entry[0], entry[1]);
				
			}
		}
		
		logger.info("Number of entries for Successfully Sent mails - "+ ok.size());
		logger.info("Number of entries for Unsuccessfully sent mails - " + error.size());
		
		HashMap<String, String> removeErrorFromSuccessList = compareErrorAndSuccessMails(ok, error);
		
		//remove the unsuccessful emails from success list using removeErrorromSuccessList Map
		for (Map.Entry<String, String> entryError : removeErrorFromSuccessList.entrySet()){
			
			String key = entryError.getKey();
			String val = entryError.getValue();
			
			if (ok.containsKey(key)){
				ok.remove(key);
			}
		}
		
		logger.info("Number of entries for Successfully Sent mails - "+ ok.size());
		
		logger.info("List of successfully sent mails ");
		showContents(ok);
		
		logger.info("List of unsuccessfully sent mails ");
		showContents(error);
		
	}
	
	public HashMap<String, String> storeSuccessfullySentMails(String [] okMails){
		
		HashMap<String, String> ok = new HashMap<String, String>();
		
		ok.put(okMails[0], okMails[1]);
		
		return ok;
	}
	
	public HashMap<String, String> storeErrorMails(String [] errorMails){
		
		HashMap<String, String> error = new HashMap<String, String>();
		
		error.put(errorMails[0], errorMails[1]);
		
		return error;
	}
	
	//compare email ids on success with list on error 
	public HashMap<String, String> compareErrorAndSuccessMails(HashMap<String, String> ok, HashMap<String, String> error){
		
		HashMap<String, String> success = new HashMap<String, String>();
		
		logger.info("Secondary check if some email ids in success list is on error list ");
		for (Map.Entry<String, String> contents: error.entrySet()){
			
			String errorKey = contents.getKey();
			String errorVal = contents.getValue();
			
			if (ok.containsKey(errorKey)){
				
				success.put(errorKey, errorVal);
			}
		}
		
		return success;
	}
	
	public void showContents(HashMap<String, String> list){
		
	 	for (Map.Entry<String, String> entry: list.entrySet()){
	 		
	 		String key = entry.getKey();
	 		String val = entry.getValue();
	 		
	 		logger.info(key + " - "+ val);
	 	}
	}
}
