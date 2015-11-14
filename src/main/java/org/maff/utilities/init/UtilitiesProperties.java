package org.maff.utilities.init;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

import org.apache.log4j.Logger;

public class UtilitiesProperties {

	static final Logger logger = Logger.getLogger(UtilitiesProperties.class);
	private HashMap<String, String> mapProperties = new HashMap<String, String>();
	
	
	public void initProperties(){
		
		HashMap<String, String> mapTemp = new HashMap<String, String>();
		
		//Initialize all properties by reading properties file on resources folder 
		try(InputStream in = (getClass().getClassLoader().getResourceAsStream("utilities.properties"))){
			
			Properties prop = new Properties();
			prop.load(in);
			
			logger.info("Load all configured properties ");
			for ( String property : prop.stringPropertyNames()){
				
				String value = prop.getProperty(property);
				
				mapTemp.put(property, value);
				
				setMapProperties(mapTemp);
				
				logger.info(property + " = " + value);
			}
		}catch(IOException e){
			
			logger.error(e);
		}	
	}


	public HashMap<String, String> getMapProperties() {
		return mapProperties;
	}


	public void setMapProperties(HashMap<String, String> mapProperties) {
		this.mapProperties = mapProperties;
	}
	
	
}
