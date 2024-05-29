package com.qa.doingerp.parameters;

public class FrameworkParameters
{
  private String relativePath;
  public final String fileSeparator = System.getProperty("file.separator");

  private Boolean stopExecution = Boolean.valueOf(false);
  private String runConfiguration;
  private static FrameworkParameters frameworkParameters;

  public String getRelativePath()
  {
    return this.relativePath;
  }

  public void setRelativePath(String relativePath)
  {
    this.relativePath = relativePath;
  }

  public Boolean getStopExecution()
  {
    return this.stopExecution;
  }

  public void setStopExecution(Boolean stopExecution)
  {
    this.stopExecution = stopExecution;
  }

  public String getRunConfiguration()
  {
    return this.runConfiguration;
  }

  public void setRunConfiguration(String runConfiguration)
  {
    this.runConfiguration = runConfiguration;
  }

  public static synchronized FrameworkParameters getInstance()
  {
    if (frameworkParameters == null) {
      frameworkParameters = new FrameworkParameters();
    }

    return frameworkParameters;
  }

  public Object clone() throws CloneNotSupportedException
  {
    throw new CloneNotSupportedException();
  }
}