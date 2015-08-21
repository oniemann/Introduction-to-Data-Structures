import java.util.GregorianCalendar;
import static java.lang.System.*;
import static java.util.Calendar.*; 

//accepts a month and a year as arguments and returns a data structure
//representing a month which can easily be printed.
class makemonth {
    static int[][] make_month(int month, int year, GregorianCalendar cal) {
        int[][] currentMonth = new int[WEEKS_IN_MONTH][DAYS_IN_WEEK];
        int week = 0;

        while (month == cal.get(GregorianCalendar.MONTH)) {
            int dayofweek = 0;
            while (dayofweek < 6) {
                //finds the day part of the date and assigns it to a 2d array [week#][dayofweek]
                int calday = cal.get (GregorianCalendar.DAY_OF_MONTH);
                dayofweek = cal.get (GregorianCalendar.DAY_OF_WEEK) - 1;
                currentMonth[week][dayofweek] = calday;
                
                //fastforwards a day into the future
                cal.add (GregorianCalendar.DAY_OF_MONTH, 1);
            }
            //when the dayofweek indice fills, increments the week indice by one
            week++;
        }
        return currentMonth;
    }

    static final int WEEKS_IN_MONTH = 6;
    static final int DAYS_IN_WEEK = 7;
}
