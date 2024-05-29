package com.qa.doingerp.utilities;

import com.codeborne.selenide.*;
import com.codeborne.selenide.impl.CiReportUrl;
import org.openqa.selenium.MutableCapabilities;

public class SelenideCustomConfiguration implements Config {

    private final PropertiesReader properties = new PropertiesReader("selenide.properties");


    private String browser = this.getProperty("selenide.browser", "chrome");
    private boolean headless = Boolean.parseBoolean(this.getProperty("selenide.headless", "false"));
    private String remote = this.getProperty("selenide.remote", (String)null);
    private String browserSize = this.getProperty("selenide.browserSize", "1366x768");
    private String browserVersion = this.getProperty("selenide.browserVersion", (String)null);
    private String browserPosition = this.getProperty("selenide.browserPosition", (String)null);
    private boolean webdriverLogsEnabled = Boolean.parseBoolean(this.getProperty("selenide.webdriverLogsEnabled", "false"));
    private String browserBinary = this.getProperty("selenide.browserBinary", (String)null);
    private String pageLoadStrategy = this.getProperty("selenide.pageLoadStrategy", "normal");
    private long pageLoadTimeout = Long.parseLong(this.getProperty("selenide.pageLoadTimeout", "30000"));
    private MutableCapabilities browserCapabilities = new MutableCapabilities();
    private String baseUrl = this.getProperty("selenide.baseUrl", "http://localhost:8080");
    private long timeout = Long.parseLong(this.getProperty("selenide.timeout", "4000"));
    private long pollingInterval = Long.parseLong(this.getProperty("selenide.pollingInterval", "200"));
    private boolean holdBrowserOpen = Boolean.parseBoolean(this.getProperty("selenide.holdBrowserOpen", "false"));
    private boolean reopenBrowserOnFail = Boolean.parseBoolean(this.getProperty("selenide.reopenBrowserOnFail", "true"));
    private boolean clickViaJs = Boolean.parseBoolean(this.getProperty("selenide.clickViaJs", "false"));
    private boolean screenshots = Boolean.parseBoolean(this.getProperty("selenide.screenshots", "true"));
    private boolean savePageSource = Boolean.parseBoolean(this.getProperty("selenide.savePageSource", "true"));
    private String reportsFolder = this.getProperty("selenide.reportsFolder", "build/reports/tests");
    private String downloadsFolder = this.getProperty("selenide.downloadsFolder", "build/downloads");
    private String reportsUrl = (new CiReportUrl()).getReportsUrl(this.getProperty("selenide.reportsUrl", (String)null));
    private boolean fastSetValue = Boolean.parseBoolean(this.getProperty("selenide.fastSetValue", "false"));
    private TextCheck textCheck;
    private SelectorMode selectorMode;
    private AssertionMode assertionMode;
    private FileDownloadMode fileDownload;
    private boolean proxyEnabled;
    private String proxyHost;
    private int proxyPort;
    private long remoteReadTimeout;
    private long remoteConnectionTimeout;

    public SelenideCustomConfiguration() {
        this.textCheck = TextCheck.valueOf(this.getProperty("selenide.textCheck", TextCheck.PARTIAL_TEXT.name()));
        this.selectorMode = SelectorMode.valueOf(this.getProperty("selenide.selectorMode", SelectorMode.CSS.name()));
        this.assertionMode = AssertionMode.valueOf(this.getProperty("selenide.assertionMode", AssertionMode.STRICT.name()));
        this.fileDownload = FileDownloadMode.valueOf(this.getProperty("selenide.fileDownload", FileDownloadMode.HTTPGET.name()));
        this.proxyEnabled = Boolean.parseBoolean(this.getProperty("selenide.proxyEnabled", "false"));
        this.proxyHost = this.getProperty("selenide.proxyHost", (String)null);
        this.proxyPort = Integer.parseInt(this.getProperty("selenide.proxyPort", "0"));
        this.remoteReadTimeout = Long.parseLong(this.getProperty("selenide.remoteReadTimeout", "90000"));
        this.remoteConnectionTimeout = Long.parseLong(this.getProperty("selenide.remoteConnectionTimeout", "10000"));
    }

    public String baseUrl() {
        return this.baseUrl;
    }

    public SelenideCustomConfiguration baseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public long timeout() {
        return this.timeout;
    }

    public SelenideCustomConfiguration timeout(long timeout) {
        this.timeout = timeout;
        return this;
    }

    public long pollingInterval() {
        return this.pollingInterval;
    }

    public SelenideCustomConfiguration pollingInterval(long pollingInterval) {
        this.pollingInterval = pollingInterval;
        return this;
    }

    public boolean holdBrowserOpen() {
        return this.holdBrowserOpen;
    }

    public SelenideCustomConfiguration holdBrowserOpen(boolean holdBrowserOpen) {
        this.holdBrowserOpen = holdBrowserOpen;
        return this;
    }

    public boolean reopenBrowserOnFail() {
        return this.reopenBrowserOnFail;
    }

    public SelenideCustomConfiguration reopenBrowserOnFail(boolean reopenBrowserOnFail) {
        this.reopenBrowserOnFail = reopenBrowserOnFail;
        return this;
    }

    public boolean clickViaJs() {
        return this.clickViaJs;
    }

    public SelenideCustomConfiguration clickViaJs(boolean clickViaJs) {
        this.clickViaJs = clickViaJs;
        return this;
    }

    public boolean screenshots() {
        return this.screenshots;
    }

    public SelenideCustomConfiguration screenshots(boolean screenshots) {
        this.screenshots = screenshots;
        return this;
    }

