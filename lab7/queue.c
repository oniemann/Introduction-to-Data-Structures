// $Id: queue.c,v 1.71 2015-02-24 15:16:55-08 - - $


//The following program was an exercise in creating and freeing queues 
//using malloc. main.c and queue.h is ommitted, as I did not create them. 

#include <assert.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "queue.h"

#define STUBPRINTF(...) fprintf (stderr, __VA_ARGS__);

//struct declaration
//item is the node's value
//link connects it to next node
typedef struct queue_node queue_node;
struct queue_node {
   queue_item_t item;
   queue_node *link;
};

//declares pointers to the beginning and end of queue
struct queue {
   queue_node *front;
   queue_node *rear;
};

//creates a new queue
queue *new_queue (void) {
   queue *this = malloc (sizeof (struct queue));
   this->front = NULL;
   this->rear = NULL;
   assert (this != NULL);

   return this;
}

//frees queue
void free_queue (queue *this) {
   assert (isempty_queue (this));
   free (this);
}


//creates a node and inserts it to the back of the queue
void insert_queue (queue *this, queue_item_t item) {
   queue_node *temp = malloc (sizeof (struct queue_node));
   temp->item = NULL;
   temp->link = NULL;
   assert(temp!=NULL);
   temp->item = item;
   if (this->rear == NULL) this->front = temp;
   else this->rear->link = temp;
   this->rear = temp;
}

//removes the front node of the queue
queue_item_t remove_queue (queue *this) {
   assert (! isempty_queue (this));
   queue_item_t result = this->front->item;
   queue_node *old = this->front;
   this->front = this->front->link;
   free(old);
   return result;
}

//checks whether or not the queue is empty
bool isempty_queue (queue *this) {
   return this->front == NULL;
}

