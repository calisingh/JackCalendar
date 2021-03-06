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
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * View for the application - containg GUI components (ie. Jpanel, JTextArea,
 * JLabel, etc.)
 *
 * @authors Kunwarpreet, Jooyul, Carissa
 */
public class View implements ChangeListener {
  // Instance Variables
  private final MyCalendar model;
  private LocalDate currentDate;
  private List<LocalDate> daysToShow;
  private int lastHighlighted;
  private ViewStrategy viewStrategy;

  // Variables for the GUI
  private final JPanel monthlyCalendarPanel;
  private final JTextArea contentText;
  private JLabel monthName = new JLabel("", SwingConstants.CENTER);
  private JLabel yearName = new JLabel("", SwingConstants.CENTER);
  private final HashMap<Integer, JButton> daysButtons = new HashMap<>();
  private JButton dayBtn = new JButton("Day");
  private JButton weekBtn = new JButton("Week");
  private JButton monthBtn = new JButton("Month");
  private JButton agendaBtn = new JButton("Agenda");

  // Variables to initialize for the dimensions
  final int WINDOW_WIDTH = 910;
  final int WINDOW_HEIGHT = 320;
  final int BASE_SPACE = 20;
  final int PANEL_WIDTH = (WINDOW_WIDTH - 3 * BASE_SPACE) / 2;
  final int PANEL_HEIGHT = WINDOW_HEIGHT - 2 * BASE_SPACE;
  final int BTN_WIDTH = 80;
  final int BTN_HEIGHT = 25;
  final int CALENDAR_ROW = 6;
  final int CALENDAR_COL = 7;

