import java.util.ArrayList;

import javax.swing.event.ChangeListener;

public class CalendarModel{
  
  /* Constructor */
  public CalendarModel() 
  {
    // myCalendar = new MyCalendar();
    // myCalendar.loadFile();
  }

  /* attach ChangeListener */
  public void attach(ChangeListener c)
  {
    changeListeners.add(c);
  }

  // private MyCalendar myCalendar;
  private ArrayList<ChangeListener> changeListeners = new ArrayList<ChangeListener>();
}
