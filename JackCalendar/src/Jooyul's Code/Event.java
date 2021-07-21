
/**
 * Event.java
 * 
 * Summer 2021, SJSU CS151
 * Programming Assignment 1
 * Author: Jooyul Yoon
 * Date: Oct 17, 2021
 */

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Event {
  String title, daysOfWeek, startTime, endTime;
  List<TimeInterval> timeIntervalList = new ArrayList<>();
  boolean isOneTimeEvent, isValid;

  private int startHour, startMinute;

  // Constructor
  public Event(String title, String schedule) {
    this.title = title.trim();
    this.timeIntervalList = setSchedule(schedule.trim());
    if (this.timeIntervalList == null)
      this.isValid = false;
    else
      this.isValid = true;
  }

  /**
   * This comparator allows to sort in order of start time
   */
  public static Comparator<Event> eventComparator = new Comparator<Event>() {
    public int compare(Event e1, Event e2) {
      LocalDate startDate1 = e1.timeIntervalList.get(0).date;
      LocalDate startDate2 = e2.timeIntervalList.get(0).date;
      int startHour1 = e1.startHour;
      int startMinute1 = e1.startMinute;
      int startHour2 = e2.startHour;
      int startMinute2 = e2.startMinute;

      if (startDate1.isAfter(startDate2))
        return 1;
      else if (startDate1.isBefore(startDate2))
        return -1;
      else { // same date
        if (startHour1 > startHour2)
          return 1;
        else if (startHour1 < startHour2)
          return -1;
        else { // same hour
          if (startMinute1 <= startMinute2)
            return 1;
          else
            return -1;
        }
      }
    }
  };

  /**
   * This convert string form of schedule passed by parameter to individual
   * information: start date, end date etc then set list of time interval based on
   * the data converted returns List of timeInterval
   */
  private List<TimeInterval> setSchedule(String schedule) {
    Scanner sc = new Scanner(schedule);
    String firstInput = sc.next();
    String startTime = sc.next();
    String endTime = sc.next();
    String daysOfWeek, startDate, endDate;

    // check if time input is invalid
    if (!isValidTime(startTime, endTime)) {
      sc.close();
      return null;
    }

    if (firstInput.length() < 5) // This is Recurring event
    {
      isOneTimeEvent = false;
      daysOfWeek = firstInput;
      startDate = sc.next();
      endDate = sc.next();
    } else // This is One Time Event
    {
      isOneTimeEvent = true;
      daysOfWeek = "";
      startDate = endDate = firstInput;
    }

    this.daysOfWeek = daysOfWeek;
    this.startTime = startTime;
    this.endTime = endTime;
    startHour = Integer.parseInt(startTime.split(":")[0]);
    startMinute = Integer.parseInt(startTime.split(":")[1]);

    sc.close();
    return setTimeInterval(daysOfWeek, startDate, endDate, startTime, endTime);
  }

  /**
   * This sets time interval list based on the time information passed by
   * parameter
   */
  private List<TimeInterval> setTimeInterval(String daysOfWeekStr, String startDateStr, String endDateStr,
      String startTime, String endTime) {
    List<TimeInterval> tiList = new ArrayList<>();
    List<LocalDate> ldList = new ArrayList<>();
    LocalDate startDate = LocalDate.of(Integer.parseInt("20" + endDateStr.split("/")[2]),
        Integer.parseInt(startDateStr.split("/")[0]), Integer.parseInt(startDateStr.split("/")[1]));
    LocalDate endDate = LocalDate.of(Integer.parseInt("20" + endDateStr.split("/")[2]),
        Integer.parseInt(endDateStr.split("/")[0]), Integer.parseInt(endDateStr.split("/")[1]));
    // list of Localdate from specific start date to end date
    List<LocalDate> tempList = startDate.datesUntil(endDate.plusDays(1)).collect(Collectors.toList());

    if (daysOfWeekStr == "") // one time event
      ldList = tempList;
    else { // recurring event
      // filter days of week
      List<String> daysOfWeek = parseDaysOfWeek(daysOfWeekStr);
      for (LocalDate c : tempList) {
        if (daysOfWeek.contains(c.getDayOfWeek().toString()))
          ldList.add(c);
      }
    }

    // Set List of TimeInterval
    for (LocalDate c : ldList) {
      TimeInterval timeInterval = new TimeInterval(c, startTime, endTime);
      tiList.add(timeInterval);
    }

    return tiList;
  }

  /**
   * return list of format of day of week ex. MWF => returns ["MONDAY",
   * "WEDNESDAY", "FRIDAY"]
   */
  private List<String> parseDaysOfWeek(String str) {
    List<String> daysOfWeek = new ArrayList<String>();

    if (str.contains("S"))
      daysOfWeek.add("SUNDAY");
    if (str.contains("M"))
      daysOfWeek.add("MONDAY");
    if (str.contains("T"))
      daysOfWeek.add("TUESDAY");
    if (str.contains("W"))
      daysOfWeek.add("WEDNESDAY");
    if (str.contains("R"))
      daysOfWeek.add("THURSDAY");
    if (str.contains("F"))
      daysOfWeek.add("FRIDAY");
    if (str.contains("A"))
      daysOfWeek.add("SATURDAY");

    return daysOfWeek;
  }

  /**
   * 1. check format of time input (HH:MM) 2. check if start time is smaller than
   * end time
   * 
   * @param startTime
   * @param endTime
   */
  private boolean isValidTime(String startTime, String endTime) {
    if (!startTime.matches("\\d{1,}:\\d{1,2}") || !endTime.matches("\\d{1,}:\\d{1,2}"))
      return false;
    else {
      int startTimeInMinute = Integer.parseInt(startTime.split(":")[0]) * 60
          + Integer.parseInt(startTime.split(":")[1]);
      int endTimeInMinute = Integer.parseInt(endTime.split(":")[0]) * 60 + Integer.parseInt(endTime.split(":")[1]);

      if (startTimeInMinute < 0 || startTimeInMinute > (24 * 60) || endTimeInMinute < 0 || endTimeInMinute > (24 * 60))
        return false;
      else if (startTimeInMinute > endTimeInMinute)
        return false;
    }

    return true;
  }

  /**
   * This returns first day of the event
   * 
   * @return string
   */
  public String firstDayToString() {
    LocalDate c = timeIntervalList.get(0).date;
    return c.getMonthValue() + "/" + c.getDayOfMonth() + "/" + (c.getYear() - 2000);
  }

  /**
   * This return last day of this event
   * 
   * @return string
   */
  public String lastDayToString() {
    LocalDate c = timeIntervalList.get(timeIntervalList.size() - 1).date;
    return c.getMonthValue() + "/" + c.getDayOfMonth() + "/" + (c.getYear() - 2000);
  }

}
