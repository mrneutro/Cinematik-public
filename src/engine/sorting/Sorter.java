package engine.sorting;

import java.util.ArrayList;

/**
 * Can sort objects which implements Sorting interface
 */
public class Sorter<T extends Sorting> extends ArrayList<T> implements Iterable<T> {
    public static final int ASC = 0;
    public static final int DESC = 1;

    /**
     * Sorts object using direction from param method
     *
     * @param method direction of sorting, Sorter.ASC or Sorter.DESC
     */
    public void sort(int method) {
        if (this.size() < 2)
            return;
        boolean change = true;
        while (change) {
            change = false;
            for (int i = 0; i < this.size() - 1; i++) {
                int criteria = this.get(i).getSortElement().compareTo(this.get(i + 1).getSortElement());
                if (method == ASC ? criteria > 0 : criteria < 0) {
                    T temp = this.get(i);
                    this.set(i, this.get(i + 1));
                    this.set(i + 1, temp);
                    change = true;
                }
            }
        }
    }
}
