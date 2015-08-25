// $Id: nsort.c,v 1.1 2015-02-18 12:00:42-08 - - $

//Sorts inputted words into lexicographic order

#define _GNU_SOURCE
#include <assert.h>
#include <libgen.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <stdbool.h>
#include <string.h>

char *program_name = NULL; 
int exit_status = EXIT_SUCCESS;
#define STDIN_NAME "-";

typedef struct node node;
struct node {
   char *item;
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
node *sortword(char *word, node *head) {
   node *prev = NULL;
   node *curr = head;
   //finds the location in the list to insert the number
   while (curr != NULL) {
      if (strcmp(curr->item,word) > 0) break;
      prev = curr;
      curr = curr->link;  
   }
   //puts the number in the proper spot
   node *tmp = malloc (sizeof (struct node));
   assert (tmp != NULL);
   tmp->item = malloc (strlen(word) + 1);
   strcpy(tmp->item,word);
   assert (tmp->item != NULL);
   tmp->link = curr;

   //updates list and clears memory
   if (prev == NULL) head = tmp;
   else prev->link = tmp;
   return head;
}

//prints pointer addresses if in debug mode
void printword(node *head, options *opts,char **argv, char *progname) {
   if (opts->debug) {
      printf ("%s: head= %p\n", argv[0], head);
      for (node *curr = head; curr != NULL; curr = curr->link){
         printf ("%s: %p-> node {\n"
              "    string= %p->\"%s\",\n"
              "    link= %p}\n",
              progname, curr, curr->item, curr->item, curr->link);
      };
   }
   else {
      for (node *curr = head; curr != NULL; curr = curr->link)
         printf("%s\n", curr->item);
   }
}

//clears mallocs
void clearmem(node *head) {
   while (head != NULL) {
      node *old = head;
      head = head->link;
      free (old->item);
      free (old);
   }
}

int main(int argc, char **argv) {
   options opts;
   program_name = basename(argv[0]);
   scan_options(argc, argv, &opts);
   char err_buffer [256];
   char err_buf_fmt [16];
   char buffer [82];
   node *head = NULL;
   sprintf (err_buf_fmt, "%%%lds", sizeof err_buffer - 1);
   int linenr;
   
   for (linenr = 1; ; ++linenr) {
      char *gotline = fgets(buffer, sizeof buffer, stdin);
      if (gotline == NULL) break;
      
      char *nlpos = strchr (buffer, '\n');

      //if the user hits ctrl-D
      if (nlpos != NULL)
         *nlpos = '\0';
      
      //prints message if newline character is not find
      else {        
         fprintf (stderr, "%s: %d: unterminated line: %s\n",
                   program_name, linenr, buffer);
         exit_status = EXIT_FAILURE;
      }
      //places the word into list
      char *makeit = strdup(buffer);
      head = sortword(makeit, head);
      free(makeit);
   }
   printword(head, &opts, argv, program_name);
   clearmem(head);
   return exit_status;
}
