// $Id: hashset.c,v 1.9 2014-05-15 20:01:08-07 - - $

#include <assert.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "debug.h"
#include "hashset.h"
#include "strhash.h"

#define HASH_NEW_SIZE 15

typedef struct hashnode hashnode;
struct hashnode {
   char *word;
   hashnode *link;
};

struct hashset {
   size_t size;
   size_t load;
   hashnode **chains;
};

hashset *new_hashset (void) {
   hashset *this = malloc (sizeof (struct hashset));
   assert (this != NULL);
   this->size = HASH_NEW_SIZE;
   this->load = 0;
   size_t sizeof_chains = this->size * sizeof (hashnode *);
   this->chains = malloc (sizeof_chains);
   assert (this->chains != NULL);
   memset (this->chains, 0, sizeof_chains);
   DEBUGF ('h', "%p -> struct hashset {size = %zd, chains=%p}\n",
                this, this->size, this->chains);
   return this;
}

void free_hashset (hashset *this) {
   for(size_t i = 0; i < this->size; ++i){
      while(this->chains[i] != NULL) {
         hashnode* older = this->chains[i];
         this->chains[i] = this->chains[i]->link;
         free(older);
      }
      free(this->chains[i]);
   }
   free(this->chains);
   free(this);
   DEBUGF ('h', "free (%p)\n", this);
}

hashset *array_double(hashset* this) {
   this->size = this->size * 2 + 1;
   size_t sizeof_chains = this->size * sizeof (hashnode *);
   hashnode **newchain = malloc (sizeof_chains);
   assert (newchain != NULL);
   memset (newchain, 0, sizeof_chains);

   size_t index = -1;
   size_t maxindex = this->size;
   while(index < maxindex) {
      index += 1;
      if (this->chains[index] == NULL)
         continue;
      while (this->chains[index] != NULL) {
         char* word = this->chains[index]->word;
         size_t newindex = strhash ((const char*)word) % this->size;
         newchain[newindex]->word = word;
         this->chains[index] = this->chains[index]->link;
         this->load += 1;
      }
   }
   this->chains = newchain;
   return this;
}

void put_hashset (hashset *this, const char *item) {
   char* word = (char*) item;
   size_t size = strhash (word) % this->size;
   int cmp = 0; 
   if((this->load * 2) > this->size)
      array_double(this);

   // check if chain is empty
   if(this->chains[size] == NULL){
      this->chains[size] = malloc(sizeof (struct hashnode));
      assert (this->chains[size] != NULL);
      this->chains[size]->word = word;
      this->load += 1;
   }else {
      hashnode* prev = NULL;
      //hashnode* search = NULL;
      //search->word = NULL;
      hashnode* search = this->chains[size];
      for (; search->link != NULL; search = search->link){
         // find insertion point
         cmp = strcmp(search->word, item);
         if (cmp >= 0) break;
         prev = search;
      }
      if (cmp != 0) {
         hashnode *temp = malloc (sizeof (struct hashnode));
         //temp->word = NULL;
         //temp->link = NULL;
         assert(temp != NULL);
         temp->word = word;
         temp->link = search;
         if (prev == NULL) this->chains[size] = temp;
         else prev->link = temp;
         this->load += 1;
      } 
   }
   printf("hashset->load: %lu   word: %s\n",this->load, this->chains[size]->word);
}

bool has_hashset (hashset *this, const char *item) {
   char* word = (char*) item;
   size_t size = strhash(word) % this->size;
   hashnode *search = this->chains[size];
   if (search == NULL)
      return false;
   for (; search->link != NULL; search = search->link){
      int cmp = strcmp(search->word, word);
      if (cmp == 0) return true;
   }
   return false;
}

void dumpstats(hashset* this) {
   printf("%zd words in the hash set\n",this->load);
   printf("%zd size of the hash array\n",this->size);
   size_t frequency[this->load];
   memset(frequency,0,this->load*sizeof(size_t));
   for (size_t index = 0; index < this->load; ++index) {
      int counter = 1;
      while (this->chains[index] != NULL) {
         this->chains[index] = this->chains[index]->link;
         counter += 1;
      }
      frequency[counter] = frequency[counter] + 1;
   }
   for (size_t index = 0; index < this->load; ++index) {
      if (frequency[index] != 0) {
         printf("%10lu chains of size %zd\n", frequency[index] / index, index);
      }
   }
}
void dump_hashset(hashset* this) {

}
