/**
 * MyCalendar.java
 * 
 * Summer 2021, SJSU CS151
 * Programming Assignment 1
 * Author: Jooyul Yoon
 * Date: Oct 17, 2021
 */

import java.util.*;
import java.io.*;
import java.time.LocalDate;

public class MyCalendar {
  private final List<Event> eventList = new ArrayList<Event>();

  /**
   * add Event passed by parameter to the eventList
   */
  public void addEvent(Event newEvent) {
    // when time input is valid and no time conflict
    if(!isTimeConflict(newEvent)){
      if(newEvent.isValid)
        eventList.add(newEvent);
      else         
        System.out.println("Error: Failed to add '" + newEvent.title + "' - invalid time input\n");

    }
    // time input is invalid
  }

  /**
   * This is onlycalled when a user chooses [V]iew
   * It prompts user to choose Day view or Month View
   */
  public void viewEventsBy() {
    String option = "";
    Scanner sc = new Scanner(System.in);

    // prompt
    printNewLine(14);
    System.out.println("[D]ay view or [M]onth view ?");
    option = sc.next().toLowerCase();

    switch(option){
      case "d":
        dayView();
        break;
      case "m":
        monthView();
        break;
      default:
        printNewLine(12);
        System.out.println("Error: Invalid option, please try again.");
        break;
    }
  }
 
  /**
   * This is called when a user chooses [C]reate
   * Ont Time Event Only
   */
  public void createNewEvent() {
    Event newEvent = getNewEventInfo(); // this return null when date input is invalid

    if(newEvent == null) {
      printNewLine(7);
      System.out.println("Error: Failed to add - invalid date input");
      return;
    }
    
    System.out.println("'" + newEvent.title + "' Successfully Added!");
    addEvent(newEvent);
  }

  /**
   * This prompts user to enter date
   * and show events on that date
   */
  public void searchEvent() {
    String option = "";
    String goToDate;
    Scanner sc = new Scanner(System.in);

    printNewLine(14);
    System.out.print("Enter a date to go (MM/DD/YY): "); goToDate = sc.next();
    if(!isDateValid(goToDate)){ 
      printNewLine(12);
      System.out.println("Error: invalid date input"); 
      return; 
    }

    LocalDate c = LocalDate.of(Integer.parseInt("20" + goToDate.split("/")[2]), 
                                Integer.parseInt(goToDate.split("/")[0]),
                                Integer.parseInt(goToDate.split("/")[1]));
    // show events on the date user entered
    showEventByDate(c);         

    // previous / next / going back
    while(option != "g"){
      option = sc.next().toLowerCase();
      switch (option){
        case "p":
          c = c.minusDays(1);
          showEventByDate(c);
          break;
        case "n":
          c = c.plusDays(1);
          showEventByDate(c);
          break;
        case "g":
          printNewLine(6);
          System.out.println("Going back to main menu!");
          printNewLine(6);
          return;
        default:
          System.out.println("Error: Invalid option, please try again.");
          break;
      }
    }
  }

  /**
   * This is called when a user chooses [E]ventList
   */
  public void showEventList() {
    int count = 0;
    List<Event> oneTimeEventList = oneTimeEvents();
    List<Event> recurringEventsList = recurringEvents();

    // show ont time events
    System.out.println("\nONE TIME EVENTS");
    if(oneTimeEventList.size() == 0) { // event does not exist
      System.out.println(" - "); count++;
    } else {
      for(Event event: oneTimeEventList) {
        System.out.printf("  %-25s\t%-10s\t%5s - %5s\n",
                              event.title,
                              event.firstDayToString(),
                              event.startTime,
                              event.endTime);
        count++;                      
      }
    }

    // show recurringn events
    System.out.println("\nRECURRING EVENTS");
    if(recurringEventsList.size() == 0) { // event does not exist
      System.out.println(" - ");
      count++;
    }
    else {
      for(Event event: recurringEventsList) {
        System.out.printf("  %-25s\t%-10s\t%5s - %5s\n",
                              event.title,
                              event.firstDayToString(),
                              event.startTime,
                              event.endTime);
        count++;                      
      }
    }
    printNewLine(9-count);
  }

