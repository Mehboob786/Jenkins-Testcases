package com.qa.doingerp.parameters;

import com.qa.doingerp.exception.UnExpectedException;
import com.qa.doingerp.utilities.Settings;

import java.util.Properties;

public class TestData
{
  private String datatablePath;
  private String datatableName;
  private String currentTestcase;
  private int currentIteration = 0; private int currentSubIteration = 0;

  private Properties properties = Settings.getInstance();

  public TestData(String datatablePath, String datatableName)
  {
    this.datatablePath = datatablePath;
    this.datatableName = datatableName;
  }

  public void setCurrentRow(String currentTestcase, int currentIteration, int currentSubIteration)
  {
    this.currentTestcase = currentTestcase;
    this.currentIteration = currentIteration;
    this.currentSubIteration = currentSubIteration;
  }

  public int  getCurrentSubIteration(){
	  return currentSubIteration;
  }
  private void checkPreRequisites() {
    if (this.currentTestcase == null) {
      throw new UnExpectedException(
        "TestData.currentTestCase is not set!");
    }
    if (this.currentIteration == 0) {
      throw new UnExpectedException(
        "TestData.currentIteration is not set!");
    }
    if (this.currentSubIteration == 0)
      throw new UnExpectedException(
        "TestData.currentSubIteration is not set!");
  }

  public synchronized String getData(String datasheetName, String fieldName)
  {
    checkPreRequisites();

    XcelAccess testDataAccess = new XcelAccess(this.datatablePath,
      this.datatableName);
    testDataAccess.setDatasheetName(datasheetName);

    int rowNum = testDataAccess.getRowNum(this.currentTestcase, 0);
    if (rowNum == -1) {
      throw new UnExpectedException("The test case \"" + this.currentTestcase +
        "\" is not found in the test data sheet \"" + 
        datasheetName + "\"!");
    }
    rowNum = testDataAccess.getRowNum(Integer.toString(this.currentIteration),
      1, rowNum);
    if (rowNum == -1) {
      throw new UnExpectedException("The iteration number \"" +
        this.currentIteration + "\" of the test case \"" + 
        this.currentTestcase + 
        "\" is not found in the test data sheet \"" + 
        datasheetName + "\"!");
    }
    rowNum = testDataAccess.getRowNum(
      Integer.toString(this.currentSubIteration), 2, rowNum);
    if (rowNum == -1) {
      throw new UnExpectedException("The sub iteration number \"" +
        this.currentSubIteration + "\" under iteration number \"" + 
        this.currentIteration + "\" of the test case \"" + 
        this.currentTestcase + 
        "\" is not found in the test data sheet \"" + 
        datasheetName + "\"!");
    }

    String dataValue = testDataAccess.getValue(rowNum, fieldName);

    String dataReferenceIdentifier = this.properties
      .getProperty("DataReferenceIdentifier");
    if (dataValue.startsWith(dataReferenceIdentifier)) {
      dataValue = getCommonData(fieldName, dataValue);
    }

    return dataValue;
  }



  public synchronized String getObjRepoData(String fieldName){
    checkPreRequisites();
     XcelAccess testDataAccess = new XcelAccess(this.datatablePath,
            this.datatableName);
          testDataAccess.setDatasheetName("objectRepo");
          int rowNum = testDataAccess.getRowNum(fieldName, 1, 0);
         return testDataAccess.getValue(rowNum, 2)+"-"+testDataAccess.getValue(rowNum, 3);

  }
  
  
  
  public synchronized String getData(String fieldName)
  {
    checkPreRequisites();
    
    

    XcelAccess testDataAccess = new XcelAccess(this.datatablePath,
      this.datatableName);
    String datasheetName = this.properties.getProperty("DataSheet",null);
    if(datasheetName!=null && !datasheetName.equals(""))
    	testDataAccess.setDatasheetName(datasheetName.toLowerCase());
    else
        testDataAccess.setDatasheetName("Test_Data");


    int rowNum = testDataAccess.getRowNum(this.currentTestcase, 0);
    if (rowNum == -1) {
      throw new UnExpectedException("The test case \"" + this.currentTestcase +
        "\" is not found in the test data sheet \"" + 
        datasheetName + "\"!");
    }

    rowNum = testDataAccess.getRowNum(Integer.toString(this.currentIteration),
      1, rowNum);
   if (rowNum == -1) {
      throw new UnExpectedException("The iteration number \"" +
        this.currentIteration + "\" of the test case \"" + 
        this.currentTestcase + 
        "\" is not found in the test data sheet \"" + 
        datasheetName + "\"!");
    }
    rowNum = testDataAccess.getRowNum(
      Integer.toString(this.currentSubIteration), 2, rowNum);
    if (rowNum == -1) {
      throw new UnExpectedException("The sub iteration number \"" +
        this.currentSubIteration + "\" under iteration number \"" + 
        this.currentIteration + "\" of the test case \"" + 
        this.currentTestcase + 
        "\" is not found in the test data sheet \"" + 
        datasheetName + "\"!");
    }

    String dataValue = testDataAccess.getValue(rowNum, fieldName);

    String dataReferenceIdentifier = this.properties
      .getProperty("DataReferenceIdentifier");
    if (dataValue.startsWith(dataReferenceIdentifier)) {
      dataValue = getCommonData(fieldName, dataValue);
    }

    return dataValue;
  }

