package engine;

import engine.accounts.Client;
import engine.discount.AbstractDiscount;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Reservation in multicinema
 */
public class Reservation implements Serializable, Comparable<Reservation>, Cloneable {
    private static final long serialVersionUID = 2L;
    private Client reservedBy;
    private Show show;
    private boolean payed;
    private double totalSum;
    private LocalDateTime time;
    private Place position;
    private AbstractDiscount abstractDiscount;

    /**
     * Reservation constructor
     *
     * @param show       Show
     * @param reservedBy reserved by
     * @param payed      if true - tickets if buyed, reserved otherwise
     * @param totalSum   total sum
     * @param discount   discount object, null if no discount
     * @param position   position in hall
     */
    public Reservation(Show show, Client reservedBy, boolean payed, double totalSum, AbstractDiscount discount, Place position) {
        this.show = show;
        this.reservedBy = reservedBy;
        this.payed = payed;
        this.totalSum = totalSum;
        this.position = position;
        this.abstractDiscount = discount;
        time = LocalDateTime.now();
    }

    /**
     * Show time
     *
     * @return
     */
    public LocalDateTime getTime() {
        return time;
    }

    /**
     * Discounting if exists
     *
     * @return
     */
    public AbstractDiscount getAbstractDiscount() {
        return abstractDiscount;
    }

    /**
     * Setts payed status
     *
     * @param payed
     */
    public void setPayed(boolean payed) {
        this.payed = payed;
    }

    /**
     * Returns a show this reservation belongs to
     *
     * @return
     */
    public Show getShow() {
        return show;
    }

    /**
     * Returna a total sum payed for this show
     *
     * @return
     */
    public double getTotalSum() {
        return totalSum;
    }

    /**
     * Is just ticket or reservation
     *
     * @return
     */
    public boolean isPayed() {
        return payed;
    }

    /**
     * Getts a client who reserved this position
     *
     * @return
     */
    public Client getReservedBy() {
        return reservedBy;
    }

    /**
     * Return a hall position
     *
     * @return
     */
    public Place getPosition() {
        return position;
    }

    @Override
    public int compareTo(Reservation o) {
        return time.compareTo(o.time);
    }

    @Override
    public Object clone() {
        Reservation clone = null;
        try {
            clone = (Reservation) super.clone();
            clone.reservedBy = (Client) reservedBy.clone();
            clone.show = (Show) show.clone();
            clone.position = (Place) position.clone();
        } catch (CloneNotSupportedException ignore) {
            //NOP
        }
        return clone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o.getClass() == getClass())) return false;

        Reservation that = (Reservation) o;

        if (payed != that.payed) return false;
        if (Double.compare(that.totalSum, totalSum) != 0) return false;
        if (abstractDiscount != null ? !abstractDiscount.equals(that.abstractDiscount) : that.abstractDiscount != null)
            return false;
        if (!position.equals(that.position)) return false;
        if (!reservedBy.equals(that.reservedBy)) return false;
        if (!show.equals(that.show)) return false;
        if (!time.equals(that.time)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = reservedBy.hashCode();
        result = 31 * result + show.hashCode();
        result = 31 * result + (payed ? 1 : 0);
        temp = Double.doubleToLongBits(totalSum);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + time.hashCode();
        result = 31 * result + position.hashCode();
        result = 31 * result + (abstractDiscount != null ? abstractDiscount.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return getClass().getName() + "{" +
                "reservedBy=" + reservedBy +
                ", show=" + show +
                ", payed=" + payed +
                ", totalSum=" + totalSum +
                ", time=" + time +
                ", position=" + position +
                ", abstractDiscount=" + abstractDiscount +
                '}';
    }
}
