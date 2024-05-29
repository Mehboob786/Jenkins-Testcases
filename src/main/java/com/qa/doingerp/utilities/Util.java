package com.qa.doingerp.utilities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

public class Util
{
  public static Date getCurrentTime()
  {
    Calendar calendar = Calendar.getInstance();
    return calendar.getTime();
  }

  public static String getCurrentFormattedTime()
  {
    Properties properties = Settings.getInstance();
    DateFormat dateFormat = new SimpleDateFormat(properties.getProperty("DateFormatString"));
    Calendar calendar = Calendar.getInstance();
    return dateFormat.format(calendar.getTime());
  }

  public static String getFormattedTime(Date time)
  {
    Properties properties = Settings.getInstance();
    DateFormat dateFormat = new SimpleDateFormat(properties.getProperty("DateFormatString"));
    return dateFormat.format(time);
  }

  public static String getTimeDifference(Date startTime, Date endTime)
  {
    long timeDifference = endTime.getTime() - startTime.getTime();
    timeDifference /= 1000L;
    String timeDifferenceDetailed = Long.toString(timeDifference / 60L) + " minute(s), " + 
      Long.toString(timeDifference % 60L) + " seconds";
    return timeDifferenceDetailed;
  }
}