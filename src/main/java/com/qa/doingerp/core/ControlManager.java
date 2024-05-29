package com.qa.doingerp.core;


import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.codeborne.selenide.Config;
import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.WebDriverProvider;
import com.codeborne.selenide.WebDriverRunner;
import com.qa.doingerp.driver.ChromeProfileWebDriverProvider;
import com.qa.doingerp.driver.ChromeWebDriverProvider;
import com.qa.doingerp.driver.EdgeWebDriverProvider;
import com.qa.doingerp.exception.OnError;
import com.qa.doingerp.exception.UnExpectedException;
import com.qa.doingerp.parameters.*;
import com.qa.doingerp.reports.Report;
import com.qa.doingerp.reports.Status;
import com.qa.doingerp.utilities.SelenideCustomConfiguration;
import com.qa.doingerp.utilities.Settings;
import com.qa.doingerp.utilities.TimeStamp;
import com.qa.doingerp.utilities.Util;
import org.junit.Assert;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import com.qa.doingerp.core.BrowserOptions;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.rmi.Remote;
import java.time.Duration;
import java.util.*;

/**
 *
 */
public class ControlManager {
	private ArrayList<String> businessFlowData;
	private ArrayList<SeleniumIDE> stepDefinitions;
	public  int currentIteration, currentSubIteration;
	private Date startTime, endTime;
	private String timeStamp;

	private TestData testData;
	private Report report;
	private SelenideDriver driver;
	private ScriptHelper scriptHelper;

	private Properties properties;
	private FrameworkParameters frameworkParameters = FrameworkParameters
			.getInstance();
	private CommonData commonData = null;

	private ExtentReports extent;
	private ExtentTest extentTest;
	

	
	/**
	 * The {@link TestParameters} object
	 */
	public TestParameters testParameters = new TestParameters();

	/**
	 * Constructor to initialize the ControlManager
	 */
	public ControlManager() {
		setRelativePath();
		properties = Settings.getInstance();
		setDefaultTestParameters();
	}

	private void setRelativePath() {
		String relativePath = new File(System.getProperty("user.dir"))
				.getAbsolutePath();
		frameworkParameters.setRelativePath(relativePath);
	}

	private void setDefaultTestParameters() {
		testParameters.setCurrentScenario(this.getClass().getSimpleName());
		testParameters.setIterationMode(IterationOptions.RunAllIterations);
		testParameters.setBrowser(properties.getProperty("DefaultBrowser")
				.toLowerCase());
		testParameters.setBrowserVersion(properties
				.getProperty("DefaultBrowserVersion"));
		testParameters.setPlatform(properties
				.getProperty("DefaultPlatform"));
	}

	/**
	 * Function to execute the given test case
	 */
	public void driveTestExecution() throws MalformedURLException {

		launchWebDriver();
		loadBusinessFlow();
		loadTestIteration();
		loadTestReport();
		beforeHook();
		executeTestFlow();
		afterHook();
		wrapUp();
	}