    public boolean savePageSource() {
        return this.savePageSource;
    }

    public SelenideCustomConfiguration savePageSource(boolean savePageSource) {
        this.savePageSource = savePageSource;
        return this;
    }

    public String reportsFolder() {
        return this.reportsFolder;
    }

    public SelenideCustomConfiguration reportsFolder(String reportsFolder) {
        this.reportsFolder = reportsFolder;
        return this;
    }

    public String downloadsFolder() {
        return this.downloadsFolder;
    }

    public SelenideCustomConfiguration downloadsFolder(String downloadsFolder) {
        this.downloadsFolder = downloadsFolder;
        return this;
    }

    public String reportsUrl() {
        return this.reportsUrl;
    }

    public SelenideCustomConfiguration reportsUrl(String reportsUrl) {
        this.reportsUrl = reportsUrl;
        return this;
    }

    public boolean fastSetValue() {
        return this.fastSetValue;
    }

    public TextCheck textCheck() {
        return this.textCheck;
    }

    public SelenideCustomConfiguration fastSetValue(boolean fastSetValue) {
        this.fastSetValue = fastSetValue;
        return this;
    }

    public SelenideCustomConfiguration textCheck(TextCheck textCheck) {
        this.textCheck = textCheck;
        return this;
    }

    public SelectorMode selectorMode() {
        return this.selectorMode;
    }

    public SelenideCustomConfiguration selectorMode(SelectorMode selectorMode) {
        this.selectorMode = selectorMode;
        return this;
    }

    public AssertionMode assertionMode() {
        return this.assertionMode;
    }

    public SelenideCustomConfiguration assertionMode(AssertionMode assertionMode) {
        this.assertionMode = assertionMode;
        return this;
    }

    public FileDownloadMode fileDownload() {
        return this.fileDownload;
    }

    public SelenideCustomConfiguration fileDownload(FileDownloadMode fileDownload) {
        this.fileDownload = fileDownload;
        return this;
    }

    public boolean proxyEnabled() {
        return this.proxyEnabled;
    }

    public SelenideCustomConfiguration proxyEnabled(boolean proxyEnabled) {
        this.proxyEnabled = proxyEnabled;
        return this;
    }

    public String proxyHost() {
        return this.proxyHost;
    }

    public SelenideCustomConfiguration proxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
        return this;
    }

    public int proxyPort() {
        return this.proxyPort;
    }

    public SelenideCustomConfiguration proxyPort(int proxyPort) {
        this.proxyPort = proxyPort;
        return this;
    }

    public String browser() {
        return this.browser;
    }

    public SelenideCustomConfiguration browser(String browser) {
        this.browser = browser;
        return this;
    }

    public boolean headless() {
        return this.headless;
    }

    public SelenideCustomConfiguration headless(boolean headless) {
        this.headless = headless;
        return this;
    }

    public String remote() {
        return this.remote;
    }

    public SelenideCustomConfiguration remote(String remote) {
        this.remote = remote;
        return this;
    }

    public String browserSize() {
        return this.browserSize;
    }

    public SelenideCustomConfiguration browserSize(String browserSize) {
        this.browserSize = browserSize;
        return this;
    }

    public String browserVersion() {
        return this.browserVersion;
    }

    public SelenideCustomConfiguration browserVersion(String browserVersion) {
        this.browserVersion = browserVersion;
        return this;
    }

    public String browserPosition() {
        return this.browserPosition;
    }

    public SelenideCustomConfiguration browserPosition(String browserPosition) {
        this.browserPosition = browserPosition;
        return this;
    }

    public boolean webdriverLogsEnabled() {
        return this.webdriverLogsEnabled;
    }

    public SelenideCustomConfiguration webdriverLogsEnabled(boolean webdriverLogsEnabled) {
        this.webdriverLogsEnabled = webdriverLogsEnabled;
        return this;
    }

    public String browserBinary() {
        return this.browserBinary;
    }

    public SelenideCustomConfiguration browserBinary(String browserBinary) {
        this.browserBinary = browserBinary;
        return this;
    }

    public String pageLoadStrategy() {
        return this.pageLoadStrategy;
    }

    public long pageLoadTimeout() {
        return this.pageLoadTimeout;
    }

    public SelenideCustomConfiguration pageLoadStrategy(String pageLoadStrategy) {
        this.pageLoadStrategy = pageLoadStrategy;
        return this;
    }

    public SelenideCustomConfiguration pageLoadTimeout(long pageLoadTimeout) {
        this.pageLoadTimeout = pageLoadTimeout;
        return this;
    }

    public MutableCapabilities browserCapabilities() {
        return this.browserCapabilities;
    }

    public SelenideCustomConfiguration browserCapabilities(MutableCapabilities browserCapabilities) {
        this.browserCapabilities = browserCapabilities;
        return this;
    }

    private String getProperty(String key, String defaultValue) {
        String value = this.properties.getProperty(key, defaultValue);
        return value != null && value.trim().isEmpty() && defaultValue == null ? null : value;
    }

    public long remoteReadTimeout() {
        return this.remoteReadTimeout;
    }

    public SelenideCustomConfiguration remoteReadTimeout(long remoteReadTimeout) {
        this.remoteReadTimeout = remoteReadTimeout;
        return this;
    }

    public long remoteConnectionTimeout() {
        return this.remoteConnectionTimeout;
    }

    public SelenideCustomConfiguration remoteConnectionTimeout(long remoteConnectionTimeout) {
        this.remoteConnectionTimeout = remoteConnectionTimeout;
        return this;
    }

    public String toString() {
        return this.getClass().getSimpleName();
    }
}
