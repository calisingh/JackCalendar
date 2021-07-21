import javax.swing.*;
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

  private final MyCalendar model;
  private final JPanel monthView;
  private final ArrayList<JButton> daysButtons = new ArrayList<>();
  private final JTextArea dayView;
  private JLabel monthName = new JLabel();
  private boolean firstRun;
  private int lastHighlight;

  public View(MyCalendar model) {

    firstRun = true;
    this.model = model;
    lastHighlight = this.getCalendar(model).get(Calendar.DAY_OF_MONTH);
    // JTextArea allows to have multiple lines (multiple events)
    dayView = new JTextArea();
    dayView.setPreferredSize(new Dimension(450, 350));
    dayView.setEditable(false);
    monthView = new JPanel();
    monthView.setLayout(new GridLayout(0, 7));
    monthView.setPreferredSize(new Dimension(300, 300));
    createButtons(this.getCalendar(model).get(Calendar.DAY_OF_MONTH), this.getCalendar(model));
    highlight(this.getCalendar(model).get(Calendar.DAY_OF_MONTH) - 1);

    JButton createEvent = new JButton("Create");
    createEvent.setPreferredSize(new Dimension(50, 50));
    createEvent.addActionListener(e -> createEventPopup());
    JPanel container = new JPanel();
    container.setLayout(new BorderLayout());
    monthName.setText(Util.getMonths().get(this.getCalendar(model).get(Calendar.MONTH)) + " "
        + this.getCalendar(model).get(Calendar.YEAR));
    container.add(monthName, BorderLayout.NORTH);
    container.add(
        new JLabel("       S             M             T             W             T              F             S"),
        BorderLayout.CENTER);
    container.add(monthView, BorderLayout.SOUTH);

    JButton previousDayButton = new JButton("<");
    previousDayButton.addActionListener(e -> {
      GregorianCalendar cal = this.getCalendar(model);
      cal.add(Calendar.DAY_OF_MONTH, -1);
      model.updateListeners(cal);
    });
    JButton nextDayButton = new JButton(">");
    nextDayButton.addActionListener(e -> {
      GregorianCalendar cal = this.getCalendar(model);
      cal.add(Calendar.DAY_OF_MONTH, 1);
      model.updateListeners(cal);
    });
    JButton quitButton = new JButton("Quit");
    quitButton.setPreferredSize(new Dimension(60, 30));
    quitButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5, true));
    quitButton.addActionListener(e -> {
      model.quit();
      Runtime.getRuntime().exit(0);
    });

    dateAndDetails(this.getCalendar(model).get(Calendar.DAY_OF_MONTH));

    JFrame frame = new JFrame();
    frame.add(previousDayButton);
    frame.add(nextDayButton);
    frame.add(quitButton);
    frame.add(container);
    frame.add(dayView);
    frame.add(createEvent);
    frame.add(previousDayButton);
    frame.add(nextDayButton);
    frame.add(quitButton);
    frame.setLayout(new FlowLayout());
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);

  }

  public void createButtons(int i, GregorianCalendar gregorianCalendar) {

    for (int k = 1; k <= gregorianCalendar.getActualMaximum(Calendar.DAY_OF_MONTH); k++) {
      JButton button = new JButton("" + k);
      button.setPreferredSize(new Dimension(4, 4));

      button.addActionListener(x -> {
        String date = button.getText();
        GregorianCalendar calendar = new GregorianCalendar(this.getCalendar(model).get(Calendar.YEAR),
            this.getCalendar(model).get(Calendar.MONTH), this.getCalendar(model).get(Calendar.DAY_OF_MONTH));
        highlight(Integer.parseInt(date) - 1);
        model.update();
      });
      daysButtons.add(button);
    }
    for (JButton button : daysButtons) {
      monthView.add(button);
    }
  }

  /**
   * highligh the given date in the calendar.
   *
   * @param i
   */
  public void highlight(int i) {
    if (i != 0) {
      daysButtons.get(i).setBorder(new LineBorder(Color.RED, 2));
    }
    if (!firstRun) {
      daysButtons.get(lastHighlight).setBorder(new JButton().getBorder());
    }
    firstRun = false;
    lastHighlight = i;

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
    if (String.valueOf(o).length() == 1) {
      String newDate = "" + 0 + o;
      o = Integer.parseInt(newDate);
    }
    int dayOfWeek = this.getCalendar(model).get(Calendar.DAY_OF_WEEK);
    String date = ((this.getCalendar(model).get(Calendar.MONTH) + 1) + "/" + o);
    String completeDate = ((this.getCalendar(model).get(Calendar.MONTH) + 1) + "/" + o + "/"
        + this.getCalendar(model).get(Calendar.YEAR));
    StringBuilder events = new StringBuilder();
    LocalDate parsedDate = LocalDate.parse(completeDate, DateTimeFormatter.ofPattern("MM/dd/yyyy"));

    if (model.getEventMap().containsKey(parsedDate)) {
      ArrayList<Event> list = model.getEventMap().get(parsedDate);
      Collection<Event> nonDuplicateCollection = list.stream()
          .collect(Collectors.toMap(Event::getName, Function.identity(), (a, b) -> a)).values();
      List<Event> list1 = new ArrayList<>(nonDuplicateCollection);
      list1.sort(Comparator.comparing(Event::getStartTime));
      for (Event event : list1) {
        events.append(event.getName()).append(" ").append(event.getStartTime()).append(" ").append(event.getEndTime());
        events.append("\n");
      }
    }
    dayView.setText(Util.getDays().get(dayOfWeek - 1) + " " + date);
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

  @Override
  public void stateChanged(ChangeEvent e) {
    monthName.setText(Util.getMonths().get(this.getCalendar(model).get(Calendar.MONTH)) + " "
        + this.getCalendar(model).get(Calendar.YEAR));
    dateAndDetails(this.getCalendar(model).get(Calendar.DAY_OF_MONTH));
    highlight(this.getCalendar(model).get(Calendar.DAY_OF_MONTH) - 1);

  }

}
