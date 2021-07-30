import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * MyCalendar class handles all the backend logic for this calenday. It is
 * responsible for providing the functionality to the user - model.
 *
 * @authors Kunwarpreet, Jooyul, Carissa
 */
public class MyCalendar {
	private final HashMap<LocalDate, ArrayList<Event>> events;
	private final ArrayList<ChangeListener> listeners;
	private GregorianCalendar gregorianCalendar;

	/**
	 * Constructor for the MyCalendar class. It initializes HashMap of events,
	 * ArrayList of ChangeListener, and gregorianCalendar
	 */
	public MyCalendar() {
		this.events = new HashMap<>();
		this.listeners = new ArrayList<>();
		this.gregorianCalendar = new GregorianCalendar();
	}

	/**
	 * This returns the GregorianCalendar from the model.
	 */
	public GregorianCalendar getGregorianCalendar() {
		return gregorianCalendar;
	}

	/**
	 * This returns the event map. Key - LocalDate Value - ArrayList of events
	 * 
	 * @return events HashMap
	 */
	public HashMap<LocalDate, ArrayList<Event>> getEventMap() {
		return events;
	}

	/**
	 * loadAndUpdateEvents is used to read and load all the existing events from the
	 * input file. It reads the file in a particular format (Math
	 * Class;2014;1;2;MWF;17;18;) and generate events from it, storing them into the
	 * appropriate map.
	 *
	 * @param filename file File: file to read events from
	 * @throws FileNotFoundException - if file is not found
	 */
	public void loadAndUpdateEvents(String filename) throws FileNotFoundException {
		print("## Loading '" + filename + "' ##");
		File file = new File(filename);
		Scanner fileScanner = new Scanner(file);
		while (fileScanner.hasNextLine()) {
			String eventInfo = fileScanner.nextLine();
			String[] details = eventInfo.split(";");

			try {
				// Invalid number of arguments if not 7
				if (details.length != 7) {
					print("Can't Load '" + eventInfo + "' - Invalid number of arguments");
					continue;
				}

				String name = details[0];
				int year = Integer.parseInt(details[1]);
				int startMonth = Integer.parseInt(details[2]);
				int endMonth = Integer.parseInt(details[3]);
				String recursOn = details[4];
				String startTime = details[5];
				String endTime = details[6];
				int startHour = Integer.parseInt(startTime);
				int endHour = Integer.parseInt(endTime);

				// Invalid format of input
				if ((year < 0) || (startMonth < 0) || (startMonth > 12) || (endMonth < 0) || (endMonth > 12)
						|| (startHour < 0) || (startHour > 23) || (endHour < 0) || (endHour > 23)) {
					print("Can't Load '" + eventInfo + "' - Invalid format of input ");
					continue;
				}

				LocalDate startDate = LocalDate.of(year, startMonth, 1).with(TemporalAdjusters.firstDayOfMonth());
				LocalDate endDate = LocalDate.of(year, endMonth, 1).with(TemporalAdjusters.lastDayOfMonth());

				if (Integer.parseInt(startTime) < 10)
					startTime = "0" + startTime;
				if (Integer.parseInt(endTime) < 10)
					endTime = "0" + endTime;
				startTime += ":00";
				endTime += ":00";

				List<LocalDate> tempLocalDateList = startDate.datesUntil(endDate.plusDays(1))
						.collect(Collectors.toList());
				ArrayList<String> dofList = new ArrayList<String>();
				for (int i = 0; i < recursOn.length(); i++) {
					dofList.add(getDayOfWeek("" + recursOn.charAt(i)));
				}

				for (LocalDate c : tempLocalDateList) {
					if (dofList.contains(c.getDayOfWeek().toString())) {
						LocalTime start = LocalTime.parse(startTime, DateTimeFormatter.ISO_LOCAL_TIME);
						LocalTime end = LocalTime.parse(endTime, DateTimeFormatter.ISO_LOCAL_TIME);
						Event newEvent = new Event(name, c, start, end);
						if (!isConflicts(newEvent))
							updateEvent(c, newEvent);
					}
				}

				print("Loading '" + name + "' is done!");
			} catch (NumberFormatException nfe) {
				print("Can't Load '" + details[0] + "' - Number Format Exception");
			}
		}
		fileScanner.close();
		for (ChangeListener l : listeners) {
			l.stateChanged(new ChangeEvent(this));
		}
	}

	/**
	 * Checks too see if the new event has time conflicts with the existing events
	 * 
	 * @param New - New event
	 */
	public Boolean isConflicts(Event newEvent) {
		// No event existing that conflicts
		if (events.get(newEvent.getDate()) == null)
			return false;

		// Check to see if there are conflicts
		for (Event e : events.get(newEvent.getDate())) {
			if (TimeInterval.isConflicting(newEvent, e)) {
				print(newEvent.getName() + " not added due to time conflict with " + e.getName() + " - "
						+ newEvent.getDate());
				return true;
			}
		}
		return false;
	}

	/**
	 * Attaches the listener to the list of listener
	 *
	 * @param listener
	 */
	public void attach(ChangeListener listener) {
		listeners.add(listener);
	}

	/**
	 * Adds event to the eventMap and updates the ChangeListener
	 *
	 * @param event
	 */
	public void updateEvent(LocalDate c, Event event) {
		if (events.get(c) != null) {
			events.get(c).add(event);
		} else {
			ArrayList<Event> list = new ArrayList<Event>();
			list.add(event);
			events.put(c, list);
		}

		for (ChangeListener l : listeners) {
			l.stateChanged(new ChangeEvent(this));
		}
	}

	/**
	 * Converts the first letter of day of week to the full name of it
	 * 
	 * @param dayOfWeekChar String - dayOfWeekChar MTWRFA
	 */
	private String getDayOfWeek(String dayOfWeekChar) {
		switch (dayOfWeekChar.toLowerCase()) {
			case "s":
				return "SUNDAY";
			case "m":
				return "MONDAY";
			case "t":
				return "TUESDAY";
			case "w":
				return "WEDNESDAY";
			case "r":
				return "THURSDAY";
			case "f":
				return "FRIDAY";
			case "a":
				return "SATURDAY";
			default: {
				System.out.println("Unknown Day of Week: " + dayOfWeekChar);
				return null;
			}
		}
	}

	/**
	 * 
	 * @param x - object being passed in
	 */
	private void print(Object x) {
		System.out.println(x);
	}
}
