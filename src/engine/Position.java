package engine;

import java.io.Serializable;

/**
 * Position in hall, can have a reservation and status
 */
public class Position implements Serializable, Cloneable, Comparable<Position> {
    private static final long serialVersionUID = 5L;

    public static enum Status {BUSY, DISABLED, RESERVED, FREE}

    private Position.Status status;
    private Reservation reservation;
    private Place place;

    /**
     * Initiator
     *
     * @param status      Status of position
     * @param reservation Reservation
     * @param place       Place
     */
    public Position(Status status, Reservation reservation, Place place) {
        this.status = status;
        this.reservation = reservation;
        this.place = place;
    }

    /**
     * Returns a status of place
     *
     * @return status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Returns a reservation
     *
     * @return reservation
     */
    public Reservation getReservation() {
        return reservation;
    }

    /**
     * Returns a place
     *
     * @return place
     */
    public Place getPlace() {
        return place;
    }

    @Override
    public String toString() {
        return getClass().getName() + "{" +
                "status=" + status +
                ", reservation=" + reservation +
                ", place=" + place +
                '}';
    }

    @Override
    protected Object clone() {
        Position clone = null;
        try {
            clone = (Position) super.clone();
            clone.place = (Place) place.clone();
            clone.reservation = (Reservation) reservation.clone();
        } catch (CloneNotSupportedException ignore) {
            //NOP
        }
        return clone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o.getClass() == getClass())) return false;
        Position position = (Position) o;

        if (!place.equals(position.place)) return false;
        if (!reservation.equals(position.reservation)) return false;
        if (status != position.status) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = status.hashCode();
        result = 31 * result + reservation.hashCode();
        result = 31 * result + place.hashCode();
        return result;
    }

    @Override
    public int compareTo(Position o) {
        return place.compareTo(o.place);
    }
}
