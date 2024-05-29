package com.qa.doingerp.runner;

import com.aventstack.extentreports.ExtentReports;
import com.qa.doingerp.parameters.TestParameters;
import com.qa.doingerp.parameters.FrameworkParameters;
import com.qa.doingerp.reports.Report;
import com.qa.doingerp.utilities.Util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;


/**
 * Class to facilitate parallel execution of test scripts
 * @author thanan
 * @version 1.0
 * @since Feb 2024
 */
public class MultiThreadExecutor implements Runnable
{
	private FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();
	private TestParameters testParameters;
	private Date startTime, endTime;
	private String testStatus;
	private Report report;
	private ExtentReports extent;
	
	
	/**
	 * Constructor to initialize the details of the test case to be executed
	 * @param testParameters The {@link TestParameters} object (passed from the {@link TestRunner})
	 * @param report The {@link Report} object (passed from the {@link TestRunner})
	 */
	public MultiThreadExecutor(TestParameters testParameters, Report report, ExtentReports extent)
	{
		super();

		this.testParameters = testParameters;
		this.report = report;
		this.extent = extent;
	}
	
	@Override
	public void run()
	{
		startTime = Util.getCurrentTime();
		testStatus = invokeTestScript();
		endTime = Util.getCurrentTime();
		String executionTime = Util.getTimeDifference(startTime, endTime);
		report.updateResultSummary(testParameters, executionTime, testStatus);
	}
	
	private String invokeTestScript()
	{
		if(frameworkParameters.getStopExecution()) {
			testStatus = "Test Execution Aborted";
		} else {
			try {
				Class<?> driverScriptClass = Class.forName("com.qa.doingerp.core.ControlManager");
				Object driverScript = driverScriptClass.newInstance();

				Field testParameters = driverScriptClass.getDeclaredField("testParameters");
				testParameters.setAccessible(true);
				testParameters.set(driverScript, this.testParameters);


				Field extentReport = driverScriptClass.getDeclaredField("extent");
				extentReport.setAccessible(true);
				extentReport.set(driverScript, this.extent);

				Method driveTestExecution =
						driverScriptClass.getMethod("driveTestExecution", (Class<?>[]) null);
				driveTestExecution.invoke(driverScript, (Object[]) null);

				Field testReport = driverScriptClass.getDeclaredField("report");
				testReport.setAccessible(true);
				Report report = (Report) testReport.get(driverScript);
				testStatus = report.testStatus;
			} catch (ClassNotFoundException e) {
				testStatus = "Reflection Error - ClassNotFoundException";
				e.printStackTrace();
			}  catch (IllegalArgumentException e) {
				testStatus = "Reflection Error - IllegalArgumentException";
				e.printStackTrace();
			} catch (InstantiationException e) {
				testStatus = "Reflection Error - InstantiationException";
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				testStatus = "Reflection Error - IllegalAccessException";
				e.printStackTrace();
			} catch (SecurityException e) {
				testStatus = "Reflection Error - SecurityException";
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				testStatus = "Reflection Error - NoSuchFieldException";
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				testStatus = "Reflection Error - NoSuchMethodException";
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				testStatus = "Failed";
				System.out.println("Exception: " + e.getMessage());
				System.out.println("Exception Cause: " + e.getCause().getMessage());
			}
		}

		return testStatus;
	}
	
	
	
}