package com.qa.doingerp.reports;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.qa.doingerp.exception.UnExpectedException;
import com.qa.doingerp.parameters.FrameworkParameters;
import com.qa.doingerp.parameters.TestParameters;
import com.qa.doingerp.utilities.Settings;
import com.qa.doingerp.utilities.Util;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;


/**
 * Class to encapsulate all the reporting features of the framework
 * @author thanan
 * @version 1.0
 * @since Feb 2024
 */
public class Report
{
	private String reportPath, reportName;
	private int stepNumber;
	private int nStepsPassed = 0, nStepsFailed = 0;
	private int nTestsPassed = 0, nTestsFailed = 0;
	private int logLevel;
	private final List<ReportType> reportTypes = new ArrayList<ReportType>();
	private WebDriver driver;

	
	private final FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();
	private final Properties properties = Settings.getInstance();

	public ExtentTest getExtentTest() {
		return extentTest;
	}

	public void setExtentTest(ExtentTest extentTest) {
		this.extentTest = extentTest;
	}

	private ExtentTest extentTest;
	
	/**
	 * The status of the test being executed
	 */
	public String testStatus = "Passed";
	/**
	 * The description of the failure (applicable only if the script fails)
	 */
	public String failureDescription;
	
	
	/**
	 * Function to set the path where the reports are stored
	 * @param reportPath The report path
	 * @see #setReportName(String)
	 */
	public void setReportPath(String reportPath)
	{
		this.reportPath = reportPath;
	}
	
	/**
	 * Function to set the report name
	 * @param reportName The report name
	 * @see #setReportPath(String)
	 */
	public void setReportName(String reportName)
	{
		System.out.println("Set report name to " + reportName);
		this.reportName = reportName;
	}
	
	/**
	 * Function to set the {@link WebDriver} object
	 * @param driver The {@link WebDriver} object
	 */
	public void setDriver(WebDriver driver)
	{
		this.driver = driver;
	}
	
	/**
	 * Function to set the logging level of the reports
	 * @param logLevel The logging level (varies from 0-minimal to 5-detailed)
	 */
	public void setReportLogLevel(int logLevel)
	{
		this.logLevel = logLevel;
	}
	
	
	/**
	 * Function to initialize the different types of reports
	 */
	public void initialize()
	{
		if(reportName != null)
			reportName = reportName.replace(",","_").replace("-","_").replace(" ", "");

		if(properties.getProperty("HTMLReport").equals("True")) {
			new File(reportPath + frameworkParameters.fileSeparator + "HTML Results").mkdir();
			reportTypes.add(new HtmlReport(reportPath, reportName));
		}

	}
	
	/**
	 * Function to create the Test log files and initialize the headers
	 */
	public void createTestLogHeader(String iterationMode, int startIteration, int endIteration)
	{
        for (ReportType reportType : reportTypes) {
            reportType.createTestLogHeader(reportName, iterationMode, startIteration, endIteration);
        }
	}
	
	/**
	 * Function to create the Result Summary files and initialize the headers
	 */
	public void createResultSummaryHeader()
	{
        for (ReportType reportType : reportTypes) {
            reportType.createResultSummaryHeader();
        }
	}
	
	/**
	 * Function to add headers representing the beginning of an iteration
	 * @param currentIteration The current iteration number being executed
	 */
	public void createIterationHeader(int currentIteration)
	{
        for (ReportType reportType : reportTypes) {
            reportType.createIterationHeader(currentIteration);
        }
		
		stepNumber = 1;
	}
	
	/**
	 * Function to add headers representing a section in the test log
	 * @param sectionName The name of the section to be added
	 */
	public void createSectionHeader(String sectionName)
	{
        for (ReportType reportType : reportTypes) {
            reportType.createSectionHeader(sectionName);
        }
	}
	
