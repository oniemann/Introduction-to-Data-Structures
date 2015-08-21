// $Id: linkedlist.java,v 1.18 2013-10-03 19:48:04-07 - - $

//
// NAME
//    linkedlist - read in words and print them
//
// DESCRIPTION
//    A simple linked list example.  Reads words from stdin,
//    puts them into a linked list, and prints the list.
//    Insertions are done via three methods:
//       -f - push onto the front of the list.
//       -r - append at the end of the list.
//       -a - insert into the list in ascending lexicographic order.
//    Printing is done via three methods:
//       -l - print forward using a loop.
//       -f - print forward using recursion.
//       -b - print backward using recursion.
//

import java.util.Scanner;
import static java.lang.System.*;

class linkedlist {
   static Scanner stdin = new Scanner (System.in);

   // Inner class holding a string and a pointer.
   static class node {
      String value;
      node link;
   }

   // Print a node.
   static void print_node (node curr) {
      out.printf ("%s-> {\"%s\", %s}%n", curr, curr.value, curr.link);
   }

   // Print forward using a loop.
   static void print_forward_loop (node head) {
      for (node curr = head; curr != null; curr = curr.link) {
         print_node (curr);
      }
   }

   // Print forward using recursion.
   static void print_forward_recur (node head) {
      if (head == null) return;
      print_node (head);
      print_forward_recur (head.link);
   }

   // Print backward using recursion.
   static void print_backward_recur (node head) {
      if (head == null) return;
      print_backward_recur (head.link);
      print_node (head);
   }


   // Loop pushing each node onto the front of the list.
   static node loop_push_front () {
      node head = null;
      while (stdin.hasNext ()) {
         String word = stdin.next ();
         node tmp = new node ();
         tmp.value = word;
         tmp.link = head;
         head = tmp;
      }
      return head;
   }

   // Loop appending each new node onto the end of the list.
   static node loop_append_rear () {
      node head = null;
      node tail = null;
      while (stdin.hasNext ()) {
         String word = stdin.next ();
         node tmp = new node ();
         tmp.value = word;
         tmp.link = null;
         if (tail == null) head = tmp;
                      else tail.link = tmp;
         tail = tmp;
      }
      return head;
   }

   // Loop inserting each new node in lexicographic order.
   static node loop_insert_lexicographic () {
      node head = null;
      while (stdin.hasNext ()) {
         String word = stdin.next ();
         node prev = null;
         node curr = head;
         int cmp = 1;
         // Phase 1 - find insertion point. -- O(n)
         while (curr != null) {
            cmp = curr.value.compareTo (word);
            if (cmp >= 0) break;
            prev = curr;
            curr = curr.link;
         }
         // Phase 2 - insert if not already there. -- O(1)
         if (cmp != 0) {
            node tmp = new node ();
            tmp.value = word;
            tmp.link = curr;
            if (prev == null) head = tmp;
                         else prev.link = tmp;
         }
      }
      return head;
   }


   static String[] usage = {
      "Usage: linkedlist insert-print,",
      "where insert = f  - to insert at front",
      "             = r  - to insert at rear",
      "             = a  - to insert in ascending order",
      "      print  = l  - to print forward using a loop",
      "             = f  - to print forward recursively",
      "             = b  - to print backward recursively",
   };
   static void usage_exit (){
      for (String line: usage) err.printf ("%s%n",  line);
      System.exit (1);
   }

   static String progname () {
      String pathname = getProperty ("java.class.path");
      if (pathname == null || pathname.length () == 0) return ".";
      String[] components = pathname.split ("/");
      for (int index = components.length - 1; index >= 0; --index) {
         if (components[index].length () > 0) return components[index];
      }
      return "/";
   }

   public static void main (String[] args) {
      node head = null;
      if (args.length != 1 || args[0].length () != 2) usage_exit ();
      out.printf ("Running: %s %s%n", progname (), args[0]);
      switch (args[0].charAt(0)) {
         case 'f': head = loop_push_front (); break;
         case 'r': head = loop_append_rear (); break;
         case 'a': head = loop_insert_lexicographic (); break;
         default: usage_exit ();
      }
      switch (args[0].charAt(1)) {
         case 'l': print_forward_loop (head); break;
         case 'f': print_forward_recur (head); break;
         case 'b': print_backward_recur (head); break;
         default: usage_exit ();
      }
   }

}

/*
//TEST// echo This is some test data >/tmp/linkedlist.data
//TEST// echo Not much test data here >>/tmp/linkedlist.data
//TEST// for opt in fl ff fb
//TEST// do
//TEST//    ./linkedlist $opt >/tmp/linkedlist.$opt.out 2>&1 \
//TEST//          </tmp/linkedlist.data
//TEST//    catnv /tmp/linkedlist.data /tmp/linkedlist.$opt.out \
//TEST//          >linkedlist.$opt.lis </dev/null
//TEST// done
//TEST// mkpspdf linkedlist.ps linkedlist.java linkedlist.*.lis
*/

