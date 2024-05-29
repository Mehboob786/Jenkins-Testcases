package com.qa.doingerp.exception;

@SuppressWarnings("serial")
public class UnExpectedException extends RuntimeException
{
  public String errorName = "Error";

  public UnExpectedException(String errorDescription)
  {
    super(errorDescription);
  }

  public UnExpectedException(String errorName, String errorDescription)
  {
    super(errorDescription);
    this.errorName = errorName;
  }
}