	/**
	 * Function to update the test log with the details of a particular test step
	 * @param stepName The test step name
	 * @param stepDescription The description of what the test step does
	 * @param stepStatus The status of the test step
	 */
	public void updateTestLog(String stepName, String stepDescription, Status stepStatus)
	{
		if(stepStatus.equals(Status.FAIL)) {

			testStatus = "Failed";
			
			if(failureDescription == null) {
				failureDescription = stepDescription;
			} else {
				failureDescription = failureDescription + "; " + stepDescription;
			}
			
			nStepsFailed++;
		}
		
		if(stepStatus.equals(Status.PASS)) {
			nStepsPassed++;
		}
		
		if(stepStatus.ordinal() <= logLevel)
		{	
			FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();
			
			String screenshotName = null;
			
			if(stepStatus.equals(Status.FAIL)) {
				boolean takeScreenshotFailedStep = Boolean.parseBoolean(properties.getProperty("TakeScreenshotFailedStep"));

				if(takeScreenshotFailedStep) {
					screenshotName = reportName + "_" + Util.getCurrentFormattedTime().replace(" ", "_").replace(":", "-") + ".png";
					String screenShotPath = reportPath + frameworkParameters.fileSeparator + "Screenshots" + frameworkParameters.fileSeparator + screenshotName;
					takeScreenshot(screenShotPath);
					stepDescription = "Screenshot - " + screenshotName + "<br />" + stepDescription;
					try{
						extentTest.info("Error");
						extentTest.assignCategory(stepName);
						extentTest.fail(stepDescription, MediaEntityBuilder.createScreenCaptureFromPath("Screenshots" + frameworkParameters.fileSeparator + screenshotName).build());
					}catch (Exception ignored){}

				}else{
					extentTest.info("Error");
					extentTest.assignCategory(stepName);
				}
			}
			
			if(stepStatus.equals(Status.PASS)) {
				Boolean takeScreenshotPassedStep = Boolean.parseBoolean(properties.getProperty("TakeScreenshotPassedStep"));

				if(takeScreenshotPassedStep) {
					screenshotName = reportName + "_" + Util.getCurrentFormattedTime().replace(" ", "_").replace(":", "-") + ".png";
					String screenShotPath = reportPath + frameworkParameters.fileSeparator + "Screenshots" + frameworkParameters.fileSeparator + screenshotName;
					takeScreenshot(screenShotPath);
					stepDescription = "Screenshot - " + screenshotName + "<br />" + stepDescription;
					try{
						extentTest.fail(stepName, MediaEntityBuilder.createScreenCaptureFromPath("Screenshots" + frameworkParameters.fileSeparator + screenshotName).build());
					}catch (Exception ignored){}
				}else{
					extentTest.pass(stepName);
				}
			}
			
			if(stepStatus.equals(Status.SCREENSHOT))
			{

				screenshotName = reportName + "_" + Util.getCurrentFormattedTime().replace(" ", "_").replace(":", "-") + ".png";
				takeScreenshot(reportPath + frameworkParameters.fileSeparator + "Screenshots" + frameworkParameters.fileSeparator + screenshotName);
				stepDescription = "Screenshot - " + screenshotName + "<br />" + stepDescription;
								}

            for (ReportType reportType : reportTypes) {
                reportType.updateTestLog(Integer.toString(stepNumber), stepName, stepDescription, stepStatus, screenshotName);
            }
			
			stepNumber++;
		}
	}



	/**
	 * Function to take a screenshot and store in the specified path
	 * @param screenshotPath Absolute path of the folder where the screenshot is to be saved
	 */
	private void takeScreenshot(String screenshotPath)
	{
		if (driver == null) {
			throw new UnExpectedException("Report.driver is not initialized!");
		}
		
		
			File scrFile = null;
			if(driver.getClass().equals(RemoteWebDriver.class))
			{
				WebDriver augmentedDriver = new Augmenter().augment(driver);
				scrFile=((TakesScreenshot) augmentedDriver).getScreenshotAs(OutputType.FILE);
				
				try {
					FileUtils.copyFile(scrFile, new File(screenshotPath), true);
				} catch (IOException e) {
					System.err.println(Arrays.toString(e.getStackTrace()));
					throw new UnExpectedException("Error while writing screenshot to file");
				}
				
			}
			else
			{
				if(!(getCurrentBrowser().contains("internetexplorer")))
				{
					
					
		              
		              if(driver.getClass().equals(RemoteWebDriver.class)){
		                     WebDriver augmentedDriver = new Augmenter().augment(driver);
		                     scrFile = ((TakesScreenshot) augmentedDriver).getScreenshotAs(OutputType.FILE);
		              }
		              else
		                     scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

					try 
					{
						FileUtils.copyFile(scrFile, new File(screenshotPath), true);
					} 
					catch (IOException e)
					{
					e.printStackTrace();
					throw new UnExpectedException("Error while writing screenshot to file");
					}
				}
				else
				{
					// screenshot proceedure for internet explorer
					
					try
					{
						Robot robot = new Robot();
		            
						Rectangle captureSize = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
						BufferedImage bufferedImage = robot.createScreenCapture(captureSize);
		            
						ImageIO.write(bufferedImage, "png", new File(screenshotPath));
		            }
					catch(Exception ex)
					{
		                ex.printStackTrace();
		            }

				}
			}
			
		
	}
	

