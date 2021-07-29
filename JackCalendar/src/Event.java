import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Event class represents an event. It contains of identifiers like 'name',
 * 'date', 'startTime', 'endTime' etc which can be used to lookup an event. This
 * class consists of constructor, and getters and setters.
 *
 * @authors Kunwarpreet, Jooyul, Carissa
 */
public class Event {
	private String name;
	private LocalDate date;
	private LocalTime startTime;
	private LocalTime endTime;

	public Event(String name, LocalDate date, LocalTime startTime, LocalTime endTime) {
		this.name = name;
		this.date = date;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}
}
