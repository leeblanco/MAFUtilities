package org.maff.utilities.Parser;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.maff.utilities.Reader.ReadBonusPoints;
import org.maff.utilities.Save.SaveBonus;
import org.maff.utilities.init.UtilitiesProperties;

public class BonusPointsParser {

	private static Logger logger = Logger.getLogger(BonusPointsParser.class);
	UtilitiesProperties props = new UtilitiesProperties();
	ReadBonusPoints bonusFile = new ReadBonusPoints();
	SaveBonus save = new SaveBonus();
	
	public void extractBonusPoints(){
		List<String[]> bonusEntries = new ArrayList<String[]>();
		List<String[]> parsedAMF = new ArrayList<String[]>();
		List<String[]> parsed60Days = new ArrayList<String[]>();
		
		logger.info("Starting Bonus Points File Converter Utility " );
		
		logger.info("Initialize Properties File ");
		props.initProperties();
		
		logger.info("Retrieve Bonus fileName ");
		String bonusFileName = props.getMapProperties().get("bonusPtsFile");
		String bonusFileConvName = props.getMapProperties().get("bonusPtsFileConv");
		
		logger.info("Start reading " + bonusFile);
		bonusEntries.addAll(bonusFile.readBonusPointsFile(bonusFileName));
		
		for (String[] entry: bonusEntries){
			
			if ("60 Days Spend Bonus".equalsIgnoreCase(entry[1])){
				logger.debug("Saving 60 days Spend "+ entry[0] + " - " + entry[1]+ " - "+ entry[2]);
				
				parsed60Days.add(entry);
			}else{
				
				logger.debug("Saving AMF Bonus "+ entry[0] + " - " + entry[1]+ " - "+ entry[2]);
				parsedAMF.add(entry);
			}
		}
		
		if (!(save.checkFileExist(bonusFileConvName)) ){
			
			save.saveBonus(parsed60Days, bonusFileConvName+"60Days.txt");
			save.saveBonus(parsedAMF, bonusFileConvName+"AMF.txt");
		
		}else{
			logger.info("File Exist - Cannot save file");
		}
		
	}
	
}
