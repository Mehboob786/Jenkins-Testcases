package com.qa.doingerp.utilities;

import com.codeborne.selenide.*;
import com.qa.doingerp.exception.UnExpectedException;
import com.qa.doingerp.parameters.TestData;
import com.qa.doingerp.reports.Report;
import com.qa.doingerp.reports.Status;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

/**
 * This class contains, commonly used actions on web page
 */
public class UserActions {

	private final SelenideDriver driver;
	private final Report report;
	private TestData testData;

	public UserActions(SelenideDriver driver, Report report, TestData testData) {
		this.driver = driver;
		this.report = report;
		this.testData = testData;
	}


	public void openUrl(String url) {
		try {
			driver.getWebDriver().get(url);
			report.updateTestLog("Launch the application url", "URL:<b>" + url + "</b> is launched", Status.PASS);
		} catch (Exception e) {
			report.updateTestLog("Launch the application url", "URL:<b>" + url + "</b> is not successfully launched", Status.FAIL);
		}
	}

	/**
	 * Method to validate element is present
	 *
	 * @param element
	 * @return
	 */
	public boolean isElementPresent(SelenideElement element) {

		boolean toReturn;
		try {
			return element.is(Condition.visible, Duration.ofSeconds(10));
		} catch (Exception e) {
			toReturn = false;
		}
		return false;
	}

	/**
	 * Method to validate element is present
	 *
	 * @param element
	 * @return
	 */
	public boolean isElementPresentIDE(SelenideElement element, String description, String secs) {

		boolean toReturn;
		if (Objects.isNull(secs) || secs.trim().equalsIgnoreCase(""))
			secs = "15";
		try {
			toReturn = element.is(Condition.visible, Duration.ofSeconds(Integer.parseInt(secs)));
			report.updateTestLog(description, "The element is present", Status.PASS);
			return toReturn;
		} catch (Exception e) {
			report.updateTestLog(description, "The element is not present", Status.FAIL);
			toReturn = false;
		}
		return toReturn;
	}

	/**
	 * Method to validate element is present
	 *
	 * @param element
	 * @return
	 */
	public boolean isElementNotPresentIDE(SelenideElement element, String description) {

		boolean toReturn;
		try {
			toReturn = element.is(Condition.visible, Duration.ofSeconds(10));
			report.updateTestLog(description, "The element is present", Status.FAIL);
			return toReturn;
		} catch (Exception e) {
			report.updateTestLog(description, "The element is not present", Status.PASS);
			toReturn = true;
		}
		return toReturn;
	}

	public void waitForElementToBeClickable(By by, int timeSecs, String description) {

		try {
		/*WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), Duration.ofSeconds(timeSecs));
		wait.until(ExpectedConditions.elementToBeClickable(by));*/
			$(by).shouldBe(interactable, Duration.ofSeconds(timeSecs)).should(enabled, Duration.ofSeconds(timeSecs));
			report.updateTestLog(description, "The element to be clickable", Status.PASS);
		} catch (Exception e) {
			report.updateTestLog(description, "The element ito be clickable", Status.FAIL);

		}
	}

	public void waitForElementToPresent(By by, int timeSecs, String description) {

		try {
			/*WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), Duration.ofSeconds(timeSecs));
			wait.until(ExpectedConditions.presenceOfElementLocated(by));
			*/

			$(by).shouldBe(visible, Duration.ofSeconds(timeSecs)).should(appear);
			report.updateTestLog(description, "The element is present", Status.PASS);
		} catch (Exception e) {
			report.updateTestLog(description, "The element is not present", Status.FAIL);

		}
	}


	public void switchToFrameByElement(SelenideElement frame, String description) {

		try {
			driver.switchTo().frame(frame);
			report.updateTestLog(description, "switched successfully to frame ", Status.PASS);
		} catch (Exception e) {
			report.updateTestLog(description, "switched is not successfull to frame ", Status.FAIL);
		}

	}

