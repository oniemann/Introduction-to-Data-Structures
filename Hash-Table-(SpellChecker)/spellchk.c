// $Id: spellchk.c,v 1.9 2014-05-15 21:07:47-07 - - $

#include <assert.h>
#include <errno.h>
#include <libgen.h>
#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>
#include <unistd.h>
#include <ctype.h>

#include "debug.h"
#include "hashset.h"
#include "yyextern.h"

#define STDIN_NAME       "-"
#define DEFAULT_DICTNAME \
        "/afs/cats.ucsc.edu/courses/cmps012b-wm/usr/dict/words"
#define DEFAULT_DICT_POS 0
#define EXTRA_DICT_POS   1
#define NUMBER_DICTS     2

bool xopt = false;
bool xxopt = false;

void print_error (const char *object, const char *message) {
   fflush (NULL);
   fprintf (stderr, "%s: %s: %s\n", program_name, object, message);
   fflush (NULL);
   exit_status = EXIT_FAILURE;
}

FILE *open_infile (const char *filename) {
   FILE *file = fopen (filename, "r");
   if (file == NULL) print_error (filename, strerror (errno));
   DEBUGF ('m', "filename = \"%s\", file = 0x%p\n", filename, file);
   return file;
}

void spellcheck (const char *filename, hashset *hashset) {
   yylineno = 1;
   DEBUGF ('m', "filename = \"%s\", hashset = 0x%p\n",
                filename, hashset);
   for (;;) {
      int token = yylex ();
      if (token == 0) break;
      DEBUGF ('m', "line %d, yytext = \"%s\"\n", yylineno, yytext)
      if(!has_hashset(hashset, yytext)) {
         char* copy = malloc(sizeof(char)*strlen(yytext));
         copy = strcpy(copy,yytext);
         int i = 0;
         while(copy[i]){
            copy[i] = tolower(copy[i]);
            ++i;
         }
         if(!has_hashset(hashset,copy)){
            printf("%s: %d: %s\n", filename, yylineno, yytext);
         }
      }
   }
}

void load_dictionary (const char *dictionary_name, hashset *hashset) {
   if (dictionary_name == NULL) return;
   DEBUGF ('m', "dictionary_name = \"%s\", hashset = %p\n",
           dictionary_name, hashset);
   char *token;

   //opens up the file to be read
   FILE *file = open_infile (dictionary_name); 
   char buffer[1024];
   //uploads file one word at a time, chopping off all nextlines and
   //replacing them with endlines. The words are then placed in a 
   //hash set.
   for(;;){
      token = fgets(buffer, sizeof(buffer), file);
      if (token == NULL) break;
      token = strchr(buffer, '\n');
      if (token != NULL) *token = '\0';
      token = strdup(buffer);
      assert (token != NULL);
      put_hashset(hashset, token);
   }
}


void scan_options (int argc, char** argv,
                   char **default_dictionary,
                   char **user_dictionary) {
   // Scan the arguments and set flags.
   opterr = false;
   for (;;) {
      int option = getopt (argc, argv, "nxyd:@:");
      if (option == EOF) break;
      switch (option) {
         char optopt_string[16]; // used in default:
         case 'd': *user_dictionary = optarg;
                   break;
         case 'n': *default_dictionary = NULL;
                   break;
         case 'x': if (xopt)
                      xxopt = true;
                   else xopt = true;
                   break;
         case 'y': yy_flex_debug = true;
                   break;
         case '@': set_debug_flags (optarg);
                   if (strpbrk (optarg, "@y")) yy_flex_debug = true;
                   break;
         default : sprintf (optopt_string, "-%c", optopt);
                   print_error (optopt_string, "invalid option");
                   break;
      }
   }
}


int main (int argc, char **argv) {
   program_name = basename (argv[0]);
   char *default_dictionary = DEFAULT_DICTNAME;
   char *user_dictionary = NULL;
   hashset *hashset = new_hashset ();
   yy_flex_debug = false;
   xopt = false;
   xxopt = false;
   scan_options (argc, argv, &default_dictionary, &user_dictionary);

   // Load the dictionaries into the hash table.
   load_dictionary (default_dictionary, hashset);
   load_dictionary (user_dictionary, hashset);

   // Read and do spell checking on each of the files.
   if (!xopt) {
   if (optind >= argc) {
      yyin = stdin;
      spellcheck (STDIN_NAME, hashset);
   }else {
      for (int fileix = optind; fileix < argc; ++fileix) {
         DEBUGF ('m', "argv[%d] = \"%s\"\n", fileix, argv[fileix]);
         char *filename = argv[fileix];
         if (strcmp (filename, STDIN_NAME) == 0) {
            yyin = stdin;
            spellcheck (STDIN_NAME, hashset);
         }else {
            yyin = open_infile (filename);
            if (yyin == NULL) continue;
            spellcheck (filename, hashset);
            fclose (yyin);
         }
      }
   }
   } else  dumpstats(hashset); 
   if (xxopt) dump_hashset(hashset);
   free_hashset(hashset);
   yylex_destroy ();
   return exit_status;
}

