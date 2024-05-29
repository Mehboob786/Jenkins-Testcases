package com.qa.doingerp.reports;

import com.qa.doingerp.exception.UnExpectedException;
import com.qa.doingerp.parameters.FrameworkParameters;
import com.qa.doingerp.utilities.Settings;
import com.qa.doingerp.utilities.Util;

import java.io.*;
import java.util.Properties;

public class HtmlReport
  implements ReportType
{
  private String testLogPath;
  private String resultSummaryPath;
  private static String headingColor;
  private static String subHeadingColor;
  private static String settingColor;
  private static String bodyColor;
  private Properties properties = Settings.getInstance();

  public HtmlReport(String reportPath, String reportName)
  {
    FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();

    this.testLogPath = 
      (reportPath + frameworkParameters.fileSeparator + "HTML Results" + 
      frameworkParameters.fileSeparator + reportName + ".html");

    this.resultSummaryPath = 
      (reportPath + frameworkParameters.fileSeparator + "HTML Results" + 
      frameworkParameters.fileSeparator + "Summary" + ".html");

    setReportsTheme();
  }

  private void setReportsTheme()
  {
    Theme reportTheme = Theme.valueOf(this.properties.getProperty("ReportsTheme"));

    switch (reportTheme) {
    case AUTUMN:
      headingColor = "#7E5D56";
      subHeadingColor = "#A6A390";
      settingColor = "#EDE9CE";
      bodyColor = "#F6F3E4";
      break;
    case MYSTIC:
      headingColor = "#687C7D";
      subHeadingColor = "#8B9292";
      settingColor = "#C6D0D1";
      bodyColor = "#EDEEF0";
      break;
    case REBEL:
      headingColor = "#979797";
      subHeadingColor = "#B2B27A";
      settingColor = "#fff";
      bodyColor = "#fff";
      break;
      case CLASSIC:
      headingColor = "#686145";
      subHeadingColor = "#A6A390";
      settingColor = "#EDE9CE";
      bodyColor = "#E8DEBA";
      break;
    case SERENE:
      headingColor = "#953735";
      subHeadingColor = "#747474";
      settingColor = "#A6A6A6";
      bodyColor = "#D9D9D9";
      break;
    case OLIVE:
      headingColor = "#CE824E";
      subHeadingColor = "#AA9B7C";
      settingColor = "#F3DEB1";
      bodyColor = "#F8F1E7";
      break;
    case RETRO:
      headingColor = "#7B597A";
      subHeadingColor = "#799DB2";
      settingColor = "#ADE0FF";
      bodyColor = "#C5AFC6";
      break;
    case HD:
      headingColor = "#000000";
      subHeadingColor ="#A52A2A";
      settingColor = "#D2691E";
      bodyColor = "#F0FFFF";
    default:
      headingColor = "#B3D9FF";
      subHeadingColor = "#8F98B2";
      settingColor = "#CCD9FF";
      bodyColor = "#8A4117";
    }
  }

  public void createTestLogHeader(String reportName, String iterationMode, int startIteration, int endIteration)
  {
    File testLogFile = new File(this.testLogPath);
    FileOutputStream outputStream;
    try {
      testLogFile.createNewFile();
    } catch (IOException e) {
      e.printStackTrace();
      throw new UnExpectedException("Error while creating HTML test log file");
    }

    try
    {
      outputStream = new FileOutputStream(testLogFile);
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
      throw new UnExpectedException("Cannot find HTML test log file");
    }
    PrintStream printStream = new PrintStream(outputStream);

    String projectName = this.properties.getProperty("ProjectName");
    iterationMode = iterationMode.replaceAll("<", "<i>");
    iterationMode = iterationMode.replaceAll("Iteration>", "Iteration</i>");

    String testLogHeader = "<html> \n\t <head> \n\t\t <title>" + 
      projectName + " - " + reportName + " Automation Execution Results" + 
      "</title> \n" + 
      "\t </head> \n\n" + 
      "\t <body> \n" + 
      "\t\t <p align='center'> \n" + 
      "\t\t\t <table border='2' bordercolor='#000000' bordercolorlight='#000000' cellspacing='0' id='table1' width='1000' height='100'> \n" + 
      "\t\t\t\t <tr bgcolor='" + headingColor + "'> \n" + 
      "\t\t\t\t\t <td colspan='5'> \n" + 
      "\t\t\t\t\t\t <p align='center'><font color='" + bodyColor + "' size='4' face='Copperplate Gothic Bold'>" + 
      projectName + " - " + reportName + " Automation Execution Results" + 
      "</font></p> \n" + 
      "\t\t\t\t\t </td> \n" + 
      "\t\t\t\t </tr>\n" + 
      "\t\t\t\t <tr bgcolor='" + settingColor + "'> \n" + 
      "\t\t\t\t\t <td colspan='3'> \n" + 
      "\t\t\t\t\t\t <p align='justify'><b><font color='" + headingColor + "' size='2' face='Verdana'>" + 
      "&nbsp;Date & Time: " + Util.getCurrentFormattedTime() +
      "</font></b></p> \n" + 
      "\t\t\t\t\t </td> \n" + 
      "\t\t\t\t\t <td colspan='2'> \n" + 
      "\t\t\t\t\t\t <p align='justify'><b><font color='" + headingColor + "' size='2' face='Verdana'>" + 
      "&nbsp;Iteration Mode: " + iterationMode + 
      "</font></b></p> \n" + 
      "\t\t\t\t\t </td> \n" + 
      "\t\t\t\t </tr> \n" + 
      "\t\t\t\t <tr bgcolor='" + settingColor + "'> \n" + 
      "\t\t\t\t\t <td colspan='3'> \n" + 
      "\t\t\t\t\t\t <p align='justify'><b><font color='" + headingColor + "' size='2' face='Verdana'>" + 
      "&nbsp;Start Iteration: " + startIteration + 
      "</font></b></p> \n" + 
      "\t\t\t\t\t </td> \n" + 
      "\t\t\t\t\t <td colspan='2'> \n" + 
      "\t\t\t\t\t\t <p align='justify'><b><font color='" + headingColor + "' size='2' face='Verdana'>" + 
      "&nbsp;End Iteration: " + endIteration + 
      "</font></b></p> \n" + 
      "\t\t\t\t\t </td> \n" + 
      "\t\t\t\t </tr> \n" + 
      "\t\t\t\t <tr bgcolor='" + headingColor + "'> \n" + 
      "\t\t\t\t\t <td><b><center><font color='" + bodyColor + "' size='2' face='Verdana'>Step_No</font></center></b></td> \n" + 
      "\t\t\t\t\t <td><b><center><font color='" + bodyColor + "' size='2' face='Verdana'>Step_Name</font></center></b></td> \n" + 
      "\t\t\t\t\t <td><b><center><font color='" + bodyColor + "' size='2' face='Verdana'>Description</font></center></b></td> \n" + 
      "\t\t\t\t\t <td><b><center><font color='" + bodyColor + "' size='2' face='Verdana'>Status</font></center></b></td> \n" + 
      "\t\t\t\t\t <td><b><center><font color='" + bodyColor + "' size='2' face='Verdana'>Step_Time</font></center></b></td> \n" + 
      "\t\t\t\t </tr> \n";

    printStream.println(testLogHeader);
    printStream.close();
  }

  public void createResultSummaryHeader()
  {
    File resultSummaryFile = new File(this.resultSummaryPath);
    try
    {
      resultSummaryFile.createNewFile();
    } catch (IOException e) {
      e.printStackTrace();
      throw new UnExpectedException("Error while creating HTML result summary file");
    }
    FileOutputStream outputStream;
    try
    {
       outputStream = new FileOutputStream(resultSummaryFile);
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
      throw new UnExpectedException("Cannot find HTML result summary file");
    }
    PrintStream printStream = new PrintStream(outputStream);

    String projectName = this.properties.getProperty("ProjectName");

    String resultSummaryHeader = "<html> \n\t <head> \n\t\t <title>" + 
      projectName + " - Automation Execution Results Summary</title> \n" + 
      "\t </head> \n\n" + 
      "\t <body> \n" + 
      "\t\t <p align='center'> \n" + 
      "\t\t\t <table border='2' bordercolor='#000000' bordercolorlight='#000000' cellspacing='0' id='table1' width='900' height='31' bordercolorlight='#000000'> \n" + 
      "\t\t\t\t <tr bgcolor='" + headingColor + "'> \n" + 
      "\t\t\t\t\t <td colspan='5'> \n" + 
      "\t\t\t\t\t\t <p align='center'><font color='" + bodyColor + "' size='4' face='Copperplate Gothic Bold'>" + 
      projectName + " - Automation Execution Results Summary" + 
      "</font></p> \n" + 
      "\t\t\t\t\t </td> \n" + 
      "\t\t\t\t </tr> \n" + 
      "\t\t\t\t <tr bgcolor='" + settingColor + "'> \n" + 
      "\t\t\t\t\t <td colspan='3'> \n" + 
      "\t\t\t\t\t\t <p align='justify'><b><font color='" + headingColor + "' size='2' face='Verdana'>" + 
      "&nbsp;Date & Time: " + Util.getCurrentFormattedTime() + 
      "</font></b></p> \n" + 
      "\t\t\t\t\t </td> \n" + 
      "\t\t\t\t\t <td colspan='2'> \n" + 
      "\t\t\t\t\t\t <p align='justify'><b><font color='" + headingColor + "' size='2' face='Verdana'>" + 
      "&nbsp;OnError: " + this.properties.getProperty("OnError") + 
      "</font></b></p> \n" + 
      "\t\t\t\t\t </td> \n" + 
      "\t\t\t\t </tr> \n" + 
      "\t\t\t\t <tr bgcolor='" + headingColor + "'> \n" + 
      "\t\t\t\t\t <td><b><center><font color='" + bodyColor + "' size='2' face='Verdana'>Test_Scenario</font></center></b></td> \n" + 
      "\t\t\t\t\t <td><b><center><font color='" + bodyColor + "' size='2' face='Verdana'>Test_Case</font></center></b></td> \n" + 
      "\t\t\t\t\t <td><b><center><font color='" + bodyColor + "' size='2' face='Verdana'>Description</font></center></b></td> \n" + 
      "\t\t\t\t\t <td><b><center><font color='" + bodyColor + "' size='2' face='Verdana'>Execution_Time</font></center></b></td> \n" + 
      "\t\t\t\t\t <td><b><center><font color='" + bodyColor + "' size='2' face='Verdana'>Status</font></center></b></td> \n" + 
      "\t\t\t\t </tr> \n";

    printStream.println(resultSummaryHeader);
    printStream.close();
  }

  public void createIterationHeader(int currentIteration)
  {
    try
    {
      BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(this.testLogPath, true));

      String iterationHeader = "\t\t\t\t <tr bgcolor='" + subHeadingColor + "'> \n" + 
        "\t\t\t\t\t <td colspan='5'><center><b>" + 
        "Iteration: " + currentIteration + 
        "</b></center></td> \n" + 
        "\t\t\t\t </tr> \n";
      bufferedWriter.write(iterationHeader);
      bufferedWriter.close();
    } catch (IOException e) {
      e.printStackTrace();
      throw new UnExpectedException("Error while adding iteration header to HTML test log");
    }
  }

  public void createSectionHeader(String sectionName)
  {
    try
    {
      BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(this.testLogPath, true));

      String iterationHeader = "\t\t\t\t <tr bgcolor='" + settingColor + "'> \n" + 
        "\t\t\t\t\t <td colspan='5'><font color='" + headingColor + 
        "' size='2' face='Verdana'><b>&nbsp;" + sectionName + 
        "</b></font></td> \n" + 
        "\t\t\t\t </tr> \n";
      bufferedWriter.write(iterationHeader);
      bufferedWriter.close();
    } catch (IOException e) {
      e.printStackTrace();
      throw new UnExpectedException("Error while adding iteration header to HTML test log");
    }
  }

  public void updateTestLog(String stepNumber, String stepName, String stepDescription, Status stepStatus, String screenShotName)
  {
    try {
      BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(this.testLogPath, true));

      String testStepRow = "\t\t\t\t <tr bgcolor='" + bodyColor + "'> \n" + 
        "\t\t\t\t\t <td><center>" + stepNumber + "</center></td> \n" + 
        "\t\t\t\t\t <td>" + stepName + "</td> \n" + 
        "\t\t\t\t\t <td>" + stepDescription + "</td> \n";

      if (stepStatus.equals(Status.FAIL)) {
        Boolean takeScreenshotFailedStep = Boolean.valueOf(Boolean.parseBoolean(this.properties.getProperty("TakeScreenshotFailedStep")));
        if (takeScreenshotFailedStep.booleanValue()) {
          testStepRow = testStepRow + "\t\t\t\t\t <td><a href='..\\Screenshots\\" + 
            screenShotName + "'>" + 
            "<font color='red'><b><center>" + stepStatus + "</center></b></font>" + 
            "</a>" + 
            "</td> \n";
        }
        else {
          testStepRow = testStepRow + "\t\t\t\t\t <td><font color='red'><b><center>" + 
            stepStatus + "</center></b></font>" + 
            "</td> \n";
        }
      }
      else if (stepStatus.equals(Status.PASS)) {
        Boolean takeScreenshotPassedStep = Boolean.valueOf(Boolean.parseBoolean(this.properties.getProperty("TakeScreenshotPassedStep")));
        if (takeScreenshotPassedStep.booleanValue()) {
          testStepRow = testStepRow + "\t\t\t\t\t <td><a href='..\\Screenshots\\" + 
            screenShotName + "'>" + 
            "<font color='green'><b><center>" + stepStatus + "</center></b></font>" + 
            "</a>" + 
            "</td> \n";
        }
        else {
          testStepRow = testStepRow + "\t\t\t\t\t <td><font color='green'><b><center>" + 
            stepStatus + "</center></b></font>" + 
            "</td> \n";
        }
      }
      else if (stepStatus.equals(Status.SCREENSHOT))
      {
        testStepRow = testStepRow + "\t\t\t\t\t <td><a href='..\\Screenshots\\" + 
          screenShotName + "'>" + 
          "<font color='blue'><b><center>" + stepStatus + "</center></b></font>" + 
          "</a>" + 
          "</td> \n";
      }
      else {
        testStepRow = testStepRow + "\t\t\t\t\t <td><b><center>" + 
          stepStatus + "</center></b>" + 
          "</td> \n";
      }

      testStepRow = testStepRow + "\t\t\t\t\t <td><center>" + 
        Util.getCurrentFormattedTime() + "</center>" + 
        "</td> \n" + 
        "\t\t\t\t </tr> \n";

      bufferedWriter.write(testStepRow);
      bufferedWriter.close();
    } catch (IOException e) {
      e.printStackTrace();
      throw new UnExpectedException("Error while updating HTML test log");
    }
  }

  public void updateResultSummary(String scenarioName, String testcaseName, String testcaseDescription, String executionTime, String testStatus)
  {
    try
    {
      BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(this.resultSummaryPath, true));

      String testcaseRow = "\t\t\t\t <tr bgcolor='" + bodyColor + "'> \n" + 
        "\t\t\t\t\t <td>" + scenarioName + "</td> \n" + 
        "\t\t\t\t\t <td><a href='" + scenarioName + "_" + testcaseName + ".html' " + 
        "target='about_blank'>" + testcaseName + 
        "</a></td> \n" + 
        "\t\t\t\t\t <td>" + testcaseDescription + "</td> \n" + 
        "\t\t\t\t\t <td><center>" + executionTime + "</center></td> \n";

      if (testStatus.equalsIgnoreCase("passed")) {
        testcaseRow = testcaseRow + "\t\t\t\t\t <td><font color='green'><b><center>" + 
          testStatus + "</center></b></font>" + 
          "</td> \n" + 
          "\t\t\t\t </tr> \n";
      }
      else {
        testcaseRow = testcaseRow + "\t\t\t\t\t <td><font color='red'><b><center>" + 
          testStatus + "</center></b></font>" + 
          "</td> \n" + 
          "\t\t\t\t </tr> \n";
      }

      bufferedWriter.write(testcaseRow);
      bufferedWriter.close();
    } catch (IOException e) {
      e.printStackTrace();
      throw new UnExpectedException("Error while updating HTML result summary");
    }
  }

  public void createTestLogFooter(String executionTime, int nStepsPassed, int nStepsFailed)
  {
    try
    {
      BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(this.testLogPath, true));

      String testLogFooter = "\t\t\t\t <tr bgcolor='" + settingColor + "'> \n" + 
        "\t\t\t\t\t <td colspan='5'> \n" + 
        "\t\t\t\t\t\t <center><b><font color='" + headingColor + "' size='2' face='Verdana'>" + 
        "Execution Duration: " + executionTime + "</font></b></center> \n" + 
        "\t\t\t\t\t </td> \n" + 
        "\t\t\t\t </tr> \n" + 
        "\t\t\t\t <tr bgcolor='" + settingColor + "'> \n" + 
        "\t\t\t\t\t <td colspan='3'> \n" + 
        "\t\t\t\t\t\t <b><font color='green'>&nbsp;Steps passed: " + nStepsPassed + "</font></b> \n" + 
        "\t\t\t\t\t </td> \n" + 
        "\t\t\t\t\t <td colspan=2> \n" + 
        "\t\t\t\t\t\t <b><font color='red'>&nbsp;Steps failed: " + nStepsFailed + "</font></b> \n" + 
        "\t\t\t\t\t </td> \n" + 
        "\t\t\t\t </tr> \n" + 
        "\t\t\t </table> \n" + 
        "\t\t </p> \n" + 
        "\t </body> \n" + 
        "</html>";

      bufferedWriter.write(testLogFooter);
      bufferedWriter.close();
    } catch (IOException e) {
      e.printStackTrace();
      throw new UnExpectedException("Error while adding footer to HTML test log");
    }
  }

  public void createResultSummaryFooter(String totalExecutionTime, int nTestsPassed, int nTestsFailed)
  {
    try
    {
      BufferedWriter bufferWriter = new BufferedWriter(new FileWriter(this.resultSummaryPath, true));
      String resultSummaryFooter = "\t\t\t\t <tr bgcolor='" + settingColor + "'> \n" + 
        "\t\t\t\t\t <td colspan='5'> \n" + 
        "\t\t\t\t\t\t <center><b><font color='" + headingColor + "' size='2' face='Verdana'>" + 
        "Total Duration: " + totalExecutionTime + "</font></b></center> \n" + 
        "\t\t\t\t\t </td> \n" + 
        "\t\t\t\t </tr> \n" + 
        "\t\t\t\t <tr bgcolor='" + settingColor + "'><center><b> \n" + 
        "\t\t\t\t\t <td colspan='3'> \n" + 
        "\t\t\t\t\t\t <font color='green'><b>&nbsp;Tests passed: " + nTestsPassed + "</b></font> \n" + 
        "\t\t\t\t\t </td> \n" + 
        "\t\t\t\t\t <td colspan='2'> \n" + 
        "\t\t\t\t\t\t <font color='red'><b>&nbsp;Tests failed: " + nTestsFailed + "</b></font> \n" + 
        "\t\t\t\t\t </td> \n" + 
        "\t\t\t\t </b></center></tr> \n" + 
        "\t\t\t </table> \n" + 
        "\t\t </p> \n" + 
        "\t </body> \n" + 
        "</html>";

      bufferWriter.write(resultSummaryFooter);
      bufferWriter.close();
    } catch (IOException e) {
      e.printStackTrace();
      throw new UnExpectedException("Error while adding footer to HTML result summary");
    }
  }

  private static enum Theme
  {
    AUTUMN, OLIVE, CLASSIC, RETRO, MYSTIC, SERENE, REBEL, HD;
  }
}