import java.time.*;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Interface to view strategy as required per requirements and guidelines
 */
public interface ViewStrategy {
  public List<LocalDate> updateDaysToShow(LocalDate currentDate);

  public char getStrategy();
}

/*
 * Day View Strategy
 */
class DayViewStrategy implements ViewStrategy {
  public List<LocalDate> updateDaysToShow(LocalDate currentDate) {

    /*
     * Initializes New ArrayList
     */
    List<LocalDate> daysToShow = new ArrayList<LocalDate>();

    /*
     * Updates ArrayList<LocalDate> Days To Show
     */
    daysToShow.add(currentDate);
    return daysToShow;
  }

  public char getStrategy() {
    return 'd';
  }
}

/*
 * Week View Strategy
 */
class WeekViewStrategy implements ViewStrategy {
  public List<LocalDate> updateDaysToShow(LocalDate currentDate) {
    /*
     * Update ArrayList<LocalDate> Days To Show
     */
    LocalDate startDate = currentDate.with(WeekFields.of(Locale.US).dayOfWeek(), 1L);
    LocalDate lastDate = currentDate.with(WeekFields.of(Locale.US).dayOfWeek(), 7L);

    return startDate.datesUntil(lastDate.plusDays(1)).collect(Collectors.toList());
  }

  public char getStrategy() {
    return 'w';
  }
}

/*
 * Month View Strategy
 */
class MonthViewStrategy implements ViewStrategy {
  public List<LocalDate> updateDaysToShow(LocalDate currentDate) {
    /*
     * Update ArrayList<LocalDate> Days To Show
     */
    int currentYear = currentDate.getYear();
    Month currentMonth = currentDate.getMonth();
    int lastDateOfMonth = currentMonth.length(currentDate.isLeapYear());
    LocalDate startDate = LocalDate.of(currentYear, currentMonth, 1);
    LocalDate lastDate = LocalDate.of(currentYear, currentMonth, lastDateOfMonth);

    return startDate.datesUntil(lastDate.plusDays(1)).collect(Collectors.toList());
  }

  public char getStrategy() {
    return 'm';
  }
}

/*
 * Agenda View Strategy
 */
class AgendaViewStrategy implements ViewStrategy {
  public List<LocalDate> updateDaysToShow(LocalDate currentDate) {
    return null;
  }

  public char getStrategy() {
    return 'a';
  }
}