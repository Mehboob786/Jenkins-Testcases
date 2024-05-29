package com.qa.doingerp.components;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selectors;
import com.codeborne.selenide.SelenideElement;
import com.github.javafaker.Faker;
import com.qa.doingerp.core.ReusableLibrary;
import com.qa.doingerp.core.ScriptHelper;
import com.qa.doingerp.core.SeleniumIDE;
import com.qa.doingerp.parameters.CommonData;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;

public class IDEfeatures extends ReusableLibrary {
    /**
     * Constructor to initialize the {@link ScriptHelper} object and in turn the objects wrapped by it
     *
     * @param scriptHelper The {@link ScriptHelper} object
     * @param: driver The {@link: WebDriver} object
     */
    public IDEfeatures(ScriptHelper scriptHelper) {
        super(scriptHelper);
    }

    private SeleniumIDE seleniumIDE;
    private CommonData commonData;

    /**
     * Method to click the element on webpage
     */

    public void click(){

        if(seleniumIDE.getValue().startsWith("#")){
            String parameter = testData.getData(seleniumIDE.getValue().substring(1));
            seleniumIDE.setValue(parameter);
        }

        String holder =replacePlaceHolder(seleniumIDE.getTarget(), seleniumIDE.getValue());
        seleniumIDE.setTarget(holder);
        SelenideElement element = objElement(seleniumIDE.getTarget());
        userActions.clickButtonIDE(element, seleniumIDE.getDescription());
    }


    private String replacePlaceHolder(String target, String value){
      String placeHolder =  testData.getObjRepoData(target);
        if(placeHolder.contains("<dropDownText>")){
            placeHolder= placeHolder.replace("<dropDownText>", value);
        }
        return placeHolder;
    }

    /**
     * Method to double click the element on webpage
     */
    public void doubleClick(){

        if(seleniumIDE.getTarget().equalsIgnoreCase("objSignOut")){
            SelenideElement element = objElement(seleniumIDE.getTarget());
        }
        SelenideElement element = objElement(seleniumIDE.getTarget());
        userActions.doubleClick(element, seleniumIDE.getDescription());
    }
    /**
     * Method to double click the element on webpage
     */
    public void rightClick(){

        if(seleniumIDE.getTarget().equalsIgnoreCase("objSignOut")){
            SelenideElement element = objElement(seleniumIDE.getTarget());
        }
        SelenideElement element = objElement(seleniumIDE.getTarget());
        userActions.rightClick(element, seleniumIDE.getDescription());
    }



    /**
     * Method to open the application
     */
    public void open(){

        if(seleniumIDE.getValue().startsWith("#")){
            String parameter = testData.getData(seleniumIDE.getValue().substring(1));
            if(parameter.contains("{")){
                String colName = parameter.split("\\{")[1].split("}")[0];
                String text = testData.getData(colName);
                parameter = parameter.replace("{"+colName+"}", text);
            }
            seleniumIDE.setValue(parameter);
        }
        userActions.openUrl(seleniumIDE.getValue());

        }


        public void hasSizeEquals(){
            if(seleniumIDE.getValue().startsWith("#")){
                String parameter = testData.getData(seleniumIDE.getValue().substring(1));
                seleniumIDE.setValue(parameter);
            }
            ElementsCollection element = objElements(seleniumIDE.getTarget());
            userActions.sizeEquals(element, seleniumIDE.getDescription(), Integer.parseInt(seleniumIDE.getValue()));

        }
    public void hasSizeGreaterThans(){
        if(seleniumIDE.getValue().startsWith("#")){
            String parameter = testData.getData(seleniumIDE.getValue().substring(1));
            seleniumIDE.setValue(parameter);
        }
        ElementsCollection element = objElements(seleniumIDE.getTarget());
        userActions.sizeGreater(element, seleniumIDE.getDescription(), Integer.parseInt(seleniumIDE.getValue()));

    }
    public void hasSizeLesserThan(){
        if(seleniumIDE.getValue().startsWith("#")){
            String parameter = testData.getData(seleniumIDE.getValue().substring(1));
            seleniumIDE.setValue(parameter);
        }
        ElementsCollection element = objElements(seleniumIDE.getTarget());
        userActions.sizeLesser(element, seleniumIDE.getDescription(), Integer.parseInt(seleniumIDE.getValue()));

    }

