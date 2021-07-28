import javafx.util.Pair;

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
 * MyCalendar class handles all the backend logic for this solution. It is
 * responsible to providing the functionality to the user. This acts as the
 * model for the application.
 *
 * @authors Kunwarpreet, Jooyul, Carissa
 */
public class MyCalendar {
	private final HashMap<LocalDate, ArrayList<Event>> events;
	private final HashMap<String, HashMap<LocalDate, ArrayList<Event>>> recurringEvents;
	private final ArrayList<ChangeListener> listeners;
	private GregorianCalendar gregorianCalendar;

	/**
	 * Constructor for the MyCalendar class. It initializes the following: HashMap
	 * of events HashMap of recurringEvents
	 *
	 * @param sc scanner
	 */
	public MyCalendar() {
		this.events = new HashMap<>();
		this.recurringEvents = new HashMap<>();
		this.listeners = new ArrayList<>();
		this.gregorianCalendar = new GregorianCalendar();
	}

	/**
	 * returns the GregorianCalendar from the model.
	 *
	 * @param listener
	 */
	public GregorianCalendar getGregorianCalendar() {
		return gregorianCalendar;
	}

	/**
	 * return the event map.
	 *
	 * @return
	 */
	public HashMap<LocalDate, ArrayList<Event>> getEventMap() {
		return events;
	}

	/**
	 * Used to read and load all the existing events from the file. It reads the
	 * file in a particular format and generate events from it and store them in the
	 * appropriate map.
	 *
	 * @param filename file File: file to read reservations from
	 * @throws FileNotFoundException
	 */
	public void loadAndUpdateEvents(String filename) throws FileNotFoundException{
		print("## Loading '" + filename +"' ##");
		File file = new File(filename);
		Scanner fileScanner = new Scanner(file);
		while (fileScanner.hasNextLine()) {
			String[] details = fileScanner.nextLine().split(";");
			String name = details[0];
			int year = Integer.parseInt(details[1]);
			int startMonth = Integer.parseInt(details[2]);
			int endMonth = Integer.parseInt(details[3]);
			String recursOn = details[4];
			LocalDate startDate = LocalDate.of(year, startMonth, 1).with(TemporalAdjusters.firstDayOfMonth());
			LocalDate endDate = LocalDate.of(year, endMonth, 1).with(TemporalAdjusters.lastDayOfMonth());
			String startTime = details[5];
			String endTime = details[6];

			if(Integer.parseInt(startTime) < 10) 	startTime = "0" + startTime;
			if(Integer.parseInt(endTime) < 10) 			endTime = "0" + endTime;
			startTime += ":00";
			endTime += ":00";

			List<LocalDate> tempLocalDateList = startDate.datesUntil(endDate.plusDays(1))
																			.collect(Collectors.toList());
			ArrayList<String> dofList = new ArrayList<String>();
			for(int i = 0; i < recursOn.length(); i++) {
				dofList.add(getDayOfWeek("" + recursOn.charAt(i)));
			}

			for(LocalDate c: tempLocalDateList) {
				if(dofList.contains(c.getDayOfWeek().toString())) {
					LocalTime start = LocalTime.parse(startTime, DateTimeFormatter.ISO_LOCAL_TIME);
					LocalTime end = LocalTime.parse(endTime, DateTimeFormatter.ISO_LOCAL_TIME);
					Event event = new Event(name, c, start, end);
					updateEvent(c, event);
				}
			}

			print("Loading '" + name + "' is done!");
		}
		fileScanner.close();
		for (ChangeListener l : listeners) {
			l.stateChanged(new ChangeEvent(this));
		}
	}

	public Pair<Event, Boolean> saveEvents(String name, LocalDate parsedDate, LocalTime parsedStartTime,
			LocalTime parsedEndTime) {
		Event event = new Event(name, parsedDate, parsedStartTime, parsedEndTime);
		if ((event.getStartTime() == null || event.getEndTime() == null)) {
			print("Event not created!. Time cannot be null");
			return new Pair<>(null, false);
		}
		if (event.getStartTime().compareTo(event.getEndTime()) > 0) {
			print("Event not created!");
			print("Start time has to be before the end time!");
			return new Pair<>(null, false);
		}
		Pair<Event, Boolean> conflicts = new Pair<>(null, false);
		for (Map.Entry<LocalDate, ArrayList<Event>> entry : events.entrySet()) {
			for (Event e : entry.getValue()) {
				if (TimeInterval.isConflicting(e, event)) {
					conflicts = new Pair<>(e, true);
				}
			}
		}
		for (Map.Entry<String, HashMap<LocalDate, ArrayList<Event>>> entry : recurringEvents.entrySet()) {
			for (Map.Entry<LocalDate, ArrayList<Event>> entry2 : entry.getValue().entrySet()) {
				for (Event e : entry2.getValue()) {
					if (TimeInterval.isConflicting(e, event)) {
						conflicts = new Pair<>(e, true);
					}
				}

			}
		}
		if (conflicts.getValue()) {
			print("ERROR");
			System.out.println("\" " + event.getName() + " \"" + " cannot be created. It conflicts with " + "\" "
					+ conflicts.getKey().getName() + " \"");
			return new Pair<>(null, false);
		}
		return new Pair<>(event, true);

	}

	/**
	 * attaches the listener to the list of listener
	 *
	 * @param listener
	 */
	public void attach(ChangeListener listener) {
		listeners.add(listener);
	}

	/**
	 * add event to the eventMap and update the ChangeListener
	 *
	 * @param event
	 */
	public void updateEvent(LocalDate c, Event event) {
		if(events.get(c) != null) {
			events.get(c).add(event);
		}
		else {
			ArrayList<Event> list = new ArrayList<Event>();
			list.add(event);
			events.put(c, list);
		}

		for (ChangeListener l : listeners) {
			l.stateChanged(new ChangeEvent(this));
		}
	}

	private String getDayOfWeek(String detail) {
		switch(detail.toLowerCase()) {
			case "s": return "SUNDAY";
			case "m": return "MONDAY";
			case "t": return "TUESDAY";
			case "w": return "WEDNESDAY";
			case "r": return "THURSDAY";
			case "f": return "FRIDAY";
			case "a": return "SATURDAY";
			default:  {
				System.out.println("Unknown Day of Week: " + detail);
				return null;
			}
		}
	}

	private void print(Object x) {
		System.out.println(x);
	}

}
