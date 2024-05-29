package com.qa.doingerp.utilities;



import com.qa.doingerp.parameters.FrameworkParameters;
import com.qa.doingerp.utilities.Util;

import java.io.File;


/**
 * Singleton class which manages the creation of timestamped result folders for every test batch execution
 * @author thanan
 * @version 3.0
 * @since Feb 2024
 */
public class TimeStamp
{
	private static String timeStamp;
	
	/**
	 * Function to return the timestamped result folder path
	 * @return The timestamped result folder path
	 */
	public static synchronized String getInstance()
	{
		if(timeStamp == null) {
			FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();
			
			timeStamp = frameworkParameters.getRunConfiguration() +
							frameworkParameters.fileSeparator + "Run_" +
							Util.getCurrentFormattedTime().replace(" ", "_").replace(":", "-");
            
			String reportPathWithTimeStamp =
					frameworkParameters.getRelativePath() + frameworkParameters.fileSeparator +"target" + frameworkParameters.fileSeparator +
					"results" + frameworkParameters.fileSeparator + timeStamp;
            
            new File(reportPathWithTimeStamp).mkdirs();
    		new File(reportPathWithTimeStamp + frameworkParameters.fileSeparator + "Screenshots").mkdir();
		}
		
		return timeStamp;
	}
	
	public Object clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}
}