	/**
	 * Function to update the results summary with the status of the executed test case
	 * @param testParameters The {@link TestParameters} object
	 * @param executionTime The time taken to execute the test case
	 * @param testStatus The Pass/Fail status of the test case
	 */
	public void updateResultSummary(TestParameters testParameters, String executionTime, String testStatus)
	{
		if(testStatus.equalsIgnoreCase("failed"))
		{
			nTestsFailed++;
		}
		else if(testStatus.equalsIgnoreCase("passed"))
		{
			nTestsPassed++;
		}

        for (ReportType reportType : reportTypes) {
            String strDeviceName = testParameters.getDevicename().replaceAll(" ", "");
            //TODO Change made here; verify it works
            reportType.updateResultSummary(testParameters.getCurrentScenario(),
                    testParameters.getCurrentCleanTestcase() + "_" + strDeviceName,
                    testParameters.getCurrentTestDescription(),
                    executionTime, testStatus);
        }
	}
	
	/**
	 * Function to create footers to close the test log files
	 * @param executionTime The time taken to execute the test case
	 */
	public void createTestLogFooter(String executionTime)
	{
        for (ReportType reportType : reportTypes) {
            reportType.createTestLogFooter(executionTime, nStepsPassed, nStepsFailed);
        }
	}
	
	/**
	 * Function to create footers to close the results summary files
	 */
	public void createResultSummaryFooter(String totalExecutionTime)
	{
		for(int i=0; i < reportTypes.size(); i++) {
			reportTypes.get(i).createResultSummaryFooter(totalExecutionTime, nTestsPassed, nTestsFailed);
		}
	}
	
	public String getCleanTestCaseName()
	{
		return reportName.replace(",","_").replace("-","_").replace(" ", "");
	}


/**
       * Function to update the test log with the details of a particular test step
       * @param stepName The test step name
       * @param stepDescription The description of what the test step does
       * @param stepStatus The status of the test step
       * @param screenshotName Name of the screenshot
       */
       public void updateTestLog(String stepName, String stepDescription, Status stepStatus, String screenshotName)
       {
              if(stepStatus.equals(Status.FAIL)) {
                     testStatus = "Failed";
                     
                     if(failureDescription == null) {
                           failureDescription = stepDescription;
                     } else {
                           failureDescription = failureDescription + "; " + stepDescription;
                     }
                     
                     nStepsFailed++;
              }
              
              if(stepStatus.equals(Status.PASS)) {
                     nStepsPassed++;
              }
              
              if(stepStatus.ordinal() <= logLevel)
              {      
                     FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();
                     if(screenshotName == null)
                           screenshotName = reportName + "_" + Util.getCurrentFormattedTime().replace(" ", "_").replace(":", "-") + ".png";
                     
                     if(stepStatus.equals(Status.FAIL)) {
                           boolean takeScreenshotFailedStep = Boolean.parseBoolean(properties.getProperty("TakeScreenshotFailedStep"));
                           
                           if(takeScreenshotFailedStep) {
                                  takeScreenshot(reportPath + frameworkParameters.fileSeparator + "Screenshots" + frameworkParameters.fileSeparator + screenshotName);
                                  stepDescription = "Screenshot - " + screenshotName + "<br />" + stepDescription;
                           }
                     }
                     
                     if(stepStatus.equals(Status.PASS)) {
                           boolean takeScreenshotPassedStep = Boolean.parseBoolean(properties.getProperty("TakeScreenshotPassedStep"));
                           if(takeScreenshotPassedStep) {
                                  takeScreenshot(reportPath + frameworkParameters.fileSeparator + "Screenshots" + frameworkParameters.fileSeparator + screenshotName);
                                  stepDescription = "Screenshot - " + screenshotName + "<br />" + stepDescription;
                           }
                     }
                     
                     if(stepStatus.equals(Status.SCREENSHOT))
                     {
                           takeScreenshot(reportPath + frameworkParameters.fileSeparator + "Screenshots" + frameworkParameters.fileSeparator + screenshotName);
                           stepDescription = "Screenshot - " + screenshotName + "<br />" + stepDescription;
                     }

                  for (ReportType reportType : reportTypes) {


                      reportType.updateTestLog(Integer.toString(stepNumber), stepName, stepDescription, stepStatus, screenshotName);
                  }
                     
                     stepNumber++;
              }
       }

       public String getCurrentBrowser()
       {
   			String sCurrentBrowser="";
   			String sCurrentBrowserText = (String) ((JavascriptExecutor) driver).executeScript("return navigator.userAgent;");
   			if(sCurrentBrowserText.contains("Chrome"))
   			{	
   				sCurrentBrowser="chrome";		
   			}
   			if(sCurrentBrowserText.contains("MSIE"))
   			{	
   				sCurrentBrowser="internet";		
   			}
   			if(sCurrentBrowserText.contains("firefox"))
   			{	
   				sCurrentBrowser="firefox";		
   			}
   			return sCurrentBrowser;
   		}


}