import java.util.ArrayList;

import javax.swing.event.ChangeListener;

public class CalendarModel{
  
  /* Constructor */
  public CalendarModel() 
  {
    // myCalendar = new MyCalendar();
    // myCalendar.addEvent(new Event("Title", "2014;1;2;MWF;17;18;"));
  }

  
  // public MyCalendar getCalendar() {
  //   return myCalendar;
  // }

  /* attach ChangeListener */
  public void attach(ChangeListener c)
  {
    changeListeners.add(c);
  }

  // private MyCalendar myCalendar;
  private ArrayList<ChangeListener> changeListeners = new ArrayList<ChangeListener>();

}
