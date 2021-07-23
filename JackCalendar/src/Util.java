import java.util.ArrayList;

public class Util {

    public static ArrayList<String> getMonths() {
        ArrayList<String> monthList = new ArrayList<>();
        monthList.add("January");
        monthList.add("February");
        monthList.add("March");
        monthList.add("April");
        monthList.add("May");
        monthList.add("June");
        monthList.add("July");
        monthList.add("August");
        monthList.add("September");
        monthList.add("October");
        monthList.add("November");
        monthList.add("December");

        return monthList;
    }

    public static ArrayList<String> getDays() {
        ArrayList<String> dayList = new ArrayList<>();
        dayList.add("Sunday");
        dayList.add("Monday");
        dayList.add("Tuesday");
        dayList.add("Wednesday");
        dayList.add("Thursday");
        dayList.add("Friday");
        dayList.add("Saturday");

        return dayList;
    }
}
