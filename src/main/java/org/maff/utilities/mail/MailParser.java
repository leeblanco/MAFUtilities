package org.maff.utilities.mail;

import java.io.IOException;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.maff.utilities.init.UtilitiesProperties;

public class MailParser {

	private static Logger logger = Logger.getLogger(MailParser.class);
	private static UtilitiesProperties prop = new UtilitiesProperties();
	private static Session session = null;
	
	
	//initiate mail authentication to gmail account
	public static Session smtpMailSession(){
		
		logger.info("Initiate Gmail Session");
		logger.info("Authentication.... ");
		session = Session.getInstance(prop.getProperties(), new javax.mail.Authenticator(){
			protected PasswordAuthentication getPasswordAuthentication(){
				return new PasswordAuthentication(prop.getMapProperties().get("emailUserName"), prop.getMapProperties().get("emailPassword"));
			}
		});
		
		return session;
	}
	
	public static Session imapMailSession(){
		
		logger.info("Initiate Gmail Session");
		logger.info("Authentication.... ");
		session = Session.getDefaultInstance(prop.getProperties());
		
		return session;
	}
	
	public static void sendMail(){
		
		smtpMailSession();
		
		try{
			
			logger.info("Composing Message ");
			
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("elbram803@gmail.com"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("lee.blanco.2@gmail.com"));
			message.setSubject("Test mail bon voyager");
			message.setText("Success");
			
			Transport.send(message);
			
			logger.info("Message sent");
		
		}catch(MessagingException me){
			logger.error(me);
		}
	}
	
	public static void readMail(){
		
		imapMailSession();
		
		try{
			logger.info("Reading email");
			
			Store store = session.getStore("imaps");
			store.connect("smtp.gmail.com","elbram803@gmail.com", "197346825");
			
			Folder inbox = store.getFolder("inbox");
			inbox.open(Folder.READ_ONLY);
			int messageCount = inbox.getMessageCount();
			
			logger.info("Number of inbox messages "+ messageCount);
			
			inbox.close(true);
			store.close();
			
		}catch(Exception e){
			logger.error(e);
		}
	}
	
	public static void main(String [] args){
		
//		sendMail();
		
		readMail();
	}
}
