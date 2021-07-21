/**
 * TimeInterval.java
 * 
 * Summer 2021, SJSU CS151
 * Programming Assignment 1
 * Author: Jooyul Yoon
 * Date: Oct 17, 2021
 */

import java.time.LocalDate;

public class TimeInterval {
  LocalDate date;
  int startTimeInMinute, endTimeInMinute;

  // Constructor
  public TimeInterval(LocalDate c, String startTime, String endTime) {
    date = c;
    startTimeInMinute = convertToMinute(startTime);
    endTimeInMinute = convertToMinute(endTime);
  }

  /**
   * ex. convertToMinute(1:30) => returns 90
   */
  private int convertToMinute(String timeStr) {
    int yearInMinute = date.getYear()                           * 60 * 24 * 365;
    int monthInMinute = date.getMonthValue()                    * 60 * 24 * date.getMonth().length(date.isLeapYear());
    int dayInMinute = date.getDayOfMonth()                      * 60 * 24;
    int hourInMinute = Integer.parseInt(timeStr.split(":")[0])  * 60;
    int minute = Integer.parseInt(timeStr.split(":")[1]);

    return yearInMinute + monthInMinute + dayInMinute + hourInMinute + minute;
  }
}
