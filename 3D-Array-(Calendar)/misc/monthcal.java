// $Id: monthcal.java,v 1.4 2013-03-28 16:52:19-07 - - $
//
// SYNOPSIS
//    monthcal month year
//
// DESCRIPTION
//    Prints out one month of the Gregorian calendar using the
//    Julian changeover date of 1752-09-14 used by the British
//    Empire and her colonies.
//
// NOTES
//    Month is represented inside GregorianCalendar zero-based,
//    meaning their numbers are 0..11, not 1..12 as is needed
//    for output.
//

import java.util.GregorianCalendar;
import static java.lang.System.*;
import static java.util.Calendar.*;

class monthcal {
   static final GregorianCalendar CHANGE_DATE
               = new GregorianCalendar (1752, SEPTEMBER, 14);

   //
   // main()
   // Create a Gregorian calendar with British Empire changeover.
   // Iterate over the month/year from args, printing the days of
   // the week.
   //
   public static void main (String[] args) {
      GregorianCalendar cal = new GregorianCalendar();
      cal.setGregorianChange (CHANGE_DATE.getTime());

      int calmonth;
      int calyear;
      if (args.length > 0) {
         // Use the month specified and fix off-by-one problem.
         calmonth = Integer.parseInt (args[0]) - 1;
         calyear  = Integer.parseInt (args[1]);
      }else {
         // Use the current month.
         calmonth = cal.get (GregorianCalendar.MONTH);
         calyear  = cal.get (GregorianCalendar.YEAR);
      }
      cal.set (calyear, calmonth, 1);

      while (calmonth == cal.get (GregorianCalendar.MONTH)) {
         int calday = cal.get (GregorianCalendar.DAY_OF_MONTH);
         int weekday = cal.get (GregorianCalendar.DAY_OF_WEEK);
         out.printf ("%04d/%02d/%02d is weekday %d.%n",
                     calyear, calmonth + 1, calday, weekday);
         cal.add (GregorianCalendar.DAY_OF_MONTH, 1);
      }
   }

}

//TEST// monthcal >monthcal.out
//TEST// mkpspdf monthcal.ps monthcal.java* monthcal.out

