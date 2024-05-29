package com.qa.doingerp.driver;

import com.codeborne.selenide.WebDriverProvider;
import com.qa.doingerp.utilities.Settings;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Objects;
import java.util.Properties;

public class ChromeProfileWebDriverProvider implements WebDriverProvider {
    private static final Properties properties = Settings.getInstance();
    @NotNull
    @Override
    public WebDriver createDriver(@NotNull Capabilities capabilities) {
        ChromeOptions options = new ChromeOptions();
        String profilePath = properties.getProperty("profilePath");
        if(!(Objects.isNull(profilePath) || profilePath.isEmpty())) {
           // options.addArguments("disable-extensions");
            options.addArguments("user-data-dir="+profilePath);

        }
        return new ChromeDriver(options);
    }
}
