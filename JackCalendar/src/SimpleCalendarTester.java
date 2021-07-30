/**
 * @authors Kunwarpreet, Jooyul, Carissa
 */
public class SimpleCalendarTester {
	/**
	 * Main for testing the application.
	 * 
	 * @param args - Argument passed into the main method to test application
	 */

	public static void main(String[] args) {
		MyCalendar calender = new MyCalendar();
		View view = new View(calender);
		calender.attach(view);
	}
}
