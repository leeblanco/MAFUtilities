package org.maff.utilities.remote;

import org.apache.log4j.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class RemoteFileTransfer {
	
	static final Logger logger = Logger.getLogger(RemoteFileTransfer.class);
	
	public void transferFile(String hostName, String origin, String destination, String userName, String password){
		
		logger.info("Start transferring file from " + origin + " to :"+ destination);
		
		JSch jsch = new JSch();
		Session session = null;
		
		logger.info("Connecting to.....  "+ hostName);
		
		try{
			
			session = jsch.getSession(userName, hostName, 22);
			session.setConfig("StrictHostKeyChecking", "no");
			session.setPassword(password);
			session.connect();
			
			Channel channel = session.openChannel("sftp");
			channel.connect();
			
			ChannelSftp channelsftp = (ChannelSftp) channel;
			channelsftp.get(origin, destination);
			channelsftp.exit();
			
			
		}catch(JSchException je){
			logger.error(je);
		}catch(SftpException se){
			logger.error(se);
		}finally{
			
			session.disconnect();
		}
	}
}
