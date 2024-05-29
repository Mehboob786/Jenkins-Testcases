package com.qa.doingerp.runner;


import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.qa.doingerp.parameters.FrameworkParameters;
import com.qa.doingerp.parameters.IterationOptions;
import com.qa.doingerp.parameters.XcelAccess;
import com.qa.doingerp.reports.DetailedSummary;
import com.qa.doingerp.parameters.TestParameters;
import com.qa.doingerp.utilities.Settings;
import com.qa.doingerp.utilities.TimeStamp;
import com.qa.doingerp.reports.Report;
import com.qa.doingerp.utilities.Util;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Class to manage the batch execution of test scripts within the framework
 * 
 * @author 	thanan
 * @version 1.0
 * @since Feb 2024
 */
public class TestRunner {
	private static ArrayList<TestParameters> testInstancesToRun;
	private static Report report;
	private static Properties properties;
	private static FrameworkParameters frameworkParameters = FrameworkParameters
			.getInstance();

	private static Date startTime, endTime;
	private static String timeStamp;
	private static String runConfiguration;
	public static String reportPathWithTimeStamp;
	public static int timeout;


	public static void main(String[] args) throws IOException {
		
		
		setRelativePath();
		initializeTestBatch();
		setupErrorLog();
		initializeSummaryReport();
		driveBatchExecution();
		wrapUp();
	}	

	private static void setRelativePath() {
		System.out.println("Set Relative Path");
		String relativePath = new File(System.getProperty("user.dir"))
				.getAbsolutePath();
		frameworkParameters.setRelativePath(relativePath);
	}
	

	private static void initializeTestBatch() throws IOException {
		System.out.println("Inside Initialize test batch");
		startTime = Util.getCurrentTime();

		//initialise the standard HTML report
		report = new Report();

		properties = Settings.getInstance();
		runConfiguration = properties.getProperty("RunConfiguration");
		
		//set the timeout for maximum instance
		timeout=Integer.parseInt(properties.getProperty("Timeout"));
		

		String[] arrRunConfigurations = runConfiguration.split(",");
		frameworkParameters.setRunConfiguration(runConfiguration);
		
		String strSheetName=properties.getProperty("commonTestData");
			testInstancesToRun = new ArrayList<TestParameters>();

		for (String thisRunConfiguration : arrRunConfigurations) {
			List<String> testCases = Arrays.asList(properties.get("TestCases").toString().split(","));
			if(!testCases.isEmpty() && !testCases.get(0).equalsIgnoreCase("All")){
				testInstancesToRun.addAll(cherryPickRunInfo(thisRunConfiguration,testCases));
			}
			else {
				testInstancesToRun.addAll(getRunInfo(thisRunConfiguration));
			}
			}
		

		timeStamp = TimeStamp.getInstance();

		reportPathWithTimeStamp = frameworkParameters.getRelativePath()
				+ frameworkParameters.fileSeparator + "target"+frameworkParameters.fileSeparator+"results"
				+ frameworkParameters.fileSeparator + timeStamp;

	}

	private static void setupErrorLog() throws IOException {

		String errorLogFile = reportPathWithTimeStamp
				+ frameworkParameters.fileSeparator + "ErrorLog.txt";
		System.setErr(new PrintStream(new FileOutputStream(errorLogFile)));

	}

	private static void initializeSummaryReport() {
		report.setReportPath(reportPathWithTimeStamp);
		report.initialize();
		report.createResultSummaryHeader();
	}

	private static void driveBatchExecution() {
        //initiate the extent report
        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(reportPathWithTimeStamp+frameworkParameters.fileSeparator+"TestReport.html");
        htmlReporter.loadXMLConfig(TestRunner.class.getClassLoader().getResource("Extent-Config.xml").getFile());
        ExtentReports extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        extent.setSystemInfo("OS", System.getProperty("os.name"));


		int nThreads = Integer.parseInt(properties
				.getProperty("NumberOfThreads"));
		ExecutorService parallelExecutor = Executors
				.newFixedThreadPool(nThreads);

	
		for (int currentTestInstance = 0; currentTestInstance < testInstancesToRun
				.size(); currentTestInstance++) {
			MultiThreadExecutor testRunner = new MultiThreadExecutor(
					testInstancesToRun.get(currentTestInstance), report, extent);
			parallelExecutor.execute(testRunner);
			
			if (frameworkParameters.getStopExecution()) {
				break;
			}
		}

		parallelExecutor.shutdown();
		while (!parallelExecutor.isTerminated()) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		
	}