	private void launchWebDriver() throws MalformedURLException {

	   startTime = Util.getCurrentTime();

	   if(properties.getProperty("GridMode").equalsIgnoreCase("on") ){
			String buildNo = properties.getProperty("BuildNo");
			String videoName = testParameters.getCurrentTestcase().trim()
					.replaceAll(" ", "_")
					.replaceAll("'", "")+"_"+buildNo;
		   SelenideCustomConfiguration configuration = new SelenideCustomConfiguration();
		   DesiredCapabilities capabilities=BrowserOptions.createCapabilities(testParameters.getBrowser(),videoName);
		  /* configuration.browser(testParameters.getBrowser());
		   configuration.browserCapabilities(capabilities);
		   configuration.timeout(Integer.parseInt(properties.getProperty("Timeout")));
		   configuration.remote(properties.getProperty("GridHubUrl"));
		   ;*/
		   RemoteWebDriver remDriver = new RemoteWebDriver(new URL(properties.get("GridHubUrl").toString()), capabilities);

		   driver = new SelenideDriver(configuration, remDriver, null);

		   driver.open();
           driver.getWebDriver().manage().window().maximize();
		   String VncUrl="http://gridview.doingerp.com/#/sessions/"+driver.getSessionId().toString();
		   System.out.println("View Your Test Script: "+VncUrl);

	   }else {
		   SelenideCustomConfiguration configuration = new SelenideCustomConfiguration();
		  if( testParameters.getBrowser().equalsIgnoreCase("chrome") && testParameters.getSSO().equalsIgnoreCase("no"))
			   configuration.browser(ChromeWebDriverProvider.class.getName()) ;
          else if( testParameters.getBrowser().equalsIgnoreCase("chrome") && testParameters.getSSO().equalsIgnoreCase("yes"))
              configuration.browser(ChromeProfileWebDriverProvider.class.getName()) ;
		  else if( testParameters.getBrowser().equalsIgnoreCase("edge"))
			  configuration.browser(EdgeWebDriverProvider.class.getName()) ;
		  else
			   configuration.browser(testParameters.getBrowser());

		   configuration.timeout(Integer.parseInt(properties.getProperty("Timeout")));

		   driver = new SelenideDriver(configuration);
		   driver.open();
           driver.getWebDriver().manage().window().maximize();
	   }

 }

 private WebDriver gridSetUp(String browser, Platform platform){
		/*try{
			DesiredCapabilities capabilities = new DesiredCapabilities();
			capabilities.setBrowserName(browser.toLowerCase());
			//capabilities.setPlatform(platform);
			driver = new RemoteWebDriver(new URL(properties.getProperty("GridHubUrl")), capabilities);
		}catch (Exception e){
			throw new UnExpectedException("The initiatialisation of remote webdriver throws the exception ", e.getMessage());
		}*/
		return null;
 }

	private void loadBusinessFlow() {

		String datatablePath = getClass().getClassLoader().getResource("businessflows").getPath();

		testData = new TestData(datatablePath,
				testParameters.getCurrentScenario());
		report = new Report();
		scriptHelper = new ScriptHelper(testData, report, driver);

		businessFlowData = getBusinessFlow();

		if (!properties.getProperty("Experimental").equals("On"))
			frameworkParameters.setRunConfiguration(properties
					.getProperty("RunConfiguration"));
		
		timeStamp = TimeStamp.getInstance();
		commonData = new CommonData();
	}
	

	private synchronized void loadTestIteration() {
		switch (testParameters.getIterationMode()) {
		case RunAllIterations:
			String datatablePath =  getClass().getClassLoader().getResource("businessflows").getPath();

			XcelAccess testDataAccess = new XcelAccess(datatablePath,
					testParameters.getCurrentScenario());

			if(properties
					.getProperty("DataSheet").equals(""))
			testDataAccess.setDatasheetName("Test_Data");
			else
				testDataAccess.setDatasheetName(properties
						.getProperty("DataSheet"));


			int startRowNum = testDataAccess.getRowNum(
					testParameters.getCurrentTestcase(), 0);
			int nTestcaseRows = testDataAccess.getRowCount(
					testParameters.getCurrentTestcase(), 0, startRowNum);
			int nSubIterations = testDataAccess
					.getRowCount("1", 1, startRowNum); // Assumption: Every test
														// case will have at
														// least one iteration
			//int nIterations = nTestcaseRows / nSubIterations;
			testParameters.setEndIteration(1);

			currentIteration = 1;
			currentSubIteration=1;
			break;

		case RunOneIterationOnly:
			currentIteration = 1;
			break;

		case RunRangeOfIterations:
			if (testParameters.getStartIteration() > testParameters
					.getEndIteration()) {
				throw new UnExpectedException("Error",
						"StartIteration cannot be greater than EndIteration!");
			}
			currentIteration = testParameters.getStartIteration();
			break;
		}
	}

