package com.qa.doingerp.core;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.HashMap;
import java.util.Map;

public class BrowserOptions {

    public static DesiredCapabilities createCapabilities(String browser,String VideoName) {
        switch (browser.toLowerCase()) {
            case "chrome":
                return createChromeOptions(VideoName);
            case "firefox":
                return createFirefoxOptions(VideoName);
            default:
                throw new IllegalArgumentException("Unsupported browser: " + browser);
        }
    }

    private static DesiredCapabilities createChromeOptions(String VideoName) {
        Map<String, Object> selenoidOptions = new HashMap<>();
        selenoidOptions.put("enableVideo", true);
        selenoidOptions.put("enableVNC", true);
        selenoidOptions.put("screenResolution", "1920x1080x24");
        selenoidOptions.put("videoName", VideoName+".mp4");

        ChromeOptions options = new ChromeOptions();
        options.setCapability("selenoid:options", selenoidOptions);
        options.setCapability("browserName", "chrome");
        options.setCapability("browserVersion", "124.0");
        return new DesiredCapabilities(options);
    }

    private static DesiredCapabilities createFirefoxOptions(String VideoName) {
        Map<String, Object> selenoidOptions = new HashMap<>();
        selenoidOptions.put("enableVideo", true);
        selenoidOptions.put("enableVNC", true);
        selenoidOptions.put("screenResolution", "1920x1080x24");
        selenoidOptions.put("videoName", VideoName+".mp4");

        FirefoxOptions options = new FirefoxOptions();
        options.setCapability("selenoid:options", selenoidOptions);
        options.setCapability("browserName", "firefox");
        options.setCapability("browserVersion", "112.0");
        return new DesiredCapabilities(options);
    }


}


