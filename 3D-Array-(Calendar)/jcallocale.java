//$Id$
import java.util.GregorianCalendar;
import java.util.Locale;
import static java.lang.System.*;
import static java.util.Calendar.*; 

class jcal {
    public static void main(String[] args) {
        //creates calendar object which will be manipulated
        GregorianCalendar cal = new GregorianCalendar();
        cal.setGregorianChange (CHANGE_DATE.getTime());
        int calmonth = 0, calyear = 0;
        Locale locale = null;
        locale = set_language(args, locale);

        //if only one argument's given, it is interpreted as the year
        //of desired calendars
        if (args.length == 1) {
            //checks if the argument is a number
            try {
                calyear = Integer.parseInt (args[0]);
            }catch (NumberFormatException error) {
                String argument = args[0];
                misclib.die("not a valid year " + (String)argument);
            }
            //checks to see that 0 < year < 9999 and presents an error
            //if out of bounds
            if(calyear < 1 || calyear > 9999){
                misclib.die("year `" + (int)calyear
                           + "' not in range 1..9999");
            }
            //makes the calendar year and then prints it
            cal.set (calyear, calmonth, 1);
            int[][][] currentyear = make_year(calyear, cal);
            print_year(currentyear, 1, calyear, locale);
        }
        //if two arguments are given, the first is taken to be the month
        //and the second is taken to be the year
        else if (args.length == 2) {
            //checks to see if the input is valid
            calmonth = check_input(args, calmonth);
            calyear = Integer.parseInt (args[1]);
            cal.set (calyear, calmonth, 1);
            //creates/prints the calendar month in the desired year
            int[][] currentmonth = make_month(calmonth, calyear, cal);  
            print_month(calmonth, currentmonth, calyear, locale);   
        }

        //creates an international calendar
        else if (args.length == 3) {
            //checks to see that the locale option is legitimate
            check_language(args, locale);
            calmonth = String.format(locale, "%tB", cal);
            calyear = Integer.parseInt(args[1]);

        }

        //presents an error if more than two arguments are given
        else if (args.length > 3) {
            misclib.warn("Usage: [[month] year]");
            misclib.die("       [-locale] [[month] year]");
        }
        //if no arguments are given, the current day and month are used
        else {
            calmonth = cal.get (GregorianCalendar.MONTH);
            calyear = cal.get (GregorianCalendar.YEAR);
            cal.set (calyear, calmonth, 1);
            int[][] currentmonth = make_month(calmonth, calyear, cal);  
            print_month(calmonth, currentmonth, calyear, locale);   
        }
    }

    static void check_language(String[] args, Locale locale) {
        for(Locale options : locales) {
            //if it is, the arguments are set mo/yr/locale for the
            //sake of reusing functions
            if(args[0] == options) {
                temp = args[2];
                args[2] = args[0];
                args[0] = args[1];
                args[1] = temp;
                break;
            }
        }
        if(locale == null) {
            misclib.warn("Usage: [[month] year]");
            misclib.die("       [locale] [[month] year]");
        }
    }
    //determines the calendar language
    static Locale set_language(String[] args, Locale locale) {     
        Locale[] locales = Locale.getAvailableLocales();

        for(Locale options : locales) {
            if(args[0] == options) {
                locale = new Locale(args[0]);
            }
        }
        if(locale == null)
            locale = Locale.getDefault();

        return locale;
    }

