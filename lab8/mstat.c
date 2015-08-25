//$Id: mstat.c,v 1.173 2015-03-04 23:10:27-08 - - $
//name: Okeefe Niemann
//username: oniemann


//The following program displays the time at which every file in the cwd has been last accessed.

#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <unistd.h>
#include <string.h>
#include <errno.h>

#define SEC_PER_DAY (24 * 60 * 60) 
#define SEC_180_DAYS (SEC_PER_DAY * 180)

int exit_status = EXIT_SUCCESS;

int main (int argc, char **argv) {
   //create

   struct stat fileInfo;
   for (int i = 0; i < argc; ++i) {
      char *targetfile = argv[i];

      //checks to see if the argument is contained in working directory
      int available;
      available = lstat(targetfile, &fileInfo);
      if (available == -1) {
         fprintf(stderr, "%s: %s: %s\n", argv[0], 
                  argv[i], strerror(errno));
         exit_status = EXIT_FAILURE;
         continue; 
      }

      char buffer [80];
      mode_t mode = fileInfo.st_mode;
      off_t size = fileInfo.st_size;
      time_t time1 = fileInfo.st_mtime;
      struct tm *timeinfo = localtime(&time1);
      char newbuf [80];
      int isSym = readlink(targetfile, newbuf, sizeof(newbuf));

      time_t currenttime = time(NULL);

      if ((long) currenttime - (long) time1 > SEC_180_DAYS)
         strftime(buffer, 80, "%b %e  %Y", timeinfo);
      else
         strftime(buffer, 80, "%b %e %R", timeinfo);

      if (argc == 1) {
            printf("%6o %9d %s .\n", mode, (int) size, buffer); 
            break;
      }
      else if (argc > 1 && i > 0) {
         printf("%6o %9d %s %s", mode, (int) size, 
                                    buffer, targetfile);
         if (isSym != -1) {
            newbuf[isSym] = '\0';
            printf(" -> %s", newbuf);
         }
         printf("\n");
      }
   }
   
   return exit_status;
}
