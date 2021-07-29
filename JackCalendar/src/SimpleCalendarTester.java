/**
 * @authors Kunwarpreet, Jooyul, Carissa
 */
public class SimpleCalendarTester {
	/**
	 * Main for testing the application.
	 * 
	 * @param args
	 */

	public static void main(String[] args) {
		MyCalendar calender = new MyCalendar();
		View view = new View(calender);
		calender.attach(view);
	}
}