  /**
   * This is called when a user chooses [D]elete
   */
  public void deleteEvent() {
    String option = "";
    printNewLine(14); System.out.print("[S]elected  [A]ll   [DR] ");
    Scanner sc = new Scanner(System.in);
    option = sc.next().toLowerCase();

    switch(option){
      case "s":
        deleteSelectedOneTime();
        break;
      case "a":
        deleteAllOneTime();
        break;
      case "dr":
        deleteRecur();
        break;
      default:
        printNewLine(12);
        System.out.println("Error: Invalid option. Please try again.");
        break;
    }
  }

  /**
   * save current eventList to file
   * it is only called when user tries to terminate the program. 
   */
  public void saveToFile() {
    File outputfile = new File("output.txt");
    try {
      outputfile.createNewFile();
      FileWriter writer = new FileWriter(outputfile);
      List<Event> oneTimeEventList = oneTimeEvents(); // this returns list of one time events
      List<Event> recurringEventsList = recurringEvents(); // returns list of recurring events
  
      // Write to the file
      writer.write("\nONE TIME EVENTS\n");
      if(oneTimeEventList.size() == 0) writer.write(" - \n");
      for(Event event: oneTimeEventList) {
        writer.write("  " + event.title);
        writer.write("    " + event.firstDayToString() + "\t"
                                  + event.startTime + " - " 
                                  + event.endTime + "\n");
      }
  
      writer.write("\nRECURRING EVENTS\n");
      if(recurringEventsList.size() == 0) writer.write(" - \n");
      for(Event event: recurringEventsList) {
        writer.write("  " + event.title);
        writer.write("    " + event.daysOfWeek + "\t" 
                                  + event.firstDayToString() + " ~ "
                                  + event.lastDayToString() + "\t"
                                  + event.endTime + " - "
                                  + event.startTime + "\n");
      }
      writer.close();
    } catch (IOException e) {
      System.out.println("Error: file not created");
      e.printStackTrace();
    }
  }



  /**
   * this prompts user to enter specific date and name of the event to delete
   * One Time Event Only
   */
  private void deleteSelectedOneTime() {
    String date, name;
    boolean found = false;
    int count = 0;
    List<Event> oneTimeEvents = oneTimeEvents();
    Scanner sc = new Scanner(System.in);
    
    printNewLine(14);
    System.out.print("Enter a date (MM/DD/YY): "); date = sc.nextLine();

    // Date input validation
    if(!isDateValid(date)) { 
      printNewLine(12);
      System.out.println("Error: invalid date input: " + date); 
      return;
    }

    LocalDate c = LocalDate.of(Integer.parseInt("20" + date.split("/")[2]), 
                                Integer.parseInt(date.split("/")[0]), 
                                Integer.parseInt(date.split("/")[1]));

    System.out.println("=============================================");
    System.out.println(c.getDayOfWeek() + ", " + 
                       c.getMonth() + " " + 
                       c.getDayOfMonth() + ", " + 
                       c.getYear() + "\n");

    for(Event e : oneTimeEvents) {
      for(TimeInterval t : e.timeIntervalList) {
        if(c.isEqual(t.date)) {
          System.out.println("  " + e.title);
          count++;
        }
      }
    }

    // When no event on the specific date
    if(count == 0) {
      printNewLine(6);
      System.out.println("There is no One time event on " + c.toString());
      printNewLine(6);
      return;
    }

    printNewLine(10-count);
    System.out.println("=============================================");

    System.out.print("Enter the name of the event to delete: "); name = sc.nextLine();

    for(Event event : eventList) {
      if(event.isOneTimeEvent 
      && event.title.toLowerCase().equals(name.toLowerCase())
      && event.timeIntervalList.get(0).date.isEqual(c)) {
        eventList.remove(event);
        found = true;
        break;
      }
    }

    if(found) System.out.println("Successfully deleted!");
    else      System.out.println("Error: event not found");
  }

