import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;

import javax.swing.event.*;

public class CalendarView implements ChangeListener {
  /* Constructor */
  public CalendarView(CalendarModel m) {
    model = m;
    myCalendar = m.getCalendar();
    currentDate = LocalDate.now();
    JFrame f = new JFrame("JACK CALENDAR");

    JPanel leftPanel = new JPanel();
    leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));
    leftPanel.setBounds(BASE_SPACE, BASE_SPACE, PANEL_WIDTH, PANEL_HEIGHT);
    JPanel rightPanel = new JPanel();
    rightPanel.setBounds(PANEL_WIDTH + 2 * BASE_SPACE, BASE_SPACE, PANEL_WIDTH, PANEL_HEIGHT);

    /* Left Panel */
    JPanel titlePanel = new JPanel();
    titlePanel.setLayout(new GridLayout());
    titlePanel.setBorder(new EmptyBorder(BASE_SPACE / 2, BASE_SPACE, BASE_SPACE / 2, BASE_SPACE));
    JPanel calendarPanel = new JPanel();

    /* Left Panel Component */
    JLabel monthLabel = new JLabel(getMonthAbbreviation(currentDate), SwingConstants.CENTER);
    monthLabel.setFont(new Font("Arial", Font.BOLD, 20));
    JLabel yearLabel = new JLabel(Integer.toString(currentDate.getYear()));
    JButton btnPrevMonth = new JButton("<");
    btnPrevMonth.setPreferredSize(new Dimension(BTN_SIZE, BTN_SIZE));
    JButton btnNextMonth = new JButton(">");
    btnNextMonth.setPreferredSize(new Dimension(BTN_SIZE, BTN_SIZE));
    JButton btnToday = new JButton("Today");

    /* Right Panel Component */
    JButton btnD = new JButton("Day");
    JButton btnW = new JButton("Week");
    JButton btnM = new JButton("Month");
    JButton btnA = new JButton("Agenda");
    JTextArea content = new JTextArea(11, 30);
    JButton btnCreate = new JButton("Create");
    JButton btnEdit = new JButton("Edit");
    JButton btnDelete = new JButton("Delete");
    JButton btnLoadFile = new JButton("File");

    /* Initialize Calendar */
    calendarDateBtnHandler(calendarPanel, currentDate, content);

    content.setText("Welcome to JACK Calendar!");
    /* ActionListeners */
    btnPrevMonth.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // update current date
        currentDate = currentDate.minusMonths(1);
        // update title
        monthLabel.setText(getMonthAbbreviation(currentDate));
        yearLabel.setText(Integer.toString(currentDate.getYear()));
        // update monthly calendar
        calendarPanel.removeAll();
        calendarDateBtnHandler(calendarPanel, currentDate, content);
      }
    });
    btnNextMonth.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // update current date
        currentDate = currentDate.plusMonths(1);
        // update title
        monthLabel.setText(getMonthAbbreviation(currentDate));
        yearLabel.setText(Integer.toString(currentDate.getYear()));
        // update monthly calendar
        calendarPanel.removeAll();
        calendarDateBtnHandler(calendarPanel, currentDate, content);
      }
    });
    btnToday.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // update current date
        currentDate = LocalDate.now();
        // update title
        monthLabel.setText(getMonthAbbreviation(currentDate));
        yearLabel.setText(Integer.toString(currentDate.getYear()));
        // update monthly calendar
        calendarPanel.removeAll();
        calendarDateBtnHandler(calendarPanel, currentDate, content);
      }
    });

    /* Left Panel Component */
    titlePanel.add(monthLabel);
    titlePanel.add(yearLabel);
    titlePanel.add(btnPrevMonth);
    titlePanel.add(btnNextMonth);
    titlePanel.add(btnToday);
    /* Right Panel Component */
    leftPanel.add(titlePanel);
    leftPanel.add(calendarPanel);
    rightPanel.add(btnD);
    rightPanel.add(btnW);
    rightPanel.add(btnM);
    rightPanel.add(btnA);
    rightPanel.add(content);
    rightPanel.add(btnCreate);
    rightPanel.add(btnEdit);
    rightPanel.add(btnDelete);
    rightPanel.add(btnLoadFile);

    f.add(leftPanel);
    f.add(rightPanel);

    f.setSize(WINDOW_WIDTH, WINDOW_HEIGHT + 28);
    f.setLayout(null);
    f.setVisible(true);
  }

  /**
   * January Jan. February Feb. March Mar. April Apr. May May June Jun. July Jul.
   * August Aug. September Sep. or Sept. October Oct. December Dec. November Nov.
   * 
   * @param date Local Date
   * @return String of Month Abbreviations
   */
  public String getMonthAbbreviation(LocalDate date) {
    return date.getMonth().toString().substring(0, 3);
  }

  public void calendarDateBtnHandler(JPanel calendarPanel, LocalDate c, JTextArea content) {
    int totalDaysOfMonth = c.getMonth().length(c.isLeapYear());
    int offset = 0;
    String firstDayOfMonth = LocalDate.of(c.getYear(), c.getMonth(), 1).getDayOfWeek().name();

    // offset depends on the first day of the first week
    if (firstDayOfMonth == "SUNDAY")
      offset = 0;
    else if (firstDayOfMonth == "MONDAY")
      offset = 1;
    else if (firstDayOfMonth == "TUESDAY")
      offset = 2;
    else if (firstDayOfMonth == "WEDNESDAY")
      offset = 3;
    else if (firstDayOfMonth == "THURSDAY")
      offset = 4;
    else if (firstDayOfMonth == "FRIDAY")
      offset = 5;
    else if (firstDayOfMonth == "SATURDAY")
      offset = 6;
    else
      throw new IllegalArgumentException("Invalid day of week");

    calendarPanel.setLayout(new GridLayout(7, 7));
    calendarPanel.setBorder(new EmptyBorder(BASE_SPACE / 2, BASE_SPACE, BASE_SPACE, BASE_SPACE));
    JLabel sunLabel = new JLabel("S", SwingConstants.CENTER);
    sunLabel.setForeground(Color.red);
    sunLabel.setFont(new Font("Arial", Font.BOLD, 14));
    JLabel monLabel = new JLabel("M", SwingConstants.CENTER);
    monLabel.setFont(new Font("Arial", Font.BOLD, 14));
    JLabel tueLabel = new JLabel("T", SwingConstants.CENTER);
    tueLabel.setFont(new Font("Arial", Font.BOLD, 14));
    JLabel wedLabel = new JLabel("W", SwingConstants.CENTER);
    wedLabel.setFont(new Font("Arial", Font.BOLD, 14));
    JLabel thuLabel = new JLabel("T", SwingConstants.CENTER);
    thuLabel.setFont(new Font("Arial", Font.BOLD, 14));
    JLabel friLabel = new JLabel("F", SwingConstants.CENTER);
    friLabel.setFont(new Font("Arial", Font.BOLD, 14));
    JLabel satLabel = new JLabel("S", SwingConstants.CENTER);
    satLabel.setFont(new Font("Arial", Font.BOLD, 14));
    calendarPanel.add(sunLabel);
    calendarPanel.add(monLabel);
    calendarPanel.add(tueLabel);
    calendarPanel.add(wedLabel);
    calendarPanel.add(thuLabel);
    calendarPanel.add(friLabel);
    calendarPanel.add(satLabel);

    for (int i = 0; i < offset; i++) {
      JButton dateBtn = new JButton();
      dateBtn.setEnabled(false);
      dateBtn.setVisible(false);
      calendarPanel.add(dateBtn);
    }
    // Add buttons
    for (int i = 0; i < totalDaysOfMonth; i++) {
      JButton dateBtn = new JButton(Integer.toString(i + 1));
      calendarPanel.add(dateBtn);

      dateBtn.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          int date = Integer.parseInt(dateBtn.getText());
          // Update current date
          currentDate = LocalDate.of(c.getYear(), c.getMonth(), date);
          System.out.println("current date is: " + currentDate.toString());

          // update content of the date
          // content.removeAll();
          content.setText(myCalendar.getEventInfo(currentDate));
        }
      });
    }

    int remainingDays = CALENDAR_ROW * CALENDAR_COL - offset - totalDaysOfMonth;
    for (int i = 0; i < remainingDays; i++) {
      JButton dateBtn = new JButton();
      dateBtn.setEnabled(false);
      dateBtn.setVisible(false);
      calendarPanel.add(dateBtn);
    }
  }

  @Override
  public void stateChanged(ChangeEvent e) {
    // TODO Auto-generated method stub

  }

  final int WINDOW_WIDTH = 800;
  final int WINDOW_HEIGHT = 300;
  final int BASE_SPACE = 20;
  final int PANEL_WIDTH = (WINDOW_WIDTH - 3 * BASE_SPACE) / 2;
  final int PANEL_HEIGHT = WINDOW_HEIGHT - 2 * BASE_SPACE;
  final int BTN_SIZE = 25;
  final int CALENDAR_ROW = 6;
  final int CALENDAR_COL = 7;

  private LocalDate currentDate;
  private MyCalendar myCalendar;
  private CalendarModel model;
}
