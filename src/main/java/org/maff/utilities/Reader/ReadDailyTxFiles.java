package org.maff.utilities.Reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.log4j.Logger;
import org.maff.utilities.model.DailyTransaction;

public class ReadDailyTxFiles {

	private static Logger logger = Logger.getLogger(ReadDailyTxFiles.class);
	
	/**
	 * Reads the contents of daily transaction file from CRM and store in DailyTransaction class
	 * 
	 * @author Lee
	 * Created Date 2016-02-14
	 * */
	public List<DailyTransaction> readDailyTxFile(String fileName){
		
		List<DailyTransaction> listOfDailyTx = new ArrayList<DailyTransaction>();
		
		CSVFormat csvProp = CSVFormat.DEFAULT.withDelimiter('|');
		//csvProp.DEFAULT.withSkipHeaderRecord(true);
		
		logger.info("Opening File ::::  "+ fileName);
		
		try(CSVParser parser = new CSVParser(new FileReader(fileName), csvProp)){
		
			logger.info("Set DailyTransaction Object ");
			
			for (CSVRecord record: parser){
				DailyTransaction tx = new DailyTransaction();
				
				tx.setAccountNumber(record.get(0));
				tx.setHashedAccountNumber(record.get(1));
				tx.setCardNumber(record.get(2));
				tx.setHashedCardNumber(record.get(3));
				tx.setMerchantCategoryCode(record.get(4));
				tx.setMerchantName(record.get(5));
				tx.setMerchantCity(record.get(6));
				tx.setReferenceNumber(record.get(7));
				tx.setAuthorizationResponseCode(record.get(8));
				tx.setPostingDate(record.get(9));
				tx.setPurchaseDate(record.get(10));
				tx.setIsInternational(record.get(11));
				tx.setTransactionAmount(record.get(12));
				tx.setTransactionCode(record.get(13));
				tx.setTransactionType(record.get(14));
				tx.setPctCode(record.get(15));
				
				listOfDailyTx.add(tx);
			}
		
		}catch(FileNotFoundException ioE){
			logger.error(ioE);
		}catch(IOException ioE){
			logger.error(ioE);
		}
		
		return listOfDailyTx;
	}
	
	/**
	 * CSV file reader doesn't use APACHE CSV jar
	 * Method was created because we need to skip the header and read only the contents 
	 * of the file
	 * 
	 * @author Lee
	 * Created Date 2016-02-24
	 * */
	public List<DailyTransaction> readDailyTxFileSkipHeader(String fileName){
		
		List<DailyTransaction> listOfDailyTx = new ArrayList<DailyTransaction>();
		
		logger.info("Opening File skipping header ::::  "+ fileName);
		
		try(BufferedReader read2Buf = new BufferedReader(new FileReader (fileName))){
		
			logger.info("Set DailyTransaction Object ");
			
			String line = read2Buf.readLine();
			int iter = 0;
			while ( (line = read2Buf.readLine())!= null){
				DailyTransaction tx = new DailyTransaction();
				String [] record = line.split("\\|");
				
				tx.setAccountNumber(record[0]);
				tx.setHashedAccountNumber(record[1]);
				tx.setCardNumber(record[2]);
				tx.setHashedCardNumber(record[3]);
				tx.setMerchantCategoryCode(record[4]);
				tx.setMerchantName(record[5]);
				tx.setMerchantCity(record[6]);
				tx.setReferenceNumber(record[7]);
				tx.setAuthorizationResponseCode(record[8]);
				tx.setPostingDate(record[9]);
				tx.setPurchaseDate(record[10]);
				tx.setIsInternational(record[11]);
				tx.setTransactionAmount(record[12]);
				tx.setTransactionCode(record[13]);
				tx.setTransactionType(record[14]);
				tx.setPctCode(record[15]);
				
				listOfDailyTx.add(tx);
			}
		
		}catch(FileNotFoundException ioE){
			logger.error(ioE);
		}catch(IOException ioE){
			logger.error(ioE);
		}
		
		return listOfDailyTx;
	} 
}