	private void loadTestReport() {
		String reportPath = frameworkParameters.getRelativePath()
				+ frameworkParameters.fileSeparator + "target"+frameworkParameters.fileSeparator+"results"
				+ frameworkParameters.fileSeparator + timeStamp;

		report.setReportPath(reportPath);
		report.setDriver(driver.getWebDriver());
		report.setReportLogLevel(Integer.parseInt(properties
				.getProperty("LogLevel")));
		//set the extent report
		if(testParameters.getBrowser().equalsIgnoreCase("Mobile")) {
			extentTest = extent.createTest(testParameters.getDevicename()+"_"+testParameters.getCurrentTestDescription());
		}else{
			extentTest = extent.createTest(testParameters.getCurrentTestDescription());

		}
		//report.setExtentTest(extentTest);
		String buildNo = properties.getProperty("BuildNo");
		String videoName = testParameters.getCurrentTestcase().trim()
				.replaceAll(" ", "_")
				.replaceAll("'", "")+"_"+buildNo;
		String video_url = properties.getProperty("VideoBaseUrl")+videoName+".mp4";
		extentTest.info("Video log link : <a href='"+video_url+"'>Video Link</a>");

		String strDevice=testParameters.getDevicename().replaceAll(" ", "");
		// TODO Change made here; verify it works
		report.setReportName(testParameters.getCurrentScenario() + "_"
				+ testParameters.getCurrentCleanTestcase()+"_"+strDevice);
		report.initialize();
		report.createTestLogHeader(
				testParameters.getIterationMode().toString(),
				testParameters.getStartIteration(),
				testParameters.getEndIteration());


	}

