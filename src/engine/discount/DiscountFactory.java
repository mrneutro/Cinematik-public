package engine.discount;

import engine.Reservation;
import engine.Show;
import engine.accounts.Client;

import java.util.ArrayList;

/**
 * Discount factory
 */
public abstract class DiscountFactory {
    /**
     * Generates a discount policy especcialy for user
     *
     * @param person Client in input
     * @param show
     * @return Returns a discounting policy
     */
    public static AbstractDiscount getDiscount(Client person, Show show) {
        AbstractDiscount sel = new Discount(0, "N/A");

        if (person.getEmploy().getValue() == "Studente") {
            sel = new Discount(2, "Studente");
        }

        int hour = show.getDate().getHour();
        if (hour > 8 && hour < 15) {
            sel = new Discount(3, "Mattina");
        }

        if (person.getEmploy().getValue() == "Pensionato") {
            sel = new Discount(5, "per pensionati");
        }

        if (person.getAge() < 10) {
            sel = new Discount(10, "Baby");
        }

        ArrayList<Reservation> tickets = person.getMyTickets();
        if (tickets != null && tickets.size() > 0 && tickets.size() % 5 == 0) {
            sel = new Discount(50, "Ogni quinto paghi la meta!");
        }

        return sel;
    }

}
