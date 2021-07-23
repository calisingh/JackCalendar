import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.time.LocalDate;

import javax.swing.event.*;


/**
 * View and Controller for Calendar
 * 
 * @author Carissa, Jooyul, Kunwarpreet
 */
public class CalendarView implements ChangeListener {

  final int WINDOW_WIDTH = 800;
  final int WINDOW_HEIGHT = 300;
  final int BASE_SPACE = 20;
  final int PANEL_WIDTH = (WINDOW_WIDTH - 3 * BASE_SPACE) / 2;
  final int PANEL_HEIGHT = WINDOW_HEIGHT - 2 * BASE_SPACE;
  final int BTN_SIZE = 25;
  final int CALENDAR_ROW = 6;
  final int CALENDAR_COL = 7;

  LocalDate current = LocalDate.now();
  private JFrame frame = new JFrame("JACK CALENDAR");
  private LocalDate currentDate;
  private CalendarModel model;

    
    /* Constructor */
    public CalendarView(CalendarModel m) {

    // private MyCalendar myCalendar;

      model = m;
      // myCalendar = m.getCalendar();
      currentDate = LocalDate.now();

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
      // JButton btnEdit = new JButton("Edit");
      // JButton btnDelete = new JButton("Delete");
      JButton btnLoadFile = new JButton("From File");

      /* Initialize Calendar */
      showMonthlyCalendar(calendarPanel, currentDate, content);

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
          showMonthlyCalendar(calendarPanel, currentDate, content);
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
          showMonthlyCalendar(calendarPanel, currentDate, content);
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
          showMonthlyCalendar(calendarPanel, currentDate, content);
        }
      });
      btnCreate.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          createEventDialog();
        }
      });
      btnLoadFile.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          loadFileDialog();
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
      // rightPanel.add(btnEdit);
      // rightPanel.add(btnDelete);
      rightPanel.add(btnLoadFile);

      f.add(leftPanel);
      f.add(rightPanel);

      f.setSize(WINDOW_WIDTH, WINDOW_HEIGHT + 28);
      f.setLayout(null);
      f.setVisible(true);
    }


      frame.add(leftPanel);
      frame.add(rightPanel);

      frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT + 28);
      frame.setLayout(null);
      frame.setVisible(true);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
    }

    public void showMonthlyCalendar(JPanel calendarPanel, LocalDate c, JTextArea content) {
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
            // content.setText(myCalendar.getEventInfo(currentDate));

            // content.setText(myCalendar.getEventInfo(currentDate));
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

  private void createEventDialog() {
		JDialog eventDialog = new JDialog();
    JPanel panel = (JPanel) eventDialog.getContentPane();
		JTextField eventText = new JTextField(10);
    JTextField yearText = new JTextField(5);
		JTextField dateText = new JTextField(5);
		JTextField startTimeText = new JTextField(5);
		JTextField endTimeText = new JTextField(5);
		JButton btnSave = new JButton("Save");

    btnSave.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // Empty input
        if(
          eventText.getText().isEmpty() || 
          yearText.getText().isEmpty() || 
          dateText.getText().isEmpty() || 
          startTimeText.getText().isEmpty() || 
          endTimeText.getText().isEmpty()
        ) {
          JDialog ErrorDialog = new JDialog();
          JButton btnClose = new JButton("Close");
          btnClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              ErrorDialog.dispose();
            }
          });
          ErrorDialog.setLocation(100, 100);
          ErrorDialog.setSize(new Dimension(100, 100));
          ErrorDialog.add(new JLabel("Error: Empty Input"));
          ErrorDialog.add(btnClose);
          ErrorDialog.setLayout(new GridLayout(2, 0));
          ErrorDialog.pack();
          ErrorDialog.setVisible(true);
        }
        else {
          String eventTitle = eventText.getText();
          String eventSchedule = yearText.getText() + ";" + dateText.getText()    + ";" + 
                                  startTimeText.getText() + ";" + endTimeText.getText() + ";";
          // Event newEvent = new Event(eventTitle, eventSchedule);
        }

        // close dialog
        eventDialog.dispose();
      }
    });

    panel.add(new JLabel("Event Name"));
    panel.add(eventText);
    panel.add(new JLabel("Year"));
    panel.add(yearText);
    panel.add(new JLabel("Date"));
    panel.add(dateText);
    panel.add(new JLabel("Start Time (0~23)"));
    panel.add(startTimeText);
    panel.add(new JLabel("End Time (0~23)"));
    panel.add(endTimeText);
    panel.add(new JLabel(""));
    panel.add(btnSave);
    
    panel.setBorder(new EmptyBorder(5, 10, 5, 10));
		eventDialog.setTitle("Create event");
		eventDialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
    eventDialog.setLayout(new GridLayout(6, 2));
		eventDialog.pack();
		eventDialog.setVisible(true);
  }

  public void loadFileDialog() {
    JFrame frame = new JFrame("File Browser");

    JFileChooser fileChooser = new JFileChooser(".");
    JButton btnClose = new JButton("Close");
    fileChooser.setControlButtonsAreShown(false);
    frame.getContentPane().add(fileChooser, BorderLayout.CENTER);
    frame.getContentPane().add(btnClose, BorderLayout.SOUTH);
    btnClose.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        frame.dispose();
      }
    });
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
}
