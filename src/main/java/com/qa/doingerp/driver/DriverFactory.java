package com.qa.doingerp.driver;

import com.qa.doingerp.parameters.TestParameters;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.HashMap;
import java.util.Map;


/**
 * Factory for creating the Driver object based on the required browser
 *
 * @author thanan
 * @version 1.0
 * @since Feb 2024
 */
public class DriverFactory {

    /**
     * Function to return the appropriate {@link RemoteWebDriver} object based on the browser name passed
     *
     * @return The corresponding {@link RemoteWebDriver} object
     * @param: browser The name of the browser
     */
    public static WebDriver getDriver(TestParameters testparameters) {
        WebDriver webDriver = null;
        if (testparameters.getBrowser().equalsIgnoreCase("Firefox")) {
            //this webdriverManager take care of set up the **.exe file in system path
            webDriver = new FirefoxDriver();
        } else if (testparameters.getBrowser().equalsIgnoreCase("InternetExplorer")) {
            webDriver = new InternetExplorerDriver();
        } else if (testparameters.getBrowser().equalsIgnoreCase("Chrome")) {

            ChromeOptions options = new ChromeOptions();
            options.addArguments("user-data-dir=C:\\Users\\CONSUL~1.ONE\\AppData\\Local\\Temp\\3\\scoped_dir47644_120054629\\Default");
            webDriver = new ChromeDriver(options);
        } else if (testparameters.getBrowser().equalsIgnoreCase("Mobile")) {
            // below code is for mobile emulation set up
            Map<String, String> mobileEmulation = new HashMap<>();
            mobileEmulation.put("deviceName", testparameters.getDevicename());
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.setExperimentalOption("mobileEmulation", mobileEmulation);
            webDriver = new ChromeDriver(chromeOptions);
        }

        //TODO: to implement event firing webdriver for an unexpected error handling
            //webDriver.manage().deleteAllCookies();
        webDriver.manage().window().maximize();
        System.out.println(webDriver.manage().getCookies());

        webDriver.manage().getCookies().forEach(e-> System.out.println(e.getName()+"="+e.getValue()));

        return webDriver;
    }
}