	private static ArrayList<TestParameters> getRunInfo(String sheetName) {

	    //get the scenario sheet name
        String scenarioSheetPath = TestRunner.class.getClassLoader().getResource("businessflows").getPath();
		XcelAccess runManagerAccess = new XcelAccess(scenarioSheetPath, properties.getProperty("workbookName"));
		runManagerAccess.setDatasheetName(sheetName);
		
		int nTestInstances = runManagerAccess.getLastRowNum();
		ArrayList<TestParameters> testInstancesToRun = new ArrayList<TestParameters>();

		for (int currentTestInstance = 1; currentTestInstance <= nTestInstances; currentTestInstance++) {
			String executeFlag = runManagerAccess.getValue(currentTestInstance,
					"Execute");

			if (executeFlag.equalsIgnoreCase("Yes")) {
				TestParameters testParameters = new TestParameters();
				
				testParameters.setRowNum(currentTestInstance);
				testParameters.setCurrentSheet(sheetName);
				
				testParameters.setCurrentScenario(runManagerAccess.getValue(
						currentTestInstance, "Test_Scenario"));

				testParameters.setCurrentTestcase(runManagerAccess.getValue(
						currentTestInstance, "Test_Case"));
				testParameters.setCurrentTestDescription(runManagerAccess
						.getValue(currentTestInstance, "Description"));

				testParameters.setIterationMode(IterationOptions
						.valueOf(runManagerAccess.getValue(currentTestInstance,
								"Iteration_Mode")));
				String startIteration = runManagerAccess.getValue(
						currentTestInstance, "Start_Iteration");
				if (!startIteration.equals("")) {
					testParameters.setStartIteration(Integer
							.parseInt(startIteration));
				}
				String endIteration = runManagerAccess.getValue(
						currentTestInstance, "End_Iteration");
				if (!endIteration.equals("")) {
					testParameters.setEndIteration(Integer
							.parseInt(endIteration));
				}
				//browser
				String browser = runManagerAccess.getValue(currentTestInstance,"Browser");
										testParameters.setBrowser(browser);

				//platform
				String platform = runManagerAccess.getValue(currentTestInstance, "Platform");
						testParameters.setPlatform(platform);

				//device settings
				String devicename = runManagerAccess.getValue(
						currentTestInstance, "Device");
				testParameters.setDevicename(devicename);
				//SSO details
				String sso = runManagerAccess.getValue(currentTestInstance, "SSO");
				testParameters.setSSO(sso);

				testInstancesToRun.add(testParameters);
			}
		}
		System.out.println("Total test case:"+testInstancesToRun.size());

		return testInstancesToRun;
	}

	private static ArrayList<TestParameters> cherryPickRunInfo(String sheetName, List<String> testCases) {

		//get the scenario sheet name
		String scenarioSheetPath = TestRunner.class.getClassLoader().getResource("businessflows").getPath();
		XcelAccess runManagerAccess = new XcelAccess(scenarioSheetPath, properties.getProperty("workbookName"));
		runManagerAccess.setDatasheetName(sheetName);
		// get the grid browsers

		String[] gridBrows = properties.get("GridBrowsers").toString().split(",");
		Queue<String> browsers = new LinkedList<>(Arrays.asList(gridBrows));

		int nTestInstances = runManagerAccess.getLastRowNum();
		ArrayList<TestParameters> testInstancesToRun = new ArrayList<TestParameters>();

		testCases.forEach( eachCase ->{
		for (int currentTestInstance = 1; currentTestInstance <= nTestInstances; currentTestInstance++) {
			String executeFlag = runManagerAccess.getValue(currentTestInstance,
					"Test_Case");

			if (executeFlag.equalsIgnoreCase(eachCase)) {
				TestParameters testParameters = new TestParameters();

				testParameters.setRowNum(currentTestInstance);
				testParameters.setCurrentSheet(sheetName);

				testParameters.setCurrentScenario(runManagerAccess.getValue(
						currentTestInstance, "Test_Scenario"));

				testParameters.setCurrentTestcase(runManagerAccess.getValue(
						currentTestInstance, "Test_Case"));
				testParameters.setCurrentTestDescription(runManagerAccess
						.getValue(currentTestInstance, "Description"));

				testParameters.setIterationMode(IterationOptions
						.valueOf(runManagerAccess.getValue(currentTestInstance,
								"Iteration_Mode")));
				String startIteration = runManagerAccess.getValue(
						currentTestInstance, "Start_Iteration");
				if (!startIteration.equals("")) {
					testParameters.setStartIteration(Integer
							.parseInt(startIteration));
				}
				String endIteration = runManagerAccess.getValue(
						currentTestInstance, "End_Iteration");
				if (!endIteration.equals("")) {
					testParameters.setEndIteration(Integer
							.parseInt(endIteration));
				}
				//browser
				//String browser = runManagerAccess.getValue(currentTestInstance,"Browser");
				testParameters.setBrowser(browsers.poll());
				if(browsers.isEmpty())
					Collections.addAll( browsers, gridBrows);

				//platform
				String platform = runManagerAccess.getValue(currentTestInstance, "Platform");
				testParameters.setPlatform(platform);

				//device settings
				String deviceName = runManagerAccess.getValue(
						currentTestInstance, "Device");
				testParameters.setDevicename(deviceName);
				//SSO details
				String sso = runManagerAccess.getValue(currentTestInstance, "SSO");
				testParameters.setSSO(sso);

				testInstancesToRun.add(testParameters);
			}
		}
		});
		System.out.println("Total test case:"+testInstancesToRun.size());

		return testInstancesToRun;
	}


	private static void wrapUp() {
		
		endTime = Util.getCurrentTime();
		closeSummaryReport();

		try {
			DetailedSummary.generateDetailedSummary(
					frameworkParameters.getRelativePath()
							+ frameworkParameters.fileSeparator + "target"+frameworkParameters.fileSeparator+"results"
							+ frameworkParameters.fileSeparator + timeStamp,
					frameworkParameters.fileSeparator);
		} catch (IOException e) {
			e.printStackTrace();
		}



		//if it is windows OS platform, open the summary report

		if(System.getProperty("os.name").toLowerCase().contains("windows")) {
			try {
				Runtime.getRuntime().exec(
						"RunDLL32.EXE shell32.dll,ShellExec_RunDLL "
								+ reportPathWithTimeStamp
								+ "\\TestReport.Html");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void closeSummaryReport() {
		String totalExecutionTime = Util.getTimeDifference(startTime, endTime);
		report.createResultSummaryFooter(totalExecutionTime);
	}

				
		
	
}