    /**
     * Method to enter the text in text box
      */
    public void type(){

        if(seleniumIDE.getValue().startsWith("#")){
            String parameter = testData.getData(seleniumIDE.getValue().substring(1));
            seleniumIDE.setValue(parameter);
        }else if(seleniumIDE.getValue().contains("[")|| seleniumIDE.getValue().contains("]")){
            String value = fakerData(seleniumIDE.getValue());
            seleniumIDE.setValue(value);
            String key = seleniumIDE.getTarget().replace("obj", "");
            commonData.setRunTimeData(key, value);
        }else if(seleniumIDE.getValue().startsWith("$")) {
            String parameter = commonData.getRunTimeData(seleniumIDE.getValue().substring(1));
            seleniumIDE.setValue(parameter);
        }

            SelenideElement element = objElement(seleniumIDE.getTarget());
        userActions.typeInEditBoxIDE(element,seleniumIDE.getValue(), seleniumIDE.getDescription()+": "+seleniumIDE.getValue());

        }

    public void typeAndSelect() throws InterruptedException {

        if(seleniumIDE.getValue().startsWith("#")){
            String parameter = testData.getData(seleniumIDE.getValue().substring(1));
            //replace any random placeholder value
            if(parameter.contains("[")|| parameter.contains("]")) {
                String value = fakerData(parameter);
                seleniumIDE.setValue(value);
                String key = seleniumIDE.getTarget().replace("obj", "");
                commonData.setRunTimeData(key, value);
            }else{
                seleniumIDE.setValue(parameter);
            }

        }else if(seleniumIDE.getValue().contains("[")|| seleniumIDE.getValue().contains("]")){
            String value = fakerData(seleniumIDE.getValue());
            seleniumIDE.setValue(value);
            String key = seleniumIDE.getTarget().replace("obj", "");
            commonData.setRunTimeData(key, value);
        }else if(seleniumIDE.getValue().startsWith("$")) {
            String key = seleniumIDE.getValue().substring(1).replace("obj", "");
            String parameter = commonData.getRunTimeData(key);
            seleniumIDE.setValue(parameter);
        }

        SelenideElement element = objElement(seleniumIDE.getTarget());
        userActions.typeInAndSelect(element,seleniumIDE.getValue(), seleniumIDE.getDescription()+": "+seleniumIDE.getValue());

    }

        private String fakerData(String ip){
        Faker faker = new Faker();
        String value = "";
        if(ip.contains("randNum")) {
            String number = faker.regexify("[1-9]{4}");
             value = seleniumIDE.getValue().replace("[", "")
                     .replace("]","")
                     .replace("randNum", number);
        }else if(ip.contains("randName")) {
                String number = faker.regexify("[A-Z]{4}");
                value = seleniumIDE.getValue().replace("[", "")
                        .replace("]","")
                        .replace("randName", number);
            }
        return value;
        }

    public void getAttribute(){

        if(seleniumIDE.getValue().startsWith("&")){
            String parameter = seleniumIDE.getValue().substring(1).split(",")[0];
            String attr = seleniumIDE.getValue().substring(1).split(",")[1];

            SelenideElement element = objElement(seleniumIDE.getTarget());
            commonData.setRunTimeData(parameter,userActions.getTextOrAttribute(element, attr)) ;
            seleniumIDE.setValue(parameter);
        }

    }
    public void getText(){

        if(seleniumIDE.getValue().startsWith("&")){
            String parameter = seleniumIDE.getValue().substring(1);
            SelenideElement element = objElement(seleniumIDE.getTarget());
            commonData.setRunTimeData(parameter,userActions.getTextOrAttribute(element, null )) ;
            seleniumIDE.setValue(parameter);
        }

    }

    public void switchToFrame(){
        SelenideElement ele = objElement(seleniumIDE.getTarget());
        userActions.switchToFrame(ele, seleniumIDE.getDescription());
    }



    /**
     * Method to select the value from dropdown using text
     */
    public void selectByText(){
        if(seleniumIDE.getValue().startsWith("#")){
            String parameter = testData.getData(seleniumIDE.getValue().substring(1,seleniumIDE.getValue().length()));
            seleniumIDE.setValue(parameter);
        }
        SelenideElement element = objElement(seleniumIDE.getTarget());
        userActions.selectByTextIDE(element,seleniumIDE.getValue(), seleniumIDE.getDescription()+": "+seleniumIDE.getValue());
    }

