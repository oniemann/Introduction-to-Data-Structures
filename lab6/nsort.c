// $Id: nsort.c,v 1.1 2015-02-18 12:00:42-08 - - $

//Program sorts numbers from least to greatest
#define _GNU_SOURCE
#include <assert.h>
#include <libgen.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <stdbool.h>

char *program_name = NULL; 
int exit_status = EXIT_SUCCESS;
#define STDIN_NAME "-";

typedef struct node node;
struct node {
   double item;
   node *link;
};

typedef struct options {
   bool debug;
} options;

//checks to see if debug parameter was inputted
void scan_options (int argc, char **argv, options *opts) {
   opts->debug = false;
   opterr = false;
   for (;;) {
      int opt = getopt (argc, argv, "d");
      if (opt == EOF) break;
      switch (opt) {
         case 'd':
            opts->debug = true;
            break;
         default:
            exit_status = EXIT_FAILURE;
            fflush (NULL);
            fprintf (stderr, "%s: -%c: invalid option\n",
                             program_name, optopt);
            break;
      }
   }
}

//inserts node into a sorted list
node *sortnum(double number, node *head) {
   node *prev = NULL;
   node *curr = head;
   //finds the location in the list to insert the number
   while (curr != NULL) {
      if (curr->item > number) break;
      prev = curr;
      curr = curr->link;  
   }
   //puts the number in the proper spot
   node *tmp = malloc (sizeof (struct node));
   assert (tmp != NULL);
   tmp->item = number;
   tmp->link = curr;

   //updates list and clears memory
   if (prev == NULL) head = tmp;
   else prev->link = tmp;
   return head;
}

//prints pointer addresses if in debug mode
void printnum(node *head, options *opts) {
   if (opts->debug) {
      printf ("&head= %p\n", &head);
      printf ("head= %p\n", head);
      for (node *curr = head; curr != NULL; curr = curr->link) {
         printf ("%p -> struct node {item= %.15g, link= %p}\n",
               curr, curr->item, curr->link);
      }
      printf ("NULL= %p\n", NULL);
   }
   else {
      for(node *curr = head; curr != NULL; curr = curr->link)
         printf("%24.15g\n", curr->item);
   }
}

//clears mallocs
void clearmem(node *head) {
   while (head != NULL) {
      node *old = head;
      head = head->link;
      free (old);
   }
}

int main(int argc, char **argv) {
   options opts;
   program_name = basename(argv[0]);
   scan_options(argc, argv, &opts);
   char err_buffer [256];
   char err_buf_fmt [16];
   node *head = NULL;
   sprintf (err_buf_fmt, "%%%lds", sizeof err_buffer - 1);

   for (;;) {
      double number;
      int scancount = scanf("%lg", &number);

      //if the user hits ctrl-D
      if (scancount == EOF) break;
      //prints the single number if there's only one number
      if (scancount == 1)
         head = sortnum(number, head);
      else {        
         scancount = scanf (err_buf_fmt, err_buffer);
         assert (scancount == 1);
         fprintf (stderr, "bad number \"%s\"\n", err_buffer);
         exit_status = EXIT_FAILURE;
      }
   }
   printnum(head, &opts);
   clearmem(head);
   return exit_status;
}
