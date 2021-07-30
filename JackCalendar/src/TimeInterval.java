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
	 * @param event1 - First event to check
	 * @param event2 - Second event to check for conflict
	 * @return boolean isConflicting - false if it isn't, true otherwise
	 */
	public static boolean isConflicting(Event event1, Event event2) {
		if (!event1.getDate().equals(event2.getDate()))
			return false;

		if ((event1.getStartTime().compareTo(event2.getEndTime()) >= 0)
				|| (event1.getEndTime().compareTo(event2.getStartTime()) <= 0)) {
			return false;
		}
		return true;
	}

	/**
	 * @param event     - Event being passed in to check
	 * @param eventList - The Array list of events being checked against
	 * @return - the value for conflicts
	 */
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
