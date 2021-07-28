import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.io.FileNotFoundException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * View for the application.
 *
 * @authors Kunwarpreet, Jooyul, Carissa
 */
public class View implements ChangeListener {
  private LocalDate currentDate;
  private final MyCalendar model;
  private final HashMap<Integer, JButton> daysButtons = new HashMap<>();
  private int lastHighlight;
  private char viewStatus;
  private List<LocalDate> daysToShow;

  /* Variables for GUI */
  final int WINDOW_WIDTH = 900;
  final int WINDOW_HEIGHT = 300;
  final int BASE_SPACE = 20;
  final int PANEL_WIDTH = (WINDOW_WIDTH - 3 * BASE_SPACE) / 2;
  final int PANEL_HEIGHT = WINDOW_HEIGHT - 2 * BASE_SPACE;
  final int BTN_WIDTH = 79;
  final int BTN_HEIGHT = 20;
  final int CALENDAR_ROW = 6;
  final int CALENDAR_COL = 7;

  private final JPanel monthlyCalendarPanel;
  private final JTextArea contentText;
  private JLabel monthName = new JLabel("", SwingConstants.CENTER);
  private JLabel yearName = new JLabel("", SwingConstants.CENTER);
  JButton dayBtn = new JButton("Day");
  JButton weekBtn = new JButton("Week");
  JButton monthBtn = new JButton("Month");
  JButton agendaBtn = new JButton("Agenda");

