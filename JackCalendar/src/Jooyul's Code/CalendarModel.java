import java.util.ArrayList;

import javax.swing.event.ChangeListener;

public class CalendarModel {

  /* Constructor */
  public CalendarModel() {
    myCalendar = new MyCalendar();
    myCalendar.addEvent(new Event("Title", "TR 10:30 11:45 1/28/21 5/13/21"));
  }

  public MyCalendar getCalendar() {
    return myCalendar;
  }

  /* attach ChangeListener */
  public void attach(ChangeListener c) {
    changeListeners.add(c);
  }

  private MyCalendar myCalendar;
  private ArrayList<ChangeListener> changeListeners = new ArrayList<ChangeListener>();

}