    //accepts a month and a year as arguments and returns a data
    //structure representing a month which can easily be printed.
    static int[][]
           make_month(int month, int year, GregorianCalendar cal) {
        int[][] currentMonth = new int[WEEKS_IN_MONTH][DAYS_IN_WEEK];
        int week = 0;

        while (month == cal.get(GregorianCalendar.MONTH)) {
            int dayofweek = 0;
            while (dayofweek < 6) {
                //finds the day part of the date and assigns it to a
                //2d array [week#][dayofweek]
                int calday = cal.get (GregorianCalendar.DAY_OF_MONTH);
                dayofweek = cal.get (GregorianCalendar.DAY_OF_WEEK) - 1;
                currentMonth[week][dayofweek] = calday;
                
                //fastforwards a day into the future
                cal.add (GregorianCalendar.DAY_OF_MONTH, 1);

                if(month != cal.get(GregorianCalendar.MONTH))
                    break;
            }
            //when the dayofweek indice fills, increments the week
            //indice by one
            week++;
        }
        return currentMonth;
    }
    //creates a 3d array which stores the months and their corresponding
    //days
    static int[][][] make_year(int calyear, GregorianCalendar cal){
        int[][][] currentYear =
                       new int[12][WEEKS_IN_MONTH][DAYS_IN_WEEK];
        //utilizes make_month to fill up a 3rd dimension containing
        //12 indices
        for(int month = 0; month < 12; month++){
            currentYear[month] = make_month(month, calyear, cal);
        }

        return currentYear;
    }
    //prints the desired calendar month of a given year
    static void 
    print_month(int month, int[][] currentmonth, int year, Locale local) 
    {

        int weekno = 0;
        //centers the month and year of the heading
        fit_title(month, year, 1);
        days_of_week(1, local);
        out.printf("\n");
        //prints the days in rows of seven
        for (int[] days : currentmonth) {
            int day = 0;
            while (day < 7) {
                if(currentmonth[n][day] == 0){
                    out.printf(day == 0 ? "  " : "   ");
                }
                else if(weekno > 0 && currentmonth[weekno][day] == 0) {
                    out.printf("\n");
                    break;
                }
                else {
                    out.printf(day == 0 ? "%2d" : "%3d",
                                              currentmonth[weekno][day]);
                }

                day++;
            }   
            weekno++;
            if(currentmonth[weekno][day] == 0)
            System.out.printf("\n");
        }
    }
    //prints all the calendar months of a given year using the data 
    //structure created by make_year
    static void 
    print_year(int[][][] currentYear, int month, int year, Locale local)
    {
        int thismonth = 0;
        int i = 0;
 
        int bSpace = (66 - String.valueOf(year).length()) / 2;
        for(int n = 0; n < bSpace; n++)
            out.print(" ");
        if(String.valueOf(year).length() % 2 != 0)
            out.print(" ");
        out.printf("%d\n\n", year);

        for(int j = 0; j < 4; j++) {
            fit_title(month, year, 3);
            thismonth += 3;
            out.println("");
            days_of_week(3, local);
            out.println("");
            i++;        

            //outermost loop goes through 6 weeks
            for(int week = 0; week < WEEKS_IN_MONTH; week++) {
                int k = 0;
                //loops through each month to write the
                //corresponding weeks of three months in a row
                while (k < 3) {
                    int day = 0;
                    //increments the days by one each year;
                    while (day < 7) {
                        if(currentYear[month-1][week][day] == 0)
                            out.printf("   ");
                        else 
                            out.printf("%2d ",
                               currentYear[month-1][week][day]);
                        day++;
                    }
                    if(k != 2)
                        out.print(" ");
                    month++;
                    k++;
                } 
                if(week != WEEKS_IN_MONTH)
                    month -= 3;
                
                out.printf("\n");
            }
            month += 3;
            out.printf("\n");
        }
    }

    //storage of the names of all the months
    static String name_of_month(int monthno) {
        String monthstring;
        switch (monthno) {
            case 0: monthstring = "January";
                    break;
            case 1: monthstring = "February";
                    break;
            case 2: monthstring = "March";
                    break;
            case 3: monthstring = "April";
                    break;
            case 4: monthstring = "May";
                    break;
            case 5: monthstring = "June";
                    break;
            case 6: monthstring = "July";
                    break;
            case 7: monthstring = "August";
                    break;
            case 8: monthstring = "September";
                    break;
            case 9: monthstring = "October";
                    break;
            case 10: monthstring = "November";
                    break;
            default: monthstring = "December";
                    break;
        }       
        return monthstring;
    }

