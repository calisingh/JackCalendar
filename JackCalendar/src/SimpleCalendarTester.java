/**
 * @authors Kunwarpreet, Jooyul, Carrisa
 */
public class SimpleCalendarTester {
	/**
	 * Main for testing the application.
	 * 
	 * @param args
	 */
	final static String filename = "Events.txt";

	public static void main(String[] args) {
		MyCalendar calender = new MyCalendar();
		View view = new View(calender);
		calender.attach(view);
	}
}
