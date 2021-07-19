import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import javax.swing.event.*;
public class CalendarView implements ChangeListener{
  final int WINDOW_WIDTH = 800;
  final int WINDOW_HEIGHT = 300;
  final int BASE_SPACE = 20;
  final int PANEL_WIDTH = (WINDOW_WIDTH - 3 * BASE_SPACE) / 2;
  final int PANEL_HEIGHT = WINDOW_HEIGHT - 2 * BASE_SPACE;
  final int BTN_SIZE = 25;

  /* Constructor */
  public CalendarView() 
  {
    JFrame f= new JFrame("JACK CALENDAR");    

    JPanel leftPanel=new JPanel();   
    leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));
    leftPanel.setBounds(BASE_SPACE,BASE_SPACE,PANEL_WIDTH,PANEL_HEIGHT);
      JPanel titlePanel=new JPanel();
      titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
      titlePanel.setBorder(new EmptyBorder(BASE_SPACE /  2, BASE_SPACE, BASE_SPACE /  2, BASE_SPACE));
        JLabel monthLabel = new JLabel("July 2021");
        monthLabel.setFont(new Font("Arial", Font.BOLD, 20));
        JButton btnPrevMonth=new JButton("<"); btnPrevMonth.setPreferredSize(new Dimension(BTN_SIZE, BTN_SIZE));
        JButton btnNextMonth=new JButton(">"); btnNextMonth.setPreferredSize(new Dimension(BTN_SIZE, BTN_SIZE));
        JButton btnToday = new JButton("Today");
        titlePanel.add(monthLabel);titlePanel.add(btnPrevMonth); titlePanel.add(btnNextMonth);  titlePanel.add(btnToday);
      JPanel subPanel2=new JPanel();
      subPanel2.setLayout(new GridLayout(6,7));
      subPanel2.setBorder(new EmptyBorder(BASE_SPACE / 2, BASE_SPACE, BASE_SPACE, BASE_SPACE));
      subPanel2.add(new JButton("btn"));
      subPanel2.add(new JButton("btn"));
      subPanel2.add(new JButton("btn"));
      subPanel2.add(new JButton("btn"));
      subPanel2.add(new JButton("btn"));
      subPanel2.add(new JButton("btn"));
      subPanel2.add(new JButton("btn"));
      subPanel2.add(new JButton("btn"));
      subPanel2.add(new JButton("btn"));
      subPanel2.add(new JButton("btn"));
      subPanel2.add(new JButton("btn"));
      subPanel2.add(new JButton("btn"));
      subPanel2.add(new JButton("btn"));
      subPanel2.add(new JButton("btn"));
      subPanel2.add(new JButton("btn"));
      subPanel2.add(new JButton("btn"));
      subPanel2.add(new JButton("btn"));
      subPanel2.add(new JButton("btn"));
      subPanel2.add(new JButton("btn"));
      subPanel2.add(new JButton("btn"));
      subPanel2.add(new JButton("btn"));
      subPanel2.add(new JButton("btn"));
      subPanel2.add(new JButton("btn"));
      subPanel2.add(new JButton("btn"));
      subPanel2.add(new JButton("btn"));
      subPanel2.add(new JButton("btn"));
      subPanel2.add(new JButton("btn"));
      subPanel2.add(new JButton("btn"));
      subPanel2.add(new JButton("btn"));
      subPanel2.add(new JButton("btn"));
      subPanel2.add(new JButton("btn"));
      subPanel2.add(new JButton("btn"));
      subPanel2.add(new JButton("btn"));
      subPanel2.add(new JButton("btn"));
      subPanel2.add(new JButton("btn"));
      
      
    leftPanel.add(titlePanel); 
    leftPanel.add(subPanel2); 

    JPanel rightPanel=new JPanel();  
    rightPanel.setBounds(PANEL_WIDTH + 2 * BASE_SPACE,BASE_SPACE,PANEL_WIDTH,PANEL_HEIGHT);    
    JButton btnD = new JButton("Day");
    JButton btnW = new JButton("Week");
    JButton btnM = new JButton("Month");
    JButton btnA = new JButton("Agenda");
    JTextArea content = new JTextArea(11, 30);
    JButton btnCreate = new JButton("Create");
    JButton btnEdit = new JButton("Edit");
    JButton btnDelete = new JButton("Delete");
    rightPanel.add(btnD);
    rightPanel.add(btnW);
    rightPanel.add(btnM);
    rightPanel.add(btnA);
    rightPanel.add(content);
    rightPanel.add(btnCreate);
    rightPanel.add(btnEdit);
    rightPanel.add(btnDelete);
    
    
    
    f.add(leftPanel); f.add(rightPanel);  
    
    f.setSize(WINDOW_WIDTH, WINDOW_HEIGHT + 28);    
    f.setLayout(null);    
    f.setVisible(true);
  }

  @Override
  public void stateChanged(ChangeEvent e) {
    // TODO Auto-generated method stub
    
  }
  
}
