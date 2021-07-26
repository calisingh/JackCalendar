import javafx.util.Pair;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * TimeInterval is used to check if two events are conflicting.
 *
 * @authors Kunwarpreet, Jooyul, Carissa
 */
public class TimeInterval {
	/**
	 * This static method takes two events and returns if they are conflicting
	 *
	 * @param event1 First event
	 * @param event2 Second event
	 * @return boolean isConflicting?
	 */
	public static boolean isConflicting(Event event1, Event event2) {
		if (event1.getDate().equals(event2.getDate())) {
			return (event1.getStartTime().compareTo(event2.getEndTime()) < 0
					&& event1.getStartTime().compareTo(event2.getEndTime()) > 0)
					|| (event1.getEndTime().compareTo(event2.getStartTime()) > 0
							&& event1.getEndTime().compareTo(event2.getEndTime()) < 0);
		}
		return false;
	}

	public static boolean checkForConflict(Event event, HashMap<LocalDate, ArrayList<Event>> eventList) {
		Pair<Event, Boolean> conflicts = new Pair<>(null, false);
		for (Map.Entry<LocalDate, ArrayList<Event>> entry : eventList.entrySet()) {
			for (Event e : entry.getValue()) {
				if (TimeInterval.isConflicting(e, event)) {
					conflicts = new Pair<>(e, true);
				}
			}
		}
		return conflicts.getValue();
	}
}
