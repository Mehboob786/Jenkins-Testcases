package com.qa.doingerp.reports;

import java.io.*;

public class DetailedSummary {

       static String strDetailedReportPath = null;

       public static void generateDetailedSummary(String resultPath, String filesep)
                     throws IOException {
              String strHTMLResultsDirectory = resultPath + filesep + "HTML Results";
              System.out.println(resultPath + filesep + "DetailedSummary.html");
              File detailedSummary = new File(strHTMLResultsDirectory + filesep
                           + "DetailedSummary.html");

              File htmlResultsDir = new File(strHTMLResultsDirectory);
              File[] htmlFiles = htmlResultsDir.listFiles();

              String strOutput = "";

              BufferedWriter writer = new BufferedWriter(new FileWriter(
                           detailedSummary, true));

              for (File f : htmlFiles) {
                     if (!f.getName().contains("Summary")) {
                           BufferedReader reader = new BufferedReader(new FileReader(f));
                           String thisLine;
                           System.out.println("Adding " + f.getName());
                           
                           while ((thisLine = reader.readLine()) != null)
                                  strOutput = strOutput + thisLine;

                           reader.close();

                           writer.write(strOutput);
                           strOutput = "";
                     }

              }

              writer.close();

       }

}
