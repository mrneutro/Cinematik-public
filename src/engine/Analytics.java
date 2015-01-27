package engine;

import db.ContentProvider;
import engine.custom_managers.ShowManager;
import javafx.util.Pair;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Analytics class
 */
public class Analytics {
    private ContentProvider cp;
    Pair<LocalDate, LocalDate> interval;
    ObjectFilter<Film> filter;
    private double totalProfit = 0;
    private int totalTickets = 0;
    private int totalReservations = 0;
    private int ticketsWithDiscount = 0;
    private ArrayList<Reservation> reservations = new ArrayList<>();

    /**
     * Initialize counters
     *
     * @param cp       Content provider for data
     * @param interval Date interval
     * @param filter   Film filter
     */
    public Analytics(ContentProvider cp, Pair<LocalDate, LocalDate> interval, ObjectFilter<Film> filter) {
        this.cp = cp;
        this.interval = interval;
        this.filter = filter;
        generate();
    }

    /**
     * Return a profit
     *
     * @return profit
     */
    public double getTotalProfit() {
        return (double) Math.round(totalProfit * 100) / 100;
    }

    /**
     * Return total tickets
     *
     * @return tickets count
     */
    public int getTotalTickets() {
        return totalTickets;
    }

    /**
     * Return a reservations count
     *
     * @return reservations count
     */
    public int getTotalReservations() {
        return totalReservations;
    }

    /**
     * Tickets without discount
     *
     * @return count
     */
    public int getTicketsWithDiscount() {
        return ticketsWithDiscount;
    }

    /**
     * Generates reports
     */
    private void generate() {
        extractReservations();
        for (Reservation t : reservations) {
            if (t.isPayed()) { //Buyed
                totalTickets++;
                if (t.getAbstractDiscount() != null) {
                    ticketsWithDiscount++;
                    totalProfit += t.getAbstractDiscount().getPrice(t.getShow().getRegularCost());
                } else {
                    totalProfit += t.getShow().getRegularCost();
                }
            } else {
                totalReservations++;
            }
        }
    }

    @Override
    public String toString() {
        return getClass().getName() + "{" +
                "cp=" + cp +
                ", interval=" + interval +
                ", filter=" + filter +
                ", totalProfit=" + totalProfit +
                ", totalTickets=" + totalTickets +
                ", totalReservations=" + totalReservations +
                ", ticketsWithDiscount=" + ticketsWithDiscount +
                '}';
    }

    /**
     * Extracts all reservations in this period
     */
    private void extractReservations() {
        ShowManager shows = new ShowManager(cp);
        ObjectFilter<Show> showFilter = obj -> true;
        ArrayList<Show> intervalShow = shows.getInInterval(interval.getKey(), interval.getValue(), showFilter);
        intervalShow.stream().filter(t -> filter.canAdd(t.getFilm())).forEach(t -> reservations.addAll(t.getReservations()));
    }

}
