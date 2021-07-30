import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Event class represents an event. It contains identifiers and has the
 * constructors for 'name', 'date', 'startTime', 'endTime', all to streamline
 * the process of looking up an event. This class consists of the constructors -
 * getters and setters.
 *
 * @authors Kunwarpreet, Jooyul, Carissa
 */
public class Event {
	private String name;
	private LocalDate date;
	private LocalTime startTime;
	private LocalTime endTime;

	// Constructor for event
	public Event(String name, LocalDate date, LocalTime startTime, LocalTime endTime) {
		this.name = name;
		this.date = date;
		this.startTime = startTime;
		this.endTime = endTime;
	}
	// Getter to get name
	public String getName() {
		return name;
	}
	// Setter to set name
	public void setName(String name) {
		this.name = name;
	}
	// Getter to get date
	public LocalDate getDate() {
		return date;
	}
	// Setter to set date 
	public void setDate(LocalDate date) {
		this.date = date;
	}
	// Getter for getStartTime 
	public LocalTime getStartTime() {
		return startTime;
	}
	// Setter for setStartTime
	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}
	// Getter for getEndTime
	public LocalTime getEndTime() {
		return endTime;
	}
	// Setter for setEndTime
	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}
}