  /**
   * this prompts user to enter specific date 
   * and deletes all the one time event on that date
   */
  private void deleteAllOneTime(){
    String date;
    List<Event> oneTimeEvents = oneTimeEvents();
    List<Event> eventsToDelete = new ArrayList<>();
    int count = 0;
    Scanner sc = new Scanner(System.in);
    
    printNewLine(14);
    System.out.print("Enter a date (MM/DD/YY): "); date = sc.nextLine();
    // Date input validation
    if(!isDateValid(date)) { 
      printNewLine(12);
      System.out.println("Error: invalid date input: " + date); 
      return;}

    LocalDate c = LocalDate.of(Integer.parseInt("20" + date.split("/")[2]), 
                                Integer.parseInt(date.split("/")[0]), 
                                Integer.parseInt(date.split("/")[1]));

    System.out.println("=============================================");
    System.out.println(c.getDayOfWeek() + ", " + 
                       c.getMonth() + " " + 
                       c.getDayOfMonth() + ", " + 
                       c.getYear() + "\n");

    for(Event e : oneTimeEvents) {
      for(TimeInterval t : e.timeIntervalList) {
        if(c.isEqual(t.date)) {
          System.out.println("  " + e.title);
          count++;
          break;
        }
      }
    }
    
    // When no event on the specific date
    if(count == 0) {
      printNewLine(6);
      System.out.println("Error: There is no One time event on " + c.toString());
      printNewLine(6);
      return;
    }

    printNewLine(9-(count*2));
    System.out.println("=============================================");

    // Delete all ont time events
    for(Event event : eventList) {
      if(event.isOneTimeEvent && event.timeIntervalList.get(0).date.isEqual(c)) {
        System.out.println(event.title + " - deleted");
        eventsToDelete.add(event);
      }
    }
    for(Event e : eventsToDelete)
      eventList.remove(e);
  }

  /**
   * This shows list of recurring events
   * and prompt user to enter name of event to delete
   */
  private void deleteRecur() {
    String name;
    List<Event> recurringEvents = recurringEvents();
    boolean found = false;
    int count = 0;

    Scanner sc = new Scanner(System.in);
    System.out.println("=============================================");
    System.out.println("Recurring Events\n");

    // When there is no recurring event in event list
    if(recurringEvents.size() == 0) {
      printNewLine(12);
      System.out.println("Error: there is no recurring events currently");
      return;
    }

    for(Event e : recurringEvents) {
      System.out.println("  " + e.title);
      count++;
    }

    printNewLine(10-count);
    System.out.println("=============================================");

    // prompt user to enter event name
    System.out.print("Enter the name of the event to delete: "); 
    name = sc.nextLine();

    for(Event event : eventList) {
      if(!event.isOneTimeEvent && event.title.toLowerCase().equals(name.toLowerCase())) {
        eventList.remove(event);
        found = true;
        break;
      }
    }
    printNewLine(12);
    if(found) System.out.println(name + " - Successfully deleted!");
    else      System.out.println("Error: event '"+ name +"'not found");

  }

  /**
   * This returns a list containing one time events only
   */
  private List<Event> oneTimeEvents() {
    List<Event> oneTimeEventsList = new ArrayList<>();
    for(Event event : eventList) {
      if(event.isOneTimeEvent) {
        oneTimeEventsList.add(event);
      }
    }

    // sort the list by start time
    Collections.sort(oneTimeEventsList, Event.eventComparator);

    return oneTimeEventsList;

  }

  /**
   * This returns a list containing recurring events only
   */
  private List<Event> recurringEvents() {
    List<Event> recurringEventsList = new ArrayList<>();
    for(Event event : eventList) {
      if(!event.isOneTimeEvent) {
        recurringEventsList.add(event);
      }
    }

    // sort the list by start time
    Collections.sort(recurringEventsList, Event.eventComparator);

    return recurringEventsList;
  }