    /**
     * Method to select the value from dropdown using value
     */
    public void selectByValue(){
        if(seleniumIDE.getValue().startsWith("#")){
            String parameter = testData.getData(seleniumIDE.getValue().substring(1,seleniumIDE.getValue().length()));
            seleniumIDE.setValue(parameter);
        }
        SelenideElement element = objElement(seleniumIDE.getTarget());
        userActions.selectByValueIDE(element,seleniumIDE.getValue(), seleniumIDE.getDescription()+": "+seleniumIDE.getValue());
    }

    /**
     * Method to select the value from dropdown using index
     */
    public void selectByIndex(){
        if(seleniumIDE.getValue().startsWith("#")){
            String parameter = testData.getData(seleniumIDE.getValue().substring(1,seleniumIDE.getValue().length()));
            seleniumIDE.setValue(parameter);
        }
        SelenideElement element = objElement(seleniumIDE.getTarget());
        userActions.selectByIndexIDE(element,seleniumIDE.getValue(), seleniumIDE.getDescription()+": "+seleniumIDE.getValue());
    }

    /**
     * Method to validate whether element should be present
     */
    public void verifyElementPresent(){
        if(seleniumIDE.getValue().startsWith("#")){
            String parameter = testData.getData(seleniumIDE.getValue().substring(1));
            seleniumIDE.setValue(parameter);
        }

        SelenideElement element = objElement(seleniumIDE.getTarget());
        userActions.isElementPresentIDE(element, seleniumIDE.getDescription(), seleniumIDE.getValue());

    }

    public void verifyElementTextContains(){
        if(seleniumIDE.getValue().startsWith("#")){
            String parameter = testData.getData(seleniumIDE.getValue().substring(1));
            seleniumIDE.setValue(parameter);
        }else if(seleniumIDE.getValue().startsWith("@")){
            String parameter = seleniumIDE.getValue().substring(1);

            String runTimeData = commonData.getRunTimeData(parameter);
            seleniumIDE.setValue(runTimeData);
        }

        SelenideElement element = objElement(seleniumIDE.getTarget());
        userActions.verifyElementTextContainsIDE(element,  seleniumIDE.getValue(),  seleniumIDE.getDescription());


    }
    public void verifyElementFullText(){
        if(seleniumIDE.getValue().startsWith("#")){
            String parameter = testData.getData(seleniumIDE.getValue().substring(1));
            seleniumIDE.setValue(parameter);
        }else if(seleniumIDE.getValue().startsWith("@")){
            String parameter = seleniumIDE.getValue().substring(1);
            String runTimeData = commonData.getRunTimeData(parameter);
            seleniumIDE.setValue(runTimeData);
        }

        SelenideElement element = objElement(seleniumIDE.getTarget());
        userActions.verifyElementFullText(element,  seleniumIDE.getValue(),  seleniumIDE.getDescription());


    }

    /**
     * Method to validate whether element should be present
     */
    public void verifyElementNotPresent(){
        if(seleniumIDE.getValue().startsWith("#")){
            String parameter = testData.getData(seleniumIDE.getValue().substring(1));
            seleniumIDE.setValue(parameter);
        }
        SelenideElement element = objElement(seleniumIDE.getTarget());
        userActions.isElementNotPresentIDE(element, seleniumIDE.getDescription());

    }

    public void waitForElementToPresent(){
        if(seleniumIDE.getValue().startsWith("#")){
            String parameter = testData.getData(seleniumIDE.getValue().substring(1));
            seleniumIDE.setValue(parameter);
        }else if(seleniumIDE.getValue().isEmpty()){
            seleniumIDE.setValue("15");
        }


        userActions.waitForElementToPresent(By(seleniumIDE.getTarget()), Integer.parseInt(seleniumIDE.getValue()), seleniumIDE.getDescription());

    }
    public void waitForElementToBeClickable(){
        if(seleniumIDE.getValue().startsWith("#")){
            String parameter = testData.getData(seleniumIDE.getValue().substring(1));
            seleniumIDE.setValue(parameter);
        }else if(seleniumIDE.getValue().isEmpty()){
            seleniumIDE.setValue("15");
        }

        userActions.waitForElementToBeClickable(By(seleniumIDE.getTarget()), Integer.parseInt(seleniumIDE.getValue()), seleniumIDE.getDescription());

    }


