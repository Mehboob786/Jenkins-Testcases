package com.qa.doingerp.parameters;

import com.qa.doingerp.exception.UnExpectedException;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class XcelAccess
{
  private String filePath;
  private String fileName;
  private String datasheetName;

  public String getDatasheetName()
  {
    return this.datasheetName;
  }

  public void setDatasheetName(String datasheetName)
  {
    this.datasheetName = datasheetName;
  }

  public XcelAccess(String filePath, String fileName)
  {
    this.filePath = filePath;
    this.fileName = fileName;
  }

  private void checkPreRequisites()
  {
    if (this.datasheetName == null)
      throw new UnExpectedException("XcelAccess.datasheetName is not set!");
  }

  private XSSFWorkbook openFileForReading()
  {
    FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();
    String absoluteFilePath = this.filePath + frameworkParameters.fileSeparator + this.fileName + ".xlsx";
    XSSFWorkbook workbook;
    FileInputStream fileInputStream;
    
    try
    {
      fileInputStream = new FileInputStream(absoluteFilePath);
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
      throw new UnExpectedException("The specified file \"" + absoluteFilePath + "\" does not exist!");
    }
    try
    {
      workbook = new XSSFWorkbook(fileInputStream);
    }
    catch (IOException e)
    {
      //XSSFWorkbook workbook;
      e.printStackTrace();
      throw new UnExpectedException("Error while opening the specified Excel workbook \"" + absoluteFilePath + "\"");
    }
   // XSSFWorkbook workbook;
    return workbook;
  }

  private void writeIntoFile(XSSFWorkbook workbook)
  {
    FrameworkParameters frameworkParameters = FrameworkParameters.getInstance();
    String absoluteFilePath = this.filePath + frameworkParameters.fileSeparator + this.fileName + ".xlsx";
    FileOutputStream fileOutputStream ;
    try
    {
      fileOutputStream = new FileOutputStream(absoluteFilePath);
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
      throw new UnExpectedException("The specified file \"" + absoluteFilePath + "\" does not exist!");
    }
    try
    {
      workbook.write(fileOutputStream);
      fileOutputStream.close();
    } catch (IOException e) {
      e.printStackTrace();
      throw new UnExpectedException("Error while writing into the specified Excel workbook \"" + absoluteFilePath + "\"");
    }
  }

  private XSSFSheet getWorkSheet(XSSFWorkbook workbook)
  {
    XSSFSheet worksheet = workbook.getSheet(this.datasheetName);
    if (worksheet == null) {
      throw new UnExpectedException("The specified sheet \"" + this.datasheetName + "\" does not exist within the workbook \"" + this.fileName + ".xlsx\"");
    }

    return worksheet;
  }

  private String getCellValue(XSSFSheet worksheet, int rowNum, int columnNum)
  {
    XSSFRow row = worksheet.getRow(rowNum);
    if(row == null)
      return null;

    XSSFCell cell = row.getCell(columnNum);
    String cellValue;
    if (cell == null)
      cellValue = "";
    else {
      cellValue = cell.getStringCellValue().trim();
    }
    return cellValue;
  }

  private String getCellValue(XSSFSheet worksheet, XSSFRow row, int columnNum)
  {
    XSSFCell cell = row.getCell(columnNum);
    String cellValue;
    if (cell == null)
      cellValue = "";
    else {
      cellValue = cell.getStringCellValue().trim();
    }
    return cellValue;
  }

  public int getRowNum(String key, int columnNum, int startRowNum)
  {
    checkPreRequisites();

    XSSFWorkbook workbook = openFileForReading();
    XSSFSheet worksheet = getWorkSheet(workbook);

    for (int currentRowNum = startRowNum; 
      currentRowNum <= worksheet.getLastRowNum(); currentRowNum++)
    {
      String currentValue = getCellValue(worksheet, currentRowNum, columnNum);

      if (currentValue.equals(key)) {
        return currentRowNum;
      }
    }

    return -1;
  }

  public int getRowNum(String key, int columnNum)
  {
    return getRowNum(key, columnNum, 0);
  }

  public int getLastRowNum()
  {
    checkPreRequisites();

    XSSFWorkbook workbook = openFileForReading();
    XSSFSheet worksheet = getWorkSheet(workbook);

    return worksheet.getLastRowNum();
  }


  public int getRowCount(String key, int columnNum, int startRowNum)
  {
    checkPreRequisites();

    XSSFWorkbook workbook = openFileForReading();
    XSSFSheet worksheet = getWorkSheet(workbook);

    int rowCount = 0;
    Boolean keyFound = Boolean.valueOf(false);

    for (int currentRowNum = startRowNum; 
      currentRowNum <= worksheet.getLastRowNum(); currentRowNum++)
    {
      String currentValue = getCellValue(worksheet, currentRowNum, columnNum);

      if (currentValue.equals(key)) {
        rowCount++;
        keyFound = Boolean.valueOf(true);
      } else {
        if (keyFound.booleanValue())
        {
          break;
        }
      }
    }
    return rowCount;
  }

  public int getRowCount(String key, int columnNum)
  {
    return getRowCount(key, columnNum, 0);
  }

  public int getColumnNum(String key, int rowNum)
  {
    checkPreRequisites();

    XSSFWorkbook workbook = openFileForReading();
    XSSFSheet worksheet = getWorkSheet(workbook);

    XSSFRow row = worksheet.getRow(rowNum);

    for (int currentColumnNum = 0; 
      currentColumnNum < row.getLastCellNum(); currentColumnNum++)
    {
      String currentValue = getCellValue(worksheet, row, currentColumnNum);

      if (currentValue.equals(key)) {
        return currentColumnNum;
      }
    }

    return -1;
  }

  public String getValue(int rowNum, int columnNum)
  {
    checkPreRequisites();

    XSSFWorkbook workbook = openFileForReading();
    XSSFSheet worksheet = getWorkSheet(workbook);

    String cellValue = getCellValue(worksheet, rowNum, columnNum);

    return cellValue;
  }

  public String getValue(int rowNum, String columnHeader)
  {
    checkPreRequisites();

    XSSFWorkbook workbook = openFileForReading();
    XSSFSheet worksheet = getWorkSheet(workbook);

    XSSFRow row = worksheet.getRow(0);
    int columnNum = -1;

    for (int currentColumnNum = 0; 
      currentColumnNum < row.getLastCellNum(); currentColumnNum++)
    {
      String currentValue = getCellValue(worksheet, row, currentColumnNum);

      if (currentValue.equals(columnHeader)) {
        columnNum = currentColumnNum;
        break;
      }
    }

    if (columnNum == -1) {
      throw new UnExpectedException("The specified column header \"" + columnHeader + "\" is not found in the sheet \"" + this.datasheetName + "\"!");
    }
    String cellValue = getCellValue(worksheet, rowNum, columnNum);
    return cellValue;
  }

  public void setValue(int rowNum, int columnNum, String value)
  {
    checkPreRequisites();

    XSSFWorkbook workbook = openFileForReading();
    XSSFSheet worksheet = getWorkSheet(workbook);

    XSSFRow row = worksheet.getRow(rowNum);
    XSSFCell cell = row.createCell(columnNum);
    cell.setCellType(CellType.NUMERIC);
    cell.setCellValue(value);

    writeIntoFile(workbook);
  }

  public void setValue(int rowNum, String columnHeader, String value)
  {
    checkPreRequisites();

    XSSFWorkbook workbook = openFileForReading();
    XSSFSheet worksheet = getWorkSheet(workbook);

    XSSFRow row = worksheet.getRow(0);
    int columnNum = -1;

    for (int currentColumnNum = 0; 
      currentColumnNum < row.getLastCellNum(); currentColumnNum++)
    {
      String currentValue = getCellValue(worksheet, row, currentColumnNum);

      if (currentValue.equals(columnHeader)) {
        columnNum = currentColumnNum;
        break;
      }
    }

    if (columnNum == -1) {
      throw new UnExpectedException("The specified column header " + columnHeader + " is not found in the sheet \"" + this.datasheetName + "\"!");
    }
    row = worksheet.getRow(rowNum);
    XSSFCell cell = row.createCell(columnNum);
    cell.setCellType(CellType.NUMERIC);
    cell.setCellValue(value);

    writeIntoFile(workbook);
  }

  public void createWorkbook()
  {
    XSSFWorkbook workbook = new XSSFWorkbook();

    writeIntoFile(workbook);
  }

  public void addSheet(String sheetName)
  {
    XSSFWorkbook workbook = new XSSFWorkbook();
    XSSFSheet worksheet = workbook.createSheet(sheetName);
    worksheet.createRow(0);

    writeIntoFile(workbook);

    setDatasheetName(sheetName);
  }

  public int addRow()
  {
    checkPreRequisites();

    XSSFWorkbook workbook = openFileForReading();
    XSSFSheet worksheet = getWorkSheet(workbook);

    int newRowNum = worksheet.getLastRowNum() + 1;
    worksheet.createRow(newRowNum);

    writeIntoFile(workbook);

    return newRowNum;
  }

  public void addColumn(String columnHeader)
  {
    checkPreRequisites();

    XSSFWorkbook workbook = openFileForReading();
    XSSFSheet worksheet = getWorkSheet(workbook);

    XSSFRow row = worksheet.getRow(0);
    int lastCellNum = row.getLastCellNum();
    if (lastCellNum == -1) {
      lastCellNum = 0;
    }
    XSSFCell cell = row.createCell(lastCellNum);
    cell.setCellType(CellType.NUMERIC);
    cell.setCellValue(columnHeader);

    writeIntoFile(workbook);
  }

  public HashMap<String, Integer> getCurrentRowAndColIndex( String scenarioName){
    checkPreRequisites();
    XSSFWorkbook workbook = openFileForReading();
    XSSFSheet worksheet = getWorkSheet(workbook);
    boolean quitRow = false;

    HashMap<String,Integer>  hashValue = new HashMap<>();

    for (int currentRowNum = 0;
         currentRowNum <= worksheet.getLastRowNum(); currentRowNum++)
    {
     XSSFRow row = worksheet.getRow(currentRowNum);
      if(row!=null && row.getLastCellNum()>0) {
        for (int currentColNum = 0; currentColNum <= row.getLastCellNum(); currentColNum++) {
          String currentValue = getCellValue(worksheet, currentRowNum, currentColNum);
                  if (currentValue != null && currentValue.equalsIgnoreCase(scenarioName)) {
            quitRow = true;
            hashValue.put("columnName", currentColNum);

            break;
          }

        }
      }
      if(quitRow) {
        hashValue.put("rowName", currentRowNum);
        break;
      }

    }

    return hashValue;
  }
}