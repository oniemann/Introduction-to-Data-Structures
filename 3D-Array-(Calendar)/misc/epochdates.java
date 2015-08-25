// $Id: epochdates.java,v 1.93 2013-03-28 15:49:06-07 - - $
//
// Prints out some dates and times.
// Illustrates the use of a date formatter.
//

import java.text.*;
import java.util.*;
import static java.lang.Math.*;
import static java.lang.System.*;

class epochdates {
   static final GregorianCalendar CHANGE_DATE
               = new GregorianCalendar (1752, Calendar.SEPTEMBER, 14);
   static final double BIG_BANG = -13.798e9; //years
   static final double RED_GIANT = 5e9; //years
   static final double YEAR_SEC = 365.2422 * 24 * 60 * 60;

   static long make_calendar (int year, int month, int day) {
      GregorianCalendar cal = new GregorianCalendar(0,0,0,0,0,0);
      cal.setGregorianChange (CHANGE_DATE.getTime());
      if (year > 0) {
         cal.set (Calendar.ERA, GregorianCalendar.AD);
         cal.set (year, month, day);
      }else if (year < 0) {
         cal.set (Calendar.ERA, GregorianCalendar.BC);
         cal.set (-year, month, day);
      }else {
         throw new IllegalArgumentException ("year == 0");
      }
      return cal.getTimeInMillis();
   }

   static long[] times = {
      Long.MIN_VALUE,
      make_calendar (-1178, Calendar.APRIL,    16),
      make_calendar ( -753, Calendar.APRIL,    21),
      make_calendar (    1, Calendar.JANUARY,   1),
      make_calendar ( 1066, Calendar.OCTOBER,  14),
      Integer.MIN_VALUE * 1000L,
      0L,
      currentTimeMillis(),
      Integer.MAX_VALUE * 1000L,
      make_calendar ( 9999, Calendar.DECEMBER, 31),
      Long.MAX_VALUE,
   };


   public static void main (String[] args) {
      TimeZone gmt = new SimpleTimeZone (0, "GMT");
      Calendar cal = new GregorianCalendar ();
      out.printf ("%,24.0f = %-19s%,16.0f BCE%n", BIG_BANG * YEAR_SEC,
                  "Big Bang", BIG_BANG);
      for (long time : times) {
         cal.setTimeInMillis (time);
         cal.setTimeZone (gmt);
         String date = String.format ("%1$tA, %1$tB %1$te,", cal);
         out.printf ("%,24.0f = %-24s", time / 1e3, date);
         int year = cal.get (Calendar.YEAR);
         out.printf (abs (year) <= 9999 ? "%11d" : "%,11d", year);
         out.printf (" %s", cal.get (Calendar.ERA)
                     == GregorianCalendar.AD ? "CE" : "BCE");
         if (time >= Integer.MIN_VALUE * 1000L &&
             time <= Integer.MAX_VALUE * 1000L) {
            out.printf (" %1$tT %1$TZ", cal);
         }
         out.printf ("%n");
      }
      out.printf ("%,24.0f = %-19s%,16.0f CE%n", RED_GIANT * YEAR_SEC,
                  "Sun is Red Giant", RED_GIANT);
   }

}

//TEST// ./epochdates >epochdates.out 2>&1
//TEST// mkpspdf epochdates.ps epochdates.java* epochdates.out

