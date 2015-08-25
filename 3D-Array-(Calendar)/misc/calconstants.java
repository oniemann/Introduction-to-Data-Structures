// $Id: calconstants.java,v 1.3 2013-03-27 21:28:49-07 - - $

//
// Printout Calendar constants.
//

import static java.lang.System.*;
import java.util.Calendar;

class calconstants {
   public static void main (String[] args) {
      out.printf ("DAY_OF_WEEK SUNDAY = %d%n", Calendar.SUNDAY);
      out.printf ("DAY_OF_WEEK MONDAY = %d%n", Calendar.MONDAY);
      out.printf ("DAY_OF_WEEK TUESDAY = %d%n", Calendar.TUESDAY);
      out.printf ("DAY_OF_WEEK WEDNESDAY = %d%n", Calendar.WEDNESDAY);
      out.printf ("DAY_OF_WEEK THURSDAY = %d%n", Calendar.THURSDAY);
      out.printf ("DAY_OF_WEEK FRIDAY = %d%n", Calendar.FRIDAY);
      out.printf ("DAY_OF_WEEK SATURDAY = %d%n", Calendar.SATURDAY);
      out.printf ("MONTH JANUARY = %d%n", Calendar.JANUARY);
      out.printf ("MONTH FEBRUARY = %d%n", Calendar.FEBRUARY);
      out.printf ("MONTH MARCH = %d%n", Calendar.MARCH);
      out.printf ("MONTH APRIL = %d%n", Calendar.APRIL);
      out.printf ("MONTH MAY = %d%n", Calendar.MAY);
      out.printf ("MONTH JUNE = %d%n", Calendar.JUNE);
      out.printf ("MONTH JULY = %d%n", Calendar.JULY);
      out.printf ("MONTH AUGUST = %d%n", Calendar.AUGUST);
      out.printf ("MONTH SEPTEMBER = %d%n", Calendar.SEPTEMBER);
      out.printf ("MONTH OCTOBER = %d%n", Calendar.OCTOBER);
      out.printf ("MONTH NOVEMBER = %d%n", Calendar.NOVEMBER);
      out.printf ("MONTH DECEMBER = %d%n", Calendar.DECEMBER);
      out.printf ("MONTH UNDECIMBER = %d%n", Calendar.UNDECIMBER);
   }
}

//TEST// ./calconstants >calconstants.out 2>&1
//TEST// mkpspdf calconstants.ps calconstants.java* calconstants.out

