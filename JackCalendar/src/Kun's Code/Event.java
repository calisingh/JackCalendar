import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Event class represents an event. It contains of identifiers like 'name',
 * 'date', 'startTime', 'endTime' etc which can be used to lookup an event. This
 * class consists of constructor, and getters and setters.
 *
 * @authors Kunwarpreet, Jooyul, Carrisa
 */

public class Event {
	private String name;
	private LocalDate date;
	private LocalTime startTime;
	private LocalTime endTime;
	private String recursOn;
	private LocalDate startDate;
	private LocalDate endDate;

	public Event(String name, LocalDate date, LocalTime startTime, LocalTime endTime) {
		this.name = name;
		this.date = date;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public Event(String name, String recursOn, LocalDate date, LocalTime startTime, LocalTime endTime,
			LocalDate startDate, LocalDate endDate) {
		this.name = name;
		this.recursOn = recursOn;
		this.date = date;
		this.startTime = startTime;
		this.endTime = endTime;
		this.startDate = startDate;
		this.endDate = endDate;
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

	public String getRecursOn() {
		return recursOn;
	}

	public void setRecursOn(String recursOn) {
		this.recursOn = recursOn;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	public String print() {
		return "Event{" + "name='" + name + '\'' + ", date=" + date + ", startTime=" + startTime + ", endTime="
				+ endTime + '}';
	}
}
