package engine;

import java.io.Serializable;

/**
 * Place in hall
 */
public class Place implements Serializable, Comparable<Place>, Cloneable {
    private static final long serialVersionUID = 4L;
    private int row;
    private int col;

    /**
     * Constructor of place
     *
     * @param row position row
     * @param col position col
     */
    public Place(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * Return a row position
     *
     * @return row position
     */
    public int getRow() {
        return row;
    }

    /**
     * Return a col position
     *
     * @return row position
     */
    public int getCol() {
        return col;
    }

    @Override
    protected Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Place)) return false;

        Place place = (Place) o;

        if (col != place.col) return false;
        if (row != place.row) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = row;
        result = 31 * result + col;
        return result;
    }

    @Override
    public String toString() {
        return getClass().getName() + "{" +
                "row=" + row +
                ", col=" + col +
                '}';
    }

    @Override
    public int compareTo(Place o) {
        if (row * col < o.row * o.col) {
            return -1;
        } else if (row * col == o.row * o.col) {
            return 0;
        } else return 1;
    }
}
