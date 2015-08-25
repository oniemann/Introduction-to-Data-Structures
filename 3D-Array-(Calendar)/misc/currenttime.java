// $Id: currenttime.java,v 1.6 2013-03-28 16:44:17-07 - - $

import static java.lang.System.*;

class currenttime {
   static final int DAY_LENGTH = 24 * 60 * 60 * 1000;

   static int intyear (long now) {
      return Integer.parseInt (String.format ("%tY", now));
   }

   static int intmonth (long now) {
      return Integer.parseInt (String.format ("%tm", now));
   }

   public static void main (String[] args) {
      long now = System.currentTimeMillis();
      long days = now / DAY_LENGTH;
      long time = now % DAY_LENGTH;
      out.printf ("It is now %d = %<1tc.%n", now);
      out.printf ("%tA, %<tB %<te, %<tY, %<tl:%<tM:%<tS.%<tL"
                + " %<Tp %<tZ%n", now);
      out.printf ("It is %d days since January 1, 1970.%n", days);
      out.printf ("It is %.3f seconds since midnight ", time / 1e3);
      out.printf ("at the Royal Observatory Greenwich.%n");
      out.printf ("The offset from GMT for %tZ is %<tz%n", now);
      out.printf ("int year = %d, int month = %d%n",
                  intyear (now), intmonth (now));
   }

}

//TEST// ./currenttime >currenttime.out 2>&1
//TEST// mkpspdf currenttime.ps currenttime.java* currenttime.out

