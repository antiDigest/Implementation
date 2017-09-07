/**
 * Item class for generics programming
 *
 * @author Antriksh, Gunjan, Atif
 * Short Project 1: 2017/08/28
 */

package cs6301.g1025.utils;

public class Item implements Comparable<Item> {
    public int element;

    Item(int x) {
        element = x;
    }

    public int getItem() {
        return element;
    }

    public void setItem(int x) {
        element = x;
    }

    public String toString() {
        return Integer.toString(element);
    }

    public int compareTo(Item another) {
        if (this.element < another.element) {
            return 1;
        } else if (this.element > another.element) {
            return -1;
        } else return 0;
    }
}