	/**
	 * Function to do required setup activities before starting the test
	 * execution
	 */
	protected void beforeHook() {
		driver.getWebDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
		
		InetAddress addr;
		String hostName = "Host name not found";
		try {
			addr = InetAddress.getLocalHost();
			hostName = addr.getHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		    String s = (String) ((JavascriptExecutor) driver.getWebDriver()).executeScript("return navigator.userAgent;");
			report.updateTestLog("Startup",
					"Windows: " + testParameters.getPlatform() + "<br>"
							+ "Browser: "
							+ testParameters.getBrowser() + "<br>"
							+ "Host Name: " + hostName, Status.DONE);

	}

	private void executeTestFlow() {
		while (currentIteration <= testParameters.getEndIteration()) {
			report.createIterationHeader(currentIteration);

			try {
				executeTestcase(businessFlowData);
			} catch (UnExpectedException fx) {
				exceptionHandler(fx, fx.errorName);
			} catch (InvocationTargetException ix) {
				exceptionHandler((Exception) ix.getCause(), "Error");
			} catch (Exception ex) {
				exceptionHandler(ex, "Error");
			}

			currentIteration++;
		}
	}


	private ArrayList<String> getBusinessFlow() {
		String businessFlowPath = getClass().getClassLoader().getResource("businessflows").getPath();
		XcelAccess businessFlowAccess = new XcelAccess(businessFlowPath,
				testParameters.getCurrentScenario());
		businessFlowAccess.setDatasheetName("Business_flows");

		int rowNum = businessFlowAccess.getRowNum(
				testParameters.getCurrentTestcase(), 0);
		if (rowNum == -1) {
			throw new UnExpectedException("The test case \""
					+ testParameters.getCurrentTestcase()
					+ "\" is not found in the Business Flow sheet!");
		}

		String dataValue;
		ArrayList<String> businessFlowData = new ArrayList<String>();
		int currentColumnNum = 1;
		while (true) {
			dataValue = businessFlowAccess.getValue(rowNum, currentColumnNum);
			if (dataValue.equals("")) {
				break;
			}
			businessFlowData.add(dataValue);
			currentColumnNum++;
		}

		if (businessFlowData.size() == 0) {
			throw new UnExpectedException(
					"No business flow found against the test case \""
							+ testParameters.getCurrentTestcase() + "\"");
		}

		return businessFlowData;
	}


	private ArrayList<SeleniumIDE> getComponentFLow(String componentName) {
		String businessFlowPath = getClass().getClassLoader().getResource("businessflows").getPath();
		XcelAccess businessFlowAccess = new XcelAccess(businessFlowPath,
				testParameters.getCurrentScenario());
		businessFlowAccess.setDatasheetName("ComponentSheet");

		//rewrite the new logic based on selenium IDE feature
		String description, command, target, value;
		ArrayList<SeleniumIDE> businessFlowData = new ArrayList<SeleniumIDE>();
        int rowNumber = businessFlowAccess.getRowNum(componentName,1);
        int columnNumber = 2; //since the description starts from 3rd column

		do {
		    //Initiate the SeleniumIDE class
            SeleniumIDE seleniumIDE = new SeleniumIDE();
            //get the description
            description = businessFlowAccess.getValue(rowNumber, columnNumber);

			//add desription
            seleniumIDE.setDescription(description);
            //get the command
            command = businessFlowAccess.getValue(rowNumber, columnNumber+1);
            seleniumIDE.setCommand(command);
            //get the target
            target = businessFlowAccess.getValue(rowNumber, columnNumber+2);
            seleniumIDE.setTarget( target);
            //get the value
            value = businessFlowAccess.getValue(rowNumber, columnNumber+3);
            seleniumIDE.setValue(value);

			businessFlowData.add(seleniumIDE);
            rowNumber++;
            String breakValue = businessFlowAccess.getValue(rowNumber,1);
            if(breakValue ==  null || !breakValue.equals(""))
            	break;

		}while(true);

		if (businessFlowData.size() == 0) {
			throw new UnExpectedException(
					"No steps found against the test scenario \""
							+ testParameters.getCurrentTestcase() + "\"");
		}

		return businessFlowData;
	}

	private void executeTestcase(ArrayList<String> businessFlowData)
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, ClassNotFoundException,
			InstantiationException {
		HashMap<String, Integer> keywordDirectory = new HashMap<String, Integer>();

		for (int currentKeywordNum = 0; currentKeywordNum < businessFlowData
				.size(); currentKeywordNum++) {
			String[] currentFlowData = businessFlowData.get(currentKeywordNum)
					.split(",");
			String currentKeyword = currentFlowData[0];

			int nKeywordIterations;
			if (currentFlowData.length > 1) {
				nKeywordIterations = Integer.parseInt(currentFlowData[1]);
			} else {
				nKeywordIterations = 1;
			}

			for (int currentKeywordIteration = 0; currentKeywordIteration < nKeywordIterations; currentKeywordIteration++) {
				if (keywordDirectory.containsKey(currentKeyword)) {
					keywordDirectory.put(currentKeyword,
							keywordDirectory.get(currentKeyword) + 1);
				} else {
					keywordDirectory.put(currentKeyword, 1);
				}
				currentSubIteration = keywordDirectory.get(currentKeyword);

				testData.setCurrentRow(testParameters.getCurrentTestcase(),
						currentIteration, currentSubIteration);

				if (currentSubIteration > 1) {
					report.createSectionHeader(currentKeyword
							+ " (Sub-Iteration: " + currentSubIteration + ")");
				} else {

				}
				testData.setCurrentRow(testParameters.getCurrentTestcase(),
						currentIteration, currentSubIteration);
                report.createSectionHeader(currentKeyword);
				ExtentTest node = extentTest.createNode(currentKeyword);
				report.setExtentTest(node);
                //logic to iterate the steps for each component
				stepDefinitions = getComponentFLow(currentKeyword);

				System.out.println("\n----------------------The Current component: "+currentKeyword+" -------");
				for(int k=0; k<stepDefinitions.size(); k++) {

					invokeBusinessComponent(stepDefinitions.get(k));


				}

                			}
		}
	}

	private void invokeBusinessComponent(SeleniumIDE seleniumIDE)
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, ClassNotFoundException,
			InstantiationException {
		Boolean isMethodFound = false;
		final String CLASS_FILE_EXTENSION = ".class";
		File[] packageDirectories = { new File(
				frameworkParameters.getRelativePath()
						+ frameworkParameters.fileSeparator+ "target"+
						frameworkParameters.fileSeparator+"test-classes"+
						frameworkParameters.fileSeparator+"com"+frameworkParameters.fileSeparator+"qa"+frameworkParameters.fileSeparator
						+"doingerp"+frameworkParameters.fileSeparator+"components")};

		String currentKeyword =null ;
		for (File packageDirectory : packageDirectories) {
			File[] packageFiles = packageDirectory.listFiles();
			String packageName = packageDirectory.getName();

			for (int i = 0; i < packageFiles.length; i++) {
				File packageFile = packageFiles[i];
				String fileName = packageFile.getName();

				// We only want the .class files
				if (fileName.endsWith(CLASS_FILE_EXTENSION)) {
					// Remove the .class extension to get the class name
					String className = fileName.substring(0, fileName.length()
							- CLASS_FILE_EXTENSION.length());

					Class<?> reusableComponents = Class.forName("com.qa.doingerp."+packageName
							+ "." + className);
					Method executeComponent;


					try {
						// Convert the first letter of the method to lowercase
						// (in line with java naming conventions)
						 currentKeyword = seleniumIDE.getCommand().substring(0, 1)
								.toLowerCase() + seleniumIDE.getCommand().substring(1);
						executeComponent = reusableComponents.getMethod(
								currentKeyword, (Class<?>[]) null);
					} catch (NoSuchMethodException ex) {
						// If the method is not found in this class, search the
						// next class
						continue;
					}

					isMethodFound = true;

					Constructor<?> ctor = reusableComponents
							.getDeclaredConstructors()[0];
					Object businessComponent = ctor.newInstance(scriptHelper);

					//set for common class
					Field fieldSeleniumIDE = null;
					try {
						fieldSeleniumIDE = reusableComponents
								.getDeclaredField("seleniumIDE");
						fieldSeleniumIDE.setAccessible(true);
						fieldSeleniumIDE.set(businessComponent, seleniumIDE);
					} catch (Exception e) {
						//System.out.println("Failed to set common data");
					}

					//set for common data
					Field fieldCommonData= null;
					try {
						fieldCommonData = reusableComponents
								.getDeclaredField("commonData");
						fieldCommonData.setAccessible(true);
						fieldCommonData.set(businessComponent, commonData);
					} catch (Exception e) {
						//System.out.println("Failed to set common data");
					}



					System.out.println("Executing Steps: "+seleniumIDE.getDescription()+" and the command: " + currentKeyword
							+ " ........");
					long startTime = System.currentTimeMillis();
					executeComponent.invoke(businessComponent, (Object[]) null);
					System.out
							.println("=== Done === Time taken : "
									+ (System.currentTimeMillis() - startTime)
									+ " ===");
					System.out
							.println("\n");

					break;
				}
			}
		}

		if (!isMethodFound) {
			throw new UnExpectedException("Keyword " + currentKeyword
					+ " not found within any class "
					+ "inside the businesscomponents package");
		}
	}

	private void invokeBusinessComponent1(SeleniumIDE seleniumIDE, String currentKeyword)
			throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, ClassNotFoundException,
			InstantiationException {
		Boolean isMethodFound = false;
		final String CLASS_FILE_EXTENSION = ".class";
		File[] packageDirectories = { new File(
				frameworkParameters.getRelativePath()
						+ frameworkParameters.fileSeparator+ "target"+
						frameworkParameters.fileSeparator+"test-classes"+
						frameworkParameters.fileSeparator+"com"+frameworkParameters.fileSeparator+"qa"+frameworkParameters.fileSeparator
        +"doingerp"+frameworkParameters.fileSeparator+"components")};

		for (File packageDirectory : packageDirectories) {
			File[] packageFiles = packageDirectory.listFiles();
			String packageName = packageDirectory.getName();

			for (int i = 0; i < Objects.requireNonNull(packageFiles).length; i++) {
				File packageFile = packageFiles[i];
				String fileName = packageFile.getName();

				// We only want the .class files
				if (fileName.endsWith(CLASS_FILE_EXTENSION)) {
					// Remove the .class extension to get the class name
					String className = fileName.substring(0, fileName.length()
							- CLASS_FILE_EXTENSION.length());

					Class<?> reusableComponents = Class.forName("com.qa.doingerp."+packageName
							+ "." + className);
					Method executeComponent;

					try {
						// Convert the first letter of the method to lowercase
						// (in line with java naming conventions)
						currentKeyword = currentKeyword.substring(0, 1)
								.toLowerCase() + currentKeyword.substring(1);
						executeComponent = reusableComponents.getMethod(
								currentKeyword, (Class<?>[]) null);
					} catch (NoSuchMethodException ex) {
						// If the method is not found in this class, search the
						// next class
						continue;
					}

					isMethodFound = true;

					Constructor<?> ctor = reusableComponents
							.getDeclaredConstructors()[0];
					Object businessComponent = ctor.newInstance(scriptHelper);

					Field fieldCommonData = null;
					try {
						fieldCommonData = reusableComponents
								.getDeclaredField("seleniumIDE");
						fieldCommonData.setAccessible(true);
						fieldCommonData.set(businessComponent, seleniumIDE);
					} catch (Exception e) {
						//System.out.println("Failed to set common data");
					}



					System.out.println("=== Executing " + currentKeyword
							+ " ===");
					long startTime = System.currentTimeMillis();
					executeComponent.invoke(businessComponent, (Object[]) null);
					System.out
							.println("=== Done === Time taken : "
									+ (System.currentTimeMillis() - startTime)
									+ " ===");
						break;
				}
			}
		}

		if (!isMethodFound) {
			throw new UnExpectedException("Keyword " + currentKeyword
					+ " not found within any class "
					+ "inside the businesscomponents package");
		}
	}

	private void exceptionHandler(Exception ex, String exceptionName) {
		// Error reporting
		String exceptionDescription = ex.getMessage();
		if (exceptionDescription == null) {
			exceptionDescription = ex.toString();
		}

		if (ex.getCause() != null) {
			report.updateTestLog( ex.getClass().getSimpleName(), exceptionDescription
					+ " <b>Caused by: </b>" + ex.getCause(), Status.FAIL);
		} else {
			report.updateTestLog( ex.getClass().getSimpleName(), exceptionDescription,
					Status.FAIL);
		}
		ex.printStackTrace();

		// Error response
		OnError onError = OnError.valueOf(properties.getProperty("OnError"));
		switch (onError) {
		case NextIteration:
			report.updateTestLog(
					"User Info",
					"Test case iteration terminated by user! Proceeding to next iteration (if applicable)...",
					Status.DONE);
			currentIteration++;
			executeTestFlow();
			break;

		case NextTestCase:
			report.updateTestLog(
					"User Info",
					"Test case terminated by user! Proceeding to next test case (if applicable)...",
					Status.DONE);
			break;

		case Stop:
			frameworkParameters.setStopExecution(true);
			break;
		}

		// Wrap up execution
		afterHook();
		wrapUp();
	}

	

	/**
	 * Function to do required tear down activities at the end of the test
	 * execution
	 */
	protected void afterHook() {
		driver.getWebDriver().quit();
	}

	private void wrapUp() {

		endTime = Util.getCurrentTime();
		closeTestReport();

		extent.flush();

		if (report.testStatus.equalsIgnoreCase("Failed")) {
			Assert.fail(report.failureDescription);
		}
	}

	private void closeTestReport() {
		String executionTime = Util.getTimeDifference(startTime, endTime);
		report.createTestLogFooter(executionTime);
	}
	
		
	
	
	public String getCurrentBrowserVersion() {
		
		String sBrowserVersion="";
		
		if(testParameters.getBrowser().equalsIgnoreCase("Safari"))
		{
			sBrowserVersion=properties.getProperty("SafariVersion");
		}
		if(testParameters.getBrowser().equalsIgnoreCase("Chrome"))
		{
			sBrowserVersion=properties.getProperty("ChromeVersion");
		}
		if(testParameters.getBrowser().equalsIgnoreCase("InternetExplorer"))
		{
			sBrowserVersion=properties.getProperty("IEversion");
		}
		if(testParameters.getBrowser().equalsIgnoreCase("Firefox"))
		{
			sBrowserVersion=properties.getProperty("FFversion");
		}
		
		return sBrowserVersion;
	}
	
}
