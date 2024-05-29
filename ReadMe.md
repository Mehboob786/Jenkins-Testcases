### Doingerp Low Code Test Automation framework

This is the test automation framework to automate the web application using selenium-Webdriver Java.
And also this is a framework to facilitate or support the tool(It's not an ideal replacement of tool).


#### Introduction

The framework is designed such a way that the functional test consultant can also able to automate with minimum
techincal knowledge. This core concept of framework derived from Selenium IDE features(`target` and `comamnd` and `value`).
And this is completely the `object` driven approach and features like report, parallel execution, object repository and test data management
are implemented.

#### Setup and Installation(for `WINDOWS` only)
1. Microsoft Excel App 
2. Maven
3. Java

    ##### Excel App
    By default the Excel app would be available in all Windows platform and make sure the file format is `*.xlsx`(not a `.xls`)
    
    ##### Maven
    Maven is a project development management and comprehension tool. Based on the concept of a project object model: builds, dependency management, documentation creation, site publication, and distribution publication
    are all controlled from the`pom.xml` declarative file. Maven can be extended by plugins and this helps us to act as a bridge between
    any `CI` and test automation framework
    
    Refer below links to set up the maven in the local machine: https://www.mkyong.com/maven/how-to-install-maven-in-windows/
    
    ##### JAVA
    This Java Development Kit(JDK) allows you to code and run Java programs. It's possible that you install multiple JDK versions 
    on the same PC. But Its recommended that you install only latest version.
    
    Refer below link for Java setup in local machine: https://www.guru99.com/install-java.html
        
After the installation make sure all are properly installed by running the below command in command prompt[Open Run =>`cmd`]
```aidl
1. mvn -version
2. java -version
```

### Scenario work book
This is the controller of the framework and contains the list of scenarios which we want to execute

Column Name| Description 
----------- | ------------
Test_Scenario | This is name of the workbook which contains the original code implementation
Test_Case | Name of the test case(Don't use any special characters)
Description | Description of the test case
Execute | `Yes` or `No` option to enable or disable the test case execution
Browser | Browser to select from dropdown
Platform | By default it always in `WINDOWS`


### Business flow workbook
The name of the workbook should be the name of the `Test_Scenario` column and this work book consist of test steps implementation details
![Business flow](https://github.com/Jurtz/myhu/blob/master/src/main/resources/documentation/Businessflow.png)

Sheet Name | Description
------------ | ------------
Business_Flow | In this sheet all scenarios have thier own step implementation
Test_Data | Commanly used test data across all scenarios in the workbook, if any cell in the `Value` column encountered with `#` the same will be replaced with value from `Test_Data` sheet
objectRepo  | All objects should be stored in this work book and same should be referred across all scenarios in the `Business_Flow` sheet

#### Test Scenarios
All steps in the test scenarios have these mandatory steps

Column Name | Description 
------------- | ----------
Description | Step name and the same will be refected in test report
Command | Specific User Actions
Target | Object properties or reference to the object properties
Value | This have references or values, if steps needs any input for the scenario

#### Test Data
Test data which are used across the test scenarios, it always good to keep track all test data's against each scenario

#### Object Repo
The object repo consist of all object properties related to each page and this consist of four columns

Column Name | Description
------------ | -----------
PageName | Name of the page in which object refers
ObjName | The name should be unique
ObjIdentifier | Select the object identifier from the dropdown
ObjValue | Corresponding identifier value of the object



#### Test Script execution
Keep the `Execute` column `Yes` in the Scenario workbook and click the 'Run.bat' file

#### Result
At the end of test execution, the HTML results popup in any default browser of local machine

## Path to files
Test scenario manager -----> ``src/test/resources/scenarios``
Teststeps --------> `src/test/resources/scenarios`
Globalvariable -------> `src/test/resources/config/test`


For any queries or enchancement, please reach out to DoingErp Consultant team




