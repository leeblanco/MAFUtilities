package org.maff.utilities.Parser;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.maff.utilities.model.ErrorTransaction;

public class HashErrorTxParser {

	private static final Logger logger = Logger.getLogger(HashErrorTxParser.class);
	
	/**
	 * Retrieves all hash card numbers with credit card hash error
	 * 
	 * @author Lee
	 * Created Date 2016-03-06
	 * */
	public List<String> retrieveHashNumber(List<ErrorTransaction> listErrTx){
		
		logger.info("Retrieve Hash Numbers ");
		
		List<String> hashNumList = new ArrayList<String>();
		
		for (ErrorTransaction tx: listErrTx){
			
			if ("Credit Card hash not found".equals(tx.getErrorCategory())){
				logger.info("Hashed Card No :: " +tx.getHash()+ " Error Category::: "+ tx.getErrorCategory());
				hashNumList.add(tx.getHash());
			}
		}
		
		return hashNumList;
	}
	
	/**
	 * Retrieves the Reference Number of Beam transactions that encountered
	 * error and this will be returned to the caller of the method.
	 * 
	 * @author Lee
	 * Created Date 2016-03-12
	 * 
	 * */
	public List<String> retrieveRefNo(List<ErrorTransaction> listErrTx){
		
		logger.info("Retrieve Reference Numbers ");
		
		List<String> refNumList = new ArrayList<String>();
		
		for (ErrorTransaction tx: listErrTx){
			
			if ("Credit Card hash not found".equals(tx.getErrorCategory())){
				logger.info("Hashed Card No :: " +tx.getHash()+ " Error Category::: "+ tx.getErrorCategory());
				refNumList.add(tx.getReferenceNumber());
			}
		}
		
		return refNumList;
	}
}