  /**
   * This prompts user to enter new event's 
   * name, date, start time, and end time
   */
  private Event getNewEventInfo() {
    String title, schedule, date, startTime, endTime;
    Scanner sc = new Scanner(System.in);

    // prompt
    printNewLine(12);
    System.out.println("Creating an event (one-time event only)\n");
    System.out.print("Name: "); title = sc.nextLine();
    System.out.print("Date (MM/DD/YY): "); date = sc.nextLine();
    System.out.print("Starting Time (HH:MM, 24hour): "); startTime = sc.nextLine();
    System.out.print("  Ending Time (HH:MM, 24hour): "); endTime = sc.nextLine();

    if(!isDateValid(date)) 
      return null;

    // show the input
    printNewLine(2);
    System.out.println(" " + title + " " + date + " " + startTime + " " + endTime);
    printNewLine(2);
    
    // set correct format of 'schedule' 
    // to pass (new Event) parameter
    schedule = date + " " + startTime + " " + endTime;

    return new Event(title, schedule);
  }

  /**
   * This shows current day's events
   * It also allows user to see previous or next date
   */
  private void dayView() {
    LocalDate c = LocalDate.now();
    Scanner sc = new Scanner(System.in);
    String option = "";

    // show today's events
    showEventByDate(c);

    // previous or next or going back
    while(option != "g"){
      option = sc.next().toLowerCase();
      switch (option){
        case "p":
          c = c.minusDays(1);
          showEventByDate(c);
          break;
        case "n":
          c = c.plusDays(1);
          showEventByDate(c);
          break;
        case "g":
          printNewLine(6);
          System.out.println("Going back to main menu!");
          printNewLine(6);
          return;
        default:
          System.out.println("SCHEDULES");
          System.out.println("=============================================");
          System.out.println(c.getDayOfWeek() + ", " + 
                            c.getMonth() + " " + 
                            c.getDayOfMonth() + ", " + 
                            c.getYear() + "\n");
          printNewLine(7);
          System.out.println("Error: Invalid option, please try again.\n");
          System.out.println("=============================================");
          System.out.print("[P]revious or [N]ext or [G]o back to main menu ?");
          break;
      }
    }
  }

  /**
   * @param c
   * This shows events of specific date c
   */
  private void showEventByDate(LocalDate c){
    int count = 0;
    System.out.println("SCHEDULES");
    System.out.println("=============================================");
    System.out.println(c.getDayOfWeek() + ", " + 
                       c.getMonth() + " " + 
                       c.getDayOfMonth() + ", " + 
                       c.getYear() + "\n");
    for (Event event : eventList) {
      for(TimeInterval t : event.timeIntervalList) {
        if (c.equals(t.date)){
          System.out.println("  " + event.title + ": "
                                  + event.startTime + " - "
                                  + event.endTime);
          count++;
          break;
        }
      }
    }
    printNewLine(9-count);
    System.out.println("=============================================");
    System.out.print("[P]revious or [N]ext or [G]o back to main menu ?");
  }

  /**
   * This shows current day's month
   * It also allows user to see previous or next date
   */
  private void monthView(){
    LocalDate c = LocalDate.now();
    String option = "";
    showEventByMonth(c);

    // previous / next / going back
    while(option != "g"){
      Scanner sc = new Scanner(System.in);
      option = sc.next().toLowerCase();
      switch (option){
        case "p":
          c = c.minusMonths(1);
          showEventByMonth(c);
          break;
        case "n":
          c = c.plusMonths(1);
          showEventByMonth(c);
          break;
        case "g":
          printNewLine(6);
          System.out.println("Going back to main menu!");
          printNewLine(6);
          return;
        default:
          printNewLine(10);
          System.out.println("Error: Invalid option, please try again.\n");
          System.out.print("[P]revious or [N]ext or [G]o back to main menu ?");
          break;
      }
    }
  }

