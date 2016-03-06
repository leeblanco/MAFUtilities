package org.maff.utilities.Reader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;
import org.maff.utilities.model.ErrorTransaction;

public class ReadErrorTxFiles {

	private static final Logger logger = Logger.getLogger(ReadErrorTxFiles.class);
	
	/**
	 * Reads error files returned after uploading Daily Transaction
	 * files to Beam. Files will be read one at a time and stored in the list
	 * This method will only return list of transaction errors for every file being
	 * processed.
	 * 
	 * @author Lee
	 * Created Date 2016-02-29
	 * 
	 * */
	public List<ErrorTransaction> readErrorTxFile(String fileName){
	
		CSVFormat csvProp = CSVFormat.DEFAULT.withDelimiter(',');
		List<ErrorTransaction> listErrorTx = new ArrayList<ErrorTransaction>();
		logger.info("Opening File ::::  "+ fileName);
		
		try(CSVParser parser = new CSVParser(new FileReader(fileName), csvProp)){
		
			logger.info("Setting Error Transaction object ");
			
			for (CSVRecord entry: parser){
				ErrorTransaction tx = new ErrorTransaction();
				
				tx.setBin(entry.get(0));
				tx.setLastDigits(entry.get(1));
				tx.setHash(entry.get(2));
				tx.setAccountHash(entry.get(3));
				tx.setReferenceNumber(entry.get(4));
				tx.setPurchaseDate(entry.get(5));
				tx.setMerchant(entry.get(6));
				tx.setAmount(entry.get(7));
				tx.setErrorCategory(entry.get(8));
				tx.setErrorMessage(entry.get(9));
				
				listErrorTx.add(tx);
			}
			
		}catch(IOException e){
			logger.error(e);
		}
		
		return listErrorTx;
	}	
	
	/**
	 * Read all the errror text files without using apache csv
	 * 
	 * @author Lee
	 * Created Date 2016-03-05
	 * */
	public List<ErrorTransaction> readErrorTxFileNative(String fileName){
		
		List<ErrorTransaction> storeErrTxList = new ArrayList<ErrorTransaction>();
		
		try(BufferedReader buf = new BufferedReader(new FileReader(fileName))){
			
			//Read the header
			String line = buf.readLine();
			
			while ( (line = buf.readLine())!= null ){
				
				ErrorTransaction storeErrTx = new ErrorTransaction();
				
				String [] split = line.split(",");
				storeErrTx.setBin(split[0]);
				storeErrTx.setLastDigits(split[1]);
				storeErrTx.setHash(split[2]);
				storeErrTx.setAccountHash(split[3]);
				storeErrTx.setReferenceNumber(split[4]);
				storeErrTx.setPurchaseDate(split[5]);
				storeErrTx.setMerchant(split[6]);
				storeErrTx.setAmount(split[7]);
				storeErrTx.setErrorCategory(split[8]);
				storeErrTx.setErrorMessage(split[9]);

				storeErrTxList.add(storeErrTx);
			}
			
		}catch(IOException ioE){
			logger.error("Error Reading the file using method without using Apache CSV"+ ioE);
		}
		
		return storeErrTxList;
	}
}