    //centers the title of the various options forcalendar visualization
    static void fit_title(int monthname, int year, int iterations) {
        int sum, bSpaces;
        //out.println("The name of this month is: " + name);
        int yearlength = String.valueOf(year).length();
        sum = (DAYS_IN_WEEK * 3 - 1);

        //this case centers "month year" on the calendar
        if(iterations == 1) {
            String name = name_of_month(monthname);
            bSpaces = (sum - name.length() - yearlength - 1) / 2;
            int i = 0;
            while (i < bSpaces) {
                out.print(" ");
                i++;
            }
            out.print(name + " " + year);
            out.printf("\n");
        }

        //for this case, need to print the title of three
        //months with equal buffer space for each
        else if(iterations == 3) {
                int j = 0;
                int extraSpace = 0;

                while(j < iterations) {
                    String name = name_of_month(monthname + j - 1);
                    bSpaces = (sum - name.length()) / 2;
                    int i = 0;
                    int k = 0;
                    while(i < bSpaces) {
                        out.print(" ");
                        i++;
                    }

                    out.printf(name);
                    if(name.length() % 2 == 0)
                        extraSpace = 3;
                    else
                        extraSpace = 4;

                    while(k < bSpaces + extraSpace) {
                        out.print(" ");
                        k++;
                    }
                    j++;
                }
            
        }
    }

    //storage of the names of the days of the week
    static void 
    days_of_week(int iterations, Locale locale) {
        GregorianCalendar cal = new GregorianCalendar(locale);
        cal.set (MONTH, JANUARY);
        cal.set (DAY_OF_MONTH, 1);
        cal.add (DAY_OF_MONTH, (8 - cal.get (DAY_OF_WEEK)) % 7);
        do {
            String dayname = String.format (locale, "%tA", cal);
            out.printf(locale, "%3s", dayname); 
            cal.add (DAY_OF_MONTH, 1);
      }while (cal.get (DAY_OF_WEEK) != SUNDAY);
/*
        int i = 0;
        while(i < iterations) {
            out.printf("Su Mo Tu We Th Fr Sa");
            if(i != iterations - 1)
                out.printf("  ");
            i++;
        }
*/
    }

    //checks the validity of [[month] year] input
    static int check_input(String[] args, int calmonth) {
        int value = 0;

        //if/else ifs allow inputs of the names of months as valid
        //arguments
        if (args[0].equalsIgnoreCase("January"))
            return 0;
        else if (args[0].equalsIgnoreCase("February"))
            return 1;
        else if (args[0].equalsIgnoreCase("March"))
            return 2;
        else if (args[0].equalsIgnoreCase("April"))
            return 3;
        else if (args[0].equalsIgnoreCase("May"))
            return 4;
        else if (args[0].equalsIgnoreCase("June"))
            return 5;
        else if (args[0].equalsIgnoreCase("July"))
            return 6;
        else if (args[0].equalsIgnoreCase("August"))
            return 7;
        else if (args[0].equalsIgnoreCase("September"))
            return 8;
        else if (args[0].equalsIgnoreCase("October"))
            return 9;
        else if (args[0].equalsIgnoreCase("November"))
            return 10;
        else if (args[0].equalsIgnoreCase("December"))
            return 11;
        else {
            //makes sure the year is a number
            try {
               value = Integer.parseInt (args[1]);
            }catch (NumberFormatException error) {
                String argument = args[1];
                misclib.die("not a valid year " + (String)argument);
            }
            //makes sure the month is a number if it's not a name
            try {
               value = Integer.parseInt (args[0]) - 1;
            }catch (NumberFormatException error) {
                String argument = args[0];
                misclib.die((String)argument +
                " is neither a month number (1..12) nor a name");
            }
            //makes sure the month is between January and December
            calmonth = Integer.parseInt (args[0]) - 1;
            if (calmonth < 0 || calmonth > 11)
                misclib.die((int)calmonth + 1 +
                " is neither a month number (1..12) nor a name");
        }
        return value;
    }


    //sets the changeover from Julius to Gregorian Calendar
    static final GregorianCalendar CHANGE_DATE 
                    = new GregorianCalendar (1752, SEPTEMBER, 14);
    static final int WEEKS_IN_MONTH = 6;
    static final int DAYS_IN_WEEK = 7;
}