  /**
   * @param c
   * This shows the month of the specific date c
   * it also indicates events on the month with {} brackets
   */
  private void showEventByMonth(LocalDate c) {
    int totalDaysOfMonth = c.getMonth().length(c.isLeapYear());
    int count = 0, offset = 0;
    
    System.out.println("=============================================");
    String firstDayOfMonth = LocalDate.of(c.getYear(), c.getMonth(), 1).getDayOfWeek().name();
    if     (firstDayOfMonth == "SUNDAY")    offset = 0;
    else if(firstDayOfMonth == "MONDAY")    offset = 1;
    else if(firstDayOfMonth == "TUESDAY")   offset = 2;
    else if(firstDayOfMonth == "WEDNESDAY") offset = 3;
    else if(firstDayOfMonth == "THURSDAY")  offset = 4;
    else if(firstDayOfMonth == "FRIDAY")    offset = 5;
    else if(firstDayOfMonth == "SATURDAY")  offset = 6;

    System.out.println(" " + c.getMonth() + " " + c.getYear());
    System.out.println("\n Su Mo Tu We Th Fr Sa");

    // indentation
    if(c.getDayOfMonth() != 1)
      System.out.print(" ");
    for(int i = 0; i < offset; i++) 
      System.out.print("   ");
    
    // if start date is SUNDAY, change it to 7
		// to make sure (7 - offset) is smaller than 7
    if(offset == 0) offset = 7;
    
    // iterate days of month
    for(int i = 1; i <= totalDaysOfMonth; i++) {
      LocalDate ld = LocalDate.of(c.getYear(), c.getMonth(), i);

      // if event existing
      if(isEventExist(ld)) {
        if(i > 1 && i < 10 && !isEventExist(ld.minusDays(1))) 
          System.out.print(" ");
        System.out.print("{"+ i + "}");
      } 
      // if event not existing
      else {
        if (i < 10) 
          System.out.print(" ");
        System.out.print(i);
        if(!isEventExist(ld.plusDays(1))) 
          System.out.print(" ");
      }

			// when interator reaches saturday it writes new line.
      if(i % 7 == (7 - offset)) {
        System.out.print("\n ");
        count++;
      }
    } 

    printNewLine(9-count);
    System.out.println("=============================================");
    System.out.print("[P]revious or [N]ext or [G]o back to main menu ?");
  }

  /**
   * @param c
   * @return true if there is any event on the date c
   *         false, otherwise
   */
  private boolean isEventExist(LocalDate c) {
    for(Event event : eventList) {
      for(TimeInterval t : event.timeIntervalList) {
        if(c.equals(t.date))
          return true;
      }
    }
    return false;
  }

  /**
   * @param date
   * @return true if string format of date is valid
   *         false, otherwise
   */
  private boolean isDateValid(String dateStr){

    if(dateStr.matches("\\d{1,2}/\\d{1,2}/\\d{2}")) {
      int year = Integer.parseInt("20" + dateStr.split("/")[2]);
      int month = Integer.parseInt(dateStr.split("/")[0]);
      int dayOfMonth = Integer.parseInt(dateStr.split("/")[1]);

      try {
        LocalDate c = LocalDate.of(year, month, dayOfMonth);
      } catch(Exception e) {
        return false;
      }
      return true;
    }
    return false;
  }

  /**
   * @param count
   * this print number of new lines
   */
  private void printNewLine(int count) {
    for(int i = 0; i < count; i++)
      System.out.println();
  }

  /**
   * Cheking time conflict
   */
  private boolean isTimeConflict(Event newEvent){
    for(TimeInterval newTimeInterval : newEvent.timeIntervalList) {
      LocalDate newEventDate = newTimeInterval.date;
      int newEventStartTime = newTimeInterval.startTimeInMinute;
      int newEventEndTime = newTimeInterval.endTimeInMinute;
      for(Event oldEvent : eventList) {
        for(TimeInterval oldTimeInterval : oldEvent.timeIntervalList) {
          LocalDate oldEventDate = oldTimeInterval.date;
          int oldEventStartTime = oldTimeInterval.startTimeInMinute;
          int oldEventEndTime = oldTimeInterval.endTimeInMinute;

          if(!newEventDate.isEqual(oldEventDate))
            continue;
          if((newEventStartTime < oldEventStartTime && newEventEndTime > oldEventStartTime) ||
             (newEventStartTime < oldEventEndTime   && newEventEndTime > oldEventEndTime)   ||
             (newEventStartTime > oldEventEndTime   && newEventEndTime < oldEventEndTime)) {
            System.out.println("Error: time conflict: '" + newEvent.title + "'' with '" + oldEvent.title + "'");
            printNewLine(1);
            System.out.println(oldEvent.title + " " + oldEvent.startTime + " - " + oldEvent.endTime);
            
            return true;
          }
        }
      }
    }
    return false;
  }
}
