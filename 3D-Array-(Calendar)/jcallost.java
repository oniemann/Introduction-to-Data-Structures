//$Id: jcal.java,v 1.6 2015-01-20 18:20:17-08 okeefe - $
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
        if(args.length > 0)
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
            calmonth = 1;
            cal.set (calyear, calmonth, 1);
            int[][][] currentyear = make_year(calyear, cal);
            print_year(currentyear, 1, calyear);
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
            print_month(calmonth, currentmonth, calyear);   
        }

        //creates an international calendar
        else if (args.length == 3) {
            //checks to see that the locale option is legitimate
            check_language(args, locale);
            String localemonth = String.format(locale, "%tB", cal);
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
            print_month(calmonth, currentmonth, calyear);   
        }
    }

    static void check_language(String[] args, Locale locale) {
        Locale[] locales = Locale.getAvailableLocales();
        for(Locale options : locales) {
            //if it is, the arguments are set mo/yr/locale for the
            //sake of reusing functions
            String optionsID = options.toLanguageTag();
            if(args[0].equalsIgnoreCase(optionsID)) {
                String temp = args[2];
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
            String optionsID = options.toLanguageTag();
            if(args[0].equalsIgnoreCase(optionsID)) {
                locale = new Locale(args[0]);
                break;
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
    print_month(int month, int[][] currentmonth, int year) 
    {

        int weekno = 0;
        //centers the month and year of the heading
        fit_title(month, year, 1);
        days_of_week(1);
        out.printf("\n");
        //prints the days in rows of seven
        for (int[] days : currentmonth) {
            int day = 0;
            while (day < 7) {
                if(weekno > 0 && currentmonth[weekno][day] == 0) {
                    break;
                }
                else if(currentmonth[weekno][day] == 0){
                    out.printf(day == 0 ? "  " : "   ");
                }
                else {
                    out.printf(day == 0 ? "%2d" : "%3d",
                                              currentmonth[weekno][day]);
                }

                day++;
            }   
            weekno++;
            //if(currentmonth[weekno][day] == 0)
            System.out.printf("\n");
        }
    }
    //prints all the calendar months of a given year using the data 
    //structure created by make_year
    static void 
    print_year(int[][][] currentYear, int month, int year)
    {
        int thismonth = month;
        System.out.println(month);
        int i = 0;
 
        int bSpace = (66 - String.valueOf(year).length()) / 2;
        for(int n = 0; n < bSpace; n++)
            out.print(" ");
        if(String.valueOf(year).length() % 2 != 0) {
            bSpace += 1;
        }
        out.printf("%d\n\n", year);
        //for (int n = 0; n < (bSpace); n++)
        //    out.print(" ");
        //out.printf("\n\n");

        for(int j = 0; j < 4; j++) {
            fit_title(month, year, 3);
            out.println("");
            days_of_week(3);
            out.println("");
            i++;        

            //outermost loop goes through 6 weeks
            for(int week = 0; week < WEEKS_IN_MONTH; week++) {
                int k = 0;
                boolean en = false;
                //loops through each month to write the
                //corresponding weeks of three months in a row
                while (k < 3) {
                    int day = 0;
                    //increments the days by one each year;
                    while (day < 7) {
                        //if the last day of the 3rd month is reached
                        if(week > 0 && k == 2 && currentYear[month-1][week][day] == 0) {
                            break;
                        }
                        //if on the last week, check to see if it's the
                        //last day of all three months
                        else if (week > 4) {
                            en = 
                            end_of_row(currentYear, month, day, k, week);
                            if (en == true) {
                                day = 7;
                                k = 3;
                                break;
                            }
                            else {
                                last_week(currentYear, month, week, day);
                            }
                        }
                        //if the day of month is zero, print blank
                        else if (currentYear[month-1][week][day] == 0)
                            out.printf("   ");
                        //if on the last day of the third month, don't
                        //include space after the day
                        //else if (k == 2 && day==6)
                        //    out.printf("%2d",
                        //       currentYear[month-1][week][day]);
                        //just print out the day followed by a space
                        else if (day == 0 || currentYear[month-1][week][day] == 1)
                            out.printf("%2d",
                                currentYear[month-1][week][day]);
                        else
                            out.printf(" %2d",
                               currentYear[month-1][week][day]);
                        day++;
                    }

  
                    if(k != 2 && en == false)
                            out.print("   ");

                    month++;
                    k++;
                } 
                //if the week does not equal 6, then the months are
                //resets by 3 to accomodate coming months
                if(week != WEEKS_IN_MONTH)
                    month -= 3;

                if(en == true)
                    break;
                else
                    out.printf("\n");
            }
            month += 5;
            if(j != 3)
                out.printf("\n");
        }
    }

    static void last_week(int[][][] cY, int month, int week, int day){
        if (cY[month-1][week][day] == 0) {
            int testsum = 0;
            for(int z = 0; z < 3; z++) {
                for(int y = 0; y < 7; y++) {
                    if(cY[month - 1 + z][5][y] == 0)
                        testsum++;
                }
            }
            //out.printf("THE NUMBER IS [%d]", testsum);
            if(testsum == 21)
                out.printf("");   
            else
                out.printf(day == 0 ? "  " : "   ");
        }
        else if (day == 0)
            out.printf("%2d",
                cY[month-1][week][day]);
        else
            out.printf(" %2d",
                cY[month-1][week][day]);
    }

    static boolean 
        end_of_row(int[][][] cY, int month, int day, int k, int week){
        boolean endOfMonth = false;
        //look at current month
            //if the month ahead has a Sunday equal to zero 
            //if the day after the current is equal to zero

        if (k == 0 && day < 6) {
            if(cY[month-1][week][day + 1] == 0) {
                if (cY[month][week][0] == 0){
                    if (cY[month+1][week][0] == 0) {
                        endOfMonth = true;
                        last_week(cY, month, week, day);
                        out.printf("\n");
                    }
                }
            }
        }
        //if on 2nd month and not the last day of week, check both to
        //see if first day of next month has zero value and that the 
        //next day is 0
        else if (k == 1 && day < 6) {
            if(cY[month-1][week][day + 1] == 0) {
                if(cY[month][week][0] == 0) {
                    endOfMonth = true;
                    last_week(cY, month, week, day);
                    out.printf("\n");
                }
            }
        }
        //if on third month, just check to see if the next day is blank
        else if (k == 2 && day < 6) {
            if(cY[month-1][week][day + 1] == 0) {
                endOfMonth = true;
                last_week(cY, month, week, day);
                out.printf("\n");
            }
        }

        return endOfMonth;
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
        int extraSpace = 0;
        int yearlength = String.valueOf(year).length();
        sum = (DAYS_IN_WEEK * 3 - 1);

        //this case centers "month year" on the calendar
        if(iterations == 1) {
            String name = name_of_month(monthname);
            bSpaces = (sum - name.length() - yearlength) / 2;
            int i = 0;
            while (i < bSpaces) {
                out.print(" ");
                i++;
            }
            out.print(name + " " + year);
            int k = 0;

            if(name.length() % 2 == 0)
                extraSpace = -1;
            else
                extraSpace = 0;

            while(k < bSpaces + extraSpace) {
                out.print(" ");
                k++;
            }
            out.println("");
        }

        //for this case, need to print the title of three
        //months with equal buffer space for each
        else if(iterations == 3) {
                int j = 0;

                while(j < iterations) {
                    String name = name_of_month(monthname + j - 1);
                    bSpaces = (sum - name.length() + 1) / 2;
                    int i = 0;
                    int k = 0;
                    if(name.length() % 2 == 0)
                        extraSpace = 0;
                    else
                        extraSpace = -1;

                    while(k < bSpaces + extraSpace) {
                        out.print(" ");
                        k++;
                    }
                    out.printf(name);
                    
                    while(i < bSpaces) {
                        out.print(" ");
                        i++;
                    }
                    if(j!= 2)
                        out.print("   ");
                    j++;
                }
            
        }
    }

    //storage of the names of the days of the week
    static void 
    days_of_week(int iterations) {
        /*
        GregorianCalendar cal = new GregorianCalendar(locale);
        cal.set (MONTH, JANUARY);
        cal.set (DAY_OF_MONTH, 1);
        cal.add (DAY_OF_MONTH, (8 - cal.get (DAY_OF_WEEK)) % 7);
        do {
            String dayname = String.format (locale, "%tA", cal);
            out.printf(locale, "%3s", dayname); 
            cal.add (DAY_OF_MONTH, 1);
      }while (cal.get (DAY_OF_WEEK) != SUNDAY);
*/
        int i = 0;
        while(i < iterations) {
            out.printf("Su Mo Tu We Th Fr Sa");
            if(i != iterations - 1)
                out.printf("   ");
            i++;
        }

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