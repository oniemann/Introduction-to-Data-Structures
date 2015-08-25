import java.util.*;
import java.lang.*;

class practicemap {
    class node {
        String item;
        node link;
    }
    node head;
    node curr;
    node prev;

    void next() {
        curr = curr.link;
    }
    void append(String item) {
        node tmp = new node();
        tmp.item = item;
        tmp.link = null;
        curr.link = tmp;
        curr = curr.link;
    }
}
class print_list {
    public static void main(String[] args) {
        node list = new node();
        list.head = null;
        for (int ctr = 0; ctr < 10; ++ctr) {
            list.append((String)ctr);
        }
        for (;;) {
            if (list.link == null) break;
            System.out.prinf("%s\n",list.item)
            list.next();
        }
    }
}