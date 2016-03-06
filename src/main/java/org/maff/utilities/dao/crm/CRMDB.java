package org.maff.utilities.dao.crm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class CRMDB {

	final static Logger logger = Logger.getLogger(CRMDB.class);
	
	private static Connection con;
	private static String username="";
	private static String password="";
	private static String url=" ";
	private static String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	
	static {
		try{
			Class.forName(driver);
		}catch(ClassNotFoundException e){
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection(){
		
		try {
			con = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return con;
	}
	
	
	public static void closeDBResources(ResultSet rs, Statement stmt, Connection con){
		
		try{
			if (rs!=null){
				
				rs.close();
				rs=null;
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		try{
			if (stmt!=null){
				
				stmt.close();
				stmt=null;
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		
		try{
			if (con!=null){
				
				con.close();
				con=null;
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		
	}
	
	
	public static void closeDBResourcesPS(ResultSet rs, PreparedStatement pstmt, Connection con){
		
		try{
			if (rs!=null){
				
				rs.close();
				rs=null;
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		try{
			if (pstmt!=null){
				
				pstmt.close();
				pstmt=null;
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		
		try{
			if (con!=null){
				
				con.close();
				con=null;
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		
	}


	/**
	 * Retrieves the card numbers for Transactions that encountered an error 
	 * when uploading the files to Beam.
	 * 
	 * @author Lee
	 * Created Date 2016-03-05
	 * */
	public List<String> retrieveErrorTxCardNumber(List<String> errorHash){
		
		List<String> listCardNum = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		
		String sqlInParam = concatErrorHashedCardNo(errorHash);
		
		sql.append("SELECT MCI_CARD_NO FROM MAF_CUST_CI WHERE MCI_HASHED_CARD_NO IN ("+sqlInParam+")");
		
		logger.info("QUERY TO RETRIEVE ERROR TRANSACTION FROM HASHEDCARDNUMBER::: " + sql.toString());
		logger.info("IN PARAMETER " + sqlInParam);
		
		
		Connection con = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try{
			
			pstmt = con.prepareStatement(sql.toString());
			
			rs = pstmt.executeQuery();
			
			while (rs.next()){
				
				String cardNo = rs.getString("MCI_CARD_NO");
				logger.info("Card Number: " + cardNo );
				
				listCardNum.add(cardNo);
				
			}
			
		}catch(SQLException sqlE){
			logger.error("Error Query Hashed Card Number "+ sqlE);
		}finally{
			closeDBResourcesPS(rs, pstmt, con);
		}
		
		return listCardNum;
	}


	public String concatErrorHashedCardNo(List<String> errorHash) {
		StringBuffer concat = new StringBuffer();
		for (int x=0; x<errorHash.size(); x++){
			
			if ( x != (errorHash.size()-1) ){
				
				concat.append("'" +errorHash.get(x)+ "', ");
			
			}else{
				concat.append(errorHash.get(x));
			}
		}
		
		return concat.toString();
	}
	
	/**
	 * Query CRM DB for success email IDs will return Account Number and Month
	 * 
	 * @author Lee
	 * */
	public static void retrieveSuccessRecords(List<String> successEmail, String emdFromDate, String emdToDate, String sdFromDate, String sdToDate){
		
		StringBuffer sql = new StringBuffer();
		String indicator = "PRN";
		sql.append("SELECT SD.MSD_CARD_NO \'Account Number\', CONVERT(VARCHAR(10),SD.CREATED_ON,105) \'Month\' ");
		sql.append(" FROM MAF_IMS_SD SD WITH (NOLOCK) LEFT OUTER JOIN ( ");
		sql.append(" SELECT DISTINCT MCDD_ACC_NO,MCDD_LAST_NAME FROM MAF_CUST_DD WITH (NOLOCK) "); 
		sql.append(" WHERE  MCDD_INDICATOR= ? ) DD ON SD.MSD_CARD_NO=DD.MCDD_ACC_NO ");
		sql.append(" LEFT OUTER JOIN MAF_IMS_EMD EMD WITH (NOLOCK) ON SD.MSD_CARD_NO=EMD.MEMD_CARD_NO AND MEMD_STMT_FLAG IS NULL AND ");
		sql.append(" CAST(CONVERT(VARCHAR(10),EMD.CREATED_ON,103) AS DATETIME)>= ? ");
		sql.append(" AND CAST(CONVERT(VARCHAR(10),EMD.CREATED_ON,103) AS DATETIME)<= ? ");
		sql.append(" WHERE  CAST(CONVERT(VARCHAR(10),SD.CREATED_ON,103) AS DATETIME)>= ? ");
		sql.append(" AND CAST(CONVERT(VARCHAR(10),SD.CREATED_ON,103) AS DATETIME)<= ? and EMD.MEMD_TO_MID = ?) ");
		
		/*String emdFromDate = "10/12/2015";
		String emdToDate = "11/12/2015";
		String sdFromDate = "10/12/2015";
		String sdToDate = "11/12/2015";
*/		
		logger.info("Query ::::: "+ sql.toString());
		
		//Create a connection to CRM DB
		Connection con = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		
		try{
			
			pstmt = con.prepareStatement(sql.toString());
			
			for (String email : successEmail){
				
				pstmt.setString(1, indicator);
				pstmt.setString(2, emdFromDate);
				pstmt.setString(3, emdToDate);
				pstmt.setString(4, sdFromDate);
				pstmt.setString(5, sdToDate);
				pstmt.setString(6, email);
				
				logger.info("1 - "+ indicator + " 2 - " + emdFromDate + " 3 - "+ emdToDate + " 4 - "+ sdFromDate + " 5 - "+ sdToDate+ " 6 - "+ email);
				rs = pstmt.executeQuery();
			
			}
		}catch(SQLException sqlE){
			
			logger.info("Oppsy opps error look for success account number "+ sqlE.getMessage());
		
		}finally{
			
			//close resources
			closeDBResourcesPS(rs,pstmt,con);
		}
	}
	
	/**
	 * Query will retrieve the account number which has errors which will be passed on to next query 
	 * 
	 * @author Lee
	 * Modified by Lee 2016-01-16
	 * 
	 * */	
	public static void retrieveErrorAccountNumber(List<String> errorMailList, String emdFromDate, String emdToDate, String sdFromDate, String sdToDate){
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT SD.MSD_CARD_NO, CONVERT(VARCHAR(10),SD.CREATED_ON,105) as 'Cycle_Date', emd.MEMD_TO_MID as Email ");
		sql.append(" FROM MAF_IMS_SD SD WITH (NOLOCK) LEFT OUTER JOIN (SELECT DISTINCT MCDD_ACC_NO,MCDD_LAST_NAME FROM MAF_CUST_DD WITH (NOLOCK) ");
		sql.append(" WHERE  MCDD_INDICATOR= ?) DD ON SD.MSD_CARD_NO=DD.MCDD_ACC_NO LEFT OUTER JOIN MAF_IMS_EMD EMD WITH (NOLOCK) ");
		sql.append(" ON SD.MSD_CARD_NO=EMD.MEMD_CARD_NO AND MEMD_STMT_FLAG IS NULL AND ");
		sql.append(" CAST(CONVERT(VARCHAR(10),EMD.CREATED_ON,103) AS DATETIME)>= ? ");
		sql.append(" AND CAST(CONVERT(VARCHAR(10),EMD.CREATED_ON,103) AS DATETIME)<= ? "); 
		sql.append(" WHERE  CAST(CONVERT(VARCHAR(10),SD.CREATED_ON,103) AS DATETIME)>= ? "); 
		sql.append(" AND CAST(CONVERT(VARCHAR(10),SD.CREATED_ON,103) AS DATETIME)<= ? ");
		sql.append(" AND EMD.MEMD_TO_MID = ? ");

		String indicator = "PRN";
		
		logger.info("Query ::::: "+ sql.toString());
		
		//Create a connection to CRM DB
		Connection con = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try{
			
			pstmt = con.prepareStatement(sql.toString());
			
			for (String email : errorMailList){
				
				pstmt.setString(1, indicator);
				pstmt.setString(2, emdFromDate);
				pstmt.setString(3, emdToDate);
				pstmt.setString(4, sdFromDate);
				pstmt.setString(5, sdToDate);
				pstmt.setString(6, email);
				
				logger.info("1 - "+ indicator + " 2 - " + emdFromDate + " 3 - "+ emdToDate + " 4 - "+ sdFromDate + " 5 - "+ sdToDate+ " 6 - "+ email);
				rs = pstmt.executeQuery();
			
			}
		}catch(SQLException sqlE){
			
			logger.info("Oppsy opps error on first query to look for error account number " + sqlE.getMessage());
		
		}finally{
			
			//close resources
			closeDBResourcesPS(rs,pstmt,con);
		}
	}
	
	/**
	 * Query will retrieve the information needed to create error campaign file
	 * 
	 * @author Lee
	 * Modified by Lee 2016-01-16
	 * 
	 * */
	public static void retrieveErrorDetails(List<String> errorAccNumbers ){
		
		StringBuffer sql = new StringBuffer();
		
		sql.append(" with sequenced as ");
		sql.append(" ( 	SELECT *, ROW_NUMBER() over (PARTITION BY mci_cust_id order by mci_card_no) as sequenced_id ");
		sql.append(" FROM MAF_CUST_CI where MCI_ACC_NO = ? AND MCI_INDICATOR = ? )");
		sql.append(" SELECT  mci_cust_id as 'Customer ID', mci_acc_no as 'Account Number', MCI_CARD_NO as 'Card Number', ");
		sql.append(" MCI_CUST_MOBILE_NO as 'Mobile No', MCI_CUST_EMAIL_ID as Email from sequenced with(nolock) where sequenced_id=1 ");
		
		String indicator = "PRN";
		
		logger.info("Query ::::: "+ sql.toString());
		
		//Create a connection to CRM DB
		Connection con = getConnection();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try{
			
			pstmt = con.prepareStatement(sql.toString());
			
			for (String accNumber : errorAccNumbers){
				
				pstmt.setString(1, accNumber);
				pstmt.setString(2, indicator);
				
				logger.info("1 - "+ accNumber + " 2 - " + indicator );
				rs = pstmt.executeQuery();
			
			}
		}catch(SQLException sqlE){
			
			logger.info("Oppsy opps error on 2nd query to look for error account number " + sqlE.getMessage());
		
		}finally{
			
			//close resources
			closeDBResourcesPS(rs,pstmt,con);
		}
	}
	
	//for testing
	public static void selectRecords(){
		
		String hCardNo = "228183461471521479491186987205180841422032464464195";
//		String sql = "SELECT * FROM MAF_CUST_CI WITH(NOLOCK) WHERE MCI_HASHED_CARD_NO = \'"+ hCardNo+"\'";
		String sql = "SELECT * FROM MAF_CUST_CI WITH(NOLOCK) WHERE MCI_HASHED_CARD_NO = ?";
//		String sql = "SELECT top 1 * FROM MAF_CUST_CI WITH(NOLOCK) ";
		String cardNo = "";
		
		
		Connection selCon = getConnection();
//		Statement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try{
			
			
//			stmt = selCon.createStatement();
			pstmt = selCon.prepareStatement(sql);
			pstmt.setString(1, hCardNo);
			
			logger.info(sql);
			
//			rs = stmt.executeQuery(sql);
			rs = pstmt.executeQuery();
			
			while (rs.next()){
				
				//int cardNoIndx = rs.getInt("MCI_CARD_NO");
				cardNo = rs.getString("MCI_CARD_NO");
				
				logger.info("Card No: "+ cardNo);
			}
			
		}catch(SQLException e){
		
			e.printStackTrace();
			
		}finally{
		
			closeDBResources(rs,pstmt,selCon);
			
		}
		
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		logger.info("Connect to CRM ADMIN ");
		
		selectRecords();
	}
	
	

}