    public void setWindowSize(){

        if(seleniumIDE.getValue().startsWith("#")){
            String parameter = testData.getData(seleniumIDE.getValue().substring(1));
            seleniumIDE.setValue(parameter);
        }
        int width = Integer.parseInt(seleniumIDE.getValue().split(",")[0]);
        int height = Integer.parseInt(seleniumIDE.getValue().split(",")[1]);


        Dimension d = new Dimension(width,height);
        //Resize current window to the set dimension
       driver.getWebDriver().manage().window().setSize(d);
    }

    public void navigateBack(){
       driver.getWebDriver().navigate().back();
    }


    public void switchToFrameByElement(){
        if(seleniumIDE.getValue().startsWith("#")){
            String parameter = testData.getData(seleniumIDE.getValue().substring(1));
            seleniumIDE.setValue(parameter);
        }
        SelenideElement element = objElement(seleniumIDE.getTarget());
        userActions.switchToFrameByElement(element, seleniumIDE.getDescription());
    }

    public void switchToDefaultContent(){
        userActions.switchToDefaultContent();
    }


    public void pressEnter(){
        SelenideElement ele = objElement(seleniumIDE.getTarget());
        userActions.pressEnter(ele, seleniumIDE.getDescription());
    }

    public void pressDownAndEnter(){
        if(seleniumIDE.getValue().startsWith("#")){
            String parameter = testData.getData(seleniumIDE.getValue().substring(1));
            seleniumIDE.setValue(parameter);
        }
        SelenideElement ele = objElement(seleniumIDE.getTarget());
        userActions.pressDownAndEnter(ele, seleniumIDE.getDescription(), seleniumIDE.getValue());
    }
    public void pressUpAndEnter(){
        if(seleniumIDE.getValue().startsWith("#")){
            String parameter = testData.getData(seleniumIDE.getValue().substring(1));
            seleniumIDE.setValue(parameter);
        }
        SelenideElement ele = objElement(seleniumIDE.getTarget());
        userActions.pressUpAndEnter(ele, seleniumIDE.getDescription(), seleniumIDE.getValue());
    }

    private ElementsCollection objElements(String properties){

        if (properties.toLowerCase().startsWith("obj")) {
            properties = testData.getObjRepoData(properties);
        }
        String selectorStrategy = properties.split("-")[0];

        String selectorValue = properties.substring(selectorStrategy.length()+1);

        if(selectorStrategy.equalsIgnoreCase("xpath"))
            return driver.$$x(selectorValue);
        else if(selectorStrategy.equalsIgnoreCase("name"))
            return driver.$$(By.name(selectorValue));
        else  if(selectorStrategy.equalsIgnoreCase("className"))
            return driver.$$(By.className(selectorValue));
        else  if(selectorStrategy.equalsIgnoreCase("cssSelector") || selectorStrategy.equalsIgnoreCase("css"))
            return driver.$$(selectorValue);
        else  if(selectorStrategy.equalsIgnoreCase("linkText"))
            return driver.$$(By.linkText(selectorValue));
        else  if(selectorStrategy.equalsIgnoreCase("partialLinkText"))
            return driver.$$(By.partialLinkText(selectorValue));
        else  if(selectorStrategy.equalsIgnoreCase("id"))
            return driver.$$(By.id(selectorValue));
        else  if(selectorStrategy.equalsIgnoreCase("tagName"))
            return driver.$$(By.tagName(selectorValue));
        else if(selectorStrategy.equalsIgnoreCase("byText"))
            return driver.$$(Selectors.byText(selectorValue));
        else if(selectorStrategy.equalsIgnoreCase("withText"))
            return driver.$$(Selectors.withText(selectorValue));
        else if(selectorStrategy.equalsIgnoreCase("withTagAndText"))
            return driver.$$(Selectors.withTagAndText(selectorValue.split("=")[0], selectorValue.split("=")[1]));
        else if(selectorStrategy.equalsIgnoreCase("byTagAndText"))
            return driver.$$(Selectors.byTagAndText(selectorValue.split("=")[0], selectorValue.split("=")[1]));
        else if(selectorStrategy.equalsIgnoreCase("byAttribute"))
            return driver.$$(Selectors.byAttribute(selectorValue.split("=")[0], selectorValue.split("=")[1]));
        else
            return null;


    }