  private String getCommonData(String fieldName, String dataValue) {

    XcelAccess commonDataAccess = new XcelAccess(this.datatablePath,
      this.datatableName);

    String dataReferenceIdentifier = this.properties
      .getProperty("DataReferenceIdentifier");
    String dataReferenceId = dataValue.split(dataReferenceIdentifier)[1];

    if (dataReferenceId.equals(this.properties.getProperty("DataReferenceReservedCharacters", "Non reserved")))
    {
      dataReferenceId = this.properties.getProperty("Environment", "No Environment chosen in Global Properties");
    }
    //Here to select the environment specific sheet
    
    String datasheetName = this.properties.getProperty("commonTestData",null);
    if(datasheetName!=null)
    	commonDataAccess.setDatasheetName(datasheetName.toLowerCase());
    else{
    	System.err.println("Datasheet not set in global setting properties file by default it set as 'topshop'");
    }
        int rowNum = commonDataAccess.getRowNum(dataReferenceId, 0);
    if (rowNum == -1) {
      throw new UnExpectedException(
        "The common test data row identified by \"" + 
        dataReferenceId + 
        "\" is not found in the Common Testdata.xlsx file!");
    }

    dataValue = commonDataAccess.getValue(rowNum, fieldName);

    return dataValue;
  }

  public synchronized void putData(String datasheetName, String fieldName, String dataValue)
  {
    checkPreRequisites();

    XcelAccess testDataAccess = new XcelAccess(this.datatablePath,
      this.datatableName);
    testDataAccess.setDatasheetName(datasheetName);

    int rowNum = testDataAccess.getRowNum(this.currentTestcase, 0);
    if (rowNum == -1) {
      throw new UnExpectedException("The test case \"" + this.currentTestcase +
        "\" is not found in the test data sheet \"" + 
        datasheetName + "\"!");
    }
    rowNum = testDataAccess.getRowNum(Integer.toString(this.currentIteration), 
      1, rowNum);
    if (rowNum == -1) {
      throw new UnExpectedException("The iteration number \"" +
        this.currentIteration + "\" of the test case \"" + 
        this.currentTestcase + 
        "\" is not found in the test data sheet \"" + 
        datasheetName + "\"!");
    }
    rowNum = testDataAccess.getRowNum(
      Integer.toString(this.currentSubIteration), 2, rowNum);
    if (rowNum == -1) {
      throw new UnExpectedException("The sub iteration number \"" +
        this.currentSubIteration + "\" under iteration number \"" + 
        this.currentIteration + "\" of the test case \"" + 
        this.currentTestcase + 
        "\" is not found in the test data sheet \"" + 
        datasheetName + "\"!");
    }

    testDataAccess.setValue(rowNum, fieldName, dataValue);
  }

  public synchronized String getExpectedResult(String fieldName)
  {
    checkPreRequisites();

    XcelAccess expectedResultsAccess = new XcelAccess(
      this.datatablePath, this.datatableName);
    expectedResultsAccess.setDatasheetName("Parametrized_Checkpoints");

    int rowNum = expectedResultsAccess.getRowNum(this.currentTestcase, 0);
    if (rowNum == -1) {
      throw new UnExpectedException("The test case \"" + this.currentTestcase +
        "\" is not found in the parametrized checkpoints sheet!");
    }
    rowNum = expectedResultsAccess.getRowNum(
      Integer.toString(this.currentIteration), 1, rowNum);
    if (rowNum == -1) {
      throw new UnExpectedException("The iteration number \"" +
        this.currentIteration + "\" of the test case \"" + 
        this.currentTestcase + 
        "\" is not found in the parametrized checkpoints sheet!");
    }
    rowNum = expectedResultsAccess.getRowNum(
      Integer.toString(this.currentSubIteration), 2, rowNum);
    if (rowNum == -1) {
      throw new UnExpectedException("The sub iteration number \"" +
        this.currentSubIteration + "\" under iteration number \"" + 
        this.currentIteration + "\" of the test case \"" + 
        this.currentTestcase + 
        "\" is not found in the parametrized checkpoints sheet!");
    }

    String dataValue = expectedResultsAccess.getValue(rowNum, fieldName);

    return dataValue;
  }
}