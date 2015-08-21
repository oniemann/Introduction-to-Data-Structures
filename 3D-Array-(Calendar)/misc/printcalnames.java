// $Id: printcalnames.java,v 1.13 2013-03-28 16:41:08-07 - - $
//
// NAME
//    printcalnames - print out names of weekdays and months
//
// SYNOPSIS
//    printcalnames [language [country [variant]]]
//
// DESCRIPTION
//    Prints out the names of the days of the week and months of
//    the year in the given language, country, and variant.  The
//    months are printed out in full and the days both in full 
//    and abbreviated, as needed for the assignment.
//

import java.util.*;
import static java.lang.Math.*;
import static java.lang.System.*;
import static java.util.Calendar.*;

class printcalnames {
   static final int EXIT_FAILURE = 1;

   static String prefix (String string, int length) {
      return length > string.length ()
           ? string
           : string.substring (0, length);
   }

   static void printcalnames (Locale locale, String argstr) {
      String language = locale.getDisplayName();
      String foreign = locale.getDisplayName (locale);
      GregorianCalendar cal = new GregorianCalendar (locale);
      cal.set (MONTH, JANUARY);
      cal.set (DAY_OF_MONTH, 1);
      cal.add (DAY_OF_MONTH, (8 - cal.get (DAY_OF_WEEK)) % 7);

      // Print out the days, in full and abbreviated.
      do {
         String dayname = String.format (locale, "%tA", cal);
         out.printf (locale, "[%s] %s (%s): day %d = %s (%s)%n",
                     argstr, language, foreign, cal.get (DAY_OF_WEEK),
                     dayname, prefix (dayname, 2));
         cal.add (DAY_OF_MONTH, 1);
      }while (cal.get (DAY_OF_WEEK) != SUNDAY);

      // Print out the months.
      do {
         String monthname = String.format (locale, "%tB", cal);
         out.printf (locale, "[%s] %s (%s): month %2tm = %s (%s)%n",
                     argstr, language, foreign, cal, monthname,
                     prefix (monthname, 3));
         cal.add (MONTH, 1);
      }while (cal.get (MONTH) != JANUARY);
   }


   static String join (String[] args) {
      String result = "";
      for (String arg: args) result += "_" + arg;
      return result.substring (result.length() == 0 ? 0 : 1);
   }

   public static void main (String[] args) {
      Locale locale = null;
      switch (args.length) {
         case 0 : locale = Locale.getDefault();
                  break;
         case 1 : locale = new Locale(args[0]);
                  break;
         case 2 : locale = new Locale(args[0], args[1]);
                  break;
         case 3 : locale = new Locale(args[0], args[1], args[2]);
                  break;
         default: err.printf ("Usage: %s %s%n",
                              "printcalnames",
                              "[language [country [variant]]]");
                  System.exit (EXIT_FAILURE);
      }
      printcalnames (locale, join (args));
   }

}

//TEST// cp /dev/null printcalnames.out
//TEST// for lang in '' da en es fr ga it nl no pt sv
//TEST// do
//TEST//    perl -e 'print "#" x 72, "\n"' >>printcalnames.out
//TEST//    printcalnames $lang >>printcalnames.out
//TEST// done
//TEST// mkpspdf printcalnames.ps printcalnames.java* printcalnames.out*

