// $Id: crpn.c,v 1.28 2014-04-08 15:23:19-07 - - $

//Below is a reverse polish notation calculator. This utilizes stacks to
//calculate inputted queries.


#include <assert.h>
#include <libgen.h>
#include <stdio.h>
#include <stdlib.h>

int exit_status = EXIT_SUCCESS;
#define EMPTY (-1)
#define SIZE 16

//declaration of struct which will keep track of inputted numbers
struct stack {
   int top;
   double numbers[SIZE];
};

//input checker
void bad_operator (const char *oper) {
   fflush (NULL);
   fprintf (stderr, "\"%s\": invalid operator\n", oper);
   fflush (NULL);
   exit_status = EXIT_FAILURE;
}


//pushes an inputted number onto the top of stack
void push (struct stack *the_stack, double number) {
   if (the_stack->top >= SIZE - 1)
      printf("%f: stack underflow\n", number);
   else
      the_stack->numbers[++the_stack->top] = number;

   //printf ("the_stack=%p, top=%d, number=%.15g\n",
   //        the_stack, the_stack->top, number);
}


//performs operation on two selected pointers
void do_binop (struct stack *the_stack, char oper) {
   //makes sure that there exists numbers in the stack
   if (the_stack->top < 1)
      printf("'%d': stack underflow", oper);
   else {
      double right = the_stack->numbers[the_stack->top--];
      double left = the_stack->numbers[the_stack->top--];
      switch (oper) {
         case '+': push (the_stack, left + right); break;
         case '-': push (the_stack, left - right); break;
         case '*': push (the_stack, left * right); break;
         case '/': push (the_stack, left / right); break;
      }
   }
   //printf ("the_stack=%p, top=%d, oper='%c'\n",
   //        the_stack, the_stack->top, oper);
}

//prints the stack
void do_print (struct stack *the_stack) {
   if(the_stack->top == EMPTY)
      printf("stack is empty\n");
   else {
      for(int pos = 0; pos <= the_stack->top; ++pos)
         printf("%.15g\n", the_stack->numbers[pos]);
   }
  //printf ("the_stack=%p, top=%d\n", the_stack, the_stack->top);
}

//clears the stack
void do_clear (struct stack *the_stack) {
   the_stack->top = EMPTY;
   
   //printf ("the_stack=%p, top=%d\n", the_stack, the_stack->top);
}

//operates with "do_binop" to simplify presentation of arithmetic
void do_operator (struct stack *the_stack, const char *oper) {
   switch (oper[0]) {
      case '+': do_binop (the_stack, '+'); break;
      case '-': do_binop (the_stack, '-'); break;
      case '*': do_binop (the_stack, '*'); break;
      case '/': do_binop (the_stack, '/'); break;
      case ';': do_print (the_stack);      break;
      case '@': do_clear (the_stack);      break;
      default : bad_operator(oper);        break;
   }
   
   //printf ("the_stack=%p, top=%d, oper=\"%s\"\n",
  //         the_stack, the_stack->top, oper);
}



int main (int argc, char **argv) {
   //ensures user ran the program without any variables
   if (argc != 1) {
      fprintf (stderr, "Usage: %s\n", basename (argv[0]));
      fflush (NULL);
      exit (EXIT_FAILURE);
   }

   //stack declaration
   struct stack the_stack;
   the_stack.top = EMPTY;
   char buffer[1024];

   //program functionality
   for (;;) {
      int scanrc = scanf ("%1023s", buffer);
      if (scanrc == EOF) break;
      assert (scanrc == 1);
      if (buffer[0] == '#') {
         scanrc = scanf ("%1023[^\n]", buffer);
         continue;
      }
      char *endptr;
      double number = strtod (buffer, &endptr);
      //inputs the numbers until reaching the end of the user input
      if (*endptr == '\0') {
         push (&the_stack, number);
      //if no endline is reached, error thrown
      }else if (buffer[1] != '\0') {
         bad_operator (buffer);
      //if input is correct, runs the calculator
      }else {
         do_operator (&the_stack, buffer);
      }
   }
   return exit_status;
}
