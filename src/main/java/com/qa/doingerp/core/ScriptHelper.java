package com.qa.doingerp.core;


import com.codeborne.selenide.SelenideDriver;
import com.qa.doingerp.parameters.TestData;
import com.qa.doingerp.reports.Report;
import org.openqa.selenium.WebDriver;


/**
 * Wrapper class for common framework objects, to be used across the entire test case and dependent libraries
 * @author thanan
 * @version 1.0
 * @since Feb 2024
 */
public class ScriptHelper
{
	private final TestData testData;
	private final Report report;
	private final SelenideDriver driver;

	
	/**
	 * Constructor to initialize all the objects wrapped by the {@link ScriptHelper} class
	 * @param testData The {@link TestData} object
	 * @param report The {@link Report} object
	 * @param driver The {@link WebDriver} object
	 */
	public ScriptHelper(TestData testData, Report report, SelenideDriver driver)
	{
		this.testData = testData;
		this.report = report;
		this.driver = driver;

	}
	
	/**
	 * Function to get the {@link TestData} object of the {@link ScriptHelper} class
	 * @return The {@link TestData} object
	 */
	public TestData getTestData()
	{
		return testData;
	}
	
	/**
	 * Function to get the {@link Report} object of the {@link ScriptHelper} class
	 * @return The {@link Report} object
	 */
	public Report getReport()
	{
		return report;
	}
	
	/**
	 * Function to get the {@link WebDriver} object of the {@link ScriptHelper} class
	 * @return The {@link WebDriver} object
	 */
	public SelenideDriver getDriver()
	{
		return driver;
	}


}