    private SelenideElement objElement(String properties){

        if (properties.toLowerCase().startsWith("obj")) {
           properties = testData.getObjRepoData(properties);
        }
        String selectorStrategy = properties.split("-")[0];

        String selectorValue = properties.substring(selectorStrategy.length()+1);

                if(selectorStrategy.equalsIgnoreCase("xpath"))
                    return driver.$x(selectorValue);
                else if(selectorStrategy.equalsIgnoreCase("name"))
                    return driver.$(By.name(selectorValue));
                else  if(selectorStrategy.equalsIgnoreCase("className"))
                    return driver.$(By.className(selectorValue));
                else  if(selectorStrategy.equalsIgnoreCase("cssSelector") || selectorStrategy.equalsIgnoreCase("css"))
                    return driver.$(selectorValue);
                else  if(selectorStrategy.equalsIgnoreCase("linkText"))
                    return driver.$(By.linkText(selectorValue));
                else  if(selectorStrategy.equalsIgnoreCase("partialLinkText"))
                    return driver.$(By.partialLinkText(selectorValue));
                else  if(selectorStrategy.equalsIgnoreCase("id"))
                    return driver.$(By.id(selectorValue));
                else  if(selectorStrategy.equalsIgnoreCase("tagName"))
                    return driver.$(By.tagName(selectorValue));
                else if(selectorStrategy.equalsIgnoreCase("byText"))
                    return driver.$(Selectors.byText(selectorValue));
                else if(selectorStrategy.equalsIgnoreCase("withText"))
                    return driver.$(Selectors.withText(selectorValue));
                else if(selectorStrategy.equalsIgnoreCase("withTagAndText"))
                     return driver.$(Selectors.withTagAndText(selectorValue.split("=")[0], selectorValue.split("=")[1]));
                else if(selectorStrategy.equalsIgnoreCase("byTagAndText"))
                     return driver.$(Selectors.byTagAndText(selectorValue.split("=")[0], selectorValue.split("=")[1]));
                else if(selectorStrategy.equalsIgnoreCase("byAttribute"))
                     return driver.$(Selectors.byAttribute(selectorValue.split("=")[0], selectorValue.split("=")[1]));
                else
                    return null;


    }

    private By By(String properties){

        if (properties.toLowerCase().startsWith("obj")) {
            properties = testData.getObjRepoData(properties);
        }

        String selectorStrategy = properties.split("=")[0];

        String selectorValue = properties.substring(selectorStrategy.length()+1);

        if(selectorStrategy.equalsIgnoreCase("xpath"))
            return By.xpath(selectorValue);
        else if(selectorStrategy.equalsIgnoreCase("name"))
            return By.name(selectorValue);
        else  if(selectorStrategy.equalsIgnoreCase("className"))
            return By.className(selectorValue);
        else  if(selectorStrategy.equalsIgnoreCase("cssSelector") || selectorStrategy.equalsIgnoreCase("css"))
            return By.cssSelector(selectorValue);
        else  if(selectorStrategy.equalsIgnoreCase("linkText"))
            return By.linkText(selectorValue);
        else  if(selectorStrategy.equalsIgnoreCase("partialLinkText"))
            return By.partialLinkText(selectorValue);
        else  if(selectorStrategy.equalsIgnoreCase("id"))
            return By.id(selectorValue);
        else  if(selectorStrategy.equalsIgnoreCase("tagName"))
            return By.tagName(selectorValue);
        else
            return null;


    }

    public void waitForPageLoad(){
        if(seleniumIDE.getValue().startsWith("#")){
            String parameter = testData.getData(seleniumIDE.getValue().substring(1));
            seleniumIDE.setValue(parameter);
        }
        userActions.waitForJStoLoad(Integer.parseInt(seleniumIDE.getValue()));
    }

    public void hardWait() throws InterruptedException {
        if(seleniumIDE.getValue().startsWith("#")){
            String parameter = testData.getData(seleniumIDE.getValue().substring(1));
            seleniumIDE.setValue(parameter);
        }

        Thread.sleep(Integer.parseInt(seleniumIDE.getValue())* 1000L);

    }
}