  // View Constructor
  public View(MyCalendar model) {

    // Initializes the variables
    this.model = model;
    lastHighlighted = model.getGregorianCalendar().get(Calendar.DAY_OF_MONTH);
    currentDate = LocalDate.now();
    daysToShow = new ArrayList<>();
    daysToShow.add(currentDate);
    String today = currentDate.getDayOfWeek().toString() + "   " + currentDate.getMonthValue() + "/"
        + currentDate.getDayOfMonth();
    viewStrategy = new DayViewStrategy(); // Default View Status: day view

    // Initializes GUI components
    final JFrame frame = new JFrame();
    // Panels for the Calendar GUI
    JPanel leftPanel = new JPanel();
    JPanel rightPanel = new JPanel();
    JPanel topLeftPanel = new JPanel();
    JPanel topRightPanel = new JPanel();
    JPanel bottomRightPanel = new JPanel();

    // Labels on the Calendar GUI
    monthlyCalendarPanel = new JPanel();
    contentText = new JTextArea(10, 33);
    JScrollPane scrollPane = new JScrollPane(contentText);

    // Buttons on the Calendar GUI
    JButton prevMonthBtn = new JButton("<");
    JButton nextMonthBtn = new JButton(">");
    JButton prevDayBtn = new JButton("<");
    JButton nextDayBtn = new JButton(">");
    JButton todayBtn = new JButton("today");
    JButton createEventBtn = new JButton("Create");
    JButton quitBtn = new JButton("Quit");
    JButton fileBtn = new JButton("Load File");

    // The following sets up panels - such as their space, scroll bars (both
    // vertical and horizontal), width, and grid layout for the actual calendar
    leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));
    leftPanel.setBorder(new EmptyBorder(BASE_SPACE, BASE_SPACE, BASE_SPACE, BASE_SPACE / 2));
    rightPanel.setLayout(new BorderLayout());
    rightPanel.setBorder(new EmptyBorder(BASE_SPACE / 2, BASE_SPACE / 2, BASE_SPACE, BASE_SPACE));
    topLeftPanel.setLayout(new GridLayout(1, 0));
    topLeftPanel.setBorder(new EmptyBorder(0, BASE_SPACE, BASE_SPACE / 2, BASE_SPACE));
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    monthlyCalendarPanel.setLayout(new GridLayout(7, 7));

    // This sets up the components to show the events, both when reading in from
    // input text and displaying the current events that fall on that day, week,
    // month, or year
    monthName.setText(getMonthAbbreviation(currentDate));
    yearName.setText(Integer.toString(currentDate.getYear()));
    contentText.setText(today);
    monthName.setFont(new Font("Arial", Font.BOLD, 20));
    contentText.setEditable(false);
    prevMonthBtn.setPreferredSize(new Dimension(20, 30));
    dayBtn.setPreferredSize(new Dimension(BTN_WIDTH, BTN_HEIGHT));
    weekBtn.setPreferredSize(new Dimension(BTN_WIDTH, BTN_HEIGHT));
    monthBtn.setPreferredSize(new Dimension(BTN_WIDTH, BTN_HEIGHT));
    agendaBtn.setPreferredSize(new Dimension(BTN_WIDTH, BTN_HEIGHT));
    quitBtn.setPreferredSize(new Dimension(BTN_WIDTH, BTN_HEIGHT));
    prevDayBtn.setPreferredSize(new Dimension(BTN_WIDTH, BTN_HEIGHT));
    nextDayBtn.setPreferredSize(new Dimension(BTN_WIDTH, BTN_HEIGHT));
    prevMonthBtn.setPreferredSize(new Dimension(BTN_WIDTH, BTN_HEIGHT));
    nextMonthBtn.setPreferredSize(new Dimension(BTN_WIDTH, BTN_HEIGHT));
    todayBtn.setPreferredSize(new Dimension(BTN_WIDTH, BTN_HEIGHT));
    createEventBtn.setPreferredSize(new Dimension(BTN_WIDTH, BTN_HEIGHT));
    fileBtn.setPreferredSize(new Dimension(BTN_WIDTH, BTN_HEIGHT));

    quitBtn.setForeground(Color.RED);

    // Creates button and highlights the current date
    showMonthlyCalendar(currentDate);
    highlightViewBtn(dayBtn);
    highlightDaysBtn(currentDate, lastHighlighted);
    lastHighlighted = currentDate.getDayOfMonth();

    // These are the action listeners for the buttons to streamline the navigation
    // process
    prevDayBtn.addActionListener(e -> updateAndHighlightCurrentDate(currentDate.minusDays(1)));
    nextDayBtn.addActionListener(e -> updateAndHighlightCurrentDate(currentDate.plusDays(1)));
    prevMonthBtn.addActionListener(e -> updateAndHighlightCurrentDate(currentDate.minusMonths(1)));
    nextMonthBtn.addActionListener(e -> updateAndHighlightCurrentDate(currentDate.plusMonths(1)));
    todayBtn.addActionListener(e -> updateAndHighlightCurrentDate(LocalDate.now()));
    dayBtn.addActionListener(e -> contentsBtnHandler(new DayViewStrategy()));
    weekBtn.addActionListener(e -> contentsBtnHandler(new WeekViewStrategy()));
    monthBtn.addActionListener(e -> contentsBtnHandler(new MonthViewStrategy()));
    agendaBtn.addActionListener(e -> contentsBtnHandler(new AgendaViewStrategy()));
    createEventBtn.addActionListener(e -> createEventPopup());
    fileBtn.addActionListener(e -> loadFileHandler());
    quitBtn.addActionListener(e -> frame.dispose());

    // Adds components to containers
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
    frame.setLocation(320, 200);
    ;
    frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }

  /**
   * Updates current date to the date passed through the parameter
   * 
   * @param dateToUpdate LocalDate
   */
  private void updateAndHighlightCurrentDate(LocalDate dateToUpdate) {
    // Update Current Local Date
    currentDate = dateToUpdate;

    // Updates Title
    monthName.setText(getMonthAbbreviation(dateToUpdate));
    yearName.setText(Integer.toString(dateToUpdate.getYear()));

    // Updates Monthly Calendar
    monthlyCalendarPanel.removeAll();
    showMonthlyCalendar(dateToUpdate);

    // Highlights the date
    highlightDaysBtn(dateToUpdate, lastHighlighted);
    lastHighlighted = dateToUpdate.getDayOfMonth();

    switch (viewStrategy.getStrategy()) {
      case 'd':
        contentsBtnHandler(new DayViewStrategy());
        break;
      case 'w':
        contentsBtnHandler(new WeekViewStrategy());
        break;
      case 'm':
        contentsBtnHandler(new MonthViewStrategy());
        break;
      case 'a':
        contentsBtnHandler(new AgendaViewStrategy());
        break;
      default:
        break;
    }
  }

  /**
   * Shows the schedule based on the view selection status.
   */
  private void showSchedule() {
    // Clears Text Area
    contentText.setText("");
    switch (viewStrategy.getStrategy()) {
      // Case d - shows Day View
      case 'd': {
        String dateStr = currentDate.getDayOfWeek().toString() + "   " + currentDate.getMonthValue() + "/"
            + currentDate.getDayOfMonth();
        StringBuilder events = new StringBuilder();

        if (model.getEventMap().containsKey(currentDate)) {
          ArrayList<Event> list = model.getEventMap().get(currentDate);
          Collection<Event> nonDuplicateCollection = list.stream()
              .collect(Collectors.toMap(Event::getName, Function.identity(), (a, b) -> a)).values();
          List<Event> list1 = new ArrayList<>(nonDuplicateCollection);
          list1.sort(Comparator.comparing(Event::getStartTime));
          for (Event event : list1) {
            events.append(event.getName()).append(" ").append(event.getStartTime()).append(" ")
                .append(event.getEndTime());
            events.append("\n");
          }
        }
        // Sets new text
        contentText.append(dateStr);
        contentText.append("\n\n");
        contentText.append(events.toString());
        break;
      }
      // Shows Week View, Month View, Agenda View
      case 'w':
      case 'm':
      case 'a': {
        String startDateStr = daysToShow.get(0).toString();
        String endDateStr = daysToShow.get(daysToShow.size() - 1).toString();
        contentText.append("[" + startDateStr + "]   ~   [" + endDateStr + "]\n\n");

        for (LocalDate dates : daysToShow) {
          StringBuilder events = new StringBuilder();
          String date = dates.getDayOfWeek().toString().substring(0, 3) + "   " + dates.getMonthValue() + "/"
              + dates.getDayOfMonth();

          if (model.getEventMap().containsKey(dates)) {
            ArrayList<Event> list = model.getEventMap().get(dates);
            Collection<Event> nonDuplicateCollection = list.stream()
                .collect(Collectors.toMap(Event::getName, Function.identity(), (a, b) -> a)).values();
            List<Event> list1 = new ArrayList<>(nonDuplicateCollection);
            list1.sort(Comparator.comparing(Event::getStartTime));
            for (Event event : list1) {
              events.append(" | " + event.getName());
            }
            /* Set new text */
            contentText.append(date);
            contentText.append(" :  ");
            contentText.append(events.toString() + " |");
            contentText.append("\n");
          }
        }
        break;
      }
      default: {
        contentText.setText("ERROR: Unknown View Selector");
        break;
      }
    }
  }

  /**
   * The following switches the view selection modes to the new strategy passed
   * through a parameter, highlights buttons, and updates the the content
   * depending on the strategy.
   * 
   * @param newStrategy ViewStrategy
   *                    <li><code>DayViewStrategy</code></li>
   *                    <li><code>WeekViewStrategy</code></li>
   *                    <li><code>MonthViewStrategy</code></li>
   *                    <li><code>AgendaViewStrategy</code></li>
   */
  private void contentsBtnHandler(ViewStrategy newStrategy) {
    // Updated View Selection Mode
    setViewStrategy(newStrategy);

    // Updates ArrayList<LocalDate> Days To Show
    if (viewStrategy.getStrategy() == 'a') {
      AgendaViewStrategyHandler();
      return;
    } else {
      daysToShow = viewStrategy.updateDaysToShow(currentDate);
    }

    // Highlights view selection button
    switch (viewStrategy.getStrategy()) {
      case 'd':
        highlightViewBtn(dayBtn);
        break;
      case 'w':
        highlightViewBtn(weekBtn);
        break;
      case 'm':
        highlightViewBtn(monthBtn);
        break;
      case 'a':
        highlightViewBtn(agendaBtn);
        break;
      default:
        break;
    }

    // Updates contents
    showSchedule();
  }

  private void AgendaViewStrategyHandler() {
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

        // Make sure start date should be earlier than end Date */
        if (startLocalDate.isAfter(endLocalDate)) {
          JOptionPane.showMessageDialog(null, "start date should be after end date");
        } else {
          // Updates ArrayList<LocalDate> Days To Show
          daysToShow = startLocalDate.datesUntil(endLocalDate.plusDays(1)).collect(Collectors.toList());

          // Highlights view selection button
          highlightViewBtn(agendaBtn);

          // Updates contents
          showSchedule();

          agendaFrame.dispose();
          return;
        }
      } catch (NumberFormatException nfe) {
        JOptionPane.showMessageDialog(null, "Please type numeric value");
      } catch (DateTimeException dte) {
        JOptionPane.showMessageDialog(null, "Please type proper input of date formmat");
      }
    });

    agendaPanel.add(new JLabel());
    agendaPanel.add(new JLabel("Year", SwingConstants.CENTER));
    agendaPanel.add(new JLabel("Month", SwingConstants.CENTER));
    agendaPanel.add(new JLabel("Date", SwingConstants.CENTER));
    agendaPanel.add(new JLabel("start Date", SwingConstants.CENTER));
    agendaPanel.add(startYearField);
    agendaPanel.add(startMonthField);
    agendaPanel.add(startDateField);
    agendaPanel.add(new JLabel("End Date", SwingConstants.CENTER));
    agendaPanel.add(endYearField);
    agendaPanel.add(endMonthField);
    agendaPanel.add(endDateField);
    agendaPanel.add(new JLabel());
    agendaPanel.add(new JLabel());
    agendaPanel.add(new JLabel());
    agendaPanel.add(submitBtn);

    agendaFrame.add(agendaPanel);

    agendaPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
    agendaPanel.setLayout(new GridLayout(4, 4));

    agendaFrame.setTitle("Set Period");
    agendaFrame.setSize(500, 200);
    agendaFrame.setLocation(500, 250);
    agendaFrame.setVisible(true);
  }

  /**
   * creates event
   */
  private void createEventPopup() {
    JFrame createFrame = new JFrame();
    JPanel createPanel = new JPanel();
    JTextField eventName = new JTextField(10);
    JTextField startTime = new JTextField(10);
    JTextField endTime = new JTextField(10);
    JButton saveBtn = new JButton("SAVE");
    JButton cancelBtn = new JButton("CANCEL");

    saveBtn.addActionListener(e -> {
      try {
        String eventNameStr = eventName.getText();
        String startTimeStr = startTime.getText();
        String endTimeStr = endTime.getText();
        int startHour = Integer.parseInt(startTimeStr);
        int endHour = Integer.parseInt(endTimeStr);
        String startTimeInLocalTime = startTimeStr + ":00";
        String endTimeInLocalTime = endTimeStr + ":00";

        // Checks to see if the event name is empty
        if (eventNameStr.isEmpty()) {
          JOptionPane.showMessageDialog(null, "Please check the name of the event\n It should not be empty");
        }
        // Checks if the time inputs are numbers between 0 and 23
        else if (startHour < 0 || startHour > 23 || endHour < 0 || endHour > 23) {
          JOptionPane.showMessageDialog(null, "Please check your start or end time\n They should be between 0 and 23");
        }
        // Checks to see if the start time is greater than the end time
        else if (startHour >= endHour) {
          JOptionPane.showMessageDialog(null, "Start time shoule be greater than end time");
        }
        // Checks for valid user input */
        else {
          if (startHour < 10)
            startTimeInLocalTime = "0" + startTimeInLocalTime;
          if (endHour < 10)
            endTimeInLocalTime = "0" + endTimeInLocalTime;
          Event newEvent = new Event(eventNameStr, currentDate,
              LocalTime.parse(startTimeInLocalTime, DateTimeFormatter.ISO_LOCAL_TIME),
              LocalTime.parse(endTimeInLocalTime, DateTimeFormatter.ISO_LOCAL_TIME));

          // Checks too see if time conflicts with another event
          if (model.isConflicts(newEvent)) {
            JOptionPane.showMessageDialog(null, "Event Time is conflicting! Please try again.");
          }

          // If successful, this creates event
          else {
            model.updateEvent(currentDate, newEvent);
            createFrame.dispose();
          }
        }
      } catch (NumberFormatException nfe) {
        JOptionPane.showMessageDialog(null, "Please check the format");
      }
    });

    cancelBtn.addActionListener(e -> createFrame.dispose());

    createPanel.add(new JLabel("Event\t\t"));
    createPanel.add(eventName);
    createPanel.add(new JLabel("Start Hour (0 ~ 23)"));
    createPanel.add(startTime);
    createPanel.add(new JLabel("End Hour (0 ~ 23)"));
    createPanel.add(endTime);
    createPanel.add(saveBtn);
    createPanel.add(cancelBtn);

    createFrame.add(createPanel);
    createPanel.setLayout(new BoxLayout(createPanel, BoxLayout.PAGE_AXIS));
    createPanel.setBorder(new EmptyBorder(BASE_SPACE, BASE_SPACE, BASE_SPACE, BASE_SPACE));

    createFrame.setTitle("Create New Event");
    createFrame.setSize(250, 250);
    createFrame.setLocation(650, 250);
    createFrame.setVisible(true);

  }

  /**
   * Loads recurring events from file
   */
  private void loadFileHandler() {
    System.out.println("LOAD FILE HANDLER START");
    JFrame loadFrame = new JFrame();
    JPanel loadPanel = new JPanel();
    JLabel label = new JLabel("File Path");
    JTextField fileNameField = new JTextField(10);
    JButton loadBtn = new JButton("LOAD");
    JButton cancelBtn = new JButton("CANCEL");
    loadBtn.setPreferredSize(new Dimension(BTN_WIDTH, BTN_HEIGHT));
    cancelBtn.setPreferredSize(new Dimension(BTN_WIDTH, BTN_HEIGHT));

    loadBtn.addActionListener(e -> {
      try {
        /* Load */
        String fileName = "JackCalendar/data/" + fileNameField.getText();
        model.loadAndUpdateEvents(fileName);
        loadFrame.dispose();
      } catch (FileNotFoundException fnf) {
        JOptionPane.showMessageDialog(null, "The file is not found");
      }
    });

    cancelBtn.addActionListener(e -> loadFrame.dispose());

    loadPanel.add(label);
    loadPanel.add(fileNameField);
    loadPanel.add(loadBtn);
    loadPanel.add(cancelBtn);
    loadPanel.add(new JLabel("(hint: input.txt)"));

    loadFrame.add(loadPanel);

    loadPanel.setBorder(new EmptyBorder(BASE_SPACE, BASE_SPACE, BASE_SPACE, BASE_SPACE));
    loadPanel.setLayout(new FlowLayout());
    loadFrame.setTitle("Load Events From File");
    loadFrame.setSize(500, 200);
    loadFrame.setLocation(500, 250);
    loadFrame.setVisible(true);
  }

  /**
   * Highlights current view selection
   */
  private void highlightViewBtn(JButton btn) {
    dayBtn.setBorder(UIManager.getBorder("Button.border"));
    weekBtn.setBorder(UIManager.getBorder("Button.border"));
    monthBtn.setBorder(UIManager.getBorder("Button.border"));
    agendaBtn.setBorder(UIManager.getBorder("Button.border"));
    btn.setBorder(new LineBorder(Color.pink, 2, true));
  }

  /**
   * Highlights the given date in the monthly calendar
   *
   * @param i
   */
  public void highlightDaysBtn(LocalDate c, int lastHighlight) {
    daysButtons.get(lastHighlight).setBorder(UIManager.getBorder("Button.border"));
    daysButtons.get(c.getDayOfMonth()).setBorder(new LineBorder(Color.pink, 3, true));
  }

  /**
   * 
   * Abbreviation of the month is just the first 3 letters
   * 
   * @param date Local Date
   * @return String of Month Abbreviations
   */
  private String getMonthAbbreviation(LocalDate date) {
    return date.getMonth().toString().substring(0, 3);
  }

  /**
   * The following adds JButton, JLabel Components to the JPanel, monthView and
   * shows month view corresponding to the LocalDate in parameter.
   * 
   * @param c LocalDate
   */
  private void showMonthlyCalendar(LocalDate c) {
    int totalDaysOfMonth = c.getMonth().length(c.isLeapYear());
    int offset = 0;
    String firstDayOfMonth = LocalDate.of(c.getYear(), c.getMonth(), 1).getDayOfWeek().name();
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
    // Adds date buttons
    for (int i = 1; i <= totalDaysOfMonth; i++) {
      JButton dateBtn = new JButton(Integer.toString(i));
      monthlyCalendarPanel.add(dateBtn);

      // Date Button Listener
      dateBtn.addActionListener(e -> {
        /* Show content in day view */
        int date = Integer.parseInt(dateBtn.getText());

        // Updates current date
        currentDate = LocalDate.of(c.getYear(), c.getMonth(), date);
        // currentDateToString(currentDate);

        switch (viewStrategy.getStrategy()) {
          case 'd':
            contentsBtnHandler(new DayViewStrategy());
            break;
          case 'w':
            contentsBtnHandler(new WeekViewStrategy());
            break;
          case 'm':
            contentsBtnHandler(new MonthViewStrategy());
            break;
          case 'a':
            contentsBtnHandler(new AgendaViewStrategy());
            break;
          default:
            break;
        }

        // Highlights the date
        highlightDaysBtn(currentDate, lastHighlighted);
        lastHighlighted = date;
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

  /**
   * Strategy Pattern
   * 
   * @newStrategy ViewStrategy
   */
  private void setViewStrategy(ViewStrategy newStrategy) {
    viewStrategy = newStrategy;
  }

  /**
   * This updates the content when Calendar Model is updated, when events are
   * created or loaded from file
   */
  @Override
  public void stateChanged(ChangeEvent e) {
    switch (viewStrategy.getStrategy()) {
      case 'a':
      case 'd':
        contentsBtnHandler(new DayViewStrategy());
        break;
      case 'w':
        contentsBtnHandler(new WeekViewStrategy());
        break;
      case 'm':
        contentsBtnHandler(new MonthViewStrategy());
        break;
      default:
        break;
    }
  }
}