	public void switchToDefaultContent() {


		try {
			driver.switchTo().defaultContent();
			report.updateTestLog("Frame switched to default content", "", Status.PASS);
		} catch (Exception e) {
			report.updateTestLog("Frame switched to default content is failed", "", Status.FAIL);
		}

	}

	/**
	 * Method to validate the element is not present
	 *
	 * @param element
	 * @return
	 */
	public boolean isElementNotPresent(SelenideElement element) {
		boolean toReturn = false;

		try {
			return !element.has(visible, Duration.ofSeconds(5));
		} catch (Exception e) {
			toReturn = true;
		}
		return toReturn;

	}

	/**
	 * method to clear the text box
	 *
	 * @param elements
	 */
	public void clear(SelenideElement... elements) {
		boolean isDisplayedEdtBox = false;
		for (SelenideElement eachElement : elements) {
			eachElement.clear();

		}

	}

	/**
	 * Method to make the element visible
	 *
	 * @param element
	 */

	public void makeBlock(SelenideElement element) {
		JavascriptExecutor js = (JavascriptExecutor) driver.getWebDriver();
		js.executeScript("arguments[0].style.display = 'block'", element);
	}

	/**
	 * Method to return the visible element
	 *
	 * @param lstEle
	 * @return
	 */
	public SelenideElement returnVisibleElement(List<SelenideElement> lstEle) {
		for (SelenideElement element : lstEle) {
			try {
				if (element.isDisplayed())
					return element;
			} catch (Exception e) {

			}
		}
		return null;

	}

	/**
	 * Method to click on web page
	 *
	 * @param buttonElementName
	 * @param btnName
	 * @param strPageName
	 */
	public void clickButton(SelenideElement buttonElementName, String btnName, String strPageName) {
		boolean blnFlag = false;

		try {
			buttonElementName.click();
			blnFlag = true;

		} catch (WebDriverException wdexc) {
			JavascriptExecutor js = (JavascriptExecutor) driver.getWebDriver();
			js.executeScript("arguments[0].click();", buttonElementName);
			blnFlag = true;
		} catch (Exception exc) {
			blnFlag = false;
		}

		if (blnFlag)
			report.updateTestLog("Click the <b>" + btnName + "</b> button from <b>" + strPageName + "</b>", "<b>" + btnName + "</b> button is clicked successfully", Status.PASS);
		else
			report.updateTestLog("Click the <b>" + btnName + "</b> button from <b>" + strPageName + "</b>", "<b>" + btnName + "</b> button is not clicked successfully", Status.FAIL);
	}

	/**
	 * Method to click on web page
	 *
	 * @param description
	 */
	public void clickButtonIDE(SelenideElement buttonElementName, String description) {
		boolean blnFlag = false;

		try {
			if (buttonElementName.is(Condition.visible, Duration.ofSeconds(10))) {
				buttonElementName.should(visible).click(ClickOptions.usingJavaScript());
				report.updateTestLog(description, "User action successfully completed", Status.PASS);
			} else {
				report.updateTestLog(description, "Button is not visible to click", Status.FAIL);

			}


		} catch (ElementNotInteractableException wdexc) {

			JavascriptExecutor js = (JavascriptExecutor) driver.getWebDriver();
			js.executeScript("arguments[0].click();", buttonElementName);
			blnFlag = true;
		} catch (Exception exc) {
			report.updateTestLog(description, "Button is not successfully clicked", Status.FAIL);
		}

	}

	public void doubleClick(SelenideElement buttonElementName, String description) {
		boolean blnFlag = false;

		try {
			buttonElementName.should(visible, interactable).doubleClick();
			blnFlag = true;

		} catch (WebDriverException wdexc) {

			JavascriptExecutor js = (JavascriptExecutor) driver.getWebDriver();
			js.executeScript("arguments[0].click();", buttonElementName);
			blnFlag = true;
		} catch (Exception exc) {
			report.updateTestLog(description, "Button is not successfully clicked", Status.FAIL);
			return;
		}

		if (blnFlag)
			report.updateTestLog(description, "User action successfully completed", Status.PASS);
		else
			report.updateTestLog(description, "Button is not successfully clicked", Status.FAIL);
	}

