/**  Java example: Simple class that stores just a single int
 *   @author rbk
 *  Ver 1.0: 2017/08/08
 */

package cs6301.g10.utils;
public class Item implements Comparable<Item> {
    private int element;

    public Item(int x) { element = x; }

    public int getItem() { return element; }

    public void setItem(int x) { element = x; }

    public int compareTo(Item another) {
        if (this.element < another.element) { return -1; }
        else if (this.element > another.element) { return 1; }
        else return 0;
    }

    public String toString() { return Integer.toString(element); }
}

/* Sample output:
Binary search for 8: true
*/
