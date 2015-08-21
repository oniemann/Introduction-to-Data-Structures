// $Id: integerarg.java,v 1.6 2013-03-28 16:57:36-07 - - $

// One student's README asks:
// Tell me how to use integers as arguments for java programs plz?
// Here is the solution to that.

import static java.lang.System.*;
class integerarg {
   public static void main (String[] args) {
      for (int argi = 0; argi < args.length; ++argi) {
         try {
            int value = Integer.parseInt (args[argi]);
            out.printf ("OK: %s: is the integer %d%n",
                        args[argi], value);
         }catch (NumberFormatException error) {
            out.printf ("NumberFormatException: %s: %s%n", args[argi],
                        error.getMessage());
         }
      }
   }
}

/*
//TEST// integerarg -55 0777 0x23 123foo hello 5563 9876543210 \
//TEST//            >integerarg.out
//TEST// mkpspdf integerarg.ps integerarg.java* integerarg.out
*/