	public void rightClick(SelenideElement buttonElementName, String description) {
		boolean blnFlag = false;

		try {

			buttonElementName.should(visible).contextClick();
			blnFlag = true;

		} catch (WebDriverException wdexc) {

			JavascriptExecutor js = (JavascriptExecutor) driver.getWebDriver();
			js.executeScript("arguments[0].click();", buttonElementName);
			blnFlag = true;
		} catch (Exception exc) {
			report.updateTestLog(description, "Button is not successfully clicked", Status.FAIL);
			return;
		}

		if (blnFlag)
			report.updateTestLog(description, "User action successfully completed", Status.PASS);
		else
			report.updateTestLog(description, "Button is not successfully clicked", Status.FAIL);
	}

	/**
	 * Method to click the link on web page
	 *
	 * @param linkElementName
	 * @param lnkName
	 * @param strPageName
	 */
	public void clickLink(SelenideElement linkElementName, String lnkName, String strPageName) {
		boolean blnFlag = false;

		try {
			linkElementName.click();
			blnFlag = true;
		} catch (WebDriverException wdexc) {
			JavascriptExecutor js = (JavascriptExecutor) driver.getWebDriver();
			js.executeScript("arguments[0].click();", linkElementName);
			blnFlag = true;
		} catch (Exception exc) {
			blnFlag = false;
		}

		if (blnFlag)
			report.updateTestLog("Click the <b>" + lnkName + "</b> link from " + strPageName, "<b>" + lnkName + "</b> link is clicked successfully", Status.PASS);
		else
			report.updateTestLog("Click the <b>" + lnkName + "</b> link from " + strPageName, "<b>" + lnkName + "</b> link is not clicked successfully", Status.FAIL);
	}

	/**
	 * Method to enter in to text box
	 *
	 * @param editBox
	 * @param strFieldValue
	 * @param strFieldName
	 * @param strPageName
	 */
	public void typeInEditBox(SelenideElement editBox, String strFieldValue, String strFieldName, String strPageName) {
		boolean blnFlag = false;

		if (isElementPresent(editBox)) {
			editBox.clear();
			editBox.type(strFieldValue);
			blnFlag = true;
		}

		if (blnFlag)
			report.updateTestLog("Enter the text <b>" + strFieldValue + "</b> for the field <b>" + strFieldName + "</b> in " + strPageName + " page", "Text successfully entered", Status.PASS);
		else
			report.updateTestLog("Enter the text <b>" + strFieldValue + "</b> for the field <b>" + strFieldName + "</b> in " + strPageName + " page", "Unable to enter the text in <b>" + strFieldName + "</b>", Status.FAIL);

	}

	/**
	 * Method to enter in to text box
	 *
	 * @param editBox
	 * @param strFieldValue
	 * @param description
	 */
	public void typeInEditBoxIDE(SelenideElement editBox, String strFieldValue, String description) {
		boolean blnFlag = false;

		if (isElementPresent(editBox)) {
			editBox.clear();
			editBox.sendKeys(strFieldValue);
			blnFlag = true;
		}

		if (blnFlag) {
			report.updateTestLog(description, "Text successfully entered", Status.PASS);
		} else {
			report.updateTestLog(description, "Unable to enter the text", Status.FAIL);
			throw new UnExpectedException("Element is not available to click");
		}
	}

