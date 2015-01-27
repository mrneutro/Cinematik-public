package engine;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Hall of multicinema
 */
public class Hall implements Serializable, Cloneable, Comparable<Hall> {
    private static final long serialVersionUID = 10L;
    private int number;
    private int rows = 0;
    private int cols = 0;
    private int free = 0;
    private int busy = 0;
    private int reserved = 0;
    private int disabled = 0;
    private Position[][] positions;

    /**
     * Will add new reservation to its relative place in hall
     *
     * @param reservation reservation object
     * @param place       place data
     * @throws engine.BusyPlaceException, UnExistentPlaceException
     */
    public void setReservation(Reservation reservation, Place place) throws UnExistentPlaceException, BusyPlaceException {
        if (place.getRow() > rows || place.getCol() > cols) {
            throw new UnExistentPlaceException("Impossibile reservare posto in sala " + place);
        }
        Position position = new Position(reservation.isPayed() ? Position.Status.BUSY : Position.Status.RESERVED, reservation, place);
        positions[place.getRow()][place.getCol()] = position;
        free--;
        if (reservation.isPayed()) {
            busy++;
        } else {
            reserved++;
        }
    }

    /**
     * Confirms a ticket
     *
     * @param reservation reservation to confirm
     * @param place       place
     * @throws UnExistentPlaceException
     * @throws BusyPlaceException
     */
    public void confirmReservation(Reservation reservation, Place place) throws UnExistentPlaceException, BusyPlaceException {
        if (place.getRow() > rows || place.getCol() > cols) {
            throw new UnExistentPlaceException("Impossibile reservare posto in sala " + place);
        }
        Position position = new Position(reservation.isPayed() ? Position.Status.BUSY : Position.Status.RESERVED, reservation, place);
        positions[place.getRow()][place.getCol()] = position;
        reserved--;
        busy++;
    }

    /**
     * Sets disabled place
     *
     * @param place position to disable
     * @throws UnExistentPlaceException in case if this place not exists
     * @throws BusyPlaceException       if it is just reserved
     */
    public void setDisabled(Place place) throws UnExistentPlaceException, BusyPlaceException {
        if (place.getRow() > rows || place.getCol() > cols) {
            throw new UnExistentPlaceException("Impossibile reservare posto in sala " + place);
        }
        if (positions[place.getRow()][place.getCol()] != null) {
            throw new BusyPlaceException("Posto " + place.getRow() + "/" + place.getCol() + " è già occupato!");
        }
        Position position = new Position(Position.Status.DISABLED, null, place);
        positions[place.getRow()][place.getCol()] = position;
        free--;
        disabled++;
    }

    /**
     * Return a state of place position
     *
     * @param place position
     * @return state
     * @throws UnExistentPlaceException
     */
    public Position.Status getState(Place place) throws UnExistentPlaceException {
        if (place.getRow() > rows || place.getCol() > cols) {
            throw new UnExistentPlaceException("Impossibile reservare posto in sala " + place);
        }
        return positions[place.getRow()][place.getCol()] == null ? Position.Status.FREE : positions[place.getRow()][place.getCol()].getStatus();
    }

    /**
     * Sets enabled place
     *
     * @param place position to enable
     * @throws UnExistentPlaceException in case if this place not exists
     * @throws NotChangedException      if it is just enabled
     */
    public void setEnabled(Place place) throws NotChangedException {
        if (place.getRow() > rows || place.getCol() > cols) {
            throw new UnExistentPlaceException("Impossibile reservare posto in sala " + place);
        }
        if (positions[place.getRow()][place.getCol()] == null || positions[place.getRow()][place.getCol()].getStatus() != Position.Status.DISABLED) {
            throw new NotChangedException("Posto " + place.getRow() + "/" + place.getCol() + " è già abilitato!");
        }
        positions[place.getRow()][place.getCol()] = null;
        free++;
        disabled--;
    }

    /**
     * Removes reservation from this hall
     *
     * @param place place in hall to remove
     * @throws engine.UnExistentPlaceException
     */
    public void removeReservation(Place place) throws UnExistentPlaceException {
        if (place.getRow() > rows || place.getCol() > cols) {
            throw new UnExistentPlaceException("Impossibile reservare posto in sala " + place);
        }
        Position toRemove = positions[place.getRow()][place.getCol()];
        if (toRemove == null) return;
        positions[place.getRow()][place.getCol()] = null;

        free++;
        if (toRemove.getStatus() == Position.Status.BUSY) {
            busy--;
        } else if (toRemove.getStatus() == Position.Status.RESERVED) {
            reserved--;
        } else {
            disabled--;
        }
    }

    /**
     * Returns a number of the hall, starts from 0
     *
     * @return current hall number
     */
    public int getNumber() {
        return number;
    }

    /**
     * Creates new hall
     *
     * @param number number of the hall, must start from 0
     * @param rows   count of rows of the hall
     * @param cols   count of coloumns of the hall
     * @throws java.lang.IllegalArgumentException if the size is 0
     */
    public Hall(int number, int rows, int cols) {
        if (rows == 0 || cols == 0) {
            throw new IllegalArgumentException("Dimensione della sala non valida");
        }
        this.number = number;
        this.rows = rows;
        this.cols = cols;
        positions = new Position[rows][cols];
        free = rows * cols;
    }

    @Override
    public Object clone() {
        try {
            Position copy[][] = new Position[rows][cols];
            Hall clone = (Hall) super.clone();
            clone.positions = copy;
            return clone;
        } catch (CloneNotSupportedException ignore) {
            return null;
        }
    }

    @Override
    public String toString() {
        return getClass().getName() + "{" +
                "number=" + number +
                ", rows=" + rows +
                ", cols=" + cols +
                ", free=" + free +
                ", busy=" + busy +
                ", reserved=" + reserved +
                ", positions=" + Arrays.toString(positions) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Hall)) return false;

        Hall hall = (Hall) o;

        if (busy != hall.busy) return false;
        if (cols != hall.cols) return false;
        if (disabled != hall.disabled) return false;
        if (free != hall.free) return false;
        if (number != hall.number) return false;
        if (reserved != hall.reserved) return false;
        if (rows != hall.rows) return false;
        if (!Arrays.deepEquals(positions, ((Hall) o).positions)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = number;
        result = 31 * result + rows;
        result = 31 * result + cols;
        result = 31 * result + free;
        result = 31 * result + busy;
        result = 31 * result + reserved;
        result = 31 * result + disabled;
        result = 31 * result + Arrays.deepHashCode(positions);
        return result;
    }

    /**
     * Count of rows
     *
     * @return count of rows
     */
    public int getRows() {
        return rows;
    }

    /**
     * Count of coloums
     *
     * @return count of cols
     */
    public int getCols() {
        return cols;
    }

    /**
     * Free places in the hall
     *
     * @return free places
     */
    public int getFree() {
        return free;
    }

    /**
     * Busy places in the hall
     *
     * @return busy places
     */
    public int getBusy() {
        return busy;
    }

    /**
     * Array of places and relative reservation
     *
     * @return return positions as two level array
     */
    public Position[][] getPositions() {
        return positions;
    }

    /**
     * Count of reserved places
     *
     * @return reserved places
     */
    public int getReserved() {
        return reserved;
    }

    /**
     * Count of disabled places
     *
     * @return reserved places
     */
    public int getDisabled() {
        return disabled;
    }

    @Override
    public int compareTo(Hall o) {
        return (number > o.getNumber() ? 1 : (number < o.getNumber() ? -1 : 0));
    }
}
