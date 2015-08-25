// $Id: localeinfo.java,v 1.10 2013-03-28 16:42:51-07 - - $
//
// NAME
//    localeinfo - print all available Java locales
//
// DESCRIPTION
//    Prints out all known locales and some information about them.
//

import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;
import static java.lang.System.*;

class localeinfo {

   static void printlocale (Locale locale) {
      out.printf ("%s => %s [%s]%n", locale, locale.getDisplayName(),
                  locale.getDisplayName (locale));
   }

   static Comparator<Locale> comparator = new Comparator<Locale>() {
      public int compare (Locale obj1, Locale obj2) {
         return obj1.toString().compareTo (obj2.toString());
      }
   };

   public static void main (String[] args) {
      boolean onelevel = args.length == 0;

      out.printf ("The default locale is:%n");
      printlocale (Locale.getDefault());

      out.printf ("The available locales are:%n");
      Locale[] locales = Locale.getAvailableLocales();
      Arrays.sort (locales, comparator);
      for (Locale locale : locales) {
         if (onelevel && locale.toString().indexOf ("_") >= 0) continue;
         printlocale (locale);
      }
   }

}

//TEST// ./localeinfo >localeinfo.out 2>&1
//TEST// mkpspdf localeinfo.ps localeinfo.java* localeinfo.out