	public void typeInAndSelect(SelenideElement editBox, String strFieldValue, String description) throws InterruptedException {
		boolean blnFlag = false;

		if (isElementPresent(editBox)) {
			editBox.setValue(strFieldValue).pressEnter();
			synchronized (editBox) {
				editBox.should(visible).wait(3000);
			}
			editBox.press(Keys.ARROW_DOWN, Keys.ENTER);
			blnFlag = true;
		}

		if (blnFlag)
			report.updateTestLog(description, "Text successfully entered", Status.PASS);
		else
			report.updateTestLog(description, "Unable to enter the text", Status.FAIL);

	}

	public void switchToFrame(SelenideElement ele, String description) {
		try {
			driver.switchTo().frame(ele);
		} catch (Exception e) {

			report.updateTestLog("Switch to the frame", description, Status.FAIL);
		}
	}


	/**
	 * Method to verify text contains in the element
	 *
	 * @param objIdentifier
	 * @param expectedValue
	 * @param pageName
	 */
	public void verifyElementTextContains(SelenideElement objIdentifier, String expectedValue, String pageName) {

		if (isElementPresent(objIdentifier)) {
			String actualValue = objIdentifier.getText().trim();

			if (expectedValue.contains(actualValue) || actualValue.contains(expectedValue))
				report.updateTestLog("Verify <b>" + expectedValue + "</b> text present in page <b>" + pageName + "</b>", "Text present in page <b>" + pageName + "</b>", Status.PASS);
			else
				report.updateTestLog("Verify <b>" + expectedValue + "</b> text present in page <b>" + pageName + "</b>", "Actual text: <b>" + actualValue + "</b> and Expected text: <b>" + expectedValue + "</b>", Status.FAIL);

		} else {
			report.updateTestLog("Verify <b>" + expectedValue + "</b> text present in page <b>" + pageName + "</b>", expectedValue + " is not present", Status.FAIL);

		}
	}


	/**
	 * Method to verify text contains in the element
	 *
	 * @param objIdentifier
	 * @param expectedValue
	 */
	public void verifyElementTextContainsIDE(SelenideElement objIdentifier, String expectedValue, String description) {

		if (isElementPresent(objIdentifier)) {
			String actualValue = objIdentifier.getText().trim();

			if (actualValue.contains(expectedValue))
				report.updateTestLog(description, expectedValue + " text present in the element", Status.PASS);
			else
				report.updateTestLog(description, expectedValue + " text not present on the element", Status.FAIL);

		}
	}

	/**
	 * Method to verify text contains in the element
	 *
	 * @param objIdentifier
	 * @param expectedValue
	 */
	public void verifyElementFullText(SelenideElement objIdentifier, String expectedValue, String description) {

		if (isElementPresent(objIdentifier)) {
			String actualValue = objIdentifier.getText().trim();

			if (actualValue.contains(expectedValue))
				report.updateTestLog(description, expectedValue + " text present in the element", Status.PASS);
			else
				report.updateTestLog(description, expectedValue + " text not present on the element", Status.FAIL);

		}
	}


	public void selectByTextIDE(SelenideElement selectIdentifier, String textName, String description) {

		try {
			selectIdentifier.should(visible).selectOption(textName);
			report.updateTestLog(description, "Value successfully selected", Status.PASS);
		} catch (Exception e) {
			report.updateTestLog(description, "Unable to select the value from dropdown " + e.getMessage(), Status.FAIL);
		}


	}

	public void selectByValueIDE(SelenideElement selectIdentifier, String textName, String description) {

		try {
			selectIdentifier.should(visible).selectOptionByValue(textName);
			report.updateTestLog(description, "Value successfully selected", Status.PASS);
		} catch (Exception e) {
			report.updateTestLog(description, "Unable to select the value from dropdown " + e.getMessage(), Status.FAIL);
		}


	}

	public void selectByIndexIDE(SelenideElement selectIdentifier, String index, String description) {

		try {
			selectIdentifier.should(visible).selectOption(index);
			report.updateTestLog(description, "Value successfully selected", Status.PASS);
		} catch (Exception e) {
			report.updateTestLog(description, "Unable to select the value from dropdown " + e.getMessage(), Status.FAIL);
		}


	}

