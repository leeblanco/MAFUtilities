package org.maff.utilities.main;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.maff.utilities.Parser.ParseEhdfLogs;
import org.maff.utilities.Reader.ReadDailyTxFiles;
import org.maff.utilities.Reader.ReadEhdfLogs;
import org.maff.utilities.Reader.ReadErrorTxFiles;
import org.maff.utilities.Reader.ReaderUtility;
import org.maff.utilities.controller.ErrorTxController;
import org.maff.utilities.dao.DailyTxDAO;
import org.maff.utilities.init.UtilitiesProperties;
import org.maff.utilities.model.DailyTransaction;
import org.maff.utilities.model.ErrorTransaction;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class UtilitiesMain {

	static final Logger logger = Logger.getLogger(UtilitiesMain.class);
	
	public static void main(String[] args) {
		
//		BouncedCampaign();
//		DailyTransactionTesting();
//		DailyTxTestFileReader();
		
//		testRetrievalFileName();
//		testRetrievalFileReader();
		generateDailyTxFile();
		
//		ErrorRetrievalTesting();
//		deleteAllDlyTx();
	}

	
	/**
	 * Test retrieval of error transaction files
	 * 
	 * @author Lee
	 * Created Date 2016-02-29
	 * 
	 * */
	public static void ErrorRetrievalTesting(){
		
		ReadErrorTxFiles read = new ReadErrorTxFiles();
		ReaderUtility util = new ReaderUtility();
		
		List<String> listOfFileNames = new ArrayList<String>();
		List<ErrorTransaction> listOfErrorTx = new ArrayList<ErrorTransaction>();
		List<ErrorTransaction> tempErrorTx = new ArrayList<ErrorTransaction>();
		
		retrieveErrTxFileNames(read, util, listOfErrorTx);
	}


	public static void retrieveErrTxFileNames(ReadErrorTxFiles read,
			ReaderUtility util, List<ErrorTransaction> listOfErrorTx) {
		List<String> listOfFileNames;
		List<ErrorTransaction> tempErrorTx;
		Pattern fileNameRegex = Pattern.compile("[^*]+|(\\*)");
		
//		String path = "C:\\Users\\Lee\\Documents\\O\\Work\\MAF\\Beam\\DailyTx\\Test\\";
		String path = "C:\\Users\\Lee\\Documents\\O\\Work\\MAF\\Beam\\FilesToProcess\\";
		listOfFileNames = util.listFileNames(path);
		
		logger.info("List of Files ");
		for (String entry: listOfFileNames){
			logger.info(entry);
			
			Matcher regexMatch = fileNameRegex.matcher(entry);
			if (regexMatch.find()){
				
				logger.info("List of Files as matched by Regex "+ entry);
				
				logger.info("Read the file :::::: " + path + entry);
				
				tempErrorTx = read.readErrorTxFileNative(path+entry);
				
				logger.info("Copy temporary record into one data structure ");
				listOfErrorTx.addAll(tempErrorTx);
			}
		}				
		
		logger.info("List of Unified Contents ::::: ");
		
		for (ErrorTransaction entry: listOfErrorTx){
			
			logger.info(entry.getBin()+ " "+ entry.getLastDigits()+ " "+ entry.getHash()+ " "+
			entry.getAccountHash()+ " " + entry.getReferenceNumber()+ " "+ entry.getPurchaseDate()+ " "+
			entry.getMerchant()+ " "+ entry.getAmount()+ " "+ entry.getErrorCategory()+ " "+ entry.getErrorMessage());
		}
	}
	
	
	/**
	 * Test retrieval of file names and reading each file
	 * 
	 * @author Lee
	 * Created Date 2016-02-24
	 * */
	public static void testRetrievalFileReader(){
		
		ReadDailyTxFiles read = new ReadDailyTxFiles();
		ReaderUtility util = new ReaderUtility();
		
		List<String> listOfFileNames = new ArrayList<String>();
		List<DailyTransaction> listOfDlyTx = new ArrayList<DailyTransaction>();
		List<DailyTransaction> tempDlyTx = new ArrayList<DailyTransaction>();
		
		Pattern fileNameRegex = Pattern.compile("[^*]+|(\\*)");
		
//		String path = "C:\\Users\\Lee\\Documents\\O\\Work\\MAF\\Beam\\DailyTx\\Test\\";
		String path = "C:\\Users\\Lee\\Documents\\O\\Work\\MAF\\Beam\\FilesToProcess\\";
		listOfFileNames = util.listFileNames(path);
		
		logger.info("List of Files ");
		for (String entry: listOfFileNames){
			logger.info(entry);
			
			Matcher regexMatch = fileNameRegex.matcher(entry);
			if (regexMatch.find()){
				
				logger.info("List of Files as matched by Regex "+ entry);
				
				logger.info("Read the file :::::: " + path + entry);
				
				tempDlyTx = read.readDailyTxFileSkipHeader(path+entry);
				
				logger.info("Contents of ::: " + entry);
				printDailyTransaction(tempDlyTx);
				
				logger.info("Copy temporary record into one data structure ");
				listOfDlyTx.addAll(tempDlyTx);
			}
		}				
		
		logger.info("List of Unified Contents ::::: ");
		printDailyTransaction(listOfDlyTx);
		
		
		addRecords(listOfDlyTx);
	}

	/**
	 * Store into DB all records parsed from Daily Transaction file
	 * 
	 * @author Lee
	 * Created Date 2016-02-24
	 * */
	public static void addRecords(List<DailyTransaction> listDailyTx){
		
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring4.xml");
		DailyTxDAO dlyDAO = context.getBean(DailyTxDAO.class);

		logger.info("Insert Daily Transaction Object");
		dlyDAO.batchInsert(listDailyTx);
		
		context.close();
	}
	
	/**
	 * Deletes all records in Daily Transactions table
	 * 
	 * @author Lee
	 * Created Date 2016-02-28
	 * */
	public static void deleteAllDlyTx(){
		
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring4.xml");
		DailyTxDAO dao = context.getBean(DailyTxDAO.class);
		
		logger.info("Deleting all records from Daily Transactions table ");
		
		dao.deleteAllDailyTxRecord();
		
	}
	
	/**
	 * Removes header values from list of Daily Transaction objects so that only
	 * real values will remain
	 * 
	 * @author Lee
	 * Created Date 2016-02-21
	 * */
	public static void removeHeaderValues(List<DailyTransaction> listOfDlyTx,List<DailyTransaction> tempDlyTx, List<Integer> listHeaderIndx) {
		//get index number for headers contained in the list
		for (int x=0; x<tempDlyTx.size(); x++){
			
			if (tempDlyTx.get(x).getAccountNumber().equals("accountNumber")){
				logger.info("Store::: "+ x);
				listHeaderIndx.add(x);
			}
		}
		
		//Remove headers using list of indexes stored in previous loop
		for (Integer x: listHeaderIndx){
			tempDlyTx.remove(x);
		}
		listOfDlyTx.addAll(tempDlyTx);
		tempDlyTx.clear();
	}

	/**
	 * Just prints the value of Daily Transaction Object
	 * 
	 * @author Lee
	 * Created Date 2016-02-21
	 * */
	public static void printDailyTransaction(List<DailyTransaction> tempDlyTx) {
		
		int indxCtr = 0;
		for (DailyTransaction obj : tempDlyTx){
			StringBuffer line = new StringBuffer();
			
			line.append(obj.getAccountNumber()+ " "+ obj.getHashedAccountNumber()+ " ");
			line.append(obj.getCardNumber()+ " "+ obj.getHashedCardNumber()+ " "+obj.getMerchantCategoryCode()+" ");
			line.append(obj.getMerchantName()+ " "+ obj.getMerchantCity()+" ");
			line.append(obj.getReferenceNumber()+ " "+ obj.getAuthorizationResponseCode()+ " ");
			line.append(obj.getPostingDate()+ " "+ obj.getPurchaseDate()+ " ");
			line.append(obj.getIsInternational()+ " "+ obj.getTransactionAmount()+" ");
			line.append(obj.getTransactionCode()+" "+ obj.getTransactionType()+ " ");
			line.append(obj.getPctCode());
			
			logger.info(indxCtr+ " "+line);
			indxCtr++;
		}
	}
	
	/**
	 * Test REtrieval of File Names
	 * 
	 * @author Lee
	 * Created Date 2016-02-21
	 * */
	public static void testRetrievalFileName(){
		
		ReadDailyTxFiles read = new ReadDailyTxFiles();
		ReaderUtility util = new ReaderUtility();
		
		List<String> listOfFileNames = new ArrayList<String>();
		
		String path = "C:\\Users\\Lee\\Documents\\O\\Work\\MAF\\Beam\\DailyTx\\";
		
		listOfFileNames = util.listFileNames(path);
		
		logger.info("List of Files ");
		for (String entry: listOfFileNames){
			logger.info(entry);
		}
	}
	
	/**
	 * Daily Transaction Testing method
	 * 
	 * @author Lee
	 * Created Date 2016-02-15
	 * 
	 * */
	public static void DailyTransactionTesting(){
		
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring4.xml");
		DailyTxDAO dlyDAO = context.getBean(DailyTxDAO.class);
		Date createdDate = new Date();
		
		DailyTransaction dlyTx = new DailyTransaction();
		dlyTx.setAccountNumber("123");
		dlyTx.setHashedAccountNumber("123xxx");
		dlyTx.setCardNumber("123456");
		dlyTx.setPurchaseDate("2016-02-14");
		dlyTx.setPostingDate("2016-02-14");
		dlyTx.setHashedCardNumber("123456789");
		dlyTx.setIsInternational("Yes");
		dlyTx.setMerchantCategoryCode("123");
		dlyTx.setMerchantCity("Dubai");
		dlyTx.setMerchantName("Banco de Poro");
		dlyTx.setReferenceNumber("Reference");
		dlyTx.setCreatedDate(createdDate);
		dlyTx.setUpdatedDate(createdDate);
		logger.info("Insert Daily Transaction Object");
		dlyDAO.add(dlyTx);
		
		context.close();
	}
	
	/**
	 * Generates Daily Transaction file which haave encountered credit card hash not found
	 * 
	 * @author Lee
	 * Created Date 2016-03-12
	 * */
	public static void generateDailyTxFile(){
		
		ErrorTxController process = new ErrorTxController();
		String errorLogPath = "C:\\Users\\Lee\\Documents\\O\\Work\\MAF\\Beam\\ErrorFiles\\";
		String savePath = "C:\\Users\\Lee\\Documents\\O\\Work\\MAF\\Beam\\ErrorFiles\\Processed\\DailyTransaction_2016-03-12.txt"; 	
		
		process.createDailyTransaction(errorLogPath, savePath);
	}
	
	public static void DailyTxTestFileReader(){
		
		ReadDailyTxFiles open = new ReadDailyTxFiles();
		String fileName = "C:\\Users\\Lee\\Documents\\O\\Work\\MAF\\Beam\\DailyTx\\Daily_Transaction_06022016.txt";
		List<DailyTransaction> txList = new ArrayList<DailyTransaction>();
		
		txList = open.readDailyTxFile(fileName);
		
		logger.info("List All Transaction");
		for (DailyTransaction tx: txList){

			logger.info(tx.getAccountNumber()+ " - " + tx.getHashedAccountNumber()+ " - "+ tx.getCardNumber()+ " - " +			
			tx.getHashedAccountNumber()+ " - " + tx.getMerchantCategoryCode()+ " - " + tx.getMerchantName()+ " - " +
			tx.getMerchantCity()+ " - " +tx.getReferenceNumber()+ " - " +tx.getAuthorizationResponseCode()+ " - " +
			tx.getPostingDate()+ " - " + tx.getPurchaseDate()+ " - " +tx.getIsInternational()+ " - " + tx.getTransactionAmount()+ " - " +
			tx.getTransactionCode()+ " - " + tx.getTransactionType()+ " - " +tx.getPctCode());
		}
	}
	
	public static void BouncedCampaign() {
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
