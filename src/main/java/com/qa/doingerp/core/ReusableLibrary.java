package com.qa.doingerp.core;


import com.codeborne.selenide.SelenideDriver;
import com.qa.doingerp.parameters.TestData;
import com.qa.doingerp.parameters.FrameworkParameters;
import com.qa.doingerp.reports.Report;
import com.qa.doingerp.utilities.Settings;
import com.qa.doingerp.utilities.UserActions;
import org.openqa.selenium.WebDriver;

import java.util.Properties;


/**
 * Abstract base class for reusable libraries created by the user
 * @author thanan
 * @version 1.0
 * @since Feb 2024
 */
public abstract class ReusableLibrary
{
	/**
	 * The {@link TestData} object (passed from the test script)
	 */
	protected TestData testData;
	/**
	 * The {@link Report} object (passed from the test script)
	 */
	protected Report report;
	/**
	 * The {@link WebDriver} object
	 */
	protected SelenideDriver driver;
	/**
	 * The {@link ScriptHelper} object (required for calling one reusable library from another)
	 */
	protected ScriptHelper scriptHelper;
	
	/**
	 * The {@link Properties} object with settings loaded from the framework properties file
	 */
	protected Properties properties = Settings.getInstance();
	/**
	 * The {@link FrameworkParameters} object
	 */
	protected FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();

	/**
	 * The {@link UserActions} object
	 */
	protected UserActions userActions;
	
	
	/**
	 * Constructor to initialize the {@link ScriptHelper} object and in turn the objects wrapped by it
	 * @param scriptHelper The {@link ScriptHelper} object
	 * @param: driver The {@link WebDriver} object
	 */
	public ReusableLibrary(ScriptHelper scriptHelper)
	{
		this.scriptHelper = scriptHelper;
		this.testData = scriptHelper.getTestData();
		this.report = scriptHelper.getReport();
		this.driver = scriptHelper.getDriver();
		this.userActions = new UserActions(driver, report, testData);
	}
}