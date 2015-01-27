package engine;

import engine.sorting.Sorting;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Show, main connector of multicinema
 */
public class Show implements Serializable, Comparable<Show>, Sorting, Cloneable {
    private static final long serialVersionUID = 1L;
    private Film film;
    private LocalDateTime date;
    private double regularCost;
    private boolean isDiscount;
    private ArrayList<Reservation> reservations = new ArrayList<>();
    private Hall hall;
    private transient Comparable comparator;

    /**
     * @param film        Film
     * @param time        Time of projection
     * @param regularCost Regular cost of ticket
     * @param isDiscount  is enabled discounting
     * @param hall        hall where it is projected in
     */
    public Show(Film film, LocalDateTime time, double regularCost, boolean isDiscount, Hall hall) {
        this.film = film;
        this.date = time;
        this.regularCost = regularCost;
        this.isDiscount = isDiscount;
        this.hall = (Hall) hall.clone();
    }


    /**
     * Removes all outdated reservations from halls
     */
    public void removeOutDatedReservations() {
        if (isOutDated()) {
            for (Reservation res : reservations) {
                try {
                    if (!res.isPayed())
                        removeReservation(res, res.getPosition());
                } catch (NotChangedException ignore) {
                    //NOP
                }
            }
        }
    }

    /**
     * Checks if the show is outdated
     *
     * @return boolean if the show is outdated
     */
    public boolean isOutDated() {
        LocalDateTime orderLimit = LocalDateTime.now().plusHours(12);
        if (date.isBefore(orderLimit)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Adds a reservation
     *
     * @param reservation reservation
     * @param place       positioning
     * @throws UnExistentPlaceException if place not exists
     * @throws BusyPlaceException       if place is just busy
     */
    public void addReservation(Reservation reservation, Place place) throws UnExistentPlaceException, BusyPlaceException {
        hall.setReservation(reservation, place);
        reservations.add(reservation);
    }

    /**
     * Removes reservation
     *
     * @param reservation reservation
     * @param place       position
     * @throws UnExistentPlaceException if place not exists
     * @throws BusyPlaceException       if place is just busy
     */
    public void removeReservation(Reservation reservation, Place place) throws UnExistentPlaceException, BusyPlaceException {
        reservation.getShow().getHall().removeReservation(place);
        reservations.remove(reservation);
    }

    /**
     * Confirming of reservation
     *
     * @param reservation reservation
     * @param place       position
     * @throws UnExistentPlaceException if place not exists
     * @throws BusyPlaceException       if place is just busy
     */
    public void confirmReservation(Reservation reservation, Place place) throws UnExistentPlaceException, BusyPlaceException {
        reservation.setPayed(true);
        reservation.getShow().getHall().confirmReservation(reservation, place);
    }

    /**
     * Return reservations
     *
     * @return
     */
    public ArrayList<Reservation> getReservations() {
        return reservations;
    }

    /**
     * @return show film
     */
    public Film getFilm() {
        return film;
    }

    /**
     * @return datetime of projection
     */
    public LocalDateTime getDate() {
        return date;
    }

    /**
     * @return regular cost
     */
    public double getRegularCost() {
        return regularCost;
    }

    /**
     * @return discounting if exists otherwise false
     */
    public boolean getDiscount() {
        return isDiscount;
    }

    /**
     * @return returns a hall
     */
    public Hall getHall() {
        return hall;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Show clone = null;
        try {
            clone = (Show) super.clone();
            clone.film = (Film) film.clone();
            clone.hall = (Hall) hall.clone();
            ArrayList<Reservation> clonedList = new ArrayList<>(reservations.size());
            for (Reservation item : reservations) clonedList.add((Reservation) item.clone());
            clone.reservations = clonedList;

        } catch (CloneNotSupportedException ignore) {
            //NOP
        }
        return clone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o.getClass() == this.getClass())) return false;

        Show show = (Show) o;

        if (isDiscount != show.isDiscount) return false;
        if (Double.compare(show.regularCost, regularCost) != 0) return false;
        if (comparator != null ? !comparator.equals(show.comparator) : show.comparator != null) return false;
        if (!date.equals(show.date)) return false;
        if (!film.equals(show.film)) return false;
        if (!hall.equals(show.hall)) return false;
        Collections.sort(reservations);
        Collections.sort(((Show) o).reservations);
        if (!reservations.equals(show.reservations)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = film.hashCode();
        result = 31 * result + date.hashCode();
        long temp = Double.doubleToLongBits(regularCost);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (isDiscount ? 1 : 0);
        result = 31 * result + reservations.hashCode();
        result = 31 * result + hall.hashCode();
        result = 31 * result + (comparator != null ? comparator.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(Show o) {
        return date.compareTo(o.date);
    }

    @Override
    public Comparable getSortElement() {
        return comparator;
    }

    @Override
    public String toString() {
        return getClass().getName() + "{" +
                "film=" + film +
                ", date=" + date +
                ", regularCost=" + regularCost +
                ", isDiscount=" + isDiscount +
                ", reservations=" + reservations +
                ", hall=" + hall +
                ", comparator=" + comparator +
                '}';
    }

    @Override
    public void setSortElement(String sortingby) {
        switch (sortingby) {
            case "title":
                comparator = film.getTitle();
                break;
            case "hall":
                comparator = hall.getNumber();
                break;
            case "date":
                comparator = date;
                break;
            case "postsfree":
                comparator = getHall().getFree();
                break;
            case "postsbusy":
                comparator = getHall().getBusy();
                break;
            default:
                comparator = date;
        }
    }
}
