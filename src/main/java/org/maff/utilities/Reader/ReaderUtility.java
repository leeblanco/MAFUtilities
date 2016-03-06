package org.maff.utilities.Reader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ReaderUtility {

	/**
	 * This will list all files specified on the path
	 * 
	 * @author Lee
	 * Created Date 2016-02-14
	 * */
	public List<String> listFileNames(String fileName){
		
		List<String> fileNames = new ArrayList<String>();
		
		File path = new File(fileName);
		File[] listOfFiles = path.listFiles();
		
		//iterate through all contents and check if content is a file then store in arrayList
		for (File files: listOfFiles){
			
			if (files.isFile()){
				fileNames.add(files.getName());
			}
		}
		
		return fileNames;
	}
}
