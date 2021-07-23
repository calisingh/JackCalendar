import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * View for the application.
 *
 * @authors Kunwarpreet, Jooyul, Carrisa
 */
public class View implements ChangeListener {
  private LocalDate currentDate;
  private final MyCalendar model;
  private final HashMap<Integer, JButton> daysButtons = new HashMap<>();
  private boolean firstRun;
  private int lastHighlight;

  /* Variables for GUI */
  final int WINDOW_WIDTH = 880;
  final int WINDOW_HEIGHT = 300;
  final int BASE_SPACE = 20;
  final int PANEL_WIDTH = (WINDOW_WIDTH - 3 * BASE_SPACE) / 2;
  final int PANEL_HEIGHT = WINDOW_HEIGHT - 2 * BASE_SPACE;
  final int BTN_SIZE = 25;
  final int CALENDAR_ROW = 6;
  final int CALENDAR_COL = 7;

  private final JPanel monthView;
  private final JTextArea dayView;
  private JLabel monthName = new JLabel("", SwingConstants.CENTER);
  private JLabel yearName = new JLabel("", SwingConstants.CENTER);

  public View(MyCalendar model) {
    /* other instance variables */
    firstRun = true;
    this.model = model;
    lastHighlight = this.getCalendar(model).get(Calendar.DAY_OF_MONTH);
    currentDate = LocalDate.now();
    String today = currentDate.getDayOfWeek().toString() + " " + 
                    currentDate.getMonthValue() + "/" + 
                    currentDate.getDayOfMonth();

    final JFrame frame = new JFrame();
    // panels
    JPanel leftPanel = new JPanel();
    JPanel rightPanel = new JPanel();
    JPanel titlePanel = new JPanel();
    JPanel topRightPanel = new JPanel();
    JPanel bottomRightPanel = new JPanel();
    // labels
    monthView = new JPanel();
    dayView = new JTextArea(10, 33);
    // Buttons
    JButton prevMonthBtn = new JButton("<");
    JButton nextMonthBtn = new JButton(">");
    JButton prevDayBtn = new JButton("<");
    JButton nextDayBtn = new JButton(">");
    JButton todayBtn = new JButton("today");

    JButton dayBtn = new JButton("Day");
    JButton WeekBtn = new JButton("Week");
    JButton monthBtn = new JButton("Month");
    JButton agendaBtn = new JButton("Agenda");
    JButton createEventBtn = new JButton("Create");
    JButton quitBtn = new JButton("Quit");
    JButton fileBtn = new JButton("From File");

    // /* set panels */
    leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));
    leftPanel.setBorder(new EmptyBorder(BASE_SPACE,BASE_SPACE,BASE_SPACE,BASE_SPACE/2));
    rightPanel.setBorder(new EmptyBorder(BASE_SPACE/2,BASE_SPACE/2,BASE_SPACE, BASE_SPACE));
    titlePanel.setLayout(new GridLayout(1, 0));
    titlePanel.setBorder(new EmptyBorder(0, BASE_SPACE, BASE_SPACE/2, BASE_SPACE));
    
    /* set components (JTextArea allows to have multiple lines (multiple events)) */
    monthName.setText(getMonthAbbreviation(currentDate));
    yearName.setText(Integer.toString(currentDate.getYear()));
    dayView.setText(today);
    monthName.setFont(new Font("Arial", Font.BOLD, 20));
    dayView.setEditable(false);
    monthView.setLayout(new GridLayout(7, 7));
    prevMonthBtn.setPreferredSize(new Dimension(20, 30));
    quitBtn.setForeground(Color.RED);
    

    /* Create Buttons and Highlight today */
    showMonthlyCalendar(currentDate);
    highlight(currentDate, lastHighlight);
    lastHighlight = currentDate.getDayOfMonth();

    /* Action Listener for buttons */
    createEventBtn.addActionListener(e -> createEventPopup());

    prevDayBtn.addActionListener(e -> {
      moveDateHandler(-1);
    });

    nextDayBtn.addActionListener(e -> {
      moveDateHandler(1);
    });

    prevMonthBtn.addActionListener(e -> {
        moveMonthHandler(-1);
    });

    nextMonthBtn.addActionListener(e -> {
      moveMonthHandler(1);
    });

    todayBtn.addActionListener(e -> {
        // update current date
        currentDate = LocalDate.now();
        // update title
        monthName.setText(getMonthAbbreviation(currentDate));
        yearName.setText(Integer.toString(currentDate.getYear()));
        // update monthly calendar
        monthView.removeAll();
        showMonthlyCalendar(currentDate);
        model.updateListeners(this.getCalendar(model));
        /* Highlight the date*/
        highlight(currentDate, lastHighlight);
        lastHighlight = currentDate.getDayOfMonth();

        System.out.println(currentDate.toString());
    });

    quitBtn.addActionListener(e -> {
      model.quit();
      Runtime.getRuntime().exit(0);
    });


    // dateAndDetails(this.getCalendar(model).get(Calendar.DAY_OF_MONTH));

    titlePanel.add(monthName);
    titlePanel.add(yearName);
    titlePanel.add(prevMonthBtn);
    titlePanel.add(nextMonthBtn);
    titlePanel.add(todayBtn);
    
    leftPanel.add(titlePanel);
    leftPanel.add(monthView);

    topRightPanel.add(dayBtn);
    topRightPanel.add(WeekBtn);
    topRightPanel.add(monthBtn);
    topRightPanel.add(agendaBtn);
    topRightPanel.add(quitBtn);
    bottomRightPanel.add(prevDayBtn);
    bottomRightPanel.add(createEventBtn);
    bottomRightPanel.add(fileBtn);
    bottomRightPanel.add(nextDayBtn);

    rightPanel.add(topRightPanel);
    rightPanel.add(dayView);
    rightPanel.add(bottomRightPanel);

    frame.add(leftPanel);
    frame.add(rightPanel);

    frame.setLayout(new GridLayout(0, 2));
    frame.setTitle("JACK CALENDAR");
    frame.setLocation(320, 200);;
    frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }


  private void moveDateHandler(int i) {
    GregorianCalendar cal = this.getCalendar(model);
    currentDate = currentDate.plusDays(i);
    cal.add(Calendar.DAY_OF_MONTH, -1);
    model.updateListeners(cal);

    /* First Day of Month */
    if(currentDate.getDayOfMonth() == currentDate.getMonth().length(currentDate.isLeapYear())){
      monthView.removeAll();
      showMonthlyCalendar(currentDate);
    }
    /* Last Day of Month */
    if(currentDate.getDayOfMonth() == 1){
      monthView.removeAll();
      showMonthlyCalendar(currentDate);
    }

    /* Highlight the date*/
    highlight(currentDate, lastHighlight);
    lastHighlight = currentDate.getDayOfMonth();

    System.out.println(currentDate.toString());
  }

  private void moveMonthHandler(int i) {
    // update current date
    currentDate = currentDate.plusMonths(i);
    // update title
    monthName.setText(getMonthAbbreviation(currentDate));
    yearName.setText(Integer.toString(currentDate.getYear()));
    // update monthly calendar
    monthView.removeAll();
    showMonthlyCalendar(currentDate);
    model.updateListeners(this.getCalendar(model));

    /* Highlight the date*/
    highlight(currentDate, lastHighlight);
    lastHighlight = currentDate.getDayOfMonth();

    System.out.println(currentDate.toString());
  }


  /**
   * highligh the given date in the calendar.
   *
   * @param i
   */
  public void highlight(LocalDate c, int lastHighlight) {
    daysButtons.get(lastHighlight).setBorder(UIManager.getBorder("Button.border"));
    daysButtons.get(c.getDayOfMonth()).setBorder(new LineBorder(Color.RED, 2, true));
  }

  private void createEventPopup() {
    JFrame createFrame = new JFrame();
    createFrame.setSize(500, 200);
    createFrame.setTitle("Create New Event");
    JTextField eventName = new JTextField(10);
    JTextField startTime = new JTextField(10);
    JTextField endTime = new JTextField(10);
    JButton saveButton = new JButton("SAVE");
    JLabel to = new JLabel("to");
    saveButton.setSize(50, 50);
    saveButton.addActionListener(e -> {
      if (startTime.getText().isEmpty() && endTime.getText().isEmpty()) {
        JOptionPane.showMessageDialog(null, "Please check your start or end time! It should be in HH:mm format");
      } else if (!model.saveEvents(eventName.getText(), this.getCalendar(model).toZonedDateTime().toLocalDate(),
          LocalTime.parse(startTime.getText(), DateTimeFormatter.ISO_LOCAL_TIME),
          LocalTime.parse(endTime.getText(), DateTimeFormatter.ISO_LOCAL_TIME)).getValue()) {
        JFrame conflictMessage = new JFrame();
        conflictMessage.setLayout(new GridLayout(2, 0));
        JLabel jLabel = new JLabel("Event Time is conflicting! Please try again.");
        conflictMessage.add(jLabel);
        JButton goBack = new JButton("Go Back");
        goBack.addActionListener(e2 -> conflictMessage.dispose());
        conflictMessage.add(goBack);
        conflictMessage.setVisible(true);
        conflictMessage.pack();
      } else {
        createFrame.dispose();
        Event event = new Event(eventName.getText(), this.getCalendar(model).toZonedDateTime().toLocalDate(),
            LocalTime.parse(startTime.getText(), DateTimeFormatter.ISO_LOCAL_TIME),
            LocalTime.parse(endTime.getText(), DateTimeFormatter.ISO_LOCAL_TIME));
        model.updateEvent(event);
      }
    });
    createFrame.setLayout(new FlowLayout());
    createFrame.add(new JLabel("Event"));
    createFrame.add(eventName);
    createFrame.add(startTime);
    createFrame.add(to);
    createFrame.add(endTime);
    createFrame.add(saveButton);
    createFrame.setVisible(true);

  }

  private void dateAndDetails(int o) {
    String date = currentDate.getMonthValue() + "/" + currentDate.getDayOfMonth();

    // String completeDate = ((this.getCalendar(model).get(Calendar.MONTH) + 1) + "/" + o + "/"
    //     + this.getCalendar(model).get(Calendar.YEAR));
    // if((this.getCalendar(model).get(Calendar.MONTH) + 1) < 10) completeDate = "0" + completeDate;
    StringBuilder events = new StringBuilder();
    // LocalDate parsedDate = LocalDate.parse(completeDate, DateTimeFormatter.ofPattern("MM/dd/yyyy"));

    // if (model.getEventMap().containsKey(parsedDate)) {
    //   ArrayList<Event> list = model.getEventMap().get(parsedDate);
      if (model.getEventMap().containsKey(currentDate)) {
        ArrayList<Event> list = model.getEventMap().get(currentDate);
      Collection<Event> nonDuplicateCollection = list.stream()
          .collect(Collectors.toMap(Event::getName, Function.identity(), (a, b) -> a)).values();
      List<Event> list1 = new ArrayList<>(nonDuplicateCollection);
      list1.sort(Comparator.comparing(Event::getStartTime));
      for (Event event : list1) {
        events.append(event.getName()).append(" ").append(event.getStartTime()).append(" ").append(event.getEndTime());
        events.append("\n");
      }
    }
    dayView.setText(currentDate.getDayOfWeek() + " " + date);
    dayView.append("\n");
    dayView.append(events.toString());
  }

  /**
   * return the GregorianCalendar of the current model.
   *
   * @param model
   * @return
   */
  public GregorianCalendar getCalendar(MyCalendar model) {
    return model.getGregorianCalendar();
  }

  /**
   * January Jan. / February Feb. / March Mar. / April Apr. / May May / June Jun.
   * / July Jul. / August Aug. / September Sep. / October Oct. / November Nov. /
   * December Dec.
   * 
   * @param date Local Date
   * @return String of Month Abbreviations
   */
  public String getMonthAbbreviation(LocalDate date) {
    return date.getMonth().toString().substring(0, 3);
  }

  
  public void showMonthlyCalendar(LocalDate c) {
    int totalDaysOfMonth = c.getMonth().length(c.isLeapYear());
    int offset = 0;
    String firstDayOfMonth = LocalDate.of(c.getYear(), c.getMonth(), 1).getDayOfWeek().name();

    // offset depends on the first day of the first week
    if      (firstDayOfMonth == "SUNDAY")     offset = 0;
    else if (firstDayOfMonth == "MONDAY")     offset = 1;
    else if (firstDayOfMonth == "TUESDAY")    offset = 2;
    else if (firstDayOfMonth == "WEDNESDAY")  offset = 3;
    else if (firstDayOfMonth == "THURSDAY")   offset = 4;
    else if (firstDayOfMonth == "FRIDAY")     offset = 5;
    else if (firstDayOfMonth == "SATURDAY")   offset = 6;
    else 
      throw new IllegalArgumentException("Invalid day of week");

    monthView.setLayout(new GridLayout(7, 7));
    
    JLabel sunLabel = new JLabel("S", SwingConstants.CENTER);
    JLabel monLabel = new JLabel("M", SwingConstants.CENTER);
    JLabel tueLabel = new JLabel("T", SwingConstants.CENTER);
    JLabel wedLabel = new JLabel("W", SwingConstants.CENTER);
    JLabel thuLabel = new JLabel("T", SwingConstants.CENTER);
    JLabel friLabel = new JLabel("F", SwingConstants.CENTER);
    JLabel satLabel = new JLabel("S", SwingConstants.CENTER);
    sunLabel.setForeground(Color.red);
    sunLabel.setFont(new Font("Arial", Font.BOLD, 14));
    monLabel.setFont(new Font("Arial", Font.BOLD, 14));
    tueLabel.setFont(new Font("Arial", Font.BOLD, 14));
    wedLabel.setFont(new Font("Arial", Font.BOLD, 14));
    thuLabel.setFont(new Font("Arial", Font.BOLD, 14));
    friLabel.setFont(new Font("Arial", Font.BOLD, 14));
    satLabel.setFont(new Font("Arial", Font.BOLD, 14));

    monthView.add(sunLabel);
    monthView.add(monLabel);
    monthView.add(tueLabel);
    monthView.add(wedLabel);
    monthView.add(thuLabel);
    monthView.add(friLabel);
    monthView.add(satLabel);

    for (int i = 0; i < offset; i++) {
      JButton dateBtn = new JButton();
      dateBtn.setEnabled(false);
      monthView.add(dateBtn);
    }
    /* Add Date Buttons */
    for (int i = 1; i <= totalDaysOfMonth; i++) {
      JButton dateBtn = new JButton(Integer.toString(i));
      monthView.add(dateBtn);

      /* Date Button Listener */
      dateBtn.addActionListener(e -> {
        /* Show content in day view */
          int date = Integer.parseInt(dateBtn.getText());

          /* Update current date */
          currentDate = LocalDate.of(c.getYear(), c.getMonth(), date);

          /* update content of the date */
          dateAndDetails(date);

          /* Highlight the date*/
          highlight(currentDate, lastHighlight);
          lastHighlight = date;

          System.out.println(currentDate.toString());
      });
      daysButtons.put(i, dateBtn);
    }

    int remainingDays = CALENDAR_ROW * CALENDAR_COL - offset - totalDaysOfMonth;
    for (int i = 0; i < remainingDays; i++) {
      JButton dateBtn = new JButton();
      dateBtn.setEnabled(false);
      monthView.add(dateBtn);
    }
  }

  @Override
  public void stateChanged(ChangeEvent e) {
    // monthName.setText(Util.getMonths().get(this.getCalendar(model).get(Calendar.MONTH)) + " "
    //     + this.getCalendar(model).get(Calendar.YEAR));
    dateAndDetails(this.getCalendar(model).get(Calendar.DAY_OF_MONTH));
  }
}