  /**
   * Constructor
   */
  public View(MyCalendar model) {

    /* Initialize Default Variables */
    this.model = model;
    lastHighlight = this.getCalendar(model).get(Calendar.DAY_OF_MONTH);
    currentDate = LocalDate.now();
    daysToShow = new ArrayList<>();
    daysToShow.add(currentDate);
    String today = currentDate.getDayOfWeek().toString() + "   " + 
                    currentDate.getMonthValue() + "/" + 
                    currentDate.getDayOfMonth();
    viewStatus = 'd'; // Default View Status: day view 

    /** Initialize GUI components **/
    final JFrame frame = new JFrame();
    /* panels */
    JPanel leftPanel = new JPanel();
    JPanel rightPanel = new JPanel();
    JPanel topLeftPanel = new JPanel();
    JPanel topRightPanel = new JPanel();
    JPanel bottomRightPanel = new JPanel();
    /* labels */
    monthlyCalendarPanel = new JPanel();
    contentText = new JTextArea(10, 33);
    JScrollPane scrollPane = new JScrollPane(contentText);
    /* Buttons */
    JButton prevMonthBtn = new JButton("<");
    JButton nextMonthBtn = new JButton(">");
    JButton prevDayBtn = new JButton("<");
    JButton nextDayBtn = new JButton(">");
    JButton todayBtn = new JButton("today");
    JButton createEventBtn = new JButton("Create");
    JButton quitBtn = new JButton("Quit");
    JButton fileBtn = new JButton("Load File");

    /* set up panels */
    leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));
    leftPanel.setBorder(new EmptyBorder(BASE_SPACE,BASE_SPACE,BASE_SPACE,BASE_SPACE/2));
    rightPanel.setLayout(new BorderLayout());
    rightPanel.setBorder(new EmptyBorder(BASE_SPACE/2,BASE_SPACE/2,BASE_SPACE, BASE_SPACE));
    topLeftPanel.setLayout(new GridLayout(1, 0));
    topLeftPanel.setBorder(new EmptyBorder(0, BASE_SPACE, BASE_SPACE/2, BASE_SPACE));
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    
    /* set up components (JTextArea allows to have multiple lines (multiple events)) */
    monthName.setText(getMonthAbbreviation(currentDate));
    yearName.setText(Integer.toString(currentDate.getYear()));
    contentText.setText(today);
    monthName.setFont(new Font("Arial", Font.BOLD, 20));
    contentText.setEditable(false);
    monthlyCalendarPanel.setLayout(new GridLayout(7, 7));
    prevMonthBtn.setPreferredSize(new Dimension(20, 30));
    dayBtn.setPreferredSize(new Dimension(BTN_WIDTH, BTN_HEIGHT));
    weekBtn.setPreferredSize(new Dimension(BTN_WIDTH, BTN_HEIGHT));
    monthBtn.setPreferredSize(new Dimension(BTN_WIDTH, BTN_HEIGHT));
    agendaBtn.setPreferredSize(new Dimension(BTN_WIDTH, BTN_HEIGHT));
    quitBtn.setPreferredSize(new Dimension(BTN_WIDTH, BTN_HEIGHT));
    prevDayBtn.setPreferredSize(new Dimension(BTN_WIDTH, BTN_HEIGHT));
    nextDayBtn.setPreferredSize(new Dimension(BTN_WIDTH, BTN_HEIGHT));
    createEventBtn.setPreferredSize(new Dimension(BTN_WIDTH, BTN_HEIGHT));
    fileBtn.setPreferredSize(new Dimension(BTN_WIDTH, BTN_HEIGHT));
    
    quitBtn.setForeground(Color.RED);
    
    /* Create Buttons and Highlight today */
    showMonthlyCalendar(currentDate);
    highlightBtn(dayBtn);
    highlight(currentDate, lastHighlight);
    lastHighlight = currentDate.getDayOfMonth();

    /* Action Listener for Buttons */
    prevDayBtn.addActionListener(e -> updateAndHighlightCurrentDate(currentDate.minusDays(1)));

    nextDayBtn.addActionListener(e -> updateAndHighlightCurrentDate(currentDate.plusDays(1)));

    prevMonthBtn.addActionListener(e -> updateAndHighlightCurrentDate(currentDate.minusMonths(1)));

    nextMonthBtn.addActionListener(e -> updateAndHighlightCurrentDate(currentDate.plusMonths(1)));

    todayBtn.addActionListener(e -> updateAndHighlightCurrentDate(LocalDate.now()));

    dayBtn.addActionListener(e -> dayViewHandler());

    weekBtn.addActionListener(e -> weekViewHandler());

    monthBtn.addActionListener(e -> monthViewHandler());

    agendaBtn.addActionListener(e -> agendaViewHandler());

    createEventBtn.addActionListener(e -> createEventPopup());

    fileBtn.addActionListener(e -> loadFileHandler());

    quitBtn.addActionListener(e -> frame.dispose());

    /* Add components to containers */
    topLeftPanel.add(monthName);
    topLeftPanel.add(yearName);
    topLeftPanel.add(prevMonthBtn);
    topLeftPanel.add(nextMonthBtn);
    topLeftPanel.add(todayBtn);
    
    topRightPanel.add(dayBtn);
    topRightPanel.add(weekBtn);
    topRightPanel.add(monthBtn);
    topRightPanel.add(agendaBtn);
    topRightPanel.add(quitBtn);

    bottomRightPanel.add(prevDayBtn);
    bottomRightPanel.add(createEventBtn);
    bottomRightPanel.add(fileBtn);
    bottomRightPanel.add(nextDayBtn);

    leftPanel.add(topLeftPanel);
    leftPanel.add(monthlyCalendarPanel);

    rightPanel.add(topRightPanel, BorderLayout.NORTH);
    rightPanel.add(scrollPane, BorderLayout.CENTER);
    rightPanel.add(bottomRightPanel, BorderLayout.SOUTH);

    frame.add(leftPanel);
    frame.add(rightPanel);

    frame.setLayout(new GridLayout(0, 2));
    frame.setTitle("JACK CALENDAR");
    frame.setLocation(320, 200);;
    frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }

  private void updateAndHighlightCurrentDate(LocalDate dateToUpdate) {
    /* Update Current Date */
    currentDate = dateToUpdate;

    /* Update Title */
    monthName.setText(getMonthAbbreviation(dateToUpdate));
    yearName.setText(Integer.toString(dateToUpdate.getYear())); 

    /* Update Monthly Calendar */
    monthlyCalendarPanel.removeAll();
    showMonthlyCalendar(dateToUpdate);

    /* Highlight the date*/
    highlight(dateToUpdate, lastHighlight);
    lastHighlight = dateToUpdate.getDayOfMonth();

    switch(viewStatus) {
      case 'd': dayViewHandler(); break;
      case 'w': weekViewHandler(); break;
      case 'm': monthViewHandler(); break;
      case 'a': dayViewHandler(); break;
      default: break;
    }
  }

  private void showSchedule() {
    contentText.setText("");
    switch(viewStatus) {
      case 'd':{
        String dateStr = currentDate.getDayOfWeek().toString() + "   " + 
                      currentDate.getMonthValue() + "/" + 
                      currentDate.getDayOfMonth();
        StringBuilder events = new StringBuilder();
      
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
        contentText.append(dateStr);
        contentText.append("\n\n");
        contentText.append(events.toString());
        break;
      }
      case 'w':
      case 'm':
      case 'a':{
        String startDateStr = daysToShow.get(0).toString();
        String endDateStr = daysToShow.get(daysToShow.size() - 1).toString();
        contentText.append("[" + startDateStr + "]   ~   [" + endDateStr + "]\n\n");

        for(LocalDate dates: daysToShow) {
          StringBuilder events = new StringBuilder();
          String date = dates.getDayOfWeek().toString().substring(0,3) + "   " + 
                        dates.getMonthValue() + "/" + dates.getDayOfMonth();
          
          if (model.getEventMap().containsKey(dates)) {
            ArrayList<Event> list = model.getEventMap().get(dates);
            Collection<Event> nonDuplicateCollection = list.stream()
                .collect(Collectors.toMap(Event::getName, Function.identity(), (a, b) -> a)).values();
            List<Event> list1 = new ArrayList<>(nonDuplicateCollection);
            list1.sort(Comparator.comparing(Event::getStartTime));
            for (Event event : list1) {
              events.append(" | " + event.getName());
            }
            contentText.append(date);
            contentText.append(" :  ");
            contentText.append(events.toString() + " |");
            contentText.append("\n");
          }
        }
        break;
      }
      default:{
        contentText.setText("ERROR: Unknown View Selector");
        break;
      }
    }
  }

  /**
   * It switches the view selection mode to 'day view', then update the content
   */
  private void dayViewHandler() {
    viewStatus = 'd';
    daysToShow.clear();
    daysToShow = new ArrayList<>();
    daysToShow.add(currentDate);
    highlightBtn(dayBtn);
    
    showSchedule();
  }

  /**
   * This switches the view selection mode to 'week view', then update the content
   */
  private void weekViewHandler() {
    viewStatus = 'w';
    LocalDate startDate = currentDate.with(WeekFields.of(Locale.US).dayOfWeek(), 1L);
    LocalDate lastDate = currentDate.with(WeekFields.of(Locale.US).dayOfWeek(), 7L);
    daysToShow = startDate.datesUntil(lastDate.plusDays(1)).collect(Collectors.toList());
    highlightBtn(weekBtn);
    
    showSchedule();
  }

  /**
   * This switches the view selection mode to 'month view', then update the content
   */
  private void monthViewHandler() {
    viewStatus = 'm';
    int currentYear = currentDate.getYear();
    Month currentMonth = currentDate.getMonth();
    int lastDateOfMonth = currentMonth.length(currentDate.isLeapYear());
    LocalDate startDate = LocalDate.of(currentYear, currentMonth, 1);
    LocalDate lastDate = LocalDate.of(currentYear, currentMonth, lastDateOfMonth);
    daysToShow = startDate.datesUntil(lastDate.plusDays(1)).collect(Collectors.toList());
    highlightBtn(monthBtn);

    showSchedule();
  }

  /**
   * This switches the view selection mode to 'agenda view', then update the content
   * This also pop ups a new frame to set period of time to search
   */
  private void agendaViewHandler() {
    viewStatus = 'a';

    JFrame agendaFrame = new JFrame();
    JPanel agendaPanel = new JPanel();
    JTextField startYearField = new JTextField(10);
    JTextField startMonthField = new JTextField(10);
    JTextField startDateField = new JTextField(10);
    JTextField endYearField = new JTextField(10);
    JTextField endMonthField = new JTextField(10);
    JTextField endDateField = new JTextField(10);
    JButton submitBtn = new JButton("Submit");

    submitBtn.addActionListener(e -> {
      try {
        int startYear = Integer.parseInt(startYearField.getText());
        int startMonth = Integer.parseInt(startMonthField.getText());
        int startDate = Integer.parseInt(startDateField.getText());
        int endYear = Integer.parseInt(endYearField.getText());
        int endMonth = Integer.parseInt(endMonthField.getText());
        int endDate = Integer.parseInt(endDateField.getText());
        LocalDate startLocalDate = LocalDate.of(startYear, startMonth, startDate);
        LocalDate endLocalDate = LocalDate.of(endYear, endMonth, endDate);

        /* Start Date should be Earlier than End Date */
        if(startLocalDate.isAfter(endLocalDate)) {
          JOptionPane.showMessageDialog(null, "start date should be after end date");
        } 
        else {
          daysToShow = startLocalDate.datesUntil(endLocalDate.plusDays(1)).collect(Collectors.toList());
          highlightBtn(agendaBtn);
          showSchedule();
          agendaFrame.dispose();
        }
    } catch (NumberFormatException nfe) {
      JOptionPane.showMessageDialog(null, "Please type numeric value");
    } catch (DateTimeException dte) {
      JOptionPane.showMessageDialog(null, "Please type proper input of date formmat");
    }
    });

    agendaPanel.add(new JLabel("Year", SwingConstants.CENTER));
    agendaPanel.add(new JLabel("Month", SwingConstants.CENTER));
    agendaPanel.add(new JLabel("Date", SwingConstants.CENTER));
    agendaPanel.add(startYearField);
    agendaPanel.add(startMonthField);
    agendaPanel.add(startDateField);
    agendaPanel.add(endYearField);
    agendaPanel.add(endMonthField);
    agendaPanel.add(endDateField);
    agendaPanel.add(submitBtn);

    agendaFrame.add(agendaPanel);

    agendaPanel.setBorder(new EmptyBorder(BASE_SPACE, BASE_SPACE, BASE_SPACE, BASE_SPACE));
    agendaPanel.setLayout(new GridLayout(4, 3));

    agendaFrame.setTitle("Load Events From File");
    agendaFrame.setSize(500, 200);
    agendaFrame.setLocation(500, 250);
    agendaFrame.setVisible(true);
  }

  private void createEventPopup() {
    JFrame createFrame = new JFrame();
    JPanel createPanel = new JPanel();
    JTextField eventName = new JTextField(10);
    JTextField startTime = new JTextField(10);
    JTextField endTime = new JTextField(10);
    JButton saveBtn = new JButton("SAVE");
    // saveBtn.setSize(50, 50);
    
    saveBtn.addActionListener(e -> {
      String eventNameStr = eventName.getText();
      String startTimeStr = startTime.getText();
      String endTimeStr = endTime.getText();
      String startTimeInLocalTime = startTimeStr + ":00";
      String endTimeInLocalTime = endTimeStr + ":00";

      /* Check if the event name is empty */
      if (eventNameStr.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Please check the name of the event\n It should not be empty");
      }
      /* check if the event time is empty */
      else if (startTimeStr.isEmpty() || endTimeStr.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Please check your start or end time\n They should not be empty");
      } 
      /* check if the time inputs are integers */
      else if (!isNumeric(startTimeStr) || !isNumeric(endTimeStr)) {
        JOptionPane.showMessageDialog(null, "Please check your start or end time\n They should be a numeric value");
      }
      /* check if the time inputs are numbers between 0 and 23 */
      else if( Integer.parseInt(startTimeStr) < 0 || Integer.parseInt(startTimeStr) > 23 || Integer.parseInt(endTimeStr) < 0 || Integer.parseInt(endTimeStr) > 23) {
          JOptionPane.showMessageDialog(null, "Please check your start or end time\n They should be between 0 and 23");
      }
      /* check if the start time is greater than the end time */
      else if( Integer.parseInt(startTimeStr) > Integer.parseInt(endTimeStr)) {
        JOptionPane.showMessageDialog(null, "Please check your start or end time\n start time shoule be greater than end time");
      }
      else {
        if(Integer.parseInt(startTimeStr) < 10)
          startTimeInLocalTime = "0" + startTimeInLocalTime;
        if(Integer.parseInt(endTimeStr) < 10)
          endTimeInLocalTime = "0" + endTimeInLocalTime;

        /* check if time conflicts with another event */
        if (!model.saveEvents(eventNameStr, this.getCalendar(model).toZonedDateTime().toLocalDate(),
            LocalTime.parse(startTimeInLocalTime, DateTimeFormatter.ISO_LOCAL_TIME),
            LocalTime.parse(endTimeInLocalTime, DateTimeFormatter.ISO_LOCAL_TIME)).getValue()) {
          JFrame conflictMessage = new JFrame();
          conflictMessage.setLayout(new GridLayout(2, 0));
          JLabel jLabel = new JLabel("Event Time is conflicting! Please try again.");
          conflictMessage.add(jLabel);
          JButton goBack = new JButton("Go Back");
          goBack.addActionListener(e2 -> conflictMessage.dispose());
          conflictMessage.add(goBack);
          conflictMessage.setVisible(true);
          conflictMessage.pack();
        } 

        /* If Successful, Create Event */
        else {
          createFrame.dispose();
          Event event = new Event(eventNameStr, currentDate,
              LocalTime.parse(startTimeInLocalTime, DateTimeFormatter.ISO_LOCAL_TIME),
              LocalTime.parse(endTimeInLocalTime, DateTimeFormatter.ISO_LOCAL_TIME));
          model.updateEvent(currentDate, event);
        }
      }
    });

    createPanel.add(new JLabel("Event\t\t"));
    createPanel.add(eventName);
    createPanel.add(new JLabel("Start Hour (0 ~ 23)"));
    createPanel.add(startTime);
    createPanel.add(new JLabel("End Hour (0 ~ 23)"));
    createPanel.add(endTime);
    createPanel.add(saveBtn);

    createPanel.setLayout(new BoxLayout(createPanel, BoxLayout.PAGE_AXIS));
    createPanel.setBorder(new EmptyBorder(BASE_SPACE, BASE_SPACE, BASE_SPACE, BASE_SPACE));
    createFrame.add(createPanel);

    createFrame.setTitle("Create New Event");
    createFrame.setSize(250, 220);
    createFrame.setLocation(650, 250);
    createFrame.setVisible(true);

  }

  private void loadFileHandler() {
    JFrame loadFrame = new JFrame();
    JLabel label = new JLabel("File Path");
    JTextField fileNameField = new JTextField(10);
    JButton loadBtn = new JButton("Load");

    loadBtn.addActionListener(e -> {
      String fileName = "JackCalendar/data/" + fileNameField.getText();

      try {
        model.loadAndUpdateEvents(fileName);
        loadFrame.dispose();
      }catch (FileNotFoundException fnf) {
        JFrame fnfMessage = new JFrame();
        JLabel jLabel = new JLabel("The File is not Found");
        JButton goBack = new JButton("Go Back");
        goBack.addActionListener(e2 -> fnfMessage.dispose());

        fnfMessage.add(jLabel);
        fnfMessage.add(goBack);
        fnfMessage.setLayout(new GridLayout(2, 0));
        fnfMessage.setLocation(700, 320);
        fnfMessage.setVisible(true);
        fnfMessage.pack();
      }

      fileNameField.setText("");
    });

    loadFrame.add(label);
    loadFrame.add(fileNameField);
    loadFrame.add(loadBtn);
    loadFrame.add(new JLabel("(hint: input.txt)"));

    loadFrame.setTitle("Load Events From File");
    loadFrame.setSize(500, 200);
    loadFrame.setLocation(500, 250);
    loadFrame.setLayout(new FlowLayout());
    loadFrame.setVisible(true);
  }
  
  private void highlightBtn(JButton btn) {
    dayBtn.setBorder(UIManager.getBorder("Button.border"));
    weekBtn.setBorder(UIManager.getBorder("Button.border"));
    monthBtn.setBorder(UIManager.getBorder("Button.border"));
    agendaBtn.setBorder(UIManager.getBorder("Button.border"));
    btn.setBorder(new LineBorder(Color.pink, 2, true));
  }

  /**
   * highligh the given date in the calendar.
   *
   * @param i
   */
  public void highlight(LocalDate c, int lastHighlight) {
    daysButtons.get(lastHighlight).setBorder(UIManager.getBorder("Button.border"));
    daysButtons.get(c.getDayOfMonth()).setBorder(new LineBorder(Color.pink, 3, true));
  }

  /**
   * return the GregorianCalendar of the current model.
   * @param model
   * @return
   */
  private GregorianCalendar getCalendar(MyCalendar model) {
    return model.getGregorianCalendar();
  }

  /**
   * January - Jan. / February - Feb. / March - Mar. / April - Apr. / 
   * May - May / June - Jun. / July - Jul. / August - Aug. / 
   * September - Sep. / October - Oct. / November - Nov. / December - Dec.
   * @param date Local Date
   * @return String of Month Abbreviations
   */
  private String getMonthAbbreviation(LocalDate date) {
    return date.getMonth().toString().substring(0, 3);
  }

  /**
   * This function adds JButton, JLabel Components to the JPanel, monthView 
   * and shows month view corresponding to the LocalDate in parameter.
   * @param c LocalDate
   */
  private void showMonthlyCalendar(LocalDate c) {
    int totalDaysOfMonth = c.getMonth().length(c.isLeapYear());
    int offset = 0;
    String firstDayOfMonth = LocalDate.of(c.getYear(), c.getMonth(), 1).getDayOfWeek().name();

    /* offset depends on the first day of the first week */
    if      (firstDayOfMonth == "SUNDAY")     offset = 0;
    else if (firstDayOfMonth == "MONDAY")     offset = 1;
    else if (firstDayOfMonth == "TUESDAY")    offset = 2;
    else if (firstDayOfMonth == "WEDNESDAY")  offset = 3;
    else if (firstDayOfMonth == "THURSDAY")   offset = 4;
    else if (firstDayOfMonth == "FRIDAY")     offset = 5;
    else if (firstDayOfMonth == "SATURDAY")   offset = 6;
    else 
      throw new IllegalArgumentException("Invalid day of week");

    monthlyCalendarPanel.setLayout(new GridLayout(7, 7));
    
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

    monthlyCalendarPanel.add(sunLabel);
    monthlyCalendarPanel.add(monLabel);
    monthlyCalendarPanel.add(tueLabel);
    monthlyCalendarPanel.add(wedLabel);
    monthlyCalendarPanel.add(thuLabel);
    monthlyCalendarPanel.add(friLabel);
    monthlyCalendarPanel.add(satLabel);

    for (int i = 0; i < offset; i++) {
      JButton dateBtn = new JButton();
      dateBtn.setEnabled(false);
      monthlyCalendarPanel.add(dateBtn);
    }
    /* Add Date Buttons */
    for (int i = 1; i <= totalDaysOfMonth; i++) {
      JButton dateBtn = new JButton(Integer.toString(i));
      monthlyCalendarPanel.add(dateBtn);

      /* Date Button Listener */
      dateBtn.addActionListener(e -> {
        /* Show content in day view */
          int date = Integer.parseInt(dateBtn.getText());

          /* Update current date */
          currentDate = LocalDate.of(c.getYear(), c.getMonth(), date);
          // currentDateToString(currentDate);

          /* update content of the date */
          switch(viewStatus) {
            case 'd': dayViewHandler(); break;
            case 'w': weekViewHandler(); break;
            case 'm': monthViewHandler(); break;
            case 'a': dayViewHandler(); break;
            default: break;
          }

          /* Highlight the date*/
          highlight(currentDate, lastHighlight);
          lastHighlight = date;
      });
      daysButtons.put(i, dateBtn);
    }

    int remainingDays = CALENDAR_ROW * CALENDAR_COL - offset - totalDaysOfMonth;
    for (int i = 0; i < remainingDays; i++) {
      JButton dateBtn = new JButton();
      dateBtn.setEnabled(false);
      monthlyCalendarPanel.add(dateBtn);
    }
  }

  private static boolean isNumeric(String str) {
    try {  
      Integer.parseInt(str);  
      return true;
    } catch(NumberFormatException e){  
      return false;  
    }    
  }

  /**
   * When Calendar Model is modified, hence created or loaded from file,
   * The content textarea will show day view only.
   */
  @Override
  public void stateChanged(ChangeEvent e) {
    dayViewHandler();
  }
}
