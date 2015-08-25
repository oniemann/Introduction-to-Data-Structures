// $Id: treemap.java,v 1.1 2012-02-07 15:43:17-08 - - $

// Development version of treemap.
// To be deleted and replaced by an actual implementation that
// does *NOT* use java.util.TreeMap.

import static java.lang.System.*;

class treemap {

   class tree {
      String key;
      String value;
      tree left;
      tree right;

      public tree() {
         this (null, null);
      }

      public tree(String key, String value) {
         this.key = key;
         this.value = value;
      }
   }
   tree root = null;

   // binary searches extrapolated from binsrchtree.java example source
   // as well as print function

   public static void print_tree(tree root, int depth) {
      if (root == null) return;
      print_tree(root.left, depth + 1);
      
      if (depth >= 0) {
         err.printf("%3d \"%s\" \"%s\" %s %s%n",depth,
                        root.key,root.value,root.left,root.right);

      }
      print_tree(root.right, depth + 1);
   }

   public void print_tree() {
      print_tree(this.root, 0);
   }

   public static tree binary_search(tree root, String key) {
      key = key.toUpperCase();
      while(root != null) {
         int cmp = root.key.compareTo(key);
         if (cmp == 0) return root;
         if (cmp > 0) root = root.left;
         else root = root.right; 
      }
      return null;
   }
   
   public static tree binary_search_NSE(tree root, String key) {
      key = key.toUpperCase();
      while(root != null) {
         int cmp = root.key.compareTo(key);
         if (cmp > 0) {
            if (root.left != null)
               root = root.left;
            else return root;
         }
         else {
            if (root.right != null)
               root = root.right; 
            else return root;
         }
      }
      return root;
   }
   
   public String get (String key) {
      // FIX ME
      try {
         return binary_search(this.root,key).value;
      }
      catch (NullPointerException e) {
         return null;
      }
   }

   public String put (String key, String value) {
      // FIX ME
      key = key.toUpperCase();
      if (root == null) root = new tree(key,value);
      else {
         tree tmp = binary_search(this.root,key);
         if (tmp != null) {
            String prev = tmp.value;
            tmp.value = value;
            return prev;
         }
         else {
            tree search = binary_search_NSE(this.root,key);
            tree leaf = new tree(key,value);
            if (search.left == null){
               if(leaf.key.compareTo(search.key) > 0) { 
                  tree temp = new tree(search.key,search.value);
                  search.key = leaf.key;
                  search.value = leaf.value;
                  leaf.key = temp.key;
                  leaf.value = temp.value;
                  search.left = leaf;
               }
               else search.left = leaf;
            }
            else search.right = leaf;
         }
      }
      return value;
   }

   public void debug_tree () {
      debug_tree_recur (root, 0);
   }

   private void debug_tree_recur (tree node, int depth) {
      // WRITE ME
   }

}