	public boolean waitForJStoLoad(int timeout) {

		WebDriverWait wait = new WebDriverWait(driver.getWebDriver(), Duration.ofSeconds(timeout));

		// wait for jQuery to load
		ExpectedCondition<Boolean> jQueryLoad = new ExpectedCondition<Boolean>() {

			@Override
			public Boolean apply(WebDriver driver) {
				try {
					JavascriptExecutor js = ((JavascriptExecutor) driver);
					return ((Long) (js.executeScript("return jQuery.active")) == 0);
				} catch (Exception e) {
					return true;
				}
			}
		};

		// wait for Javascript to load
		ExpectedCondition<Boolean> jsLoad = new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				JavascriptExecutor js = ((JavascriptExecutor) driver);
				return js.executeScript("return document.readyState")
						.toString().equals("complete");
			}
		};

		return wait.until(jQueryLoad) && wait.until(jsLoad);
	}


	public void pressEnter(SelenideElement ele, String description) {
		try {
			ele.should(visible).pressEnter();
			report.updateTestLog("Press the enter key", description, Status.PASS);
		} catch (Exception e) {
			report.updateTestLog("Press the enter key", description, Status.FAIL);
		}
	}

	public void pressDownAndEnter(SelenideElement ele, String description, String times) {
		try {
			synchronized (ele) {

				ele.should(visible).wait(3000);
			}

			if (Objects.isNull(times) || times.isEmpty())
				times = "1";
			List<CharSequence> charSequence = new ArrayList<CharSequence>();
			for (int i = 0; i < Integer.parseInt(times); i++) {
				charSequence.add(Keys.ARROW_DOWN);
			}
			ele.should(visible).press(charSequence.toArray(new CharSequence[0])).press(Keys.ENTER);
			report.updateTestLog("Press the enter key", description, Status.PASS);
		} catch (Exception e) {
			report.updateTestLog("Press the enter key", description, Status.FAIL);
		}
	}

	public void pressUpAndEnter(SelenideElement ele, String description, String times) {
		try {
			synchronized (ele) {

				ele.should(visible).wait(3000);
			}
			if (Objects.isNull(times) || times.isEmpty())
				times = "1";
			List<CharSequence> charSequence = new ArrayList<CharSequence>();
			for (int i = 0; i < Integer.parseInt(times); i++) {
				charSequence.add(Keys.ARROW_UP);
			}
			CharSequence[] chars = charSequence.toArray(new CharSequence[0]);
			ele.should(visible).press(chars).press(Keys.ENTER);
			report.updateTestLog("Press the enter key", description, Status.PASS);
		} catch (Exception e) {
			report.updateTestLog("Press the enter key", description, Status.FAIL);
		}
	}


	public String getTextOrAttribute(SelenideElement element, String attr) {
		if (isElementPresent(element)) {
			if (Objects.nonNull(attr))
				return element.should(visible).getAttribute(attr);
			else
				return element.should(visible).getText();
		} else {
			report.updateTestLog("Get the text for given element", "Element is not visile at the moment", Status.FAIL);

		}
		return null;
	}

	public void sizeEquals(ElementsCollection element, String description, int size) {
		element.shouldHave(CollectionCondition.size(size), Duration.ofSeconds(15));
		report.updateTestLog(description, "Validate the size of the element should be equal to " + size, Status.PASS);
	}

	public void sizeGreater(ElementsCollection element, String description, int size) {
		element.shouldHave(CollectionCondition.sizeGreaterThan(size), Duration.ofSeconds(15));
		report.updateTestLog(description, "Validate the size of the element should be greater than " + size, Status.PASS);
	}

	public void sizeLesser(ElementsCollection element, String description, int size) {
		element.shouldHave(CollectionCondition.sizeLessThan(size));
		report.updateTestLog(description, "Validate the size of the element should be lesser then "+size, 			Status.PASS);
	}
}